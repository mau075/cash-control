package ejb;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import model.Agency;
import model.Cash;
import model.Vault;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Local
public interface VaultFacadeLocal {

    void create(Vault vault);

    void edit(Vault vault);

    void remove(Vault vault);

    Vault find(Object id);

    List<Vault> findAll();

    List<Vault> findRange(int[] range);

    int count();
    
    public BigDecimal getVaultValuePerCash(Agency agency, Cash cash, String mainVault, String bank);
    
    public BigDecimal getReserveTotal(Agency agency, Cash cash);
    
}
