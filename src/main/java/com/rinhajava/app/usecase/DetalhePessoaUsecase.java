package com.rinhajava.app.usecase;

import com.rinhajava.app.domain.Pessoa;
import com.rinhajava.app.utils.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DetalhePessoaUsecase {

    private Connection conn;

    public DetalhePessoaUsecase(Connection conn) {
        this.conn = conn;
    }

    public Pessoa handleUsecase(String uuid) throws SQLException {
        try {
            PreparedStatement stmt = conn.prepareStatement(Query.SELECT_BY_ID.getSql());
            stmt.setObject(1, UUID.fromString(uuid));

            ResultSet res = stmt.executeQuery();
            if (!res.next()) {
                return null;
            }

            String[] stack = stackPrep(res.getString("stack"));

            //(UUID id, String apelido, String nome, String nascimento, String stack) {
            return new Pessoa(
                    (UUID) res.getObject("id"),
                    res.getString("apelido"),
                    res.getString("nome"),
                    res.getString("nascimento"),
                    stack
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String[] stackPrep(String stack) {
        return stack.split(";");
    }
}
