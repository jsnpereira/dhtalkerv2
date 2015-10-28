/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.controller;

import com.jsnpereira.dhtalker.configuration.Connection;
import com.jsnpereira.dhtalker.attributes.Pages;
import com.jsnpereira.dhtalker.attributes.Sessao;
import com.jsnpereira.dhtalker.entity.Usuario;
import java.io.IOException;
import java.io.Serializable;
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
public class UsuarioMobileControler implements Serializable {

    private HttpSession session;
    private Usuario logon;
    private Connection connection;
    private FacesContext context;

    /**
     * Creates a new instance of UsuarioMobileControler
     */
    public UsuarioMobileControler() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(false);
        logon = (Usuario) session.getAttribute(Sessao.USUARIO);

        if (!sessionValidade(logon)) {
            System.out.println("Sessão invalido!!");
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(Pages.Mobile.LOGIN);
            } catch (IOException ex) {
                System.err.println("Error messge: " + ex.getMessage());
            }
        }

        connection = new Connection();
    }

    public Usuario getLogon() {
        return logon;
    }

    public void setLogon(Usuario logon) {
        this.logon = logon;
    }

    private boolean sessionValidade(Usuario logon) {
        return logon != null;
    }

    public String alterarSenha() {
        System.out.println("Click alterar senha");
        return Pages.Mobile.ALTERAR_SENHA;
    }

    public String logoutUsuario() {
        if (session != null) {
            session.invalidate();
        }
        return Pages.Mobile.LOGIN;
    }

    public String iniciaChat() {
        return Pages.Mobile.CADASTRO_ATENDIMENTO;
    }

    public String cadastroAtendimento() {
        return Pages.Mobile.CADASTRO_ATENDIMENTO;
    }

    public String principal() {
        return Pages.Mobile.PRINCIPAL;
    }

}
