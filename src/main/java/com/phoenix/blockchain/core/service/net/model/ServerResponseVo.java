package com.phoenix.blockchain.core.service.net.model;

/**
 * Created by chengfeng on 2018/7/11.
 */
public class ServerResponseVo {

	/**
	 * 响应实体
	 */
	private Object item;
	/**
	 * 响应状态
	 */
	private boolean success = false;
	/**
	 * 返回错误信息
	 */
	private String message;

	public ServerResponseVo() {
	}

	public ServerResponseVo(Object item, boolean status) {
		this.item = item;
		this.success = status;
	}

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
