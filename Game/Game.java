package Game;

import Design.*;
import RMI.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

public class Game {

    private final Server server;

    public Game(Server server){
        this.server = server;
    }

    private int turn = 7;
    private final BattleshipFrame frame = new BattleshipFrame();

    //Playground erstellen und Schiffliste erstellen
    private Playground playground = new Playground();
    private Playground enemyPlayground = new Playground();

    public void game(boolean host) {

        frame.initialGUI(10, playground);
        frame.initialGUI(21, enemyPlayground);
        enemyPlayground.enabled(false);

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
                                        enemyPlayground = enemyPlayground.copyPlayground(playground, false);
                                        //frame.intialGUI(21, enemyPlayground);
                                    }
                                }
                            } else if (turn == -1) {
                                if (host && server.getHostTurn()) {
                                    server.sendPlayground(enemyPlayground, 1);
                                    server.changeHostTurn();
                                    turn--;
                                    System.out.println("Server playground kopiert");
                                } else if(!(host || server.getHostTurn())) {
                                    server.sendPlayground(enemyPlayground, 2);
                                    server.changeHostTurn();
                                    turn--;
                                    System.out.println("Client playground kopiert");
                                } else if(!host && server.getHostTurn()) System.out.println("Bitte auf Server warten");
                            } else if (turn == -2) {
                                if (host && server.getHostTurn()) {
                                    try {
                                        playground = playground.copyPlayground(server.getPlayground(2), false);
                                        server.changeHostTurn();
                                        System.out.println("Client playground auf Server kopiert");
                                    } catch (RemoteException ex) {
                                        ex.printStackTrace();
                                    }
                                    turn--;
                                } else if(host && !server.getHostTurn()) System.out.println("Bitte auf Client warten");
                                else if (!(host || server.getHostTurn())) {
                                    try {
                                        playground = playground.copyPlayground(server.getPlayground(1), false);
                                        server.changeHostTurn();
                                        System.out.println("Server playground auf Client kopiert");
                                    } catch (RemoteException ex) {
                                        ex.printStackTrace();
                                    }
                                    turn--;
                                } else if(!host && server.getHostTurn()) System.out.println("Bitte auf Server warten");
                                playground.enabled(true);
                            } else if (turn == -3) {
                                if (host && server.getHostTurn()) {
                                    server.sendPlayground(playground, 2);
                                    try {
                                        enemyPlayground = enemyPlayground.copyPlayground(server.getPlayground(1), true);
                                    } catch (RemoteException ex) {
                                        ex.printStackTrace();
                                    }
                                    server.changeHostTurn();
                                    playground.getPlayground()[finalI][finalJ].setEnabled(false);
                                    hit(finalI, finalJ, playground);
                                } else if(host && !server.getHostTurn()) System.out.println("Bitte auf Client warten");
                                else if(!(host || server.getHostTurn())) {
                                    server.sendPlayground(playground, 1);
                                    try {
                                        enemyPlayground = enemyPlayground.copyPlayground(server.getPlayground(2), true);
                                    } catch (RemoteException ex) {
                                        ex.printStackTrace();
                                    }
                                    server.changeHostTurn();
                                    playground.getPlayground()[finalI][finalJ].setEnabled(false);
                                    hit(finalI, finalJ, playground);
                                } else if(!host && server.getHostTurn()) System.out.println("Bitte auf Server warten");

                                enemyPlayground.enabled(false);
                                playground.enabled((true));

                                if (playground.getShipList().isEmpty()) {
                                    turn = -1000;
                                    if(host) System.out.println("HOST GEWONNEN");
                                    if(!host) System.out.println("CLIENT GEWONNEN");
                                }
                            }
                        }
                    }
                });
            }
        }
    }
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
    public void shipDestroyed() {
        for (int i = 0; i < playground.getShipList().size(); i++) {
            boolean destroyed = true;
            for (int j = 0; j < playground.getShipList().get(i).getSize(); j++) {
                if (!playground.getPlayground()[playground.getShipList().get(i).getPos()[0][j]][playground.getShipList().get(i).getPos()[1][j]].getBackground().equals(Color.ORANGE)){
                    destroyed = false;
                }
            }
            if (destroyed){
                for (int j = 0; j < playground.getShipList().get(i).getSize(); j++) {
                    playground.getPlayground()[playground.getShipList().get(i).getPos()[0][j]][playground.getShipList().get(i).getPos()[1][j]].setBackground(Color.RED);
                }
                playground.getShipList().remove(i);
            }
        }
        for (int i = 0; i < playground.getPlayground().length; i++) {
            for (int j = 0; j < playground.getPlayground()[i].length; j++) {
                if(playground.hasNeighbor(i,j,Color.RED) && !playground.getPlayground()[i][j].getBackground().equals(Color.RED)){
                        playground.getPlayground()[i][j].setEnabled(false);
                        playground.getPlayground()[i][j].setBackground(Color.YELLOW);
                }
            }
        }
    }
    public void hit(int x, int y, Playground playground) {
        for (Ship ship : playground.getShipList()) {
            for (int j = 0; j < ship.getSize(); j++) {
                if (ship.getPos()[0][j] == x && ship.getPos()[1][j] == y) {
                    playground.getPlayground()[ship.getPos()[0][j]][ship.getPos()[1][j]].setBackground(Color.ORANGE);
                    shipDestroyed();
                    return;
                }
            }
        }
        playground.getPlayground()[x][y].setBackground(Color.WHITE);
    }
}