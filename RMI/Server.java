package RMI;

import Design.*;
import Game.Game;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements BattleshipServer {

    private Playground p1 = new Playground();
    private Playground p2 = new Playground();
    private boolean hostTurn = true;

    public Server() throws RemoteException {
        super();
    }
    public static void main(String[] args) throws RemoteException, NotBoundException {
        BattleshipServer server = new Server();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("BattleshipServer", server);
        System.out.println("Server ready");
        server.game(true);
    }
    @Override
    public boolean getHostTurn(){
        return hostTurn;
    }
    @Override
    public void changeHostTurn(){
        hostTurn = !hostTurn;
    }
    @Override
    public String game(boolean host) throws RemoteException {
        Game game = new Game(this);
        game.game(host);
        return "Yes";
    }
    @Override
    public Playground getPlayground(int p) throws RemoteException {
        if(p==1) return p1;
        if(p==2) return p2;
        return null;
    }
    @Override
    public void sendPlayground(Playground playground, int p){
        if(p==1) p1 = playground;
        if(p==2) p2 = playground;
    }
}
