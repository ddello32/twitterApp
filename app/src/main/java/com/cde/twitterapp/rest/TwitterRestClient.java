package com.cde.twitterapp.rest;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.RequiresAuthentication;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * Created by dello on 05/01/15.
 */
@Rest(rootUrl = "https://api.twitter.com/1.1/statuses/", converters = { MappingJackson2HttpMessageConverter.class },  interceptors = {LoggerInterceptor.class})
public interface TwitterRestClient {
    @Get("/user_timeline?user_id={id}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(long id);

    @Get("/user_timeline?screen_name={screenName}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(String screenName);

    @Get("/user_timeline?user_id={id}&count={count}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(long id, int count);

    @Get("/user_timeline?screen_name={screenName}&count={count}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(String screenName, int count);

    @Get("/user_timeline?user_id={id}&count={count}&max_id={max_id}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(long id, int count, int max_id);

    @Get("/user_timeline?screen_name={screenName}&count={count}&max_id={max_id}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(String screenName, int count, int max_id);

    @Get("/user_timeline?user_id={id}&count={count}&max_id={max_id}&since_id={since_id}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(long id, int count, int max_id, int since_id);

    @Get("/user_timeline?screen_name={screenName}&count={count}&max_id={max_id}&since_id={since_id}")
    @RequiresAuthentication
    List<TweetRestEntity> getUserTimeline(String screenName, int count, int max_id, int since_id);

    void setAuthentication(HttpAuthentication authentication);
}
