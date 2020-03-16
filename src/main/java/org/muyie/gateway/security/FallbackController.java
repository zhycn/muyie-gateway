package org.muyie.gateway.security;

import org.muyie.gateway.entity.BaseResponseVO;
import org.muyie.gateway.errors.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

  @GetMapping("fallback")
  public Mono<BaseResponseVO> fallback() {
    return Mono.just(BaseResponseVO.ok(Errors.REQUEST_TIMEOUT));
  }
  
}
