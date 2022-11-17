package controller;

import ejb.UsuarioFacadeLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import model.Agency;
import model.Usuario;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 6/September/2018 15:46:13 EST
 */
@Named
@SessionScoped
public class loginController implements Serializable{
    
    @EJB
    private UsuarioFacadeLocal userEJB;
    //@Inject
    private Usuario user;
    private Agency agency;
   
    @PostConstruct 
    public void init(){
        agency = new Agency();
        user = new Usuario();
    }
    
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
    
    public String login(){
        Usuario U;
        String redirect = null;
        try {
            U = userEJB.login(user);
            System.out.println("User is: "+U);
            if(U != null){
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuario", U);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Agency", U.getIdAgency());
                //ROLE VALIDATION IS DONE HERE
                int role = U.getIdRole().getIdRole();
                System.out.println("Role is: "+role);
                switch(role){
                    case 1: redirect = "/protected/manager?faces-redirect=true";
                            break;
                    case 2: redirect = "/protected/encaje?faces-redirect=true";
                            break;
                    case 3: redirect = "/protected/home?faces-redirect=true";
                            break;
                }
            }else
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Usuario o contraseña incorrectos"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
            e.printStackTrace();
        }

        return redirect;
    }
}

