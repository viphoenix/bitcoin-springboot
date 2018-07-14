package com.phoenix.blockchain.common.util;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;

import com.phoenix.blockchain.common.constants.BitCoinConstants;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

/**
 * 签名工具类
 * @author yangjian
 * @since 18-4-10
 */
public class SignUtils {

	/**
	 * 通过字符串私钥生成 PrivateKey 对象
	 * @param sCode PrivateKey 的专用密钥
	 * @return
	 */
	public static PrivateKey generatePrivateKey(String sCode) throws Exception {

		ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(BitCoinConstants.EC_PARAM_SPEC);
		ECPrivateKeySpec keySpec = new ECPrivateKeySpec(new BigInteger(sCode, 16), ecSpec);
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(BitCoinConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		return keyFactory.generatePrivate(keySpec);
	}

	/**
	 * 通过 byte[] 公钥生成 PublicKey 对象
	 * @param publicKey
	 * @return
	 */
	public static PublicKey generatePublicKey(byte[] publicKey) throws Exception {

		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(BitCoinConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		ECPublicKey pubKey = (ECPublicKey) keyFactory.generatePublic(x509KeySpec);
		return pubKey;
	}

	/**
	 * 使用私钥对源数据签名
	 * @param privateKey
	 * @param data
	 * @return
	 */
	public static String sign(String privateKey, String data) throws Exception {

		return sign(generatePrivateKey(privateKey), data);
	}

	public static String sign(PrivateKey privateKey, String data) throws Exception {

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(BitCoinConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		PrivateKey pkcs8PrivateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Signature signature = Signature.getInstance(BitCoinConstants.SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		signature.initSign(pkcs8PrivateKey);
		signature.update(data.getBytes());
		byte[] res = signature.sign();
		return HexBin.encode(res);
	}

	/**
	 * 使用公钥验证签名
	 * @param publicKey
	 * @param sign
	 * @param data
	 * @return
	 */
	public static boolean verify(byte[] publicKey, String sign, String data) throws Exception {

		return verify(generatePublicKey(publicKey), sign, data);
	}

	public static boolean verify(PublicKey publicKey, String sign, String data) throws Exception {

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory keyFactory = KeyFactory.getInstance(BitCoinConstants.KEY_GEN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		PublicKey x509PublicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		Signature signature = Signature.getInstance(BitCoinConstants.SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
		signature.initVerify(x509PublicKey);
		signature.update(data.getBytes());
		return signature.verify(HexBin.decode(sign));
	}

}
