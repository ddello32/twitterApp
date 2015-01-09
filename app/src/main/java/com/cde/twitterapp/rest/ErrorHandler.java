package com.cde.twitterapp.rest;

import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

/**
 * Created by dello on 09/01/15.
 */
public class ErrorHandler implements RestErrorHandler {
    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        //TODO: Something
        e.printStackTrace();
    }
}