package dev.qhc99.gallery_server.services;

import dev.qhc99.gallery_server.config.AppProperties;
import dev.qhc99.gallery_server.data_class.AppUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

  private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

  private final AppProperties appProperties;

  public TokenService(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  public String createToken(Authentication authentication) {
    AppUser appUser = (AppUser) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

    byte[] keyBytes = Decoders.BASE64.decode(appProperties.getAuth().getTokenSecret());
    var key = Keys.hmacShaKeyFor(keyBytes);

    return Jwts.builder()
            .setSubject(Long.toString(appUser.getId()))
            .setIssuedAt(new Date())
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
  }

  public Long getUserIdFromToken(String token) {

    var s = appProperties.getAuth().getTokenSecret();
    Key k = new SecretKeySpec(s.getBytes(),"base64");
    Claims claims = Jwts.parserBuilder()
            .setSigningKey(k)
            .build()
            .parseClaimsJws(token)
            .getBody();

    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(String authToken) {
    try {
      var s = appProperties.getAuth().getTokenSecret();
      Key k = new SecretKeySpec(s.getBytes(),"base64");
      Jwts.parserBuilder()
              .setSigningKey(k)
              .build()
              .parseClaimsJws(authToken);
      return true;
    } catch (SecurityException ex) {
      logger.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty.");
    }
    return false;
  }

}
