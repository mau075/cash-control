
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "pettycash")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pettycash.findAll", query = "SELECT p FROM Pettycash p"),
    @NamedQuery(name = "Pettycash.findByIdAgency", query = "SELECT p FROM Pettycash p WHERE p.pettycashPK.idAgency = :idAgency"),
    @NamedQuery(name = "Pettycash.findByCode", query = "SELECT p FROM Pettycash p WHERE p.pettycashPK.code = :code"),
    @NamedQuery(name = "Pettycash.findByIdCurrency", query = "SELECT p FROM Pettycash p WHERE p.pettycashPK.idCurrency = :idCurrency"),
    @NamedQuery(name = "Pettycash.findByCreated", query = "SELECT p FROM Pettycash p WHERE p.pettycashPK.created = :created"),
    @NamedQuery(name = "Pettycash.findByAmount", query = "SELECT p FROM Pettycash p WHERE p.amount = :amount")})
public class Pettycash implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PettycashPK pettycashPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Amount")
    private BigDecimal amount;
    @JoinColumns({
        @JoinColumn(name = "Code", referencedColumnName = "Code", insertable = false, updatable = false),
        @JoinColumn(name = "idAgency", referencedColumnName = "idAgency", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Checkout checkout;
    @JoinColumn(name = "idCurrency", referencedColumnName = "idCurrency", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Currency currency;

    public Pettycash() {
    }

    public Pettycash(PettycashPK pettycashPK) {
        this.pettycashPK = pettycashPK;
    }

    public Pettycash(PettycashPK pettycashPK, BigDecimal amount) {
        this.pettycashPK = pettycashPK;
        this.amount = amount;
    }

    public Pettycash(int idAgency, String code, int idCurrency, Date created) {
        this.pettycashPK = new PettycashPK(idAgency, code, idCurrency, created);
    }

    public PettycashPK getPettycashPK() {
        return pettycashPK;
    }

    public void setPettycashPK(PettycashPK pettycashPK) {
        this.pettycashPK = pettycashPK;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Checkout getCheckout() {
        return checkout;
    }

    public void setCheckout(Checkout checkout) {
        this.checkout = checkout;
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
        hash += (pettycashPK != null ? pettycashPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pettycash)) {
            return false;
        }
        Pettycash other = (Pettycash) object;
        if ((this.pettycashPK == null && other.pettycashPK != null) || (this.pettycashPK != null && !this.pettycashPK.equals(other.pettycashPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Pettycash[ pettycashPK=" + pettycashPK + " ]";
    }
    
}
