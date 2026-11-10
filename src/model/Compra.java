package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o pedido de um conjunto de jogos
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class Compra {
    private Integer idCompra;
    private Usuario usuario;
    private LocalDateTime dataCompra;
    private Double valorTotal;
    private List<ItemCompra> itens = new ArrayList<>();

    /**
     * Construtor padrão inicializando o valor total 0
     */
    public Compra() {
        this.valorTotal = 0.0;
    }

    /**
     * Construtor para registrar nova compra associada a um usuário
     * 
     * @param usuario Usuário realizador da compra
     */
    public Compra(Usuario usuario) {
        this.usuario = usuario;
        this.dataCompra = LocalDateTime.now();
        this.valorTotal = 0.0;
    }

    /**
     * Construtor completo
     * 
     * @param idCompra Identificador do pedido
     * @param usuario Comprador
     * @param dataCompra Data e hora da transação
     * @param valorTotal Valor total acumulado
     */
    public Compra(Integer idCompra, Usuario usuario, LocalDateTime dataCompra, Double valorTotal) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.dataCompra = dataCompra;
        this.valorTotal = valorTotal;
    }

    /**
     * Adiciona um jogo à lista de itens e recalcula o total da compra
     * 
     * @param jogo a ser adicionado
     */
    public void adicionarItem(Jogo jogo) {
        ItemCompra item = new ItemCompra(jogo, jogo.getPreco());
        this.itens.add(item);
        recalcularTotal();
    }

    /**
     * Remove um item da lista e recalcula o total
     * 
     * @param item a ser removido
     */
    public void removerItem(ItemCompra item) {
        this.itens.remove(item);
        recalcularTotal();
    }

    /**
     * Método para o somatório de valores
     */
    private void recalcularTotal() {
        this.valorTotal = 0.0;
        for (ItemCompra item : itens) {
            this.valorTotal += item.getPrecoPago();
        }
    }

    /**
     * Getters e Setters
     * @return
     */
    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemCompra> getItens() {
        return itens;
    }

    public void setItens(List<ItemCompra> itens) {
        this.itens = itens;
        recalcularTotal();
    }
}
