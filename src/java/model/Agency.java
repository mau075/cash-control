
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "agency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agency.findAll", query = "SELECT a FROM Agency a"),
    @NamedQuery(name = "Agency.findByIdAgency", query = "SELECT a FROM Agency a WHERE a.idAgency = :idAgency"),
    @NamedQuery(name = "Agency.findByName", query = "SELECT a FROM Agency a WHERE a.name = :name"),
    @NamedQuery(name = "Agency.findByAddress", query = "SELECT a FROM Agency a WHERE a.address = :address")})
public class Agency implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAgency")
    private Integer idAgency;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Address")
    private String address;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agency")
    private Collection<BankHasAgency> bankHasAgencyCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAgency")
    private Collection<Usuario> usuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agency")
    private Collection<Checkout> checkoutCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agency")
    private Collection<Vault> vaultCollection;

    public Agency() {
    }

    public Agency(Integer idAgency) {
        this.idAgency = idAgency;
    }

    public Agency(Integer idAgency, String name, String address) {
        this.idAgency = idAgency;
        this.name = name;
        this.address = address;
    }

    public Integer getIdAgency() {
        return idAgency;
    }

    public void setIdAgency(Integer idAgency) {
        this.idAgency = idAgency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlTransient
    public Collection<BankHasAgency> getBankHasAgencyCollection() {
        return bankHasAgencyCollection;
    }

    public void setBankHasAgencyCollection(Collection<BankHasAgency> bankHasAgencyCollection) {
        this.bankHasAgencyCollection = bankHasAgencyCollection;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @XmlTransient
    public Collection<Checkout> getCheckoutCollection() {
        return checkoutCollection;
    }

    public void setCheckoutCollection(Collection<Checkout> checkoutCollection) {
        this.checkoutCollection = checkoutCollection;
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
        hash += (idAgency != null ? idAgency.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agency)) {
            return false;
        }
        Agency other = (Agency) object;
        if ((this.idAgency == null && other.idAgency != null) || (this.idAgency != null && !this.idAgency.equals(other.idAgency))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Agency[ idAgency=" + idAgency + " ]";
    }
    
}
