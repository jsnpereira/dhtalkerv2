/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.attributes;

/**
 *
 * @author Jeison
 */
public enum Emergencia {

    SAMU(192),
    POLICIA(190);
    private long telefone;

    Emergencia(int telefone) {
        this.telefone = telefone;
    }

    public long getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

}
