package com.phoenix.blockchain.biz.service.bitcoin.blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.phoenix.blockchain.biz.service.bitcoin.pow.ProofOfWork;
import com.phoenix.blockchain.biz.service.bitcoin.transaction.TransactionManager;
import com.phoenix.blockchain.biz.service.bitcoin.transaction.TransactionPool;
import com.phoenix.blockchain.common.constants.BitCoinConstants;
import com.phoenix.blockchain.common.dal.RocksDbAccess;
import com.phoenix.blockchain.common.enums.TxStatusEnum;
import com.phoenix.blockchain.common.util.LogUtils;
import com.phoenix.blockchain.core.model.Account;
import com.phoenix.blockchain.core.model.Block;
import com.phoenix.blockchain.core.model.BlockBody;
import com.phoenix.blockchain.core.model.BlockHeader;
import com.phoenix.blockchain.core.model.Transaction;

/**
 * Created by chengfeng on 2018/7/14.
 *
 * 区块链操作管理器
 */
@Component
public class BlockChainManagerImpl implements BlockChainManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockChainManagerImpl.class);

    /**
     * 区块键值对前缀
     */
    private static final String BLOCK_PREFIX = "BLOCK_";

    /**
     * 最新区块键值对前缀
     */
    private static final String LAST_BLOCK_PREFIX = "LAST_BLOCK_";

    @Autowired
    private TransactionPool transactionPool;

    @Autowired
    private TransactionManager transactionManager;

    @Autowired
    private RocksDbAccess rocksDbAccess;

    @Autowired
    private ProofOfWork proofOfWork;

    /**
     * 创建新区快
     *
     * @param account
     * @return
     */
    @Override
    public Block createNewBlock(Account account) {

        Block newBlock = new Block();
        Block lastBlock = getLastBlock();

        if (null == lastBlock) {
            // 创建创世区块
            newBlock = createGenesisBlock(account);

        } else {
            long prevHeight = lastBlock.getHeader().getHeight();
            String prevHash = lastBlock.getHeader().getCurHash();

            BlockHeader header = new BlockHeader();

            newBlock.setHeader(header);

            header.setHeight(prevHeight + 1);
            header.setPreHash(prevHash);
            header.setDifficulty(BitCoinConstants.DIFFICULTY);
            header.setTimestamp(new Date().getTime());
        }

        // 打包交易池中交易记录,每个区块10条交易
        BlockBody body = new BlockBody();
        newBlock.setBody(body);

        List<Transaction> transactions = new ArrayList<>();

        // 打包矿工奖励交易记录
        transactions.add(transactionManager.createRewardTx(account));

        for (Transaction transaction : transactionPool.getTransactionMap().values()) {

            // TODO 失败的交易暂不清除
            if (transaction.getStatus() == TxStatusEnum.FAIL) {
                continue;
            }

            transactions.add(transaction);

            if (transactions.size() > BitCoinConstants.BLOCK_TX_NUM) {
                break;
            }
        }

        body.setTransactions(transactions);


        return newBlock;
    }

    /**
     * 创建创世区块
     *
     * @param account
     * @return
     */
    @Override
    public Block createGenesisBlock(Account account) {

        Block block = new Block();
        BlockHeader header = new BlockHeader();
        BlockBody body = new BlockBody();

        header.setHeight(0L);
        header.setPreHash(BitCoinConstants.ZERO_PRE_HASH);
        header.setDifficulty(BitCoinConstants.DIFFICULTY);
        header.setTimestamp(new Date().getTime());

        block.setHeader(header);
        block.setBody(body);

        return block;
    }

    /**
     * 获取最新区块
     *
     * @return
     */
    @Override
    public Block getLastBlock() {

        Optional<Object> result = rocksDbAccess.get(LAST_BLOCK_PREFIX);

        if (result.isPresent()) {
            return (Block) result.get();
        }

        return null;
    }

    /**
     * 存储区块
     *
     * @param block
     */
    @Override
    public void saveBlock(Block block) {
        rocksDbAccess.put(BLOCK_PREFIX + block.getHeader().getHeight(), block);
    }

    /**
     * 获取制定高度的区块
     *
     * @param height
     * @return
     */
    @Override
    public Block getBlock(long height) {


        Optional<Object> result = rocksDbAccess.get(BLOCK_PREFIX + height);

        if (result.isPresent()) {
            return (Block) result.get();
        }

        return null;
    }

    /**
     * 更新最新区块
     *
     * @param block
     */
    @Override
    public void updateLastBlock(Block block) {
        rocksDbAccess.put(LAST_BLOCK_PREFIX, block);
    }

    /**
     * 验证区块
     *
     * @param block
     * @return
     */
    @Override
    public boolean validateBlock(Block block) {

        if (!proofOfWork.validate(block)) {
            LogUtils.warn(LOGGER, "区块工作量证明验证错误. block: {0}.", block);
            return false;
        }

        // 非创世区块,验证前置区块合法性
        long height = block.getHeader().getHeight();
        Block preBlock = getBlock(height - 1);
        if (height != 0 && null != preBlock) {

            if (!StringUtils.equals(block.getHeader().getPreHash(), preBlock.getHeader().getCurHash())) {
                return false;
            }
        }

        return true;
    }
}
