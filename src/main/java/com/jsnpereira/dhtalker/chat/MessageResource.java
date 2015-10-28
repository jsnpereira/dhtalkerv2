package com.jsnpereira.dhtalker.chat;


import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jeison
 */

@PushEndpoint("/message")
public class MessageResource {
    
    @OnMessage(encoders = {JSONEncoder.class})
    public String onMessage(String message){
        System.out.println("Servidor recebido: "+message);
        return message;
    }
}
