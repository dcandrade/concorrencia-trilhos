/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import model.Server;
import model.Train;

/**
 *
 * @author dcandrade
 */
public class Controller {

    private static final int PORT = 3333;
    private static final int NUM_TRAINS = 3;
    private final Server server;
    private final Train myTrain;
    private final TreeMap<Integer, Train> trains;

    public Controller(int trainBlock) throws RemoteException, MalformedURLException, AlreadyBoundException {
        this.trains = new TreeMap<>();
        this.myTrain = new Train(trainBlock);
        this.server = new Server(this.myTrain, PORT);
        this.trains.put(trainBlock, myTrain);
    }

    private boolean addTrain(String hostname) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(PORT);
            Train train = (Train) registry.lookup(hostname);
            this.trains.put(train.getBlock(), train);
            
            return true;
        } catch (NotBoundException | AccessException ex) {
            return false;
        }
    }
    
    
    private void loadTrains() throws FileNotFoundException, IOException{
        String hostname;
        
        for (int i = 0; i < Controller.NUM_TRAINS; i++) {
            if(i != this.myTrain.getBlock()){
                hostname = "Trem"+i;
                boolean addTrain = this.addTrain(hostname);
                
                if(!addTrain){
                    throw new NoSuchObjectException("Train #"+i+" not found");
                }
            }
        }
    }
    
    public void setSpeed(int block, int speed) throws RemoteException{
        this.trains.get(block).setSpeed(speed);
    }
}
