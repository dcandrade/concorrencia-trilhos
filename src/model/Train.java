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
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class Train extends UnicastRemoteObject implements ITrain, Comparable<Train> {

    public static final int NUM_TRAINS = 3;

    private final TrainEngine engine;
    private boolean ready;

    public Train(int trainBlock) throws RemoteException {
        super();
        this.engine = new TrainEngine(trainBlock);
    }

    @Override
    public void start() throws RemoteException {
        this.engine.start();
    }

    @Override
    public void setSpeed(int newSpeed) throws RemoteException {
        this.engine.setSpeed(newSpeed);
    }

    @Override
    public Integer getBlock() {
        return this.engine.getBlock();
    }

    @Override
    public int getSpeed() {
        return this.engine.getSpeed();
    }

    @Override
    public int getX() {
        return this.engine.getX();
    }

    @Override
    public int getY() {
        return this.engine.getY();
    }

    @Override
    public Double distanceToCriticalRegion() {
        return this.engine.distanceToCriticalRegion();
    }

    @Override
    public boolean isOnCriticalRegion() {
        return this.engine.isOnCriticalRegion();
    }

    @Override
    public boolean hasPermissionCriticalRegion() {
        return this.engine.hasPermissionCriticalRegion();
    }

    @Override
    public void allowCriticalRegion() {
        this.engine.allowCriticalRegion();
    }

    @Override
    public boolean hasIntentionCriticalRegion() {
        return this.engine.hasIntentionCriticalRegion();
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
        this.engine.exitCriticalRegion();
    }

    @Override
    public float distanceLeftExitCriticalRegion() throws RemoteException {
        return this.engine.distanceLeftExitCriticalRegion();
    }

    @Override
    public void slowdown(float distance) throws RemoteException {
        this.engine.slowdown(distance);
    }

    @Override
    public void recoverSpeed() throws RemoteException {
        this.engine.recoverSpeed();
    }

}
