package com.phoenix.blockchain.core.model;

import java.math.BigDecimal;

import com.phoenix.blockchain.common.util.WalletUtils;

/**
 * Created by chengfeng on 2018/7/8.
 *
 * 账户模型
 */
public class Account extends BaseDomain {

    private static final long serialVersionUID = -817213132619286510L;
    /**
     * 账户私钥,不序列化
     */
    private transient String privateKey;

    /**
     * 账户公钥
     */
    private byte[] publicKey;

    /**
     * 账户地址
     */
    private String address;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 账户锁定状态
     */
    private boolean locked = false;

    public Account() {

    }

    public Account(byte[] publicKey) {
        this.publicKey = publicKey;
        this.address = WalletUtils.generateAddress(publicKey);
        this.balance = BigDecimal.TEN;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "Account{" +
                "privateKey='" + privateKey + '\'' +
                ", publicKey=" + WalletUtils.publicKeyEncode(publicKey) +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                ", locked=" + locked +
                '}';
    }
}
