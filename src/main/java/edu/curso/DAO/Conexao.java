package edu.curso.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String HOST = "localhost";
    private static final String PORTA = "3306";
    private static final String BANCO = "biblioteca_db";
    private static final String USUARIO = "root";
    private static final String SENHA = "123456";

    public static Connection getConnection() {

        String url = "jdbc:mariadb://" + HOST + ":" + PORTA + "/" + BANCO +
                "?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true";

        try {
            return DriverManager.getConnection(url, USUARIO, SENHA);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados local!");
            System.err.println("Verifique se o MariaDB está ligado e se a senha está correta.");
            System.err.println("Erro original: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}


