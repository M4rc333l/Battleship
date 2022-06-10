package Design;

import RMI.BattleshipServer;
import RMI.Client;
import RMI.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainOverlay {
    private String ip;
    private String port;
    private JFrame connectionframe;

    public MainOverlay(){
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

        hostButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BattleshipServer server = null;
                try {
                    server = new Server();
                    Registry registry = LocateRegistry.createRegistry(Integer.parseInt(portTextf.getText()));
                    registry.rebind("BattleshipServer", server);
                    System.out.println("Server ready");
                    server.game(true);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        connectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!ipTextf.equals("") && !portTextf.equals("")){
                    try {
                        Client client = new Client(ipTextf.getText(), Integer.parseInt(portTextf.getText()));
                        System.out.println(client.method());

                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    } catch (NotBoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }




    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
}
