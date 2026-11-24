package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável pela conexão JDBC e inicialização automática do banco bah_games_db.
 * 
 * @author Matheus Ryan
 * @version 1.0
 */
public class ConexaoBD {

    private static final String HOST = "jdbc:mysql://localhost:3306/";
    private static final String PARAMS = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_NAME = "bah_games_db";
    private static final String USER = "root";
    private static final String PASSWORD = "ryan1202";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(HOST + DB_NAME + PARAMS, USER, PASSWORD);
    }

    /**
     * Lê o script bd/bah_games_db.sql e cria o banco/tabelas caso ainda não existam
     */
    public static void inicializarBanco() {
        File scriptSql = new File("bd/bah_games_db.sql");
        if (!scriptSql.exists()) {
            return; // Se o arquivo não estiver na pasta, segue normalmente
        }

        
        try (Connection conn = DriverManager.getConnection(HOST + PARAMS, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(new FileInputStream(scriptSql), StandardCharsets.UTF_8))) {

            StringBuilder comando = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("--") || linha.startsWith("/*")) {
                    continue;
                }
                comando.append(linha).append(" ");
                if (linha.endsWith(";")) {
                    stmt.execute(comando.toString());
                    comando.setLength(0);
                }
            }
        } catch (Exception e) {
            System.err.println("Aviso ao inicializar script SQL: " + e.getMessage());
        }
    }
}