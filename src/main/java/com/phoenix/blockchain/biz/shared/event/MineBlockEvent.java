package com.phoenix.blockchain.biz.shared.event;

import org.springframework.context.ApplicationEvent;

import com.phoenix.blockchain.core.model.Block;

/**
 * Created by chengfeng on 2018/7/15.
 *
 * 区块同步事件
 */
public class MineBlockEvent extends ApplicationEvent {
    public MineBlockEvent(Block source) {
        super(source);
    }
}
