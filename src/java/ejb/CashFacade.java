package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Cash;
import model.Currency;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Stateless
public class CashFacade extends AbstractFacade<Cash> implements CashFacadeLocal {
    @PersistenceContext(unitName = "ControlMainPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CashFacade() {
        super(Cash.class);
    }
    
    @Override
    public List<Cash> cashByCurrency(Currency currency){
        String query;
        try {
            query = "SELECT c FROM Cash c WHERE c.cashPK.idCurrency= ?1 AND c.cashPK.active = 1 ORDER BY c.cashPK.value DESC";
            Query q = em.createQuery(query);  
            q.setParameter(1, currency.getIdCurrency());
            List<Cash> list = q.getResultList();
            return list;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public List<Cash> mutilatedList(Currency currency){
        String query;
        try {
            query = "SELECT c FROM Cash c WHERE c.cashPK.idCurrency= ?1 AND c.cashPK.active = 0 ORDER BY c.cashPK.value DESC";
            Query q = em.createQuery(query);  
            q.setParameter(1, currency.getIdCurrency());
            List<Cash> list = q.getResultList();
            return list;
        } catch (Exception e) {
            throw e;
        }
    }
    
}
