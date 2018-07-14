package com.phoenix.blockchain.core.service.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.phoenix.blockchain.common.util.HashUtils;
import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.common.util.SignUtils;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.model.Transaction;
import com.phoenix.blockchain.core.service.ApplicationContextProvider;
import com.phoenix.blockchain.core.service.account.AccountManager;
import com.phoenix.blockchain.core.service.event.TransactionSynEvent;

/**
 * Created by chengfeng on 2018/7/14.
 */
@Component
public class TransactionManagerImpl implements TransactionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManagerImpl.class);


    @Autowired
    private TransactionPool transactionPool;

    @Autowired
    private AccountManager accountManager;

    @Override
    public void add(Transaction transaction) {
        transactionPool.addTransaction(transaction);
    }

    @Override
    public boolean verify(Transaction transaction, String privateKey) {

        try {
            // 查询账户信息
            Account sender = accountManager.getAccount(transaction.getSendAddress());
            Preconditions.checkNotNull(sender, "付款人账户不存在." + sender.getAddress());
            Account recipient = accountManager.getAccount(transaction.getReceiptAddress());
            Preconditions.checkNotNull(recipient, "付款人账户不存在." + recipient.getAddress());

            //验证账户余额
            if (sender.getBalance().compareTo(transaction.getAmount()) == -1) {
                LogUtils.warn(LOGGER, "账户余额不足. sender: {0}.", transaction.getSendAddress());

                return false;
            }

            transaction.setPublicKey(sender.getPublicKey());
            // 交易hash
            transaction.setHash(HashUtils.sha256Hex(transaction.toString()));

            // 交易签名
            String singature = SignUtils.sign(privateKey, transaction.toString());
            transaction.setSignature(singature);

            // 交易验证是否本人,注意解密数据不能包含签名信息
            if (!SignUtils.verify(sender.getPublicKey(), singature, transaction.toString())) {
                throw new Exception("交易验证失败. sender: " + sender.getAddress());
            }


        } catch (Exception e) {

            LogUtils.warn(LOGGER, e, "交易验证失败. sender: {0}.", transaction.getSendAddress());

            return false;

        }

        return true;
    }

    /**
     * 交易同步
     *
     * @param transaction
     */
    @Override
    public void sync(Transaction transaction) {

        // 向对等节点同步交易
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApplicationContextProvider.publishEvent(new TransactionSynEvent(transaction));

            }
        }).start();

    }

    /**
     * 返回交易列表
     *
     * @return
     */
    @Override
    public List<Transaction> listTransaction() {

        List<Transaction> transactions = new ArrayList<>();

        Map<String, Transaction> transactionMap = transactionPool.getTransactionMap();

        for (Transaction transaction : transactionMap.values()) {
            transactions.add(transaction);
        }
        return transactions;
    }

    @Override
    public void clear() {
        transactionPool.clear();
    }
}
