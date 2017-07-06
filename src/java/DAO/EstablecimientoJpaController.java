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
import entidades.Establecimiento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Mesa;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Menu;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class EstablecimientoJpaController implements Serializable {

    public EstablecimientoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Establecimiento establecimiento) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (establecimiento.getMesaCollection() == null) {
            establecimiento.setMesaCollection(new ArrayList<Mesa>());
        }
        if (establecimiento.getMenuCollection() == null) {
            establecimiento.setMenuCollection(new ArrayList<Menu>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Mesa> attachedMesaCollection = new ArrayList<Mesa>();
            for (Mesa mesaCollectionMesaToAttach : establecimiento.getMesaCollection()) {
                mesaCollectionMesaToAttach = em.getReference(mesaCollectionMesaToAttach.getClass(), mesaCollectionMesaToAttach.getCodigoMesa());
                attachedMesaCollection.add(mesaCollectionMesaToAttach);
            }
            establecimiento.setMesaCollection(attachedMesaCollection);
            Collection<Menu> attachedMenuCollection = new ArrayList<Menu>();
            for (Menu menuCollectionMenuToAttach : establecimiento.getMenuCollection()) {
                menuCollectionMenuToAttach = em.getReference(menuCollectionMenuToAttach.getClass(), menuCollectionMenuToAttach.getCodigoMenu());
                attachedMenuCollection.add(menuCollectionMenuToAttach);
            }
            establecimiento.setMenuCollection(attachedMenuCollection);
            em.persist(establecimiento);
            for (Mesa mesaCollectionMesa : establecimiento.getMesaCollection()) {
                Establecimiento oldCodigoEstablecimientoOfMesaCollectionMesa = mesaCollectionMesa.getCodigoEstablecimiento();
                mesaCollectionMesa.setCodigoEstablecimiento(establecimiento);
                mesaCollectionMesa = em.merge(mesaCollectionMesa);
                if (oldCodigoEstablecimientoOfMesaCollectionMesa != null) {
                    oldCodigoEstablecimientoOfMesaCollectionMesa.getMesaCollection().remove(mesaCollectionMesa);
                    oldCodigoEstablecimientoOfMesaCollectionMesa = em.merge(oldCodigoEstablecimientoOfMesaCollectionMesa);
                }
            }
            for (Menu menuCollectionMenu : establecimiento.getMenuCollection()) {
                Establecimiento oldCodigoEstablecimientoOfMenuCollectionMenu = menuCollectionMenu.getCodigoEstablecimiento();
                menuCollectionMenu.setCodigoEstablecimiento(establecimiento);
                menuCollectionMenu = em.merge(menuCollectionMenu);
                if (oldCodigoEstablecimientoOfMenuCollectionMenu != null) {
                    oldCodigoEstablecimientoOfMenuCollectionMenu.getMenuCollection().remove(menuCollectionMenu);
                    oldCodigoEstablecimientoOfMenuCollectionMenu = em.merge(oldCodigoEstablecimientoOfMenuCollectionMenu);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEstablecimiento(establecimiento.getCodigoEstablecimiento()) != null) {
                throw new PreexistingEntityException("Establecimiento " + establecimiento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Establecimiento establecimiento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Establecimiento persistentEstablecimiento = em.find(Establecimiento.class, establecimiento.getCodigoEstablecimiento());
            Collection<Mesa> mesaCollectionOld = persistentEstablecimiento.getMesaCollection();
            Collection<Mesa> mesaCollectionNew = establecimiento.getMesaCollection();
            Collection<Menu> menuCollectionOld = persistentEstablecimiento.getMenuCollection();
            Collection<Menu> menuCollectionNew = establecimiento.getMenuCollection();
            List<String> illegalOrphanMessages = null;
            for (Mesa mesaCollectionOldMesa : mesaCollectionOld) {
                if (!mesaCollectionNew.contains(mesaCollectionOldMesa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Mesa " + mesaCollectionOldMesa + " since its codigoEstablecimiento field is not nullable.");
                }
            }
            for (Menu menuCollectionOldMenu : menuCollectionOld) {
                if (!menuCollectionNew.contains(menuCollectionOldMenu)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Menu " + menuCollectionOldMenu + " since its codigoEstablecimiento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Mesa> attachedMesaCollectionNew = new ArrayList<Mesa>();
            for (Mesa mesaCollectionNewMesaToAttach : mesaCollectionNew) {
                mesaCollectionNewMesaToAttach = em.getReference(mesaCollectionNewMesaToAttach.getClass(), mesaCollectionNewMesaToAttach.getCodigoMesa());
                attachedMesaCollectionNew.add(mesaCollectionNewMesaToAttach);
            }
            mesaCollectionNew = attachedMesaCollectionNew;
            establecimiento.setMesaCollection(mesaCollectionNew);
            Collection<Menu> attachedMenuCollectionNew = new ArrayList<Menu>();
            for (Menu menuCollectionNewMenuToAttach : menuCollectionNew) {
                menuCollectionNewMenuToAttach = em.getReference(menuCollectionNewMenuToAttach.getClass(), menuCollectionNewMenuToAttach.getCodigoMenu());
                attachedMenuCollectionNew.add(menuCollectionNewMenuToAttach);
            }
            menuCollectionNew = attachedMenuCollectionNew;
            establecimiento.setMenuCollection(menuCollectionNew);
            establecimiento = em.merge(establecimiento);
            for (Mesa mesaCollectionNewMesa : mesaCollectionNew) {
                if (!mesaCollectionOld.contains(mesaCollectionNewMesa)) {
                    Establecimiento oldCodigoEstablecimientoOfMesaCollectionNewMesa = mesaCollectionNewMesa.getCodigoEstablecimiento();
                    mesaCollectionNewMesa.setCodigoEstablecimiento(establecimiento);
                    mesaCollectionNewMesa = em.merge(mesaCollectionNewMesa);
                    if (oldCodigoEstablecimientoOfMesaCollectionNewMesa != null && !oldCodigoEstablecimientoOfMesaCollectionNewMesa.equals(establecimiento)) {
                        oldCodigoEstablecimientoOfMesaCollectionNewMesa.getMesaCollection().remove(mesaCollectionNewMesa);
                        oldCodigoEstablecimientoOfMesaCollectionNewMesa = em.merge(oldCodigoEstablecimientoOfMesaCollectionNewMesa);
                    }
                }
            }
            for (Menu menuCollectionNewMenu : menuCollectionNew) {
                if (!menuCollectionOld.contains(menuCollectionNewMenu)) {
                    Establecimiento oldCodigoEstablecimientoOfMenuCollectionNewMenu = menuCollectionNewMenu.getCodigoEstablecimiento();
                    menuCollectionNewMenu.setCodigoEstablecimiento(establecimiento);
                    menuCollectionNewMenu = em.merge(menuCollectionNewMenu);
                    if (oldCodigoEstablecimientoOfMenuCollectionNewMenu != null && !oldCodigoEstablecimientoOfMenuCollectionNewMenu.equals(establecimiento)) {
                        oldCodigoEstablecimientoOfMenuCollectionNewMenu.getMenuCollection().remove(menuCollectionNewMenu);
                        oldCodigoEstablecimientoOfMenuCollectionNewMenu = em.merge(oldCodigoEstablecimientoOfMenuCollectionNewMenu);
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
                Integer id = establecimiento.getCodigoEstablecimiento();
                if (findEstablecimiento(id) == null) {
                    throw new NonexistentEntityException("The establecimiento with id " + id + " no longer exists.");
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
            Establecimiento establecimiento;
            try {
                establecimiento = em.getReference(Establecimiento.class, id);
                establecimiento.getCodigoEstablecimiento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The establecimiento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Mesa> mesaCollectionOrphanCheck = establecimiento.getMesaCollection();
            for (Mesa mesaCollectionOrphanCheckMesa : mesaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Establecimiento (" + establecimiento + ") cannot be destroyed since the Mesa " + mesaCollectionOrphanCheckMesa + " in its mesaCollection field has a non-nullable codigoEstablecimiento field.");
            }
            Collection<Menu> menuCollectionOrphanCheck = establecimiento.getMenuCollection();
            for (Menu menuCollectionOrphanCheckMenu : menuCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Establecimiento (" + establecimiento + ") cannot be destroyed since the Menu " + menuCollectionOrphanCheckMenu + " in its menuCollection field has a non-nullable codigoEstablecimiento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(establecimiento);
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

    public List<Establecimiento> findEstablecimientoEntities() {
        return findEstablecimientoEntities(true, -1, -1);
    }

    public List<Establecimiento> findEstablecimientoEntities(int maxResults, int firstResult) {
        return findEstablecimientoEntities(false, maxResults, firstResult);
    }

    private List<Establecimiento> findEstablecimientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Establecimiento.class));
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

    public Establecimiento findEstablecimiento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Establecimiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstablecimientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Establecimiento> rt = cq.from(Establecimiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
