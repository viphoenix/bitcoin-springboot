package com.phoenix.blockchain.common.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by chengfeng on 2018/7/12.
 *
 *  sha256 hash算法工具类
 */
public class HashUtils {

    /**
     * 加密字符串
     *
     * @param input
     * @return
     */
    public static String sha256Hex(String input) {
        return DigestUtils.sha256Hex(input);
    }

    /**
     * 加密字节数组
     *
     * @param input
     * @return
     */
    public static String sha256Hex(byte[] input) {
        return DigestUtils.sha256Hex(input);
    }

    /**
     * 加密
     *
     * @param input
     * @return
     */
    public static byte[] sha256(String input) {
        return DigestUtils.sha256(input);
    }

    /**
     * 加密
     *
     * @param input
     * @return
     */
    public static byte[] sha256(byte[] input) {
        return DigestUtils.sha256(input);
    }
}
