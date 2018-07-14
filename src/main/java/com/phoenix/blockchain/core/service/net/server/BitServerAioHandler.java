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

import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.common.util.SignUtils;
import com.phoenix.blockchain.common.util.WalletUtils;
import com.phoenix.blockchain.core.enums.MessageTypeEnum;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.model.Transaction;
import com.phoenix.blockchain.core.service.transaction.TransactionPool;
import com.phoenix.blockchain.core.service.account.AccountManager;
import com.phoenix.blockchain.core.service.net.BaseAioHandler;
import com.phoenix.blockchain.core.service.net.model.MessagePacket;
import com.phoenix.blockchain.core.service.net.model.ServerResponseVo;
import com.phoenix.blockchain.core.service.transaction.TransactionManager;

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
            accountManager.saveAccount(account);
            responseVo.setItem(account);
            responseVo.setSuccess(true);

            LogUtils.warn(LOGGER, "账户验证成功. account.address: {0}", account.getAddress());

        } else {
            responseVo.setSuccess(false);
            responseVo.setItem(account);
            responseVo.setMessage("账户信息同步失败.");

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
        ServerResponseVo responseVo = new ServerResponseVo();

        List<Account> accounts = accountManager.listAccounts();

        responseVo.setItem(accounts);
        responseVo.setSuccess(true);

        responsePacket.setType(MessageTypeEnum.ACCOUNT_LIST_SYN_RESPONSE.getCode());
        responsePacket.setBody(SerializeUtils.serialize(responseVo));

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
        ServerResponseVo responseVo  = new ServerResponseVo();

        try {
            if (SignUtils.verify(transaction.getPublicKey(), transaction.getSignature(), transaction.toString())) {
                transactionPool.addTransaction(transaction);
                responseVo.setSuccess(true);
                responseVo.setItem(transaction);
            } else {
                responseVo.setItem(transaction);
                responseVo.setSuccess(false);
                responseVo.setMessage("交易验证失败. sender: " + transaction.getSendAddress() + ", txHash: " + transaction
                        .getHash());
            }
        } catch (Exception e) {
            responseVo.setItem(transaction);
            responseVo.setSuccess(false);
            responseVo.setMessage("交易同步异常. sender: " + transaction.getSendAddress() + ", txHash: " + transaction
                    .getHash());
        }

        packet.setType(MessageTypeEnum.TRANSATOIN_SYN_RESPONSE.getCode());
        packet.setBody(SerializeUtils.serialize(responseVo));

        return packet;

    }
}
