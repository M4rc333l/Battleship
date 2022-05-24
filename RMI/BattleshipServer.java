package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BattleshipServer extends Remote {
    String method() throws RemoteException;
    void spielstarten() throws RemoteException;
    int zahl() throws  RemoteException;
}
