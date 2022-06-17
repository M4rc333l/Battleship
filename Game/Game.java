package Game;

import Design.*;
import RMI.*;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

/**
 * In der Game Klasse laeuft die Logik des eigentlichen Spiels ab
 */
public class Game {

    private final BattleshipServer server;

    public Game(BattleshipServer server){
        this.server = server;
    }
    //Spielphase
    private int turn = 7;
    //True, wenn ein Spieler ein Schiff getroffen hat
    private boolean hit = false;
    private BattleshipFrame frame;

    private final Playground playground = new Playground();
    private Playground enemyPlayground = new Playground();

    /**
     * Die Game Methode ist der ganze Ablauf des Spiels
     * Es werden erst alle Schiffe platziert
     * Dann muessen beide Spieler auf den Startbutton klicken, um den gegnerischen Playground abzurufen
     * Dann faengt der Host an zu schießen,
     * Das Spiel endet, sobald ein Spieler alle Schiffe getroffen hat
     * @param host true beim Server, false beim Client
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

        frame.getExitButton().addActionListener(e -> {
            if(frame.getExitButton().isEnabled()) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        frame.getStartButton().addActionListener(e -> {
            if(frame.getStartButton().isEnabled()) {
                try {
                    if (host && server.getClientCopy()) {
                        getPlayground(2, playground);
                        frame.setText("Gegnerischen Playground empfangen! Du darfst anfangen!");
                        turn--;
                        playground.enabled(true);
                        frame.getStartButton().setEnabled(false);
                    }
                    else if (!(host || !server.getHostCopy())) {
                        getPlayground(1, playground);
                        frame.setText("Gegnerischen Playground empfangen!");
                        turn--;
                        playground.enabled(true);
                        frame.getStartButton().setEnabled(false);
                    }
                    else if(host && !server.getClientCopy()) frame.setText("Bitte warte bis Spieler 2 alle Schiffe platziert hat!");
                    else frame.setText("Bitte warte bis Spieler 1 alle Schiffe platziert hat!");
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
                                        enemyPlayground = enemyPlayground.copyPlayground(playground);
                                        enemyPlayground.enabled(false);
                                        try {
                                            if (host) {
                                                sendPlayground(1);
                                                frame.setText("Playground an Gegner gesendet!");
                                                server.setHostCopy();
                                            } else {
                                                sendPlayground(2);
                                                frame.setText("Playground an Gegner gesendet!");
                                                server.setClientCopy();
                                            }
                                        } catch (RemoteException ex) {
                                            lostConnection();
                                        }
                                        playground.enabled(false);
                                        frame.getStartButton().setEnabled(true);
                                    }
                                }
                            } else if (turn == -2) {
                                try {
                                    if (host && server.getHostTurn()) {
                                        if(server.getWinner()) {
                                            frame.setText("Du hast leider verloren! Spieler 2 hat gewonnen!");
                                            turn = Integer.MIN_VALUE;
                                            getHits(2, enemyPlayground);
                                            frame.getExitButton().setEnabled(true);
                                        } else {
                                            server.sendHit(finalI, finalJ, 1);
                                            playground.getPlayground()[finalI][finalJ].setEnabled(false);
                                            hit(finalI, finalJ, playground, true);
                                        }
                                        getHits(2, enemyPlayground);
                                        if(hit) hit = false;
                                        else server.changeHostTurn();
                                    } else if(host && !server.getHostTurn()) frame.setText("Du bist nicht dran! Bitte auf Spieler 2 warten!");
                                    else if(!(host || server.getHostTurn())) {
                                        if(server.getWinner()) {
                                            frame.setText("Du hast leider verloren! Spieler 1 hat gewonnen!");
                                            turn = Integer.MIN_VALUE;
                                            getHits(1, enemyPlayground);
                                            frame.getExitButton().setEnabled(true);
                                        } else {
                                            server.sendHit(finalI, finalJ, 2);
                                            playground.getPlayground()[finalI][finalJ].setEnabled(false);
                                            hit(finalI, finalJ, playground, true);
                                            getHits(1, enemyPlayground);
                                        }
                                        if(hit) hit = false;
                                        else server.changeHostTurn();
                                    } else if(!host && server.getHostTurn()) frame.setText("Du bist nicht dran! Bitte auf Spieler 1 warten!");
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
                                    frame.getExitButton().setEnabled(true);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Die Schiffe werden platziert und in einer Liste gespeichert
     * @param x1 x-Startpunkt
     * @param y1 y-Startpunkt
     * @param x2 x-Endpunkt
     * @param y2 x-Endpunkt
     * @param size Groese des Schiffes
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
     * Bestimmt, ob ein Schiff zerstoert wurde
     * @param playground Playground fuer welchen dies ueberprueft werden soll
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
     * Setzt einen Schuss in einen Playground
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param playground Playground in den geschossen wird
     * @param disable true, wenn die globale Variable hit aktualisiert werden soll bei einem Treffer
     *                false, wenn sie nicht aktualisiert werden soll (Beim Schießen in den zweiten Playground)
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
     * Sendet alle Positionen der Schiffe zum Server
     * @param p 1 = Host und 2 = Client
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
     * Ruft die Positionen der gegnerischen Schiffe vom Server ab
     * @param p 1 = Host und 2 = Client
     * @param playground Playground in welchen die gegnerischen Schiffe gesetzt werden sollen
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
     * Ruft alle bisherigen Schuesse des Gegners ab
     * @param p 1 = Host und 2 = Client
     * @param playground Playground in welchen die Schuesse gesetzt werden
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
     * ErrorMessage, falls keine Verbindung mehr existiert
     */
    public void lostConnection() {
        System.out.println("Verbindung verloren!");
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}