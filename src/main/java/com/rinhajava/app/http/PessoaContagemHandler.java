package com.rinhajava.app.http;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.rinhajava.app.utils.Query;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class PessoaContagemHandler implements HttpHandler {

    Connection conn;

    public PessoaContagemHandler(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(Query.COUNT_PESSOAS.getSql());

            if(!res.next()) {
                throw new SQLException("Erro ao executar a query de contagem de pessoas");
            }

            String count = String.valueOf(res.getObject(1));

            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, count.length());

            exchange.getResponseBody().write(count.getBytes());
            exchange.close();
        } catch (Exception e) {
            System.out.println("ERRO CONTAGEM PESSOAS: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
