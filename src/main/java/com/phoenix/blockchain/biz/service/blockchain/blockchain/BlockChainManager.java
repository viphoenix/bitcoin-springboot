package com.phoenix.blockchain.biz.service.blockchain.blockchain;

import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.model.Block;

/**
 * Created by chengfeng on 2018/7/14.
 *
 * 区块链操作管理器
 */
public interface BlockChainManager {

    /**
     * 创建新区块
     *
     * @return
     */
    Block createNewBlock(Account account);

    /**
     * 创建创世区块
     *
     * @return
     */
    Block createGenesisBlock(Account account);

    /**
     * 获取当前区块链最新区块
     *
     * @return
     */
    Block getLastBlock();

    /**
     * 存储区块
     *
     * @param block
     */
    void saveBlock(Block block);

    /**
     * 获取制定高度的区块
     *
     * @param height
     * @return
     */
    Block getBlock(long height);

    /**
     * 更新最新区块
     *
     * @param block
     */
    void updateLastBlock(Block block);

    /**
     * 验证区块
     *
     * @param block
     * @return
     */
    boolean validateBlock(Block block);

}
