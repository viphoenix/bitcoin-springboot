package com.phoenix.blockchain.core.service.net.client;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.core.Aio;

import com.phoenix.blockchain.common.dal.DbAccess;
import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.core.service.net.model.MessagePacket;
import com.phoenix.blockchain.core.service.net.model.Node;
import com.phoenix.blockchain.core.service.net.config.TioConfig;

/**
 * Created by chengfeng on 2018/7/11.
 * 客户端启动类
 */
@Component
public class ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private TioConfig tioConfig;

    @Resource
    private ClientGroupContext clientGroupContext;

    private AioClient aioClient;

    @Autowired
    private DbAccess dbAccess;

    @PostConstruct
    public void start() throws Exception {

        aioClient = new AioClient(clientGroupContext);

        // 客户端启动时清除所有历史数据
        dbAccess.clearNodes();

        List<Node> peerNodes = tioConfig.getNodes();

        // 持久化对等节点
        if (!CollectionUtils.isEmpty(peerNodes)) {
            dbAccess.addNodes(peerNodes);
        }

        // 与对等节点建立连接
        for (Node node : peerNodes) {
            ClientChannelContext channelContext = aioClient.connect(node);
            Aio.send(channelContext, new MessagePacket(SerializeUtils.serialize(MessagePacket.HELLO_MESSAGE)));
            Aio.bindGroup(channelContext, tioConfig.getClientGroupName());
        }

    }

    /**
     * 发送消息到一个group
     * @param messagePacket
     */
    public void sendGroup(MessagePacket messagePacket) {
        Aio.sendToGroup(clientGroupContext, tioConfig.getClientGroupName(), messagePacket);
    }
}
