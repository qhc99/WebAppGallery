package dev.qhc99.gallery_server.controllers;

import dev.qhc99.gallery_server.data_class.*;
import dev.qhc99.gallery_server.exceptions.BadRequestException;
import dev.qhc99.gallery_server.data_class.DBUser;
import dev.qhc99.gallery_server.repos.DBUserRepository;
import dev.qhc99.gallery_server.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final DBUserRepository DBUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          DBUserRepository DBUserRepository,
                          PasswordEncoder passwordEncoder,
                          TokenService tokenService){
        this.authenticationManager = authenticationManager;
        this.DBUserRepository = DBUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenService.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(DBUserRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        DBUser DBUser = new DBUser();
        DBUser.setName(signUpRequest.getName());
        DBUser.setEmail(signUpRequest.getEmail());
        DBUser.setPassword(signUpRequest.getPassword());
        DBUser.setProvider(AuthProvider.local);

        DBUser.setPassword(passwordEncoder.encode(DBUser.getPassword()));

        DBUser result = DBUserRepository.save(DBUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }

}
