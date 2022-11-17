
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 9/September/2018 15:41:53 EST
 */
@Embeddable
public class BankHasAgencyPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "idBank")
    private String idBank;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAgency")
    private int idAgency;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCurrency")
    private int idCurrency;

    public BankHasAgencyPK() {
    }

    public BankHasAgencyPK(String idBank, int idAgency, int idCurrency) {
        this.idBank = idBank;
        this.idAgency = idAgency;
        this.idCurrency = idCurrency;
    }

    public String getIdBank() {
        return idBank;
    }

    public void setIdBank(String idBank) {
        this.idBank = idBank;
    }

    public int getIdAgency() {
        return idAgency;
    }

    public void setIdAgency(int idAgency) {
        this.idAgency = idAgency;
    }

    public int getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(int idCurrency) {
        this.idCurrency = idCurrency;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBank != null ? idBank.hashCode() : 0);
        hash += (int) idAgency;
        hash += (int) idCurrency;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BankHasAgencyPK)) {
            return false;
        }
        BankHasAgencyPK other = (BankHasAgencyPK) object;
        if ((this.idBank == null && other.idBank != null) || (this.idBank != null && !this.idBank.equals(other.idBank))) {
            return false;
        }
        if (this.idAgency != other.idAgency) {
            return false;
        }
        if (this.idCurrency != other.idCurrency) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.BankHasAgencyPK[ idBank=" + idBank + ", idAgency=" + idAgency + ", idCurrency=" + idCurrency + " ]";
    }
    
}
