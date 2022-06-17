package dev.qhc99.gallery_server.services;

import dev.qhc99.gallery_server.data_class.DBUser;
import dev.qhc99.gallery_server.data_class.GithubUserJson;
import dev.qhc99.gallery_server.data_class.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;

@Service
public class UserDBToAppUserService implements Serializable {

  public static final Logger logger = LoggerFactory.getLogger(UserDBToAppUserService.class);


  public AppUser create(DBUser DBUser) {
    return create(DBUser, new ArrayList<>());
  }

  public AppUser create(DBUser DBUser, List<SimpleGrantedAuthority> authorities) {
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));


    return new AppUser(
            DBUser.getId(),
            DBUser.getEmail(),
            DBUser.getPassword(),
            authorities
    );
  }

  public AppUser create(DBUser DBUser, Map<String, Object> attributes) {
    AppUser appUser = create(DBUser);
    appUser.setAttributes(attributes);
    return appUser;
  }
}