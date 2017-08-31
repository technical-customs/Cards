package card;

public class Pair<K, V> {
    K a;
    V b;
    public Pair(K a, V b){
        this.a = a;
        this.b = b;
    }
    
    public K getA(){
        return this.a;
    }
    public V getB(){
        return this.b;
    }
}