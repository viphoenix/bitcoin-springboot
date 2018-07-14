package com.phoenix.blockchain.biz.service.transaction;

import java.util.List;

import com.phoenix.blockchain.core.model.Transaction;

/**
 * Created by chengfeng on 2018/7/14.
 */
public interface TransactionManager {

    /**
     * 添加交易
     *
     * @param transaction
     */
    void add(Transaction transaction);

    /**
     * 交易验证
     *
     * @param transaction
     * @return
     */
    boolean verify(Transaction transaction, String privateKey);

    /**
     * 交易同步
     *
     * @param transaction
     */
    void sync(Transaction transaction);

    /**
     * 返回交易列表
     *
     * @return
     */
    List<Transaction> listTransaction();

    /**
     * 清空交易列表
     */
    void clear();
}
