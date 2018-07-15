package com.phoenix.blockchain.biz.service.blockchain.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.phoenix.blockchain.biz.service.blockchain.account.AccountManager;
import com.phoenix.blockchain.biz.service.event.TransactionSynEvent;
import com.phoenix.blockchain.common.constants.BitCoinConstants;
import com.phoenix.blockchain.common.enums.TxStatusEnum;
import com.phoenix.blockchain.common.util.HashUtils;
import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.common.util.SignUtils;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.model.Transaction;
import com.phoenix.blockchain.core.service.ApplicationContextProvider;

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
            Account sender = accountManager.getBaseAccount(transaction.getSendAddress());
            Preconditions.checkNotNull(sender, "付款人账户不存在." + sender.getAddress());
            Account recipient = accountManager.getBaseAccount(transaction.getReceiptAddress());
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

            // 交易验证是否本人,注意解密时,交易序列化数据不能包含签名信息
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

    /**
     * 创建矿工奖励交易
     *
     * @param mineAccount
     * @return
     */
    @Override
    public Transaction createRewardTx(Account mineAccount) {

        Transaction transaction = new Transaction();

        transaction.setReceiptAddress(mineAccount.getAddress());
        transaction.setAmount(BitCoinConstants.REWARD_FEE);
        transaction.setHash(HashUtils.sha256Hex(transaction.toString()));

        return transaction;
    }

    /**
     * 执行区块中的交易
     *
     * @param transactions
     * @throws Exception
     */
    @Override
    public void execute(List<Transaction> transactions) throws Exception{

        for (int i = 0; i < transactions.size(); ++i) {

            // 矿工奖励交易记录
            if (i == 0) {
                Account recipient = accountManager.getMineAccount();

                recipient.setBalance(recipient.getBalance().add(transactions.get(i).getAmount()));
                accountManager.saveMineAccount(recipient);

                continue;
            }

            // 普通转账交易
            Transaction transaction = transactions.get(i);
            Account sender = accountManager.getBaseAccount(transaction.getSendAddress());
            Account recipient = accountManager.getBaseAccount(transaction.getReceiptAddress());

            // 验证签名
            boolean verify = SignUtils.verify(sender.getPublicKey(), transaction.getSignature(), transaction.toString());
            if (!verify) {
                transaction.setStatus(TxStatusEnum.FAIL);
                transaction.setErrorMsg("交易签名错误");

                continue;
            }

            // 验证是否足额
            if (transaction.getAmount().compareTo(sender.getBalance()) == 1) {
                transaction.setStatus(TxStatusEnum.FAIL);
                transaction.setErrorMsg("账户余额不足");
                continue;
            }

            sender.setBalance(sender.getBalance().subtract(transaction.getAmount()));
            recipient.setBalance(recipient.getBalance().add(transaction.getAmount()));

            accountManager.saveBaseAccount(sender);
            accountManager.saveBaseAccount(recipient);

            // 从交易池移除交易
            transactionPool.delete(transaction);
        }
    }

    @Override
    public void clear() {
        transactionPool.clear();
    }
}
