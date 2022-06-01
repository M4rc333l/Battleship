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
    public String method() throws RemoteException, NotBoundException {
        return server.game(false);
    }
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Client client = new Client("26.197.80.44", 1099);
        System.out.println(client.method());
    }
}
