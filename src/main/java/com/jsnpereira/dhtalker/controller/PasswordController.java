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
import com.jsnpereira.dhtalker.persistence.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PasswordController {

    private Connection connection;
    private String newPassword, repeatPassword;
    private Usuario logon;
    private HttpSession session;
    private FacesContext context;
    private UsuarioJpaController usuarioDAO;

    /**
     * Creates a new instance of PasswordController
     */
    public PasswordController() {
        connection = new Connection();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(false);
        logon = (Usuario) session.getAttribute(Sessao.USUARIO);

        if (!sessionValidate(logon)) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(Pages.login);
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    private boolean sessionValidate(Usuario Logon) {
        return (logon != null);
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public Usuario getLogon() {
        return logon;
    }

    public void setLogon(Usuario logon) {
        this.logon = logon;
    }

    private boolean checkPassword() {
        System.out.println("Nova senha: " + newPassword + " = Senha Repetidas " + repeatPassword);
        return newPassword.equals(repeatPassword);
    }

    public String passwordChanged() {
        System.out.println("Inicio alterar senha");
        if (checkPassword()) {
            System.out.println("Senha repetida com sucesso e salva nova senha");
            usuarioDAO = new UsuarioJpaController(connection.getEntityFactory());
            try {
                logon.setSenha(newPassword);
                usuarioDAO.edit(logon);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(PasswordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PasswordController.class.getName()).log(Level.SEVERE, null, ex);
            }
            (new AddMessage()).informationMessage("Senha alterando com sucesso!!!", "messageAlterLogin");
        } else {
            System.out.println("Senha repetida sem sucesso e enviado uma mensagem");
            (new AddMessage()).informationMessage("As senhas foram incorretas", "messageAlterLogin");
        }
        return pagePassword();
    }

    public String pagePassword() {
        if (logon.getTipoIdentificacao().equals(TipoIdentificacao.ADMIN.toString())) {
            return Pages.Admin.ALTERAR_SENHA;
        } else if (logon.getTipoIdentificacao().equals(TipoIdentificacao.ATENDIMENTO.toString())) {
            return Pages.Destkop.ALTERAR_SENHA;
        } else if (logon.getTipoIdentificacao().equals(TipoIdentificacao.USUARIO.toString())) {
            return Pages.Mobile.ALTERAR_SENHA;
        }
        return null;
    }

    public String mainScreen() {

        if (logon.getTipoIdentificacao().equals(TipoIdentificacao.ADMIN.toString())) {
            return Pages.Admin.PRINCIPAL;
        } else if (logon.getTipoIdentificacao().equals(TipoIdentificacao.ATENDIMENTO.toString())) {
            return Pages.Destkop.PRINCIPAL;
        }
        return null;
    }

    public String registerScreen() {
        return Pages.Admin.CADASTRO;
    }

    public String logoutUsuario() {
        if (session != null) {
            session.invalidate();
        }
        return Pages.login;
    }

    public String salaAtendimento() {
        return Pages.Destkop.SALA_CHAT;
    }
}
