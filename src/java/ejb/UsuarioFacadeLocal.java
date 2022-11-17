package ejb;

import java.util.List;
import javax.ejb.Local;
import model.Usuario;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 7/September/2018 01:05:37 EST
 */
@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();
    
    //CUSTOM METHODS
    
    public Usuario login(Usuario user);
    
    public List<Usuario> listUsers();

    public String getUserSalt(String idUser);

    public String getUserPassword(String idUser);
    
}
