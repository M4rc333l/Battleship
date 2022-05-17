package Game;

import Design.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private int turn = 7;

    public Game() {

    }

    //Playground erstellen und Schiffliste erstellen
    private final List<Ship> shipList = new ArrayList<>();
    private final Playground playground = new Playground();

    public void intialGUI(){
        JFrame frame = new JFrame("Battleships");
        frame.setSize(2000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(11, 11, 11, 11);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        for (int i = 0; i < 10; i++) {
            constraints.gridx = i+1;
            constraints.gridy = 0;
            char letter = (char) (65+i);
            JLabel letterLabel = new JLabel(String.valueOf(letter));
            frame.getContentPane().add(letterLabel, constraints);
            constraints.gridx = 0;
            constraints.gridy = i+1;
            JLabel numberLabel = new JLabel(String.valueOf(i+1));
            frame.getContentPane().add(numberLabel, constraints);
            for (int j = 0; j < 10; j++) {
                constraints.gridx = i+1;
                constraints.gridy = j+1;
                //Hier wird der Playground in einen Frame eingefügt
                frame.getContentPane().add(playground.getPlayground()[i][j],constraints);
            }
        }
        frame.setVisible(true);
    }


    public void placeShip(int x1, int y1, int x2, int y2, int size){
        int [][] pos = new int[2][size];
        boolean vertical = y1 - y2 != 0;
        for(int i = 0; i < size; i++){
            if(!vertical && x2 > x1){
                pos[0][i] = x1+i;
                pos[1][i] = y1;
                playground.getPlayground()[x1+i][y1].setEnabled(false);
                playground.getPlayground()[x1+i][y1].setBackground(playground.getShipColor());
            }
            if(!vertical && x1 > x2){
                pos[0][i] = x1-i;
                pos[1][i] = y1;
                playground.getPlayground()[x1-i][y1].setEnabled(false);
                playground.getPlayground()[x1-i][y1].setBackground(playground.getShipColor());
            }
            if(vertical && y2 > y1){
                pos[0][i] = x1;
                pos[1][i] = y1+i;
                playground.getPlayground()[x1][y1+i].setEnabled(false);
                playground.getPlayground()[x1][y1+i].setBackground(playground.getShipColor());
            }
            if(vertical && y1 > y2){
                pos[0][i] = x1;
                pos[1][i] = y1-i;
                playground.getPlayground()[x1][y1-i].setEnabled(false);
                playground.getPlayground()[x1][y1-i].setBackground(playground.getShipColor());
            }
        }
        shipList.add(new Ship(true,size,pos,vertical));
    }

    public void game(){

        final int[] zaehler = {0};
        final int[] x1 = {0};
        final int[] y1 = {0};
        final int[] x2 = {0};
        final int[] y2 = {0};
        for (int i = 0; i < playground.getPlayground().length; i++){
            for (int j = 0; j < playground.getPlayground()[i].length; j++){
                int finalI = i;
                int finalJ = j;
                playground.getPlayground()[i][j].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if(playground.getPlayground()[finalI][finalJ].isEnabled()){
                            if(turn > -1){
                                int size = 0;
                                if(turn == 7 || turn == 6) size = 4;
                                if(turn == 5 || turn == 4|| turn == 3) size = 3;
                                if(turn == 2 || turn == 1 || turn == 0) size = 2;

                                if (zaehler[0] == 0){
                                    x1[0] = finalI;
                                    y1[0] = finalJ;
                                    playground.getPlayground()[finalI][finalJ].setEnabled(false);
                                    playground.changeButtons(finalI,finalJ,size,false);
                                }
                                if (zaehler[0] == 1){
                                    x2[0] = finalI;
                                    y2[0] = finalJ;
                                }
                                zaehler[0]++;

                                if (zaehler[0] == 2){
                                    placeShip(x1[0],y1[0], x2[0], y2[0], size);
                                    playground.changeButtons(finalI,finalJ,size,true);
                                    if(turn!=6 && turn !=3) playground.disableNotPlaceable(size);
                                    else if(turn == 6 || turn ==3) playground.disableNotPlaceable(size-1);
                                    zaehler[0] = 0;
                                    turn--;
                                }
                            }
                            else if (turn==-1){
                                playground.clear();
                                turn--;
                            }
                            else{
                                playground.getPlayground()[finalI][finalJ].setEnabled(false);
                                hit(finalI, finalJ);
                            }
                        }
                    }
                });
            }
        }
    }
    public void shipDestroyed(){
        for (int i = 0; i < shipList.size(); i++){
            boolean destroyed = true;
            for (int j = 0; j < shipList.get(i).getSize(); j++){
                //if (!playground.getPlayground()[shipList.get(i).getPos()[0][j]][shipList.get(i).getPos()[1][j]].getText().equals("X")){
                if (!playground.getPlayground()[shipList.get(i).getPos()[0][j]][shipList.get(i).getPos()[1][j]].getBackground().equals(Color.ORANGE)){
                        destroyed = false;
                }
            }
            if (destroyed){
                for (int j = 0; j < shipList.get(i).getSize(); j++){
                    playground.getPlayground()[shipList.get(i).getPos()[0][j]][shipList.get(i).getPos()[1][j]].setBackground(Color.RED);
                }
                shipList.remove(i);
            }
        }
        for (int i = 0; i < playground.getPlayground().length; i++){
            for (int j = 0; j < playground.getPlayground()[i].length; j++){
            if(playground.hasNeighbor(i,j,Color.RED) && !playground.getPlayground()[i][j].getBackground().equals(Color.RED)){
                        playground.getPlayground()[i][j].setEnabled(false);
                        playground.getPlayground()[i][j].setBackground(Color.YELLOW);
                    }
            }
        }
        if(shipList.isEmpty()){

        }
    }
    public void hit(int x, int y){
        for (Ship ship : shipList) {
            System.out.println("SCHIFF 1 mit Groeße: " + ship.getSize());
            for (int j = 0; j < ship.getSize(); j++) {
                System.out.println("J : " + j);
                System.out.println("POS1 : " + ship.getPos()[0][j]);
                System.out.println("POS2 : " + ship.getPos()[1][j]);
                if (ship.getPos()[0][j] == x && ship.getPos()[1][j] == y) {
                    //playground.getPlayground()[ship.getPos()[0][j]][ship.getPos()[1][j]].setText("X TREFFER");
                    playground.getPlayground()[ship.getPos()[0][j]][ship.getPos()[1][j]].setBackground(Color.ORANGE);
                    shipDestroyed();
                    return;
                }
                System.out.println();
            }
        }
        //playground.getPlayground()[x][y].setText("X KEIN TREFFER");
        playground.getPlayground()[x][y].setBackground(Color.WHITE);
    }
}