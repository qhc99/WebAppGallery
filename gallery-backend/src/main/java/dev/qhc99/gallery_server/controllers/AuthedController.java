package dev.qhc99.gallery_server.controllers;

import dev.qhc99.gallery_server.data_class.ApiResponse;
import dev.qhc99.gallery_server.data_class.AppUser;
import dev.qhc99.gallery_server.data_class.DBUser;
import dev.qhc99.gallery_server.exceptions.ResourceNotFoundException;
import dev.qhc99.gallery_server.repos.DBUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AuthedController {

  static final Logger logger = LoggerFactory.getLogger(AuthedController.class);
  private final DBUserRepository DBUserRepository;

  public AuthedController(DBUserRepository DBUserRepository) {
    this.DBUserRepository = DBUserRepository;
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public DBUser getCurrentUser(@AuthenticationPrincipal AppUser appUser) {
    return DBUserRepository.findById(appUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", appUser.getId()));
  }

  @GetMapping("/stared")
  @PreAuthorize("hasAuthority('STAR')")
  public ApiResponse getStar() {
    logger.info("star controller passed");
    return new ApiResponse(true, "User stared");
  }
}
