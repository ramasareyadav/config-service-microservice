package com.example.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * One application that is both:
 *  - the Spring Cloud Config Server (reads the GitHub config repo), and
 *  - the Eureka Server (service registry, dashboard at http://localhost:8888).
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
