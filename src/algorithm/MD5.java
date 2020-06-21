package algorithm;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MD5 {
    public int[] register = {0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476};
    public int[][] sShift = {{7, 12, 17, 22}, {5, 9, 14, 20}, {4, 11, 16, 23}, {6, 10, 15, 21}};
    public int[] T = {0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501, 0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821, 0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, 0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8, 0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a, 0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70, 0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05, 0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665, 0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039, 0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1, 0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391};
    public int[] gap = {1, 5, 3, 7};
    public int[] begin = {0, 1, 5, 0};
    public String result;
    public byte[] resultBytes;
    public int[] padding(int[] plaintext, long length){
        int mode;
        long paddingLength = length - (int)(length % 512);
        if(length % 512 >= 448) {
            mode = 32;
        } else {
            mode = 16;
        }
        paddingLength += mode * 32;
        long wordLength = paddingLength/32;
        int[] padedPlaintext = new int[(int)wordLength];
        for (int i = 0; i < plaintext.length; i++) {
            padedPlaintext[i] = plaintext[i];
        }
        for (int i = plaintext.length + 1; i < wordLength - 2; i++) {
            padedPlaintext[i] = 0;
        }
        padedPlaintext[(int)wordLength-2] = ((int)(length % ((long)Integer.MAX_VALUE + 1))) - 8;
        padedPlaintext[(int)wordLength-1] = 0;
        System.out.println("paddingtext" + Arrays.toString(padedPlaintext));
        return padedPlaintext;
    }
    public int logicFunction(int[] input, int round){
        int value;
        if(round == 0){
            value = (input[0] & input[1]) | ((~input[0]) & input[2]);
        } else if (round == 1){
            value = (input[0] & input[2]) | (input[1] & (~input[2]));
        } else if (round == 2){
            value = input[0] ^ input[1] ^ input[2];
        } else{
            value = input[1] ^ (input[0] | (~input[2]));
        }
        return value;
    }
    public int leftShift(int data, int size){
        return (data << size) | (data >>> (32 - size));
    }

    public void singleStep(int x, int round, int step){
        int temp;
        int[] BCD = {register[1], register[2], register[3]};
        temp = logicFunction(BCD, round);
        temp = temp + register[0] + x + T[round*16 + step];
        temp = leftShift(temp, sShift[round][step % 4]);
        temp += register[1];
        register[0] = register[3];
        register[3] = register[2];
        register[2] = register[1];
        register[1] = temp;
    }

    public void roundStep(int round, int[] X){
        for (int i = 0; i < 16; i++) {
            singleStep(X[(i * gap[round] + begin[round]) % 16], round, i);
        }
    }

    public void singleHash(int[] X){
        int[] temp = register.clone();
        for (int i = 0; i < 4; i++) {
            roundStep(i, X);
        }
        for (int i = 0; i < 4; i++) {
            register[i] += temp[i];
        }
    }

    public void hash(int[] plaintext, int length){
        int[] paddedText = padding(plaintext, length);
        for (int i = 0; i < paddedText.length / 16; i++) {
            int[] temp = Arrays.copyOfRange(paddedText, i*16, (i+1)*16);
            singleHash(temp);
        }
        resultBytes = int2bytes(register);
        result = bytes2String(resultBytes);
    }

    public void hash(String plaintexts){
        byte[] temp = plaintexts.getBytes();
        byte[] bytes = new byte[plaintexts.length() + 1];
        for (int i = 0; i < temp.length; i++) {
            bytes[i] = temp[i];
        }
        bytes[plaintexts.length()] = (byte) 0x80;
        int[] data = new int[bytes.length / 4 + 1];
        for (int i = 0; i < plaintexts.length() / 4 + 1; i++) {
            data[i] = 0;
            for (int j = 0; j < 4; j++) {
                if (i*4+j >= bytes.length) continue;
                if(bytes[i*4+j] < 0)
                    data[i] += ((int)(bytes[i*4+j] + 256)) << (8 * j);
                else
                    data[i] += ((int)bytes[i*4+j]) << (8 * j);
            }
        }
        hash(data, bytes.length * 8);
    }

    public String bytes2String(byte[] bytes){
        StringBuffer ss = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            ss.append(String.format("%x", bytes[i]));
        }
        return new String(ss);
    }

    public byte[] int2bytes(int[] ints){
        byte[] bytes = new byte[ints.length * 4];
        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < 4; j++) {
                bytes[i*4+j] = (byte) (ints[i] >> (j*8));
            }
        }
        return bytes;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MD5 md5 = new MD5();
        String msg = "asrtyhfghjxdthrxsytxctr";
        md5.hash(msg);
        System.out.println(md5.result);
        MessageDigest md = MessageDigest.getInstance("MD5");
        System.out.println((md5.bytes2String(md.digest(msg.getBytes("UTF-8")))));
    }
}

