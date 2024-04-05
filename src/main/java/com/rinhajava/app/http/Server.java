package com.rinhajava.app.http;

import com.rinhajava.app.config.PostgresConnection;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    PostgresConnection connection = new PostgresConnection();

    public HttpServer server;

    public Server() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(6969), 0);
    }

    public void startServerHandlers() {
        this.server.createContext("/", new HelloHandler());
        this.server.createContext("/pessoas", new PessoaHandler(connection.conn));

        server.setExecutor(null);
        server.start();
    }
}

