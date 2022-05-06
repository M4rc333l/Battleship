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
    private List<Ship> shipList = new ArrayList<Ship>();
    private Playground playground = new Playground();

    public void intialGUI() {
        JFrame frame = new JFrame("Battleships");
        frame.setSize(2000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(11, 11, 11, 11);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        for (int i = 0; i < 10; i++) {
            constraints.gridx = i + 1;
            constraints.gridy = 0;
            char letter = (char) (65 + i);
            JLabel letterLabel = new JLabel(String.valueOf(letter));
            frame.getContentPane().add(letterLabel, constraints);
            constraints.gridx = 0;
            constraints.gridy = i + 1;
            JLabel numberLabel = new JLabel(String.valueOf(i + 1));
            frame.getContentPane().add(numberLabel, constraints);
            for (int j = 0; j < 10; j++) {
                constraints.gridx = i + 1;
                constraints.gridy = j + 1;
                //Hier wird der Playground in einen Frame eingefügt
                frame.getContentPane().add(playground.getPlayground()[i][j], constraints);
            }
        }
        frame.setVisible(true);
    }

    public void placeShip(int x, int y){
        playground.getPlayground()[x][y].setBackground(Color.BLACK);
    }

    public void game() {

        final int[] zaehler = {0};
        final int[] x1 = {0};
        final int[] y1 = {0};
        final int[] x2 = {0};
        final int[] y2 = {0};
        for (int i = 0; i < playground.getPlayground().length; i++) {
            for (int j = 0; j < playground.getPlayground()[i].length; j++) {
                int finalI = i;
                int finalJ = j;
                playground.getPlayground()[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if (zaehler[0] == 0){
                            x1[0] = finalI;
                            y1[0] = finalJ;
                        }
                        if (zaehler[0] == 1){
                            x2[0] = finalI;
                            y2[0] = finalJ;
                        }
                        zaehler[0]++;

                        if (zaehler[0] == 2){
                            int size = 0;
                            if(turn == 7 || turn == 6) size = 4;
                            if(turn == 5 || turn == 4|| turn == 3) size = 3;
                            if(turn == 2 || turn == 1 || turn == 0) size = 2;

                            placeShip(x1[0],y1[0], x2[0], y2[0], size);
                            zaehler[0] = 0;
                            turn--;
                        }
                    }
                });
            }
        }
    }

    public void shipDestroyed() {
        for (int i = 0; i < shipList.size(); i++) {
            boolean destroyed = true;
            for (int j = 0; j < shipList.get(i).getSize(); j++) {
                if (!playground.getPlayground()[shipList.get(i).getPos()[0][j]][shipList.get(i).getPos()[1][j]].getText().equals("X")) {
                    destroyed = false;
                }
                if (destroyed == true) {
                    playground.getPlayground()[shipList.get(i).getPos()[0][j]][shipList.get(i).getPos()[1][j]].setBackground(Color.RED);
                    shipList.remove(i);
                }
            }
        }
    }
}