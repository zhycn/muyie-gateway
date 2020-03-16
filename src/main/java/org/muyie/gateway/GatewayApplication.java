package org.muyie.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class GatewayApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(GatewayApplication.class, args);
  }

}
