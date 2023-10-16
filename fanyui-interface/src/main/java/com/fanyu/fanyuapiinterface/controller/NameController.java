package com.fanyu.fanyuapiinterface.controller;


import com.fanyu.fanyuiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 查询者名称接口
 * @author fanyu
 * @date 2023/06/01 23:47
 **/
@RestController
@RequestMapping("/name")
public class NameController {

	@GetMapping("/get")
	public String getNameByGet(String name){
		return "Get 你的名字是"+name;
	}
	@PostMapping("/post")
	public String getNameByPost(@RequestBody String name){
		return "Post 你的名字是"+name;
	}
	@PostMapping("/user")
	public String getUsernameByPost(@RequestBody User user, HttpServletRequest request){
//		String accessKey = request.getHeader("accessKey");
//		String nonce = request.getHeader("nonce");
//		String timestamp = request.getHeader("timestamp");
//		String sign = request.getHeader("sign");
//		String body = request.getHeader("body");
//		//TODO 实际是去数据库查是否已分配给用户
//		if (!accessKey.equals("fanyu")){
//			throw new RuntimeException("无权限");
//		}
//		//校验随机数
//		if (Long.parseLong(nonce) >10000){
//			throw new RuntimeException("无权限");
//		}
//		//TODO 时间和当前时间不能超过5分钟
//		long currentTime = System.currentTimeMillis() / 1000;
//		long FIVE_MINUTES = 60 * 5L;
//		if (timestamp != null && (currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
//			throw new RuntimeException("超时连接");
//		}
		return "Post 你的名字是"+user.getUserName();
	}
}
