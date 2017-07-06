/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.PreexistingEntityException;
import DAO.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Establecimiento;
import entidades.Menu;
import entidades.Orden;
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
public class MenuJpaController implements Serializable {

    public MenuJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Menu menu) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (menu.getOrdenCollection() == null) {
            menu.setOrdenCollection(new ArrayList<Orden>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Establecimiento codigoEstablecimiento = menu.getCodigoEstablecimiento();
            if (codigoEstablecimiento != null) {
                codigoEstablecimiento = em.getReference(codigoEstablecimiento.getClass(), codigoEstablecimiento.getCodigoEstablecimiento());
                menu.setCodigoEstablecimiento(codigoEstablecimiento);
            }
            Collection<Orden> attachedOrdenCollection = new ArrayList<Orden>();
            for (Orden ordenCollectionOrdenToAttach : menu.getOrdenCollection()) {
                ordenCollectionOrdenToAttach = em.getReference(ordenCollectionOrdenToAttach.getClass(), ordenCollectionOrdenToAttach.getCodigoOrden());
                attachedOrdenCollection.add(ordenCollectionOrdenToAttach);
            }
            menu.setOrdenCollection(attachedOrdenCollection);
            em.persist(menu);
            if (codigoEstablecimiento != null) {
                codigoEstablecimiento.getMenuCollection().add(menu);
                codigoEstablecimiento = em.merge(codigoEstablecimiento);
            }
            for (Orden ordenCollectionOrden : menu.getOrdenCollection()) {
                Menu oldMenuCodigoMenuOfOrdenCollectionOrden = ordenCollectionOrden.getMenuCodigoMenu();
                ordenCollectionOrden.setMenuCodigoMenu(menu);
                ordenCollectionOrden = em.merge(ordenCollectionOrden);
                if (oldMenuCodigoMenuOfOrdenCollectionOrden != null) {
                    oldMenuCodigoMenuOfOrdenCollectionOrden.getOrdenCollection().remove(ordenCollectionOrden);
                    oldMenuCodigoMenuOfOrdenCollectionOrden = em.merge(oldMenuCodigoMenuOfOrdenCollectionOrden);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMenu(menu.getCodigoMenu()) != null) {
                throw new PreexistingEntityException("Menu " + menu + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Menu menu) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Menu persistentMenu = em.find(Menu.class, menu.getCodigoMenu());
            Establecimiento codigoEstablecimientoOld = persistentMenu.getCodigoEstablecimiento();
            Establecimiento codigoEstablecimientoNew = menu.getCodigoEstablecimiento();
            Collection<Orden> ordenCollectionOld = persistentMenu.getOrdenCollection();
            Collection<Orden> ordenCollectionNew = menu.getOrdenCollection();
            List<String> illegalOrphanMessages = null;
            for (Orden ordenCollectionOldOrden : ordenCollectionOld) {
                if (!ordenCollectionNew.contains(ordenCollectionOldOrden)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orden " + ordenCollectionOldOrden + " since its menuCodigoMenu field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codigoEstablecimientoNew != null) {
                codigoEstablecimientoNew = em.getReference(codigoEstablecimientoNew.getClass(), codigoEstablecimientoNew.getCodigoEstablecimiento());
                menu.setCodigoEstablecimiento(codigoEstablecimientoNew);
            }
            Collection<Orden> attachedOrdenCollectionNew = new ArrayList<Orden>();
            for (Orden ordenCollectionNewOrdenToAttach : ordenCollectionNew) {
                ordenCollectionNewOrdenToAttach = em.getReference(ordenCollectionNewOrdenToAttach.getClass(), ordenCollectionNewOrdenToAttach.getCodigoOrden());
                attachedOrdenCollectionNew.add(ordenCollectionNewOrdenToAttach);
            }
            ordenCollectionNew = attachedOrdenCollectionNew;
            menu.setOrdenCollection(ordenCollectionNew);
            menu = em.merge(menu);
            if (codigoEstablecimientoOld != null && !codigoEstablecimientoOld.equals(codigoEstablecimientoNew)) {
                codigoEstablecimientoOld.getMenuCollection().remove(menu);
                codigoEstablecimientoOld = em.merge(codigoEstablecimientoOld);
            }
            if (codigoEstablecimientoNew != null && !codigoEstablecimientoNew.equals(codigoEstablecimientoOld)) {
                codigoEstablecimientoNew.getMenuCollection().add(menu);
                codigoEstablecimientoNew = em.merge(codigoEstablecimientoNew);
            }
            for (Orden ordenCollectionNewOrden : ordenCollectionNew) {
                if (!ordenCollectionOld.contains(ordenCollectionNewOrden)) {
                    Menu oldMenuCodigoMenuOfOrdenCollectionNewOrden = ordenCollectionNewOrden.getMenuCodigoMenu();
                    ordenCollectionNewOrden.setMenuCodigoMenu(menu);
                    ordenCollectionNewOrden = em.merge(ordenCollectionNewOrden);
                    if (oldMenuCodigoMenuOfOrdenCollectionNewOrden != null && !oldMenuCodigoMenuOfOrdenCollectionNewOrden.equals(menu)) {
                        oldMenuCodigoMenuOfOrdenCollectionNewOrden.getOrdenCollection().remove(ordenCollectionNewOrden);
                        oldMenuCodigoMenuOfOrdenCollectionNewOrden = em.merge(oldMenuCodigoMenuOfOrdenCollectionNewOrden);
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
                Integer id = menu.getCodigoMenu();
                if (findMenu(id) == null) {
                    throw new NonexistentEntityException("The menu with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Menu menu;
            try {
                menu = em.getReference(Menu.class, id);
                menu.getCodigoMenu();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The menu with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Orden> ordenCollectionOrphanCheck = menu.getOrdenCollection();
            for (Orden ordenCollectionOrphanCheckOrden : ordenCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Menu (" + menu + ") cannot be destroyed since the Orden " + ordenCollectionOrphanCheckOrden + " in its ordenCollection field has a non-nullable menuCodigoMenu field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Establecimiento codigoEstablecimiento = menu.getCodigoEstablecimiento();
            if (codigoEstablecimiento != null) {
                codigoEstablecimiento.getMenuCollection().remove(menu);
                codigoEstablecimiento = em.merge(codigoEstablecimiento);
            }
            em.remove(menu);
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

    public List<Menu> findMenuEntities() {
        return findMenuEntities(true, -1, -1);
    }

    public List<Menu> findMenuEntities(int maxResults, int firstResult) {
        return findMenuEntities(false, maxResults, firstResult);
    }

    private List<Menu> findMenuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Menu.class));
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

    public Menu findMenu(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Menu.class, id);
        } finally {
            em.close();
        }
    }

    public int getMenuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Menu> rt = cq.from(Menu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
