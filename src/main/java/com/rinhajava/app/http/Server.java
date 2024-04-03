package com.rinhajava.app.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public HttpServer server;

    public Server() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(6969), 0);
    }

    public void startServerHandlers() {
        this.server.createContext("/", new HelloHandler());
        this.server.createContext("/pessoas", new CriaPessoaHandler());

        server.setExecutor(null);
        server.start();
    }
}

