package com.cde.twitterapp.rest;

import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.HttpAuthentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
/**
 * Created by dello on 06/01/15.
 */
@EBean
public class TwitterApiAuth extends HttpAuthentication {
    //TODO Move part of this to authenticator
    @RootContext
    Context context;
    private String consumerKey;
    private String consumerSecret;
    @RestService
    TwitterAuthRestClient authClient;
    private TokenAuthEntity bearerToken;
    private boolean initialized = false;

    public void initialize(String consumerKey, String consumerSecret) {
        Log.e("TwitterAuth", "initialize");
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        authClient.setHttpBasicAuth(consumerKey, consumerSecret);
        authClient.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        MultiValueMap<String, String> requestArgs = new LinkedMultiValueMap<String, String>();
        requestArgs.add("grant_type", "client_credentials");
        bearerToken =  authClient.requestToken(requestArgs);
        if(bearerToken == null || !bearerToken.tokenType.equals("bearer")) throw new RuntimeException("Authentication Error");
        Log.d("TwitterAuth", "Got token:" + bearerToken.accessToken);
        initialized = true;
    }
    /**
     * @return the value for the 'Authorization' HTTP header.
     */
    @Override
    public String getHeaderValue() {
        byte[] bytes = new byte[0];
        try {
            bytes = bearerToken.accessToken.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format("Bearer %s", bearerToken.accessToken);
    }
    @Override
    public String toString() {
        String s = null;
        try {
            s = String.format("Authorization: %s", getHeaderValue());
        } catch (RuntimeException re) {
            return null;
        }
        return s;
    }

    public boolean isInitialized(){
        return initialized;
    }
}
