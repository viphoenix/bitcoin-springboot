package com.phoenix.blockchain.common.constants;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by chengfeng on 2018/7/12.
 */
public class BitCoinConstants {

    /**
     * 系统版本号
     */
    public static final String SYS_VERSION = "1.0";

    /**
     * 椭圆曲线密钥生成算法，ECDSA
     */
    public static final String KEY_GEN_ALGORITHM = "ECDSA";

    /**
     * 椭圆曲线（EC）域参数设定
     */
    public static final String EC_PARAM_SPEC = "secp256k1";

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHM = "SHA1withECDSA";

    /**
     * 创世区块preHash
     */
    public static final String ZERO_PRE_HASH = "0000000000000000000000000000000000000000000000000000000000000000";

    /**
     * 区块交易记录数
     */
    public static final int BLOCK_TX_NUM = 10;

    /**
     * 矿工奖励费用
     */
    public static final BigDecimal REWARD_FEE = new BigDecimal(25);

    /**
     * 挖矿难度,即hash值前缀0的个数
     */
    public static final int DIFFICULTY = 10;

    /**
     * 挖矿难度
     */
    public static final BigInteger TARGET = BigInteger.valueOf(1).shiftLeft(256 - DIFFICULTY);


}
