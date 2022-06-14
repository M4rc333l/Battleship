package RMI;

import Game.Game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Server Klasse ist auch ein Spieler
 * Die Klasse hat alle Methoden, um die bestimmten Attributen des Spiels zu empfangen und zu senden
 */
public class Server extends UnicastRemoteObject implements BattleshipServer {

    private boolean hostTurn = true;
    private boolean hostCopy = false;
    private boolean clientCopy = false;
    private boolean winner = false;
    private final int[][] posList1 = new int[2][26];
    private final int[][] posList2 = new int[2][26];
    private final List<Integer> xList1 = new ArrayList<>();
    private final List<Integer> yList1 = new ArrayList<>();
    private final List<Integer> xList2 = new ArrayList<>();
    private final List<Integer> yList2 = new ArrayList<>();

    private int current = 0;

    public Server() throws RemoteException {
        super();
    }

    //Getter und Setter
    @Override
    public boolean getHostCopy() {
        return hostCopy;
    }
    @Override
    public boolean getClientCopy() {
        return clientCopy;
    }
    @Override
    public void setHostCopy(){
        hostCopy = true;
    }
    @Override
    public void setClientCopy(){
        clientCopy = true;
    }
    @Override
    public boolean getHostTurn() {
        return hostTurn;
    }
    @Override
    public void changeHostTurn() {
        hostTurn = !hostTurn;
    }
    @Override
    public boolean getWinner() {
        return winner;
    }
    @Override
    public void changeWinner(){
        winner = true;
    }
    @Override
    public void increaseCurrent() {
        current++;
    }
    @Override
    public void resetCurrent() {
        current = 0;
    }
    @Override
    public int getLength1() {
        return xList1.size();
    }
    @Override
    public int getLength2() {
        return xList2.size();
    }

    /**
     * Starten des Spiels
     * @param host Bestimmt, ob der Spieler Host oder Client ist
     */
    @Override
    public void game(boolean host) {
        Game game = new Game(this);
        game.game(host);
    }

    /**
     * @param p 1 = Host und 2 = Client
     * @param pos Position in der Liste
     * @return Gibt die Positionen der Schiffe zurueck
     */
    @Override
    public int getPos(int p, int pos) {
        if(p==1) return posList1[pos][current];
        if(p==2) return posList2[pos][current];
        return -1;
    }

    /**
     * Die Methode speichert die Position in der jeweiligen Schiffliste
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param p 1 = Host und 2 = Client
     */
    @Override
    public void sendPos(int x, int y, int p) {
        if(p==1){
            posList1[0][current] = x;
            posList1[1][current] = y;
        } if(p==2){
            posList2[0][current] = x;
            posList2[1][current] = y;
        }
    }

    /**
     * @param p 1 = Host und 2 = Client
     * @param x true fuer x-Koordinate, false fuer y-Koordinate
     * @param pos Position in der Liste der Schuesse
     * @return Gibt einen Hit vom anderen Spieler zurueck
     */
    @Override
    public int getHit(int p, boolean x, int pos) {
        if(p==1 && x) return xList1.get(pos);
        else if(p==1) return yList1.get(pos);
        else if(p==2 && x) return xList2.get(pos);
        else if(p==2) return yList2.get(pos);
        return -1;
    }

    /**
     * Die Methode speichert einen Hit eines Spielers
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param p 1 = Host und 2 = Client
     */
    @Override
    public void sendHit(int x, int y, int p) {
        if(p==1){
            xList1.add(x);
            yList1.add(y);
        } else if(p==2) {
            xList2.add(x);
            yList2.add(y);
        }
    }
}
