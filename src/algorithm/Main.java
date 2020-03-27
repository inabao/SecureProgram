package algorithm;

public class Main {

    public static void main(String[] args) {
        int[] c = {1,0,0,1};
        int[] a = {0,0,0,1};
        int[] message = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        StreamCipher streamCipher = new StreamCipher(c);
        streamCipher.generateA(a);
        int[] m = streamCipher.decode(streamCipher.encode(message));
        for (int single_m:m
             ) {
            System.out.print(single_m+" ");
        }
    }
}
