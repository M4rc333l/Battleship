package Design;

import javax.swing.*;
import java.awt.*;

public class Playground {

    private JButton [][] playground = new JButton[10][10];

    public Playground() {
        for (int i = 0; i < playground.length; i++) {
            for (int j = 0; j < playground[i].length; j++) {
                playground[i][j] = new FieldButton();
                playground[i][j].setPreferredSize(new Dimension(20, 20));
                playground[i][j].setBackground(new Color(80, 150, 255));
            }
        }
    }
    public JButton[][] getPlayground() {
        return playground;
    }
}