package controller;

import ejb.BankFacadeLocal;
import ejb.BankHasAgencyFacadeLocal;
import ejb.CashFacadeLocal;
import ejb.CurrencyFacadeLocal;
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
import model.Currency;
import model.Usuario;
import model.Vault;
import model.VaultPK;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 21/September/2018 00:03:39 EST
 */
@Named
@SessionScoped
public class encajeController implements Serializable{
    
    @EJB
    private CurrencyFacadeLocal currencyEJB;
    @EJB
    private CashFacadeLocal cashEJB;
    @EJB
    private VaultFacadeLocal vaultEJB;
    @EJB
    private BankFacadeLocal bankEJB;
    @EJB
    private BankHasAgencyFacadeLocal bankHasAgencyEJB;
    
    private Usuario user;
    private Agency agency;
    
    private List<Bank> banks;
    private Bank bank;
    
    private Currency local;
    private Currency foreign;
    private Vault vault;
    private VaultPK vaultPK;
    private Cash cash;
    
    private List<Cash> localCash;
    private List<Cash> foreignCash;
    private List<Cash> totalCash;
    
    private List<BigDecimal> localVault;
    private List<BigDecimal> foreignVault;
    
    private List<BigDecimal> reserveTotals;
    
    private BigDecimal totalLocalVault;
    private BigDecimal totalForeignVault;

    @PostConstruct
    public void init(){
        agency = new Agency();
        
        localVault = new ArrayList<>();
        foreignVault = new ArrayList<>();
        reserveTotals = new ArrayList<>();
        
        banks = bankEJB.findAll();
        local = currencyEJB.find(1);
        foreign = currencyEJB.find(2);
        totalCash = cashEJB.cashByCurrency(local);
        localCash = totalCash.subList(0, 3);
        foreignCash = cashEJB.cashByCurrency(foreign);
    }
    
    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public List<Cash> getLocalCash() {
        return localCash;
    }

    public void setLocalCash(List<Cash> localCash) {
        this.localCash = localCash;
    }

    public List<Cash> getForeignCash() {
        return foreignCash;
    }

    public void setForeignCash(List<Cash> foreignCash) {
        this.foreignCash = foreignCash;
    }

    public List<BigDecimal> getLocalVault() {
        return localVault;
    }

    public void setLocalVault(List<BigDecimal> localVault) {
        this.localVault = localVault;
    }

    public List<BigDecimal> getForeignVault() {
        return foreignVault;
    }

    public void setForeignVault(List<BigDecimal> foreignVault) {
        this.foreignVault = foreignVault;
    }

    public Currency getLocal() {
        return local;
    }

    public void setLocal(Currency local) {
        this.local = local;
    }

    public Currency getForeign() {
        return foreign;
    }

    public void setForeign(Currency foreign) {
        this.foreign = foreign;
    }

    public BigDecimal getTotalLocalVault() {
        return totalLocalVault;
    }

    public void setTotalLocalVault(BigDecimal totalLocalVault) {
        this.totalLocalVault = totalLocalVault;
    }

    public BigDecimal getTotalForeignVault() {
        return totalForeignVault;
    }

    public void setTotalForeignVault(BigDecimal totalForeignVault) {
        this.totalForeignVault = totalForeignVault;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public List<BigDecimal> getReserveTotals() {
        return reserveTotals;
    }

    public void setReserveTotals(List<BigDecimal> reserveTotals) {
        this.reserveTotals = reserveTotals;
    }
    
    public void sumLocal(){
        totalLocalVault = BigDecimal.ZERO;
        BigDecimal current;
        Iterator I = localVault.iterator();
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            totalLocalVault = totalLocalVault.add(current);
        }
    }
    
    public void sumForeign(){
        totalForeignVault = BigDecimal.ZERO;
        BigDecimal current;
        Iterator I = foreignVault.iterator();
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            totalForeignVault = totalForeignVault.add(current);
        }
    }
    
    private void clearLists(){
        localVault.clear();
        foreignVault.clear();
    }
    
    public void loadCashiersAndVault(){
        clearLists();
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            agency = (Agency) context.getExternalContext().getSessionMap().get("Agency");
                      
            //ITERATORS FOR SMALL VAULT VALUES
            Iterator J = localCash.iterator();
            Cash current1;
            while(J.hasNext()){                     
                current1 = (Cash) J.next();
                localVault.add(BigDecimal.ZERO);
                //reserveTotals.add(BigDecimal.ZERO);
            }
            
            //UPDATE TOTALS
            sumLocal();
            //System.out.println("Load Successful");
        }catch(Exception ex){
            
        }
    }
    
    public void refreshReserveTotals(){
        reserveTotals.clear();
        try{
            Iterator J = localCash.iterator();
            Cash current1;
            while(J.hasNext()){                     
                current1 = (Cash) J.next();
                BigDecimal value = vaultEJB.getReserveTotal(agency, current1);
                reserveTotals.add(value);
            }
        }catch(Exception e){
            
        }
    }
    
    public void registerIncome(){
        //UPDATE TOTALS
        sumLocal();
        try{
            if(verifyCash()){
                //FOR LOCAL CURRENCY
                Iterator I = localCash.iterator();
                int counter = 0;
                Vault temp;
                while(I.hasNext()){
                    vault = new Vault();
                    vaultPK = new VaultPK();
                    cash = (Cash) I.next();
                    vaultPK.setIdAgency(agency.getIdAgency());
                    vaultPK.setCreated(new Date());
                    vaultPK.setValue(cash.getCashPK().getValue());
                    vaultPK.setIdCurrency(cash.getCashPK().getIdCurrency());
                    vaultPK.setActive(true);
                    vaultPK.setMainVault("reserve");//VALUE DETERMINES THE TYPE OF VAULT TO SAVE
                    //vaultPK.setIdBank(bank.getIdBank()); //SETS THE CODE FOR THE BANK VAULT
                    vault.setVaultPK(vaultPK);
                    vault.setAmount(localVault.get(counter++));
                    temp = vaultEJB.find(vaultPK);
                    if(vault.getAmount().compareTo(BigDecimal.ZERO)!=0){//VALUES WITH ZERO ARE NOT REGISTERED

                        if(temp == null){
                            vaultEJB.create(vault);
                        }else{
                            BigDecimal newValue = temp.getAmount().add(vault.getAmount());
                            vault.setAmount(newValue);
                            vaultEJB.edit(vault);
                        }
                    }
                }
                //REGISTER THE TOTALS TO THE BANK ACCOUNT
                BankHasAgencyPK accountsPK = new BankHasAgencyPK(bank.getIdBank(), agency.getIdAgency(), 1);

                BankHasAgency current = bankHasAgencyEJB.find(accountsPK);
                BigDecimal newValue = current.getTotal().add(totalLocalVault);
                BankHasAgency accounts = new BankHasAgency(accountsPK, newValue);
                bankHasAgencyEJB.edit(accounts);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Encaje Legal", "Registro correcto"));
            }else
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error de validación", "Uno de los valores no coincide con el corte seleccionado, por favor verificar e intentar nuevamente"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
        }
    }
    
    private boolean compareWithReserves(){
        Iterator A = localVault.iterator();
        int counter = 0;
        while(A.hasNext()){
            BigDecimal temp = (BigDecimal) A.next();
            if(temp.compareTo(reserveTotals.get(counter++)) > 0)
                return false;
        }
        Iterator B = foreignVault.iterator();
        counter = 0;
        while(B.hasNext()){
            BigDecimal temp = (BigDecimal) B.next();
            if(temp.compareTo(reserveTotals.get(counter++)) > 0)
                return false;
        }
        return true;
    }
    
    private boolean verifyBankFunds(int idCurrency){
        BankHasAgencyPK accountsPK = new BankHasAgencyPK(bank.getIdBank(), agency.getIdAgency(), idCurrency);
        BankHasAgency current = bankHasAgencyEJB.find(accountsPK);
        return (current.getTotal().compareTo(totalLocalVault) >= 0);
    }
    
    public boolean verifyCash(){
        //FOR LOCAL CURRENCY
        Iterator A = localCash.iterator();
        int count = 0;
        while(A.hasNext()){
            Cash current = (Cash) A.next();
            BigDecimal v1 = current.getCashPK().getValue();
            BigDecimal v2 = localVault.get(count++);
            if(!v2.equals(BigDecimal.ZERO)){
                //System.out.println(v2.remainder(v1));
                if(v2.remainder(v1).compareTo(BigDecimal.ZERO) != 0)
                    return false;
            }
        }
        return true;
    }

    public void registerOutcome(){
        //UPDATE TOTALS
        sumLocal();
        sumForeign();
        //MAKE VERIFICATION, IF ONE OF THE VALUES IS LOWER THAN THE CURRENT AMOUNT INSIDE THE VAULT
        if (compareWithReserves()){
            try{
                if(verifyCash()){
                    if(verifyBankFunds(1)){
                        //FOR LOCAL CURRENCY
                        Iterator I = localCash.iterator();
                        int counter = 0;
                        Vault temp;
                        while(I.hasNext()){
                            vault = new Vault();
                            vaultPK = new VaultPK();
                            cash = (Cash) I.next();
                            vaultPK.setIdAgency(agency.getIdAgency());
                            vaultPK.setCreated(new Date());
                            vaultPK.setValue(cash.getCashPK().getValue());
                            vaultPK.setIdCurrency(cash.getCashPK().getIdCurrency());
                            vaultPK.setActive(false);
                            vaultPK.setMainVault("reserve");//VALUE DETERMINES THE TYPE OF VAULT TO SAVE
                            //vaultPK.setIdBank(bank.getIdBank()); //SETS THE CODE FOR THE BANK VAULT
                            vault.setVaultPK(vaultPK);
                            vault.setAmount(localVault.get(counter++));
                            temp = vaultEJB.find(vaultPK);
                            if(vault.getAmount().compareTo(BigDecimal.ZERO)!=0){//VALUES WITH ZERO ARE NOT REGISTERED
                                if(temp == null){
                                    vaultEJB.create(vault);
                                }else
                                    vaultEJB.edit(vault);
                            }
                        }
                        //REGISTER THE TOTALS TO THE BANK ACCOUNT
                        BankHasAgencyPK accountsPK = new BankHasAgencyPK(bank.getIdBank(), agency.getIdAgency(), 1);

                        BankHasAgency current = bankHasAgencyEJB.find(accountsPK);
                        BigDecimal newValue = current.getTotal().subtract(totalLocalVault);
                        BankHasAgency accounts = new BankHasAgency(accountsPK, newValue);
                        bankHasAgencyEJB.edit(accounts);

                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Encaje Legal", "Registro correcto"));
                    }else
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "La entidad seleccionada no cuenta con fondos suficientes para la transacción"));
                }else
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error de validación", "Uno de los valores no coincide con el corte seleccionado, por favor verificar e intentar nuevamente"));
            }catch(Exception e){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
            }
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Uno de los valores es mayor al total actual en bóveda de reserva, por favor verificar los valores"));
        }
    }
    
    public void checkRole(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            user = (Usuario) context.getExternalContext().getSessionMap().get("Usuario");
            if(user.getIdRole().getIdRole() != 2){
                context.getExternalContext().redirect("./../roleValidation.xhtml");
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            }
        } catch (IOException e) {
            
        }
    }
    
    public String openReportDetail(){
        return "reserveDetail.jsp";
    }
    
    public String openReportTotal(){
        return "bankTotals.jsp";
    }
    
}
