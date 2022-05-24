package Game;

import RMI.BattleshipServer;
import RMI.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//
public class MainMenu {

    public static void main(String[] args) throws RemoteException {
        Game game = new Game();
        game.game();
    }
}
