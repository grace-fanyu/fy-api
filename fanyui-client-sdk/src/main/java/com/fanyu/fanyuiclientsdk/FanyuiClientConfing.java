package com.fanyu.fanyuiclientsdk;

import com.fanyu.fanyuiclientsdk.client.FanyuiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author fanyu
 * @date 2023/05/26 19:20
 **/
@Configuration
@ConfigurationProperties("fanyui.client")
@Data
@ComponentScan
public class FanyuiClientConfing {

	private String accessKey;

	private String secretKey;

	@Bean
	public FanyuiClient fanyuiClient(){
		return new FanyuiClient(accessKey, secretKey);
	}


}
