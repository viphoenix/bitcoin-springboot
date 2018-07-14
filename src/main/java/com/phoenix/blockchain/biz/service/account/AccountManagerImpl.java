package com.phoenix.blockchain.biz.service.account;

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

    /**
     * 根据address获取指定账户
     *
     * @param address
     * @return
     */
    @Override
    public Account getAccount(String address) {

        Optional<Object> result = rocksDbAccess.get(ACCOUNT_PREFIX + address);

        if (result.isPresent()) {
            return (Account) result.get();
        }

        return null;
    }

    /**
     * 保存账户
     *
     * @param account
     */
    @Override
    public void saveAccount(Account account) {
        rocksDbAccess.put(ACCOUNT_PREFIX + account.getAddress(), account);
    }
}
