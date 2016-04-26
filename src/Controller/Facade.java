/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Controller;
import util.ITrain;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author solenir
 */
public class Facade {

    private final Controller controller;

    public Facade(int trainBlock) throws AlreadyBoundException, FileNotFoundException, IOException {
        this.controller = new Controller(trainBlock);
    }

    public void setSpeed(int block, int speed) throws RemoteException {
        this.controller.setSpeed(block, speed);
    }

    public Iterator<ITrain> getTrains(){
        return this.controller.getTrains();
    }

}
