package com.fanyu.fanyuapiinterface;

import com.fanyu.fanyuiclientsdk.client.FanyuiClient;


import com.fanyu.fanyuiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class FanyuapiInterfaceApplicationTests {

	@Resource
	private FanyuiClient fanyuiClient;

	@Test
	void contextLoads() {
		String result = fanyuiClient.getNameByGet("fanyu");
		String result2 = fanyuiClient.getNameByPost("fanyu");
		User user = new User();
		user.setUserName("fanyu");
		String result3 = fanyuiClient.getUserNameByPost(user);
		System.out.println(result);
		System.out.println(result2);
		System.out.println(result3);
	}



}
