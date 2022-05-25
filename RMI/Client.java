package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private final BattleshipServer server;

    public Client(String ip, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(ip,port);
        server = (BattleshipServer) registry.lookup("BattleshipServer");
    }
    public String method() throws RemoteException {
        return server.method();
    }
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Client client = new Client("localhost", 1099);
        System.out.println(client.method());
    }
}
