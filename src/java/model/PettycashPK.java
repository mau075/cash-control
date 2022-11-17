
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 9/September/2018 15:41:53 EST
 */
@Embeddable
public class PettycashPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAgency")
    private int idAgency;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "Code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCurrency")
    private int idCurrency;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Created")
    @Temporal(TemporalType.DATE)
    private Date created;

    public PettycashPK() {
    }

    public PettycashPK(int idAgency, String code, int idCurrency, Date created) {
        this.idAgency = idAgency;
        this.code = code;
        this.idCurrency = idCurrency;
        this.created = created;
    }

    public int getIdAgency() {
        return idAgency;
    }

    public void setIdAgency(int idAgency) {
        this.idAgency = idAgency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(int idCurrency) {
        this.idCurrency = idCurrency;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idAgency;
        hash += (code != null ? code.hashCode() : 0);
        hash += (int) idCurrency;
        hash += (created != null ? created.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PettycashPK)) {
            return false;
        }
        PettycashPK other = (PettycashPK) object;
        if (this.idAgency != other.idAgency) {
            return false;
        }
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        if (this.idCurrency != other.idCurrency) {
            return false;
        }
        if ((this.created == null && other.created != null) || (this.created != null && !this.created.equals(other.created))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PettycashPK[ idAgency=" + idAgency + ", code=" + code + ", idCurrency=" + idCurrency + ", created=" + created + " ]";
    }
    
}
