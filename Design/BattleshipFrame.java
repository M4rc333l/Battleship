package Design;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.BorderLayout.CENTER;

public class BattleshipFrame extends JFrame {

    private final GridBagConstraints constraints = new GridBagConstraints();
    private final Panel pCenter = new Panel();
    private final BackGroundPane pCenterImage = new BackGroundPane("BackgroundImage.jpg");
    private final JTextArea chat = new JTextArea();
    private final JButton startButton = new JButton("Start");
    private final ArrayList<String> chatList = new ArrayList<>();
    private final JLabel timer = new JLabel();

    public BattleshipFrame() {
        super();
        setSize(2000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        //setContentPane(new BackGroundPane("BackgroundImage.jpg"));
        Panel pWest = new Panel();
        Panel pEast = new Panel();
        Panel pNorth = new Panel();
        Panel pSouth = new Panel();
        add(pNorth, BorderLayout.NORTH);
        pNorth.add(new JLabel("Battleship",0));
        //pNorth.setPreferredSize(new Dimension(100,300));
        pEast.setPreferredSize(new Dimension(75,30));
        //pWest.setPreferredSize(new Dimension(300,30));
        //pCenterImage.setPreferredSize(new Dimension(300,300));
        pEast.add(timer, 0);
        add(pEast, BorderLayout.EAST);
        pWest.add(startButton, 0);
        //pWest.add(timer, 1);
        chat.setDisabledTextColor(Color.BLACK);
        chat.setPreferredSize(new Dimension(1000, 200));
        chat.setEnabled(false);
        pSouth.add(chat, 0);
        pCenter.setLayout(new GridBagLayout());
        //add(pCenterImage, CENTER);
        add(pCenter, CENTER);
        add(pSouth, BorderLayout.SOUTH);
        add(pWest, BorderLayout.WEST);
        startButton.setEnabled(false);
        startTimer();
        timer.setBackground(Color.WHITE);
        setVisible(true);
    }
    public void setText(String text){
        chatList.add(text);
        String result = "";
        for (String s : chatList) {
            result = result + "\n" + s;
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
            pCenterImage.add(letterLabel, constraints);
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
    public JButton getStartButton(){
        return startButton;
    }
    public void startTimer() {
        Thread t1 = new Thread() {
            int hour = 0;
            int minute = 0;
            int second = 0;
            public void run() {
                try {
                    while(true) {
                        if(second == 59) {
                            second = 0;
                            minute++;
                        }
                        else if(minute == 59) {
                            minute = 0;
                            hour++;
                        }
                        else second++;
                        String sHour = "" + hour;
                        String sMinute = "" + minute;
                        String sSecond = "" + second;
                        if(hour < 10) sHour = "0" + hour;
                        if(minute < 10) sMinute = "0" + minute;
                        if(second < 10) sSecond = "0" + second;
                        timer.setText(sHour + ":" + sMinute + ":" + sSecond);
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };t1.start();
    }
    private class BackGroundPane extends JPanel {
        Image img = null;
        BackGroundPane(String imagefile) {
            if (imagefile != null) {
                MediaTracker mt = new MediaTracker(this);
                img = Toolkit.getDefaultToolkit().getImage(imagefile);
                mt.addImage(img, 0);
                try {
                    mt.waitForAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img,0,0,this.getWidth(),this.getHeight(),this);
        }
    }
}