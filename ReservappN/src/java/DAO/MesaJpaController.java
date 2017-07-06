/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import DAO.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Establecimiento;
import entidades.Mesa;
import entidades.Reserva;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Alumnos
 */
public class MesaJpaController implements Serializable {

    public MesaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mesa mesa) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (mesa.getReservaCollection() == null) {
            mesa.setReservaCollection(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Establecimiento codigoEstablecimiento = mesa.getCodigoEstablecimiento();
            if (codigoEstablecimiento != null) {
                codigoEstablecimiento = em.getReference(codigoEstablecimiento.getClass(), codigoEstablecimiento.getCodigoEstablecimiento());
                mesa.setCodigoEstablecimiento(codigoEstablecimiento);
            }
            Collection<Reserva> attachedReservaCollection = new ArrayList<Reserva>();
            for (Reserva reservaCollectionReservaToAttach : mesa.getReservaCollection()) {
                reservaCollectionReservaToAttach = em.getReference(reservaCollectionReservaToAttach.getClass(), reservaCollectionReservaToAttach.getCodigoReserva());
                attachedReservaCollection.add(reservaCollectionReservaToAttach);
            }
            mesa.setReservaCollection(attachedReservaCollection);
            em.persist(mesa);
            if (codigoEstablecimiento != null) {
                codigoEstablecimiento.getMesaCollection().add(mesa);
                codigoEstablecimiento = em.merge(codigoEstablecimiento);
            }
            for (Reserva reservaCollectionReserva : mesa.getReservaCollection()) {
                Mesa oldCodigoMesaOfReservaCollectionReserva = reservaCollectionReserva.getCodigoMesa();
                reservaCollectionReserva.setCodigoMesa(mesa);
                reservaCollectionReserva = em.merge(reservaCollectionReserva);
                if (oldCodigoMesaOfReservaCollectionReserva != null) {
                    oldCodigoMesaOfReservaCollectionReserva.getReservaCollection().remove(reservaCollectionReserva);
                    oldCodigoMesaOfReservaCollectionReserva = em.merge(oldCodigoMesaOfReservaCollectionReserva);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMesa(mesa.getCodigoMesa()) != null) {
                throw new PreexistingEntityException("Mesa " + mesa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mesa mesa) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Mesa persistentMesa = em.find(Mesa.class, mesa.getCodigoMesa());
            Establecimiento codigoEstablecimientoOld = persistentMesa.getCodigoEstablecimiento();
            Establecimiento codigoEstablecimientoNew = mesa.getCodigoEstablecimiento();
            Collection<Reserva> reservaCollectionOld = persistentMesa.getReservaCollection();
            Collection<Reserva> reservaCollectionNew = mesa.getReservaCollection();
            if (codigoEstablecimientoNew != null) {
                codigoEstablecimientoNew = em.getReference(codigoEstablecimientoNew.getClass(), codigoEstablecimientoNew.getCodigoEstablecimiento());
                mesa.setCodigoEstablecimiento(codigoEstablecimientoNew);
            }
            Collection<Reserva> attachedReservaCollectionNew = new ArrayList<Reserva>();
            for (Reserva reservaCollectionNewReservaToAttach : reservaCollectionNew) {
                reservaCollectionNewReservaToAttach = em.getReference(reservaCollectionNewReservaToAttach.getClass(), reservaCollectionNewReservaToAttach.getCodigoReserva());
                attachedReservaCollectionNew.add(reservaCollectionNewReservaToAttach);
            }
            reservaCollectionNew = attachedReservaCollectionNew;
            mesa.setReservaCollection(reservaCollectionNew);
            mesa = em.merge(mesa);
            if (codigoEstablecimientoOld != null && !codigoEstablecimientoOld.equals(codigoEstablecimientoNew)) {
                codigoEstablecimientoOld.getMesaCollection().remove(mesa);
                codigoEstablecimientoOld = em.merge(codigoEstablecimientoOld);
            }
            if (codigoEstablecimientoNew != null && !codigoEstablecimientoNew.equals(codigoEstablecimientoOld)) {
                codigoEstablecimientoNew.getMesaCollection().add(mesa);
                codigoEstablecimientoNew = em.merge(codigoEstablecimientoNew);
            }
            for (Reserva reservaCollectionOldReserva : reservaCollectionOld) {
                if (!reservaCollectionNew.contains(reservaCollectionOldReserva)) {
                    reservaCollectionOldReserva.setCodigoMesa(null);
                    reservaCollectionOldReserva = em.merge(reservaCollectionOldReserva);
                }
            }
            for (Reserva reservaCollectionNewReserva : reservaCollectionNew) {
                if (!reservaCollectionOld.contains(reservaCollectionNewReserva)) {
                    Mesa oldCodigoMesaOfReservaCollectionNewReserva = reservaCollectionNewReserva.getCodigoMesa();
                    reservaCollectionNewReserva.setCodigoMesa(mesa);
                    reservaCollectionNewReserva = em.merge(reservaCollectionNewReserva);
                    if (oldCodigoMesaOfReservaCollectionNewReserva != null && !oldCodigoMesaOfReservaCollectionNewReserva.equals(mesa)) {
                        oldCodigoMesaOfReservaCollectionNewReserva.getReservaCollection().remove(reservaCollectionNewReserva);
                        oldCodigoMesaOfReservaCollectionNewReserva = em.merge(oldCodigoMesaOfReservaCollectionNewReserva);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = mesa.getCodigoMesa();
                if (findMesa(id) == null) {
                    throw new NonexistentEntityException("The mesa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Mesa mesa;
            try {
                mesa = em.getReference(Mesa.class, id);
                mesa.getCodigoMesa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mesa with id " + id + " no longer exists.", enfe);
            }
            Establecimiento codigoEstablecimiento = mesa.getCodigoEstablecimiento();
            if (codigoEstablecimiento != null) {
                codigoEstablecimiento.getMesaCollection().remove(mesa);
                codigoEstablecimiento = em.merge(codigoEstablecimiento);
            }
            Collection<Reserva> reservaCollection = mesa.getReservaCollection();
            for (Reserva reservaCollectionReserva : reservaCollection) {
                reservaCollectionReserva.setCodigoMesa(null);
                reservaCollectionReserva = em.merge(reservaCollectionReserva);
            }
            em.remove(mesa);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mesa> findMesaEntities() {
        return findMesaEntities(true, -1, -1);
    }

    public List<Mesa> findMesaEntities(int maxResults, int firstResult) {
        return findMesaEntities(false, maxResults, firstResult);
    }

    private List<Mesa> findMesaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mesa.class));
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

    public Mesa findMesa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mesa.class, id);
        } finally {
            em.close();
        }
    }

    public int getMesaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Mesa> rt = cq.from(Mesa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
