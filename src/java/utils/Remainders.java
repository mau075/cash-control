package utils;

import java.math.BigDecimal;
import java.math.BigInteger;


public class Remainders {
    public static void main(String[] args){
        BigDecimal A = new BigDecimal(401);
        BigDecimal B = new BigDecimal(200);
        System.out.println(A.remainder(B));
    }
}
