package algorithm;

import java.math.BigInteger;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigPrimeNumber seed;
    public RSA(){
        p = seed.probablePrime(120);
        q = seed.probablePrime(120);
    }
}
