package RMI;

import Design.*;
import Game.Game;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BattleshipServer extends Remote {
    String game(boolean host) throws RemoteException, NotBoundException;
    Game getGame() throws RemoteException;
    Playground getPlayground(int p) throws RemoteException;
    void sendPlayground(Playground playground, int p) throws RemoteException;
    boolean getHostTurn() throws RemoteException;
    void changeHostTurn() throws  RemoteException;
    boolean getWinner() throws RemoteException;
    void changeWinner() throws RemoteException;
}
