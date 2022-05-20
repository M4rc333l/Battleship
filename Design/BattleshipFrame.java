package Design;

import javax.swing.*;
import java.awt.*;

public class BattleshipFrame extends JFrame {

    private final GridBagConstraints constraints = new GridBagConstraints();

    public BattleshipFrame() {
        super();
        setSize(2000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        constraints.insets = new Insets(22, 22, 22, 22);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
    }
    public void intialGUI(int size, Playground playground) {
        for (int i = size-10; i < size; i++) {
            constraints.gridx = i+1;
            constraints.gridy = 0;
            char letter = (char) (65+(i-(size-10)));
            JLabel letterLabel = new JLabel(String.valueOf(letter));
            getContentPane().add(letterLabel, constraints);
            constraints.gridx = size-10;
            constraints.gridy = i+1 - (size-10);
            JLabel numberLabel = new JLabel(String.valueOf((i-(size-10))+1));
            getContentPane().add(numberLabel, constraints);
            for (int j = 0; j < 10; j++) {
                constraints.gridx = i+1;
                constraints.gridy = j+1;
                //Hier wird der Playground in einen Frame eingefügt
                getContentPane().add(playground.getPlayground()[i-(size-10)][j],constraints);
            }
        }
        setVisible(true);
    }
}