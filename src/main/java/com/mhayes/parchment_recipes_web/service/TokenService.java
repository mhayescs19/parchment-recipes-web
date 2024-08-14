package com.mhayes.parchment_recipes_web.service;

import com.mhayes.parchment_recipes_web.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

// Source: https://github.com/danvega/jwt-symmetric-key/blob/main/src/main/java/dev/danvega/jwt/service/TokenService.java
@Service
public class TokenService {
    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.startsWith("ROLE"))
                .collect(Collectors.joining(","));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        JwtEncoderParameters encodeParams = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), claims);
        return jwtEncoder.encode(encodeParams).getTokenValue();
    }
}
