package com.phoenix.blockchain.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chengfeng on 2018/7/8.
 *
 * 区块体
 */
public class BlockBody extends BaseDomain {

    private static final long serialVersionUID = 7122403204648369301L;

    /**
     * 交易列表
     */
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
