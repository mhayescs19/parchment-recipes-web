package com.mhayes.parchment_recipes_web.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("made it to success handler");
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String uniqueId = user.getAttribute("sub");
        String name = user.getAttribute("name");
        String fName = user.getAttribute("given_name");
        String lName = user.getAttribute("family_name");
        String email = user.getAttribute("email");

        System.out.println("id:" + uniqueId);
        System.out.println("name:" + name);
        System.out.println("fname:" + fName);
        System.out.println("lname:" + lName);
        System.out.println("email:" + email);

        response.sendRedirect("/api/recipe/");
    }
}
