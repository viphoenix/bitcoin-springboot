package com.phoenix.blockchain.core.service.net.model;

import com.phoenix.blockchain.web.vo.ResponseVO;

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

	/**
	 * 构建成功返回结果
	 *
	 * @param object
	 * @return
	 */
	public static ServerResponseVo success(Object object) {
		ServerResponseVo responseVO = new ServerResponseVo();

		responseVO.setItem(object);
		responseVO.setSuccess(true);

		return responseVO;
	}

	/**
	 * 构建失败返回结果
	 *
	 * @param object
	 * @return
	 */
	public static ServerResponseVo fail(Object object, String failMsg) {
		ServerResponseVo responseVO = new ServerResponseVo();

		responseVO.setItem(object);
		responseVO.setSuccess(false);
		responseVO.setMessage(failMsg);

		return responseVO;
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
