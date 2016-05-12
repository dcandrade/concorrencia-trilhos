package GUI;

import Controller.Facade;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.TrainEngine;

/**
 *
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class MainWindow {

    private final JFrame frame;
    private final Container container;
    private final Facade facade;

    public MainWindow(String title, int trainBlock) throws AlreadyBoundException, IOException, RemoteException, NotBoundException {
        /*
         System.setProperty("javax.net.ssl.debug", "all");
         System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
         System.setProperty("javax.net.ssl.keyStorePassword", "password");
         System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
         System.setProperty("javax.net.ssl.trustStorePassword", "password");
         System.setProperty("javax.net.ssl.trustStoreType","JCEKS");
         */

        this.frame = new JFrame(title);
        this.facade = new Facade(trainBlock);
        this.container = new JPanel(new CardLayout());

        this.container.add(facade.getFrame(), "initialPanel");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this.container);
        frame.setResizable(false);
        setWindowSize(1000, 700);
        setupPanel();

    }

    private void setWindowSize(int width, int height) {
        this.frame.setSize(width, height);
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setLocation((tela.width - width) / 2, (tela.height - height) / 2);
    }

    private void setupPanel() throws RemoteException, NotBoundException {
        ((CardLayout) container.getLayout()).show(container, "initialPanel");

        JButton buttonInitial = new JButton("Iniciar");
        buttonInitial.setBounds(850, 80, 100, 50);
        buttonInitial.setBorder(null);

        buttonInitial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    facade.startMyTrain();
                    ((JButton) ae.getSource()).setEnabled(false);
                } catch (RemoteException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });

        JButton load = new JButton("Carregar Trens");
        load.setBounds(850, 180, 120, 50);
        load.setBorder(null);

        load.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    facade.loadTrains();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.facade.getFrame().add(load);
        this.facade.getFrame().add(buttonInitial);

        this.frame.setVisible(true);

    }

    public static void main(String[] args) throws AlreadyBoundException, IOException, RemoteException, NotBoundException {
        MainWindow mainWindow;
        Properties cfg = new Properties();
        cfg.load(new FileInputStream("data.properties"));
        //mainWindow = new MainWindow("nome", Integer.parseInt(cfg.getProperty("myTrain")));
        for (int i = 1; i <= 3; i++) {
            mainWindow = new MainWindow("nome", i);
        }
    }

}
