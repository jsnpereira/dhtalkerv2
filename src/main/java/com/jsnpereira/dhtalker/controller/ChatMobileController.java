/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.controller;

import com.jsnpereira.dhtalker.attributes.Pages;
import com.jsnpereira.dhtalker.attributes.Sessao;
import com.jsnpereira.dhtalker.configuration.Connection;
import com.jsnpereira.dhtalker.entity.Cliente;
import com.jsnpereira.dhtalker.entity.Usuario;
import com.jsnpereira.dhtalker.fila.GerenciamentoDeFilaFacade;
import com.jsnpereira.dhtalker.persistence.UsuarioJpaController;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jeison
 */
@ManagedBean
@SessionScoped
public class ChatMobileController {

    private Usuario usuario;
    private Cliente cliente;
    private UsuarioJpaController usuarioDAO;
    private Connection connection;
    private FacesContext context;
    private HttpSession sessao;
    private GerenciamentoDeFilaFacade facade;

    public ChatMobileController() {
        context = FacesContext.getCurrentInstance();
        sessao = (HttpSession) context.getExternalContext().getSession(false);
        usuario = (Usuario) sessao.getAttribute(Sessao.USUARIO);

        if (!sessaoValidate(usuario)) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(Pages.Mobile.LOGIN);
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        cliente = (Cliente) sessao.getAttribute(Sessao.CLIENTE);
        cliente.setUsuario(usuario);
        facade = new GerenciamentoDeFilaFacade();
    }

    private boolean sessaoValidate(Usuario usuario) {
        return usuario != null;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void mensagem() {

    }

    public String cancelar() {
        if (facade.contains(cliente)) {
            facade.removerCliente(cliente);
        }
        System.out.println("Mobile: Sair do sala do chat");
        return Pages.Mobile.PRINCIPAL;
    }

}
