package com.nomnom.onnomnom.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GoogleTokenResponse {
    @JsonProperty("access_token")    private String accessToken;
    @JsonProperty("expires_in")      private long expiresIn;
    @JsonProperty("refresh_token")   private String refreshToken;
    @JsonProperty("scope")           private String scope;
    @JsonProperty("token_type")      private String tokenType;
    @JsonProperty("id_token")        private String idToken;
}
