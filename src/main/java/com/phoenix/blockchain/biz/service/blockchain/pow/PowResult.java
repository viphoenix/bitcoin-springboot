package com.phoenix.blockchain.biz.service.blockchain.pow;

/**
 * Created by chengfeng on 2018/7/15.
 *
 * pow验证结果
 */
public class PowResult {

    /**
     * 随机值
     */
    private Long nonce;

    /**
     * 区块Header hash
     */
    private String hash;

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
