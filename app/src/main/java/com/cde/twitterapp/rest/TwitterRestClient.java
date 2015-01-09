package com.cde.twitterapp.rest;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.RequiresAuthentication;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * Created by dello on 05/01/15.
 */
@Rest(rootUrl = "https://api.twitter.com/1.1", converters = { MappingJackson2HttpMessageConverter.class },  interceptors = {LoggerInterceptor.class})
public interface TwitterRestClient extends RestClientErrorHandling {
    @Get("/statuses/user_timeline?user_id={id}&since_id={since_id}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(long id, long since_id);

    @Get("/statuses/user_timeline?screen_name={screenName}&since_id={since_id}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(String screenName, long since_id);

    @Get("/statuses/user_timeline?user_id={id}&since_id={since_id}&count={count}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(long id, long since_id, int count);

    @Get("/statuses/user_timeline?screen_name={screenName}&since_id={since_id}&count={count}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(String screenName, long since_id, int count);

    @Get("/statuses/user_timeline?user_id={id}&since_id={since_id}&count={count}&max_id={max_id}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(long id, long since_id, int count, long max_id);

    @Get("/statuses/user_timeline?screen_name={screenName}&since_id={since_id}&count={count}&max_id={max_id}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(String screenName, long since_id, int count, long max_id);

    @Get("/users/show?screen_name={screenName}")
    @RequiresAuthentication
    UserRestEntity getUser(String screenName);

    @Get("/users/show?user_id={userId}")
    @RequiresAuthentication
    UserRestEntity getUser(long userId);

    /**
     *
     * @param authentication Needs to extend or be a initialized TwitterApiAuth... Can't change the signature due to AA and Rest
     */
    void setAuthentication(HttpAuthentication authentication);
}
