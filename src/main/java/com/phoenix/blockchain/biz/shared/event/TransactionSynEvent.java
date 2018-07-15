package com.phoenix.blockchain.biz.shared.event;

import org.springframework.context.ApplicationEvent;

import com.phoenix.blockchain.core.model.Transaction;

/**
 * Created by chengfeng on 2018/7/14.
 *
 * 交易同步事件
 */
public class TransactionSynEvent extends ApplicationEvent {
    public TransactionSynEvent(Transaction source) {
        super(source);
    }
}
