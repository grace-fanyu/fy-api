package com.fanyu.fanyuigateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude ={DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})
@EnableDubbo
public class FanyuiGatewayApplication {

	public static void main(String[] args) {
		//将dubbo缓存的绝对目录改成相对目录，避免后续项目上线出现问题 已实现
		String rootPath = System.getProperty("user.dir");
		String subDirectory = "gatewayDubboCache";
		String fullPath = rootPath + "/" + subDirectory;
		System.setProperty("user.home", fullPath);
		SpringApplication.run(FanyuiGatewayApplication.class, args);
	}

}
