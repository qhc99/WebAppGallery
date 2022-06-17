package dev.qhc99.gallery_server.services;


import dev.qhc99.gallery_server.data_class.DBUser;
import dev.qhc99.gallery_server.data_class.GithubUserJson;
import dev.qhc99.gallery_server.exceptions.ResourceNotFoundException;
import dev.qhc99.gallery_server.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

@Service
public class AppUserDetailsService implements UserDetailsService {

  public static final Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

  UserRepository userRepository;
  UserDBToAppUserService userDBToAppUserService;

  AppUserDetailsService(@Value("${project.repo.owner}") String owner,
                        @Value("${project.repo.name}") String name,
                        UserRepository userRepository,
                        UserDBToAppUserService userDBToAppUserService) {
    this.owner = owner;
    this.repo_name = name;
    this.userRepository = userRepository;
    this.userDBToAppUserService = userDBToAppUserService;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email)
          throws UsernameNotFoundException {
    DBUser DBUser = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new UsernameNotFoundException("User not found with email : " + email)
            );

    return userDBToAppUserService.create(DBUser);
  }

  private final String owner;


  private final String repo_name;

  WebClient webClient = WebClient.create("https://api.github.com");

  @Transactional
  public UserDetails loadUserById(Long id) {

    DBUser DBUser = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
    );

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    if (!DBUser.getEmail().equals("chenq123@mcmaster.ca")) {
      // too many request will be blocked by github temporarily ...
      try {
        var json_array = webClient
                .get()
                .uri(String.format("/repos/%s/%s/stargazers", owner, repo_name))
                .retrieve()
                .bodyToFlux(GithubUserJson.class)
                .timeout(Duration.ofSeconds(3));
        if (Boolean.TRUE.equals(json_array.any(g -> g.getLogin().equals(owner)).block())) {
          authorities.add(new SimpleGrantedAuthority("STAR"));
          logger.info("has star auth");
        }
        else {
          logger.info("does not have star auth");
        }
      } catch (Exception e) {
        logger.error(e.toString());
      }
    }
    else {
      logger.info("add star auth");
      authorities.add(new SimpleGrantedAuthority("STAR"));
    }

    return userDBToAppUserService.create(DBUser,authorities);
  }
}