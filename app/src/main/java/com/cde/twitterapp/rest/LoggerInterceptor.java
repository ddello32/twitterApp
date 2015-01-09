package com.cde.twitterapp.rest;

import android.util.Log;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by dello on 06/01/15.
 */
public class LoggerInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Log.d("Request", request.getHeaders().toString());
        Log.d("Body", new String(body, "UTF-8"));
        ClientHttpResponse response = execution.execute(request, body);
        Log.d("Response", response.getHeaders().toString());
        Log.d("Status", ""+response.getRawStatusCode());
        Log.d("Status", response.getStatusText());
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
        String s = "";
        String readed = null;
        while ((readed = reader.readLine()) != null) {
            s += readed + "\n";
        }
        Log.d("Body", s);
        //reader.reset();
        reader.close();
        return execution.execute(request, body);
    }
}
