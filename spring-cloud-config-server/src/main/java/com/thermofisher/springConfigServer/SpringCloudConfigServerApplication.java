package io.javaBrains.springcloudconfigserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableConfigServer
@ComponentScan("com.thermofisher.springConfigServer")
public class SpringCloudConfigServerApplication implements WebMvcConfigurer {

	public static void main(String[] args) {

		SpringApplication.run(SpringCloudConfigServerApplication.class, args);
	}
}
