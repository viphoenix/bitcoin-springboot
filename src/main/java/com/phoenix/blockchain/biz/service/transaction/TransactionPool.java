package com.phoenix.blockchain.biz.service.transaction;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.phoenix.blockchain.core.model.Transaction;

/**
 * Created by chengfeng on 2018/7/8.
 *
 * 交易池
 */
@Component
public class TransactionPool {

    private Map<String, Transaction> transactionMap = Maps.newHashMap();

    /**
     * 添加交易
     *
     * @param transaction
     */
    public void addTransaction(Transaction transaction) {

        if (!transactionMap.containsKey(transaction.getHash())) {
            transactionMap.put(transaction.getHash(), transaction);
        }
    }

    /**
     * 清空交易
     */
    public void clear() {
        transactionMap.clear();
    }

    public Map<String, Transaction> getTransactionMap() {
        return transactionMap;
    }

    public void setTransactionMap(
            Map<String, Transaction> transactionMap) {
        this.transactionMap = transactionMap;
    }

}
