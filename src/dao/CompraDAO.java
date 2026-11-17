package dao;

import model.Categoria;
import model.Compra;
import model.Jogo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelo registro de compras  e pela consulta da biblioteca do usuário
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class CompraDAO {

    /**
     * Registra a compra direta de um jogo para um usuário no banco de dados
     * 
     * @param compra Objeto Compra contendo usuário, jogo e valor pago
     */
    public void registrarCompra(Compra compra) throws SQLException {
        String sql = "INSERT INTO compra (id_usuario, id_jogo, valor_pago) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, compra.getUsuario().getIdUsuario());
            stmt.setInt(2, compra.getJogo().getIdJogo());
            stmt.setDouble(3, compra.getValorPago());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    compra.setIdCompra(rs.getInt(1));
                }
            }
        }
    }

    /**
     * verifica se o usuário já possui o jogo na biblioteca para evitar compras duplicada
     * 
     * @param idUsuario id do usuário
     * @param idJogo id do jogo
     * @return true se o usuário já comprou o jogo, false caso não tenha comprado
     */
    public boolean usuarioJaPossuiJogo(int idUsuario, int idJogo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM compra WHERE id_usuario = ? AND id_jogo = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idJogo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * CONSULTA PERSONALIZADA: Lista todos os jogos adquiridos por um usuário
     * 
     * @param idUsuario id do cliente
     * @return Lista de jogos pertencentes à biblioteca do usuário
     */
    public List<Jogo> buscarBibliotecaDoUsuario(int idUsuario) throws SQLException {
        List<Jogo> biblioteca = new ArrayList<>();
        String sql = "SELECT j.*, c.nome AS cat_nome, c.descricao AS cat_desc " +
                     "FROM compra cp " +
                     "INNER JOIN jogo j ON cp.id_jogo = j.id_jogo " +
                     "INNER JOIN categoria c ON j.id_categoria = c.id_categoria " +
                     "WHERE cp.id_usuario = ? ORDER BY cp.data_compra DESC";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Categoria c = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("cat_nome"),
                        rs.getString("cat_desc")
                    );
                    Date date = rs.getDate("data_lancamento");
                    Jogo j = new Jogo(
                        rs.getInt("id_jogo"),
                        rs.getString("titulo"),
                        rs.getString("descricao"),
                        rs.getDouble("preco"),
                        date != null ? date.toLocalDate() : null,
                        c
                    );
                    biblioteca.add(j);
                }
            }
        }
        return biblioteca;
    }
}