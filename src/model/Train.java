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
import java.util.Comparator;

/**
 * Fachada para as operações de um Trem como objeto remoto.
 *
 * @see TrainEngine
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class Train extends UnicastRemoteObject implements ITrain {

    /*Quantidade máxima de trens permitida. Como o número de quadrantes 
     disponibilizados é 3, a quantidade deve ser menor ou igual a esse valor
     */
    public static final int NUM_TRAINS = 3;

    //Engine do trem para o controle de suas operações
    private final TrainEngine engine;

    /**
     * Construtor da classe Trem.
     *
     * @param trainBlock
     * @throws RemoteException
     */
    public Train(int trainBlock) throws RemoteException {
        super();
        this.engine = new TrainEngine(trainBlock);
    }

    /**
     * Comparador de trens com base na distância para a região crítica. Caso a
     * distância seja a mesma, o quadrante do trem é analisado.
     *
     * @return Comparator entre trens
     */
    public static Comparator<ITrain> getDistanceComparator() {
        return new Comparator<ITrain>() {

            @Override
            public int compare(ITrain o1, ITrain o2) {
                try {
                    return o1.distanceToCriticalRegion().compareTo(o2.distanceToCriticalRegion());
                } catch (Exception ex) {
                    try {
                        return Integer.compare(o1.getBlock(), o2.getBlock());
                    } catch (RemoteException ex1) {
                        System.err.println(ex1.getMessage());
                    }

                }
                return 0;
            }
        };
    }

    @Override
    public boolean isLimited() {
        return this.engine.isLimited();
    }

    @Override
    public void start() throws RemoteException {
        this.engine.start();
    }

    @Override
    public void setSpeed(int speed) throws RemoteException {
        this.engine.setSpeed(speed);
    }

    @Override
    public Integer getBlock() {
        return this.engine.getBlock();
    }

    @Override
    public double getSpeed() {
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
    public void voteForCriticalRegion() {
        this.engine.voteForCriticalRegion();
    }

    @Override
    public boolean hasIntentionCriticalRegion() {
        return this.engine.hasIntentionCriticalRegion();
    }

    @Override
    public void exitCriticalRegion() throws RemoteException {
        this.engine.exitCriticalRegion();
    }

    public void unlimit() {
        this.engine.unlimit();
    }

    @Override
    public void limit() throws RemoteException {
        this.engine.limit();
    }

}
