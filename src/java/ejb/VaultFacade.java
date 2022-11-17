package ejb;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Agency;
import model.Cash;
import model.Vault;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Stateless
public class VaultFacade extends AbstractFacade<Vault> implements VaultFacadeLocal {
    @PersistenceContext(unitName = "ControlMainPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VaultFacade() {
        super(Vault.class);
    }
    
    //GETS CASH VALUE
    @Override
    public BigDecimal getVaultValuePerCash(Agency agency, Cash cash, String mainVault, String bank){
        Vault V = null;
        String query;
        try{
            query = "SELECT v from Vault v WHERE v.vaultPK.idAgency = ?1 AND v.vaultPK.created = CURRENT_DATE AND v.vaultPK.value = ?2 AND v.vaultPK.idCurrency = ?3 AND v.vaultPK.active = ?4 AND v.vaultPK.mainVault = ?5";
            Query q = em.createQuery(query);
            q.setParameter(1, agency.getIdAgency());
            q.setParameter(2, cash.getCashPK().getValue());
            q.setParameter(3, cash.getCashPK().getIdCurrency());
            q.setParameter(4, cash.getCashPK().getActive());
            q.setParameter(5, mainVault);
            List<Vault> list = q.getResultList();
            if(!list.isEmpty()){
                V = list.get(0);
                return V.getAmount();
            }else{
                return BigDecimal.ZERO;
            }
        }catch(Exception e){
            throw e;
        }
    }
    
    @Override
    public BigDecimal getReserveTotal(Agency agency, Cash cash){
        Vault V = null;
        String query;
        BigDecimal income;
        BigDecimal outcome;
        BigDecimal total;
        try{
            //CALCULATE INCOME
            query = "SELECT sum(v.amount) FROM Vault v where v.vaultPK.mainVault='reserve' and v.vaultPK.idAgency = ?1 and v.cash.cashPK = ?2";
            Query q = em.createQuery(query);
            q.setParameter(1, agency.getIdAgency());
            q.setParameter(2, cash.getCashPK());
            income = (BigDecimal) q.getSingleResult();
            //CALCULATE OUTCOME
            query = "SELECT sum(v.amount) FROM Vault v where v.vaultPK.mainVault='reserve' and v.vaultPK.idAgency = ?1 and v.cash.cashPK.value = ?2 and v.cash.cashPK.idCurrency = ?3 and v.cash.cashPK.active = 0";
            Query q1 = em.createQuery(query);
            q1.setParameter(1, agency.getIdAgency());
            q1.setParameter(2, cash.getCashPK().getValue());
            q1.setParameter(3, cash.getCashPK().getIdCurrency());
            outcome = (BigDecimal) q1.getSingleResult();
            if(outcome == null){
                outcome = BigDecimal.ZERO;
            }
            total = income.subtract(outcome);
            return total;
        }catch(Exception e){
            throw e;
        }
    }
}
