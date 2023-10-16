package com.fanyu.fanyuiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fanyu.fanyuiclientsdk.model.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import static com.fanyu.fanyuiclientsdk.utils.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 * @author fanyu
 * @date 2023/05/27 00:29
 **/
public class FanyuiClient {
	private static final String GATEWAY_HOST = "http://localhost:8090";
	/** 访问密钥 */
	private String accessKey;

	private String secretKey;

	/**
	 *
	 * @param accessKey
	 * @param secretKey
	 */
	public FanyuiClient(String accessKey,String secretKey){
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	/**
	 *
	 * @param name
	 * @return
	 */

	public String getNameByGet(String name){
		//可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("name",name);
		String result = HttpUtil.get(GATEWAY_HOST + "/api/name/",paramMap);
		System.out.println(result);
		return result;
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public String getNameByPost(@RequestParam String name) {
		//可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("name",name);
		String result = HttpUtil.get(GATEWAY_HOST + "/api/name/",paramMap);
		System.out.println(result);
		return result;
	}

	/**
	 *
	 * @param body
	 * @return
	 */
	private Map<String, String> getHeaderMap(String body) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("accessKey",accessKey);
		// 不能直接发送
		// hashMap.put("secretKey",secretKey);
		hashMap.put("nonce", RandomUtil.randomNumbers(4));
		hashMap.put("body",body);
		hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
		hashMap.put("sign",genSign(body,secretKey));
		return hashMap;

	}

	/**
	 *
	 * @param user
	 * @return
	 */
	public String getUserNameByPost(@RequestBody User user){
		String json = JSONUtil.toJsonStr(user);
		HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/")
				.addHeaders(getHeaderMap(json))
				.body(json)
				.execute();
		System.out.println(httpResponse.getStatus());
		String result = httpResponse.body();
		System.out.println(result);
		return result;

	}

}
