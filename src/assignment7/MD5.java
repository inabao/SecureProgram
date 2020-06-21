package assignment7;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MD5 {
    private final static int A = 0x67452301; //0x01234567;
    private final static int B = 0xefcdab89; //0x89abcdef;
    private final static int C = 0x98badcfe; //0xfedcba98;
    private final static int D = 0x10325476; //0x76543210;

    private final static int[] ABCD = {A, B, C, D};

    private final static int[] T = {
            0xD76AA478, 0xE8C7B756, 0x242070DB, 0xC1BDCEEE, 0xF57C0FAF, 0x4787C62A, 0xA8304613, 0xFD469501,
            0x698098D8, 0x8B44F7AF, 0xFFFF5BB1, 0x895CD7BE, 0x6B901122, 0xFD987193, 0xA679438E, 0x49B40821,
            0xF61E2562, 0xC040B340, 0x265E5A51, 0xE9B6C7AA, 0xD62F105D, 0x02441453, 0xD8A1E681, 0xE7D3FBC8,
            0x21E1CDE6, 0xC33707D6, 0xF4D50D87, 0x455A14ED, 0xA9E3E905, 0xFCEFA3F8, 0x676F02D9, 0x8D2A4C8A,
            0xFFFA3942, 0x8771F681, 0x6D9D6122, 0xFDE5380C, 0xA4BEEA44, 0x4BDECFA9, 0xF6BB4B60, 0xBEBFBC70,
            0x289B7EC6, 0xEAA127FA, 0xD4EF3085, 0x04881D05, 0xD9D4D039, 0xE6DB99E5, 0x1FA27CF8, 0xC4AC5665,
            0xF4292244, 0x432AFF97, 0xAB9423A7, 0xFC93A039, 0x655B59C3, 0x8F0CCC92, 0xFFEFF47D, 0x85845DD1,
            0x6FA87E4F, 0xFE2CE6E0, 0xA3014314, 0x4E0811A1, 0xF7537E82, 0xBD3AF235, 0x2AD7D2BB, 0xEB86D391
    };

    private final static int[] S = {
            7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
            5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
            4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
            6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
    };

    private final static int[] M = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            1, 6, 11, 0, 5, 10, 15, 4, 9, 14, 3, 8, 13, 2, 7, 12,
            5, 8, 11, 14, 1, 4, 7, 10, 13, 0, 3, 6, 9, 12, 15, 2,
            0, 7, 14, 5, 12, 3, 10, 1, 8, 15, 6, 13, 4, 11, 2, 9
    };

    private List<int[]> init(String msg) {
        int L = msg.getBytes().length * 8;
        byte[] byteMsg = new byte[((L + 65) / 512 * 512 + 512) / 8];
//        int d = -(L + 65) % 512 + 512;
        for (int i = 0; i < msg.getBytes().length; i++) {
            byteMsg[i] = msg.getBytes()[i];
        }
        byteMsg[msg.getBytes().length] += 0b10000000;
        for (int i = byteMsg.length - 8; i < byteMsg.length; i++) {
            byteMsg[i] = (byte) (L % 256);
            L = L / 256;
            if (L == 0) {
                break;
            }
        }
        List<int[]> msgBlocks = new ArrayList<>();
        for (int i = 0; i < byteMsg.length / 64; i++) {
            int[] block = new int[16];
            for (int j = 0; j < 16; j++) {
                int subBlock = 0;
                for (int k = 0; k < 4; k++) {
                    int v = byteMsg[j * 4 + k] >= 0 ? byteMsg[j * 4 + k] : byteMsg[j * 4 + k] + 256;
                    subBlock += v << (8 * (k));
                }
                block[j] = subBlock;
            }
            msgBlocks.add(block);
        }
        return msgBlocks;
    }

    private int f(int x, int y, int z) {
        return (x & y) | (~x & z);
    }

    private int g(int x, int y, int z) {
        int temp = (x & z) | (y & ~z);
        return temp;
    }

    private int h(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private int i(int x, int y, int z) {
        return y ^ (x | ~z);
    }

    private int logicSwitch(int x, int y, int z, int round) {
        switch (round) {
            case 0:
                return f(x, y, z);
            case 1:
                return g(x, y, z);
            case 2:
                return h(x, y, z);
            case 3:
                return i(x, y, z);
        }
        throw new RuntimeException();
    }

    private void round(int[] r, int m, int s, int t, int round) {
        int ans = (logicSwitch(r[1], r[2], r[3], round) + r[0] + m + t);
        ans = ((ans << s) | (ans >>> (32 - s))) + r[1];
        r[0] = r[3];
        r[3] = r[2];
        r[2] = r[1];
        r[1] = ans;
    }

    private String hash(String message) {
        List<int[]> msgBlocks = init(message);
        for (int i = 0; i < msgBlocks.size(); i++) {
            int[] m = msgBlocks.get(i);
            System.out.println(Arrays.toString(m));
        }
        int[] register = {A, B, C, D};
        for (int i = 0; i < msgBlocks.size(); i++) {
            int[] m = msgBlocks.get(i);
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 16; k++) {
//                    System.out.println(register[0] + "," + register[1] + "," + register[2] + "," + register[3] + "," + m[M[k % 4]] + "," + S[k % 4] + "," + T[k % 4]);
                    round(register, m[M[j * 16 + k]], S[j * 16 + k], T[j * 16 + k], j);
                }
            }
            for (int j = 0; j < 4; j++) {
                register[j] += ABCD[j];
            }
        }
        byte[] result = new byte[16];
        int offset = 0;
        System.out.println(Arrays.toString(register));
        for (int i : register) {
            result[offset++] = (byte) (i      );
            result[offset++] = (byte) (i >>  8);
            result[offset++] = (byte) (i >> 16);
            result[offset++] = (byte) (i >> 24);
        }
        return byte2HexString(result);
    }

    public String byte2HexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int i = b >= 0 ? b : b + 256;
            String s = Integer.toString(i, 16);
            if (s.length() < 2) {
                s = "0" + s;
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String msg = "";
        MD5 md5 = new MD5();
        System.out.println(md5.hash("adgfkaasdgdlgfisdhfgilshdf"));
        MessageDigest md = MessageDigest.getInstance("MD5");
        System.out.println(md5.byte2HexString(md.digest("adgfkaasdgdlgfisdhfgilshdf".getBytes("UTF-8"))));
    }
}
