package com.phoenix.blockchain.web.vo;

/**
 * Created by chengfeng on 2018/7/12.
 */
public class ResponseVO {

    /**
     * 成功码
     */
    public static final String SUCCESS = "200";

    /**
     * 失败码
     */
    public static final String FAIL = "404";

    /**
     * 返回码
     */
    private String returnCode;

    /**
     * 返回对象
     */
    private Object object;

    /**
     * 失败信息
     */
    private String errorMessage;

    /**
     * 描述信息
     */
    private String decription;

    /**
     * 构建成功返回结果
     *
     * @param object
     * @return
     */
    public static ResponseVO success(Object object) {
        ResponseVO responseVO = new ResponseVO();

        responseVO.setObject(object);
        responseVO.setReturnCode(SUCCESS);

        return responseVO;
    }

    /**
     * 构建失败返回结果
     *
     * @param object
     * @return
     */
    public static ResponseVO fail(Object object, String failMsg) {
        ResponseVO responseVO = new ResponseVO();

        responseVO.setObject(object);
        responseVO.setReturnCode(FAIL);
        responseVO.setErrorMessage(failMsg);

        return responseVO;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }
}
