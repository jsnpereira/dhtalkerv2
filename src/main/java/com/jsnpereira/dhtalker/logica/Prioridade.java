/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.logica;

import com.jsnpereira.dhtalker.attributes.Emergencia;
import com.jsnpereira.dhtalker.entity.Cliente;

/**
 *
 * @author Jeison
 */
public class Prioridade {

    public boolean verifica(Cliente cliente) {

        if (cliente.getTelefoneDestino() == Emergencia.POLICIA.getTelefone()) {
            System.out.println("Prioridade é policia");
            return true;
        }

        if (cliente.getTelefoneDestino() == Emergencia.SAMU.getTelefone()) {
            System.out.println("Prioridade é policia");
            return true;
        }

        return false;
    }
}
