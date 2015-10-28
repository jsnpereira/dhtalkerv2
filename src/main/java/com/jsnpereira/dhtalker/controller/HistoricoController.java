/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.controller;

import com.jsnpereira.dhtalker.attributes.Pages;
import com.jsnpereira.dhtalker.attributes.Sessao;
import com.jsnpereira.dhtalker.configuration.Connection;
import com.jsnpereira.dhtalker.entity.Historico;
import com.jsnpereira.dhtalker.entity.Usuario;
import com.jsnpereira.dhtalker.persistence.HistoricoJpaController;
import java.io.IOException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Jeison
 */
@ManagedBean
@SessionScoped
public class HistoricoController {

    private HttpSession session;
    private Connection connection;
    private FacesContext context;
    private Usuario logon;
    private HistoricoJpaController dbHistorico;
    private List<Historico> listHistorico;

    /**
     * Creates a new instance of HistoricoController
     */
    public HistoricoController() {
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
        dbHistorico = new HistoricoJpaController(connection.getEntityFactory());
        listHistorico = dbHistorico.findHistoricoEntities();
    }

    public List<Historico> getListHistorico() {
        return listHistorico;
    }

    public void setListHistorico(List<Historico> listHistorico) {
        this.listHistorico = listHistorico;
    }

    public Usuario getLogon() {
        return logon;
    }

    public void setLogon(Usuario logon) {
        this.logon = logon;
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

    private boolean sessionValidate(Usuario logon) {
        return logon != null;
    }

    public String logoutUsuario() {
        if (session != null) {
            session.invalidate();
        }
        return Pages.login;
    }
}
