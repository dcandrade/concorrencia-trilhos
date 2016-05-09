/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.TreeMap;
import model.Server;
import model.Train;
import model.TrainWatcher;

import util.ITrain;

/**
 *
 * @author dcandrade
 */
public class Controller {

    private static final int PORT = 1234;
    public static final int NUM_TRAINS = 3;

    private final ITrain myTrain;
    private final TreeMap<Integer, ITrain> trains;

    public Controller(int trainBlock) throws AlreadyBoundException, FileNotFoundException, IOException {
        this.trains = new TreeMap<>();
        this.myTrain = new Train(trainBlock);
        this.trains.put(myTrain.getBlock(), myTrain);
        Server server = new Server(myTrain, PORT+trainBlock);
        server.start();
        //myTrain.start();
        //loadTrains();
    }

    private ITrain addTrain(String hostname, int key) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(Controller.PORT+key);
            //Registry registry = LocateRegistry.getRegistry("localhost", Controller.PORT+key);
            ITrain train = (ITrain) registry.lookup(hostname);
            this.trains.put(train.getBlock(), train);
            return train;
        } catch (NotBoundException | AccessException ex) {
            System.err.println("Error while adding trains");
            System.err.println(ex.getMessage());
            return null;
        }
    }

    public void loadTrains() throws FileNotFoundException, IOException {
        String hostname;
        List<ITrain> otherTrains = new ArrayList<>();
        
        for (int i = 1; i <= Controller.NUM_TRAINS; i++) {
            if (i != this.myTrain.getBlock()) {
                hostname = "Train" + i;
                ITrain train = this.addTrain(hostname, i);

                if (train == null) {
                    throw new NoSuchObjectException("Train #" + i + " not found");
                }else{
                    otherTrains.add(train);
                    System.out.println("Train #"+i+" was sucessfully loaded");
                }
            }
        }
        
        TrainWatcher tw = new TrainWatcher(otherTrains, myTrain);
        tw.start();
    }

    public void startMyTrain() throws RemoteException {
        this.myTrain.start();
    }

    public void setSpeed(int block, int speed) throws RemoteException {
        this.trains.get(block).setSpeed(speed);
    }

    public Iterator<ITrain> getTrains() {
        List<ITrain> list = new ArrayList<>();

        for (Integer x : this.trains.keySet()) {
            list.add(this.trains.get(x));
        }

        return list.iterator();
    }

}
