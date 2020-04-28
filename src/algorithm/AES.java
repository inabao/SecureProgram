package algorithm;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class AES {
    private int keyLength;
    private int plaintextLength;
    char[][] subKey;
    int[][] turns = {{10,12,14},{12,12,14},{14,14,14}};
    char[][] multiply = {{2,3,1,1},{1,2,3,1},{1,1,2,3},{3,1,1,2}};
    char[][] multiplyInv = {{14,11,13,9},{9,14,11,13},{13,9,14,11},{11,13,9,14}};
    private char[] rCon = {1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145, 57, 114, 228, 211, 189, 97, 194, 159, 37, 74, 148, 51, 102, 204, 131, 29, 58, 116, 232, 203, 141};
    private char[] SBox = {0x63,0x7c,0x77,0x7b,0xf2,0x6b,0x6f,0xc5,0x30,0x01,0x67,0x2b,0xfe,0xd7,0xab,0x76
                          ,0xca,0x82,0xc9,0x7d,0xfa,0x59,0x47,0xf0,0xad,0xd4,0xa2,0xaf,0x9c,0xa4,0x72,0xc0
                          ,0xb7,0xfd,0x93,0x26,0x36,0x3f,0xf7,0xcc,0x34,0xa5,0xe5,0xf1,0x71,0xd8,0x31,0x15
                          ,0x04,0xc7,0x23,0xc3,0x18,0x96,0x05,0x9a,0x07,0x12,0x80,0xe2,0xeb,0x27,0xb2,0x75
                          ,0x09,0x83,0x2c,0x1a,0x1b,0x6e,0x5a,0xa0,0x52,0x3b,0xd6,0xb3,0x29,0xe3,0x2f,0x84
                          ,0x53,0xd1,0x00,0xed,0x20,0xfc,0xb1,0x5b,0x6a,0xcb,0xbe,0x39,0x4a,0x4c,0x58,0xcf
                          ,0xd0,0xef,0xaa,0xfb,0x43,0x4d,0x33,0x85,0x45,0xf9,0x02,0x7f,0x50,0x3c,0x9f,0xa8
                          ,0x51,0xa3,0x40,0x8f,0x92,0x9d,0x38,0xf5,0xbc,0xb6,0xda,0x21,0x10,0xff,0xf3,0xd2
                          ,0xcd,0x0c,0x13,0xec,0x5f,0x97,0x44,0x17,0xc4,0xa7,0x7e,0x3d,0x64,0x5d,0x19,0x73
                          ,0x60,0x81,0x4f,0xdc,0x22,0x2a,0x90,0x88,0x46,0xee,0xb8,0x14,0xde,0x5e,0x0b,0xdb
                          ,0xe0,0x32,0x3a,0x0a,0x49,0x06,0x24,0x5c,0xc2,0xd3,0xac,0x62,0x91,0x95,0xe4,0x79
                          ,0xe7,0xc8,0x37,0x6d,0x8d,0xd5,0x4e,0xa9,0x6c,0x56,0xf4,0xea,0x65,0x7a,0xae,0x08
                          ,0xba,0x78,0x25,0x2e,0x1c,0xa6,0xb4,0xc6,0xe8,0xdd,0x74,0x1f,0x4b,0xbd,0x8b,0x8a
                          ,0x70,0x3e,0xb5,0x66,0x48,0x03,0xf6,0x0e,0x61,0x35,0x57,0xb9,0x86,0xc1,0x1d,0x9e
                          ,0xe1,0xf8,0x98,0x11,0x69,0xd9,0x8e,0x94,0x9b,0x1e,0x87,0xe9,0xce,0x55,0x28,0xdf
                          ,0x8c,0xa1,0x89,0x0d,0xbf,0xe6,0x42,0x68,0x41,0x99,0x2d,0x0f,0xb0,0x54,0xbb,0x16};
    private char[] SBoxInv = {0x52,0x09,0x6a,0xd5,0x30,0x36,0xa5,0x38,0xbf,0x40,0xa3,0x9e,0x81,0xf3,0xd7,0xfb
                             ,0x7c,0xe3,0x39,0x82,0x9b,0x2f,0xff,0x87,0x34,0x8e,0x43,0x44,0xc4,0xde,0xe9,0xcb
                             ,0x54,0x7b,0x94,0x32,0xa6,0xc2,0x23,0x3d,0xee,0x4c,0x95,0x0b,0x42,0xfa,0xc3,0x4e
                             ,0x08,0x2e,0xa1,0x66,0x28,0xd9,0x24,0xb2,0x76,0x5b,0xa2,0x49,0x6d,0x8b,0xd1,0x25
                             ,0x72,0xf8,0xf6,0x64,0x86,0x68,0x98,0x16,0xd4,0xa4,0x5c,0xcc,0x5d,0x65,0xb6,0x92
                             ,0x6c,0x70,0x48,0x50,0xfd,0xed,0xb9,0xda,0x5e,0x15,0x46,0x57,0xa7,0x8d,0x9d,0x84
                             ,0x90,0xd8,0xab,0x00,0x8c,0xbc,0xd3,0x0a,0xf7,0xe4,0x58,0x05,0xb8,0xb3,0x45,0x06
                             ,0xd0,0x2c,0x1e,0x8f,0xca,0x3f,0x0f,0x02,0xc1,0xaf,0xbd,0x03,0x01,0x13,0x8a,0x6b
                             ,0x3a,0x91,0x11,0x41,0x4f,0x67,0xdc,0xea,0x97,0xf2,0xcf,0xce,0xf0,0xb4,0xe6,0x73
                             ,0x96,0xac,0x74,0x22,0xe7,0xad,0x35,0x85,0xe2,0xf9,0x37,0xe8,0x1c,0x75,0xdf,0x6e
                             ,0x47,0xf1,0x1a,0x71,0x1d,0x29,0xc5,0x89,0x6f,0xb7,0x62,0x0e,0xaa,0x18,0xbe,0x1b
                             ,0xfc,0x56,0x3e,0x4b,0xc6,0xd2,0x79,0x20,0x9a,0xdb,0xc0,0xfe,0x78,0xcd,0x5a,0xf4
                             ,0x1f,0xdd,0xa8,0x33,0x88,0x07,0xc7,0x31,0xb1,0x12,0x10,0x59,0x27,0x80,0xec,0x5f
                             ,0x60,0x51,0x7f,0xa9,0x19,0xb5,0x4a,0x0d,0x2d,0xe5,0x7a,0x9f,0x93,0xc9,0x9c,0xef
                             ,0xa0,0xe0,0x3b,0x4d,0xae,0x2a,0xf5,0xb0,0xc8,0xeb,0xbb,0x3c,0x83,0x53,0x99,0x61
                             ,0x17,0x2b,0x04,0x7e,0xba,0x77,0xd6,0x26,0xe1,0x69,0x14,0x63,0x55,0x21,0x0c,0x7d};

    public AES(char[] key)throws Exception{
        setKey(key);
        generateKey();
    }


    public char[] encode(char[] plaintext) throws Exception{
        char[][] m = reshape(plaintext);
        char[][] firstKey = getSubKey(0, plaintextLength);
        int t = turns[keyLength/2-2][plaintextLength/2-2];
        addRoundKey(m, firstKey);
        for (int i = 1; i < t; i++) {
            sBoxReplace(m);
            shiftLeft(m);
            m = mixColumns(m);
            firstKey = getSubKey(i*plaintextLength,(i+1)*plaintextLength);
            addRoundKey(m, firstKey);
        }
        sBoxReplace(m);
        shiftLeft(m);
        firstKey = getSubKey(t*plaintextLength,(t+1)*plaintextLength);
        addRoundKey(m, firstKey);
        return flatten(m);
    }

    public char[] decode(char[] plaintext) throws Exception{
        char[][] m = reshape(plaintext);
        int t = turns[keyLength/2-2][plaintextLength/2-2];
        char[][] firstKey = getSubKey(t*plaintextLength, (t+1)*plaintextLength);
        addRoundKey(m, firstKey);
        for (int i = t-1; i>=1; i--) {
            shiftLeftInv(m);
            sBoxReplaceInv(m);
            firstKey = getSubKey(i*plaintextLength,(i+1)*plaintextLength);
            addRoundKey(m, firstKey);
            m = mixColumnsInv(m);
        }
        shiftLeftInv(m);
        sBoxReplaceInv(m);
        firstKey = getSubKey(0, plaintextLength);
        addRoundKey(m, firstKey);
        return flatten(m);
    }

    private char[][] reshape(char[] plaintext)throws Exception{
        if(plaintext.length % 8 != 0)
            throw new Exception();
        plaintextLength = plaintext.length / 4;
        char[][] m = new char[plaintextLength][4];
        for (int i = 0; i < plaintextLength; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] = plaintext[i*4+j];
            }
        }
        return m;
    }

    private void shiftLeft(char[][] rowText){
        for (int i = 1; i < 4; i++) {
            char[] temp = new char[i];
            for (int j = 0; j < i; j++) {
                temp[j] = rowText[j][i];
            }
            for (int j = 0; j < plaintextLength - i; j++) {
                rowText[j][i] = rowText[j+i][i];
            }
            for (int j = 0; j < i; j++) {
                rowText[j+plaintextLength-i][i] = temp[j];
            }
        }
    }

    private void shiftLeftInv(char[][] rowText){
        for (int k = 1; k < 4; k++) {
            int i = plaintextLength - k;
            char[] temp = new char[i];
            for (int j = 0; j < i; j++) {
                temp[j] = rowText[j][k];
            }
            for (int j = 0; j < plaintextLength - i; j++) {
                rowText[j][k] = rowText[j+i][k];
            }
            for (int j = 0; j < i; j++) {
                rowText[j+plaintextLength-i][k] = temp[j];
            }
        }
    }


    private char[][] mixColumns(char[][] rowText){
        char[][] newText = new char[rowText.length][4];
        for (int i = 0; i < rowText.length; i++) {
            for (int j = 0; j < 4; j++) {
                newText[i][j] = 0;
                for (int l = 0; l < 4; l++) {
                    newText[i][j] ^= mul(rowText[i][l], multiply[j][l]);
                }
            }
        }
        return newText;
    }

    private char[][] mixColumnsInv(char[][] rowText){
        char[][] newText = new char[rowText.length][4];
        for (int i = 0; i < rowText.length; i++) {
            for (int j = 0; j < 4; j++) {
                newText[i][j] = 0;
                for (int l = 0; l < 4; l++) {
                    newText[i][j] ^= mul(rowText[i][l], multiplyInv[j][l]);
                }
            }
        }
        return newText;
    }

    public char mul(char n, char m){
        int result = 0;
        int[] bits = new int[8];
        int temp = 0;
        while(m!=0){
            bits[temp] = m % 2;
            m /= 2;
            temp ++;
        }
        while(temp > 0){
            result *= 2;
            if(result>=256) result ^= 283;
            result ^= n*bits[temp-1];
            temp--;
        }
        return (char)result;
    }


    private char[] flatten(char[][] m){
        char[] text = new char[plaintextLength*4];
        for (int i = 0; i < plaintextLength; i++) {
            for (int j = 0; j < 4; j++) {
                text[i*4+j] = m[i][j];
            }
        }
        return text;
    }

    private void addRoundKey(char[][] m, char[][] subKey){
        for (int i = 0; i < plaintextLength; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] ^= subKey[i][j];
            }
        }
    }

    public void setKey(char[] key)throws Exception{
        if(key.length % 4 != 0)
            throw new Exception();
        keyLength = key.length / 4;
        subKey = new char[120][4];
        for (int i = 0; i < keyLength; i++) {
            for (int j = 0; j < 4; j++) {
                subKey[i][j] = key[4*i+j];
            }
        }
    }
    private void sBoxReplace(char[] rowKey){
        for (int i = 0; i < rowKey.length; i++) {
            rowKey[i] = SBox[rowKey[i]];
        }
    }
    private void sBoxReplace(char[][] rowKey){
        for (int i = 0; i < rowKey.length; i++) {
            sBoxReplace(rowKey[i]);
        }
    }

    private void sBoxReplaceInv(char[] rowKey){
        for (int i = 0; i < rowKey.length; i++) {
            rowKey[i] = SBoxInv[rowKey[i]];
        }
    }
    private void sBoxReplaceInv(char[][] rowKey){
        for (int i = 0; i < rowKey.length; i++) {
            sBoxReplaceInv(rowKey[i]);
        }
    }


    private char[][] getSubKey(int begin, int end){
        char[][] subKey = new char[end-begin][4];
        for (int i = 0; i < end - begin; i++) {
            subKey[i] = this.subKey[begin+i];
        }
        return subKey;
    }

    private void generateKey(){
        for (int i = 1; i < (120 / keyLength); i++) {
            for (int j = 0; j < keyLength; j++) {
                if (j == 0){
                    subKey[i*4] = subWord(subKey[(i - 1)*4+3]);
                    subKey[i*4][0] ^= rCon[i-1];
                }else{
                    subKey[i*4+j] = subKey[i*4+j-1].clone();
                }
                if(keyLength>6 && j==4)
                    sBoxReplace(subKey[i*4+j]);
                for (int k = 0; k < 4; k++) {
                    subKey[i*4+j][k] ^= subKey[(i-1)*4+j][k];
                }
            }
        }
    }

    private char[] subWord(char[] words){
        char[] temp = new char[4];
        temp[3] = words[0];
        for (int i = 0; i < 3; i++) {
            temp[i] = words[i+1];
        }
        sBoxReplace(temp);
        return temp;
    }

    public static void main(String[] args) throws Exception {
        char[] k = {43,126,21,22,40,174,210,166,171,247,21,136,9,207,79,60};

        char[] m = {50,67,246,168,136,90,48,141,49,49,152,162,224,55,7,52};
        AES aes = new AES(k);
        for (char i:aes.decode(aes.encode(m))) {
            System.out.print(" "+(int)i);
        }
    }
}
