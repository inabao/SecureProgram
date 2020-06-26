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

    public BigInteger encoder(String s){
        return singleEncoder(new BigInteger(s.getBytes()));
    }

    public BigInteger encoder(byte[] s){
        return singleEncoder(new BigInteger(s));
    }

    public byte[] decoder(BigInteger bigInteger){
        byte[] bytes = singleDecoder(bigInteger).toByteArray();
        return bytes;
    }

    public String bytes2String(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer(bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.insert(i, (char)bytes[i]);
        }
        return new String(stringBuffer);
    }

    public static void main(String[] args) {
        RSA rsa = new RSA();
        System.out.println(rsa.encoder("math"));
        System.out.println(rsa.bytes2String(rsa.decoder(rsa.encoder("math"))));
    }
}
