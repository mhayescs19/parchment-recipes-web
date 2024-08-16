package com.mhayes.parchment_recipes_web.auth;

import com.mhayes.parchment_recipes_web.dto.GoogleIdTokenDto;
import com.mhayes.parchment_recipes_web.exception.DuplicateUserException;
import com.mhayes.parchment_recipes_web.model.User;
import com.mhayes.parchment_recipes_web.service.GoogleAuthService;
import com.mhayes.parchment_recipes_web.service.SecurityContextService;
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
    @Autowired
    private SecurityContextService securityContextService;
    @Autowired
    GoogleAuthService googleAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("HELLO FROM AUTH SUCCESS CONTROLLER");
        /*
        Extract user details from authentication success
         */
        String googleIdToken = googleAuthService.extractGoogleIdToken(authentication);
        System.out.println(googleIdToken);
        /*
        Validate that Google ID token is authentic
         */
        GoogleIdTokenDto userDetails = googleAuthService.validateToken(googleIdToken);

        if (userDetails != null) {
            OAuth2User user = (OAuth2User) authentication.getPrincipal();

            String uniqueId = user.getAttribute("sub");
            String fName = user.getAttribute("given_name");
            String lName = user.getAttribute("family_name");
            String email = user.getAttribute("email");
        /*
        Create a new user in the DB unless user already exists, add new roles to security context
         */
            try {
                User persistedUser = userService.createUser(uniqueId, email, fName, lName, "READ,ROLE_USER"); // grant basic roles on account creation, bug: current authentication is not updated with these roles
                securityContextService.setAuthenticatedUser(persistedUser); // update security context with proper Authentication
            } catch (DuplicateUserException e) {
                System.out.println("user already exists");
            } finally {
                // issue jwt
            }
        }

        // how to get authentication from persisted user in db
        // how to get token to client from call to sign in with google

        // temporary redirect to valid auth
        //response.sendRedirect("/auth/token");
    }
}
