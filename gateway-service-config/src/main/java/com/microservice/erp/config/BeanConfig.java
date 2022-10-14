package com.microservice.erp.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Configuration
public class BeanConfig {

    @Bean
    @LoadBalanced /*@LoadBalanced required when Auth Service added into Eureka-Discovery*/
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }

}
