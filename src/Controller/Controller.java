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

import java.util.TreeMap;

import model.Train;
import util.ITrain;

/**
 *
 * @author dcandrade
 */
public class Controller {

    private  int PORT = 3333;
    private static final int NUM_TRAINS = 3;
   
    private final Train myTrain;
    private final TreeMap<Integer, ITrain> trains;

    public Controller(Train myTrain) throws AlreadyBoundException, FileNotFoundException, IOException {
        this.trains = new TreeMap<>();
        this.myTrain = myTrain;
        loadTrains();
    }
   

    private boolean addTrain(String hostname, int key) throws RemoteException {
        try {
        	if (key == 1){
	            Registry registry = LocateRegistry.getRegistry(3333);
	            ITrain train = (ITrain) registry.lookup(hostname);
	            this.trains.put(train.getBlock(), train);
	            return true;
        	}
        	else if (key == 2){
    	            Registry registry = LocateRegistry.getRegistry(3334);
    	            ITrain train = (ITrain) registry.lookup(hostname);
    	            this.trains.put(train.getBlock(), train);
    	            return true;
            	}
        	else if (key == 3){
	            Registry registry = LocateRegistry.getRegistry(3335);
	            ITrain train = (ITrain) registry.lookup(hostname);
	            this.trains.put(train.getBlock(), train);
	            return true;
        	}
        	return false;
        } catch (NotBoundException | AccessException ex) {
            return false;
        }
    }
    
    private void loadTrains() throws FileNotFoundException, IOException{
        String hostname;
        
        for (int i = 1; i < Controller.NUM_TRAINS; i++) {
            if(i != this.myTrain.getBlock()){
                hostname = "Train"+i;
                boolean addTrain = this.addTrain(hostname, i);
                
                if(!addTrain){
                    throw new NoSuchObjectException("Train #"+i+" not found");
                }
            }
        }
    }
    
    public void setSpeed(int block, int speed) throws RemoteException{
        this.trains.get(block).setSpeed(speed);
    }

	public int getAmountRemoteObject() throws FileNotFoundException, IOException {
				
		return trains.size() ;
		
	}
	
	public TreeMap<Integer, ITrain> getTreeMap(){
		return trains;
	}

	
		
}
