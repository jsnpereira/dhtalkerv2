/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.attributes;

/**
 *
 * @author jsnpereira
 */
public class Pages {

    public final static String login = "/web.xhtml";

    public class Destkop {
        public final static String PRINCIPAL = "/Desktop/usuario/principal.xhtml";
        public final static String ALTERAR_SENHA = "/Desktop/usuario/senha.xhtml";
        public final static String CADASTRO = "/Desktop/usuario/cadastro.xhtml";
        public final static String LOGIN = "/web.xhtml";
        public final static String SALA_CHAT = "/Desktop/usuario/salaChat.xhtml";
    }

    public class Admin {

        public final static String PRINCIPAL = "/Desktop/admin/principal.xhtml";
        public final static String ALTERAR_SENHA = "/Desktop/admin/senha.xhtml";
        public final static String CADASTRO = "/Desktop/admin/cadastro.xhtml";
        public final static String HISTORICO = "/Desktop/admin/historico.xhtml";
    }

    public class Mobile {

        public final static String PRINCIPAL = "/Desktop/Mobile/principalMobile.xhtml";
        public final static String ALTERAR_SENHA = "/Desktop/Mobile/senhaMobile.xhtml";
        public final static String CADASTRO = "/Desktop/Mobile/cadastroMobile.xhtml";
        public final static String CADASTRO_ATENDIMENTO = "/Desktop/Mobile/chatAtendimento.xhtml";
        public final static String SALA_CHAT = "/Desktop/Mobile/salaChat.xhtml";
        public final static String FILA_ESPERA = "/Desktop/Mobile/fila.xhtml";
        public final static String LOGIN = "/index.xhtml";
    }

}
