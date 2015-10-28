/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.fila;

import com.jsnpereira.dhtalker.entity.Cliente;
import com.jsnpereira.dhtalker.logica.Prioridade;
import java.util.Iterator;

/**
 *
 * @author Jeison
 */
public class GerenciamentoDeFilaFacade {

    private GerenciamentoDeFila fila;
    private Prioridade p = new Prioridade();
    

    public GerenciamentoDeFilaFacade() {
        fila = GerenciamentoDeFila.getInstance();
    }

    public void inserir(Cliente cliente) {
        if (p.verifica(cliente)) {
            fila.inserirPioridade(cliente);
        } else {
            fila.inserir(cliente);
        }
    }

    public Cliente getCliente() {
        if (fila.verificaFilaPrioridadeDisponivel()) {
            return fila.getPrimeiroClientePioridade();
        }
        return fila.getPrimeiroCliente();
    }

    public Cliente chamarCliente() {
        if (fila.verificaFilaPrioridadeDisponivel()) {
            return fila.getChamarClientePrioridade();
        }
        return fila.getChamarCliente();
    }

    public int quantidadeTotal() {
        return fila.quantidade();
    }

    public int posicao(Cliente cliente) {
        if (p.verifica(cliente)) {
            return fila.posicaoPrioridade(cliente);
        }
        return fila.posicao(cliente);
    }

    public boolean removerCliente(Cliente cliente) {
        return fila.remove(cliente);
    }

    public Iterator<Cliente> Clientes() {
        return fila.getListaClientes();
    }
    
    public boolean contains(Cliente cliente){
        return fila.contains(cliente);
    }
    
    public boolean existeFila(){
        return fila.existeFila();
    }
    
    public void removePorUsuario(String usuario){
        fila.removePorUsuario(usuario);
    }
}
