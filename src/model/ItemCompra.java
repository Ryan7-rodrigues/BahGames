package model;

/**
 * Classe que representa um item específico associado a uma compra de jogo
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class ItemCompra {
	private Integer idItem;
	private Jogo jogo;
	private Double precoPago;

	/**
	 * Construtor padrão
	 */
	public ItemCompra() {
	}

	/**
	 * Construtor para inclusão do item
	 * 
	 * @param jogo      Instância do jogo adquirido
	 * @param precoPago Valor pago no momento da aquisição
	 */
	public ItemCompra(Jogo jogo, Double precoPago) {
		this.jogo = jogo;
		this.precoPago = precoPago;
	}

	/**
	 * Construtor completo
	 * 
	 * @param idItem
	 * @param jogo
	 * @param precoPago
	 */
	public ItemCompra(Integer idItem, Jogo jogo, Double precoPago) {
		this.idItem = idItem;
		this.jogo = jogo;
		this.precoPago = precoPago;
	}

	/**
	 * getters e setters
	 * 
	 * @return
	 */
	public Integer getIdItem() {
		return idItem;
	}

	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}

	public Jogo getJogo() {
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

	public Double getPrecoPago() {
		return precoPago;
	}

	public void setPrecoPago(Double precoPago) {
		this.precoPago = precoPago;
	}
}