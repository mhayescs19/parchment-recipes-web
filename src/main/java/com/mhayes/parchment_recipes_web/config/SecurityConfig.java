package com.mhayes.parchment_recipes_web.config;

import com.mhayes.parchment_recipes_web.auth.OAuth2SuccessHandler;
import com.mhayes.parchment_recipes_web.service.UserService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.key}") // get value from .properties
    private String jwtKey;

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Autowired
    private UserService userService;

    @Bean // adds to application context and accessible anywhere in the application through a component
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> // CSRF should be enabled for endpoints using sessions to prevent malicious actions on user's behalf during session
                        csrf.ignoringRequestMatchers("/api/**")) // disable CSRF for APIs only. APIs are secured by JWTs, so CSRF can be disabled
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/token").hasRole("USER"); // accessor must have user role to generate a token
                    auth.requestMatchers("/login", "/oauth2/**").permitAll(); // login route is accessible to anyone
                    auth.anyRequest().authenticated(); // any other endpoint requires authentication
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // create session for login, but not access to APIs
                .userDetailsService(userService) // point to
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .oauth2Login(auth ->
                        auth.successHandler(oAuth2SuccessHandler)) // custom success handler to access OAuth2AuthenticationToken, compare against db and issue JWT
                .build();
    }

    // Source: https://github.com/danvega/jwt-symmetric-key/blob/main/src/main/java/dev/danvega/jwt/config/SecurityConfig.java
    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes())); // jwt decoder is generally a part of the traditional authentication server (component)
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] bytes = jwtKey.getBytes();
        SecretKeySpec originalKey = new SecretKeySpec(bytes,0,bytes.length,"RSA");
        return NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS512).build(); // use the same algorithm as the JWT is created with
    }
}
