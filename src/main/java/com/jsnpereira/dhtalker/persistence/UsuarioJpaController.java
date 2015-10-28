/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsnpereira.dhtalker.persistence;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.jsnpereira.dhtalker.entity.Historico;
import com.jsnpereira.dhtalker.entity.Usuario;
import com.jsnpereira.dhtalker.persistence.exceptions.IllegalOrphanException;
import com.jsnpereira.dhtalker.persistence.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Jeison
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getHistoricoList() == null) {
            usuario.setHistoricoList(new ArrayList<Historico>());
        }
        if (usuario.getHistoricoList1() == null) {
            usuario.setHistoricoList1(new ArrayList<Historico>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Historico> attachedHistoricoList = new ArrayList<Historico>();
            for (Historico historicoListHistoricoToAttach : usuario.getHistoricoList()) {
                historicoListHistoricoToAttach = em.getReference(historicoListHistoricoToAttach.getClass(), historicoListHistoricoToAttach.getIdhistorico());
                attachedHistoricoList.add(historicoListHistoricoToAttach);
            }
            usuario.setHistoricoList(attachedHistoricoList);
            List<Historico> attachedHistoricoList1 = new ArrayList<Historico>();
            for (Historico historicoList1HistoricoToAttach : usuario.getHistoricoList1()) {
                historicoList1HistoricoToAttach = em.getReference(historicoList1HistoricoToAttach.getClass(), historicoList1HistoricoToAttach.getIdhistorico());
                attachedHistoricoList1.add(historicoList1HistoricoToAttach);
            }
            usuario.setHistoricoList1(attachedHistoricoList1);
            em.persist(usuario);
            for (Historico historicoListHistorico : usuario.getHistoricoList()) {
                Usuario oldAtendenteOfHistoricoListHistorico = historicoListHistorico.getAtendente();
                historicoListHistorico.setAtendente(usuario);
                historicoListHistorico = em.merge(historicoListHistorico);
                if (oldAtendenteOfHistoricoListHistorico != null) {
                    oldAtendenteOfHistoricoListHistorico.getHistoricoList().remove(historicoListHistorico);
                    oldAtendenteOfHistoricoListHistorico = em.merge(oldAtendenteOfHistoricoListHistorico);
                }
            }
            for (Historico historicoList1Historico : usuario.getHistoricoList1()) {
                Usuario oldUsuarioOfHistoricoList1Historico = historicoList1Historico.getUsuario();
                historicoList1Historico.setUsuario(usuario);
                historicoList1Historico = em.merge(historicoList1Historico);
                if (oldUsuarioOfHistoricoList1Historico != null) {
                    oldUsuarioOfHistoricoList1Historico.getHistoricoList1().remove(historicoList1Historico);
                    oldUsuarioOfHistoricoList1Historico = em.merge(oldUsuarioOfHistoricoList1Historico);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Historico> historicoListOrphanCheck = usuario.getHistoricoList();
            for (Historico historicoListOrphanCheckHistorico : historicoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Historico " + historicoListOrphanCheckHistorico + " in its historicoList field has a non-nullable atendente field.");
            }
            List<Historico> historicoList1OrphanCheck = usuario.getHistoricoList1();
            for (Historico historicoList1OrphanCheckHistorico : historicoList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Historico " + historicoList1OrphanCheckHistorico + " in its historicoList1 field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Usuario accessLogon(Usuario login) {
        Usuario resultLogin;
        EntityManager em = getEntityManager();

        System.out.println("Acess login");
        Query query = em.createNamedQuery("Usuario.findByUsuario");
        query.setParameter("usuario", login.getUsuario());

        try {
            resultLogin = (Usuario) query.getSingleResult();

            if (resultLogin.getUsuario().equals(login.getUsuario())) {
                if (resultLogin.getSenha().equals(login.getSenha())) {
                    return resultLogin;
                }
            }
        } catch (NoResultException e) {
            return null;
        }
        return null;
    }

    public boolean checkUsuario(Usuario usuario) {
        List<Usuario> lista;
        EntityManager em = getEntityManager();

        System.out.println("Acess login");
        Query query = em.createNamedQuery("Usuario.findByUsuario");
        query.setParameter("usuario", usuario.getUsuario());
        lista = query.getResultList();
        System.out.println("Usuário encontrado: "+lista.size());

        return lista.size() == 0;
    }

    public String gerarIDusuario() {
        Random generator = new Random();
        return String.valueOf(generator.nextInt(10000));
    }

}
