package com.microservice.erp.config;

import com.microservice.erp.config.AuthFilter;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Configuration
@PropertySource("classpath:service-names.properties")
public class SpringCloudConfig {

    @Value("${app.first.url}")
    private String firstURL;

    @Value("${app.second.url}")
    private String secondURL;

    @Value("${app.auth.url}")
    private String authURL;

    @Value("${app.auth.validation.url}")
    private String authValidationURL;

    @Bean
    public GlobalFilter globalFilter() {
        return (exchange, chain) -> {
            System.out.println("Pre Global filter");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //TODO:
                System.out.println("Post Global filter");
            }));
        };
    }

    @Bean("CustomAuthFilter")
    public GatewayFilter getAuthFilter(WebClient.Builder builder){
        return AuthFilter.createGatewayFilter(builder, new AuthFilter.Config(authValidationURL));
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder
                        , @Qualifier("CustomAuthFilter") GatewayFilter authFilter
                        , RedisRateLimiter rateLimiter) {
        return builder.routes()
                .route("employeeModuleRateLimit"
                        , r -> r.path("/api/employee/v1/rateLimit/**")
                                .filters(f -> {
                                    //Code breakdown for readability:
                                    return f.requestRateLimiter()
                                            .configure(c -> c.setRateLimiter(rateLimiter));
                                })
                                .uri(firstURL))
                .route("employeeModuleDelayed"
                        , r -> r.path("/api/employee/v1/delayed/**")
                                .filters(f -> {
                                    //Code breakdown for readability:
                                    return f.filter(authFilter)
                                            .circuitBreaker(c -> c.setName("id-employee-circuit")
                                                    .setFallbackUri("/api/employee/v1/errorFallback"));
                                                    //OR Generic messages:
                                                    //.setFallbackUri("/api/fallback/messages/unreachable"));
                                })
                                .uri(firstURL))
                .route("employeeModule"
                        , r -> r.path("/api/employee/v1/**")
                            .filters(f -> f.filter(authFilter))
                            .uri(firstURL))
                .route("consumerModule"
                        , r -> r.path("/api/consumer/**")
                            .uri(secondURL))
                /*.route("authModule"
                        , r -> r.path("/api/auth/**")
                            .uri(authURL))*/
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCircuitBreakerFactory() {
        return (factory) -> factory.configureDefault(id -> {
            //Code breakdown for readability:
            Duration timeout = Duration.ofMillis(2100); //For testing replace with 5100ms.
            return new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(TimeLimiterConfig.custom()
                            .timeoutDuration(timeout)
                            .build())
                    .build();
        });
    }

    @Bean
    public RedisRateLimiter redisRateLimiter(){
        /**
         * defaultReplenishRate: Default number of request an user can do in a second without dropping any request.
         * defaultBurstCapacity: Maximum number of request an user allowed to do in a second.
         */
        return new RedisRateLimiter(2, 3);
    }

    @Bean
    public KeyResolver userKeyResolver(){
        /**
         * RedisRateLimiter need a KeyResolver, without this limiter will not work.
         */
        return exchange -> Mono.just("rate-limiter-key");
    }

}