package RMI;

import Game.Game;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements BattleshipServer{

    public Server() throws RemoteException {
        super();
    }
    public static void main(String[] args) throws RemoteException {
        BattleshipServer server = new Server();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("BattleshipServer", server);
        System.out.println("Server ready");
    }
    @Override
    public String method() throws RemoteException {
        Game game = new Game("Server");
        game.game();
        return "Yes";
    }
    public void spielstarten() throws RemoteException{
        System.out.println("1234");
    }
    public int zahl() throws RemoteException{
        return 15;
    }
}
