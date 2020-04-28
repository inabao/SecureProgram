package algorithm;

import java.util.BitSet;

public class DES {
    private BitSet key;
    private BitSet[] subKey = new BitSet[16];
    private int[] subKeySituation = {57,49,41,33,25,17,9,1,
                                     58,50,42,34,26,18,10,2,
                                     59,51,43,35,27,19,11,3,
                                     60,52,44,36,63,55,47,39,
                                     31,23,15,7,62,54,46,38,
                                     30,22,14,6,61,53,45,37,
                                     29,21,13,5,28,20,12,4};
    private int[] compressSituation = {14,17,11,24,1,5,
                                       3,28,15,6,21,10,
                                       23,19,12,4,26,8,
                                       16,7,27,20,13,2,
                                       41,52,31,37,47,55,
                                       30,40,51,45,33,48,
                                       44,49,39,56,34,53,
                                       46,42,50,36,29,32};
    private int[] beginSituation = {58,50,42,34,26,18,10,2,
                                    60,52,44,36,28,20,12,4,
                                    62,54,46,38,30,22,14,6,
                                    64,56,48,40,32,24,16,8,
                                    57,49,41,33,25,17, 9,1,
                                    59,51,43,35,27,19,11,3,
                                    61,53,45,37,29,21,13,5,
                                    63,55,47,39,31,23,15,7};
    private int[] beginInvSituation = {40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,
                               38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
                               36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,
                               34,2,42,10,50,18,58,26,33,1,41, 9,49,17,57,25};
    private int[] extendSituation = {32,1,2,3,4,5,
                             4,5,6,7,8,9,
                             8,9,10,11,12,13,
                             12,13,14,15,16,17,
                             16,17,18,19,20,21,
                             20,21,22,23,24,25,
                             24,25,26,27,28,29,
                             28,29,30,31,32,1};
    private int[][] Sbox = {{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7,
                     0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8,
                     4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0,
                     15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13},
                    {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10,
                     3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5,
                     0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15,
                     13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9},
                    {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8,
                     13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1,
                     13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7,
                     1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12},
                    {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15,
                     13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9,
                     10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4,
                     3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14},
                    {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9,
                     14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6,
                     4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14,
                     11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3},
                    {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11,
                     10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8,
                     9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6,
                     4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13},
                    {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1,
                     13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6,
                     1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2,
                     6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12},
                    {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7,
                     1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2,
                     7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8,
                     2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}};
    private int[] pSituation = {16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,
                        2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25};
    private int[] numKey = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};

    public DES(BitSet key){
        this.key = key;
        generateSubKey();
    }

    public void setKey(BitSet key) {
        this.key = key;
        generateSubKey();
    }

    public BitSet getKey() {
        return key;
    }

    //加密
    public BitSet encode(BitSet bitSet){
        bitSet = changeSituation(bitSet, beginSituation);
        bitSet = code(bitSet, true);
        bitSet = changeSituation(bitSet, beginInvSituation);
        return bitSet;
    }

    //解密
    public BitSet decode(BitSet bitSet){
        bitSet = changeSituation(bitSet, beginSituation);
        bitSet = code(bitSet, false);
        bitSet = changeSituation(bitSet, beginInvSituation);
        return bitSet;
    }

    //初始置换，64bit ---> 56bit
    private BitSet subKeyInitial(){
        return changeSituation(key, subKeySituation);
    }

    //字节替换
    private BitSet changeSituation(BitSet sourceSet, int[] situation){
        BitSet targetSet = new BitSet(situation.length);
        for (int i = 0; i < situation.length; i++) {
            targetSet.set(i, sourceSet.get(situation[i]-1));
        }
        return targetSet;
    }

    //左移一位
    private BitSet cycleLeft(BitSet bitSet){
        boolean label = bitSet.get(0);
        int i;
        BitSet temp = new BitSet(28);
        for (i = 0; i < bitSet.length()-1; i++) {
            temp.set(i, bitSet.get(i+1));
        }
        temp.set(27, label);
        return temp;
    }

    // 左移两位
    private BitSet cycleLeft2(BitSet bitSet){
        boolean label = bitSet.get(0);
        boolean label1 = bitSet.get(1);
        int i;
        BitSet temp = new BitSet(28);
        for (i = 0; i < bitSet.length()-2; i++) {
            temp.set(i, bitSet.get(i+2));
        }
        temp.set(26, label);
        temp.set(27, label1);
        return temp;
    }

    //生成子密钥
    private void generateSubKey(){
        BitSet initBitSet = subKeyInitial();
        BitSet C = initBitSet.get(0, 28);
        BitSet D = initBitSet.get(28, 56);
        for (int i = 0; i < 16; i++) {
            if(numKey[i]==1){
                C = cycleLeft(C);
                D = cycleLeft(D);
            }else{
                C = cycleLeft2(C);
                D = cycleLeft2(D);
            }
            for (int j = 0; j < 28; j++) {
                initBitSet.set(j, C.get(j));
            }
            for (int j = 0; j < 28; j++) {
                initBitSet.set(j + 28, D.get(j));
            }
            subKey[i] = changeSituation(initBitSet, compressSituation);
        }
    }

    //编码
    private BitSet code(BitSet bitSet, boolean encode){
        BitSet L = bitSet.get(0, 32);
        BitSet R = bitSet.get(32, 64);

        for (int i = 0; i < 16; i++) {
            BitSet temp;
            temp = (BitSet) R.clone();
            if(encode)
                R = lunFunction(R, subKey[i]);
            else
                R = lunFunction(R, subKey[15-i]);
            R.xor(L);
            System.out.println(L);
            L = temp;
        }
        BitSet newBitSet = new BitSet(64);
        for (int i = 0; i < 32; i++) {
            newBitSet.set(i, R.get(i));
        }
        for (int i = 0; i < 32; i++) {
            newBitSet.set(32 + i, L.get(i));
        }
        return newBitSet;
    }

    //S盒替换
    private BitSet sBoxReplace(BitSet bitSet, int[] box){
        int value_x = (bitSet.get(0) ? 1:0) * 2 + (bitSet.get(5) ? 1:0);
        int value_y = 0;
        for (int i = 1; i <= 4; i++) {
            value_y += (int)((bitSet.get(i) ? 1:0) * Math.pow(2, 4-i));
        }
        int value = box[value_x*16 + value_y];
        BitSet newBitSet = new BitSet(4);
        for (int i = 0; i < 4; i++) {
            if(value%2 == 1) newBitSet.set(3-i, true);
            else newBitSet.set(3-i, false);
            value /= 2;
        }
        return newBitSet;
    }

    //轮函数
    private BitSet lunFunction(BitSet bitSet, BitSet subKey){
        bitSet = changeSituation(bitSet, extendSituation);
        bitSet.xor(subKey);
        BitSet newBitSet = new BitSet(32);
        for (int i = 0; i < 8; i++) {
            BitSet singleBitSet = bitSet.get(i*6, (i+1)*6);
            singleBitSet = sBoxReplace(singleBitSet, Sbox[i]);
            for (int j = 0; j < 4; j++) {
                newBitSet.set(i*4+j, singleBitSet.get(j));
            }
        }
        return changeSituation(newBitSet, pSituation);
    }

    public static void main(String[] args) {
        int[] a ={0,0,0,1,0,0,1,1,
                  0,0,1,1,0,1,0,0,
                  0,1,0,1,0,1,1,1,
                  0,1,1,1,1,0,0,1,
                  1,0,0,1,1,0,1,1,
                  1,0,1,1,1,1,0,0,
                  1,1,0,1,1,1,1,1,
                  1,1,1,1,0,0,0,1};
        int[] b ={0,0,0,0,0,0,0,1,
                  0,0,1,0,0,0,1,1,
                  0,1,0,0,0,1,0,1,
                  0,1,1,0,0,1,1,1,
                  1,0,0,0,1,0,0,1,
                  1,0,1,0,1,0,1,1,
                  1,1,0,0,1,1,0,1,
                  1,1,1,0,1,1,1,1};
        BitSet key = new BitSet(64);
        BitSet m = new BitSet(64);
        for (int i = 0; i < 64; i++) {
            if(a[i]==0) key.set(i, false);
            else key.set(i, true);
        }
        for (int i = 0; i < 64; i++) {
            if(b[i]==0) m.set(i, false);
            else m.set(i, true);
        }
        DES des = new DES(key);
        System.out.println(m);
        System.out.println(des.encode(m));
        System.out.println(des.decode(des.encode(m)));
    }
}
