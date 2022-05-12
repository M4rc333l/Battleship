package Game;

public class Ship {

    private boolean status;
    private int size;
    private int[][] pos;
    private boolean orientation;

    public Ship(boolean status, int size, int[][] pos, boolean orientation) {
        this.status = status;
        this.size = size;
        this.pos = pos;
        this.orientation = orientation;
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
    public boolean getOrientation() {return orientation;}
}