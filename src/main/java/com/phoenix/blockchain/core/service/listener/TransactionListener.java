package com.phoenix.blockchain.core.service.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.core.enums.MessageTypeEnum;
import com.phoenix.blockchain.core.model.Transaction;
import com.phoenix.blockchain.core.service.net.client.ClientService;
import com.phoenix.blockchain.core.service.event.TransactionSynEvent;
import com.phoenix.blockchain.core.service.net.model.MessagePacket;

/**
 * Created by chengfeng on 2018/7/14.
 */
@Component
public class TransactionListener {

    @Autowired
    private ClientService clientService;

    @EventListener(TransactionSynEvent.class)
    public void syncTransaction(TransactionSynEvent event) {

        Transaction transaction = (Transaction) event.getSource();

        MessagePacket packet = new MessagePacket();
        packet.setType(MessageTypeEnum.TRANSATOIN_SYN_REQUEST.getCode());
        packet.setBody(SerializeUtils.serialize(transaction));

        clientService.sendGroup(packet);

    }
}
