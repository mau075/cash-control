
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 9/September/2018 15:41:53 EST
 */
@Entity
@Table(name = "cash")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cash.findAll", query = "SELECT c FROM Cash c"),
    @NamedQuery(name = "Cash.findByValue", query = "SELECT c FROM Cash c WHERE c.cashPK.value = :value"),
    @NamedQuery(name = "Cash.findByIdCurrency", query = "SELECT c FROM Cash c WHERE c.cashPK.idCurrency = :idCurrency"),
    @NamedQuery(name = "Cash.findByActive", query = "SELECT c FROM Cash c WHERE c.cashPK.active = :active")})
public class Cash implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CashPK cashPK;
    @JoinColumn(name = "idCurrency", referencedColumnName = "idCurrency", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Currency currency;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cash")
    private Collection<Vault> vaultCollection;

    public Cash() {
    }

    public Cash(CashPK cashPK) {
        this.cashPK = cashPK;
    }

    public Cash(BigDecimal value, int idCurrency, boolean active) {
        this.cashPK = new CashPK(value, idCurrency, active);
    }

    public CashPK getCashPK() {
        return cashPK;
    }

    public void setCashPK(CashPK cashPK) {
        this.cashPK = cashPK;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @XmlTransient
    public Collection<Vault> getVaultCollection() {
        return vaultCollection;
    }

    public void setVaultCollection(Collection<Vault> vaultCollection) {
        this.vaultCollection = vaultCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cashPK != null ? cashPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cash)) {
            return false;
        }
        Cash other = (Cash) object;
        if ((this.cashPK == null && other.cashPK != null) || (this.cashPK != null && !this.cashPK.equals(other.cashPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Cash[ cashPK=" + cashPK + " ]";
    }
    
}
