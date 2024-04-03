package com.rinhajava.app.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HelloHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String resp = "<h1>justin case</h1>";

        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, resp.getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(resp.getBytes());
        output.flush();
        exchange.close();
    }
}
