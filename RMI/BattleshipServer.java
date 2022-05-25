package RMI;

import Design.*;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BattleshipServer extends Remote {
    String method(boolean host) throws RemoteException, NotBoundException;
    void start() throws RemoteException, NotBoundException;
    int zahl() throws  RemoteException;
    Playground getPlayground() throws RemoteException;
    void sendPlayground(Playground playground) throws RemoteException;
}
