package dao;


import model.Categoria;
import model.Jogo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar a inclusão, exclusão e consulta de jogos na Wishlist do usuário
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class WishlistDAO {

    /**
     * Adiciona um jogo à lista de desejos do usuário
     * 
     * @param idUsuario Identificador do cliente
     * @param idJogo Identificador do jogo desejado
     * @throws SQLException Caso ocorra erro de inclusão 
     */
    public void adicionar(int idUsuario, int idJogo) throws SQLException {
        String sql = "INSERT INTO wishlist (id_usuario, id_jogo) VALUES (?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idJogo);
            stmt.executeUpdate();
        }
    }

    /**
     * Remove um jogo da lista de desejos
     * 
     * @param idUsuario ID do usuário
     * @param idJogo ID do jogo a ser removido
     * @throws SQLException Caso ocorra erro no MySQL
     */
    public void remover(int idUsuario, int idJogo) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE id_usuario = ? AND id_jogo = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idJogo);
            stmt.executeUpdate();
        }
    }

    /**
     * Retorna a lista de jogos salvos na Wishlist de um usuário específico
     * 
     * @param idUsuario ID do usuário
     * @return Lista com os jogos da Wishlist
     * @throws SQLException Caso ocorra erro SQL
     */
    public List<Jogo> listarPorUsuario(int idUsuario) throws SQLException {
        List<Jogo> lista = new ArrayList<>();
        String sql = "SELECT j.*, c.nome AS cat_nome, c.descricao AS cat_desc " +
                     "FROM wishlist w " +
                     "INNER JOIN jogo j ON w.id_jogo = j.id_jogo " +
                     "INNER JOIN categoria c ON j.id_categoria = c.id_categoria " +
                     "WHERE w.id_usuario = ? ORDER BY w.data_adicao DESC";

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
                    lista.add(j);
                }
            }
        }
        return lista;
    }
}