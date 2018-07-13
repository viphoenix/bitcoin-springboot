package com.phoenix.blockchain.core.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.phoenix.blockchain.common.util.SerializeUtils;
import com.phoenix.blockchain.core.enums.MessageTypeEnum;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.service.event.AccountSyncEvent;
import com.phoenix.blockchain.core.service.net.model.MessagePacket;
import com.phoenix.blockchain.core.service.net.client.ClientService;

/**
 * Created by chengfeng on 2018/7/12.
 *
 * 账户消息监听器
 */
@Component
public class AccountListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountListener.class);

    @Autowired
    private ClientService clientService;

    @EventListener(AccountSyncEvent.class)
    public void syncAccount(AccountSyncEvent event) {

        Account account = (Account) event.getSource();

        MessagePacket packet = new MessagePacket();

        packet.setType(MessageTypeEnum.ACCOUNT_SYN_REQUEST.getCode());
        packet.setBody(SerializeUtils.serialize(account));

        clientService.sendGroup(packet);
    }
}
