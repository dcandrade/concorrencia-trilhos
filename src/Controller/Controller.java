/**
 * Componente Curricular: Módulo Integrador de Concorrência e Conectividade
 * Autores: Daniel Andrade e Solenir Figuerêdo Data: 12/05/2016
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import java.util.TreeMap;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.swing.JPanel;

import model.Server;
import model.Train;
import model.TrainWatcher;
import GUI.Client;

import util.ITrain;

/**
 * Classe responsável por controlar, em parte, o funcionamento da aplicação
 * de forma geral. Ela armazena o trem executado localmente, bem como
 * os trens que participam da aplicação, ou seja, os trens remotos disponibilizados
 * a partir do serviço RMI(Inovação de Método Remoto) pela linguagem Java.
 * Além disso armazena uma instância da classe Cliente, a qual axiliará no 
 * funcionamento da aplicação. Armazena também uma pora que será responsável por
 * controlar o acesso a um objeto remoto, no nosso caso os trens. 
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class Controller {

    private static final int PORT = 1234;  //Porta utilizada como parâmetro na "obtenção de objetos remotos"
    private final ITrain myTrain; //Trem que será disponibilizado remotamente;
    private final TreeMap<Integer, ITrain> trains; //TreeMap que armazena todos os trens que estão sendo executados;
    private final Client client; //Client onde são feito alguns controles. Desabilitação de JSlider por exemplo.

    /**
     * Construtor da classe Controller, onde são instanciados os principais
     * objetos utilizados pela classe Controller.
     * @param trainBlock inteiro que representa o identificador do trem.
     * @throws AlreadyBoundException execeção lançada pelo RMI durante associação, caso já tenha um objeto como mesmo nome.
     * @throws FileNotFoundException associação RMI não encontrada.
     * @throws IOException Problemas de entrada e saida de dados.
     */
    public Controller(int trainBlock) throws AlreadyBoundException, FileNotFoundException, IOException {
        this.myTrain = new Train(trainBlock); //Criação do objeto Train que será disponibilizado remotamente.
        this.client = new Client(myTrain); //Insere objeto local no na classe Client.

        this.trains = new TreeMap<>(); //Instancia o Tree Map que conterá os objetos remotos;
        this.trains.put(myTrain.getBlock(), myTrain); //Inserindo objeto no map dos trens;

        /*Criação de um objeto server que será responsável por fazer a associação RMI
         * de um objeto Train. Nele são passados como argumento o objeto que será associado
         * e a porta que fará a interface entre ele e as chamdas de método remoto.
         *
         */        
        Server server = new Server(myTrain, PORT + trainBlock);
        server.start(); //Inicia a execução do objeto Server, afinal ele é um fluxo independente de execução. 
    }

    private ITrain addTrain(String hostname, int key) throws RemoteException, FileNotFoundException, IOException {
        try {
            //Registry registry = LocateRegistry.getRegistry(null, Controller.PORT + key, new SslRMIClientSocketFactory());
            //Registry registry = LocateRegistry.getRegistry(Controller.PORT + key);
            Properties cfg = new Properties();
            cfg.load(new FileInputStream("data.properties"));
            String host =  cfg.getProperty("train"+key);
            Registry registry = LocateRegistry.getRegistry(host, Controller.PORT+key, new SslRMIClientSocketFactory());
            ITrain train = (ITrain) registry.lookup(hostname);
            this.trains.put(train.getBlock(), train);
            this.client.addTrain(train);
            return train;
        } catch (NotBoundException | AccessException ex) {
            System.err.println("Error while adding trains");
            System.err.println(ex.getMessage());
            return null;
        }
    }

    public void loadTrains() throws FileNotFoundException, IOException {
        String hostname;
        List<ITrain> otherTrains = new ArrayList<>();

        for (int i = 1; i <= Train.NUM_TRAINS; i++) {
            if (i != this.myTrain.getBlock()) {
                hostname = "Train" + i;
                ITrain train = this.addTrain(hostname, i);

                if (train == null) {
                    throw new NoSuchObjectException("Train #" + i + " not found");
                } else {
                    otherTrains.add(train);
                    System.out.println("Train #" + i + " was sucessfully loaded");
                }
            }
        }

        TrainWatcher tw = new TrainWatcher(otherTrains, myTrain);
        tw.start();
        this.client.start();
        this.client.repaintRail();
    }

    public void startMyTrain() throws RemoteException {
        this.myTrain.start();
    }

    public void setSpeed(int block, int speed) throws RemoteException {
        this.trains.get(block).setSpeed(speed);
    }

    public JPanel getFrame() {
        return this.client.getSliderFrame();
    }

    public Iterator<ITrain> getTrains() {
        List<ITrain> list = new ArrayList<>();

        for (Integer x : this.trains.keySet()) {
            list.add(this.trains.get(x));
        }

        return list.iterator();
    }

}
