package dev.qhc99.gallery_server.service;

import dev.qhc99.gallery_server.data.GithubUserJson;
import dev.qhc99.gallery_server.data.User;
import dev.qhc99.gallery_server.data.AppUser;
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
public class UserToAppUserService implements Serializable {

  public static final Logger logger = LoggerFactory.getLogger(UserToAppUserService.class);

  private final String owner;


  private final String repo_name;

  UserToAppUserService(
          @Value("${project.repo.owner}") String owner,
          @Value("${project.repo.name}") String name) {
    this.owner = owner;
    this.repo_name = name;
  }

  WebClient webClient = WebClient.create("https://api.github.com");

  public AppUser create(User user) {
    List<GrantedAuthority> authorities = new ArrayList<>(List.of(
            new SimpleGrantedAuthority("ROLE_USER")));

    try {
      var json_array = webClient
              .get()
              .uri(String.format("/repos/%s/%s/stargazers", owner, repo_name))
              .retrieve()
              .bodyToFlux(GithubUserJson.class)
              .timeout(Duration.ofSeconds(3));
      if (Boolean.TRUE.equals(json_array.any(g -> g.getLogin().equals(owner)).block())) {
        authorities.add(new SimpleGrantedAuthority("stared"));
        logger.info("has star auth");
      }
    } catch (Exception e) {
      logger.error(e.toString());
    }
    return new AppUser(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            authorities
    );
  }

  public AppUser create(User user, Map<String, Object> attributes) {
    AppUser appUser = create(user);
    appUser.setAttributes(attributes);
    return appUser;
  }
}