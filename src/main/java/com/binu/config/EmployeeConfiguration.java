package com.binu.config;

import com.binu.interceptor.EmployeeLoginInterceptor;
import com.binu.interceptor.SecInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EmployeeConfiguration implements WebMvcConfigurer {
	private final Logger log = LoggerFactory.getLogger(EmployeeConfiguration.class);

	@Autowired
	EmployeeLoginInterceptor loginInterceptor;
	@Autowired
	SecInterceptor secInterceptor;

	@Value("${oauth2.ClientID}")
	private String CLIENT_ID;

	@Override
	//public void addInterceptors(InterceptorRegistry registry) {
	//	registry.addInterceptor(loginInterceptor);
	//}
	public void addInterceptors(InterceptorRegistry registry) {

		log.info("Setting CLientID {}",CLIENT_ID);
		this.secInterceptor.setCliendId(CLIENT_ID);
		registry.addInterceptor(secInterceptor);
	}
}
