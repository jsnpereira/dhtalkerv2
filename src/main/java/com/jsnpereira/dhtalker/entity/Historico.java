/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jeison
 */
@Entity
@Table(name = "historico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Historico.findAll", query = "SELECT h FROM Historico h"),
    @NamedQuery(name = "Historico.findByIdhistorico", query = "SELECT h FROM Historico h WHERE h.idhistorico = :idhistorico"),
    @NamedQuery(name = "Historico.findByInicioTempo", query = "SELECT h FROM Historico h WHERE h.inicioTempo = :inicioTempo"),
    @NamedQuery(name = "Historico.findByFinalTempo", query = "SELECT h FROM Historico h WHERE h.finalTempo = :finalTempo"),
    @NamedQuery(name = "Historico.findByNumeroDestino", query = "SELECT h FROM Historico h WHERE h.numeroDestino = :numeroDestino"),
    @NamedQuery(name = "Historico.findByNomeDestino", query = "SELECT h FROM Historico h WHERE h.nomeDestino = :nomeDestino")})
public class Historico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idhistorico")
    private Integer idhistorico;
    @Column(name = "inicio_tempo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioTempo;
    @Column(name = "final_tempo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finalTempo;
    @Column(name = "numero_destino")
    private long numeroDestino;
    @Column(name = "nome_destino")
    private String nomeDestino;
    @JoinColumn(name = "atendente", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario atendente;
    @JoinColumn(name = "usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Historico() {
    }

    public Historico(Integer idhistorico) {
        this.idhistorico = idhistorico;
    }

    public Integer getIdhistorico() {
        return idhistorico;
    }

    public void setIdhistorico(Integer idhistorico) {
        this.idhistorico = idhistorico;
    }

    public Date getInicioTempo() {
        return inicioTempo;
    }

    public void setInicioTempo(Date inicioTempo) {
        this.inicioTempo = inicioTempo;
    }

    public Date getFinalTempo() {
        return finalTempo;
    }

    public void setFinalTempo(Date finalTempo) {
        this.finalTempo = finalTempo;
    }

    public long getNumeroDestino() {
        return numeroDestino;
    }

    public void setNumeroDestino(long numeroDestino) {
        this.numeroDestino = numeroDestino;
    }

    public String getNomeDestino() {
        return nomeDestino;
    }

    public void setNomeDestino(String nomeDestino) {
        this.nomeDestino = nomeDestino;
    }

    public Usuario getAtendente() {
        return atendente;
    }

    public void setAtendente(Usuario atendente) {
        this.atendente = atendente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idhistorico != null ? idhistorico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historico)) {
            return false;
        }
        Historico other = (Historico) object;
        if ((this.idhistorico == null && other.idhistorico != null) || (this.idhistorico != null && !this.idhistorico.equals(other.idhistorico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Historico{" + "inicioTempo=" + inicioTempo + ", finalTempo=" + finalTempo + ", numeroDestino=" + numeroDestino + ", nomeDestino=" + nomeDestino + ", atendente=" + atendente + ", usuario=" + usuario + '}';
    }

    
    
}
