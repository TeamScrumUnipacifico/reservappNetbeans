/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DAO.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Mesa;
import entidades.Usuario;
import entidades.Factura;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Orden;
import entidades.Reserva;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author usuario
 */
public class ReservaJpaController implements Serializable {

    public ReservaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reserva reserva) throws RollbackFailureException, Exception {
        if (reserva.getFacturaCollection() == null) {
            reserva.setFacturaCollection(new ArrayList<Factura>());
        }
        if (reserva.getOrdenCollection() == null) {
            reserva.setOrdenCollection(new ArrayList<Orden>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Mesa codigoMesa = reserva.getCodigoMesa();
            if (codigoMesa != null) {
                codigoMesa = em.getReference(codigoMesa.getClass(), codigoMesa.getCodigoMesa());
                reserva.setCodigoMesa(codigoMesa);
            }
            Usuario documento = reserva.getDocumento();
            if (documento != null) {
                documento = em.getReference(documento.getClass(), documento.getDocumento());
                reserva.setDocumento(documento);
            }
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : reserva.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getCodigoFactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            reserva.setFacturaCollection(attachedFacturaCollection);
            Collection<Orden> attachedOrdenCollection = new ArrayList<Orden>();
            for (Orden ordenCollectionOrdenToAttach : reserva.getOrdenCollection()) {
                ordenCollectionOrdenToAttach = em.getReference(ordenCollectionOrdenToAttach.getClass(), ordenCollectionOrdenToAttach.getCodigoOrden());
                attachedOrdenCollection.add(ordenCollectionOrdenToAttach);
            }
            reserva.setOrdenCollection(attachedOrdenCollection);
            em.persist(reserva);
            if (codigoMesa != null) {
                codigoMesa.getReservaCollection().add(reserva);
                codigoMesa = em.merge(codigoMesa);
            }
            if (documento != null) {
                documento.getReservaCollection().add(reserva);
                documento = em.merge(documento);
            }
            for (Factura facturaCollectionFactura : reserva.getFacturaCollection()) {
                Reserva oldCodigoReservaOfFacturaCollectionFactura = facturaCollectionFactura.getCodigoReserva();
                facturaCollectionFactura.setCodigoReserva(reserva);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldCodigoReservaOfFacturaCollectionFactura != null) {
                    oldCodigoReservaOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldCodigoReservaOfFacturaCollectionFactura = em.merge(oldCodigoReservaOfFacturaCollectionFactura);
                }
            }
            for (Orden ordenCollectionOrden : reserva.getOrdenCollection()) {
                Reserva oldReservaCodigoReservaOfOrdenCollectionOrden = ordenCollectionOrden.getReservaCodigoReserva();
                ordenCollectionOrden.setReservaCodigoReserva(reserva);
                ordenCollectionOrden = em.merge(ordenCollectionOrden);
                if (oldReservaCodigoReservaOfOrdenCollectionOrden != null) {
                    oldReservaCodigoReservaOfOrdenCollectionOrden.getOrdenCollection().remove(ordenCollectionOrden);
                    oldReservaCodigoReservaOfOrdenCollectionOrden = em.merge(oldReservaCodigoReservaOfOrdenCollectionOrden);
                }
            }
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

    public void edit(Reserva reserva) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Reserva persistentReserva = em.find(Reserva.class, reserva.getCodigoReserva());
            Mesa codigoMesaOld = persistentReserva.getCodigoMesa();
            Mesa codigoMesaNew = reserva.getCodigoMesa();
            Usuario documentoOld = persistentReserva.getDocumento();
            Usuario documentoNew = reserva.getDocumento();
            Collection<Factura> facturaCollectionOld = persistentReserva.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = reserva.getFacturaCollection();
            Collection<Orden> ordenCollectionOld = persistentReserva.getOrdenCollection();
            Collection<Orden> ordenCollectionNew = reserva.getOrdenCollection();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its codigoReserva field is not nullable.");
                }
            }
            for (Orden ordenCollectionOldOrden : ordenCollectionOld) {
                if (!ordenCollectionNew.contains(ordenCollectionOldOrden)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orden " + ordenCollectionOldOrden + " since its reservaCodigoReserva field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codigoMesaNew != null) {
                codigoMesaNew = em.getReference(codigoMesaNew.getClass(), codigoMesaNew.getCodigoMesa());
                reserva.setCodigoMesa(codigoMesaNew);
            }
            if (documentoNew != null) {
                documentoNew = em.getReference(documentoNew.getClass(), documentoNew.getDocumento());
                reserva.setDocumento(documentoNew);
            }
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getCodigoFactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            reserva.setFacturaCollection(facturaCollectionNew);
            Collection<Orden> attachedOrdenCollectionNew = new ArrayList<Orden>();
            for (Orden ordenCollectionNewOrdenToAttach : ordenCollectionNew) {
                ordenCollectionNewOrdenToAttach = em.getReference(ordenCollectionNewOrdenToAttach.getClass(), ordenCollectionNewOrdenToAttach.getCodigoOrden());
                attachedOrdenCollectionNew.add(ordenCollectionNewOrdenToAttach);
            }
            ordenCollectionNew = attachedOrdenCollectionNew;
            reserva.setOrdenCollection(ordenCollectionNew);
            reserva = em.merge(reserva);
            if (codigoMesaOld != null && !codigoMesaOld.equals(codigoMesaNew)) {
                codigoMesaOld.getReservaCollection().remove(reserva);
                codigoMesaOld = em.merge(codigoMesaOld);
            }
            if (codigoMesaNew != null && !codigoMesaNew.equals(codigoMesaOld)) {
                codigoMesaNew.getReservaCollection().add(reserva);
                codigoMesaNew = em.merge(codigoMesaNew);
            }
            if (documentoOld != null && !documentoOld.equals(documentoNew)) {
                documentoOld.getReservaCollection().remove(reserva);
                documentoOld = em.merge(documentoOld);
            }
            if (documentoNew != null && !documentoNew.equals(documentoOld)) {
                documentoNew.getReservaCollection().add(reserva);
                documentoNew = em.merge(documentoNew);
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Reserva oldCodigoReservaOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getCodigoReserva();
                    facturaCollectionNewFactura.setCodigoReserva(reserva);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldCodigoReservaOfFacturaCollectionNewFactura != null && !oldCodigoReservaOfFacturaCollectionNewFactura.equals(reserva)) {
                        oldCodigoReservaOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldCodigoReservaOfFacturaCollectionNewFactura = em.merge(oldCodigoReservaOfFacturaCollectionNewFactura);
                    }
                }
            }
            for (Orden ordenCollectionNewOrden : ordenCollectionNew) {
                if (!ordenCollectionOld.contains(ordenCollectionNewOrden)) {
                    Reserva oldReservaCodigoReservaOfOrdenCollectionNewOrden = ordenCollectionNewOrden.getReservaCodigoReserva();
                    ordenCollectionNewOrden.setReservaCodigoReserva(reserva);
                    ordenCollectionNewOrden = em.merge(ordenCollectionNewOrden);
                    if (oldReservaCodigoReservaOfOrdenCollectionNewOrden != null && !oldReservaCodigoReservaOfOrdenCollectionNewOrden.equals(reserva)) {
                        oldReservaCodigoReservaOfOrdenCollectionNewOrden.getOrdenCollection().remove(ordenCollectionNewOrden);
                        oldReservaCodigoReservaOfOrdenCollectionNewOrden = em.merge(oldReservaCodigoReservaOfOrdenCollectionNewOrden);
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
                Integer id = reserva.getCodigoReserva();
                if (findReserva(id) == null) {
                    throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.");
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
            Reserva reserva;
            try {
                reserva = em.getReference(Reserva.class, id);
                reserva.getCodigoReserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Factura> facturaCollectionOrphanCheck = reserva.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reserva (" + reserva + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable codigoReserva field.");
            }
            Collection<Orden> ordenCollectionOrphanCheck = reserva.getOrdenCollection();
            for (Orden ordenCollectionOrphanCheckOrden : ordenCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reserva (" + reserva + ") cannot be destroyed since the Orden " + ordenCollectionOrphanCheckOrden + " in its ordenCollection field has a non-nullable reservaCodigoReserva field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Mesa codigoMesa = reserva.getCodigoMesa();
            if (codigoMesa != null) {
                codigoMesa.getReservaCollection().remove(reserva);
                codigoMesa = em.merge(codigoMesa);
            }
            Usuario documento = reserva.getDocumento();
            if (documento != null) {
                documento.getReservaCollection().remove(reserva);
                documento = em.merge(documento);
            }
            em.remove(reserva);
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

    public List<Reserva> findReservaEntities() {
        return findReservaEntities(true, -1, -1);
    }

    public List<Reserva> findReservaEntities(int maxResults, int firstResult) {
        return findReservaEntities(false, maxResults, firstResult);
    }

    private List<Reserva> findReservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reserva.class));
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

    public Reserva findReserva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reserva> rt = cq.from(Reserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
