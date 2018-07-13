package com.phoenix.blockchain.core.service.net.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.server.ServerGroupContext;

import com.phoenix.blockchain.core.service.net.client.BitClientAioHandler;
import com.phoenix.blockchain.core.service.net.client.BitClientAioListener;
import com.phoenix.blockchain.core.service.net.server.BitServerAioHandler;
import com.phoenix.blockchain.core.service.net.server.BitServerAioListener;


/**
 * Created by chengfeng on 2018/7/11.
 */
@Configuration
public class GroupContextConfig {

	@Autowired
	TioConfig tioConfig;

	/**
	 * 客户端消息 handler, 包括编码、解码、消息处理
	 */
	@Autowired
	BitClientAioHandler bitClientAioHandler;

	/**
	 * 客户端事件监听器
	 */
	@Autowired
	BitClientAioListener bitClientAioListener;

	/**
	 * 服务端消息 handler, 包括编码、解码、消息处理
	 */
	@Autowired
	BitServerAioHandler bitServerAioHandler;

	/**
	 * 服务端事件监听器
	 */
	@Autowired
	BitServerAioListener bitServerAioListener;

	/**
	 * 客户端一组连接共用的上下文对象
	 * @return
	 */
	@Bean
	public ClientGroupContext clientGroupContext() {

		//断链后自动连接
		ReconnConf reconnConf = new ReconnConf(5000L, 20);
		ClientGroupContext clientGroupContext = new ClientGroupContext(bitClientAioHandler, bitClientAioListener, reconnConf);
		//设置心跳包时间间隔
		clientGroupContext.setHeartbeatTimeout(tioConfig.getHeartTimeout());
		return clientGroupContext;
	}

	/**
	 * 服务端一组连接共用的上下文对象
	 * @return
	 */
	@Bean
	public ServerGroupContext serverGroupContext() {

		ServerGroupContext serverGroupContext = new ServerGroupContext(tioConfig.getServerGroupContextName(),
				bitServerAioHandler, bitServerAioListener);

		serverGroupContext.setHeartbeatTimeout(tioConfig.getHeartTimeout());

		return serverGroupContext;
	}

}
