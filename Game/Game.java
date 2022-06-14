package Game;

import Design.*;
import RMI.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

/**
 * In der Game Klasse läuft die ganze Logik des Spiels ab.
 */
public class Game {

    private final BattleshipServer server;

    public Game(BattleshipServer server){
        this.server = server;
    }

    private int turn = 7;
    private boolean hit = false;
    private BattleshipFrame frame;

    //Playground erstellen und Schiffliste erstellen
    private final Playground playground = new Playground();
    private Playground enemyPlayground = new Playground();


    /**
     * Die Game Methode ist der ganze Ablauf des Spiels.
     * @param host
     */
    public void game(boolean host) {

        frame = new BattleshipFrame();

        frame.initialGUI(10, playground);
        frame.initialGUI(21, enemyPlayground);
        enemyPlayground.enabled(false);

        final int[] zaehler = {0};
        final int[] x1 = {0};
        final int[] y1 = {0};
        final int[] x2 = {0};
        final int[] y2 = {0};

        frame.getStartButton().addActionListener(e -> {
            if(frame.getStartButton().isEnabled()) {
                try {
                    if (host && server.getClientCopy()) {
                        getPlayground(2, playground);
                        frame.setText("Client playground auf Server kopiert");
                        turn--;
                        playground.enabled(true);
                        frame.getStartButton().setEnabled(false);
                    }
                    else if (!(host || !server.getHostCopy())) {
                        getPlayground(1, playground);
                        frame.setText("Server playground auf Client kopiert");
                        turn--;
                        playground.enabled(true);
                        frame.getStartButton().setEnabled(false);
                    }
                    else if(host && !server.getClientCopy()) frame.setText("Bitte auf Client warten");
                    else frame.setText("Bitte auf Server warten");
                } catch (RemoteException ex) {
                    lostConnection();
                }
            }
        });
        for (int i = 0; i < playground.getPlayground().length; i++) {
            for (int j = 0; j < playground.getPlayground()[i].length; j++) {
                int finalI = i;
                int finalJ = j;
                playground.getPlayground()[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if(playground.getPlayground()[finalI][finalJ].isEnabled()) {
                            if (turn > -1) {
                                int size = 0;
                                if (turn == 7 || turn == 6) size = 4;
                                if (turn == 5 || turn == 4 || turn == 3) size = 3;
                                if (turn == 2 || turn == 1 || turn == 0) size = 2;

                                if (zaehler[0] == 0) {

                                    x1[0] = finalI;
                                    y1[0] = finalJ;
                                    playground.getPlayground()[finalI][finalJ].setEnabled(false);
                                    playground.changeButtons(finalI, finalJ, size, false);
                                }
                                if (zaehler[0] == 1) {
                                    x2[0] = finalI;
                                    y2[0] = finalJ;
                                }
                                zaehler[0]++;

                                if (zaehler[0] == 2) {
                                    placeShip(x1[0], y1[0], x2[0], y2[0], size);
                                    playground.changeButtons(finalI, finalJ, size, true);
                                    if (turn != 6 && turn != 3) playground.disableNotPlaceable(size);
                                    else playground.disableNotPlaceable(size - 1);
                                    zaehler[0] = 0;
                                    turn--;
                                    if(turn == -1) {
                                        enemyPlayground = enemyPlayground.copyPlayground(playground, 1);
                                        enemyPlayground.enabled(false);
                                        try {
                                            if (host) {
                                                sendPlayground(1);
                                                frame.setText("Server playground kopiert");
                                                server.setHostCopy();
                                            } else {
                                                sendPlayground(2);
                                                frame.setText("Client playground kopiert");
                                                server.setClientCopy();
                                            }
                                        } catch (RemoteException ex) {
                                            lostConnection();
                                        }
                                        frame.getStartButton().setEnabled(true);
                                    }
                                }
                            } else if (turn == -2) {
                                try {
                                    if (host && server.getHostTurn()) {
                                        if(server.getWinner()) {
                                            frame.setText("Du hast leider verloren! Spieler 2 hat gewonnen!");
                                            turn = Integer.MIN_VALUE;
                                        } else {
                                            server.sendHit(finalI, finalJ, 1);
                                            playground.getPlayground()[finalI][finalJ].setEnabled(false);
                                            hit(finalI, finalJ, playground, true);
                                        }
                                        getHits(2, enemyPlayground);
                                        if(hit) hit = false;
                                        else server.changeHostTurn();
                                    } else if(host && !server.getHostTurn()) frame.setText("Bitte auf Client warten");
                                    else if(!(host || server.getHostTurn())) {
                                        if(server.getWinner()) {
                                            frame.setText("Du hast leider verloren! Spieler 1 hat gewonnen");
                                            turn = Integer.MIN_VALUE;
                                        } else {
                                            server.sendHit(finalI, finalJ, 2);
                                            playground.getPlayground()[finalI][finalJ].setEnabled(false);
                                            hit(finalI, finalJ, playground, true);
                                            getHits(1, enemyPlayground);
                                        }
                                        if(hit) hit = false;
                                        else server.changeHostTurn();
                                    } else if(!host && server.getHostTurn()) frame.setText("Bitte auf Server warten");
                                } catch (RemoteException ex) {
                                    lostConnection();
                                }
                                enemyPlayground.enabled(false);
                                playground.enabled((true));
                                if (playground.shipsDestroyed()) {
                                    turn = Integer.MIN_VALUE;
                                    if(host) {
                                        frame.setText("Du hast gewonnen!");
                                        try {
                                            server.changeHostTurn();
                                            server.changeWinner();
                                            getHits(2, enemyPlayground);

                                        } catch (RemoteException ex) {
                                            lostConnection();
                                        }
                                    } else {
                                        frame.setText("Du hast gewonnen!");
                                        try {
                                            server.changeHostTurn();
                                            server.changeWinner();
                                            getHits(1, enemyPlayground);
                                        } catch (RemoteException ex) {
                                            lostConnection();
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * In der placeShip Methode werden die Schiffe platziert un die Schiffe werden in einer Liste gespiechert.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param size
     */
    public void placeShip(int x1, int y1, int x2, int y2, int size) {
        int [][] pos = new int[2][size];
        boolean vertical = y1 - y2 != 0;
        for(int i = 0; i < size; i++) {
            if(!vertical && x2 > x1) {
                pos[0][i] = x1+i;
                pos[1][i] = y1;
                playground.getPlayground()[x1+i][y1].setEnabled(false);
                playground.getPlayground()[x1+i][y1].setBackground(playground.getShipColor());
            }
            if(!vertical && x1 > x2) {
                pos[0][i] = x1-i;
                pos[1][i] = y1;
                playground.getPlayground()[x1-i][y1].setEnabled(false);
                playground.getPlayground()[x1-i][y1].setBackground(playground.getShipColor());
            }
            if(vertical && y2 > y1) {
                pos[0][i] = x1;
                pos[1][i] = y1+i;
                playground.getPlayground()[x1][y1+i].setEnabled(false);
                playground.getPlayground()[x1][y1+i].setBackground(playground.getShipColor());
            }
            if(vertical && y1 > y2) {
                pos[0][i] = x1;
                pos[1][i] = y1-i;
                playground.getPlayground()[x1][y1-i].setEnabled(false);
                playground.getPlayground()[x1][y1-i].setBackground(playground.getShipColor());
            }
        }
        playground.getShipList().add(new Ship(size,pos));
    }

    /**
     * Die Methode shipDestroyed bestimmt ob ein Schiff zerstört wurde.
     * @param playground
     */
    public void shipDestroyed(Playground playground) {
        for (int i = 0; i < playground.getShipList().size(); i++) {
            boolean destroyed = true;
            for (int j = 0; j < playground.getShipList().get(i).getSize(); j++) {
                if (!playground.getPlayground()[playground.getShipList().get(i).getPos()[0][j]][playground.getShipList().get(i).getPos()[1][j]].getBackground().equals(Color.ORANGE)){
                    destroyed = false;
                }
            }
            if(destroyed) playground.getShipList().get(i).setDestroyed();
            if (playground.getShipList().get(i).isDestroyed()){
                for (int j = 0; j < playground.getShipList().get(i).getSize(); j++) {
                    playground.getPlayground()[playground.getShipList().get(i).getPos()[0][j]][playground.getShipList().get(i).getPos()[1][j]].setBackground(Color.RED);
                }
            }
        }
        for (int i = 0; i < playground.getPlayground().length; i++) {
            for (int j = 0; j < playground.getPlayground()[i].length; j++) {
                if(playground.hasNeighbor(i,j,Color.RED) && !playground.getPlayground()[i][j].getBackground().equals(Color.RED)){
                    playground.getPlayground()[i][j].setEnabled(false);
                    playground.getPlayground()[i][j].setBackground(Color.GRAY);
                }
            }
        }
    }

    /**
     * Die Methode hit wird ein Treffer festgestellt.
     * @param x
     * @param y
     * @param playground
     * @param disable
     */
    public void hit(int x, int y, Playground playground, boolean disable) {
        for (Ship ship : playground.getShipList()) {
            for (int j = 0; j < ship.getSize(); j++) {
                if (ship.getPos()[0][j] == x && ship.getPos()[1][j] == y) {
                    playground.getPlayground()[ship.getPos()[0][j]][ship.getPos()[1][j]].setBackground(Color.ORANGE);
                    if(disable) hit = true;
                    shipDestroyed(playground);
                    return;
                }
            }
        }
        playground.getPlayground()[x][y].setBackground(Color.WHITE);
    }

    /**
     * Die Methode sendet einen Playground zu einem anderen Spieler
     * @param p
     * @throws RemoteException
     */
    public void sendPlayground(int p) throws RemoteException {
        for(int i = 0; i<enemyPlayground.getShipList().size();i++){
            for(int j = 0; j<enemyPlayground.getShipList().get(i).getSize();j++){
                server.sendPos(enemyPlayground.getShipList().get(i).getPos()[0][j], enemyPlayground.getShipList().get(i).getPos()[1][j], p);
                server.increaseCurrent();
            }
        }
        server.resetCurrent();
    }

    /**
     * Die Methode erhält einen Playground zu einem anderen Spieler
     * @param p
     * @param playground
     * @throws RemoteException
     */
    public void getPlayground(int p, Playground playground) throws RemoteException {
        for(int i = 0; i<playground.getShipList().size();i++){
            for(int j = 0; j<playground.getShipList().get(i).getSize();j++){
                playground.getShipList().get(i).getPos()[0][j] = server.getPos(p, 0);
                playground.getShipList().get(i).getPos()[1][j] = server.getPos(p, 1);
                server.increaseCurrent();
            }
        }
        playground.clear(false);
        server.resetCurrent();
    }

    /**
     * Ein Spieler erhält vom anderen Spieler den Hit
     * @param p
     * @param playground
     * @throws RemoteException
     */
    public void getHits(int p, Playground playground) throws RemoteException {
        int count = -1;
        if(p==1) count = server.getLength1();
        if(p==2) count = server.getLength2();
        for(int i =0;i<count;i++){
            hit(server.getHit(p, true, i), server.getHit(p, false, i), playground, false);
        }
    }

    /**
     * Ein ErrorMessage, falls keine Verbindung mehr exestiert.
     */
    public void lostConnection() {
        try {
            frame.setText("Verbindung zum Server verloren");
            Thread.sleep(5000);
            frame.dispose();
        } catch(Exception e){
            frame.setText("Verbindung verloren");
        }
    }
}