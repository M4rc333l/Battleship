package Design;

import RMI.BattleshipServer;
import RMI.Client;
import RMI.Server;

import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainOverlay {

    private final JFrame connectionframe;

    /**
     * Fenster zum hosten oder joinen eines Spiels
     */
    public MainOverlay() {
        connectionframe = new JFrame("Join Game");
        connectionframe.setSize(300,200);
        connectionframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectionframe.setLayout(new java.awt.GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel ipLabel = new JLabel("IP Adress:");
        JLabel portLabel = new JLabel("Port:");

        JTextField ipTextf = new JTextField(" ");
        JTextField portTextf = new JTextField(" ");

        JButton hostButton = new JButton("Host");
        JButton connectionButton = new JButton("Connect");

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(4,4,4,4);
        constraints.gridx = 0;
        constraints.gridy = 0;
        connectionframe.getContentPane().add(ipLabel,constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        connectionframe.getContentPane().add(portLabel,constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        connectionframe.getContentPane().add(hostButton,constraints);
        constraints.gridx = 2;
        constraints.gridy = 4;
        connectionframe.getContentPane().add(connectionButton,constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth=2;
        connectionframe.getContentPane().add(ipTextf,constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth=2;
        connectionframe.getContentPane().add(portTextf,constraints);
        connectionframe.setVisible(true);

        hostButton.addActionListener(e -> {
            try {
                String text = portTextf.getText().replaceAll("\\s+", "");
                BattleshipServer server = new Server();
                Registry registry = LocateRegistry.createRegistry(Integer.parseInt(text));
                registry.rebind("BattleshipServer", server);
                System.out.println("Server ready");
                connectionframe.dispose();
                server.game(true);
            } catch (Exception ex) {
                System.out.println("Verbindung nicht möglich");
            }
        });
        connectionButton.addActionListener(e -> {
            if (!ipTextf.getText().equals("") && !portTextf.getText().equals("")){
                try {
                    String textIP = ipTextf.getText().replaceAll("\\s+", "");
                    String textPort = portTextf.getText().replaceAll("\\s+", "");
                    Client client = new Client(textIP, Integer.parseInt(textPort));
                    connectionframe.dispose();
                    client.game();
                } catch (Exception ex) {
                    System.out.println("Verbindung nicht möglich");
                }
            }
        });
    }
}
