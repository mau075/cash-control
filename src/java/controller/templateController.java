package controller;

import ejb.UsuarioFacadeLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import model.Agency;
import model.Usuario;
import utils.PasswordUtils;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 9/September/2018 10:26:45 EST
 */
@Named
@SessionScoped
public class templateController implements Serializable{
    
    private Usuario user;
    private Agency agency;
    
    private String currentPassword;
    private String newPassword;
    private String validatePassword;
    
    @EJB
    private UsuarioFacadeLocal usuarioEJB;
    
    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getValidatePassword() {
        return validatePassword;
    }

    public void setValidatePassword(String validatePassword) {
        this.validatePassword = validatePassword;
    }
 
    @PostConstruct
    public void init(){
        agency = new Agency();
        FacesContext context = FacesContext.getCurrentInstance();
        user = (Usuario) context.getExternalContext().getSessionMap().get("Usuario");
    }
    
    public void verifySession(){
        try {
            if(user == null){
                FacesContext.getCurrentInstance().getExternalContext().redirect("./../auth.xhtml");
            }
        } catch (Exception e) {
            
        }
    }
    
    public void logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
    
    public void changePassword(){
        try{
            String userPassword = user.getPassword();
            String userSalt = user.getSalt();
            if(PasswordUtils.verifyUserPassword(currentPassword, userPassword, userSalt)){
                String salt = PasswordUtils.getSalt(30);
                String password = PasswordUtils.generateSecurePassword(newPassword, salt);
                user.setPassword(password);
                user.setSalt(salt);
                usuarioEJB.edit(user);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contraseña de usuario", "Actualización correcta"));
            }else
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje", "La contraseña proporcionada no es correcta"));  
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Excepción", ex.toString()));  
        }
    }
}
