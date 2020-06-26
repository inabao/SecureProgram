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
        do{
            q = bigPrimeNumber.probablePrime(160);
//            BigInteger temp = bigPrimeNumber.probablePrime(351);
            p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
        }while(!bigPrimeNumber.judgePrime(p));
        BigInteger h = bigPrimeNumber.probablePrime(80);
        g = h.modPow(p.subtract(BigInteger.ONE).divide(q), p);
        x = bigPrimeNumber.probablePrime(80).mod(q);
        y = g.modPow(x, p);
    }

    public BigInteger[] sign(String message){
        BigInteger[] signValue = new BigInteger[2];
        MD5 md5 = new MD5();
        md5.hash(message);
        BigInteger h = new BigInteger(md5.resultBytes).mod(q);
        BigInteger k = bigPrimeNumber.probablePrime(80).mod(q);
        BigInteger r = g.modPow(k, p).mod(q);
        BigInteger s = h.add(x.multiply(r)).multiply(BigPrimeNumber.exgcd(k, q)).mod(q);
        signValue[0] = r;
        signValue[1] = s;
        return signValue;
    }

    public boolean valid(BigInteger[] signValue, String message){
        MD5 md5 = new MD5();
        md5.hash(message);
        BigInteger h = new BigInteger(md5.resultBytes).mod(q);
        BigInteger w = BigPrimeNumber.exgcd(signValue[1], q);
        BigInteger u1 = h.multiply(w).mod(q);
        BigInteger u2 = signValue[0].multiply(w).mod(q);
        BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);
        if(v.equals(signValue[0])) return true;
        return false;
    }
    public static void main(String[] args) {
        DSA dsa = new DSA();
        String message1 = "这个消息能弄多长呢";
        String message2 = "这个消息能弄多长呢";
        System.out.println(dsa.valid(dsa.sign(message1), message2));
    }
}

