package com.cde.twitterapp.rest;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresAuthentication;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;

/**
 * Rest client for twitter's API Oauth2 app authentication.
 * Created by dello on 06/01/15.
 */
@Rest(rootUrl = "https://api.twitter.com", converters = { FormHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class }, interceptors = {LoggerInterceptor.class})
interface TwitterAuthRestClient extends RestClientErrorHandling{
    @Post("/oauth2/token")
    @RequiresHeader("Content-Type")
    @RequiresAuthentication
    public TokenAuthEntity requestToken(MultiValueMap<String, String> map);

    void setHeader(String name, String value);

    void setHttpBasicAuth(String username, String password);


}
