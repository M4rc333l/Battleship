package Design;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.BorderLayout.CENTER;

public class BattleshipFrame extends JFrame {

    private final GridBagConstraints constraints = new GridBagConstraints();
    private final Panel pCenter = new Panel();
    private final JTextArea chat = new JTextArea();
    private final JButton hostGame = new JButton("Host Game");
    private final JButton joinGame = new JButton("Join Game");
    private ArrayList<String> chatList = new ArrayList<>();

    public BattleshipFrame() {
        super();
        setSize(2000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Panel pWest = new Panel();
        pWest.add(hostGame, 0);
        pWest.add(joinGame, 1);
        Panel pSouth = new Panel();
        chat.setDisabledTextColor(Color.BLACK);
        chat.setPreferredSize(new Dimension(1000, 200));
        chat.setEnabled(false);
        pSouth.add(chat, 0);
        pCenter.setLayout(new GridBagLayout());
        add(pCenter, CENTER);
        add(pSouth, BorderLayout.SOUTH);
        add(pWest, BorderLayout.WEST);
        setVisible(true);
    }
    public JButton getHostGameButton(){
        return hostGame;
    }
    public JButton getJoinGameButton(){
        return joinGame;
    }
    public void setText(String text){
        chatList.add(text);
        String result = "";
        for(int i=0;i<chatList.size();i++){
            result = result + "\n" + chatList.get(i);
        }
        chat.setText(result);
    }
    public void initialGUI(int size, Playground playground) {
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        for (int i = size-10; i < size; i++) {
            constraints.gridx = i+1;
            constraints.gridy = 0;
            char letter = (char) (65+(i-(size-10)));
            JLabel letterLabel = new JLabel(String.valueOf(letter));
            pCenter.add(letterLabel, constraints);
            constraints.gridx = size-10;
            constraints.gridy = i+1 - (size-10);
            JLabel numberLabel = new JLabel(String.valueOf((i-(size-10))+1));
            pCenter.add(numberLabel, constraints);
            for (int j = 0; j < 10; j++) {
                constraints.gridx = i+1;
                constraints.gridy = j+1;
                //Hier wird der Playground in einen Frame eingefügt
                pCenter.add(playground.getPlayground()[i-(size-10)][j],constraints);
            }
        }
        setVisible(true);
    }
}