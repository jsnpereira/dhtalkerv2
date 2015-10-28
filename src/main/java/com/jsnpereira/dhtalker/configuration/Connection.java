/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jeison
 */
public class Connection {

    String persistencia = "dhtalkerv2_dhtalkerv2_war_1.0PU";
    EntityManagerFactory factory;

    public EntityManagerFactory getEntityFactory() {
        return Persistence.createEntityManagerFactory(persistencia);
    }
}
