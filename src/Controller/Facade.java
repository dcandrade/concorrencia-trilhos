
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
package Controller;

import util.ITrain;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 * Classe que será responsavel por intermediar a comunicação entre a interface
 * gráfica e o controller da aplicação. Ela tem como atributo um controller
 * pois é  partir dele que os métodos do controller serão chamados pela interface
 * da aplicação.
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

    public Iterator<ITrain> getTrains() {
        return this.controller.getTrains();
    }

    public void startMyTrain() throws RemoteException {
        this.controller.startMyTrain();
    }

    public void loadTrains() throws IOException {
        System.out.println("Loading trains...");
        this.controller.loadTrains();
    }

    public JPanel getFrame() {
        return this.controller.getFrame();
    }
}
