package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private BattleshipServer server;

    public Client() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("26.197.80.44",1099);
        server = (BattleshipServer) registry.lookup("BattleshipServer");
    }
    public String method() throws RemoteException {
        String result = server.method();
        return result;
    }
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Client client = new Client();
        System.out.println(client.method());
    }
}
