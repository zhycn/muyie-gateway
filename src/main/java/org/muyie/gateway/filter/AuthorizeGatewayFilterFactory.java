package org.muyie.gateway.filter;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.muyie.gateway.Constants;
import org.muyie.gateway.errors.Errors;
import org.muyie.gateway.errors.GatewayAuthenticationException;
import org.muyie.gateway.security.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

// Authorize=true | false
@Component
public class AuthorizeGatewayFilterFactory
    extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {

  private static final Logger log = LoggerFactory.getLogger(AuthorizeGatewayFilterFactory.class);

  @Autowired
  private TokenProvider tokenProvider;

  public AuthorizeGatewayFilterFactory() {
    super(Config.class);
    log.info("Loaded GatewayFilterFactory [Authorize]");
  }

  @Override
  public List<String> shortcutFieldOrder() {
    return Arrays.asList("enabled");
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      if (!config.isEnabled()) {
        return chain.filter(exchange);
      }

      ServerHttpRequest request = exchange.getRequest();
      String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

      if (StringUtils.startsWith(authHeader, "Bearer ")) {
        String authToken = StringUtils.substring(authHeader, 7);
        String subject = tokenProvider.getSubject(authToken);
        ServerHttpRequest newRequest =
            request.mutate().header(Constants.MOYIA_TOKEN, subject).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
      }

      return Mono.error(new GatewayAuthenticationException(Errors.UNAUTHORIZED,
          "Invalid Authentication:" + authHeader));
    };
  }

  public static class Config {
    // 控制是否开启认证
    private boolean enabled;

    public Config() {}

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }
  }

}
