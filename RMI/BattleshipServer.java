package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BattleshipServer extends Remote {
    void game(boolean host) throws Exception;
    int getPos(int p, int pos) throws RemoteException;
    void sendPos(int x, int y, int p) throws RemoteException;
    void increaseCurrent() throws RemoteException;
    void resetCurrent() throws RemoteException;
    int getLength1() throws RemoteException;
    int getLength2() throws RemoteException;
    boolean getHostTurn() throws RemoteException;
    void changeHostTurn() throws RemoteException;
    boolean getWinner() throws RemoteException;
    void changeWinner() throws RemoteException;
    int getHit(int p, boolean x, int pos) throws RemoteException;
    void sendHit(int x, int y, int p) throws RemoteException;
    boolean getHostCopy() throws RemoteException;
    boolean getClientCopy() throws RemoteException;
    void setHostCopy() throws RemoteException;
    void setClientCopy() throws RemoteException;
}
