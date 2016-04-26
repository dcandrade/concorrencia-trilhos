package GUI;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
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

    private ITrain myTrain;
    private Thread threadServer;
    private static Thread threadClient;
    private MainWindow window;
    private static final int PORT = 3335;

    public Main() throws RemoteException {
        this.myTrain = new Train(Point.DOWN_RIGHT_BLOCK);
        this.threadServer = new Thread(new Server());
       // this.threadServer.start();

    }

    public static void main(String args[]) throws RemoteException {
        threadClient = new Thread(new Client());
                threadClient.start();

    }

    class Client implements Runnable {

        @Override
        public void run() {
            try {
                window = new MainWindow("Collision Train", myTrain);

            } catch (RemoteException | NotBoundException | MalformedURLException | AlreadyBoundException e) {
                System.err.println(e.getMessage());
            }

        }
    }


}
