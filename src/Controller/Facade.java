package Controller;

import util.ITrain;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;

/**
 *
 * @author Daniel Andrade 
 * @author Solenir Figuerêdo
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

    public void startMyTrain() throws RemoteException{
         this.controller.startMyTrain();
    }
    
    public void loadTrains() throws IOException{
        System.out.println("Loading trains...");
        this.controller.loadTrains();
    }
}
