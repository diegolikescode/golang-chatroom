package com.rinhajava.app.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.rinhajava.app.config.PostgresConnection;
import com.rinhajava.app.domain.Pessoa;
import com.rinhajava.app.domain.dto.PessoaCadastroDB;
import com.rinhajava.app.usecase.BuscaTermoUsecase;
import com.rinhajava.app.usecase.DetalhePessoaUsecase;
import com.rinhajava.app.utils.Query;
import com.rinhajava.app.utils.RequestBodyDeserializer;
import com.rinhajava.app.utils.exceptions.InvalidInputException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PessoaHandler implements HttpHandler {

    DetalhePessoaUsecase detalhesUsecase;
    BuscaTermoUsecase buscaTermoUsecase;

    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    Connection conn;

    public PessoaHandler(Connection conn) {
        this.conn = conn;
        this.detalhesUsecase = new DetalhePessoaUsecase(conn);
        this.buscaTermoUsecase = new BuscaTermoUsecase(conn);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("POST")) {
            try {
                preparePost(exchange);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(422, 0);
                flushRequest(exchange);
            }
        } else if (exchange.getRequestMethod().equals("GET")) {
            String url = exchange.getRequestURI().getPath();
            Pattern pattern = Pattern
                    .compile("/pessoas/([0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12})");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                try {
                    String uuid = url.split("/")[url.split("/").length - 1];
                    Pessoa p = detalhesUsecase.handleUsecase(uuid);
                    String json = mapper.writeValueAsString(p);

                    exchange.sendResponseHeaders(200, json.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(json.getBytes());
                } catch (SQLException e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(404, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(404, 0);
                } finally {
                    flushRequest(exchange);
                }
            } else {
                try {
                    String path = exchange.getRequestURI().getQuery();
                    String[] pathSplit = path.split("t=");
                    if(pathSplit.length == 2) {
                        List<Pessoa> pessoas = buscaTermoUsecase.handleUsecase(pathSplit[1]);
                        String json = mapper.writeValueAsString(pessoas);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, json.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(json.getBytes());
                    } else {
                        exchange.sendResponseHeaders(400, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(400, 0);
                } finally {
                    flushRequest(exchange);
                }
            }
        }
    }

    private void preparePost(HttpExchange exchange) throws IOException, SQLException {
        PessoaCadastroDB p = null;
        try {
            module.addDeserializer(String.class, new RequestBodyDeserializer());
            mapper.registerModule(module);

            p = mapper.readValue(exchange.getRequestBody(), PessoaCadastroDB.class);
            if (!verifyRequestBody(p)) {
                throw new InvalidInputException("Um ou mais campos do input sao invalidos");
            }
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(422, 0);
            flushRequest(exchange);
            return;
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
