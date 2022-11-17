
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 9/September/2018 15:41:53 EST
 */
@Embeddable
public class CashPK implements Serializable {
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

    public CashPK() {
    }

    public CashPK(BigDecimal value, int idCurrency, boolean active) {
        this.value = value;
        this.idCurrency = idCurrency;
        this.active = active;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (value != null ? value.hashCode() : 0);
        hash += (int) idCurrency;
        hash += (active ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CashPK)) {
            return false;
        }
        CashPK other = (CashPK) object;
        if ((this.value == null && other.value != null) || (this.value != null && !this.value.equals(other.value))) {
            return false;
        }
        if (this.idCurrency != other.idCurrency) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CashPK[ value=" + value + ", idCurrency=" + idCurrency + ", active=" + active + " ]";
    }
    
}
