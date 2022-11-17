package utils;



/**
 *
 * @author usuario
 */
public class Test {
    public static void main(String[] args){
        // User provided password to validate
        String providedPassword = "90khtL7sYfBJPawhn9y+UbkVODvEF74BofvlqpkbMfc=";
        System.out.println(providedPassword);        
        String salt = PasswordUtils.getSalt(30);
        System.out.println(salt);
        String securePassword = PasswordUtils.generateSecurePassword(providedPassword, salt);
        System.out.println(securePassword);
        
        if(PasswordUtils.verifyUserPassword(providedPassword, securePassword, salt)) 
        {
            System.out.println("Provided user password " + providedPassword + " is correct.");
        } else {
            System.out.println("Provided password is incorrect");
        }
    }
}
