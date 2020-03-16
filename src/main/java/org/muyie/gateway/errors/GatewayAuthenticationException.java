package org.muyie.gateway.errors;

public class GatewayAuthenticationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private Errors errors;

  public GatewayAuthenticationException(Errors errors, String message, Throwable cause) {
    super(message, cause);
    this.errors = errors;
  }

  public GatewayAuthenticationException(Errors errors, String message) {
    super(message);
    this.errors = errors;
  }

  public GatewayAuthenticationException(Errors errors, Throwable cause) {
    super(cause);
    this.errors = errors;
  }

  public Errors getErrors() {
    return errors;
  }

}
