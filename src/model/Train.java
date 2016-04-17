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

import util.TrainInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Daniel Andrade e Solenir Figuerêdo
 */
public class Train extends UnicastRemoteObject implements TrainInterface {

    private Ponto train;
    public static final int DOWN_LEFT_BLOCK = 1;
    public static final int DOWN_RIGHT_BLOCK = 2;
    public static final int UPPER_BLOCK = 3;

    public Train(int trainBlock) throws RemoteException {
        super();
        if (trainBlock > 3 || trainBlock < 1) {
            throw new IllegalArgumentException("Invalid block");
        }
        this.train = new Ponto(trainBlock);
    }
    
    @Override
    public void setSpeed(int newSpeed) throws RemoteException {
        this.train.setStepSize(newSpeed);
    }

    @Override
    public int getBlock() {
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

}
