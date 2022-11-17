package ejb;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Agency;
import model.Checkout;
import model.Currency;
import model.Pettycash;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Stateless
public class PettycashFacade extends AbstractFacade<Pettycash> implements PettycashFacadeLocal {
    @PersistenceContext(unitName = "ControlMainPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PettycashFacade() {
        super(Pettycash.class);
    }
    
    @Override
    public BigDecimal getCurrencyValuePerCheckout(Agency agency, Checkout checkout, Currency currency){
        Pettycash P = null;
        String query;
        try{
            query = "SELECT p from Pettycash p WHERE p.pettycashPK.code = ?1 AND p.pettycashPK.idCurrency = ?2 AND p.pettycashPK.created = CURRENT_DATE AND p.pettycashPK.idAgency = ?3";
            Query q = em.createQuery(query);
            q.setParameter(1, checkout.getCheckoutPK().getCode());
            q.setParameter(2, currency.getIdCurrency());
            q.setParameter(3, agency.getIdAgency());
            List<Pettycash> list = q.getResultList();
            if(!list.isEmpty()){
                P = list.get(0);
                return P.getAmount();
            }else{
                return BigDecimal.ZERO;
            }
        }catch(Exception e){
            throw e;
        }
    }
}
