package ejb;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import model.Agency;
import model.Checkout;
import model.Currency;
import model.Pettycash;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Local
public interface PettycashFacadeLocal {

    void create(Pettycash pettycash);

    void edit(Pettycash pettycash);

    void remove(Pettycash pettycash);

    Pettycash find(Object id);

    List<Pettycash> findAll();

    List<Pettycash> findRange(int[] range);

    int count();
    
    public BigDecimal getCurrencyValuePerCheckout(Agency agency, Checkout checkout, Currency currency);
    
}
