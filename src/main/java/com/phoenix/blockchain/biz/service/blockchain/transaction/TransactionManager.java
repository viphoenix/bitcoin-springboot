package com.phoenix.blockchain.biz.service.blockchain.transaction;

import java.util.List;

import com.phoenix.blockchain.core.model.Account;
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
     * 创建矿工奖励交易记录
     *
     * @return
     */
    Transaction createRewardTx(Account mineAccount);

    /**
     * 执行交易
     */
    void execute(List<Transaction> transactions) throws Exception;

    /**
     * 清空交易列表
     */
    void clear();
}
