package org.muyie.gateway.security;

import org.apache.commons.lang3.StringUtils;
import org.muyie.gateway.entity.BaseResponseVO;
import org.muyie.gateway.errors.Errors;
import org.muyie.gateway.errors.GatewayAuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class AuthorizeController {

  private TokenProvider tokenProvider;

  public AuthorizeController(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @PostMapping("authorize")
  public Mono<BaseResponseVO> auth(@RequestBody AuthorizeRequest authRequest) {
    if (StringUtils.isNotEmpty(authRequest.getSubject())) {
      String token =
          tokenProvider.createToken(authRequest.getSubject(), authRequest.isRememberMe());
      return Mono.just(BaseResponseVO.ok(new AuthorizeResponse(token)));
    }
    return Mono.error(
        new GatewayAuthenticationException(Errors.BAD_REQUEST, "Bad Request: subject is empty."));
  }

}
