
package GUI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.server.UnicastRemoteObject;

import model.Point;
import model.Train;
import util.ITrain;

/**
 *
 * @author solenir
 */
public class Main {
	private Train myTrain;
	private Thread threadServer;
	private Thread threadClient;
    private MainWindow window;
    private static final int PORT = 3333;
	
    public Main () throws RemoteException{
    	this.myTrain = new Train(Point.UPPER_BLOCK);
    	this.threadServer = new Thread(new Server());
    	this.threadServer.start();
    	
    }
    
    public static void  main(String args[]) throws RemoteException{
    	new Main();   
    
    }
        
    
    class Client implements Runnable {
  
    	@Override
    	public void run() {
    		try {
				window = new MainWindow("Collision Train", myTrain);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
    		
    	}
    }

    class Server implements Runnable {

    	public Server(){
    		
    	}

    	@Override
    	public void run()  {
    		
     		 try {
				LocateRegistry.createRegistry(PORT);
				ITrain stub = (ITrain) UnicastRemoteObject.exportObject(myTrain, 0);
	    	    //Train stub = (Train) exportObject(train, port);
	    	    Registry registry = LocateRegistry.getRegistry(PORT);
	    	    registry.bind("Train" + myTrain.getBlock(), stub);
	    	    System.out.println("Server online");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		finally {
    			threadClient = new Thread(new Client());
    	    	threadClient.start();
				
			}
    	     		    		
    	}
    }
    
    
}

