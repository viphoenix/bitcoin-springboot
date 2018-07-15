package com.phoenix.blockchain.biz.service.blockchain.account;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.phoenix.blockchain.common.dal.RocksDbAccess;
import com.phoenix.blockchain.core.model.Account;

/**
 * Created by chengfeng on 2018/7/12.
 */
@Component
public class AccountManagerImpl implements AccountManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountManagerImpl.class);

    private static final String ACCOUNT_PREFIX = "ACCOUNT_";

    private static final String MINE_ACCOUNT_PREFIX = "MINE_ACCOUNT_";

    @Autowired
    private RocksDbAccess rocksDbAccess;

    /**
     * 根据秘钥对创建账户
     *
     * @param keyPair
     * @return
     */
    @Override
    public Account create(KeyPair keyPair) {

        return new Account(keyPair.getPublic().getEncoded());
    }

    /**
     * 获取账户列表
     * @return
     */
    @Override
    public List<Account> listAccounts() {

        List<Object> result = rocksDbAccess.selectByPrefix(ACCOUNT_PREFIX);
        List<Account> accounts = new ArrayList<>();
        for (Object o : result) {
            accounts.add((Account) o);
        }
        return accounts;
    }

    @Override
    public Account getBaseAccount(String address) {
        return getAccount(ACCOUNT_PREFIX + address);
    }

    @Override
    public Account getMineAccount() {
        return getAccount(MINE_ACCOUNT_PREFIX);
    }

    /**
     * 保存普通账户
     *
     * @param account
     */
    @Override
    public void saveBaseAccount(Account account) {
        saveAccount(ACCOUNT_PREFIX + account.getAddress(), account);
    }

    /**
     * 保存挖矿账户
     *
     * @param account
     */
    @Override
    public void saveMineAccount(Account account) {
        saveAccount(MINE_ACCOUNT_PREFIX, account);
    }

    /**
     * 保存账户
     *
     * @param account
     */
    private void saveAccount(String prefix, Account account) {
        rocksDbAccess.put(prefix, account);
    }

    /**
     * 根据key获取指定账户
     *
     * @param key
     * @return
     */
    private Account getAccount(String key) {

        Optional<Object> result = rocksDbAccess.get(key);

        if (result.isPresent()) {
            return (Account) result.get();
        }

        return null;
    }
}
