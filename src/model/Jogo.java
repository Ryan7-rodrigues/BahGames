package model;

import java.time.LocalDate;

/**
 * Classe que representa um jogo dentro do catálogo da plataforma
 * 
 * @author Ryan
 * @version 1.0
 * @since 03/11/2026
 */
public class Jogo {
	private Integer idJogo;
	private String titulo;
	private String descricao;
	private Double preco;
	private LocalDate dataLancamento;
	private Categoria categoria;

	/**
	 * Construtor padrão
	 */
	public Jogo() {
	}

	/**
	 * Construtor para inclusão de novos jogos.
	 * 
	 * @param titulo         Título do jogo
	 * @param descricao      descrição detalhada
	 * @param preco          Preço de venda em Reais
	 * @param dataLancamento Data de lançamento
	 * @param categoria      Objeto Categoria associado ao jogo
	 */
	public Jogo(String titulo, String descricao, Double preco, LocalDate dataLancamento, Categoria categoria) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.preco = preco;
		this.dataLancamento = dataLancamento;
		this.categoria = categoria;
	}

	/**
	 * Construtor completo com ID.
	 * 
	 * @param idJogo
	 * @param titulo
	 * @param descricao
	 * @param preco
	 * @param dataLancamento
	 * @param categoria
	 */
	public Jogo(Integer idJogo, String titulo, String descricao, Double preco, LocalDate dataLancamento,
			Categoria categoria) {
		this.idJogo = idJogo;
		this.titulo = titulo;
		this.descricao = descricao;
		this.preco = preco;
		this.dataLancamento = dataLancamento;
		this.categoria = categoria;
	}

	// Getters e Setters
	public Integer getIdJogo() {
		return idJogo;
	}

	public void setIdJogo(Integer idJogo) {
		this.idJogo = idJogo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(LocalDate dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	/**
	 * Metódo toString
	 * 
	 * título + preço
	 */
	@Override
	public String toString() {
		return titulo + " - R$ " + String.format("%.2f", preco);
	}
}