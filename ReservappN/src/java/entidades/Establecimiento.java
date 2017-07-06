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
 * @author Alumnos
 */
@Entity
@Table(name = "establecimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Establecimiento.findAll", query = "SELECT e FROM Establecimiento e")
    , @NamedQuery(name = "Establecimiento.findByCodigoEstablecimiento", query = "SELECT e FROM Establecimiento e WHERE e.codigoEstablecimiento = :codigoEstablecimiento")
    , @NamedQuery(name = "Establecimiento.findByNombre", query = "SELECT e FROM Establecimiento e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Establecimiento.findByNit", query = "SELECT e FROM Establecimiento e WHERE e.nit = :nit")
    , @NamedQuery(name = "Establecimiento.findByDireccion", query = "SELECT e FROM Establecimiento e WHERE e.direccion = :direccion")
    , @NamedQuery(name = "Establecimiento.findByCorreo", query = "SELECT e FROM Establecimiento e WHERE e.correo = :correo")
    , @NamedQuery(name = "Establecimiento.findByTelefono", query = "SELECT e FROM Establecimiento e WHERE e.telefono = :telefono")
    , @NamedQuery(name = "Establecimiento.findByLongitud", query = "SELECT e FROM Establecimiento e WHERE e.longitud = :longitud")
    , @NamedQuery(name = "Establecimiento.findByLatitud", query = "SELECT e FROM Establecimiento e WHERE e.latitud = :latitud")
    , @NamedQuery(name = "Establecimiento.findByMesas", query = "SELECT e FROM Establecimiento e WHERE e.mesas = :mesas")
    , @NamedQuery(name = "Establecimiento.findByDocumento", query = "SELECT e FROM Establecimiento e WHERE e.documento = :documento")})
public class Establecimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigoEstablecimiento", nullable = false)
    private Integer codigoEstablecimiento;
    @Size(max = 45)
    @Column(name = "Nombre", length = 45)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "Nit", nullable = false, length = 9)
    private String nit;
    @Size(max = 45)
    @Column(name = "Direccion", length = 45)
    private String direccion;
    @Size(max = 45)
    @Column(name = "Correo", length = 45)
    private String correo;
    @Size(max = 45)
    @Column(name = "Telefono", length = 45)
    private String telefono;
    @Size(max = 45)
    @Column(name = "Longitud", length = 45)
    private String longitud;
    @Size(max = 45)
    @Column(name = "Latitud", length = 45)
    private String latitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Mesas", nullable = false)
    private int mesas;
    @Size(max = 65)
    @Column(name = "documento", length = 65)
    private String documento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoEstablecimiento")
    private Collection<Mesa> mesaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoEstablecimiento")
    private Collection<Menu> menuCollection;

    public Establecimiento() {
    }

    public Establecimiento(Integer codigoEstablecimiento) {
        this.codigoEstablecimiento = codigoEstablecimiento;
    }

    public Establecimiento(Integer codigoEstablecimiento, String nit, int mesas) {
        this.codigoEstablecimiento = codigoEstablecimiento;
        this.nit = nit;
        this.mesas = mesas;
    }

    public Integer getCodigoEstablecimiento() {
        return codigoEstablecimiento;
    }

    public void setCodigoEstablecimiento(Integer codigoEstablecimiento) {
        this.codigoEstablecimiento = codigoEstablecimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public int getMesas() {
        return mesas;
    }

    public void setMesas(int mesas) {
        this.mesas = mesas;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @XmlTransient
    public Collection<Mesa> getMesaCollection() {
        return mesaCollection;
    }

    public void setMesaCollection(Collection<Mesa> mesaCollection) {
        this.mesaCollection = mesaCollection;
    }

    @XmlTransient
    public Collection<Menu> getMenuCollection() {
        return menuCollection;
    }

    public void setMenuCollection(Collection<Menu> menuCollection) {
        this.menuCollection = menuCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoEstablecimiento != null ? codigoEstablecimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Establecimiento)) {
            return false;
        }
        Establecimiento other = (Establecimiento) object;
        if ((this.codigoEstablecimiento == null && other.codigoEstablecimiento != null) || (this.codigoEstablecimiento != null && !this.codigoEstablecimiento.equals(other.codigoEstablecimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Establecimiento[ codigoEstablecimiento=" + codigoEstablecimiento + " ]";
    }
    
}
