package org.qhc99.gallery_server.controller;

import org.qhc99.gallery_server.exception.ResourceNotFoundException;
import org.qhc99.gallery_server.data.User;
import org.qhc99.gallery_server.db.UserRepository;
import org.qhc99.gallery_server.data.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@AuthenticationPrincipal AppUser appUser) {
        return userRepository.findById(appUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", appUser.getId()));
    }

    @GetMapping("/user/stared")
    @PreAuthorize("hasAuthority('stared')")
    public String getStar() {
        return "{response: true}";
    }
}
