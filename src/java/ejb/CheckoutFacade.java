package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Agency;
import model.Checkout;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Stateless
public class CheckoutFacade extends AbstractFacade<Checkout> implements CheckoutFacadeLocal {
    @PersistenceContext(unitName = "ControlMainPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CheckoutFacade() {
        super(Checkout.class);
    }
    
    @Override
    public List<Checkout> checkoutsByAgency(Agency agency){
        String query;
        try {
            //query = "SELECT c FROM Checkout c WHERE c.idAgency = ?1";
            query = "SELECT c FROM Checkout c WHERE c.checkoutPK.idAgency = :agency";
            Query q = em.createQuery(query);  
            q.setParameter("agency", agency.getIdAgency());
            List<Checkout> list = q.getResultList();
            return list;
        } catch (Exception e) {
            throw e;
        }
    }
}
