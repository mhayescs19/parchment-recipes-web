package com.mhayes.parchment_recipes_web.service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.mhayes.parchment_recipes_web.dto.GoogleIdTokenDto;
import com.mhayes.parchment_recipes_web.exception.GoogleIdTokenValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleAuthService {
    @Value("${client.id}")
    String clientId;

    public GoogleIdTokenDto validateToken(String googleIdToken) {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken = null; // verifies the JWT signature, the aud claim, the iss claim, and the exp claim
        try {
            idToken = verifier.verify(googleIdToken);
        } catch (GeneralSecurityException e) {
            throw new GoogleIdTokenValidationException(e);
        } catch (IOException e) {
            throw new GoogleIdTokenValidationException(e);
        }

        if (idToken != null) { //todo handle with an exception instead
            System.out.println("TOKEN VALIDATED");
            Payload payload = idToken.getPayload();

            GoogleIdTokenDto userDetails = GoogleIdTokenDto.builder()
                    .userId(payload.getSubject())
                    .name((String) payload.get("name"))
                    .givenName((String) payload.get("given_name"))
                    .familyName((String) payload.get("family_name"))
                    .email(payload.getEmail())
                    .emailVerified(payload.getEmailVerified())
                    .locale((String) payload.get("locale"))
                    .pictureUrl((String) payload.get("picture"))
                    .build();

            return userDetails;

        } else {
            System.out.println("Invalid ID token.");
            throw new GoogleIdTokenValidationException();
        }
    }

    public String extractGoogleIdToken(Authentication authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        // Get the ID Token from OidcUser
        String idToken = oidcUser.getIdToken().getTokenValue();

        return idToken;
    }
}
