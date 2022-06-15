package dev.qhc99.gallery_server.controller;

import dev.qhc99.gallery_server.exception.ResourceNotFoundException;
import dev.qhc99.gallery_server.data.User;
import dev.qhc99.gallery_server.db.UserRepository;
import dev.qhc99.gallery_server.data.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@AuthenticationPrincipal AppUser appUser) {
        return userRepository.findById(appUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", appUser.getId()));
    }

    @GetMapping("/stared")
    @PreAuthorize("hasAuthority('stared')")
    public String getStar() {
        return "{authorized: true}";
    }
}