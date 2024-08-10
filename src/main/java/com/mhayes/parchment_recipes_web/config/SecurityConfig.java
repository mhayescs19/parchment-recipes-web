package com.mhayes.parchment_recipes_web.config;

import com.mhayes.parchment_recipes_web.auth.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/oauth2/**").permitAll(); // login route is accessible to anyone
                    auth.anyRequest().authenticated(); // any other endpoint requires authentication
                })
                .oauth2Login(auth ->
                        auth.successHandler(oAuth2SuccessHandler)) // custom success handler to access OAuth2AuthenticationToken, compare against db and issue JWT
                .build();
    }
}
