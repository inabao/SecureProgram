package algorithm;

import java.math.BigInteger;
import java.util.Arrays;

public class RSAsign {
    RSA rsa = new RSA();
    public byte[] sign(String message){
        MD5 md5 = new MD5();
        md5.hash(message);
        byte[] hashValue = md5.resultBytes;
        BigInteger signValue = rsa.encoder(hashValue);
        return signValue.toByteArray();
    }
    public boolean valid(byte[] signValueBytes, String message){
        MD5 md5 = new MD5();
        BigInteger signValue = new BigInteger(signValueBytes);
        byte[] h = rsa.decoder(signValue);
        md5.hash(message);
        byte[] H = md5.resultBytes;
        for (int i = 0; i < h.length; i++) {
            if(h[i] != H[i]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        RSAsign rsaSign = new RSAsign();
        String message = "hello world";
        byte[] signValue = rsaSign.sign(message);
        System.out.println(rsaSign.valid(signValue, message));
    }
}
