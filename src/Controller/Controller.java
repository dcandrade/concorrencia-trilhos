/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Server;
import model.Train;

/**
 *
 * @author dcandrade
 */
public class Controller {

    private static final String HOSTNAME = "hostname";
    private static final int PORT = 3333;
    private final Server server;
    private final List<Train> trains;

    public Controller(int trainBlock) throws RemoteException, MalformedURLException {
        this.trains = new ArrayList<>();
        Train train = new Train(trainBlock);
        this.server = new Server(train, HOSTNAME, PORT);
        this.trains.add(train);
    }

    private boolean addTrain(String hostname) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(PORT);
            Train train = (Train) registry.lookup(hostname);
            this.trains.add(train);
            
            return true;
        } catch (NotBoundException | AccessException ex) {
            return false;
        }
    }
}
