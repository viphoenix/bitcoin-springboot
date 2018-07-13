package com.phoenix.blockchain.core.service.net.model;

import java.io.Serializable;


/**
 * Created by chengfeng on 2018/7/11.
 */
public class Node extends org.tio.core.Node implements Serializable {

	public Node(String ip, int port) {
		super(ip, port);
	}

	public Node() {
		super(null, 0);
	}
}
