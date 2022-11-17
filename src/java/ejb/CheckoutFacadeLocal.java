package ejb;

import java.util.List;
import javax.ejb.Local;
import model.Agency;
import model.Checkout;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Local
public interface CheckoutFacadeLocal {

    void create(Checkout checkout);

    void edit(Checkout checkout);

    void remove(Checkout checkout);

    Checkout find(Object id);

    List<Checkout> findAll();

    List<Checkout> findRange(int[] range);

    int count();
    
    
    public List<Checkout> checkoutsByAgency(Agency agency);
}
