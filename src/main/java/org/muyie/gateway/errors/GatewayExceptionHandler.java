package org.muyie.gateway.errors;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

  public GatewayExceptionHandler(ErrorAttributes errorAttributes,
      ResourceProperties resourceProperties, ErrorProperties errorProperties,
      ApplicationContext applicationContext) {
    super(errorAttributes, resourceProperties, errorProperties, applicationContext);
  }

  @Override
  protected int getHttpStatus(Map<String, Object> errorAttributes) {
    return HttpStatus.OK.value();
  }

  @Override
  protected Map<String, Object> getErrorAttributes(ServerRequest request,
      boolean includeStackTrace) {
    Throwable t = this.getError(request);

    if (t instanceof GatewayAuthenticationException) {
      GatewayAuthenticationException ex = (GatewayAuthenticationException) t;
      return buildMap(ex.getErrors());
    }

    return buildMap(Errors.INTERNAL_SERVER_ERROR);
  }

  private Map<String, Object> buildMap(Errors errors) {
    Map<String, Object> result = new HashMap<>();
    result.put("retCode", errors.getCode());
    result.put("retMsg", errors.getMesssage());
    result.put("retData", new HashMap<>());
    return result;
  }

}
