package org.muyie.gateway.security;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.muyie.gateway.Constants;
import org.muyie.gateway.MoxGatewayProperties;
import org.muyie.gateway.errors.Errors;
import org.muyie.gateway.errors.GatewayAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class TokenProvider implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);

  private String secret;

  private long tokenValidityInMilliseconds;

  private long tokenValidityInMillisecondsForRememberMe;

  private MoxGatewayProperties gatewayProperties;

  public TokenProvider(MoxGatewayProperties gatewayProperties) {
    this.gatewayProperties = gatewayProperties;
  }

  @PostConstruct
  public void init() {
    String secret = gatewayProperties.getSecurity().getJwt().getSecret();

    if (StringUtils.isNotEmpty(secret)) {
      log.warn("Warning: the JWT key used is not Base64-encoded. "
          + "We recommend using the `queqiao.security.jwt.base64-secret` key for optimum security.");
      this.secret = secret;
    } else {
      log.debug("Using a Base64-encoded JWT secret key");
      String base64Secret = gatewayProperties.getSecurity().getJwt().getBase64Secret();
      this.secret = new String(Base64Utils.decodeFromString(base64Secret));
    }

    this.tokenValidityInMilliseconds =
        1000 * gatewayProperties.getSecurity().getJwt().getTokenValidityInSeconds();
    this.tokenValidityInMillisecondsForRememberMe =
        1000 * gatewayProperties.getSecurity().getJwt().getTokenValidityInSecondsForRememberMe();
  }

  public String createToken(String subject, boolean rememberMe) {
    long now = (new Date()).getTime();
    Date validity;
    if (rememberMe) {
      validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
    } else {
      validity = new Date(now + this.tokenValidityInMilliseconds);
    }

    return Jwts.builder().setSubject(subject)
        .claim(Constants.AUTHORITIES_KEY, Constants.AUTHORITIES_ROLE)
        .signWith(SignatureAlgorithm.HS512, secret).setExpiration(validity).compact();
  }

  public String getSubject(String token) {
    if (!validateToken(token)) {
      throw new GatewayAuthenticationException(Errors.UNAUTHORIZED,
          "Invalid Token: " + token);
    }

    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      return true;
    } catch (SignatureException | MalformedJwtException e) {
      log.info("Invalid JWT signature.");
      log.trace("Invalid JWT signature.", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token.");
      log.trace("Expired JWT token.", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token.");
      log.trace("Unsupported JWT token.", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT token compact of handler are invalid.");
      log.trace("JWT token compact of handler are invalid.", e);
    }
    return false;
  }

}
