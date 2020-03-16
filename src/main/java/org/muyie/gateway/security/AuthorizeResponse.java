package org.muyie.gateway.security;

public class AuthorizeResponse {

  private String token;

  public AuthorizeResponse() {}
  
  public AuthorizeResponse(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
