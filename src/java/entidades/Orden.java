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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "orden", catalog = "reservappDos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orden.findAll", query = "SELECT o FROM Orden o"),
    @NamedQuery(name = "Orden.findByCodigoOrden", query = "SELECT o FROM Orden o WHERE o.codigoOrden = :codigoOrden"),
    @NamedQuery(name = "Orden.findByNombre", query = "SELECT o FROM Orden o WHERE o.nombre = :nombre"),
    @NamedQuery(name = "Orden.findByPrecio", query = "SELECT o FROM Orden o WHERE o.precio = :precio"),
    @NamedQuery(name = "Orden.findByDescripcion", query = "SELECT o FROM Orden o WHERE o.descripcion = :descripcion"),
    @NamedQuery(name = "Orden.findByImagen", query = "SELECT o FROM Orden o WHERE o.imagen = :imagen"),
    @NamedQuery(name = "Orden.findByEstado", query = "SELECT o FROM Orden o WHERE o.estado = :estado")})
public class Orden implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CodigoOrden", nullable = false)
    private Integer codigoOrden;
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
    @JoinColumn(name = "menu_CodigoMenu", referencedColumnName = "CodigoMenu", nullable = false)
    @ManyToOne(optional = false)
    private Menu menuCodigoMenu;
    @JoinColumn(name = "reserva_CodigoReserva", referencedColumnName = "CodigoReserva", nullable = false)
    @ManyToOne(optional = false)
    private Reserva reservaCodigoReserva;

    public Orden() {
    }

    public Orden(Integer codigoOrden) {
        this.codigoOrden = codigoOrden;
    }

    public Orden(Integer codigoOrden, String nombre, int precio) {
        this.codigoOrden = codigoOrden;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Integer getCodigoOrden() {
        return codigoOrden;
    }

    public void setCodigoOrden(Integer codigoOrden) {
        this.codigoOrden = codigoOrden;
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

    public Menu getMenuCodigoMenu() {
        return menuCodigoMenu;
    }

    public void setMenuCodigoMenu(Menu menuCodigoMenu) {
        this.menuCodigoMenu = menuCodigoMenu;
    }

    public Reserva getReservaCodigoReserva() {
        return reservaCodigoReserva;
    }

    public void setReservaCodigoReserva(Reserva reservaCodigoReserva) {
        this.reservaCodigoReserva = reservaCodigoReserva;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoOrden != null ? codigoOrden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orden)) {
            return false;
        }
        Orden other = (Orden) object;
        if ((this.codigoOrden == null && other.codigoOrden != null) || (this.codigoOrden != null && !this.codigoOrden.equals(other.codigoOrden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Orden[ codigoOrden=" + codigoOrden + " ]";
    }
    
}
