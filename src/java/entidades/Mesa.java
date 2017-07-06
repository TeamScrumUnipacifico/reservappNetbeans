/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "mesa", catalog = "reservappDos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mesa.findAll", query = "SELECT m FROM Mesa m"),
    @NamedQuery(name = "Mesa.findByCodigoMesa", query = "SELECT m FROM Mesa m WHERE m.codigoMesa = :codigoMesa"),
    @NamedQuery(name = "Mesa.findByPuestos", query = "SELECT m FROM Mesa m WHERE m.puestos = :puestos"),
    @NamedQuery(name = "Mesa.findByUbicacion", query = "SELECT m FROM Mesa m WHERE m.ubicacion = :ubicacion"),
    @NamedQuery(name = "Mesa.findByEstado", query = "SELECT m FROM Mesa m WHERE m.estado = :estado")})
public class Mesa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CodigoMesa", nullable = false)
    private Integer codigoMesa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Puestos", nullable = false)
    private int puestos;
    @Size(max = 45)
    @Column(name = "Ubicacion", length = 45)
    private String ubicacion;
    @Column(name = "Estado")
    private Boolean estado;
    @JoinColumn(name = "codigoEstablecimiento", referencedColumnName = "codigoEstablecimiento", nullable = false)
    @ManyToOne(optional = false)
    private Establecimiento codigoEstablecimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoMesa")
    private Collection<Reserva> reservaCollection;

    public Mesa() {
    }

    public Mesa(Integer codigoMesa) {
        this.codigoMesa = codigoMesa;
    }

    public Mesa(Integer codigoMesa, int puestos) {
        this.codigoMesa = codigoMesa;
        this.puestos = puestos;
    }

    public Integer getCodigoMesa() {
        return codigoMesa;
    }

    public void setCodigoMesa(Integer codigoMesa) {
        this.codigoMesa = codigoMesa;
    }

    public int getPuestos() {
        return puestos;
    }

    public void setPuestos(int puestos) {
        this.puestos = puestos;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Establecimiento getCodigoEstablecimiento() {
        return codigoEstablecimiento;
    }

    public void setCodigoEstablecimiento(Establecimiento codigoEstablecimiento) {
        this.codigoEstablecimiento = codigoEstablecimiento;
    }

    @XmlTransient
    public Collection<Reserva> getReservaCollection() {
        return reservaCollection;
    }

    public void setReservaCollection(Collection<Reserva> reservaCollection) {
        this.reservaCollection = reservaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoMesa != null ? codigoMesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mesa)) {
            return false;
        }
        Mesa other = (Mesa) object;
        if ((this.codigoMesa == null && other.codigoMesa != null) || (this.codigoMesa != null && !this.codigoMesa.equals(other.codigoMesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Mesa[ codigoMesa=" + codigoMesa + " ]";
    }
    
}
