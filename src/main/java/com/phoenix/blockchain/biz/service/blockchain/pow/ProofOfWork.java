package com.phoenix.blockchain.biz.service.blockchain.pow;

import java.math.BigInteger;

import org.springframework.stereotype.Component;

import com.phoenix.blockchain.common.constants.BitCoinConstants;
import com.phoenix.blockchain.common.util.ByteUtils;
import com.phoenix.blockchain.common.util.HashUtils;
import com.phoenix.blockchain.core.model.Block;

/**
 * Created by chengfeng on 2018/7/14.
 *
 * 工作量证明算法
 */
@Component
public class ProofOfWork {

    /**
     * 挖矿
     *
     * @param block
     * @return
     */
    public PowResult mine(Block block) {

        PowResult result = new PowResult();

        long nonce = 0L;
        String headerHash = "";
        while (true) {

            headerHash = HashUtils.sha256Hex(generateHeaderByte(block, nonce));

            // TODO 计算hash值前缀0的个数,待验证
            if (new BigInteger(headerHash, 16).shiftRight(256 - BitCoinConstants.DIFFICULTY) == BigInteger.ZERO) {

                break;
            }

            ++nonce;
        }

        result.setHash(headerHash);
        result.setNonce(nonce);

        return result;

    }

    /**
     * 区块确认验证
     *
     * @param block
     * @return
     */
    public boolean validate (Block block) {
        byte[] preHashBytes = generateHeaderByte(block, block.getHeader().getNonce());

        return new BigInteger(HashUtils.sha256Hex(preHashBytes), 16).shiftRight(256 - BitCoinConstants.DIFFICULTY) ==
                BigInteger.ZERO;
    }

    /**
     * 序列化
     *
     * @param block
     * @param nonce
     * @return
     */
    private byte[] generateHeaderByte(Block block, long nonce) {

        String preHash = block.getHeader().getPreHash();

        byte[] preHashBytes = new BigInteger(preHash, 16).toByteArray();

        return ByteUtils.merge(
                preHashBytes,
                ByteUtils.toBytes(block.getHeader().getTimestamp()),
                ByteUtils.toBytes(block.getHeader().getDifficulty()),
                ByteUtils.toBytes(nonce)
        );
    }

}
