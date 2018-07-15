package com.phoenix.blockchain.core.service.net.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import com.phoenix.blockchain.biz.service.blockchain.account.AccountManager;
import com.phoenix.blockchain.biz.service.blockchain.blockchain.BlockChainManager;
import com.phoenix.blockchain.biz.service.blockchain.pow.ProofOfWork;
import com.phoenix.blockchain.biz.service.blockchain.transaction.TransactionManager;
import com.phoenix.blockchain.biz.service.blockchain.transaction.TransactionPool;
import com.phoenix.blockchain.common.enums.MessageTypeEnum;
import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.common.util.SignUtils;
import com.phoenix.blockchain.common.util.WalletUtils;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.model.Block;
import com.phoenix.blockchain.core.model.Transaction;
import com.phoenix.blockchain.core.service.net.BaseAioHandler;
import com.phoenix.blockchain.core.service.net.model.MessagePacket;
import com.phoenix.blockchain.core.service.net.model.ServerResponseVo;

/**
 * Created by chengfeng on 2018/7/11.
 */
@Component
public class BitServerAioHandler extends BaseAioHandler implements ServerAioHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitServerAioHandler.class);

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private TransactionPool transactionPool;

    @Autowired
    private TransactionManager transactionManager;

    @Autowired
    private BlockChainManager blockChainManager;

    @Autowired
    private ProofOfWork proofOfWork;

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {

        LogUtils.info(LOGGER, "请求节点信息，{0}", channelContext.getClientNode());

        MessagePacket messagePacket = (MessagePacket) packet;

        MessageTypeEnum messageType = MessageTypeEnum.getByCode(messagePacket.getType());
        byte[] messageBody = messagePacket.getBody();

        MessagePacket responsePacket =  null;

        if (null == messageBody || null == messageType) {
            return;
        }

        switch (messageType) {

            case ACCOUNT_SYN_REQUEST:
                Account account = (Account) SerializeUtils.unSerialize(messageBody);
                responsePacket = synAccount(account);
                break;

            case ACCOUNT_LIST_SYN_REQUEST:

                responsePacket = fetchAccountList();
                break;

            case TRANSATOIN_SYN_REQUEST:
                Transaction transaction = (Transaction) SerializeUtils.unSerialize(messageBody);
                synTransaction(transaction);
                break;

            case BLOCK_SYN_REQUEST:
                Block block = (Block) SerializeUtils.unSerialize(messageBody);
                responsePacket = synBlock(block);
                break;

            default:
                LogUtils.warn(LOGGER, "未识别的消息类型,暂时忽略. type: {0}.", messagePacket.getType());

        }

        Aio.send(channelContext, responsePacket);
    }

    /**
     * 同步账户并返回响应报文
     *
     * @param account
     * @return
     */
    private MessagePacket synAccount(Account account) {
        LogUtils.info(LOGGER, "开始同步账户信息. account.address: {0}", account.getAddress());

        MessagePacket packet = new MessagePacket();
        packet.setType(MessageTypeEnum.ACCOUNT_SYN_RESPONSE.getCode());

        ServerResponseVo responseVo = new ServerResponseVo();

        // 验证账户
        if (WalletUtils.verifyAddress(account.getAddress())) {
            accountManager.saveBaseAccount(account);
            responseVo = ServerResponseVo.success(account);
            LogUtils.warn(LOGGER, "账户验证成功. account.address: {0}", account.getAddress());

        } else {
            responseVo = ServerResponseVo.fail(account, "账户信息同步失败.");

            LogUtils.warn(LOGGER, "账户信息非法. account.address: {0}", account.getAddress());
        }

        packet.setBody(SerializeUtils.serialize(responseVo));

        return packet;

    }

    /**
     * 获取当前节点账户列表
     *
     * @return
     */
    private MessagePacket fetchAccountList() {

        LogUtils.info(LOGGER, "收到同步账户列表请求.");

        MessagePacket responsePacket =  new MessagePacket();

        List<Account> accounts = accountManager.listAccounts();

        responsePacket.setType(MessageTypeEnum.ACCOUNT_LIST_SYN_RESPONSE.getCode());
        responsePacket.setBody(SerializeUtils.serialize(ServerResponseVo.success(accounts)));

        return responsePacket;
    }

    /**
     * 同步交易
     *
     * @param transaction
     * @return
     */
    private MessagePacket synTransaction(Transaction transaction) {

        LogUtils.info(LOGGER, "收到同步交易请求. txHash: {0}", transaction.getHash());

        MessagePacket packet = new MessagePacket();
        ServerResponseVo responseVo  = null;

        try {

            if (SignUtils.verify(transaction.getPublicKey(), transaction.getSignature(), transaction.toString())) {

                transactionPool.addTransaction(transaction);
                responseVo = ServerResponseVo.success(transaction);

            } else {

                String message = "交易验证失败. sender: " + transaction.getSendAddress() + ", txHash: " + transaction
                        .getHash();
                responseVo = ServerResponseVo.fail(transaction, message);
            }
        } catch (Exception e) {

            String message = "交易同步异常. sender: " + transaction.getSendAddress() + ", txHash: " + transaction
                    .getHash();
            responseVo = ServerResponseVo.fail(transaction, message);
        }

        packet.setType(MessageTypeEnum.TRANSATOIN_SYN_RESPONSE.getCode());
        packet.setBody(SerializeUtils.serialize(responseVo));

        return packet;

    }

    /**
     * 区块确认
     *
     * @param block
     * @return
     * @throws Exception
     */
    private MessagePacket synBlock(Block block) throws Exception {
        LogUtils.info(LOGGER, "收到同步区块请求. txHash: {0}", block.getHeader().getCurHash());

        MessagePacket packet = new MessagePacket();
        ServerResponseVo responseVo  = null;

        if (blockChainManager.validateBlock(block)) {

            // 执行交易并移除完成的交易记录
            transactionManager.execute(block.getBody().getTransactions());

            // 保存区块
            blockChainManager.saveBlock(block);

            // 更新最新区块索引
            blockChainManager.updateLastBlock(block);

            responseVo = ServerResponseVo.success(block);

        } else {

            LogUtils.warn(LOGGER, "区块确认失败. block: {0}.", block);
            responseVo = ServerResponseVo.fail(block, "区块确认失败");
        }

        packet.setType(MessageTypeEnum.BLOCK_SYN_RESPONSE.getCode());
        packet.setBody(SerializeUtils.serialize(responseVo));

        return packet;

    }
}
