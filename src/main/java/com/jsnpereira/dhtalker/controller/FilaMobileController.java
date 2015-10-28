/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.controller;

import com.jsnpereira.dhtalker.attributes.Pages;
import com.jsnpereira.dhtalker.attributes.Sessao;
import com.jsnpereira.dhtalker.entity.Cliente;
import com.jsnpereira.dhtalker.entity.Usuario;
import com.jsnpereira.dhtalker.fila.GerenciamentoDeFilaFacade;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jeison
 */
@ManagedBean
@ViewScoped
public class FilaMobileController {

    private Usuario logon;
    private Cliente cliente;
    private HttpSession sessao;
    private FacesContext context;
    private GerenciamentoDeFilaFacade facade;
    private String posicao;

    /**
     * Creates a new instance of FilaController
     */
    public FilaMobileController() {
        context = FacesContext.getCurrentInstance();
        sessao = (HttpSession) context.getExternalContext().getSession(false);
        logon = (Usuario) sessao.getAttribute(Sessao.USUARIO);

        if (!sessionValidate(logon)) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(Pages.Destkop.LOGIN);
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        cliente = (Cliente) sessao.getAttribute(Sessao.CLIENTE);

        if (sessionValidate(cliente)) {
            System.out.println("Entidade cliente valido");
            facade = new GerenciamentoDeFilaFacade();
        } else {
            System.out.println("Entidade cliente inválida");
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public String getQuantidadeTotalDeFila() {
        return String.valueOf(facade.quantidadeTotal()) + " fila";
    }

    public String desistirFilaDeEspera() {
        if (removerCliente()) {
            return Pages.Mobile.PRINCIPAL;
        } else {
            return Pages.Mobile.FILA_ESPERA;
        }
    }

    private boolean removerCliente() {
        if (facade.removerCliente(cliente)) {
            sessao.removeAttribute(Sessao.CLIENTE);
            return true;
        } else {
            return false;
        }
    }

    private boolean sessionValidate(Object o) {
        return o != null;
    }

    public String logoutUsuario() {
        if (sessao != null) {
            sessao.invalidate();
        }
        return Pages.Mobile.LOGIN;
    }

    public String alterarSenha() {
        return Pages.Mobile.ALTERAR_SENHA;
    }

    public String principal() {
        return Pages.Mobile.PRINCIPAL;
    }

    public void atualizaPosicaoCliente() {
        this.posicao = facade.posicao(cliente) + "º fila";
        System.out.println("Posicao do cliente: " + posicao);
    }

}
