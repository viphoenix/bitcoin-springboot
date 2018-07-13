package com.phoenix.blockchain.core.service.net.model;

import org.tio.core.intf.Packet;

/**
 * Created by chengfeng on 2018/7/11.
 */
public class MessagePacket extends Packet {

    /**
     * 消息头的长度 1+4
     */
    public static final int HEADER_LENGTH = 5;

    /**
     * 打招呼信息
     */
    public static final String HELLO_MESSAGE = "Hello";
    /**
     * 获取账户列表的消息信号
     */
    public static final String FETCH_ACCOUNT_LIST_SYMBOL = "get_accounts_list";
    /**
     * 获取节点列表的消息信号
     */
    public static final String FETCH_NODE_LIST_SYMBOL = "get_nodes_list";
    /**
     * 消息类别，类别值在 MessagePacketType 常量类中定义
     */
    private byte type;

    private byte[] body;

    public MessagePacket(byte[] body) {
        this.body = body;
    }

    public MessagePacket() {
    }

    public MessagePacket(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    /**
     * @return
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * @param body
     */
    public void setBody(byte[] body) {
        this.body = body;
    }
}
