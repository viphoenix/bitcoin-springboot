package com.phoenix.blockchain.biz.service.blockchain.account;

import java.security.KeyPair;
import java.util.List;

import com.phoenix.blockchain.core.model.Account;

/**
 * Created by chengfeng on 2018/7/12.
 *
 * 账户管理类
 */
public interface AccountManager {

    /**
     * 创建账户
     *
     * @return
     */
    Account create(KeyPair keyPair);

    /**
     * 返回账户列表
     *
     * @return
     */
    List<Account> listAccounts();

    /**
     * 获取普通账户信息
     *
     * @param address
     * @return
     */
    Account getBaseAccount(String address);

    /**
     * 获取挖矿账户信息
     *
     * @return
     */
    Account getMineAccount();

    /**
     * 保存普通账户
     *
     * @param account
     */
    void saveBaseAccount(Account account);

    /**
     * 保存挖矿账户
     * @param account
     */
    void saveMineAccount(Account account);

}
