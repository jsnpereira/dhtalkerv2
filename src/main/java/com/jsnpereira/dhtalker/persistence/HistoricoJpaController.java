/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.persistence;

import com.jsnpereira.dhtalker.entity.Historico;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.jsnpereira.dhtalker.entity.Usuario;
import com.jsnpereira.dhtalker.persistence.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jeison
 */
public class HistoricoJpaController implements Serializable {

    public HistoricoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Historico historico) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario atendente = historico.getAtendente();
            if (atendente != null) {
                atendente = em.getReference(atendente.getClass(), atendente.getIdUsuario());
                historico.setAtendente(atendente);
            }
            Usuario usuario = historico.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                historico.setUsuario(usuario);
            }
            em.persist(historico);
            if (atendente != null) {
                atendente.getHistoricoList().add(historico);
                atendente = em.merge(atendente);
            }
            if (usuario != null) {
                usuario.getHistoricoList().add(historico);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Historico historico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historico persistentHistorico = em.find(Historico.class, historico.getIdhistorico());
            Usuario atendenteOld = persistentHistorico.getAtendente();
            Usuario atendenteNew = historico.getAtendente();
            Usuario usuarioOld = persistentHistorico.getUsuario();
            Usuario usuarioNew = historico.getUsuario();
            if (atendenteNew != null) {
                atendenteNew = em.getReference(atendenteNew.getClass(), atendenteNew.getIdUsuario());
                historico.setAtendente(atendenteNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                historico.setUsuario(usuarioNew);
            }
            historico = em.merge(historico);
            if (atendenteOld != null && !atendenteOld.equals(atendenteNew)) {
                atendenteOld.getHistoricoList().remove(historico);
                atendenteOld = em.merge(atendenteOld);
            }
            if (atendenteNew != null && !atendenteNew.equals(atendenteOld)) {
                atendenteNew.getHistoricoList().add(historico);
                atendenteNew = em.merge(atendenteNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getHistoricoList().remove(historico);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getHistoricoList().add(historico);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historico.getIdhistorico();
                if (findHistorico(id) == null) {
                    throw new NonexistentEntityException("The historico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historico historico;
            try {
                historico = em.getReference(Historico.class, id);
                historico.getIdhistorico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historico with id " + id + " no longer exists.", enfe);
            }
            Usuario atendente = historico.getAtendente();
            if (atendente != null) {
                atendente.getHistoricoList().remove(historico);
                atendente = em.merge(atendente);
            }
            Usuario usuario = historico.getUsuario();
            if (usuario != null) {
                usuario.getHistoricoList().remove(historico);
                usuario = em.merge(usuario);
            }
            em.remove(historico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Historico> findHistoricoEntities() {
        return findHistoricoEntities(true, -1, -1);
    }

    public List<Historico> findHistoricoEntities(int maxResults, int firstResult) {
        return findHistoricoEntities(false, maxResults, firstResult);
    }

    private List<Historico> findHistoricoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Historico.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Historico findHistorico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Historico.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoricoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Historico> rt = cq.from(Historico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
