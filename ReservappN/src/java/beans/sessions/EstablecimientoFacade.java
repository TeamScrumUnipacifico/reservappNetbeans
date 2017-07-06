/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.sessions;

import entidades.Establecimiento;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Alumnos
 */
@Stateless
public class EstablecimientoFacade extends AbstractFacade<Establecimiento> {

    @PersistenceContext(unitName = "ReservappNPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstablecimientoFacade() {
        super(Establecimiento.class);
    }
    
}
