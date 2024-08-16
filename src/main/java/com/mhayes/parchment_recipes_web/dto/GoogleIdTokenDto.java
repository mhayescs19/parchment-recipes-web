package com.mhayes.parchment_recipes_web.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GoogleIdTokenDto {
    private String userId;
    private String email;
    private boolean emailVerified;
    private String name;
    private String pictureUrl;
    private String locale;
    private String familyName;
    private String givenName;

}
