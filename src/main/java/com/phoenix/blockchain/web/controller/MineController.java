package com.phoenix.blockchain.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phoenix.blockchain.biz.service.blockchain.account.AccountManager;
import com.phoenix.blockchain.biz.service.blockchain.blockchain.BlockChainManager;
import com.phoenix.blockchain.biz.service.blockchain.pow.PowResult;
import com.phoenix.blockchain.biz.service.blockchain.pow.ProofOfWork;
import com.phoenix.blockchain.biz.service.blockchain.transaction.TransactionManager;
import com.phoenix.blockchain.biz.service.event.MineBlockEvent;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.model.Block;
import com.phoenix.blockchain.core.service.ApplicationContextProvider;
import com.phoenix.blockchain.web.vo.ResponseVO;

/**
 * Created by chengfeng on 2018/7/14.
 *
 * 挖矿控制器
 */
@RestController
@RequestMapping("/Mine")
public class MineController {

    @Autowired
    private BlockChainManager blockChainManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private ProofOfWork proofOfWork;

    @Autowired
    private TransactionManager transactionManager;

    @PostMapping("/start")
    public ResponseVO mine() throws Exception{

        // 1.获取挖矿账号
        Account account = accountManager.getMineAccount();

        if (null == account) {

            return ResponseVO.fail(null, "挖矿账号不存在");
        }

        // 2.创建区块
        Block block = blockChainManager.createNewBlock(account);

        // 3.pow工作量证明
        PowResult powResult = proofOfWork.mine(block);
        block.getHeader().setNonce(powResult.getNonce());
        block.getHeader().setCurHash(powResult.getHash());

        // 4.执行交易并移除完成的交易记录
        transactionManager.execute(block.getBody().getTransactions());

        // 5.保存区块
        blockChainManager.saveBlock(block);

        // 6.更新最新区块索引
        blockChainManager.updateLastBlock(block);

        // 7.同步区块
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApplicationContextProvider.publishEvent(new MineBlockEvent(block));
            }
        }).start();

        return ResponseVO.success(block);
    }
}
