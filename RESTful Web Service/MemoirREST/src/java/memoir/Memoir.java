/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoir;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roger
 */
@Entity
@Table(name = "MEMOIR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Memoir.findByMMovieName", query = "SELECT m FROM Memoir m WHERE m.mMovieName = :mMovieName")
    , @NamedQuery(name = "Memoir.findByMMovieReleaseDate", query = "SELECT m FROM Memoir m WHERE m.mMovieReleaseDate = :mMovieReleaseDate")
    , @NamedQuery(name = "Memoir.findByMWatchingDatetime", query = "SELECT m FROM Memoir m WHERE m.mWatchingDatetime = :mWatchingDatetime")
    , @NamedQuery(name = "Memoir.findByMComment", query = "SELECT m FROM Memoir m WHERE m.mComment = :mComment")
    , @NamedQuery(name = "Memoir.findByMRating", query = "SELECT m FROM Memoir m WHERE m.mRating = :mRating")
    , @NamedQuery(name = "Memoir.findByCId", query = "SELECT m FROM Memoir m WHERE m.cId.cId = :cId")
    , @NamedQuery(name = "Memoir.findByPId", query = "SELECT m FROM Memoir m WHERE m.pId.pId = :pId")
    , @NamedQuery(name = "Memoir.findByRatingAndLocationPostcode",
            query = "SELECT m FROM Memoir m WHERE m.mRating = :mRating AND m.cId.cLocationPostcode = :cPostcode")})
public class Memoir implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "M_ID")
    private Integer mId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "M_MOVIE_NAME")
    private String mMovieName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "M_MOVIE_RELEASE_DATE")
    @Temporal(TemporalType.DATE)
    private Date mMovieReleaseDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "M_WATCHING_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mWatchingDatetime;
    @Size(max = 100)
    @Column(name = "M_COMMENT")
    private String mComment;
    @Max(value=5)
    @Min(value=0)
    @Basic(optional = false)
    @NotNull
    @Column(name = "M_RATING")
    private BigDecimal mRating;
    @JoinColumn(name = "C_ID", referencedColumnName = "C_ID")
    @ManyToOne(optional = false)
    private Cinema cId;
    @JoinColumn(name = "P_ID", referencedColumnName = "P_ID")
    @ManyToOne(optional = false)
    private Person pId;

    public Memoir() {
    }

    public Memoir(Integer mId) {
        this.mId = mId;
    }

    public Memoir(Integer mId, String mMovieName, Date mMovieReleaseDate, Date mWatchingDatetime, BigDecimal mRating) {
        this.mId = mId;
        this.mMovieName = mMovieName;
        this.mMovieReleaseDate = mMovieReleaseDate;
        this.mWatchingDatetime = mWatchingDatetime;
        this.mRating = mRating;
    }

    public Integer getMId() {
        return mId;
    }

    public void setMId(Integer mId) {
        this.mId = mId;
    }

    public String getMMovieName() {
        return mMovieName;
    }

    public void setMMovieName(String mMovieName) {
        this.mMovieName = mMovieName;
    }

    public Date getMMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public void setMMovieReleaseDate(Date mMovieReleaseDate) {
        this.mMovieReleaseDate = mMovieReleaseDate;
    }

    public Date getMWatchingDatetime() {
        return mWatchingDatetime;
    }

    public void setMWatchingDatetime(Date mWatchingDatetime) {
        this.mWatchingDatetime = mWatchingDatetime;
    }

    public String getMComment() {
        return mComment;
    }

    public void setMComment(String mComment) {
        this.mComment = mComment;
    }

    public BigDecimal getMRating() {
        return mRating;
    }

    public void setMRating(BigDecimal mRating) {
        this.mRating = mRating;
    }

    public Cinema getCId() {
        return cId;
    }

    public void setCId(Cinema cId) {
        this.cId = cId;
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
        hash += (mId != null ? mId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Memoir)) {
            return false;
        }
        Memoir other = (Memoir) object;
        if ((this.mId == null && other.mId != null) || (this.mId != null && !this.mId.equals(other.mId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "memoir.Memoir[ mId=" + mId + " ]";
    }
    
}
