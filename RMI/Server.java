package RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements BattleshipServer{

    protected Server() throws RemoteException {
        super();
    }
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        BattleshipServer server = new Server();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("Server", server);
        System.out.println("Server ready");
    }
    @Override
    public String method() throws RemoteException {
        return null;
    }
}
