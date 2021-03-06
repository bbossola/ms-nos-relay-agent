package com.workshare.msnos.relay_agent.http;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class HealthcheckHandler implements HttpHandler {
 
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.add("Content-Type", "application/json" );

        if (exchange.getRequestMethod().equalsIgnoreCase("HEAD")) {
            exchange.sendResponseHeaders ( statusCode() , -1);
        } else {
            byte[] content = statusText().getBytes();
    
            exchange.sendResponseHeaders ( statusCode(), content.length);

            final OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(content);
            responseBody.close();
        }
    }

    private String statusText() {
        return "{\"result\":\"ok\"}";
    }

    private int statusCode() {
        return 200;
    }

}
