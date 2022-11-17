
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 9/September/2018 15:41:53 EST
 */
@Entity
@Table(name = "bank_has_agency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BankHasAgency.findAll", query = "SELECT b FROM BankHasAgency b"),
    @NamedQuery(name = "BankHasAgency.findByIdBank", query = "SELECT b FROM BankHasAgency b WHERE b.bankHasAgencyPK.idBank = :idBank"),
    @NamedQuery(name = "BankHasAgency.findByIdAgency", query = "SELECT b FROM BankHasAgency b WHERE b.bankHasAgencyPK.idAgency = :idAgency"),
    @NamedQuery(name = "BankHasAgency.findByTotal", query = "SELECT b FROM BankHasAgency b WHERE b.total = :total"),
    @NamedQuery(name = "BankHasAgency.findByIdCurrency", query = "SELECT b FROM BankHasAgency b WHERE b.bankHasAgencyPK.idCurrency = :idCurrency")})
public class BankHasAgency implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BankHasAgencyPK bankHasAgencyPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Total")
    private BigDecimal total;
    @JoinColumn(name = "idAgency", referencedColumnName = "idAgency", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Agency agency;
    @JoinColumn(name = "idBank", referencedColumnName = "idBank", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Bank bank;
    @JoinColumn(name = "idCurrency", referencedColumnName = "idCurrency", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Currency currency;

    public BankHasAgency() {
    }

    public BankHasAgency(BankHasAgencyPK bankHasAgencyPK) {
        this.bankHasAgencyPK = bankHasAgencyPK;
    }

    public BankHasAgency(BankHasAgencyPK bankHasAgencyPK, BigDecimal total) {
        this.bankHasAgencyPK = bankHasAgencyPK;
        this.total = total;
    }

    public BankHasAgency(String idBank, int idAgency, int idCurrency) {
        this.bankHasAgencyPK = new BankHasAgencyPK(idBank, idAgency, idCurrency);
    }

    public BankHasAgencyPK getBankHasAgencyPK() {
        return bankHasAgencyPK;
    }

    public void setBankHasAgencyPK(BankHasAgencyPK bankHasAgencyPK) {
        this.bankHasAgencyPK = bankHasAgencyPK;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bankHasAgencyPK != null ? bankHasAgencyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BankHasAgency)) {
            return false;
        }
        BankHasAgency other = (BankHasAgency) object;
        if ((this.bankHasAgencyPK == null && other.bankHasAgencyPK != null) || (this.bankHasAgencyPK != null && !this.bankHasAgencyPK.equals(other.bankHasAgencyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.BankHasAgency[ bankHasAgencyPK=" + bankHasAgencyPK + " ]";
    }
    
}
