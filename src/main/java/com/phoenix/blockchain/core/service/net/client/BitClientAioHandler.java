package com.phoenix.blockchain.core.service.net.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

import com.phoenix.blockchain.common.dal.DbAccess;
import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.core.enums.MessageTypeEnum;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.service.net.BaseAioHandler;
import com.phoenix.blockchain.core.service.net.model.MessagePacket;
import com.phoenix.blockchain.core.service.net.model.MessagePacketType;
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

    /**
     * 心跳包
     */
    private static MessagePacket heartbeatPacket = new MessagePacket(MessagePacketType.STRING_MESSAGE);

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {

        LogUtils.info(LOGGER, "响应节点信息，{0}", channelContext.getServerNode());

        MessagePacket messagePacket = (MessagePacket) packet;
        byte[] messageBody = messagePacket.getBody();
        MessageTypeEnum messageType = MessageTypeEnum.getByCode(messagePacket.getType());

        if (null == messageBody) {
            return;
        }

        switch (messageType) {
            case ACCOUNT_SYN_RESPONSE:
                ServerResponseVo responseVo = (ServerResponseVo) SerializeUtils.unSerialize(messageBody);
                Account account = (Account) responseVo.getItem();
                if (responseVo.isSuccess()) {
                    LogUtils.info(LOGGER, "账户同步成功. account.address: {0}", account.getAddress());
                } else {
                    LogUtils.info(LOGGER, "账户同步失败. account.address: {0}, failMessage: {1}.", account.getAddress(),
                            responseVo.getMessage());
                }
        }

        return;
    }

    @Override
    public Packet heartbeatPacket() {
        return heartbeatPacket;
    }
}
