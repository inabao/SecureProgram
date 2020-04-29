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

    public static BigInteger pow(BigInteger m, BigInteger k, BigInteger n){
        BigInteger bigInteger = new BigInteger("1");
        BigInteger temp;
        while(!k.equals(BigInteger.ZERO)){
            temp = k.remainder(BigInteger.TWO);
            k = k.divide(BigInteger.TWO);
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
        if (b.equals(BigInteger.ZERO)) {
            y = BigInteger.ZERO;
            x = BigInteger.ONE;
            return a;  //到达递归边界开始向上一层返回
        }
        BigInteger r = exgcd(b, a.remainder(b));
        BigInteger temp = y;    //把x y变成上一层的
        y = x.subtract(a.divide(b).multiply(y));
        x = temp;
        return y.mod(a);     //得到a b的最大公因数
    }

    public static void main(String[] args) {
        System.out.println(exgcd(BigInteger.valueOf(7),BigInteger.valueOf(3)));
    }
}
