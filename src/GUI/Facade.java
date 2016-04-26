/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controller.Controller;
import model.Train;
import util.ITrain;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.TreeMap;

/**
 *
 * @author solenir
 */
public class Facade {
    private Controller controller;
    
    public Facade (Train trainBlock) throws AlreadyBoundException, FileNotFoundException, IOException{
    	this.controller = new Controller(trainBlock);
        
    }
    
    public void setSpeed(int block, int speed) throws RemoteException{
        this.controller.setSpeed(block, speed);
    }
    
    public int getAmountRemoteObject() throws FileNotFoundException, IOException{
    	return this.controller.getAmountRemoteObject();
    	
    }
    
    public TreeMap<Integer, ITrain> getTrain(){
		return this.controller.getTreeMap();
	}
 
    
}
