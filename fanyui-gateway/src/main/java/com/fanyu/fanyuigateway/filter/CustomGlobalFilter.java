package com.fanyu.fanyuigateway.filter;

import com.fanyu.fanyucommon.model.entity.InterfaceInfo;
import com.fanyu.fanyucommon.model.entity.User;
import com.fanyu.fanyucommon.service.InnerInterfaceInfoService;
import com.fanyu.fanyucommon.service.InnerUserInterfaceInfoService;
import com.fanyu.fanyucommon.service.InnerUserService;
import com.fanyu.fanyuiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
	private static final String INTERFACE_HOST = "http://localhost:8123";
	@DubboReference(version = "1.0.1")
	private InnerUserService innerUserService;

	@DubboReference(version = "1.0.1")
	private InnerInterfaceInfoService innerInterfaceInfoService;

	@DubboReference(version = "1.0.1")
	private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
	@Resource
	StringRedisTemplate stringRedisTemplate;

	private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1","");

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		//1.打上请求日志
		//2.黑白名单(可做可不做)
		//3.用户鉴权(API签名认证)
		//4.远程调用判断接口是否存在以及获取调用接口信息
		//5.判断接口是否还有调用次数，如果没有则直接拒绝
		//6.发起接口调用
		//7.获取响应结果，打上响应日志
		//8.接口调用次数+1

		//1. 用户发送请求到API网关

		//1. 打上请求日志
		ServerHttpRequest request = exchange.getRequest();
		
		String path = INTERFACE_HOST+request.getPath().value();
		String method = request.getMethod().toString();
		
		log.info("请求唯一标识" + request.getId());
		log.info("请求路径" + request.getPath().value());
		log.info("请求方法" + request.getMethod());
		log.info("请求参数" + request.getQueryParams());
		String sourceAddress = request.getLocalAddress().getHostString();
		log.info("请求来源地址" + sourceAddress );
		log.info("请求来源地址" + request.getRemoteAddress());
		ServerHttpResponse response = exchange.getResponse();

		//2. 黑白名单（可做可不做）
//		if (!IP_WHITE_LIST.contains(sourceAddress)){
//			return handleNoAuth(response);
//		}

		//3. 用户鉴权（判断ak、sk是否合法）
		HttpHeaders headers = request.getHeaders();

		String accessKey = headers.getFirst("accessKey");
		String nonce = headers.getFirst("nonce");
		String timestamp = headers.getFirst("timestamp");
		String sign = headers.getFirst("sign");
		String body = headers.getFirst("body");

		//去数据库查询是否已经分配给用户
		User invokeUser = null;
		try {
			invokeUser = innerUserService.getInvokeUser(accessKey);
		} catch (Exception e) {
			log.info("远程调用获取被调用接口信息失败");
			log.error("getInvokeUser error", e);
		}
		if (invokeUser == null) {
			log.error("尚未分配！！！！");
			return handleNoAuth(response);
		}
		//判断随机数
		if (nonce != null && Long.parseLong(nonce) > 10000L) {
			return handleNoAuth(response);
		}
		//3.1防重放，使用redis存储请求的唯一标识，随机时间，并定时淘汰，那使用什么redis结构来实现嗯？
		//既然是单个数据，这样用string结构实现即可


		Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(nonce, "1", 5, TimeUnit.MINUTES);
		if (success ==null){
			log.error("随机数存储失败!!!!");
			return handleNoAuth(response);
		}

		//时间和当前时间不能超过5分钟
		long currentTime = System.currentTimeMillis() / 1000;
		final long FIVE_MINUTES = 60 * 5L;
		if (timestamp != null && (currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
			log.error("超时调用接口!!!!");
			return handleNoAuth(response);
		}

		// 实际情况中是从数据库中查出 secretKey
		String secretKey = invokeUser.getSecretKey();
		String serverSign = SignUtils.genSign(body, secretKey);
		if (sign == null || !sign.equals(serverSign)) {
			log.error("签名校验失败!!!!");
			return handleNoAuth(response);
		}

		//4.远程调用判断接口是否存在以及获取调用接口信息

		//数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
		InterfaceInfo interfaceInfo	=null;
		try {
			interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
		} catch (Exception e) {
			log.info("远程调用获取被调用接口信息失败");
			log.error("getInvokeUser error", e);
		}
		if (interfaceInfo == null){
			log.error("接口不存在!!!!");
			return handleNoAuth(response);
		}

		//5.判断接口是否还有调用次数，如果没有则直接拒绝
		boolean result = false;
		try {
			result= innerUserInterfaceInfoService.invokeCount(invokeUser.getId(), interfaceInfo.getId());
		} catch (Exception e) {
			log.info("接口剩余次数不足");
			log.error("getInvokeUser error", e);
		}
		if (!result){
			log.error("统计接口出现问题或者用户恶意调用不存在的接口");
			return handleNoAuth(response);
		}

		//6. 请求转发，调用模拟接口
		Mono<Void> filter = chain.filter(exchange);

		//7. 响应日志
		return handleResponse(exchange,chain);

	}

	private Mono<Void> handleNoAuth(ServerHttpResponse response){
		response.setStatusCode(HttpStatus.FORBIDDEN);
		return response.setComplete();
	}

	public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain) {


		try {
			//从交换拿响应对象
			ServerHttpResponse originalResponse = exchange.getResponse();
			//缓冲区工厂，拿到缓存数据
			DataBufferFactory bufferFactory = originalResponse.bufferFactory();
			//拿到响应码
			HttpStatus statusCode = originalResponse.getStatusCode();

			if (statusCode == HttpStatus.OK) {
				//装饰，增强能力
				ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
					//等调用完转发的接口后才会执行
					@Override
					public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
						log.info("body instanceof Flux: {}", (body instanceof Flux));
						//对象是响应式的
						if (body instanceof Flux) {
							//我们拿到真正的body
							Flux<? extends DataBuffer> fluxBody = Flux.from(body);
							//往返回值里面写数据
							//拼接字符串
							return super.writeWith(
									fluxBody.map(dataBuffer -> {
										//8. 调用成功，接口调用次数+1 todo invokeCount
										byte[] content = new byte[dataBuffer.readableByteCount()];
										dataBuffer.read(content);
										//释放掉内存
										DataBufferUtils.release(dataBuffer);
										// 构建日志
										StringBuilder sb2 = new StringBuilder(200);
										List<Object> rspArgs = new ArrayList<>();
										rspArgs.add(originalResponse.getStatusCode());
										//data
										String data = new String(content, StandardCharsets.UTF_8);
										sb2.append(data);
										//打印日志
										log.info("响应结果" + data);
										return bufferFactory.wrap(content);
							}));
						} else {
							//9.调用失败，返回规范错误码
							log.error("<--- {} 响应code异常", getStatusCode());
						}
						return super.writeWith(body);
					}
				};
				//设置 response 对象为装饰过的
				return chain.filter(exchange.mutate().response(decoratedResponse).build());
			}
			//降级处理返回数据
			return chain.filter(exchange);
		} catch (Exception e) {
			log.error("网关处理响应异常" + e);
			return chain.filter(exchange);
		}
	}
	@Override
	public int getOrder() {
		return -1;
	}
}