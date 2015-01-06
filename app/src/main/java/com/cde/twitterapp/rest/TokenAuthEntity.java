package com.cde.twitterapp.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by dello on 06/01/15.
 */
class TokenAuthEntity implements Serializable {
    @JsonProperty("token_type")
    String tokenType;
    @JsonProperty("access_token")
    String accessToken;
}
