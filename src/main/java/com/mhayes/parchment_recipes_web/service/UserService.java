package com.mhayes.parchment_recipes_web.service;

import com.mhayes.parchment_recipes_web.exception.DuplicateUserException;
import com.mhayes.parchment_recipes_web.model.SecurityUser;
import com.mhayes.parchment_recipes_web.model.User;
import com.mhayes.parchment_recipes_web.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("username not found for: " + username));
    }

    public void createUser(String oAuthUniqueId, String email, String fname, String lname, String roles) throws DuplicateUserException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new DuplicateUserException(email);
        }

        User newUser = User.builder()
                .oAuthUniqueId(oAuthUniqueId)
                .email(email)
                .fname(fname)
                .lname(lname)
                .roles(roles)
                .build();

        userRepository.save(newUser);
    }
}
