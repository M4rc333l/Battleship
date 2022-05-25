package Game;

public class Ship {

    private final int size;
    private final int[][] pos;

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
    @Override
    public String toString(){
        return "Size: " + size;
    }
}