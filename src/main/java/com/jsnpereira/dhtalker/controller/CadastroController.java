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
public class CadastroController {

    private HttpSession session;
    private Connection connection;
    private String repeatPassword;
    private FacesContext context;
    private Usuario usuario, logon;
    private UsuarioJpaController usuarioDAO;
    private int typeIdentity;

    public CadastroController() {
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
        iniciaCampos();
    }

    private void iniciaCampos() {
        usuario = new Usuario();
    }

    public Usuario getLogon() {
        return logon;
    }

    public void setLogon(Usuario logon) {
        this.logon = logon;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setIdentificacao(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getTypeIdentity() {
        return typeIdentity;
    }

    public void setTypeIdentity(int typeIdentity) {
        this.typeIdentity = typeIdentity;
    }

    public String saveData() {
        usuarioDAO = new UsuarioJpaController(connection.getEntityFactory());
        if (!usuarioDAO.checkUsuario(usuario)) {
            (new AddMessage()).informationMessage("O usuário " + usuario.getUsuario() + " já existe, por favor! tente outro.",
                    "mensagemCadastro");
        } else if (checkPassword()) {

            usuario.setId(usuarioDAO.gerarIDusuario());
            usuario.setTipoIdentificacao(checkTypeIdentity(typeIdentity));
            System.err.println("Identificacao: " + usuario.getNome() + " - " + usuario.getEmail() + " - " + usuario.getId() + " - " + usuario.getUsuario());
            try {
                usuarioDAO.create(usuario);
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            (new AddMessage()).informationMessage("Cadastro com sucesso!!",
                    "mensagemCadastro");
        } else {
            (new AddMessage()).informationMessage(
                    "s Senha reptetida foram incorreta, tente novamente!",
                    "mensagemCadastro");
            return Pages.Admin.CADASTRO;
        }
        iniciaCampos();
        return Pages.Admin.CADASTRO;
    }

    private boolean checkPassword() {
        return repeatPassword.equals(usuario.getSenha());
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

    private String checkTypeIdentity(int id) {
        switch (id) {
            case 1:
                return TipoIdentificacao.ADMIN.toString();
            case 2:
                return TipoIdentificacao.USUARIO.toString();
            case 3:
                return TipoIdentificacao.ATENDIMENTO.toString();
        }
        return null;
    }

    public void cleanRegister() {
        iniciaCampos();
    }
}
