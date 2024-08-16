package com.mhayes.parchment_recipes_web.service;

import com.mhayes.parchment_recipes_web.model.SecurityUser;
import com.mhayes.parchment_recipes_web.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {
    public void setAuthenticatedUser(User user) {
        SecurityUser securityUser = new SecurityUser(user); // create a security user for persisted user

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(securityUser,securityUser.getPassword(),securityUser.getAuthorities()); // note- password is not used, will refactor
        SecurityContextHolder.getContext().setAuthentication(newAuthentication); // update authentication to the new persisted user for this session
    }
}
