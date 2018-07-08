package com.phoenix.blockchain.common.service;

import com.phoenix.blockchain.core.model.Account;

/**
 * Created by chengfeng on 2018/7/8.
 *
 * 账户管理服务
 */
public interface AccountServiceFacade {

    /**
     * 创建账号
     *
     * @return
     */
    Account create();

    /**
     * 获取账户信息
     *
     * @param address
     * @return
     */
    Account get(String address);

    /**
     * 列出所有账号
     *
     * @return
     */
    Account list();
}
