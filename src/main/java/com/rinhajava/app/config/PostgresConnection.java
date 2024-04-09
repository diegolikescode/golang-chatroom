package com.rinhajava.app.config;

import com.rinhajava.app.utils.exceptions.DatabaseConnectionException;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresConnection {

    public static Connection conn = null;

    public PostgresConnection() {
        try {
            String uri = PropertiesLoader.getProperty("DB_URI");
            String username =  PropertiesLoader.getProperty("DB_USER");
            String password = PropertiesLoader.getProperty("DB_PASS");

            this.conn = DriverManager.getConnection(uri, username, password);

            if (this.conn == null) {
                throw new DatabaseConnectionException("Erro ao conectar com o postgres: " + uri);
            } else {
                System.out.println("Conectado com banco com SUCESSO");
            }

        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + " Falha ao tentar conectar com banco de dados");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            // close conn (conn.close()) ???
            // it would be nice to do conn pooling
        }
    }

    private String pingPostgres() {
        try {
            InetAddress inet = InetAddress.getByName("db");
            System.out.println("PINGING DATABASE SUCCESSFULLY, ADDRESS:: "+inet.getHostAddress());
            return inet.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception error::: " + e.getMessage());
            return "fuck";
        }
    }
}

