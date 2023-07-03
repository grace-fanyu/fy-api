package com.fanyu.fanyuiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具
 * @author fanyu
 * @date 2023/05/27 00:36
 **/
public class SignUtils {

	/**
	 * 生成签名
	 * @param body 用户参数
	 * @param secretKey 密钥
	 * @return 不可解密的值
	 */
	public static String genSign(String body,String secretKey) {
		Digester md5 = new Digester(DigestAlgorithm.SHA256);
		String content = body + "." + secretKey;
		return md5.digestHex(content);
	}
}
