package com.phoenix.blockchain.common.enums;

/**
 * Created by chengfeng on 2018/7/8.
 *
 * 交易状态
 */
public enum TxStatusEnum {

    SUCCESS("SUCCESS", "SUCCESS", "成功"),

    FAIL("FAIL", "FAIL", "失败")

    ;


    private String code;

    private String name;

    private String desc;

    TxStatusEnum(String code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
