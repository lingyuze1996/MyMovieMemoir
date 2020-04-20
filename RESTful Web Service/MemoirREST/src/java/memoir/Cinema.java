/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoir;

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
 * @author Roger
 */
@Entity
@Table(name = "CINEMA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cinema.findByCName", query = "SELECT c FROM Cinema c WHERE c.cName = :cName")
    , @NamedQuery(name = "Cinema.findByCLocationPostcode", query = "SELECT c FROM Cinema c WHERE c.cLocationPostcode = :cLocationPostcode")})
public class Cinema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "C_ID")
    private Integer cId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "C_NAME")
    private String cName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "C_LOCATION_POSTCODE")
    private String cLocationPostcode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cId")
    private Collection<Memoir> memoirCollection;

    public Cinema() {
    }

    public Cinema(Integer cId) {
        this.cId = cId;
    }

    public Cinema(Integer cId, String cName, String cLocationPostcode) {
        this.cId = cId;
        this.cName = cName;
        this.cLocationPostcode = cLocationPostcode;
    }

    public Integer getCId() {
        return cId;
    }

    public void setCId(Integer cId) {
        this.cId = cId;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getCLocationPostcode() {
        return cLocationPostcode;
    }

    public void setCLocationPostcode(String cLocationPostcode) {
        this.cLocationPostcode = cLocationPostcode;
    }

    @XmlTransient
    public Collection<Memoir> getMemoirCollection() {
        return memoirCollection;
    }

    public void setMemoirCollection(Collection<Memoir> memoirCollection) {
        this.memoirCollection = memoirCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cId != null ? cId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cinema)) {
            return false;
        }
        Cinema other = (Cinema) object;
        if ((this.cId == null && other.cId != null) || (this.cId != null && !this.cId.equals(other.cId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "memoir.Cinema[ cId=" + cId + " ]";
    }
    
}
