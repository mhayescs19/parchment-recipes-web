package com.mhayes.parchment_recipes_web.controller;

import com.mhayes.parchment_recipes_web.dto.GoogleIdTokenDto;
import com.mhayes.parchment_recipes_web.dto.JwtDto;
import com.mhayes.parchment_recipes_web.exception.DuplicateUserException;
import com.mhayes.parchment_recipes_web.model.User;
import com.mhayes.parchment_recipes_web.service.GoogleAuthService;
import com.mhayes.parchment_recipes_web.service.SecurityContextService;
import com.mhayes.parchment_recipes_web.service.TokenService;
import com.mhayes.parchment_recipes_web.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private GoogleAuthService googleAuthService;
    @Autowired
    private SecurityContextService securityContextService;

    @GetMapping("/token")
    public String token(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/googleIdToken")
    public ResponseEntity validateIdToken(@RequestBody JwtDto googleIdToken, HttpServletResponse response) throws IOException {
        //String googleIdToken = googleAuthService.extractGoogleIdToken(authentication);
        GoogleIdTokenDto userDetails = googleAuthService.validateToken(googleIdToken.getJwt());

        if (userDetails != null) {
            try {
                User persistedUser = userService.createUser(userDetails.getUserId(), userDetails.getEmail(), userDetails.getGivenName(), userDetails.getFamilyName(), "READ,ROLE_USER"); // grant basic roles on account creation, bug: current authentication is not updated with these roles
                securityContextService.setAuthenticatedUser(persistedUser); // update security context with proper Authentication
            } catch (DuplicateUserException e) {
                System.out.println("user already exists");
            } finally {
                // issue jwt
            }

            response.sendRedirect("/auth/token");
        }

        return (ResponseEntity) ResponseEntity.status(HttpStatus.OK);
    }
}
