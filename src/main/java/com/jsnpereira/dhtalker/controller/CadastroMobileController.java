/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.controller;

import com.jsnpereira.dhtalker.configuration.Connection;
import com.jsnpereira.dhtalker.attributes.Pages;
import com.jsnpereira.dhtalker.entity.Usuario;
import com.jsnpereira.dhtalker.attributes.TipoIdentificacao;
import com.jsnpereira.dhtalker.messages.AddMessage;
import com.jsnpereira.dhtalker.persistence.UsuarioJpaController;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jeison
 */
@ManagedBean
@RequestScoped
public class CadastroMobileController {

    private Usuario usuario;
    private UsuarioJpaController usuarioDAO;
    private Connection connection;
    private FacesContext context;
    private String senhaRepetida;

    /**
     * Creates a new instance of CadastroMobileController
     */
    public CadastroMobileController() {
        context = FacesContext.getCurrentInstance();
        connection = new Connection();
        iniciaCampos();
    }

    private void iniciaCampos() {
        usuario = new Usuario();
        senhaRepetida = "";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSenhaRepetida() {
        return senhaRepetida;
    }

    public void setSenhaRepetida(String senhaRepetida) {
        this.senhaRepetida = senhaRepetida;
    }

    private boolean checkSenhasConfirmada() {
        return senhaRepetida.equals(usuario.getSenha().toString());
    }

    public String salvarDadosUsuario() {
        usuarioDAO = new UsuarioJpaController(connection.getEntityFactory());
        
         if (!usuarioDAO.checkUsuario(usuario)) {
            (new AddMessage()).informationMessage(usuario.getUsuario()+" já existe. Por favor tente outro usuário!", "mensagemCadastroMobile");
            return Pages.Mobile.CADASTRO;
        }

        if (!checkSenhasConfirmada()) {
            (new AddMessage()).informationMessage("Senha e senha repetidas são diferente, tente novamente!", "mensagemCadastroMobile");
            return Pages.Mobile.CADASTRO;
        }

        usuarioDAO = new UsuarioJpaController(connection.getEntityFactory());
        String id = usuarioDAO.gerarIDusuario();
        usuario.setId(id);
        usuario.setTipoIdentificacao(TipoIdentificacao.USUARIO.toString());
        System.out.println(usuario.toString());
        usuarioDAO.create(usuario);

        (new AddMessage()).informationMessage("Cadastro com sucesso! Seu código é " + id+ " e usuário "+usuario.getUsuario(), "mensagemCadastroMobile");
        iniciaCampos();
        return Pages.Mobile.CADASTRO;
    }
    
    public String acessoLogin(){
        System.out.println("Volta em página de login");
        return Pages.Mobile.LOGIN;
    }

}
