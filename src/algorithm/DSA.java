package algorithm;

import java.math.BigInteger;
import java.util.Arrays;

public class DSA {
    BigPrimeNumber bigPrimeNumber;
    BigInteger p;
    BigInteger q;
    BigInteger g;
    BigInteger x;
    BigInteger y;
    public DSA(){
        bigPrimeNumber = new BigPrimeNumber();
//        p = bigPrimeNumber.probablePrime(512);
//        do{
//            q = bigPrimeNumber.probablePrime(160);
//        }while(p.subtract(BigInteger.ONE).remainder(q).equals(BigInteger.ZERO));
//        BigInteger h = bigPrimeNumber.probablePrime(256);
//        g = h.modPow(p.subtract(BigInteger.ONE).divide(q), p);
//        x = bigPrimeNumber.probablePrime(256);
//        y = g.multiply(x).mod(p);
        p = BigInteger.valueOf(29);
        q = BigInteger.valueOf(7);
        g = BigInteger.valueOf(3);
        x = BigInteger.valueOf(5);
        y = g.modPow(x, q);
        System.out.println("p:"+p);
        System.out.println("q:"+q);
        System.out.println("g:"+g);
        System.out.println("x:"+x);
        System.out.println("y:"+y);
    }

    public BigInteger[] sign(String message){
        BigInteger[] signValue = new BigInteger[2];
        MD5 md5 = new MD5();
        md5.hash(message);
        BigInteger h = new BigInteger(md5.resultBytes).mod(q);
        BigInteger k = bigPrimeNumber.probablePrime(80).mod(q);
        BigInteger r = g.modPow(k, q);
        BigInteger s = h.add(x.multiply(r)).multiply(BigPrimeNumber.exgcd(k, q)).mod(q);
        signValue[0] = r;
        signValue[1] = s;
        System.out.println(BigPrimeNumber.exgcd(k, q));
        System.out.println("r:" + r);
        System.out.println("k:" + k);
        System.out.println("s:" + s);
        System.out.println("h:" + h);
        return signValue;
    }

    public boolean valid(BigInteger[] signValue, String message){
        MD5 md5 = new MD5();
        md5.hash(message);
        BigInteger h = new BigInteger(md5.resultBytes).mod(q);
        System.out.println(h);
        BigInteger w = BigPrimeNumber.exgcd(signValue[1], q);
        BigInteger u1 = h.multiply(w).mod(q);
        BigInteger u2 = signValue[0].multiply(w).mod(q);
        BigInteger v = g.modPow(u1, q).multiply(y.modPow(u2, q)).mod(q);
        System.out.println("v:"+v);
        System.out.println("w:"+w);
        System.out.println("u1:"+u1);
        System.out.println("u2:"+u2);
        System.out.println("h:"+h);
        if(v.equals(signValue[0])) return true;
        return false;
    }
    public static void main(String[] args) {
        DSA dsa = new DSA();
        String message = "hello world";
        System.out.println(dsa.valid(dsa.sign(message), message));
    }
}
