package model;

import java.time.LocalDateTime;

/**
 * Classe que representa a compra direta de um jogo por um usuário.
 * 
 * @author Ryan
 * @version 1.0
 * @date 17/11/2026
 */
public class Compra {
    private Integer idCompra;
    private Usuario usuario;
    private Jogo jogo;
    private Double valorPago;
    private LocalDateTime dataCompra;

    /**
     * Construtor padrão
     */
    public Compra() {
    }

    /**
     * Construtor para registrar uma nova compra
     * 
     * @param usuario Usuário que está comprando o jogo
     * @param jogo Jogo adquirido
     */
    public Compra(Usuario usuario, Jogo jogo) {
        this.usuario = usuario;
        this.jogo = jogo;
        this.valorPago = jogo.getPreco();
        this.dataCompra = LocalDateTime.now();
    }

    /**
     * Construtor completo
     * 
     * @param idCompra
     * @param usuario
     * @param jogo 
     * @param valorPago 
     * @param dataCompra
     */
    public Compra(Integer idCompra, Usuario usuario, Jogo jogo, Double valorPago, LocalDateTime dataCompra) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.jogo = jogo;
        this.valorPago = valorPago;
        this.dataCompra = dataCompra;
    }

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

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }
}