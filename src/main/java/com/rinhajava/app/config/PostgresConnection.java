package com.rinhajava.app.config;

import com.rinhajava.app.utils.exceptions.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {

    public static Connection conn = null;

    public PostgresConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/rinha";
            String username = "postgres", password = "postgres";
            this.conn = DriverManager.getConnection(url, username, password);

            if (this.conn == null) {
                throw new DatabaseConnectionException("Erro ao conectar com o postgres: " + url);
            }

        } catch (ClassNotFoundException e) {
            System.out.println(e.getClass().getName() + " PostgreSQL JDBC driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + " Falha ao tentar conectar com banco de dados");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            // close conn (conn.close()) ???
        }
    }
}
