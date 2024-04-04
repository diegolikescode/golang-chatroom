package com.rinhajava.app.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rinhajava.app.config.PostgresConnection;
import com.rinhajava.app.domain.Pessoa;
import com.rinhajava.app.domain.dto.PessoaCadastroDB;
import com.rinhajava.app.utils.Query;
import com.rinhajava.app.utils.exceptions.InvalidInputException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class CriaPessoaHandler implements HttpHandler {

    ObjectMapper mapper = new ObjectMapper();
    Connection conn;

    public CriaPessoaHandler(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equals("POST")) {
            try {
                preparePost(exchange);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(422, 0);
                flushRequest(exchange);
            }
        }
    }

    private void preparePost(HttpExchange exchange) throws IOException, SQLException {
        PessoaCadastroDB p = null;
        try {
            p = mapper.readValue(exchange.getRequestBody(), PessoaCadastroDB.class);
            if (!verifyRequestBody(p)) {
                throw new InvalidInputException("Um ou mais campos do input sao invalidos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String id = handlePost(p);
        exchange.getResponseHeaders().set("Location", "/pessoas/"+id);

        if (id == null) {
            exchange.sendResponseHeaders(422, 0);
        } else {
            exchange.sendResponseHeaders(201, 0);
        }
        flushRequest(exchange);

    }

    private String handlePost(PessoaCadastroDB p) throws SQLException {
        Connection conn = PostgresConnection.conn;

        String saveStack = prepareStack(p.getStack());
        String campoQuery = prepareCampoQuery(p, saveStack);

        PreparedStatement prepStmt = conn.prepareStatement(Query.INSERT_PESSOA.getSql());
        prepStmt.setString(1, p.getApelido());
        prepStmt.setString(2, p.getNome());
        prepStmt.setString(3, p.getNascimento());
        prepStmt.setString(4, saveStack);
        prepStmt.setString(5, campoQuery);

        try{
            ResultSet res = prepStmt.executeQuery();
            System.out.println(res);

            if (!res.next()) {
                throw new InvalidInputException(
                        "A query foi executada, mas algo deu errado durante a execucao");
            }

            return res.getString(1);
        } catch (PSQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void flushRequest(HttpExchange exchange) throws IOException {
        OutputStream os = exchange.getResponseBody();
        os.flush();
        exchange.close();
        os.close();
    }

    private boolean verifyRequestBody(PessoaCadastroDB p) {
        System.out.println(p);
        if (p.getNome() == null || p.getApelido() == null) {
            return false;
        }


        return true;
    }

    private String prepareStack(String[] stack) {
        return String.join(";", stack);
    }

    private String prepareCampoQuery(PessoaCadastroDB p, String stack) {
        return String.join(";", p.getApelido(), p.getNome(), stack);
    }
}
