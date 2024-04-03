package com.rinhajava.app.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rinhajava.app.domain.Pessoa;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CriaPessoaHandler implements HttpHandler {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equals("POST")) {
            String resp = "aha";
            exchange.sendResponseHeaders(200, 0);
            OutputStream output = exchange.getResponseBody();
            output.write(resp.getBytes());
            output.flush();
            exchange.close();
        }

        String resp = "aha";
        exchange.sendResponseHeaders(200, 0);
        OutputStream output = exchange.getResponseBody();
        output.write(resp.getBytes());
        output.flush();
        exchange.close();
    }
/*
    private void preparePost(HttpExchange exchange) throws IOException {
        Pessoa p = mapper.readValue(exchange.getRequestBody(), Pessoa.class);
        String id = handlePost(p);

        exchange.getResponseHeaders().set("Location", "/pessoas/"+id);

    }

    private String handlePost(Pessoa p) {
        // salva pessoa no banco e retorna o ID;
        return "sample-uuid";
    }

    private void flushRequest() {

    }
 */
}
