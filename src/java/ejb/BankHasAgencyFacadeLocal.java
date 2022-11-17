package ejb;

import java.util.List;
import javax.ejb.Local;
import model.BankHasAgency;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Local
public interface BankHasAgencyFacadeLocal {

    void create(BankHasAgency bankHasAgency);

    void edit(BankHasAgency bankHasAgency);

    void remove(BankHasAgency bankHasAgency);

    BankHasAgency find(Object id);

    List<BankHasAgency> findAll();

    List<BankHasAgency> findRange(int[] range);

    int count();
    
}
