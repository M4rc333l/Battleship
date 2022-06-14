package Game;

import java.io.Serial;
import java.io.Serializable;

public class Ship implements Serializable {

    @Serial
    private static final long serialVersionUID = (long) (Math.random()*100000000000L);
    private final int size;
    private final int[][] pos;
    boolean destroyed = false;

    /**
     * Konstruktor
     * @param size
     * @param pos
     */
    public Ship(int size, int[][] pos) {
        this.size = size;
        this.pos = pos;
    }
    //Getter und Setter
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

    //toString
    @Override
    public String toString(){
        return "Size: " + size;
    }
}