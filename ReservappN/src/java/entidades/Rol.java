/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r")
    , @NamedQuery(name = "Rol.findByCodigodelRol", query = "SELECT r FROM Rol r WHERE r.codigodelRol = :codigodelRol")
    , @NamedQuery(name = "Rol.findByNombre", query = "SELECT r FROM Rol r WHERE r.nombre = :nombre")})
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigodelRol", nullable = false)
    private Integer codigodelRol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;
    @OneToMany(mappedBy = "rolcodigodelRol")
    private Collection<Usuario> usuarioCollection;

    public Rol() {
    }

    public Rol(Integer codigodelRol) {
        this.codigodelRol = codigodelRol;
    }

    public Rol(Integer codigodelRol, String nombre) {
        this.codigodelRol = codigodelRol;
        this.nombre = nombre;
    }

    public Integer getCodigodelRol() {
        return codigodelRol;
    }

    public void setCodigodelRol(Integer codigodelRol) {
        this.codigodelRol = codigodelRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigodelRol != null ? codigodelRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.codigodelRol == null && other.codigodelRol != null) || (this.codigodelRol != null && !this.codigodelRol.equals(other.codigodelRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Rol[ codigodelRol=" + codigodelRol + " ]";
    }
    
}
