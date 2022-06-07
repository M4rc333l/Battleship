package Game;

public class Ship {

    private final int size;
    private final int[][] pos;
    boolean destroyed = false;

    public Ship(int size, int[][] pos) {
        this.size = size;
        this.pos = pos;
    }
    public int getSize() {
        return size;
    }
    public int[][] getPos() {
        return pos;
    }
    public void setDestroyed(){
        destroyed = true;
    }
    public boolean isDestroyed(){
        return destroyed;
    }
    @Override
    public String toString(){
        return "Size: " + size;
    }
}