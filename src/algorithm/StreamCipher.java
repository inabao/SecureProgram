package algorithm;

public class StreamCipher {
    private int[] c;
    private int[] a;

    public StreamCipher(int[] c){
        this.c = c;
    }

    public void generateA(int[] inital) {
        int i = 0;
        a = new int[(int)Math.pow(2, inital.length)-1];
        for(; i < inital.length; i++){
            a[i] = inital[i];
        }
        for (; i < a.length ; i++) {
            a[i] = 0;
            for (int j = 0; j < c.length; j++) {
                a[i] += (c[j] * a[i-j-1]) % 2;
            }
        }
    }

    private int[] code(int[] m){
        int i = 0;
        int[] c = new int[m.length];
        for (int j = 0; j < m.length; j++) {
            c[j] = (m[j] + a[i]) % 2;
            i++;
            i %= a.length;
        }
        return c;
    }

    public int[] encode(int[] m){
        return code(m);
    }

    public int[] decode(int[] c){
        return code(c);
    }

}
