
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "currency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Currency.findAll", query = "SELECT c FROM Currency c"),
    @NamedQuery(name = "Currency.findByIdCurrency", query = "SELECT c FROM Currency c WHERE c.idCurrency = :idCurrency"),
    @NamedQuery(name = "Currency.findByName", query = "SELECT c FROM Currency c WHERE c.name = :name"),
    @NamedQuery(name = "Currency.findBySymbol", query = "SELECT c FROM Currency c WHERE c.symbol = :symbol")})
public class Currency implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCurrency")
    private Integer idCurrency;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Symbol")
    private String symbol;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "currency")
    private Collection<BankHasAgency> bankHasAgencyCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "currency")
    private Collection<Pettycash> pettycashCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "currency")
    private Collection<Cash> cashCollection;

    public Currency() {
    }

    public Currency(Integer idCurrency) {
        this.idCurrency = idCurrency;
    }

    public Currency(Integer idCurrency, String name, String symbol) {
        this.idCurrency = idCurrency;
        this.name = name;
        this.symbol = symbol;
    }

    public Integer getIdCurrency() {
        return idCurrency;
    }

    public void setIdCurrency(Integer idCurrency) {
        this.idCurrency = idCurrency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @XmlTransient
    public Collection<BankHasAgency> getBankHasAgencyCollection() {
        return bankHasAgencyCollection;
    }

    public void setBankHasAgencyCollection(Collection<BankHasAgency> bankHasAgencyCollection) {
        this.bankHasAgencyCollection = bankHasAgencyCollection;
    }

    @XmlTransient
    public Collection<Pettycash> getPettycashCollection() {
        return pettycashCollection;
    }

    public void setPettycashCollection(Collection<Pettycash> pettycashCollection) {
        this.pettycashCollection = pettycashCollection;
    }

    @XmlTransient
    public Collection<Cash> getCashCollection() {
        return cashCollection;
    }

    public void setCashCollection(Collection<Cash> cashCollection) {
        this.cashCollection = cashCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCurrency != null ? idCurrency.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Currency)) {
            return false;
        }
        Currency other = (Currency) object;
        if ((this.idCurrency == null && other.idCurrency != null) || (this.idCurrency != null && !this.idCurrency.equals(other.idCurrency))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Currency[ idCurrency=" + idCurrency + " ]";
    }
    
}
