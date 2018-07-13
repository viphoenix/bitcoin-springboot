package com.phoenix.blockchain.core.service.event;

import org.springframework.context.ApplicationEvent;

import com.phoenix.blockchain.core.model.Account;

/**
 * Created by chengfeng on 2018/7/12.
 *
 * 账户同步事件
 */
public class AccountSyncEvent extends ApplicationEvent {

    public AccountSyncEvent(Account account) {
        super(account);
    }
}
