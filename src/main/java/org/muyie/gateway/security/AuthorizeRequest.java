package org.muyie.gateway.security;

public class AuthorizeRequest {

  private String subject;
  private boolean rememberMe;

  public String getSubject() {
    return subject;
  }

  public boolean isRememberMe() {
    return rememberMe;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setRememberMe(boolean rememberMe) {
    this.rememberMe = rememberMe;
  }

}
