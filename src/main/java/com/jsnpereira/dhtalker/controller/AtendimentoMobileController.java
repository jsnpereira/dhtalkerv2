package com.jsnpereira.dhtalker.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jsnpereira.dhtalker.attributes.Pages;
import com.jsnpereira.dhtalker.attributes.Sessao;
import com.jsnpereira.dhtalker.entity.Cliente;
import com.jsnpereira.dhtalker.entity.Usuario;
import com.jsnpereira.dhtalker.fila.GerenciamentoDeFilaFacade;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author Jeison
 */
@ManagedBean
@SessionScoped
public class AtendimentoMobileController {

    private final static String CHANNEL = "/message";
    private Cliente cliente;
    private Usuario usuario;
    private HttpSession sessao;
    private FacesContext context;
    private GerenciamentoDeFilaFacade facade;
    private boolean logedIn;
    private String mensagem;
    private String canal;
    

    /**
     * Creates a new instance of atendimentoMobile
     */
    public AtendimentoMobileController() {

        logedIn = false;
        context = FacesContext.getCurrentInstance();
        sessao = (HttpSession) context.getExternalContext().getSession(false);
        usuario = (Usuario) sessao.getAttribute(Sessao.USUARIO);

        if (!sessaoValidate(usuario)) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(Pages.Mobile.LOGIN);
            } catch (IOException ex) {
                Logger.getLogger(AtendimentoMobileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        cliente = new Cliente();
        cliente.setUsuario(usuario);
        facade = new GerenciamentoDeFilaFacade();
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public boolean isLogedIn() {
        return logedIn;
    }

    public void setLogedIn(boolean logedIn) {
        this.logedIn = logedIn;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public void iniciaChat() {
        logedIn = true;
        canal = String.valueOf(generateIDSala());
        cliente.setCanal(canal);
        facade.inserir(cliente);
        System.out.println("Inicia o chat com canal inserido: " + cliente.getCanal());
    }

    public void cancelarChat() {
        logedIn = false;
        if (facade.contains(cliente)) {
            facade.removerCliente(cliente);
        }
        System.out.println("Clicou cancelar");
        mensagemCancelar();
    }

    public void enviarMensagem() {
        EventBus eb = EventBusFactory.getDefault().eventBus();
        eb.publish(CHANNEL, convertToJSON(usuario.getNome(), mensagem));
        System.out.println("Enviado: " + mensagem + " - username: " + usuario.getUsuario());
        mensagem = "";
    }

    private void mensagemCancelar() {
        EventBus eb = EventBusFactory.getDefault().eventBus();
        eb.publish(CHANNEL, convertToJSON(usuario.getNome(), "saiu na sala"));
    }

    public int generateIDSala() {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(10000);
    }

    private String convertToJSON(String origem, String msg) {
        JSONObject json = new JSONObject();
        Object put = json.put("canal", canal);
        json.put("origem", origem);
        json.put("mensagem", msg);
        return json.toJSONString();
    }

}
