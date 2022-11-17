package controller;

import ejb.AgencyFacadeLocal;
import ejb.BankFacadeLocal;
import ejb.BankHasAgencyFacadeLocal;
import ejb.CashFacadeLocal;
import ejb.CheckoutFacadeLocal;
import ejb.CurrencyFacadeLocal;
import ejb.RolFacadeLocal;
import ejb.UsuarioFacadeLocal;
import ejb.VaultFacadeLocal;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import model.Agency;
import model.Bank;
import model.BankHasAgency;
import model.BankHasAgencyPK;
import model.Cash;
import model.Checkout;
import model.CheckoutPK;
import model.Currency;
import model.Rol;
import model.Usuario;
import model.Vault;
import model.VaultPK;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import utils.PasswordUtils;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 24/September/2018 21:25:34 EST
 */
@Named
@SessionScoped
public class managerController implements Serializable{
    
    private Usuario user;
    
    @EJB
    private RolFacadeLocal rolEJB;
    @EJB
    private UsuarioFacadeLocal usuarioEJB;
    @EJB
    private AgencyFacadeLocal agencyEJB;
    @EJB
    private CheckoutFacadeLocal checkoutEJB;
    @EJB
    private VaultFacadeLocal vaultEJB;
    @EJB
    private CurrencyFacadeLocal currencyEJB;
    @EJB
    private CashFacadeLocal cashEJB;
    @EJB
    private BankFacadeLocal bankEJB;
    @EJB
    private BankHasAgencyFacadeLocal bankHasAgencyEJB;
    
    private List<Rol> roles;
    private List<Rol> allRoles;
    private Rol selectedRol;
    
    private List<Usuario> usuarios;
    private Usuario selectedUser;
    private Usuario newUser;
    
    private List<Agency> agencies;
    private Agency selectedAgency;
    private Agency newAgency;
    
    private List<Checkout> checkouts; 
    private Checkout selectedCheckout;
    private Checkout newCheckout;
    private CheckoutPK newPK;
    
    private List<Cash> localCash;
    private List<Cash> reserveCash;
    private Currency local;
    private List<Bank> banks;
  
    
    @PostConstruct
    public void init(){
        allRoles = rolEJB.findAll();
        roles = allRoles.subList(1, 3);
        
        usuarios = usuarioEJB.listUsers();
        agencies = agencyEJB.findAll();
        
        newUser = new Usuario();
        newAgency = new Agency();
        newCheckout = new Checkout();
        newPK = new CheckoutPK();
        checkouts = new ArrayList<>();
        
        local = currencyEJB.find(1);
        localCash = cashEJB.cashByCurrency(local);
        reserveCash = localCash.subList(0, 3);
        banks = bankEJB.findAll();
    }
    
    public void checkRole(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            user = (Usuario) context.getExternalContext().getSessionMap().get("Usuario");
            if(user.getIdRole().getIdRole() != 1){
                context.getExternalContext().redirect("./../roleValidation.xhtml");
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            }
        } catch (IOException e) {
            
        }
    }
    
    public void updateRole(){
        try{
            rolEJB.edit(selectedRol);
            usuarios = usuarioEJB.listUsers();
            roles = rolEJB.findAll();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rol", "Actualización correcta"));
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un error al actualizar", ex.getMessage()));
        }
    }
    
    public void createUser(){
        try{
            //ENCRYPTION PROCESS
            String salt = PasswordUtils.getSalt(30);
            String securePassword = PasswordUtils.generateSecurePassword("123456", salt);
            newUser.setPassword(securePassword);
            newUser.setSalt(salt);

            usuarioEJB.create(newUser);
            usuarios = usuarioEJB.listUsers();
            newUser = new Usuario();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nuevo Usuario", "Correcto"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un error al crear el usuario", e.toString()));
        }
    }
    
    public void updateUser(){
        try{
            usuarioEJB.edit(selectedUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario", "Actualización correcta"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un error al actualizar", e.getMessage()));
            selectedUser = new Usuario();
        }
    }
    
    public void deleteUser(){
        try{
            usuarioEJB.remove(selectedUser);
            usuarios = usuarioEJB.listUsers();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario", "Se ha eliminado el usuario"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un error al eliminar", e.getMessage()));
        }
    }
    
    public void createAgency(){
        try{
            agencyEJB.create(newAgency);
            agencies = agencyEJB.findAll();
            newUser = new Usuario();
            
            //INITIALIZE RESERVE VAULT TOTALS TO ZERO
            Iterator I = reserveCash.iterator();
            Vault vault;
            VaultPK vaultPK;
            Cash cash;
            while(I.hasNext()){
                vault = new Vault();
                vaultPK = new VaultPK();
                cash = (Cash) I.next();
                vaultPK.setIdAgency(newAgency.getIdAgency());
                vaultPK.setCreated(new Date());
                vaultPK.setValue(cash.getCashPK().getValue());
                vaultPK.setIdCurrency(cash.getCashPK().getIdCurrency());
                vaultPK.setActive(true);
                vaultPK.setMainVault("reserve");//VALUE DETERMINES THE TYPE OF VAULT TO SAVE
                //vaultPK.setIdBank(bank.getIdBank()); //SETS THE CODE FOR THE BANK VAULT
                vault.setVaultPK(vaultPK);
                vault.setAmount(BigDecimal.ZERO);
                vaultEJB.create(vault);
            }
            //INITIALIZE BankHasAgency VALUES FOR THE NEW AGENCY
            Iterator J = banks.iterator();
            BankHasAgency temp;
            BankHasAgencyPK tempPK;
            while(J.hasNext()){
                Bank current = (Bank) J.next();
                temp = new BankHasAgency();
                tempPK = new BankHasAgencyPK(current.getIdBank(), newAgency.getIdAgency(), 1);
                temp.setBankHasAgencyPK(tempPK);
                temp.setTotal(BigDecimal.ZERO);
                bankHasAgencyEJB.create(temp);
            }

            agencies = agencyEJB.findAll();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nueva Agencia", "Correcto"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un error al crear la agencia", e.getMessage()));
        }
    }
    
    public void createCheckout(){
        try{
            newPK.setIdAgency(selectedAgency.getIdAgency());
            newCheckout.setCheckoutPK(newPK);
            checkoutEJB.create(newCheckout);
            
            agencies = agencyEJB.findAll();
            selectedAgency = null;
            newCheckout = new Checkout();
            newPK = new CheckoutPK();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nueva Caja", "Correcto"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un error al crear la caja", e.getMessage()));
        }
    }
    
    public void findCheckouts(){
        try{
            checkouts = checkoutEJB.checkoutsByAgency(selectedAgency);
        }catch(Exception e){
            
        }
    }
    
    public void onRowEditCheckout(RowEditEvent event) {
        Checkout temp = (Checkout) event.getObject();
        try{
            checkoutEJB.edit(temp);
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un error al editar", e.getMessage()));
        }
    }
    
    public void onRowEditUser(RowEditEvent event) {
        Usuario temp = (Usuario) event.getObject();
        try{
            usuarioEJB.edit(temp);
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un error al editar", e.getMessage()));
        }
    }
    
    public void onRowEditAgency(RowEditEvent event) {
        Agency temp = (Agency) event.getObject();
        try{
            agencyEJB.edit(temp);
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ha ocurrido un error al editar", e.getMessage()));
        }
        agencies = agencyEJB.findAll();
        usuarios = usuarioEJB.listUsers();
    }
    
    public void onRowCancel(RowEditEvent event) {
        
    }
    
    public void onCellEdit(CellEditEvent event) {
        
    }
    
    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Rol getSelectedRol() {
        return selectedRol;
    }

    public void setSelectedRol(Rol selectedRol) {
        this.selectedRol = selectedRol;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Usuario selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Agency> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<Agency> agencies) {
        this.agencies = agencies;
    }

    public Agency getSelectedAgency() {
        return selectedAgency;
    }

    public void setSelectedAgency(Agency selectedAgency) {
        this.selectedAgency = selectedAgency;
    }

    public Usuario getNewUser() {
        return newUser;
    }

    public void setNewUser(Usuario newUser) {
        this.newUser = newUser;
    }

    public Agency getNewAgency() {
        return newAgency;
    }

    public void setNewAgency(Agency newAgency) {
        this.newAgency = newAgency;
    }

    public List<Checkout> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(List<Checkout> checkouts) {
        this.checkouts = checkouts;
    }

    public Checkout getSelectedCheckout() {
        return selectedCheckout;
    }

    public void setSelectedCheckout(Checkout selectedCheckout) {
        this.selectedCheckout = selectedCheckout;
    }

    public Checkout getNewCheckout() {
        return newCheckout;
    }

    public void setNewCheckout(Checkout newCheckout) {
        this.newCheckout = newCheckout;
    }

    public CheckoutPK getNewPK() {
        return newPK;
    }

    public void setNewPK(CheckoutPK newPK) {
        this.newPK = newPK;
    }
    
}
