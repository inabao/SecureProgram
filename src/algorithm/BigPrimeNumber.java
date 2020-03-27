package algorithm;

import java.math.BigInteger;
import java.util.Random;

public class BigPrimeNumber {
    private int t = 10;
    private Random random = new Random();

    private BigInteger generateB(BigInteger n, int bits){
        BigInteger bigInteger = new BigInteger(bits, random);
        while(!bigInteger.gcd(n).equals(BigInteger.ONE)){
            bigInteger = new BigInteger(bits, random);
        }
        return bigInteger;
    }

    private boolean simpleJudge(BigInteger n, BigInteger b, BigInteger k, int s){
         BigInteger r0 = b.modPow(k, n);
         if(r0.equals(n.subtract(BigInteger.ONE)) || r0.equals(BigInteger.ONE)){
             return true;
         }
         BigInteger r = r0.modPow(BigInteger.TWO, n);
         int i = 1;
         while(i<s){
             i++;
             if(r.equals(n.subtract(BigInteger.ONE))) return true;
             r = r.modPow(BigInteger.TWO, n);
         }
         return false;
    }

    public boolean judgePrime(BigInteger n){
        int temp_t = 0;
        int s = 0;
        BigInteger k = n.subtract(BigInteger.ONE);
        while(k.remainder(BigInteger.TWO).intValue() != 1){
            s++;
            k = k.divide(BigInteger.TWO);
        }
        while(temp_t < t){
            temp_t++;
            BigInteger b = generateB(n, n.bitCount());
            if(!simpleJudge(n, b, k, s)) return false;
        }
        return true;
    }

    public BigInteger probablePrime(int bits){
        BigInteger n = new BigInteger(bits, random);
        if(n.remainder(BigInteger.TWO).intValue() == 1) n.add(BigInteger.ONE);
        while(!judgePrime(n)){
            n = new BigInteger(bits, random);
            if(n.remainder(BigInteger.TWO).intValue() == 1) n.add(BigInteger.ONE);
        }
        return n;
    }

}
