package Design;

import Game.Ship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Playground {

    private final JButton [][] playground = new JButton[10][10];
    private final List<Ship> shipList = new ArrayList<>();
    private final Color waterColor = new Color(80, 150, 255);
    private final Color shipColor = new Color(0,0,0);

    public Playground(){
        for (int i = 0; i < playground.length; i++) {
            for (int j = 0; j < playground[i].length; j++) {
                playground[i][j] = new JButton();
                playground[i][j].setPreferredSize(new Dimension(20,20));
                playground[i][j].setBackground(waterColor);
            }
        }
    }
    public JButton[][] getPlayground() {
        return playground;
    }

    public Color getShipColor(){
        return shipColor;
    }

    public List<Ship> getShipList(){
        return shipList;
    }

    public boolean hasNeighbor(int x, int y, Color color) {
        boolean hasNeighbor = false;
        try{
            if (playground[x+1][y].getBackground().equals(color)) {
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x+1][y+1].getBackground().equals(color)) {
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x+1][y-1].getBackground().equals(color)) {
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x][y+1].getBackground().equals(color)) {
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if ( playground[x][y-1].getBackground().equals(color)) {
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x-1][y].getBackground().equals(color)) {
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x-1][y+1].getBackground().equals(color)) {
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x-1][y-1].getBackground().equals(color)) {
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        return hasNeighbor;
    }
    public void changeButtons(int x, int y, int size, boolean change) {
        size--;
        for (int i = 0; i < playground.length; i++) {
            for (int j = 0; j < playground[i].length; j++) {
                if(change){
                    if(!playground[i][j].getBackground().equals(shipColor) && !hasNeighbor(i,j,shipColor)) {
                        playground[i][j].setBackground(waterColor);
                        playground[i][j].setEnabled(true);
                    }
                    if(hasNeighbor(i,j,shipColor) && !playground[i][j].getBackground().equals(shipColor)) {
                        playground[i][j].setBackground(Color.GRAY);
                        playground[i][j].setEnabled(false);
                    }
                }
                else{
                    if(!((i==x+size && j==y)|| (i==x-size && j==y) || (i==x && j==y+size) || (i==x && j==y-size)) && !playground[i][j].getBackground().equals(shipColor)) {
                            playground[i][j].setBackground(Color.GRAY);
                            playground[i][j].setEnabled(false);
                    }
                }
            }
        }
    }
    public void disableNotPlaceable(int size) {
        size--;
        for (int i = 0; i < playground.length; i++) {
            for (int j = 0; j < playground[i].length; j++) {
                boolean placeable = false;
                try {
                    if (playground[i][j+size].getBackground().equals(waterColor) && playground[i][j].getBackground().equals(waterColor)) {
                        placeable = true;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
                try {
                    if (playground[i][j-size].getBackground().equals(waterColor) && playground[i][j].getBackground().equals(waterColor)) {
                        placeable = true;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
                try {
                    if (playground[i+size][j].getBackground().equals(waterColor) && playground[i][j].getBackground().equals(waterColor)) {
                        placeable = true;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
                try {
                    if (playground[i-size][j].getBackground().equals(waterColor) && playground[i][j].getBackground().equals(waterColor)) {
                        placeable = true;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
                if (!(placeable || playground[i][j].getBackground().equals(shipColor) || playground[i][j].getBackground().equals(Color.GRAY))) {
                    playground[i][j].setBackground(Color.GRAY);
                    playground[i][j].setEnabled(false);
                }
            }
        }
    }
    public void clear(boolean deleteList){
        for (JButton[] jButtons : playground) {
            for (JButton jButton : jButtons) {
                jButton.setBackground(waterColor);
                jButton.setEnabled(true);
            }
        }
        if(deleteList){
            for(int i=shipList.size()-1;i>=0;i--){
                shipList.remove(i);
            }
        }
    }
    public Playground copyPlayground(Playground playground, int mode) {
        clear(true);
        for(int i=0;i<this.playground.length;i++) {
            for(int j=0;j<this.playground[i].length;j++) {
                if(mode == 1) {
                    if(playground.getPlayground()[i][j].getBackground().equals(shipColor)) {
                        this.getPlayground()[i][j].setBackground(playground.getPlayground()[i][j].getBackground());
                    }
                } else if(mode == 2) this.getPlayground()[i][j].setBackground(playground.getPlayground()[i][j].getBackground());
            }
        }
        for (int i=0;i<playground.getShipList().size();i++) {
            int[][] pos = new int[2][playground.getShipList().get(i).getSize()];
            for(int j =0;j<playground.getShipList().get(i).getSize();j++){
                pos[0][j] = playground.getShipList().get(i).getPos()[0][j];
                pos[1][j] = playground.getShipList().get(i).getPos()[1][j];
            }
            this.getShipList().add(new Ship(playground.getShipList().get(i).getSize(), pos));
        }
        return this;
    }
    public void enabled(boolean enable){
        for (JButton[] jButtons : this.playground) {
            for (JButton jButton : jButtons) {
                if(jButton.getBackground().equals(waterColor) && enable) jButton.setEnabled(true);
                else if(!enable) jButton.setEnabled(false);
            }
        }
    }
    public boolean shipsDestroyed(){
        boolean destroyed = true;
        for (Ship ship : shipList) {
            if (!ship.isDestroyed()) {
                destroyed = false;
                break;
            }
        }
        return destroyed;
    }
}