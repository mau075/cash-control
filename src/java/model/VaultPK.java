
package model;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class VaultPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAgency")
    private int idAgency;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Created")
    @Temporal(TemporalType.DATE)
    private Date created;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Value")
    private BigDecimal value;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCurrency")
    private int idCurrency;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Active")
    private boolean active;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "MainVault")
    private String mainVault;

    public VaultPK() {
    }

    public VaultPK(int idAgency, Date created, BigDecimal value, int idCurrency, boolean active, String mainVault) {
        this.idAgency = idAgency;
        this.created = created;
        this.value = value;
        this.idCurrency = idCurrency;
        this.active = active;
        this.mainVault = mainVault;
    }

    public int getIdAgency() {
        return idAgency;
    }

    public void setIdAgency(int idAgency) {
        this.idAgency = idAgency;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public int getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(int idCurrency) {
        this.idCurrency = idCurrency;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMainVault() {
        return mainVault;
    }

    public void setMainVault(String mainVault) {
        this.mainVault = mainVault;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idAgency;
        hash += (created != null ? created.hashCode() : 0);
        hash += (value != null ? value.hashCode() : 0);
        hash += (int) idCurrency;
        hash += (active ? 1 : 0);
        hash += (mainVault != null ? mainVault.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VaultPK)) {
            return false;
        }
        VaultPK other = (VaultPK) object;
        if (this.idAgency != other.idAgency) {
            return false;
        }
        if ((this.created == null && other.created != null) || (this.created != null && !this.created.equals(other.created))) {
            return false;
        }
        if ((this.value == null && other.value != null) || (this.value != null && !this.value.equals(other.value))) {
            return false;
        }
        if (this.idCurrency != other.idCurrency) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        if ((this.mainVault == null && other.mainVault != null) || (this.mainVault != null && !this.mainVault.equals(other.mainVault))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.VaultPK[ idAgency=" + idAgency + ", created=" + created + ", value=" + value + ", idCurrency=" + idCurrency + ", active=" + active + ", mainVault=" + mainVault + " ]";
    }
    
}
