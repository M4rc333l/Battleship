package Game;

import Design.Playground;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
//
public class MainMenu {
    public static void main(String[] args) {

        Game game = new Game();
        game.intialGUI();
        game.game();

    }
}
