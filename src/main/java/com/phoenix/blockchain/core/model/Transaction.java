package com.phoenix.blockchain.core.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.phoenix.blockchain.common.util.WalletUtils;
import com.phoenix.blockchain.common.enums.TxStatusEnum;

/**
 * Created by chengfeng on 2018/7/8.
 *
 * Transaction record
 */

public class Transaction extends BaseDomain {

    private static final long serialVersionUID = 6803567064058875208L;
    /**
     * 付款人地址
     */
    private String sendAddress;

    /**
     * 付款人签名
     */
    private String signature;

    /**
     * 收款人地址
     */
    private String receiptAddress;

    /**
     * 付款人公钥
     */
    private byte[] publicKey;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易hash
     */
    private String hash;

    /**
     * 交易时间戳
     */
    private Long timestamp;

    /**
     * 交易状态
     */
    private TxStatusEnum status = TxStatusEnum.SUCCESS;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 扩展信息
     */
    private Map<String, Object> extendFiledMap = new HashMap<>();

    public Transaction() {

    }

    public Transaction(String sendAddress, String receiptAddress, BigDecimal amount, Long timestamp) {
        this.sendAddress = sendAddress;
        this.receiptAddress = receiptAddress;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public TxStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TxStatusEnum status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Map<String, Object> getExtendFiledMap() {
        return extendFiledMap;
    }

    public void setExtendFiledMap(Map<String, Object> extendFiledMap) {
        this.extendFiledMap = extendFiledMap;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "sendAddress='" + sendAddress + '\'' +
                ", receiptAddress='" + receiptAddress + '\'' +
                ", publicKey='" + WalletUtils.publicKeyEncode(publicKey) + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", hash=" + hash +
                ", status=" + status.getCode() +
                ", extendFiledMap=" + extendFiledMap +
                '}';
    }
}
