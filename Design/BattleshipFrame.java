package Design;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.BorderLayout.CENTER;

public class BattleshipFrame extends JFrame {

    private final GridBagConstraints constraints = new GridBagConstraints();
    private final JPanel pCenter = new JPanel();
    private final JTextArea chat = new JTextArea();
    private final JButton startButton = new JButton("Start");
    private final ArrayList<String> chatList = new ArrayList<>();
    private final JLabel timer = new JLabel();

    public BattleshipFrame() {
        super();
        setSize(2000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel pNorth = new JPanel();
        JPanel pSouth = new JPanel();
        JPanel pWest = new JPanel();
        JPanel pEast = new JPanel();
        pNorth.setBackground(new Color(200, 255, 255));
        pSouth.setBackground(new Color(200, 255, 255));
        pWest.setBackground(new Color(200, 255, 255));
        pEast.setBackground(new Color(200, 255, 255));
        pCenter.setBackground(new Color(200, 255, 255));
        pEast.setPreferredSize(new Dimension(75, 30));
        pNorth.setBounds(50, 50, 800, 250);
        pEast.add(timer, 0);
        pWest.add(startButton, 0);
        chat.setDisabledTextColor(Color.BLACK);
        chat.setPreferredSize(new Dimension(1000, 200));
        chat.setEnabled(false);
        pSouth.add(chat, 0);
        pCenter.setLayout(new GridBagLayout());
        add(pCenter, CENTER);
        add(pNorth, BorderLayout.NORTH);
        add(pSouth, BorderLayout.SOUTH);
        add(pWest, BorderLayout.WEST);
        add(pEast, BorderLayout.EAST);
        try {
            File shipImage = new File("ShipImage.png");
            pNorth.add(new ImageLabel(shipImage).getPic());
        } catch (Exception e) {
            System.out.println("Bild konnte nicht geladen werden");
        }
        startButton.setEnabled(false);
        startTimer();
        setVisible(true);
    }

    public void setText(String text) {
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
        for (int i = size - 10; i < size; i++) {
            constraints.gridx = i + 1;
            constraints.gridy = 0;
            char letter = (char) (65 + (i - (size - 10)));
            JLabel letterLabel = new JLabel(String.valueOf(letter));
            pCenter.add(letterLabel, constraints);
            constraints.gridx = size - 10;
            constraints.gridy = i + 1 - (size - 10);
            JLabel numberLabel = new JLabel(String.valueOf((i - (size - 10)) + 1));
            pCenter.add(numberLabel, constraints);
            for (int j = 0; j < 10; j++) {
                constraints.gridx = i + 1;
                constraints.gridy = j + 1;
                //Hier wird der Playground in einen Frame eingefügt
                pCenter.add(playground.getPlayground()[i - (size - 10)][j], constraints);
            }
        }
        setVisible(true);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public void startTimer() {
        Thread t1 = new Thread() {
            int hour = 0;
            int minute = 0;
            int second = 0;

            public void run() {
                try {
                    while (true) {
                        if (hour == 59 && minute == 59 && second == 59) Thread.interrupted();
                        else if (minute == 59 && second == 59) {
                            minute = 0;
                            second = 0;
                            hour++;
                        } else if (second == 59) {
                            second = 0;
                            minute++;
                        } else second++;
                        String sHour = "" + hour;
                        String sMinute = "" + minute;
                        String sSecond = "" + second;
                        if (hour < 10) sHour = "0" + hour;
                        if (minute < 10) sMinute = "0" + minute;
                        if (second < 10) sSecond = "0" + second;
                        timer.setText(sHour + ":" + sMinute + ":" + sSecond);
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
    }

    private static class ImageLabel extends JLabel {

        private final JLabel pic;

        public ImageLabel(File file) throws IOException {
            super();
            BufferedImage image = ImageIO.read(file);
            pic = new JLabel(new ImageIcon(image));
        }
        public JLabel getPic(){
            return pic;
        }
    }
}
