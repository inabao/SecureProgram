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
         BigInteger r = r0.modPow(BigInteger.valueOf(2), n);
         int i = 1;
         while(i<s){
             i++;
             if(r.equals(n.subtract(BigInteger.ONE))) return true;
             r = r.modPow(BigInteger.valueOf(2), n);
         }
         return false;
    }

    public boolean judgePrime(BigInteger n){
        int temp_t = 0;
        int s = 0;
        BigInteger k = n.subtract(BigInteger.ONE);
        while(k.remainder(BigInteger.valueOf(2)).intValue() != 1){
            s++;
            k = k.divide(BigInteger.valueOf(2));
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
        if(n.remainder(BigInteger.valueOf(2)).intValue() == 1) n.add(BigInteger.ONE);
        while(!judgePrime(n)){
            n = new BigInteger(bits, random);
            if(n.remainder(BigInteger.valueOf(2)).intValue() == 1) n.add(BigInteger.ONE);
        }
        return n;
    }

    public static BigInteger pow(BigInteger m, BigInteger k, BigInteger n){
        BigInteger bigInteger = new BigInteger("1");
        BigInteger temp;
        while(!k.equals(BigInteger.ZERO)){
            temp = k.remainder(BigInteger.valueOf(2));
            k = k.divide(BigInteger.valueOf(2));
            if (temp.equals(BigInteger.ONE))
                bigInteger = bigInteger.multiply(m);
            m = m.pow(2).mod(n);
            bigInteger = bigInteger.mod(n);
        }
        return bigInteger;
    }


    private static BigInteger x;
    private static BigInteger y;
    public static BigInteger exgcd(BigInteger a,BigInteger b)//扩展欧几里得算法
    {
        return ex_gcd(a, b)[1].add(b).mod(b);
    }
    public  static BigInteger[] ex_gcd(BigInteger a,BigInteger b){
        BigInteger ans;
        BigInteger[] result=new BigInteger[3];
        if(b.equals(BigInteger.ZERO)){
            result[0] = a;
            result[1] = BigInteger.ONE;
            result[2] = BigInteger.ZERO;
            return result;
        }
        BigInteger[] temp = ex_gcd(b,a.mod(b));
        ans = temp[0];
        result[0]=ans;
        result[1]=temp[2];
        result[2]=temp[1].subtract(a.divide(b).multiply(temp[2]));
        return result;
    }

    public static void main(String[] args) {
        System.out.println(exgcd(BigInteger.valueOf(7),BigInteger.valueOf(3)));
    }
}
