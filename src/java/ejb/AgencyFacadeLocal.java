package ejb;

import java.util.List;
import javax.ejb.Local;
import model.Agency;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Local
public interface AgencyFacadeLocal {

    void create(Agency agency);

    void edit(Agency agency);

    void remove(Agency agency);

    Agency find(Object id);

    List<Agency> findAll();

    List<Agency> findRange(int[] range);

    int count();
    
}
