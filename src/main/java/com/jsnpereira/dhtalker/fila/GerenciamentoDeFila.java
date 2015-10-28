/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.fila;

import com.jsnpereira.dhtalker.entity.Cliente;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Jeison
 */
public class GerenciamentoDeFila {

    private static GerenciamentoDeFila instance = null;
    private static LinkedList<Cliente> fila = new LinkedList<Cliente>();
    private static LinkedList<Cliente> filaPrioridade = new LinkedList<Cliente>();

    public static GerenciamentoDeFila getInstance() {
        if (instance == null) {
            instance = new GerenciamentoDeFila();
        }
        return instance;
    }

    public void inserir(Cliente cliente) {
        fila.addLast(cliente);
    }

    public void inserirPioridade(Cliente cliente) {
        filaPrioridade.addLast(cliente);
    }

    public Cliente getPrimeiroCliente() {
        return fila.peekFirst();
    }

    public Cliente getPrimeiroClientePioridade() {
        return filaPrioridade.peekFirst();
    }

    public Cliente getChamarCliente() {
        return fila.pollFirst();
    }

    public Cliente getChamarClientePrioridade() {
        return filaPrioridade.pollFirst();
    }

    public boolean remove(Cliente cliente) {
        if (filaPrioridade.contains(cliente)) {
            return filaPrioridade.remove(cliente);
        }
        return fila.remove(cliente);
    }

    public int posicaoPrioridade(Cliente cliente) {
        return filaPrioridade.indexOf(cliente) + 1;
    }

    public int posicao(Cliente cliente) {
        int qtd = filaPrioridade.size();
        return ((qtd + fila.indexOf(cliente)) + 1);
    }

    public int quantidade() {
        System.out.println("Quantidade de fila - Cliente: " + fila.size() + " - Prioridade: " + filaPrioridade.size());
        return fila.size() + filaPrioridade.size();
    }

    public boolean verificaFilaPrioridadeDisponivel() {
        return !filaPrioridade.isEmpty();
    }

    public Iterator<Cliente> getListaClientes() {
        LinkedList<Cliente> lista = new LinkedList<Cliente>();
        lista.addAll(filaPrioridade);
        lista.addAll(fila);
        return lista.iterator();
    }

    public boolean contains(Cliente cliente) {
        return fila.contains(cliente) || filaPrioridade.contains(cliente);
    }

    public boolean existeFila() {
        return !fila.isEmpty() || !filaPrioridade.isEmpty();
    }

    public void removePorUsuario(String usuario) {
        for (Cliente c : fila) {
            if (c.getUsuario().toString().equals(usuario)) {
                    fila.remove(c);
            }
        }

        for (Cliente c : filaPrioridade) {
            if (c.getUsuario().toString().equals(usuario)) {
                filaPrioridade.remove(c);
            }
        }
    }
}
