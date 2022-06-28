package dev.qhc99.gallery_server.config;


import dev.qhc99.gallery_server.repos.HttpCookieOAuth2AuthorizationRequestRepository;
import dev.qhc99.gallery_server.services.AppUserDetailsService;
import dev.qhc99.gallery_server.services.OAuth2DBUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final AppUserDetailsService appUserDetailsService;

  private final OAuth2DBUserService OAuth2DBUserService;

  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  public SecurityConfig(
          AppUserDetailsService appUserDetailsService,
          OAuth2DBUserService OAuth2DBUserService,
          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
    this.appUserDetailsService = appUserDetailsService;
    this.OAuth2DBUserService = OAuth2DBUserService;
    this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
  }


  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter();
  }

  /*
    By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
    the authorization request. But, since our service is stateless, we can't save it in
    the session. We'll save the request in a Base64 encoded cookie instead.
  */
  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder
            .userDetailsService(appUserDetailsService)
            .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .cors()

            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .csrf()
            .disable()
            .formLogin()
            .disable()
            .httpBasic()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())

            .and()
            .authorizeRequests()
            .antMatchers("/",
                    "/error",
                    "/favicon.ico",
                    "/**/*.png",
                    "/**/*.gif",
                    "/**/*.svg",
                    "/**/*.jpg",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js")
            .permitAll()
            .antMatchers("/auth/**", "/oauth2/**")
            .permitAll()
            .anyRequest()
            .authenticated()

            .and()
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")
            .authorizationRequestRepository(cookieAuthorizationRequestRepository())

            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")

            .and()
            .userInfoEndpoint()
            .userService(OAuth2DBUserService)

            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)

            .and()
            .requiresChannel()
            .anyRequest()
            .requiresSecure();

    // Add our custom Token based authentication filter
    http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}
