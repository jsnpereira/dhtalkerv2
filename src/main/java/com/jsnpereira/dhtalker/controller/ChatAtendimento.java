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
import com.jsnpereira.dhtalker.entity.Historico;
import com.jsnpereira.dhtalker.entity.Usuario;
import com.jsnpereira.dhtalker.fila.GerenciamentoDeFilaFacade;
import com.jsnpereira.dhtalker.persistence.HistoricoJpaController;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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
public class ChatAtendimento {

    private final static String CHANNEL = "/message";
    private HttpSession session;
    private FacesContext context;
    private Usuario logon;
    private Connection connection;
    private Cliente cliente;
    private GerenciamentoDeFilaFacade clientes;
    private boolean atendeDisponivel = true;
    private String mensagem;
    private String notificacao;
    private Historico historico;
    private HistoricoJpaController dbHistorico;

    /**
     * Creates a new instance of ChatAtendimento
     */
    public ChatAtendimento() {
        connection = new Connection();
        cliente = new Cliente();
        context = FacesContext.getCurrentInstance();
        session = (HttpSession) context.getExternalContext().getSession(true);
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
        clientes = new GerenciamentoDeFilaFacade();
        notificacao = "";
        dbHistorico = new HistoricoJpaController(connection.getEntityFactory());
    }

    public boolean isAtendeDisponivel() {
        return atendeDisponivel;
    }

    public void setAtendeDisponivel(boolean atendeDisponivel) {
        this.atendeDisponivel = atendeDisponivel;
    }
    
    public String getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(String notificacao) {
        this.notificacao = notificacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Usuario getLogon() {
        return logon;
    }

    public void setLogon(Usuario logon) {
        this.logon = logon;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean verificaFila() {
        return clientes.existeFila();
    }

    public void iniciaCliente() {
        System.out.println("Quantidade de fila: " + clientes.quantidadeTotal());

        if (atendeDisponivel) {
            if (clientes.existeFila()) {
                cliente = clientes.chamarCliente();
                atendeDisponivel = false;
                notificacao = "";
                System.out.println("Receber um cliente");
                iniciaHistorico();
            } else {
                notificacao = "Infelizmente! Não temos cliente agora, aguarde!!";
                System.out.println("Não temos cliente na fila");
            }
        } else {
            notificacao = "Estamos atendendo com cliente, deseja finalizar? Por favor clique a finalizar chat";
        }
    }

    public void finalizarCliente() {
        if (!atendeDisponivel) {
            mensagemFinalizar();
            System.out.println("Finalizado o chat com cliente.");
            atendeDisponivel = true;
            cliente = null;
            notificacao = "Finalizado o atendimento com cliente em sucesso!!";
            salvarHistorico();
        } else {
            notificacao = "Atendimento está finalizado, por favor! inicia um novo cliente!";
        }

    }

    private void mensagemFinalizar() {
        EventBus eb = EventBusFactory.getDefault().eventBus();
        eb.publish(CHANNEL, convertToJSON("A chat está finalizado com sucesso! Até póxima."));
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
        return Pages.Destkop.ALTERAR_SENHA;
    }

    public String mainScreen() {
        return Pages.Destkop.PRINCIPAL;
    }

    public String salaAtendimento() {
        return Pages.Destkop.SALA_CHAT;
    }

    public void enviar() {
        EventBus eb = EventBusFactory.getDefault().eventBus();
        eb.publish(CHANNEL, convertToJSON());
        System.out.println("Enviado: " + mensagem + " - username: " + logon.getUsuario());
        mensagem = "";
    }

    private String convertToJSON() {
        JSONObject json = new JSONObject();
        Object put = json.put("canal", cliente.getCanal());
        json.put("origem", logon.getNome());
        json.put("mensagem", mensagem);
        return json.toJSONString();
    }

    private String convertToJSON(String msg) {
        JSONObject json = new JSONObject();
        Object put = json.put("canal", cliente.getCanal());
        json.put("origem", logon.getNome());
        json.put("mensagem", msg);
        return json.toJSONString();
    }
    
    private void iniciaHistorico(){
        historico = new Historico();
        historico.setAtendente(logon);
        historico.setUsuario(cliente.getUsuario());
        historico.setNomeDestino(cliente.getNomeDestino());
        historico.setNumeroDestino( cliente.getTelefoneDestino());
        historico.setInicioTempo(gerarNovaData());
    }
    
    private void salvarHistorico(){
        if (historico != null) {
            historico.setFinalTempo(gerarNovaData());
            System.out.println(historico.toString());
            dbHistorico.create(historico);
        }
    }
    
    private Date gerarNovaData(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }
}
