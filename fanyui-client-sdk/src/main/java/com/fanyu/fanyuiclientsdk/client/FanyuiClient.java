package com.fanyu.fanyuiclientsdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fanyu.fanyuiclientsdk.model.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * 调用第三方接口的客户端
 * @author fanyu
 * @date 2023/05/27 00:29
 **/
public class FanyuiClient {
	private String accessKey;

	private String secretKey;
	private static final String GATEWAY_HOST = "http://localhost:8090";

	public FanyuiClient(String accessKey,String secretKey){
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public String getNameByGet(String name){
		//可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("name",name);
		String result = HttpUtil.get(GATEWAY_HOST + "/api/name/",paramMap);
		System.out.println(result);
		return result;
	}
	public String getNameByPost(@RequestParam String name) {
		//可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("name",name);
		String result = HttpUtil.get(GATEWAY_HOST + "/api/name/",paramMap);
		System.out.println(result);
		return result;
	}
	public String getUserNameByPost(@RequestBody User user){
		String json = JSONUtil.toJsonStr(user);
		HttpResponse httpReponse = HttpRequest.post(GATEWAY_HOST + "/api/name/")
				.body(json)
				.execute();
		System.out.println(httpReponse.getStatus());
		String result = httpReponse.body();

		System.out.println(result);
		return result;

	}


}
