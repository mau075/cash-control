
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
@Table(name = "vault")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vault.findAll", query = "SELECT v FROM Vault v"),
    @NamedQuery(name = "Vault.findByIdAgency", query = "SELECT v FROM Vault v WHERE v.vaultPK.idAgency = :idAgency"),
    @NamedQuery(name = "Vault.findByCreated", query = "SELECT v FROM Vault v WHERE v.vaultPK.created = :created"),
    @NamedQuery(name = "Vault.findByValue", query = "SELECT v FROM Vault v WHERE v.vaultPK.value = :value"),
    @NamedQuery(name = "Vault.findByIdCurrency", query = "SELECT v FROM Vault v WHERE v.vaultPK.idCurrency = :idCurrency"),
    @NamedQuery(name = "Vault.findByActive", query = "SELECT v FROM Vault v WHERE v.vaultPK.active = :active"),
    @NamedQuery(name = "Vault.findByAmount", query = "SELECT v FROM Vault v WHERE v.amount = :amount"),
    @NamedQuery(name = "Vault.findByMainVault", query = "SELECT v FROM Vault v WHERE v.vaultPK.mainVault = :mainVault")})
public class Vault implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VaultPK vaultPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Amount")
    private BigDecimal amount;
    @JoinColumn(name = "idAgency", referencedColumnName = "idAgency", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Agency agency;
    @JoinColumns({
        @JoinColumn(name = "Value", referencedColumnName = "Value", insertable = false, updatable = false),
        @JoinColumn(name = "idCurrency", referencedColumnName = "idCurrency", insertable = false, updatable = false),
        @JoinColumn(name = "Active", referencedColumnName = "Active", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Cash cash;

    public Vault() {
    }

    public Vault(VaultPK vaultPK) {
        this.vaultPK = vaultPK;
    }

    public Vault(VaultPK vaultPK, BigDecimal amount) {
        this.vaultPK = vaultPK;
        this.amount = amount;
    }

    public Vault(int idAgency, Date created, BigDecimal value, int idCurrency, boolean active, String mainVault) {
        this.vaultPK = new VaultPK(idAgency, created, value, idCurrency, active, mainVault);
    }

    public VaultPK getVaultPK() {
        return vaultPK;
    }

    public void setVaultPK(VaultPK vaultPK) {
        this.vaultPK = vaultPK;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Cash getCash() {
        return cash;
    }

    public void setCash(Cash cash) {
        this.cash = cash;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vaultPK != null ? vaultPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vault)) {
            return false;
        }
        Vault other = (Vault) object;
        if ((this.vaultPK == null && other.vaultPK != null) || (this.vaultPK != null && !this.vaultPK.equals(other.vaultPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Vault[ vaultPK=" + vaultPK + " ]";
    }
    
}
