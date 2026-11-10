package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe  responsável pela criação de conexões JDBC com o banco de dados MySQL.
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class ConexaoBD {
    private static final String URL = "jdbc:mysql://localhost:3306/bah_games_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Abre e retorna uma nova conexão com o banco de dados local
     * 
     * @return Objeto Connection pronto para execução de instruções SQL
     * @throws SQLException Caso ocorra erro de conexão ou driver
     */
    public static Connection getConexao() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do MySQL não foi localizado no classpath!", e);
        }
    }
}