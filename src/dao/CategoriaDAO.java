package dao;

import model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas operações CRUD da Categoria no MySQL
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class CategoriaDAO {

	public void cadastrar(Categoria categoria) throws SQLException {
		String sql = "INSERT INTO categoria (nome, descricao) VALUES (?, ?)";
		try (Connection conn = ConexaoBD.getConexao();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, categoria.getNome());
			stmt.setString(2, categoria.getDescricao());
			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					categoria.setIdCategoria(rs.getInt(1));
				}
			}
		}
	}

	public List<Categoria> listarTodas() throws SQLException {
		List<Categoria> lista = new ArrayList<>();
		String sql = "SELECT * FROM categoria ORDER BY nome";
		try (Connection conn = ConexaoBD.getConexao();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Categoria c = new Categoria(rs.getInt("id_categoria"), rs.getString("nome"), rs.getString("descricao"));
				lista.add(c);
			}
		}
		return lista;
	}

	public void atualizar(Categoria categoria) throws SQLException {
		String sql = "UPDATE categoria SET nome = ?, descricao = ? WHERE id_categoria = ?";
		try (Connection conn = ConexaoBD.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, categoria.getNome());
			stmt.setString(2, categoria.getDescricao());
			stmt.setInt(3, categoria.getIdCategoria());
			stmt.executeUpdate();
		}
	}

	public void excluir(int idCategoria) throws SQLException {
		String sql = "DELETE FROM categoria WHERE id_categoria = ?";
		try (Connection conn = ConexaoBD.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idCategoria);
			stmt.executeUpdate();
		}
	}
}