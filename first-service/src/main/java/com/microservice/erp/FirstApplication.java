package com.microservice.erp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@SpringBootApplication
@EnableEurekaClient
public class FirstApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FirstApplication.class, args);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(FirstApplication.class);

	@Override
	public void run(String... args) throws Exception {
		String value = "me-wow";
		LOGGER.error("doStuff encountered an error with value - {}", value);
		LOGGER.warn("doStuff needed to warn - {}", value);
		LOGGER.info("doStuff took input - {}", value);
		LOGGER.debug("doStuff needed to debug - {}", value);
		LOGGER.trace("doStuff needed more information - {}", value);
	}
}
