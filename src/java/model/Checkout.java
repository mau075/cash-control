
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 9/September/2018 15:41:53 EST
 */
@Entity
@Table(name = "checkout")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Checkout.findAll", query = "SELECT c FROM Checkout c"),
    @NamedQuery(name = "Checkout.findByCode", query = "SELECT c FROM Checkout c WHERE c.checkoutPK.code = :code"),
    @NamedQuery(name = "Checkout.findByIdAgency", query = "SELECT c FROM Checkout c WHERE c.checkoutPK.idAgency = :idAgency"),
    @NamedQuery(name = "Checkout.findByCashier", query = "SELECT c FROM Checkout c WHERE c.cashier = :cashier")})
public class Checkout implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CheckoutPK checkoutPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Cashier")
    private String cashier;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checkout")
    private Collection<Pettycash> pettycashCollection;
    @JoinColumn(name = "idAgency", referencedColumnName = "idAgency", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Agency agency;

    public Checkout() {
    }

    public Checkout(CheckoutPK checkoutPK) {
        this.checkoutPK = checkoutPK;
    }

    public Checkout(CheckoutPK checkoutPK, String cashier) {
        this.checkoutPK = checkoutPK;
        this.cashier = cashier;
    }

    public Checkout(String code, int idAgency) {
        this.checkoutPK = new CheckoutPK(code, idAgency);
    }

    public CheckoutPK getCheckoutPK() {
        return checkoutPK;
    }

    public void setCheckoutPK(CheckoutPK checkoutPK) {
        this.checkoutPK = checkoutPK;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    @XmlTransient
    public Collection<Pettycash> getPettycashCollection() {
        return pettycashCollection;
    }

    public void setPettycashCollection(Collection<Pettycash> pettycashCollection) {
        this.pettycashCollection = pettycashCollection;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (checkoutPK != null ? checkoutPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Checkout)) {
            return false;
        }
        Checkout other = (Checkout) object;
        if ((this.checkoutPK == null && other.checkoutPK != null) || (this.checkoutPK != null && !this.checkoutPK.equals(other.checkoutPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Checkout[ checkoutPK=" + checkoutPK + " ]";
    }
    
}
