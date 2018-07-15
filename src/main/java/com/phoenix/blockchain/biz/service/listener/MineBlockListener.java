package com.phoenix.blockchain.biz.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.phoenix.blockchain.biz.service.event.MineBlockEvent;
import com.phoenix.blockchain.common.enums.MessageTypeEnum;
import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.core.model.Block;
import com.phoenix.blockchain.core.service.net.client.ClientService;
import com.phoenix.blockchain.core.service.net.model.MessagePacket;

/**
 * Created by chengfeng on 2018/7/15.
 *
 * 区块事件监听器
 */
@Component
public class MineBlockListener {

    @Autowired
    private ClientService clientService;

    @EventListener(MineBlockEvent.class)
    public void syncBlock(MineBlockEvent event) {

        Block block = (Block) event.getSource();

        MessagePacket packet = new MessagePacket();

        packet.setType(MessageTypeEnum.BLOCK_SYN_REQUEST.getCode());
        packet.setBody(SerializeUtils.serialize(block));

        clientService.sendGroup(packet);
    }
}
