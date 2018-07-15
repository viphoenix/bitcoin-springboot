package com.phoenix.blockchain.biz.service.bitcoin.pow;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.phoenix.blockchain.biz.service.bitcoin.blockchain.BlockChainManager;
import com.phoenix.blockchain.biz.service.bitcoin.transaction.TransactionManager;
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
     * 是否收到新区快同步事件 TODO:后期考虑优化
     */
    public static volatile boolean newSynBlock = false;

    @Autowired
    private BlockChainManager blockChainManager;

    @Autowired
    private TransactionManager transactionManager;

    /**
     * 挖矿
     *
     * @param block
     * @return
     */
    public Optional<PowResult> mine(Block block) {

        PowResult result = new PowResult();

        boolean hasResult = false;

        long nonce = 0L;
        String headerHash = "";
        while (!newSynBlock) {

            headerHash = HashUtils.sha256Hex(generateHeaderByte(block, nonce));

            // TODO 计算hash值前缀0的个数,待验证
            if (new BigInteger(headerHash, 16).shiftRight(256 - BitCoinConstants.DIFFICULTY) == BigInteger.ZERO) {

                hasResult = true;
                break;
            }

            ++nonce;
        }

        // 如果新区块到来,还未发现区块,立即终止挖矿,与新区块比较区块高度
        if (newSynBlock && !hasResult) {
            Block lastBlock = blockChainManager.getLastBlock();

            // 如果新区块高度大,则终止本轮挖矿,并删除区块中包含的交易;如果新区块高度小,忽略
            if (lastBlock.getHeader().getHeight() > block.getHeader().getHeight()) {

                // TODO 不严谨,可能落后多个区块
                transactionManager.clearTxInBlock(lastBlock);

                return Optional.absent();
            }
        }

        result.setHash(headerHash);
        result.setNonce(nonce);

        return Optional.of(result);

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
