package controller;

import ejb.CashFacadeLocal;
import ejb.CheckoutFacadeLocal;
import ejb.CurrencyFacadeLocal;
import ejb.PettycashFacadeLocal;
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
import model.Cash;
import model.Checkout;
import model.Currency;
import model.Pettycash;
import model.PettycashPK;
import model.Usuario;
import model.Vault;
import model.VaultPK;

/**
 * @author Felipe Mauricio Gonzales Subirana
 * @version 9/September/2018 15:41:53 EST
 */
@Named
@SessionScoped
public class homeController implements Serializable{
    
    @EJB
    private CheckoutFacadeLocal checkoutEJB; 
    @EJB
    private PettycashFacadeLocal pettycashEJB;
    @EJB
    private CurrencyFacadeLocal currencyEJB;
    @EJB
    private CashFacadeLocal cashEJB;
    @EJB
    private VaultFacadeLocal vaultEJB;
    
    private Usuario user;
    private Agency agency;

    private Checkout checkout;

    private Pettycash pettycash; 
    private PettycashPK pettycashPK;
    private Cash cash;
    private Vault vault;
    private VaultPK vaultPK;
    
    private Currency local;
    private Currency foreign;
    
    private List<Checkout> checkouts;
    private List<BigDecimal> localCurrency;
    private List<BigDecimal> foreignCurrency;
    private BigDecimal totalLocalCurrency;
    private BigDecimal totalForeignCurrency;
    
    private List<Cash> localCash;
    private List<Cash> foreignCash;
    private List<Cash> mutilatedCash;
    //LISTS FOR SMALL VAULT
    private List<BigDecimal> localVault;
    private List<BigDecimal> foreignVault;
    private List<BigDecimal> mutilatedVault;
    //LISTS FOR MAIN VAULT
    private List<BigDecimal> localMainVault;
    private List<BigDecimal> foreignMainVault;
    
    private BigDecimal totalLocalVault;
    private BigDecimal totalForeignVault;
    private BigDecimal totalMainLocalVault;
    private BigDecimal totalMainForeignVault;
    private BigDecimal totalMutilated;
    
    //REPORT PARAMETERS
    private Date created;
    
    @PostConstruct 
    public void init(){
        agency = new Agency();
        checkout = new Checkout();
        pettycashPK = new PettycashPK();
        pettycash = new Pettycash();
        
        checkouts = new ArrayList<>();
        localCurrency = new ArrayList<>();
        foreignCurrency = new ArrayList<>();
        localVault = new ArrayList<>();
        foreignVault = new ArrayList<>();
        localMainVault = new ArrayList<>();
        foreignMainVault = new ArrayList<>();
        mutilatedVault = new ArrayList<>();
        
        local = currencyEJB.find(1);
        foreign = currencyEJB.find(2);
        localCash = cashEJB.cashByCurrency(local);
        foreignCash = cashEJB.cashByCurrency(foreign);
        mutilatedCash = cashEJB.mutilatedList(local);
    }
    
    private void clearLists(){
        checkouts.clear();
        localCurrency.clear();
        foreignCurrency.clear();
        
        //localCash.clear();
        //foreignCash.clear();
        localVault.clear();
        foreignVault.clear();
        localMainVault.clear();
        foreignMainVault.clear();
        mutilatedVault.clear();
    }
    
    public void checkRole(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            user = (Usuario) context.getExternalContext().getSessionMap().get("Usuario");
            if(user.getIdRole().getIdRole() != 3){
                context.getExternalContext().redirect("./../roleValidation.xhtml");
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            }
        } catch (IOException e) {
            
        }
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
    
    public void sumMainLocal(){
        totalMainLocalVault = BigDecimal.ZERO;
        BigDecimal current;
        Iterator I = localMainVault.iterator();
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            totalMainLocalVault = totalMainLocalVault.add(current);
        }
    }
    
    public void sumMainForeign(){
        totalMainForeignVault = BigDecimal.ZERO;
        BigDecimal current;
        Iterator I = foreignMainVault.iterator();
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            totalMainForeignVault = totalMainForeignVault.add(current);
        }
    }
    
    public void sumMutilated(){
        totalMutilated = BigDecimal.ZERO;
        BigDecimal current;
        Iterator I = mutilatedVault.iterator();
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            totalMutilated = totalMutilated.add(current);
        }
    }
    
    public void sumLocalCurrency(){
        totalLocalCurrency = BigDecimal.ZERO;
        BigDecimal current;
        Iterator I = localCurrency.iterator();
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            totalLocalCurrency = totalLocalCurrency.add(current);
        }
    }
    
    public void sumForeignCurrency(){
        totalForeignCurrency = BigDecimal.ZERO;
        BigDecimal current;
        Iterator I = foreignCurrency.iterator();
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            totalForeignCurrency = totalForeignCurrency.add(current);
        }
    }
    
    //VERIFIES THAT THE AMOUNT CORRESPONDS TO THE CASH (REMAINDER) ON OUT VAULT
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
        //FOR FOREIGN CURRENCY
        Iterator B = foreignCash.iterator();
        count = 0;
        while(B.hasNext()){
            Cash current = (Cash) B.next();
            BigDecimal v1 = current.getCashPK().getValue();
            BigDecimal v2 = foreignVault.get(count);
            if(!v2.equals(BigDecimal.ZERO)){
                if(v2.remainder(v1).compareTo(BigDecimal.ZERO) != 0)
                    return false;
            }
        }
        return true;
    }
    
    public boolean verifyCashMain(){
        //FOR LOCAL CURRENCY
        Iterator A = localCash.iterator();
        int count = 0;
        while(A.hasNext()){
            Cash current = (Cash) A.next();
            BigDecimal v1 = current.getCashPK().getValue();
            BigDecimal v2 = localMainVault.get(count++);
            if(!v2.equals(BigDecimal.ZERO)){
                //System.out.println(v2.remainder(v1));
                if(v2.remainder(v1).compareTo(BigDecimal.ZERO) != 0)
                    return false;
            }
        }
        //FOR FOREIGN CURRENCY
        Iterator B = foreignCash.iterator();
        count = 0;
        while(B.hasNext()){
            Cash current = (Cash) B.next();
            BigDecimal v1 = current.getCashPK().getValue();
            BigDecimal v2 = foreignMainVault.get(count++);
            if(!v2.equals(BigDecimal.ZERO)){
                if(v2.remainder(v1).compareTo(BigDecimal.ZERO) != 0)
                    return false;
            }
        }
        return true;
    }
    
    public boolean verifyMutilated(){
        //FOR LOCAL CURRENCY
        Iterator A = mutilatedCash.iterator();
        int count = 0;
        while(A.hasNext()){
            Cash current = (Cash) A.next();
            BigDecimal v1 = current.getCashPK().getValue();
            BigDecimal v2 = mutilatedVault.get(count++);
            if(!v2.equals(BigDecimal.ZERO)){
                //System.out.println(v2.remainder(v1));
                if(v2.remainder(v1).compareTo(BigDecimal.ZERO) != 0)
                    return false;
            }
        }
        return true;
    }
    
    public void loadCashiersAndVault(){
        clearLists();
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            agency = (Agency) context.getExternalContext().getSessionMap().get("Agency");
            checkouts = checkoutEJB.checkoutsByAgency(agency);
                      
            Iterator I = checkouts.iterator();
            Checkout current;
            while(I.hasNext()){
                current = (Checkout)I.next();
                localCurrency.add(pettycashEJB.getCurrencyValuePerCheckout(agency, current, local));
                foreignCurrency.add(pettycashEJB.getCurrencyValuePerCheckout(agency, current, foreign));
            }
            //ITERATORS FOR SMALL VAULT VALUES
            Iterator J = localCash.iterator();
            Cash current1;
            while(J.hasNext()){                     
                current1 = (Cash) J.next();
                localVault.add(vaultEJB.getVaultValuePerCash(agency, current1, "out", "BUN"));
            }
            
            Iterator K = foreignCash.iterator();
            while(K.hasNext()){
                current1 = (Cash) K.next();
                foreignVault.add(vaultEJB.getVaultValuePerCash(agency, current1, "out", "BUN"));
            }
            //ITERATORS FOR MAIN VAULT VALUES
            Iterator L = localCash.iterator();
            Cash current2;
            while(L.hasNext()){                     
                current2 = (Cash) L.next();
                localMainVault.add(vaultEJB.getVaultValuePerCash(agency, current2, "main", "BUN"));
            }
            
            Iterator M = foreignCash.iterator();
            while(M.hasNext()){
                current2 = (Cash) M.next();
                foreignMainVault.add(vaultEJB.getVaultValuePerCash(agency, current2, "main", "BUN"));
            }
            //ITERATOR FOR MUTILATED
            Iterator N = mutilatedCash.iterator();
            Cash current3;
            while(N.hasNext()){
                current3 = (Cash) N.next();
                mutilatedVault.add(vaultEJB.getVaultValuePerCash(agency, current3, "main", "BUN"));
            }
            
            
            //UPDATE TOTALS
            sumLocal();
            sumForeign();
            sumMainLocal();
            sumMainForeign();
            sumLocalCurrency();
            sumForeignCurrency();
            sumMutilated();
            //System.out.println("Load Successful");
        }catch(Exception ex){
            
        }
    }
    
    private boolean checkoutNoNegative(){
        Iterator I = localCurrency.iterator();
        BigDecimal current;
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            if(current.compareTo(BigDecimal.ZERO) == -1)
                return false;
        }
        Iterator J = foreignCurrency.iterator();
        while(J.hasNext()){
            current = (BigDecimal) J.next();
            if(current.compareTo(BigDecimal.ZERO) == -1)
                return false;
        }
        return true;
    }
    
    public void register(){
        //UPDATE TOTALS
       sumLocalCurrency();
       sumForeignCurrency();
       
       Iterator I = checkouts.iterator();
       int counter = 0;
       BigDecimal amount;
       Pettycash temp;
       try{
           if(checkoutNoNegative()){
                while(I.hasNext()){
                    checkout = (Checkout) I.next();

                    //FOR LOCAL
                    pettycash = new Pettycash();
                    pettycashPK = new PettycashPK();
                    amount = localCurrency.get(counter);
                    pettycashPK.setCode(checkout.getCheckoutPK().getCode());
                    pettycashPK.setCreated(new Date());
                    pettycashPK.setIdAgency(checkout.getCheckoutPK().getIdAgency());
                    pettycashPK.setIdCurrency(local.getIdCurrency());
                    pettycash.setAmount(amount);
                    pettycash.setCheckout(checkout);
                    pettycash.setCurrency(local);
                    pettycash.setPettycashPK(pettycashPK);
                    temp = pettycashEJB.find(pettycashPK);
                    if(temp == null){
                        pettycashEJB.create(pettycash);
                    }else
                        pettycashEJB.edit(pettycash);

                    //FOR FOREIGN
                    pettycash = new Pettycash();
                    pettycashPK = new PettycashPK();
                    amount = foreignCurrency.get(counter++);
                    pettycashPK.setCode(checkout.getCheckoutPK().getCode());
                    pettycashPK.setCreated(new Date());
                    pettycashPK.setIdAgency(checkout.getCheckoutPK().getIdAgency());
                    pettycashPK.setIdCurrency(foreign.getIdCurrency());
                    pettycash.setAmount(amount);
                    pettycash.setCheckout(checkout);
                    pettycash.setCurrency(foreign);
                    pettycash.setPettycashPK(pettycashPK);
                    temp = pettycashEJB.find(pettycashPK);
                    if(temp == null){
                        pettycashEJB.create(pettycash);
                    }else
                        pettycashEJB.edit(pettycash);

                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Saldos por cajero", "Actualización correcta"));
           }else
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se permiten valores negativos"));
       }catch(Exception e){
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Ha ocurrido un error"));
       }
    }
    
    private boolean vaultNoNegative(){
        Iterator I = localVault.iterator();
        BigDecimal current;
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            if(current.compareTo(BigDecimal.ZERO) == -1)
                return false;
        }
        Iterator J = foreignVault.iterator();
        while(J.hasNext()){
            current = (BigDecimal) J.next();
            if(current.compareTo(BigDecimal.ZERO) == -1)
                return false;
        }
        return true;
    }
    
    public void registerVault(){
        //UPDATE TOTALS
        sumLocal();
        sumForeign();
        try{
            if(vaultNoNegative()){
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
                        vaultPK.setActive(cash.getCashPK().getActive());
                        vaultPK.setMainVault("out");//VALUE DETERMINES THE TYPE OF VAULT TO SAVE
                        //vaultPK.setIdBank("BUN"); //SETS THE CODE FOR THE BANK VAULT
                        vault.setVaultPK(vaultPK);
                        vault.setAmount(localVault.get(counter++));
                        temp = vaultEJB.find(vaultPK);
                        if(temp == null){
                            vaultEJB.create(vault);
                        }else
                            vaultEJB.edit(vault);
                    }
                    //FOR FOREIGN CURRENCY
                    Iterator J = foreignCash.iterator();
                    counter = 0;
                    while(J.hasNext()){
                        vault = new Vault();
                        vaultPK = new VaultPK();
                        cash = (Cash) J.next();
                        vaultPK.setIdAgency(agency.getIdAgency());
                        vaultPK.setCreated(new Date());
                        vaultPK.setValue(cash.getCashPK().getValue());
                        vaultPK.setIdCurrency(cash.getCashPK().getIdCurrency());
                        vaultPK.setActive(cash.getCashPK().getActive());
                        vaultPK.setMainVault("out");//VALUE DETERMINES THE TYPE OF VAULT TO SAVE
                        //vaultPK.setIdBank("BUN"); //SETS THE CODE FOR THE BANK VAULT
                        vault.setVaultPK(vaultPK);
                        vault.setAmount(foreignVault.get(counter++));
                        temp = vaultEJB.find(vaultPK);
                        if(temp == null){
                            vaultEJB.create(vault);
                        }else
                            vaultEJB.edit(vault);
                    }           
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bóveda", "Actualización correcta"));
                }else
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error de validación", "Uno de los valores no coincide con el corte seleccionado, por favor verificar e intentar nuevamente"));
            }else
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se permiten valores negativos"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
        }
    }
    
    private boolean mainVaultNoNegative(){
        Iterator I = localMainVault.iterator();
        BigDecimal current;
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            if(current.compareTo(BigDecimal.ZERO) == -1)
                return false;
        }
        Iterator J = foreignMainVault.iterator();
        while(J.hasNext()){
            current = (BigDecimal) J.next();
            if(current.compareTo(BigDecimal.ZERO) == -1)
                return false;
        }
        return true;
    }
    
    public void registerMainVault(){
        //UPDATE TOTALS
        sumMainLocal();
        sumMainForeign();
        try{
            if(mainVaultNoNegative()){
                if(verifyCashMain()){
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
                        vaultPK.setActive(cash.getCashPK().getActive());
                        vaultPK.setMainVault("main");//VALUE DETERMINES THE TYPE OF VAULT TO SAVE
                        //vaultPK.setIdBank("BUN"); //SETS THE CODE FOR THE BANK VAULT
                        vault.setVaultPK(vaultPK);
                        vault.setAmount(localMainVault.get(counter++));
                        temp = vaultEJB.find(vaultPK);
                        if(temp == null){
                            vaultEJB.create(vault);
                        }else
                            vaultEJB.edit(vault);
                    }
                    //FOR FOREIGN CURRENCY
                    Iterator J = foreignCash.iterator();
                    counter = 0;
                    while(J.hasNext()){
                        vault = new Vault();
                        vaultPK = new VaultPK();
                        cash = (Cash) J.next();
                        vaultPK.setIdAgency(agency.getIdAgency());
                        vaultPK.setCreated(new Date());
                        vaultPK.setValue(cash.getCashPK().getValue());
                        vaultPK.setIdCurrency(cash.getCashPK().getIdCurrency());
                        vaultPK.setActive(cash.getCashPK().getActive());
                        vaultPK.setMainVault("main");//VALUE DETERMINES THE TYPE OF VAULT TO SAVE
                        //vaultPK.setIdBank("BUN"); //SETS THE CODE FOR THE BANK VAULT
                        vault.setVaultPK(vaultPK);
                        vault.setAmount(foreignMainVault.get(counter++));
                        temp = vaultEJB.find(vaultPK);
                        if(temp == null){
                            vaultEJB.create(vault);
                        }else
                            vaultEJB.edit(vault);
                    }           
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Boveda", "Actualización correcta"));
                }else
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error de validación", "Uno de los valores no coincide con el corte seleccionado, por favor verificar e intentar nuevamente"));
            }else
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se permiten valores negativos"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
        }
    }
    
    private boolean mutilatedNoNegative(){
        Iterator I = mutilatedVault.iterator();
        BigDecimal current;
        while(I.hasNext()){
            current = (BigDecimal) I.next();
            if(current.compareTo(BigDecimal.ZERO) == -1)
                return false;
        }
        return true;
    }
    
    public void registerMutilated(){
        //UPDATE TOTALS
        sumMutilated();
        try{
            if(mutilatedNoNegative()){
                if(verifyMutilated()){
                    //FOR LOCAL CURRENCY
                    Iterator I = mutilatedCash.iterator();
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
                        vaultPK.setActive(cash.getCashPK().getActive());
                        vaultPK.setMainVault("main");//VALUE DETERMINES THE TYPE OF VAULT TO SAVE
                        //vaultPK.setIdBank("BUN"); //SETS THE CODE FOR THE BANK VAULT
                        vault.setVaultPK(vaultPK);
                        vault.setAmount(mutilatedVault.get(counter++));
                        temp = vaultEJB.find(vaultPK);
                        if(temp == null){
                            vaultEJB.create(vault);
                        }else
                            vaultEJB.edit(vault);
                    }

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Billetes Mutilados", "Actualización correcta"));
                }else
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error de validación", "Uno de los valores no coincide con el corte seleccionado, por favor verificar e intentar nuevamente"));
            }else
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se permiten valores negativos"));
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
        }
    }
    
    public String openReport(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Created", created);
        return "cuadreDiario.jsp";
        
        /*try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://:3306/cashcontrol", "root", "admin123");
            
            String report = "C:\\Users\\usuario\\Documents\\NetBeansProjects\\ControlMain\\src\\java\\reports\\cuadreDiario.jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport(report);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), connection);
  
            //OutputStream outputStream = new FileOutputStream("C:\\Users\\usuario\\Documents\\NetBeansProjects\\ControlMain\\src\\java\\reports\\cuadreDiario.pdf");
            //JasperExportManager.exportReportToPdfStream(jp, outputStream);
            //JasperExportManager.exportReportToPdfFile(jp, "C:\\Users\\usuario\\Documents\\NetBeansProjects\\ControlMain\\src\\java\\reports\\cuadreDiario.pdf");
            
        } catch (JRException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(homeController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
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

    public List<BigDecimal> getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(List<BigDecimal> localCurrency) {
        this.localCurrency = localCurrency;
    }

    public List<BigDecimal> getForeignCurrency() {
        return foreignCurrency;
    }

    public void setForeignCurrency(List<BigDecimal> foreignCurrency) {
        this.foreignCurrency = foreignCurrency;
    }

    public Checkout getCheckout() {
        return checkout;
    }

    public void setCheckout(Checkout checkout) {
        this.checkout = checkout;
    }

    public List<Checkout> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(List<Checkout> checkouts) {
        this.checkouts = checkouts;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
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

    public BigDecimal getTotalLocalCurrency() {
        return totalLocalCurrency;
    }

    public void setTotalLocalCurrency(BigDecimal totalLocalCurrency) {
        this.totalLocalCurrency = totalLocalCurrency;
    }

    public BigDecimal getTotalForeignCurrency() {
        return totalForeignCurrency;
    }

    public void setTotalForeignCurrency(BigDecimal totalForeignCurrency) {
        this.totalForeignCurrency = totalForeignCurrency;
    }

    public List<BigDecimal> getLocalMainVault() {
        return localMainVault;
    }

    public void setLocalMainVault(List<BigDecimal> localMainVault) {
        this.localMainVault = localMainVault;
    }

    public List<BigDecimal> getForeignMainVault() {
        return foreignMainVault;
    }

    public void setForeignMainVault(List<BigDecimal> foreignMainVault) {
        this.foreignMainVault = foreignMainVault;
    }

    public BigDecimal getTotalMainLocalVault() {
        return totalMainLocalVault;
    }

    public void setTotalMainLocalVault(BigDecimal totalMainLocalVault) {
        this.totalMainLocalVault = totalMainLocalVault;
    }

    public BigDecimal getTotalMainForeignVault() {
        return totalMainForeignVault;
    }

    public void setTotalMainForeignVault(BigDecimal totalMainForeignVault) {
        this.totalMainForeignVault = totalMainForeignVault;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Cash> getMutilatedCash() {
        return mutilatedCash;
    }

    public void setMutilatedCash(List<Cash> mutilatedCash) {
        this.mutilatedCash = mutilatedCash;
    }

    public List<BigDecimal> getMutilatedVault() {
        return mutilatedVault;
    }

    public void setMutilatedVault(List<BigDecimal> mutilatedVault) {
        this.mutilatedVault = mutilatedVault;
    }

    public BigDecimal getTotalMutilated() {
        return totalMutilated;
    }

    public void setTotalMutilated(BigDecimal totalMutilated) {
        this.totalMutilated = totalMutilated;
    }
    
    
    
}
