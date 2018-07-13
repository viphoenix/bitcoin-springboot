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
