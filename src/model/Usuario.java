package model;

import java.time.LocalDateTime;

/**
 * Classe que representa o Usuário no sistema Bah Games.
 * 
 * @author Ryan
 * @version 1.0
 * @since 03/11/2026
 */
public class Usuario {
	private Integer idUsuario;
	private String nome;
	private String email;
	private String senha;
	private TipoPerfil tipoPerfil;
	private LocalDateTime dataCadastro;

	/**
	 * Construtor padrão
	 */
	public Usuario() {
	}

	/**
	 * Construtor utilizado para a criação de novos usuários
	 * 
	 * @param nome       Nome do usuário
	 * @param email      e-mail do usuário
	 * @param senha      Senha de acesso
	 * @param tipoPerfil (ADMINISTRADOR ou CLIENTE)
	 */
	public Usuario(String nome, String email, String senha, TipoPerfil tipoPerfil) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.tipoPerfil = tipoPerfil;
	}

	/**
	 * Construtor completo para busca e alteração de cadastros
	 * 
	 * @param idUsuario
	 * @param nome
	 * @param email
	 * @param senha
	 * @param tipoPerfil
	 * @param dataCadastro
	 */
	public Usuario(Integer idUsuario, String nome, String email, String senha, TipoPerfil tipoPerfil,
			LocalDateTime dataCadastro) {
		this.idUsuario = idUsuario;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.tipoPerfil = tipoPerfil;
		this.dataCadastro = dataCadastro;
	}

	// Getters e setters
	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoPerfil getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(TipoPerfil tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	/**
	 * Método toString
	 * 
	 * nome + e-mail
	 */
	@Override
	public String toString() {
		return nome + " (" + email + ")";
	}
}