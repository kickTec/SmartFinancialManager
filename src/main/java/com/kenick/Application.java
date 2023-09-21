package com.kenick;

import com.kenick.fund.controller.FundController;
import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class Application {

	private final static Logger logger = LoggerFactory.getLogger(Application.class);

	@Value("${server.http-port}")
	private Integer httpPort;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		logger.debug("httpPort:{}", httpPort);
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setPort(httpPort);
		tomcat.addAdditionalTomcatConnectors(connector);
		return tomcat;
	}

}
