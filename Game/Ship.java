package Game;

public class Ship {

    private boolean status;
    private int size;
    private int[][] pos;

    public Ship(boolean status, int size, int[][] pos) {
        this.status = status;
        this.size = size;
        this.pos = pos;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) { this.size = size; }
    public int[][] getPos(){return pos; }
}