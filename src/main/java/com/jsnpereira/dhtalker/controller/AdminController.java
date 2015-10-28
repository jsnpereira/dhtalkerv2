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
public class AdminController {

    private HttpSession session;
    private Connection connection;
    private Usuario logon;
    private FacesContext context;

    /**
     * Creates a new instance of AdminController
     */
    public AdminController() {
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(false);
        logon = (Usuario) session.getAttribute(Sessao.USUARIO);

        if (!sessionValidate(logon)) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(Pages.Destkop.LOGIN);
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
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

    public String logoutUsuario() {
        if (session != null) {
            session.invalidate();
        }
        return Pages.login;
    }

    private boolean sessionValidate(Usuario logon) {
        return logon != null;
    }

    public String alterPasswordScreen() {
        return Pages.Admin.ALTERAR_SENHA;
    }

    public String mainScreen() {
        return Pages.Admin.PRINCIPAL;
    }

    public String registerScreen() {
        return Pages.Admin.CADASTRO;
    }

    public String historicoScreen() {
        return Pages.Admin.HISTORICO;
    }

}
