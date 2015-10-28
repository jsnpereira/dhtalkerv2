/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.messages;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author jsnpereira
 */
public class AddMessage {

    FacesContext context;

    public AddMessage() {
        this.context = FacesContext.getCurrentInstance();
    }

    // ID é referencia da classe messagem da faces.
    public void informationMessage(String mensagem, String id) {
        context.addMessage(id, new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, null));
    }

    public void erroMessage(String mensagem, String id) {
        context.addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
    }

    public void alertMessage(String mensagem, String id) {
        context.addMessage(id, new FacesMessage(FacesMessage.SEVERITY_WARN, mensagem, null));
    }

}
