package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Usuario;
import utils.PasswordUtils;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {
    @PersistenceContext(unitName = "ControlMainPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    @Override
    public Usuario login(Usuario user){
        Usuario U = null;
        String query;
        try {
            query = "SELECT u FROM Usuario u WHERE u.idUser = ?1";
            Query q = em.createQuery(query);
            q.setParameter(1, user.getIdUser());
            
            List<Usuario> list = q.getResultList();
            if(!list.isEmpty()){
                U = list.get(0);
                System.out.println("UsuarioFacade:Parameters: "+user.getPassword()+"-"+U.getPassword() +"-"+ U.getSalt());
                if(PasswordUtils.verifyUserPassword(user.getPassword(), U.getPassword(), U.getSalt())){
                    return U;
                }else
                    return null;
            }
        } catch (Exception e) {
            throw e;
        }
        return U;
    }
    
    @Override
    public List<Usuario> listUsers(){
        String query;
        try {
            query = "SELECT u FROM Usuario u WHERE u.idUser <> 'admin'";
            Query q = em.createQuery(query);  
            List<Usuario> list = q.getResultList();
            return list;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public String getUserSalt(String idUser){
        String query;
        try {
            query = "SELECT u.Salt FROM Usuario u WHERE u.idUser = ?1";
            Query q = em.createQuery(query);  
            q.setParameter(1, idUser);
            String salt = (String) q.getSingleResult();
            return salt;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public String getUserPassword(String idUser){
        String query;
        try {
            query = "SELECT u.Password FROM Usuario u WHERE u.idUser = ?1";
            Query q = em.createQuery(query);  
            q.setParameter(1, idUser);
            String salt = (String) q.getSingleResult();
            return salt;
        } catch (Exception e) {
            throw e;
        }
    }
    
}
