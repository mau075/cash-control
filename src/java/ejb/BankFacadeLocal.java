package ejb;

import java.util.List;
import javax.ejb.Local;
import model.Bank;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Local
public interface BankFacadeLocal {

    void create(Bank bank);

    void edit(Bank bank);

    void remove(Bank bank);

    Bank find(Object id);

    List<Bank> findAll();

    List<Bank> findRange(int[] range);

    int count();
    
}
