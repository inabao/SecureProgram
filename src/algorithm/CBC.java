package algorithm;

import java.util.Arrays;
import java.util.Random;

public class CBC {
    private AES aes;
    public CBC(char[] key) throws Exception {
        aes = new AES(key);
    }
    public CBC(byte[] key) throws Exception {
        String sKey = new String(key);
        char[] cKey = new char[sKey.length()];
        for (int i = 0; i < sKey.length(); i++) {
            cKey[i] = sKey.charAt(i);
        }
        aes = new AES(cKey);
    }

    public CBC() throws Exception {
        char[] key = new char[16];
        Random r = new Random();
        for (int i = 0; i < 16; i++) {
            key[i] = (char)(r.nextInt(128));
        }
        aes = new AES(key);
    }
    private char[][] padding(String sss){
        byte[] ss = sss.getBytes();
        int trueLength = ss.length;
        int paddingLength = 16;
        if(trueLength % 16 == 0 ) trueLength += 16;
        else{
            paddingLength = 16 - (trueLength % 16);
            trueLength += paddingLength;
        }
        char[][] allM = new char[trueLength/16][16];
        for (int i = 0; i < ss.length; i++) {
            allM[i/16][i%16] = (char)(ss[i] + 128);
        }
        for (int i = ss.length; i < trueLength; i++) {
            allM[i/16][i%16] = (char)paddingLength;
        }
        return allM;
    }

    public String encode(String ss) throws Exception {
        char[][] m = padding(ss);
        char[] c = new char[m.length * 16];
        char[] IV = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < 16; j++) {
                m[i][j] = (char)(m[i][j] ^ IV[j]);
            }
            char[] tempC = aes.encode(m[i]);
            for (int j = 0; j < 16; j++) {
                c[i*16+j] = tempC[j];
            }
            IV = tempC;
        }
        return String.valueOf(c);
    }
    public String decode(String cc) throws Exception {
        char[][] c = new char[cc.length()/16][16];
        for (int i = 0; i < cc.length(); i++) {
            c[i/16][i%16] = cc.charAt(i);
        }
        char[] m = new char[cc.length()];
        char[] IV = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for (int i = 0; i < c.length; i++) {
            char[] tempM = aes.decode(c[i]);

            for (int j = 0; j < 16; j++) {
                m[j+i*16] = (char)(tempM[j] ^ IV[j]);
            }
            IV = c[i];
        }
        byte[] rawM = new byte[m.length];
        for (int i = 0; i < rawM.length; i++) {
            rawM[i] = (byte) m[i];
        }
        return new String(delPadding(rawM));
    }

    private byte[] delPadding(byte[] rawtext){
        int trueLength = rawtext.length - rawtext[rawtext.length-1];
        for (int i = 0; i < trueLength; i++) {
            rawtext[i] -= 128;
        }
        return Arrays.copyOfRange(rawtext, 0, trueLength);
    }

    public byte[] getKey(){
        return new String(aes.getKey()).getBytes();
    }
    public void setKey(char[] key) throws Exception {
        aes.setRawKey(key);
    }

    public void setKey(byte[] key) throws Exception {
        String sKey = new String(key);
        char[] ckey = new char[sKey.length()];
        for (int i = 0; i < sKey.length(); i++) {
            ckey[i] = (char)sKey.charAt(i);
        }
        aes.setRawKey(ckey);
    }


    public static void main(String[] args) throws Exception {
        CBC cbc1 = new CBC();
        CBC cbc2 = new CBC();
        byte[] key = cbc1.getKey();
        cbc2.setKey(key);
        byte[] content = cbc1.encode("我会永远永远爱你的hhhhh").getBytes();
        System.out.println(cbc2.decode(new String(content)));
    }

}
