package algorithm;

import java.math.BigInteger;
import java.util.Arrays;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger z;
    private BigInteger n;
    private BigInteger e;
    private BigInteger d;
    private BigPrimeNumber seed = new BigPrimeNumber();
    
    public RSA(){
        p = seed.probablePrime(120);
        q = seed.probablePrime(120);
        n = p.multiply(q);
        z = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = seed.probablePrime(80);
        d = BigPrimeNumber.exgcd(z, e);
    }

    public BigInteger[] getPublicKey(){
        BigInteger[] bigIntegers = new BigInteger[2];
        bigIntegers[0] = n;
        bigIntegers[1] = e;
        return bigIntegers;
    }

    private BigInteger singleEncoder(BigInteger m){
        return BigPrimeNumber.pow(m, e, n);
    }
    private BigInteger singleDecoder(BigInteger c){
        return BigPrimeNumber.pow(c, d, n);
    }

    public BigInteger[] encoder(String s){
        BigInteger[] bigInteger = new BigInteger[s.length()];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            bigInteger[i] = singleEncoder(BigInteger.valueOf((int)c));
        }
        return bigInteger;
    }
    public String decoder(BigInteger[] bigIntegers){
        StringBuffer stringBuffer = new StringBuffer(bigIntegers.length);
        for (int i = 0; i < bigIntegers.length; i++) {
            stringBuffer.insert(i, (char)singleDecoder(bigIntegers[i]).intValue());
        }
        return new String(stringBuffer);
    }



    public static void main(String[] args) {
        RSA rsa = new RSA();
        System.out.println(Arrays.toString(rsa.encoder("math")));
        System.out.println(rsa.decoder(rsa.encoder("math")));
    }
}
