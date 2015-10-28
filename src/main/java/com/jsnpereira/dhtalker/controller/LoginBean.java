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
import com.jsnpereira.dhtalker.attributes.TipoIdentificacao;
import com.jsnpereira.dhtalker.messages.AddMessage;
import com.jsnpereira.dhtalker.persistence.UsuarioJpaController;
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
public class LoginBean implements Serializable{

    private Usuario login, newLogin, oldLogin;
    private UsuarioJpaController loginDAO;
    private Connection connection;
    private FacesContext context;
    private HttpSession session;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        login = new Usuario();
        connection = new Connection();
    }

    public Usuario getLogin() {
        return login;
    }

    public void setLogin(Usuario login) {
        this.login = login;
    }

    public String logon() {
        loginDAO = new UsuarioJpaController(connection.getEntityFactory());
        context = FacesContext.getCurrentInstance();

        newLogin = loginDAO.accessLogon(login);

        if (newLogin != null) {
            System.out.println("Login - Usuário: " + newLogin.getUsuario() + " - Senha: " + newLogin.getSenha() + " - Permissão: " + newLogin.getTipoIdentificacao());
            return accessPermition(newLogin);
        } else {
            System.out.println("senha invalido - " + "Login: " + login.getUsuario() + " - " + login.getSenha());
            (new AddMessage()).informationMessage("Acesso invalido!! tente novamente!", "mensagemLogin");
            return Pages.Destkop.LOGIN;
        }
    }

    private String accessPermition(Usuario login) {
        if (login.getTipoIdentificacao().equals(TipoIdentificacao.ADMIN.toString())) {
            createSession(Sessao.USUARIO, login);
            System.out.println("acessando o principal- admin");
            return Pages.Admin.PRINCIPAL;
        }

        if (login.getTipoIdentificacao().equals(TipoIdentificacao.ATENDIMENTO.toString())) {
            createSession(Sessao.USUARIO, login);
            System.out.println("acessando o principal - Atendente");
            return Pages.Destkop.PRINCIPAL;
        }

        if (login.getTipoIdentificacao().equals(TipoIdentificacao.USUARIO.toString())) {
             (new AddMessage()).informationMessage("Acesso não autorizado!", "mensagemLogin");
            System.out.println("Acesso não autorizado- usuário mobile");
            return Pages.login;
        }

        System.out.println("Retornado o vazio");
        return null;
    }

    private void createSession(String nameAtribute, Usuario login) {
        session = (HttpSession) context.getExternalContext().getSession(false);
        session.setAttribute(nameAtribute, login);
    }

    private boolean checkLogon(Usuario login) {
        if (login != null) {
            return false;
        }

        if (login.getUsuario().isEmpty()) {
            return false;
        }

        if (login.getSenha().isEmpty()) {
            return false;
        }

        if (login.getTipoIdentificacao() != null) {
            return false;
        }
        return true;
    }

    public String cadastroUsuario() {
        return Pages.Mobile.CADASTRO;
    }
}
