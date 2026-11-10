package dao;

import model.Compra;
import model.ItemCompra;
import model.Jogo;
import model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelo registro de compras (com transação JDBC) e pela
 * visualização da biblioteca do usuário
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class CompraDAO {

	/**
	 * Registra uma nova compra no banco
	 * 
	 * @param compra Objeto Compra contendo os itens e o usuário comprador
	 * @throws SQLException Caso ocorra falha no processo de gravação
	 */
	public void finalizarCompra(Compra compra) throws SQLException {
		String sqlCompra = "INSERT INTO compra (id_usuario, valor_total) VALUES (?, ?)";
		String sqlItem = "INSERT INTO item_compra (id_compra, id_jogo, preco_pago) VALUES (?, ?, ?)";

		Connection conn = null;
		try {
			conn = ConexaoBD.getConexao();
			conn.setAutoCommit(false);

			// Inserção da Compra
			try (PreparedStatement stmtCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
				stmtCompra.setInt(1, compra.getUsuario().getIdUsuario());
				stmtCompra.setDouble(2, compra.getValorTotal());
				stmtCompra.executeUpdate();

				try (ResultSet rs = stmtCompra.getGeneratedKeys()) {
					if (rs.next()) {
						compra.setIdCompra(rs.getInt(1));
					}
				}
			}

			// Inserção dos Itens da Compra
			try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {
				for (ItemCompra item : compra.getItens()) {
					stmtItem.setInt(1, compra.getIdCompra());
					stmtItem.setInt(2, item.getJogo().getIdJogo());
					stmtItem.setDouble(3, item.getPrecoPago());
					stmtItem.addBatch();
				}
				stmtItem.executeBatch();
			}

			conn.commit(); // Efetiva a transação
		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback(); // Cancela caso ocorra erro
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}
	}

	/**
	 * CONSULTA PERSONALIZADA 2: Busca a biblioteca de jogos já adquiridos por um
	 * determinado usuário
	 * 
	 * @param idUsuario Identificador do cliente
	 * @return Lista dos jogos já comprados pelo usuário
	 * @throws SQLException Caso ocorra erro na consulta SQL
	 */
	public List<Jogo> buscarBibliotecaDoUsuario(int idUsuario) throws SQLException {
		List<Jogo> biblioteca = new ArrayList<>();
		String sql = "SELECT DISTINCT j.*, c.nome AS cat_nome, c.descricao AS cat_desc " + "FROM item_compra ic "
				+ "INNER JOIN compra cp ON ic.id_compra = cp.id_compra "
				+ "INNER JOIN jogo j ON ic.id_jogo = j.id_jogo "
				+ "INNER JOIN categoria c ON j.id_categoria = c.id_categoria "
				+ "WHERE cp.id_usuario = ? ORDER BY j.titulo";

		try (Connection conn = ConexaoBD.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idUsuario);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Categoria c = new Categoria(rs.getInt("id_categoria"), rs.getString("cat_nome"),
							rs.getString("cat_desc"));
					Date date = rs.getDate("data_lancamento");
					Jogo j = new Jogo(rs.getInt("id_jogo"), rs.getString("titulo"), rs.getString("descricao"),
							rs.getDouble("preco"), date != null ? date.toLocalDate() : null, c);
					biblioteca.add(j);
				}
			}
		}
		return biblioteca;
	}
}