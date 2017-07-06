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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "reserva", catalog = "reservappDos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reserva.findAll", query = "SELECT r FROM Reserva r"),
    @NamedQuery(name = "Reserva.findByCodigoReserva", query = "SELECT r FROM Reserva r WHERE r.codigoReserva = :codigoReserva"),
    @NamedQuery(name = "Reserva.findByUsuarioidUsuario", query = "SELECT r FROM Reserva r WHERE r.usuarioidUsuario = :usuarioidUsuario"),
    @NamedQuery(name = "Reserva.findByFechaReserva", query = "SELECT r FROM Reserva r WHERE r.fechaReserva = :fechaReserva"),
    @NamedQuery(name = "Reserva.findByIdMesa", query = "SELECT r FROM Reserva r WHERE r.idMesa = :idMesa")})
public class Reserva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CodigoReserva", nullable = false)
    private Integer codigoReserva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Usuario_idUsuario", nullable = false)
    private int usuarioidUsuario;
    @Column(name = "Fecha Reserva")
    @Temporal(TemporalType.DATE)
    private Date fechaReserva;
    @Column(name = "IdMesa")
    private Integer idMesa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoReserva")
    private Collection<Factura> facturaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservaCodigoReserva")
    private Collection<Orden> ordenCollection;
    @JoinColumn(name = "CodigoMesa", referencedColumnName = "CodigoMesa", nullable = false)
    @ManyToOne(optional = false)
    private Mesa codigoMesa;
    @JoinColumn(name = "Documento", referencedColumnName = "documento", nullable = false)
    @ManyToOne(optional = false)
    private Usuario documento;

    public Reserva() {
    }

    public Reserva(Integer codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public Reserva(Integer codigoReserva, int usuarioidUsuario) {
        this.codigoReserva = codigoReserva;
        this.usuarioidUsuario = usuarioidUsuario;
    }

    public Integer getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(Integer codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public int getUsuarioidUsuario() {
        return usuarioidUsuario;
    }

    public void setUsuarioidUsuario(int usuarioidUsuario) {
        this.usuarioidUsuario = usuarioidUsuario;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Integer getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Integer idMesa) {
        this.idMesa = idMesa;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    @XmlTransient
    public Collection<Orden> getOrdenCollection() {
        return ordenCollection;
    }

    public void setOrdenCollection(Collection<Orden> ordenCollection) {
        this.ordenCollection = ordenCollection;
    }

    public Mesa getCodigoMesa() {
        return codigoMesa;
    }

    public void setCodigoMesa(Mesa codigoMesa) {
        this.codigoMesa = codigoMesa;
    }

    public Usuario getDocumento() {
        return documento;
    }

    public void setDocumento(Usuario documento) {
        this.documento = documento;
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
