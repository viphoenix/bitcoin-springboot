package com.phoenix.blockchain.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.phoenix.blockchain.core.model.Transaction;
import com.phoenix.blockchain.biz.service.transaction.TransactionPool;
import com.phoenix.blockchain.biz.service.account.AccountManager;
import com.phoenix.blockchain.biz.service.transaction.TransactionManager;
import com.phoenix.blockchain.web.vo.ResponseVO;
import com.phoenix.blockchain.web.vo.TransactionVo;

/**
 * Created by chengfeng on 2018/7/14.
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private TransactionPool transactionPool;

    @Autowired
    private TransactionManager transactionManager;


    @PostMapping("/new")
    public ResponseVO create(@RequestBody TransactionVo transactionVo) throws Exception {

        Transaction transaction = new Transaction();

        ResponseVO responseVO = new ResponseVO();

        try {

            // 入参校验
            Preconditions.checkNotNull(transactionVo.getSender(), "付款人不能为空.");
            Preconditions.checkNotNull(transactionVo.getRecipient(), "收款人不能为空.");
            Preconditions.checkNotNull(transactionVo.getAmount(), "金额不能为空.");
            Preconditions.checkArgument(transactionVo.getAmount().compareTo(BigDecimal.ZERO) > 0, "金额非法.");
            Preconditions.checkNotNull(transactionVo.getPrivateKey(), "私钥不能为空.");

            transaction.setSendAddress(transactionVo.getSender());
            transaction.setReceiptAddress(transactionVo.getRecipient());
            transaction.setAmount(transactionVo.getAmount());

            // 交易验证
            if (!transactionManager.verify(transaction, transactionVo.getPrivateKey())) {
                throw new Exception("交易验证失败. sender: " + transaction.getSendAddress());

            }

            // 保存交易至交易池
            transactionPool.addTransaction(transaction);

            // 同步交易
            transactionManager.sync(transaction);

            responseVO.setReturnCode(ResponseVO.SUCCESS);
            responseVO.setObject(transaction);
        } catch (Exception e) {
            responseVO.setReturnCode(ResponseVO.FAIL);
            responseVO.setObject(transaction);
        }

        return responseVO;
    }

    @GetMapping("/list")
    public ResponseVO listTransaction() throws Exception{

        ResponseVO responseVO = new ResponseVO();

        List<Transaction> transactions = transactionManager.listTransaction();
        responseVO.setObject(transactions);
        responseVO.setReturnCode(ResponseVO.SUCCESS);
        responseVO.setDecription("交易列表信息.");

        return responseVO;

    }
}
