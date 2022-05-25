package RMI;

import Design.*;
import Game.Game;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements BattleshipServer{

    private Playground p1 = new Playground();
    private Playground p2 = new Playground();
    public Server() throws RemoteException {
        super();
    }
    public static void main(String[] args) throws RemoteException, NotBoundException {
        BattleshipServer server = new Server();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("BattleshipServer", server);
        System.out.println("Server ready");
        server.method(true);
    }
    @Override
    public String method(boolean host) throws RemoteException {
        Game game = new Game(host);
        game.game();
        return "Yes";
    }
    public void start() throws RemoteException {

    }
    public int zahl() throws RemoteException{
        return 15;
    }

    @Override
    public Playground getPlayground() throws RemoteException {
        return p1;
    }
    @Override
    public void sendPlayground(Playground playground){
        p1 = playground;
    }
}
