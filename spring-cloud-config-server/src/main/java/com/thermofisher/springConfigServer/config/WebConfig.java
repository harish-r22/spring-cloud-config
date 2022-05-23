package com.thermofisher.springConfigServer.config;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@Configuration
public class WebConfig implements WebMvcConfigurer{

	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Autowired
	HttpServletConfigFilter httpServletConfigFilter;
	 	
	 	@Bean
	    public FilterRegistrationBean<HttpServletConfigFilter> authenticationFilterBean() {
	 		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	 		registrationBean.setFilter(httpServletConfigFilter);
	 		registrationBean.addUrlPatterns("/springCoreService/prod","/application/prod");
	        return registrationBean;
	    }
	 	
}

