/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controller.Facade;
import java.awt.BorderLayout;
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
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Point;
import util.ITrain;

/**
 *
 * @author solenir
 */
public class MainWindow {

    private final JFrame frame;
    private final Container container;
    private final Facade facade;
    private final Client client;

    public MainWindow(String title, int trainBlock) throws AlreadyBoundException, IOException, RemoteException, NotBoundException {
        this.frame = new JFrame(title);
        this.facade = new Facade(trainBlock);
        this.client = new Client();
        this.container = new JPanel(new CardLayout());

        this.container.add(client.getRail(), "initialPanel");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this.container);
        frame.setResizable(false);
        setWindowSize(1000, 700);
        frame.setVisible(true);
        setupPanel();

    }

    private void setWindowSize(int width, int height) {
        this.frame.setSize(width, height);
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setLocation((tela.width - width) / 2, (tela.height - height) / 2);
    }

    private void setupPanel() throws RemoteException, NotBoundException {
       ((CardLayout) container.getLayout()).show(container, "initialPanel");
        this.client.repaintRail();

        Iterator<ITrain> trains = this.facade.getTrains();

        while (trains.hasNext()) {
           this.client.addTrain(trains.next());
        }

        JButton buttonInitial = new JButton("Iniciar");
        buttonInitial.setBounds(850, 80, 100, 50);
        buttonInitial.setBorder(null);
 
        buttonInitial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    facade.startMyTrain();
                } catch (RemoteException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        this.client.getSliderFrame().add(buttonInitial);
        this.container.add(this.client.getSliderFrame());
        this.container.add(buttonInitial, BorderLayout.WEST);

        this.client.start();

    }

    
    public static void main(String[] args) throws AlreadyBoundException, IOException, RemoteException, NotBoundException {
        MainWindow mainWindow = new MainWindow("nome", Point.UPPER_BLOCK);
    }

}
