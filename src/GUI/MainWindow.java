package GUI;

import Controller.Facade;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.TrainEngine;
import util.ITrain;

/**
 *
 * @author Daniel Andrade 
 * @author Solenir Figuerêdo
 */
public class MainWindow {

    private final JFrame frame;
    private final Container container;
    private final Facade facade;
    private final Client client;

    public MainWindow(String title, int trainBlock) throws AlreadyBoundException, IOException, RemoteException, NotBoundException {
        this.frame = new JFrame(title);
        this.facade = new Facade(trainBlock);
        this.client = new Client(trainBlock);
        this.container = new JPanel(new CardLayout());

        this.container.add(client.getSliderFrame(), "initialPanel");

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

    private void loadTrains() throws RemoteException, IOException {
        this.facade.loadTrains();
        Iterator<ITrain> trains = this.facade.getTrains();

        while (trains.hasNext()) {
            this.client.addTrain(trains.next());
        }

        this.client.repaintRail();
    }

    private void setupPanel() throws RemoteException, NotBoundException {
        ((CardLayout) container.getLayout()).show(container, "initialPanel");
        this.client.repaintRail();

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
                    loadTrains();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.client.getSliderFrame().add(load);
        this.client.getSliderFrame().add(buttonInitial);

        this.client.start();
        this.frame.setVisible(true);

    }

    public static void main(String[] args) throws AlreadyBoundException, IOException, RemoteException, NotBoundException {
        MainWindow mainWindow;
        mainWindow = new MainWindow("nome", TrainEngine.UPPER_BLOCK);
        mainWindow = new MainWindow("nome", TrainEngine.DOWN_RIGHT_BLOCK);
        mainWindow = new MainWindow("nome", TrainEngine.DOWN_LEFT_BLOCK);
    }

}
