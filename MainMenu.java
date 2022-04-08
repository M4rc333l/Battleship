import Design.Playground;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;

public class MainMenu {
    public static void main(String[] args) {
        intialGUI();
    }

    public static void intialGUI(){
        JFrame frame = new JFrame("Battleships");
        frame.setSize(2000,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        Playground playground = new Playground();
        constraints.insets = new Insets(11,11,11,11);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        for(int i = 0; i < playground.getPlayground().length; i++){
            constraints.gridx = i+1;
            constraints.gridy = 0;
            char letter = (char) (65+i);
            JLabel letterLabel = new JLabel(String.valueOf(letter));
            frame.getContentPane().add(letterLabel,constraints);
            constraints.gridx = 0;
            constraints.gridy = i+1;
            JLabel numberLabel = new JLabel(String.valueOf(i+1));
            frame.getContentPane().add(numberLabel,constraints);
            for (int j = 0; j < playground.getPlayground()[i].length; j++){
                constraints.gridx = i+1;
                constraints.gridy = j+1;
                frame.getContentPane().add(playground.getPlayground()[i][j], constraints);
            }
        }
        frame.setVisible(true);
    }
}
