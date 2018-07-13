package com.phoenix.blockchain.core.service.account;

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
     * 根据字节数据创建账户
     *
     * @param data
     * @return
     */
    Account create(byte[] data);

    /**
     * 返回账户列表
     *
     * @return
     */
    List<Account> listAccounts();

    /**
     * 获取账户信息
     *
     * @param address
     * @return
     */
    Account getAccount(String address);
    /**
     * 保存账户
     *
     * @param account
     */
    void saveAccount(Account account);

}
