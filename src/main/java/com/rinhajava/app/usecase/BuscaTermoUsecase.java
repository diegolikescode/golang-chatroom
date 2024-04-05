package com.rinhajava.app.usecase;

import com.rinhajava.app.domain.Pessoa;
import com.rinhajava.app.utils.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuscaTermoUsecase {

    private Connection conn;

    public BuscaTermoUsecase(Connection conn) {
        this.conn = conn;
    }

    public List<Pessoa> handleUsecase(String termo) {
        List<Pessoa> pessoas = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(Query.SELECT_BY_T.getSql());
            stmt.setString(1, termo);

            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                String[] stack = stackPrep(res.getString("stack"));

                pessoas.add(new Pessoa(
                        (UUID) res.getObject("id"),
                        res.getString("apelido"),
                        res.getString("nome"),
                        res.getString("nascimento"),
                        stack
                ));
            }

            return pessoas;
        } catch (Exception e) {
            e.printStackTrace();
            return pessoas;
        }
    }

    private String[] stackPrep(String stack) {
        return stack.split(";");
    }
}
