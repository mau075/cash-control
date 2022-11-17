package ejb;

import java.util.List;
import javax.ejb.Local;
import model.Currency;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Local
public interface CurrencyFacadeLocal {

    void create(Currency currency);

    void edit(Currency currency);

    void remove(Currency currency);

    Currency find(Object id);

    List<Currency> findAll();

    List<Currency> findRange(int[] range);

    int count();
    
}
