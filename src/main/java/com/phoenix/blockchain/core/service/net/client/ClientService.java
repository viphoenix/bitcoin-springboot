package com.phoenix.blockchain.core.service.net.client;

import static com.phoenix.blockchain.common.enums.MessageTypeEnum.ACCOUNT_LIST_SYN_REQUEST;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.core.Aio;

import com.phoenix.blockchain.biz.service.blockchain.node.NodeManager;
import com.phoenix.blockchain.common.enums.MessageTypeEnum;
import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.core.service.net.config.TioConfig;
import com.phoenix.blockchain.core.service.net.model.MessagePacket;
import com.phoenix.blockchain.core.service.net.model.Node;

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
    private NodeManager nodeManager;

    @PostConstruct
    public void start() throws Exception {

        aioClient = new AioClient(clientGroupContext);

        // 客户端启动时清除所有历史数据
        nodeManager.clearNodes();

        List<Node> peerNodes = tioConfig.getNodes();

        // 持久化对等节点
        if (!CollectionUtils.isEmpty(peerNodes)) {
            nodeManager.addNodes(peerNodes);
        }

        // 与对等节点建立连接
        for (Node node : peerNodes) {
            ClientChannelContext channelContext = aioClient.connect(node);
            Aio.send(channelContext, new MessagePacket(SerializeUtils.serialize(MessagePacket.HELLO_MESSAGE)));
            Aio.bindGroup(channelContext, tioConfig.getClientGroupName());
        }

    }

    /**
     * 应用启动时同步账户列表
     */
    @EventListener(ApplicationReadyEvent.class)
    public void synAccounts() {
        LogUtils.info(LOGGER, "请求报文类型: {0}", ACCOUNT_LIST_SYN_REQUEST);

        MessagePacket packet = new MessagePacket();
        packet.setType(MessageTypeEnum.ACCOUNT_LIST_SYN_REQUEST.getCode());
        packet.setBody(SerializeUtils.serialize("fecth account list."));
        sendGroup(packet);
    }

    /**
     * TODO: 应用启动时同步区块
     */
    @EventListener(ApplicationReadyEvent.class)
    public void synBlock() {

    }

    /**
     * 发送消息到一个group
     * @param messagePacket
     */
    public void sendGroup(MessagePacket messagePacket) {
        Aio.sendToGroup(clientGroupContext, tioConfig.getClientGroupName(), messagePacket);
    }
}
