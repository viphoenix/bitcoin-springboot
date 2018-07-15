package com.phoenix.blockchain.core.model;

import com.phoenix.blockchain.common.constants.BitCoinConstants;

/**
 * Created by chengfeng on 2018/7/8.
 *
 * 区块头
 */
public class BlockHeader extends BaseDomain {

    private static final long serialVersionUID = 7702113613467813322L;

    private String version = BitCoinConstants.SYS_VERSION;

    /**
     * 上一区块Header hash
     */
    private String preHash;

    /**
     * 当前区块Header Hash, 比特币系统未使用
     */
    private String curHash;

    /**
     * 随机值
     */
    private Long nonce;

    /**
     * 当前区块高度
     */
    private Long height;

    /**
     * 挖矿难度
     */
    private long difficulty;

    /**
     * 区块生成时间
     */
    private Long timestamp;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPreHash() {
        return preHash;
    }

    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    public String getCurHash() {
        return curHash;
    }

    public void setCurHash(String curHash) {
        this.curHash = curHash;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(long difficulty) {
        this.difficulty = difficulty;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
