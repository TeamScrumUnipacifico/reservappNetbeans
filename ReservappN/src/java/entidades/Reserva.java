/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Alumnos
 */
@Entity
@Table(name = "reserva")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reserva.findAll", query = "SELECT r FROM Reserva r")
    , @NamedQuery(name = "Reserva.findByCodigoReserva", query = "SELECT r FROM Reserva r WHERE r.codigoReserva = :codigoReserva")
    , @NamedQuery(name = "Reserva.findByFechaReserva", query = "SELECT r FROM Reserva r WHERE r.fechaReserva = :fechaReserva")
    , @NamedQuery(name = "Reserva.findByComentario", query = "SELECT r FROM Reserva r WHERE r.comentario = :comentario")
    , @NamedQuery(name = "Reserva.findByUsuarioDocumento", query = "SELECT r FROM Reserva r WHERE r.usuarioDocumento = :usuarioDocumento")})
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CodigoReserva", nullable = false)
    private Integer codigoReserva;
    @Column(name = "Fecha Reserva")
    @Temporal(TemporalType.DATE)
    private Date fechaReserva;
    @Size(max = 300)
    @Column(name = "comentario", length = 300)
    private String comentario;
    @Size(max = 15)
    @Column(name = "usuario_documento", length = 15)
    private String usuarioDocumento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoReserva")
    private Collection<Factura> facturaCollection;
    @JoinColumn(name = "CodigoMesa", referencedColumnName = "CodigoMesa")
    @ManyToOne
    private Mesa codigoMesa;
    @JoinColumn(name = "orden_CodigoOrden", referencedColumnName = "CodigoOrden")
    @ManyToOne
    private Orden ordenCodigoOrden;

    public Reserva() {
    }

    public Reserva(Integer codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public Integer getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(Integer codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUsuarioDocumento() {
        return usuarioDocumento;
    }

    public void setUsuarioDocumento(String usuarioDocumento) {
        this.usuarioDocumento = usuarioDocumento;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    public Mesa getCodigoMesa() {
        return codigoMesa;
    }

    public void setCodigoMesa(Mesa codigoMesa) {
        this.codigoMesa = codigoMesa;
    }

    public Orden getOrdenCodigoOrden() {
        return ordenCodigoOrden;
    }

    public void setOrdenCodigoOrden(Orden ordenCodigoOrden) {
        this.ordenCodigoOrden = ordenCodigoOrden;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoReserva != null ? codigoReserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reserva)) {
            return false;
        }
        Reserva other = (Reserva) object;
        if ((this.codigoReserva == null && other.codigoReserva != null) || (this.codigoReserva != null && !this.codigoReserva.equals(other.codigoReserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Reserva[ codigoReserva=" + codigoReserva + " ]";
    }
    
}
