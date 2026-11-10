package model;

/**
 * Classe de modelo para representar as categorias dos jogos
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class Categoria {
	private Integer idCategoria;
	private String nome;
	private String descricao;

	/**
	 * Construtor padrão
	 */
	public Categoria() {
	}

	/**
	 * Construtor para cadastro inicial
	 * 
	 * @param nome      Nome da categoria
	 * @param descricao Breve descrição da categoria
	 */
	public Categoria(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	/**
	 * Construtor completo com ID.
	 * 
	 * @param idCategoria
	 * @param nome
	 * @param descricao
	 */
	public Categoria(Integer idCategoria, String nome, String descricao) {
		this.idCategoria = idCategoria;
		this.nome = nome;
		this.descricao = descricao;
	}

	// Getters e Setters
	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Método toString nome
	 */
	@Override
	public String toString() {
		return nome;
	}
}