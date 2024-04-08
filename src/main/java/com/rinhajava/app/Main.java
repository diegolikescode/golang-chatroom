package com.rinhajava.app;


import com.rinhajava.app.config.PropertiesLoader;
import com.rinhajava.app.http.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args ) throws IOException {
        if (System.getenv("ENVIRONMENT") == "DOCKER") {
            PropertiesLoader.loadProperties("local.properties");
        } else {
            PropertiesLoader.loadProperties("local.properties");
            // com.rinhajava.app.Main
        }


        Server server = new Server();
        server.startServerHandlers();
        System.out.println("server started");
    }
}
