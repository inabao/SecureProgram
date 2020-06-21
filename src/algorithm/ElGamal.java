package algorithm;

import java.math.BigInteger;
import java.util.Random;

public class ElGamal {
    private static class Point {
        int x;
        int y;
        Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    private Point P = new Point(13, 23);
    private Point Q;
    private Point c1;
    private Point c2;
    private int p = 29;
    private int a = 4;
    private int k = 7;
    private int x = 9;

    public ElGamal(){
        Q = multiply(x, P);
    }

    public Point getC1() {
        return c1;
    }

    public Point getC2() {
        return c2;
    }

    public void encode(Point m){
        c1 = multiply(k, P);
        c2 = add(m, multiply(k, Q));
    }

    public Point decode(){
        return sub(c2, multiply(x, c1));
    }

    private Point sub(Point a, Point b){
        Point point = new Point(b.x, (-b.y+p)%p);
        return add(a, point);
    }

    private Point add(Point a, Point b){
        int nenda;
        if (a.x == b.x && a.y == b.y){
            nenda = (3 * a.x * a.x + this.a ) * (BigPrimeNumber.exgcd(BigInteger.valueOf(p),
                    BigInteger.valueOf(2*b.y).mod(BigInteger.valueOf(p))).intValue());
        }else{
            nenda = (b.y-a.y) * (BigPrimeNumber.exgcd(BigInteger.valueOf(p),
                    BigInteger.valueOf((b.x-a.x)%p).mod(BigInteger.valueOf(p))).intValue()) % p;
        }
        int x = ((nenda*nenda - a.x - b.x) % p + p) % p;
        int y = ((nenda*(a.x - x) - a.y) % p + p) % p;
        Point point = new Point(x, y);
        return point;
    }

    private Point multiply(int n, Point p){
        Point temp = p;
        for (int i = 0; i < n - 1; i++) {
            temp = add(temp, p);
        }
        return temp;
    }



    public static void main(String[] args) {
        ElGamal elGamal = new ElGamal();
        elGamal.encode(new ElGamal.Point(11,22));
        System.out.println(elGamal.decode().x+" "+elGamal.decode().y);
    }
}
