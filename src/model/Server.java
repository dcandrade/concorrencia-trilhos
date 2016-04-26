/**
 * Componente Curricular: Módulo Integrador de Concorrência e Conectividade
 * Autor: Daniel Andrade e Solenir Figuerêdo Data: 17/04/2016
 *
 * Declaramos que este código foi elaborado por nós em dupla e não contém nenhum
 * trecho de código de outro colega ou de outro autor, tais como provindos de
 * livros e apostilas, e páginas ou documentos eletrônicos da Internet. Qualquer
 * trecho de código de outra autoria que uma citação para o não a nossa está
 * destacado com autor e a fonte do código, e estamos cientes que estes trechos
 * não serão considerados para fins de avaliação. Alguns trechos do código podem
 * coincidir com de outros colegas pois estes foram discutidos em sessões
 * tutorias.
 */
package model;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Daniel Andrade e Solenir Figuerêdo
 */
public class Server extends UnicastRemoteObject implements Runnable {

    private final int port;
    private final Train train;

    public Server(Train train, int port) throws RemoteException, MalformedURLException, AlreadyBoundException {
        super();
        this.train = train;
        this.port = port;
    }
    
    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            LocateRegistry.createRegistry(port);
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Registry registry = LocateRegistry.getRegistry(port);
            registry.bind("Train" + train.getBlock(), train);
            System.err.println("Server online");
        } catch (RemoteException | AlreadyBoundException ex) {
            System.err.println("Error while starting server");
            System.err.println(ex.getLocalizedMessage());
        }
    }
    
    
    public static void main(String args[]) throws RemoteException, MalformedURLException, AlreadyBoundException {
        Train t = new Train(Point.DOWN_RIGHT_BLOCK);
        t.start();
        Server server = new Server(t, 3333);
        server.start();
    }

}
