package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Agency;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Stateless
public class AgencyFacade extends AbstractFacade<Agency> implements AgencyFacadeLocal {
    @PersistenceContext(unitName = "ControlMainPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgencyFacade() {
        super(Agency.class);
    }
    
}
