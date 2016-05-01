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

import util.ITrain;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Daniel Andrade e Solenir Figuerêdo
 */
public class Train extends UnicastRemoteObject implements ITrain, Comparable<Train> {

    private final TrainEngine train;
    private boolean ready;

    public Train(int trainBlock) throws RemoteException {
        super();
        this.train = new TrainEngine(trainBlock);
    }

    @Override
    public void start() throws RemoteException {
        this.train.start();
    }

    @Override
    public void setSpeed(int newSpeed) throws RemoteException {
        this.train.setStepSize(newSpeed);
    }

    @Override
    public Integer getBlock() {
        return this.train.getBlock();
    }

    @Override
    public int getSpeed() {
        return this.train.getSpeed();
    }

    @Override
    public int getX() {
        return this.train.getX();
    }

    @Override
    public int getY() {
        return this.train.getY();
    }
    
    @Override
    public Double distanceToCriticalRegion() {
        return this.train.distanceToCriticalRegion();
    }
    
    @Override
    public boolean isOnCriticalRegion() {
        return this.train.isOnCriticalRegion();
    }
    
    @Override
    public boolean hasPermissionCriticalRegion() {
        return this.train.hasPermissionCriticalRegion();
    }
    
    @Override
    public void allowCriticalRegion(){
        this.train.allowCriticalRegion();
    }
    
    @Override
     public boolean hasIntentionCriticalRegion() {
         return this.train.hasIntentionCriticalRegion();
     }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Train) {
            return ((Train) o).compareTo(this) == 0;
        }
        
        return false;
    }

    @Override
    public boolean noIsReady() throws RemoteException {

        return this.ready;
    }

    @Override
    public void setReady() throws RemoteException {
        this.ready = true;
    }

    @Override
    public int compareTo(Train o) {
        return this.getBlock().compareTo(o.getBlock());
    }

    @Override
    public void exitCriticalRegion() throws RemoteException {
        this.train.exitCriticalRegion();
    }

}
