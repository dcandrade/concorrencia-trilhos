
/**
 * Componente Curricular: Módulo Integrador de Concorrência e Conectividade
 * Autores: Daniel Andrade e Solenir Figuerêdo Data: 17/04/2016
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
import model.Election;
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
           
            //Instancia de um objeto Properties que será utilizado para auxiliar na identificação do trem.
            Properties cfg = new Properties(); 
            cfg.load(new FileInputStream("data.properties")); //Carregando os dados que estão no arquivo data.properties.
            String host =  cfg.getProperty("train"+key); //Ler o endereço Ip onde está um objeto remoto qualquer.
            /*
              Esta linha é reponsável por "importar" um registro que tenha como configuração os argumentos passados
              É importante perceber que estamos passando também um objeto do tipo SslRMIClientSocketFactory()
              pois o registro está utilizando criptografia Ssl, bem como autenticação para o servidor de registro.
            */
            Registry registry = LocateRegistry.getRegistry(host, Controller.PORT+key, new SslRMIClientSocketFactory());
            ITrain train = (ITrain) registry.lookup(hostname); // "Importação" do objeto remoto.
            this.trains.put(train.getBlock(), train); //"Pega" o objeto remoto utilizado o seu identificador.
            this.client.addTrain(train); //Insere o trem remoto numa lista de trens.
            return train; //Retorna o trem que foi inserido com sucesso.
            
        } catch (NotBoundException | AccessException ex) { //Caso alguma exceção seja lançada é capturada por esse bloco catch.
            System.err.println("Error while adding trains"); //Erro ao inserir os trens.
            System.err.println(ex.getMessage());//Exibe quais foram os erros;
            return null; //Retorna nulo para indicar o erro.
        }
    }

    /**
     * Método responsável por iniciar a importação dos trens remotos. Ele juntamente
     * com o método "addTrain" executa todo o processo de importação.
     * @throws FileNotFoundException quando oarquivo não é encontrado;
     * @throws IOException  problemas com entrada e saída.
     */
    public void loadTrains() throws FileNotFoundException, IOException {
        String hostname; //Variável que auxiliará na importação.
        List<ITrain> otherTrains = new ArrayList<>(); //Lista com trens remotos;

        //Laço de repetição usado para controlar a inserção dos trens no TreeMap.
        for (int i = 1; i <= Train.NUM_TRAINS; i++) {
            //Essa condição é utilizada para evitar que o trem local seja readicionado no TreeMap.
            if (i != this.myTrain.getBlock()) {
                hostname = "Train" + i; //Nome que o objeto remoto foi registrado. Consiste do nome "Train" + seu identificador.
                ITrain train = this.addTrain(hostname, i); //Chamda do método reponsável por importar os objetos remotos.
                
                //Condição que verifica se o trem foi adicionado com sucesso. 
                if (train == null) {
                    throw new NoSuchObjectException("Train #" + i + " not found"); //Exceção caso o trem não tenha sido adicionado.
                } else { //Caso tenha adicionando com sucesso executa esse bloco.
                    otherTrains.add(train); //Adiciona o trem inserido com sucesso nessa lista auxiliar;
                    System.out.println("Train #" + i + " was sucessfully loaded"); //Exibe mensagem de sucesso com o nome do trem adicionado. 
                }
            }
        }
       //Instancia da classe Election. Ela é reponsável por controlar o acesso dos trens a região crítica.
        Election tw = new Election(otherTrains, myTrain);
        tw.start(); //Inicia a thread Election.
        this.client.start(); //Inicia Thread Client.
        this.client.repaintRail(); //Pinta/Repinta os objetos presentes no Jpanel que contém componentes da interface.
    }

    /**
     * Método responsável por inicar a Thread que representa o trem local.
     * @throws RemoteException  lança a execção caso o objeto remoto não esteja diponível.
     */
    public void startMyTrain() throws RemoteException {
        this.myTrain.start();
    }

    /**
     * Método reponsável por modificar a velocidade do trem remotamente.
     * @param block identificação do trem.
     * @param speed nova velocidade que setá atribuida para o trem.
     * @throws RemoteException 
     */
    public void setSpeed(int block, int speed) throws RemoteException {
        this.trains.get(block).setSpeed(speed); //Modificando velocidade.
    }
    
    /**
     * Método reponsável por "pegar um RailFrame".
     * @return  um Rain
     */
    public JPanel getFrame() {
        return this.client.getSliderFrame();
    }

    /**
     * Método reponsável por inserir os trens que estão em execução num ArrayList
     * Com isso a sua iteração se torna mais simples. Desta forma temos um maior control
     * sobre eles.
     * @return um Iterator dos trens.
     */
    public Iterator<ITrain> getTrains() {
        List<ITrain> list = new ArrayList<>(); // Objeto que representará a lista de trens.

        ///Insere todos os trens na lista de trens.
        for (Integer x : this.trains.keySet()) {
            list.add(this.trains.get(x));
        }

        return list.iterator(); //Retorna o iterator dessa lista de trens.
    }

}

