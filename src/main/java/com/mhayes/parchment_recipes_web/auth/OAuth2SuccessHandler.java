package com.mhayes.parchment_recipes_web.auth;

import com.mhayes.parchment_recipes_web.exception.DuplicateUserException;
import com.mhayes.parchment_recipes_web.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /*
        Extract user details from authentication success
         */
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String uniqueId = user.getAttribute("sub");
        String fName = user.getAttribute("given_name");
        String lName = user.getAttribute("family_name");
        String email = user.getAttribute("email");
        /*
        Create a new user in the DB unless user already exists
         */
        try {
            userService.createUser(uniqueId, email, fName, lName);
        } catch (DuplicateUserException e) {
            System.out.println("user already exists");
        } finally {
            // issue jwt
        }

        // temporary redirect to valid auth
        response.sendRedirect("/api/recipe/");
    }
}
