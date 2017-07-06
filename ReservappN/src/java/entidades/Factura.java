/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alumnos
 */
@Entity
@Table(name = "factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f")
    , @NamedQuery(name = "Factura.findByCodigoFactura", query = "SELECT f FROM Factura f WHERE f.codigoFactura = :codigoFactura")
    , @NamedQuery(name = "Factura.findByMetodoPago", query = "SELECT f FROM Factura f WHERE f.metodoPago = :metodoPago")
    , @NamedQuery(name = "Factura.findByValorTotal", query = "SELECT f FROM Factura f WHERE f.valorTotal = :valorTotal")
    , @NamedQuery(name = "Factura.findByEstadoPago", query = "SELECT f FROM Factura f WHERE f.estadoPago = :estadoPago")})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CodigoFactura", nullable = false)
    private Integer codigoFactura;
    @Size(max = 45)
    @Column(name = "MetodoPago", length = 45)
    private String metodoPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ValorTotal", precision = 12)
    private Float valorTotal;
    @Column(name = "EstadoPago")
    private Boolean estadoPago;
    @JoinColumn(name = "CodigoReserva", referencedColumnName = "CodigoReserva", nullable = false)
    @ManyToOne(optional = false)
    private Reserva codigoReserva;

    public Factura() {
    }

    public Factura(Integer codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

    public Integer getCodigoFactura() {
        return codigoFactura;
    }

    public void setCodigoFactura(Integer codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Boolean getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(Boolean estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Reserva getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(Reserva codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoFactura != null ? codigoFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.codigoFactura == null && other.codigoFactura != null) || (this.codigoFactura != null && !this.codigoFactura.equals(other.codigoFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Factura[ codigoFactura=" + codigoFactura + " ]";
    }
    
}
