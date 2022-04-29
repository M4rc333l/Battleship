package Game;

public class Ship {

    private boolean status;
    private int size;

    public Ship(boolean status, int size) {
        this.status = status;
        this.size = size;
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
    public void setSize(int size) {
        this.size = size;
    }
}