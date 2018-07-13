package com.phoenix.blockchain.core.service.net.client;

import static com.phoenix.blockchain.core.enums.MessageTypeEnum.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

import com.phoenix.blockchain.common.dal.DbAccess;
import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.core.enums.MessageTypeEnum;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.service.account.AccountManager;
import com.phoenix.blockchain.core.service.net.BaseAioHandler;
import com.phoenix.blockchain.core.service.net.model.MessagePacket;
import com.phoenix.blockchain.core.service.net.model.ServerResponseVo;

/**
 * Created by chengfeng on 2018/7/11.
 */
@Component
public class BitClientAioHandler extends BaseAioHandler implements ClientAioHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(BitClientAioHandler.class);

    @Autowired
    private DbAccess dbAccess;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountManager accountManager;

    /**
     * 心跳包
     */
    private static MessagePacket heartbeatPacket = new MessagePacket(MessageTypeEnum.HEART_BEATS.getCode());

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {

        LogUtils.info(LOGGER, "响应节点信息，{0}", channelContext.getServerNode());

        MessagePacket messagePacket = (MessagePacket) packet;
        byte[] messageBody = messagePacket.getBody();
        MessageTypeEnum messageType = MessageTypeEnum.getByCode(messagePacket.getType());

        ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(messageBody);


        if (null == messageBody) {
            return;
        }

        switch (messageType) {
            case ACCOUNT_SYN_RESPONSE: {
                LogUtils.info(LOGGER, "响应报文类型: {0}", ACCOUNT_SYN_RESPONSE);

                Account account = (Account) responseVo.getItem();
                if (responseVo.isSuccess()) {
                    LogUtils.info(LOGGER, "账户同步成功. account.address: {0}", account.getAddress());
                } else {
                    LogUtils.info(LOGGER, "账户同步失败. account.address: {0}, failMessage: {1}.", account.getAddress(),
                            responseVo.getMessage());
                }

                break;
            }

            case ACCOUNT_LIST_SYN_RESPONSE: {
                LogUtils.info(LOGGER, "响应报文类型: {0}", ACCOUNT_LIST_SYN_RESPONSE);

                List<Account> accountList = (List<Account>) responseVo.getItem();
                if (responseVo.isSuccess()) {
                    // 合并账户信息
                    if (CollectionUtils.isEmpty(accountList)) {
                        return;
                    }

                    for (Account account : accountList) {

                        if (null == accountManager.getAccount(account.getAddress())) {
                            accountManager.saveAccount(account);
                            LogUtils.info(LOGGER, "账户列表同步成功. account.address: {0}", account.getAddress());
                        }

                    }
                }

                break;
            }
        }

        return;
    }

    @Override
    public Packet heartbeatPacket() {
        return heartbeatPacket;
    }
}
