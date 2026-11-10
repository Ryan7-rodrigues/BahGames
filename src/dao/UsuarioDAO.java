package dao;

import model.TipoPerfil;
import model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas operações de acesso a dados do Usuario no MySQL
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class UsuarioDAO {

	/**
	 * Cadastra um novo usuário no banco de dados
	 * 
	 * @param usuario Objeto Usuario a ser persistido
	 * @throws SQLException Caso ocorra erro no MySQL
	 */
	public void cadastrar(Usuario usuario) throws SQLException {
		String sql = "INSERT INTO usuario (nome, email, senha, tipo_perfil) VALUES (?, ?, ?, ?)";
		try (Connection conn = ConexaoBD.getConexao();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getSenha());
			stmt.setString(4, usuario.getTipoPerfil().name());
			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {
				if (rs.next()) {
					usuario.setIdUsuario(rs.getInt(1));
				}
			}
		}
	}

	/**
	 * Autentica um usuário pelo e-mail e senha informados no login
	 * 
	 * @param email E-mail cadastrado
	 * @param senha Senha cadastrada
	 * @return Objeto Usuario logado ou null caso as credenciais sejam inválidas
	 * @throws SQLException Caso ocorra erro de execução SQL
	 */
	public Usuario autenticar(String email, String senha) throws SQLException {
		String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
		try (Connection conn = ConexaoBD.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			stmt.setString(2, senha);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return instanciarUsuario(rs);
				}
			}
		}
		return null;
	}

	/**
	 * Lista todos os usuários cadastrados no sistema
	 * 
	 * @return Lista com os usuários encontrados
	 * @throws SQLException Caso ocorra erro no MySQL
	 */
	public List<Usuario> listarTodos() throws SQLException {
		List<Usuario> lista = new ArrayList<>();
		String sql = "SELECT * FROM usuario ORDER BY nome";
		try (Connection conn = ConexaoBD.getConexao();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				lista.add(instanciarUsuario(rs));
			}
		}
		return lista;
	}

	/**
	 * Atualiza os dados de um usuário existente
	 * 
	 * @param usuario Objeto contendo os dados atualizados
	 * @throws SQLException Caso ocorra erro no MySQL
	 */
	public void atualizar(Usuario usuario) throws SQLException {
		String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, tipo_perfil = ? WHERE id_usuario = ?";
		try (Connection conn = ConexaoBD.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getSenha());
			stmt.setString(4, usuario.getTipoPerfil().name());
			stmt.setInt(5, usuario.getIdUsuario());
			stmt.executeUpdate();
		}
	}

	/**
	 * Remove um usuário do banco de dados pelo seu ID
	 * 
	 * @param idUsuario Identificador único do usuário
	 * @throws SQLException Caso ocorra erro de integridade ou exclusão
	 */
	public void excluir(int idUsuario) throws SQLException {
		String sql = "DELETE FROM usuario WHERE id_usuario = ?";
		try (Connection conn = ConexaoBD.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idUsuario);
			stmt.executeUpdate();
		}
	}

	private Usuario instanciarUsuario(ResultSet rs) throws SQLException {
		Timestamp timestamp = rs.getTimestamp("data_cadastro");
		return new Usuario(rs.getInt("id_usuario"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"),
				TipoPerfil.valueOf(rs.getString("tipo_perfil")),
				timestamp != null ? timestamp.toLocalDateTime() : null);
	}
}