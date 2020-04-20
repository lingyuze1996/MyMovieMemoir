/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoir;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Roger
 */
@Entity
@Table(name = "PERSON")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Person.findByPFirstName", query = "SELECT p FROM Person p WHERE p.pFirstName = :pFirstName")
    , @NamedQuery(name = "Person.findByPSurname", query = "SELECT p FROM Person p WHERE p.pSurname = :pSurname")
    , @NamedQuery(name = "Person.findByPGender", query = "SELECT p FROM Person p WHERE p.pGender = :pGender")
    , @NamedQuery(name = "Person.findByPDob", query = "SELECT p FROM Person p WHERE p.pDob = :pDob")
    , @NamedQuery(name = "Person.findByPAddress", query = "SELECT p FROM Person p WHERE p.pAddress = :pAddress")
    , @NamedQuery(name = "Person.findByPState", query = "SELECT p FROM Person p WHERE p.pState = :pState")
    , @NamedQuery(name = "Person.findByPPostcode", query = "SELECT p FROM Person p WHERE p.pPostcode = :pPostcode")})
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "P_ID")
    private Integer pId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "P_FIRST_NAME")
    private String pFirstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "P_SURNAME")
    private String pSurname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "P_GENDER")
    private String pGender;
    @Basic(optional = false)
    @NotNull
    @Column(name = "P_DOB")
    @Temporal(TemporalType.DATE)
    private Date pDob;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "P_ADDRESS")
    private String pAddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "P_STATE")
    private String pState;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "P_POSTCODE")
    private String pPostcode;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pId")
    private Credentials credentials;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pId")
    private Collection<Memoir> memoirCollection;

    public Person() {
    }

    public Person(Integer pId) {
        this.pId = pId;
    }

    public Person(Integer pId, String pFirstName, String pSurname, String pGender, Date pDob, String pAddress, String pState, String pPostcode) {
        this.pId = pId;
        this.pFirstName = pFirstName;
        this.pSurname = pSurname;
        this.pGender = pGender;
        this.pDob = pDob;
        this.pAddress = pAddress;
        this.pState = pState;
        this.pPostcode = pPostcode;
    }

    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public String getPFirstName() {
        return pFirstName;
    }

    public void setPFirstName(String pFirstName) {
        this.pFirstName = pFirstName;
    }

    public String getPSurname() {
        return pSurname;
    }

    public void setPSurname(String pSurname) {
        this.pSurname = pSurname;
    }

    public String getPGender() {
        return pGender;
    }

    public void setPGender(String pGender) {
        this.pGender = pGender;
    }

    public Date getPDob() {
        return pDob;
    }

    public void setPDob(Date pDob) {
        this.pDob = pDob;
    }

    public String getPAddress() {
        return pAddress;
    }

    public void setPAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public String getPState() {
        return pState;
    }

    public void setPState(String pState) {
        this.pState = pState;
    }

    public String getPPostcode() {
        return pPostcode;
    }

    public void setPPostcode(String pPostcode) {
        this.pPostcode = pPostcode;
    }

    @XmlTransient
    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
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
        hash += (pId != null ? pId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.pId == null && other.pId != null) || (this.pId != null && !this.pId.equals(other.pId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "memoir.Person[ pId=" + pId + " ]";
    }
    
}
