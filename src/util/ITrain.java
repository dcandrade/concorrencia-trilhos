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
package util;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface que define os métodos que serão disponibilizados para a invocação remota.
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public interface ITrain extends Remote {

    public void setSpeed(int speed) throws RemoteException;

    public Integer getBlock() throws RemoteException;

    public double getSpeed() throws RemoteException;

    public int getX() throws RemoteException;

    public int getY() throws RemoteException;

    public void start() throws RemoteException;

    public boolean isLimited() throws RemoteException;

    public Double distanceToCriticalRegion() throws RemoteException;

    public boolean isOnCriticalRegion() throws RemoteException;

    public boolean hasPermissionCriticalRegion() throws RemoteException;

    public boolean hasIntentionCriticalRegion() throws RemoteException;

    public void voteForCriticalRegion() throws RemoteException;

    public void exitCriticalRegion() throws RemoteException;

    public void limit() throws RemoteException;

    public void unlimit() throws RemoteException;
}
