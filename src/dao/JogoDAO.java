package dao;

import model.Categoria;
import model.Jogo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas operações de persistência e consultas do Jogo no MySQL
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class JogoDAO {

    /**
     * Insere um novo jogo no catálogo
     * 
     * @param jogo Instância do jogo a ser gravado
     * @throws SQLException Caso ocorra erro de execução SQL
     */
    public void cadastrar(Jogo jogo) throws SQLException {
        String sql = "INSERT INTO jogo (titulo, descricao, preco, data_lancamento, id_categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, jogo.getTitulo());
            stmt.setString(2, jogo.getDescricao());
            stmt.setDouble(3, jogo.getPreco());
            stmt.setDate(4, Date.valueOf(jogo.getDataLancamento()));
            stmt.setInt(5, jogo.getCategoria().getIdCategoria());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    jogo.setIdJogo(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Retorna o catálogo completo de jogos trazendo os dados de sua Categoria via JOIN
     * 
     * @return Lista com todos os jogos
     * @throws SQLException Caso ocorra erro no banco de dados
     */
    public List<Jogo> listarTodos() throws SQLException {
        List<Jogo> lista = new ArrayList<>();
        String sql = "SELECT j.*, c.nome AS cat_nome, c.descricao AS cat_desc " +
                     "FROM jogo j INNER JOIN categoria c ON j.id_categoria = c.id_categoria " +
                     "ORDER BY j.titulo";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(instanciarJogo(rs));
            }
        }
        return lista;
    }

    /**
     * CONSULTA PERSONALIZADA 1: Filtra os jogos por título, categoria e valor máximo de preço
     * 
     * @param titulo Trecho do título buscado (pode ser nulo ou vazio)
     * @param idCategoria ID da categoria selecionada (pode ser nulo)
     * @param precoMax Preço máximo estipulado (pode ser nulo)
     * @return Lista de jogos que atendem aos filtros combinados
     * @throws SQLException Caso ocorra erro SQL
     */
    public List<Jogo> buscarPorFiltro(String titulo, Integer idCategoria, Double precoMax) throws SQLException {
        List<Jogo> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT j.*, c.nome AS cat_nome, c.descricao AS cat_desc " +
            "FROM jogo j INNER JOIN categoria c ON j.id_categoria = c.id_categoria WHERE 1=1 "
        );

        if (titulo != null && !titulo.trim().isEmpty()) {
            sql.append("AND j.titulo LIKE ? ");
        }
        if (idCategoria != null && idCategoria > 0) {
            sql.append("AND j.id_categoria = ? ");
        }
        if (precoMax != null && precoMax >= 0) {
            sql.append("AND j.preco <= ? ");
        }

        sql.append("ORDER BY j.preco ASC");

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (titulo != null && !titulo.trim().isEmpty()) {
                stmt.setString(paramIndex++, "%" + titulo + "%");
            }
            if (idCategoria != null && idCategoria > 0) {
                stmt.setInt(paramIndex++, idCategoria);
            }
            if (precoMax != null && precoMax >= 0) {
                stmt.setDouble(paramIndex++, precoMax);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(instanciarJogo(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Atualiza as informações de um jogo no banco de dados.
     * 
     * @param jogo Instância com dados atualizados
     * @throws SQLException Caso ocorra erro de atualização
     */
    public void atualizar(Jogo jogo) throws SQLException {
        String sql = "UPDATE jogo SET titulo = ?, descricao = ?, preco = ?, data_lancamento = ?, id_categoria = ? WHERE id_jogo = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jogo.getTitulo());
            stmt.setString(2, jogo.getDescricao());
            stmt.setDouble(3, jogo.getPreco());
            stmt.setDate(4, Date.valueOf(jogo.getDataLancamento()));
            stmt.setInt(5, jogo.getCategoria().getIdCategoria());
            stmt.setInt(6, jogo.getIdJogo());
            stmt.executeUpdate();
        }
    }

    /**
     * Remove um jogo do catálogo
     * 
     * @param idJogo Identificador único do jogo
     * @throws SQLException Caso o jogo esteja vinculado a compras ou wishlist
     */
    public void excluir(int idJogo) throws SQLException {
        String sql = "DELETE FROM jogo WHERE id_jogo = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idJogo);
            stmt.executeUpdate();
        }
    }

    private Jogo instanciarJogo(ResultSet rs) throws SQLException {
        Categoria c = new Categoria(
            rs.getInt("id_categoria"),
            rs.getString("cat_nome"),
            rs.getString("cat_desc")
        );
        Date date = rs.getDate("data_lancamento");
        return new Jogo(
            rs.getInt("id_jogo"),
            rs.getString("titulo"),
            rs.getString("descricao"),
            rs.getDouble("preco"),
            date != null ? date.toLocalDate() : null,
            c
        );
    }
}