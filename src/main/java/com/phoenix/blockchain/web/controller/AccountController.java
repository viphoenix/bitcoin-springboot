package com.phoenix.blockchain.web.controller;

import java.security.KeyPair;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phoenix.blockchain.common.dal.DbAccess;
import com.phoenix.blockchain.common.util.WalletUtils;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.service.ApplicationContextProvider;
import com.phoenix.blockchain.biz.service.account.AccountManager;
import com.phoenix.blockchain.biz.service.event.AccountSyncEvent;
import com.phoenix.blockchain.web.vo.ResponseVO;

/**
 *  Created by chengfeng on 2018/7/11.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private DbAccess dbAccess;

	@Autowired
	private AccountManager accountManager;


	/**
	 * 创建账户
	 * @param request
	 * @return
	 */
	@PostMapping("/new")
	public ResponseVO newAccount(HttpServletRequest request) throws Exception {

		// 1.生成秘钥对
		KeyPair keyPair = WalletUtils.generateKeyPair();

		// 2.创建账户
		Account account = accountManager.create(keyPair);

		// 3.持久化账户信息
		accountManager.saveAccount(account);

		// 异步广播账户信息到对等节点
		new Thread(new Runnable() {
			@Override
			public void run() {

				ApplicationContextProvider.publishEvent(new AccountSyncEvent(account));

			}
		}).start();


		// 4.生成私钥
		account.setPrivateKey(WalletUtils.privateKeyToString(keyPair.getPrivate()));

		// 5.返回结果
		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(ResponseVO.SUCCESS);
		responseVO.setObject(account);

		return responseVO;
	}

	@GetMapping("/list")
	public ResponseVO listAccounts() throws Exception {

		List<Account> accounts = accountManager.listAccounts();

		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(ResponseVO.SUCCESS);
		responseVO.setObject(accounts);
		responseVO.setDecription("账户列表信息.");

		return responseVO;
	}


	@GetMapping("/get")
	public ResponseVO getAccount(String address) throws Exception {
		Account account = accountManager.getAccount(address);

		ResponseVO responseVO = new ResponseVO();
		responseVO.setReturnCode(ResponseVO.SUCCESS);
		responseVO.setObject(account);

		return responseVO;
	}

}
