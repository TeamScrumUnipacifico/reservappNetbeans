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
@Table(name = "menu", catalog = "reservappDos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m"),
    @NamedQuery(name = "Menu.findByCodigoMenu", query = "SELECT m FROM Menu m WHERE m.codigoMenu = :codigoMenu"),
    @NamedQuery(name = "Menu.findByNombre", query = "SELECT m FROM Menu m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Menu.findByPrecio", query = "SELECT m FROM Menu m WHERE m.precio = :precio"),
    @NamedQuery(name = "Menu.findByDescripcion", query = "SELECT m FROM Menu m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Menu.findByImagen", query = "SELECT m FROM Menu m WHERE m.imagen = :imagen"),
    @NamedQuery(name = "Menu.findByEstado", query = "SELECT m FROM Menu m WHERE m.estado = :estado")})
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CodigoMenu", nullable = false)
    private Integer codigoMenu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Precio", nullable = false)
    private int precio;
    @Size(max = 45)
    @Column(name = "Descripcion", length = 45)
    private String descripcion;
    @Size(max = 45)
    @Column(name = "Imagen", length = 45)
    private String imagen;
    @Column(name = "estado")
    private Integer estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menuCodigoMenu")
    private Collection<Orden> ordenCollection;
    @JoinColumn(name = "CodigoEstablecimiento", referencedColumnName = "codigoEstablecimiento", nullable = false)
    @ManyToOne(optional = false)
    private Establecimiento codigoEstablecimiento;

    public Menu() {
    }

    public Menu(Integer codigoMenu) {
        this.codigoMenu = codigoMenu;
    }

    public Menu(Integer codigoMenu, String nombre, int precio) {
        this.codigoMenu = codigoMenu;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Integer getCodigoMenu() {
        return codigoMenu;
    }

    public void setCodigoMenu(Integer codigoMenu) {
        this.codigoMenu = codigoMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<Orden> getOrdenCollection() {
        return ordenCollection;
    }

    public void setOrdenCollection(Collection<Orden> ordenCollection) {
        this.ordenCollection = ordenCollection;
    }

    public Establecimiento getCodigoEstablecimiento() {
        return codigoEstablecimiento;
    }

    public void setCodigoEstablecimiento(Establecimiento codigoEstablecimiento) {
        this.codigoEstablecimiento = codigoEstablecimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoMenu != null ? codigoMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.codigoMenu == null && other.codigoMenu != null) || (this.codigoMenu != null && !this.codigoMenu.equals(other.codigoMenu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Menu[ codigoMenu=" + codigoMenu + " ]";
    }
    
}
