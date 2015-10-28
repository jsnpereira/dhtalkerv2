/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.entity;

/**
 *
 * @author Jeison
 */
public class Cliente {

    private Usuario usuario;
    private String nomeDestino;
    private long telefoneDestino;
    private String sala;

    public Cliente() {

    }

    public Cliente(Usuario usuario, String nomeDestino, long telefoneDestino) {
        this.usuario = usuario;
        this.nomeDestino = nomeDestino;
        this.telefoneDestino = telefoneDestino;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNomeDestino() {
        return nomeDestino;
    }

    public void setNomeDestino(String nomeDestino) {
        this.nomeDestino = nomeDestino;
    }

    public long getTelefoneDestino() {
        return telefoneDestino;
    }

    public void setTelefoneDestino(long telefoneDestino) {
        this.telefoneDestino = telefoneDestino;
    }

    public String getCanal() {
        return sala;
    }

    public void setCanal(String canal) {
        this.sala = canal;
    }
    
    

}
