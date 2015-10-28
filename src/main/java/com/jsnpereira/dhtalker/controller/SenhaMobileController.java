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
import com.jsnpereira.dhtalker.messages.AddMessage;
import com.jsnpereira.dhtalker.persistence.UsuarioJpaController;
import com.jsnpereira.dhtalker.persistence.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.io.Serializable;
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
public class SenhaMobileController implements Serializable{

    private Connection connection;
    private String novaSenha, senhaRepetidas;
    private Usuario logon;
    private HttpSession session;
    private FacesContext context;
    private UsuarioJpaController usuarioDAO;

    /**
     * Creates a new instance of SenhaMobileController
     */
    public SenhaMobileController(){
        connection = new Connection();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(false);
        logon = (Usuario) session.getAttribute(Sessao.USUARIO);
        System.out.println("Usuário: " + logon.getUsuario());
        limpaCampos();
        if (!sessionValidade(logon)) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(Pages.Mobile.LOGIN);
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void limpaCampos() {
        novaSenha = "";
        senhaRepetidas = "";
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getSenhaRepetidas() {
        return senhaRepetidas;
    }

    public void setSenhaRepetidas(String senhaRepetidas) {
        this.senhaRepetidas = senhaRepetidas;
    }

    public Usuario getLogon() {
        return logon;
    }

    public void setLogon(Usuario logon) {
        this.logon = logon;
    }

    private
            boolean sessionValidade(Usuario logon) {
        return logon != null;
    }

    private boolean verificarSenha() {
        System.out.println("Nova senha: " + novaSenha + " = senha repetida: " + senhaRepetidas);
        return novaSenha.equals(senhaRepetidas);
    }

    public String alterarSenhaSalva() {
        System.out.println("Inicia alterar senha!!");
        if (verificarSenha()) {
            System.out.println("Senha repetida com sucesso e senha salva");
            usuarioDAO = new UsuarioJpaController(connection.getEntityFactory());
            try {
                logon.setSenha(novaSenha);
                usuarioDAO.edit(logon);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(SenhaMobileController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(SenhaMobileController.class.getName()).log(Level.SEVERE, null, ex);
            }

            (new AddMessage()).informationMessage("A senha alterada com sucesso!!", "messageMobileAlterLogin");
        } else {
            (new AddMessage()).informationMessage("A nova senha e senha repetida são diferentes, tente novamente!!", "messageMobileAlterLogin");
        }
        limpaCampos();
        return Pages.Mobile.ALTERAR_SENHA;
    }

    public String logout() {
        if (logon != null) {
            session.invalidate();
        }
        return Pages.Mobile.LOGIN;
    }

    public String paginaPrincipal() {
        return Pages.Mobile.PRINCIPAL;
    }
    
    public String iniciaChat() {
        return Pages.Mobile.CADASTRO_ATENDIMENTO;
    }
}
