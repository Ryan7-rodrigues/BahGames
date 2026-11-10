package model;

import java.time.LocalDateTime;

/**
 * Classe que relaciona os jogos incluídos na Lista de Desejos do Usuário
 * 
 * @author Ryan
 * @version 1.0
 * @since 10/11/2026
 */
public class Wishlist {
    private Usuario usuario;
    private Jogo jogo;
    private LocalDateTime dataAdicao;

    /**
     * Construtor padrão
     */
    public Wishlist() {
    }

    /**
     * Construtor simplificado registrando a hora atual
     * 
     * @param usuario dono da wishlist
     * @param jogo desejado
     */
    public Wishlist(Usuario usuario, Jogo jogo) {
        this.usuario = usuario;
        this.jogo = jogo;
        this.dataAdicao = LocalDateTime.now();
    }

    /**
     * Construtor completo
     * 
     * @param usuario Usuário
     * @param jogo Jogo
     * @param dataAdicao Data e hora em que foi incluído na lista
     */
    public Wishlist(Usuario usuario, Jogo jogo, LocalDateTime dataAdicao) {
        this.usuario = usuario;
        this.jogo = jogo;
        this.dataAdicao = dataAdicao;
    }

    /**
     * Getters e Setters
     * @return
     */
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

    public LocalDateTime getDataAdicao() {
        return dataAdicao;
    }

    public void setDataAdicao(LocalDateTime dataAdicao) {
        this.dataAdicao = dataAdicao;
    }
}