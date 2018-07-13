package com.phoenix.blockchain.core.enums;

/**
 * Created by chengfeng on 2018/7/8.
 *
 * 比特币网络中的消息类型
 */
public enum MessageTypeEnum {

    // 账户同步请求
    ACCOUNT_SYN_REQUEST((byte)1, "ACCOUNT_SYN", "账户同步请求"),

    // 账户同步响应
    ACCOUNT_SYN_RESPONSE((byte)-1, "ACCOUNT_SYN_RESPONSE", "账户同步响应"),

    // 区块同步请求
    BLOCK_SYN_REQUEST((byte)2, "BLOCK_SYN_REQUEST", "区块同步请求"),

    // 区块同步响应
    BLOCK_SYN_RESPONSE((byte)-2, "BLOCK_SYN_RESPONSE", "区块同步响应"),

    // 交易同步请求
    TRANSATOIN_SYN_REQUEST((byte)3, "TRANSATOIN_SYN_REQUEST", "交易同步请求"),

    // 交易同步响应
    TRANSATOIN_SYN_RESPONSE((byte)-3, "TRANSATOIN_SYN_RESPONSE", "交易同步响应"),

    ;

    private byte code;

    private String name;

    private String desc;

    /**
     * 根据code返回消息类型
     *
     * @param code
     * @return
     * @throws Exception
     */
    public static MessageTypeEnum getByCode(byte code) {

        for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
            if (messageTypeEnum.code == code) {
                return messageTypeEnum;
            }
        }

        return null;

    }

    MessageTypeEnum(byte code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
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
