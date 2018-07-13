package com.phoenix.blockchain.common.util;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.util.Arrays;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import com.phoenix.blockchain.common.constants.BitCoinConstants;

/**
 * Created by chengfeng on 2018/7/12.
 *
 * 钱包工具类
 */
public class WalletUtils {

    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";


    /**
     * 生成一个密钥对
     * @return
     */
    public static KeyPair generateKeyPair() throws Exception {

        // 注册 BC Provider
        Security.addProvider(new BouncyCastleProvider());
        // 创建椭圆曲线算法的密钥对生成器
        KeyPairGenerator
                keyPairGenerator = KeyPairGenerator.getInstance(BitCoinConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider
                .PROVIDER_NAME);
        // 椭圆曲线（EC）域参数设定
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(BitCoinConstants.EC_PARAM_SPEC);
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 根据公钥生成钱包地址
     * @param publicKey
     * @return
     */
    public static String generateAddress(byte[] publicKey) {

        //1. 计算公钥的 SHA-256 哈希值
        byte[] sha256Bytes = HashUtils.sha256(publicKey);
        //2. 取上一步结果，计算 RIPEMD-160 哈希值
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256Bytes, 0, sha256Bytes.length);
        byte[] ripemd160Bytes = new byte[digest.getDigestSize()];
        digest.doFinal(ripemd160Bytes, 0);
        //3. 取上一步结果，前面加入地址版本号（主网版本号“0x00”）
        byte[] networkID = new BigInteger("00", 16).toByteArray();
        byte[] extendedRipemd160Bytes = ByteUtils.add(networkID, ripemd160Bytes);
        //4. 取上一步结果，计算 SHA-256 哈希值
        byte[] oneceSha256Bytes = HashUtils.sha256(extendedRipemd160Bytes);
        //5. 取上一步结果，再计算一下 SHA-256 哈希值
        byte[] twiceSha256Bytes = HashUtils.sha256(oneceSha256Bytes);
        //6. 取上一步结果的前4个字节（8位十六进制）
        byte[] checksum = new byte[4];
        System.arraycopy(twiceSha256Bytes, 0, checksum, 0, 4);
        //7. 把这4个字节加在第5步的结果后面，作为校验
        byte[] binaryAddressBytes = ByteUtils.add(extendedRipemd160Bytes, checksum);
        //8. 把结果用 Base58 编码算法进行一次编码
        return Base58.encode(binaryAddressBytes);
    }

    /**
     * 将私钥转换转成字符串
     * @return
     */
    public static String privateKeyToString(PrivateKey privateKey) {
        return ((ECPrivateKey) privateKey).getS().toString(16);
    }

    /**
     * 将 byte[] 公钥转成字符串
     * @param publicKey
     * @return
     */
    public static String publicKeyEncode(byte[] publicKey) {
        return Base58.encode(publicKey);
    }

    /**
     * 验证地址是否合法
     * @param address
     * @return
     */
    public static boolean verifyAddress(String address) {

        if (address.length() < 26 || address.length() > 35) {
            return false;
        }
        byte[] decoded = decodeBase58To25Bytes(address);
        if (null == decoded) {
            return false;
        }
        // 验证校验码
        byte[] hash1 = HashUtils.sha256(Arrays.copyOfRange(decoded, 0, 21));
        byte[] hash2 = HashUtils.sha256(hash1);

        return Arrays.equals(Arrays.copyOfRange(hash2, 0, 4), Arrays.copyOfRange(decoded, 21, 25));
    }

    /**
     * 使用 Base58 把地址解码成 25 字节
     * @param input
     * @return
     */
    private static byte[] decodeBase58To25Bytes(String input) {

        BigInteger num = BigInteger.ZERO;
        for (char t : input.toCharArray()) {
            int p = ALPHABET.indexOf(t);
            if (p == -1) {
                return null;
            }
            num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(p));
        }

        byte[] result = new byte[25];
        byte[] numBytes = num.toByteArray();
        System.arraycopy(numBytes, 0, result, result.length - numBytes.length, numBytes.length);
        return result;
    }

}
