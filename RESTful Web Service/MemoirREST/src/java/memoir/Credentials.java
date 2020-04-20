/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoir;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roger
 */
@Entity
@Table(name = "CREDENTIALS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Credentials.findByPasswordHash", query = "SELECT c FROM Credentials c WHERE c.passwordHash = :passwordHash")
    , @NamedQuery(name = "Credentials.findBySignUpDate", query = "SELECT c FROM Credentials c WHERE c.signUpDate = :signUpDate")
    , @NamedQuery(name = "Credentials.findByPId", query = "SELECT c FROM Credentials c WHERE c.pId.pId = :pId")})
public class Credentials implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "PASSWORD_HASH")
    private String passwordHash;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SIGN_UP_DATE")
    @Temporal(TemporalType.DATE)
    private Date signUpDate;
    @JoinColumn(name = "P_ID", referencedColumnName = "P_ID")
    @OneToOne(optional = false)
    private Person pId;

    public Credentials() {
    }

    public Credentials(String username) {
        this.username = username;
    }

    public Credentials(String username, String passwordHash, Date signUpDate) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.signUpDate = signUpDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(Date signUpDate) {
        this.signUpDate = signUpDate;
    }

    public Person getPId() {
        return pId;
    }

    public void setPId(Person pId) {
        this.pId = pId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credentials)) {
            return false;
        }
        Credentials other = (Credentials) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "memoir.Credentials[ username=" + username + " ]";
    }
    
}
