package Design;

import javax.swing.*;
import java.awt.*;

public class Playground {

    private final JButton [][] playground = new JButton[10][10];
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
    public Color getWaterColor(){
        return waterColor;
    }
    public Color getShipColor(){
        return shipColor;
    }

    public boolean hasNeighbor(int x, int y, Color color){
        boolean hasNeighbor = false;
        try{
            if (playground[x+1][y].getBackground().equals(color)){
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x+1][y+1].getBackground().equals(color)){
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x+1][y-1].getBackground().equals(color)){
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x][y+1].getBackground().equals(color)){
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if ( playground[x][y-1].getBackground().equals(color)){
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x-1][y].getBackground().equals(color)){
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x-1][y+1].getBackground().equals(color)){
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            if (playground[x-1][y-1].getBackground().equals(color)){
                hasNeighbor = true;
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}
        return hasNeighbor;
    }
    public void changeButtons(int x, int y, int size, boolean change){
        size--;
        for (int i = 0; i < playground.length; i++){
            for (int j = 0; j < playground[i].length; j++){
                if(change){
                    if(!playground[i][j].getBackground().equals(shipColor) && !hasNeighbor(i,j,shipColor)){
                        playground[i][j].setBackground(waterColor);
                        playground[i][j].setEnabled(true);
                    }
                    if(hasNeighbor(i,j,shipColor) && !playground[i][j].getBackground().equals(shipColor)){
                        playground[i][j].setBackground(Color.GRAY);
                        playground[i][j].setEnabled(false);
                    }
                }
                else{
                    if(!((i==x+size && j==y)|| (i==x-size && j==y) || (i==x && j==y+size) || (i==x && j==y-size)) && !playground[i][j].getBackground().equals(shipColor)){
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
                    playground[i][j].setBackground(Color.GREEN);
                    playground[i][j].setEnabled(false);
                }
            }
        }
    }
    public void clear(){
        for (int i = 0; i < playground.length; i++) {
            for (int j = 0; j < playground[i].length; j++) {
                playground[i][j].setBackground(waterColor);
                playground[i][j].setEnabled(true);
            }
        }
    }
}