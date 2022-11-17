package ejb;

import java.util.List;
import javax.ejb.Local;
import model.Cash;
import model.Currency;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Local
public interface CashFacadeLocal {

    void create(Cash cash);

    void edit(Cash cash);

    void remove(Cash cash);

    Cash find(Object id);

    List<Cash> findAll();

    List<Cash> findRange(int[] range);

    int count();
    
    //CUSTOM METHODS
    
    public List<Cash> cashByCurrency(Currency currency);

    public List<Cash> mutilatedList(Currency currency);
    
}
