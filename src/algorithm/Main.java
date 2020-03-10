package algorithm;

import java.math.BigInteger;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        BigPrimeNumber bigPrimeNumber = new BigPrimeNumber();
        System.out.println(bigPrimeNumber.probablePrime(120));
        System.out.println(bigPrimeNumber.judgePrime(BigInteger.probablePrime(120, new Random())));
    }
}
