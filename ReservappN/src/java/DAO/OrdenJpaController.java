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
import entidades.Menu;
import entidades.Orden;
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
public class OrdenJpaController implements Serializable {

    public OrdenJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orden orden) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (orden.getReservaCollection() == null) {
            orden.setReservaCollection(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Menu menuCodigoMenu = orden.getMenuCodigoMenu();
            if (menuCodigoMenu != null) {
                menuCodigoMenu = em.getReference(menuCodigoMenu.getClass(), menuCodigoMenu.getCodigoMenu());
                orden.setMenuCodigoMenu(menuCodigoMenu);
            }
            Collection<Reserva> attachedReservaCollection = new ArrayList<Reserva>();
            for (Reserva reservaCollectionReservaToAttach : orden.getReservaCollection()) {
                reservaCollectionReservaToAttach = em.getReference(reservaCollectionReservaToAttach.getClass(), reservaCollectionReservaToAttach.getCodigoReserva());
                attachedReservaCollection.add(reservaCollectionReservaToAttach);
            }
            orden.setReservaCollection(attachedReservaCollection);
            em.persist(orden);
            if (menuCodigoMenu != null) {
                menuCodigoMenu.getOrdenCollection().add(orden);
                menuCodigoMenu = em.merge(menuCodigoMenu);
            }
            for (Reserva reservaCollectionReserva : orden.getReservaCollection()) {
                Orden oldOrdenCodigoOrdenOfReservaCollectionReserva = reservaCollectionReserva.getOrdenCodigoOrden();
                reservaCollectionReserva.setOrdenCodigoOrden(orden);
                reservaCollectionReserva = em.merge(reservaCollectionReserva);
                if (oldOrdenCodigoOrdenOfReservaCollectionReserva != null) {
                    oldOrdenCodigoOrdenOfReservaCollectionReserva.getReservaCollection().remove(reservaCollectionReserva);
                    oldOrdenCodigoOrdenOfReservaCollectionReserva = em.merge(oldOrdenCodigoOrdenOfReservaCollectionReserva);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findOrden(orden.getCodigoOrden()) != null) {
                throw new PreexistingEntityException("Orden " + orden + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orden orden) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orden persistentOrden = em.find(Orden.class, orden.getCodigoOrden());
            Menu menuCodigoMenuOld = persistentOrden.getMenuCodigoMenu();
            Menu menuCodigoMenuNew = orden.getMenuCodigoMenu();
            Collection<Reserva> reservaCollectionOld = persistentOrden.getReservaCollection();
            Collection<Reserva> reservaCollectionNew = orden.getReservaCollection();
            if (menuCodigoMenuNew != null) {
                menuCodigoMenuNew = em.getReference(menuCodigoMenuNew.getClass(), menuCodigoMenuNew.getCodigoMenu());
                orden.setMenuCodigoMenu(menuCodigoMenuNew);
            }
            Collection<Reserva> attachedReservaCollectionNew = new ArrayList<Reserva>();
            for (Reserva reservaCollectionNewReservaToAttach : reservaCollectionNew) {
                reservaCollectionNewReservaToAttach = em.getReference(reservaCollectionNewReservaToAttach.getClass(), reservaCollectionNewReservaToAttach.getCodigoReserva());
                attachedReservaCollectionNew.add(reservaCollectionNewReservaToAttach);
            }
            reservaCollectionNew = attachedReservaCollectionNew;
            orden.setReservaCollection(reservaCollectionNew);
            orden = em.merge(orden);
            if (menuCodigoMenuOld != null && !menuCodigoMenuOld.equals(menuCodigoMenuNew)) {
                menuCodigoMenuOld.getOrdenCollection().remove(orden);
                menuCodigoMenuOld = em.merge(menuCodigoMenuOld);
            }
            if (menuCodigoMenuNew != null && !menuCodigoMenuNew.equals(menuCodigoMenuOld)) {
                menuCodigoMenuNew.getOrdenCollection().add(orden);
                menuCodigoMenuNew = em.merge(menuCodigoMenuNew);
            }
            for (Reserva reservaCollectionOldReserva : reservaCollectionOld) {
                if (!reservaCollectionNew.contains(reservaCollectionOldReserva)) {
                    reservaCollectionOldReserva.setOrdenCodigoOrden(null);
                    reservaCollectionOldReserva = em.merge(reservaCollectionOldReserva);
                }
            }
            for (Reserva reservaCollectionNewReserva : reservaCollectionNew) {
                if (!reservaCollectionOld.contains(reservaCollectionNewReserva)) {
                    Orden oldOrdenCodigoOrdenOfReservaCollectionNewReserva = reservaCollectionNewReserva.getOrdenCodigoOrden();
                    reservaCollectionNewReserva.setOrdenCodigoOrden(orden);
                    reservaCollectionNewReserva = em.merge(reservaCollectionNewReserva);
                    if (oldOrdenCodigoOrdenOfReservaCollectionNewReserva != null && !oldOrdenCodigoOrdenOfReservaCollectionNewReserva.equals(orden)) {
                        oldOrdenCodigoOrdenOfReservaCollectionNewReserva.getReservaCollection().remove(reservaCollectionNewReserva);
                        oldOrdenCodigoOrdenOfReservaCollectionNewReserva = em.merge(oldOrdenCodigoOrdenOfReservaCollectionNewReserva);
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
                Integer id = orden.getCodigoOrden();
                if (findOrden(id) == null) {
                    throw new NonexistentEntityException("The orden with id " + id + " no longer exists.");
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
            Orden orden;
            try {
                orden = em.getReference(Orden.class, id);
                orden.getCodigoOrden();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orden with id " + id + " no longer exists.", enfe);
            }
            Menu menuCodigoMenu = orden.getMenuCodigoMenu();
            if (menuCodigoMenu != null) {
                menuCodigoMenu.getOrdenCollection().remove(orden);
                menuCodigoMenu = em.merge(menuCodigoMenu);
            }
            Collection<Reserva> reservaCollection = orden.getReservaCollection();
            for (Reserva reservaCollectionReserva : reservaCollection) {
                reservaCollectionReserva.setOrdenCodigoOrden(null);
                reservaCollectionReserva = em.merge(reservaCollectionReserva);
            }
            em.remove(orden);
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

    public List<Orden> findOrdenEntities() {
        return findOrdenEntities(true, -1, -1);
    }

    public List<Orden> findOrdenEntities(int maxResults, int firstResult) {
        return findOrdenEntities(false, maxResults, firstResult);
    }

    private List<Orden> findOrdenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orden.class));
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

    public Orden findOrden(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orden.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orden> rt = cq.from(Orden.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
