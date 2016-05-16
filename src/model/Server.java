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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import util.ITrain;

/**
 * Classe reponsável por "registrar" um objeto remoto. ALém disso define o uso
 * de criptigrafia usando o SSL, bem como o uso de Certificado. Tem como atributo
 * a porta que seá utilziada para disponinilizar o objeto remoto, assim como 
 * o objeto que representa o trem que será disponibilizado remotamente.
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class Server extends UnicastRemoteObject implements Runnable {

    private final int port; //Porta de seviço.
    private final ITrain train; //Trem que será disponibilizado remotamente.
    
    /**
     * Construtor da classe Server. Ele é responsavel por definir os criterios de 
     * criptografia bem como o uso de autenticação. Para deifnir esses dois
     * serviços, foram usados classes classes disponibilizados justamente
     * para auxiliar no processo de criptografia e autenticação com RMI.
     * @param train objeto que será disponibilizado remotamente.
     * @param port porta que será associada ao objeto remoto.
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws AlreadyBoundException 
     */
    public Server(ITrain train, int port) throws RemoteException, MalformedURLException, AlreadyBoundException {
        super(0,new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory()); //Define criptografia e autenticação.
       
        this.train = train;
        this.port = port;
    }

    /**
     * Método responsável por iniciar a execução da Thread Server.
     */
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Método responsável por realizar o registro do objeto remoto.
     */
    @Override
    public void run() {
        try {
            //Instancia que auxiliará na obtenção do endereço Ip da máquina onde a aplicação é executada.
            Properties cfg = new Properties(); 
            cfg.load(new FileInputStream("data.properties")); //Carrega as informações do arquivo data.properties
            //Cria uma realação de confiança com a máquina que executrá o objeto remoto.
            System.setProperty("java.rmi.server.hostname", cfg.getProperty("train"+train.getBlock())); 
           
            //Cria um registro, definindo uma porta, e também a criptografia e autenticação.
            Registry registry = LocateRegistry.createRegistry(port, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
           
            //Associa o objeto com um determinado nome que será utilizado para importá-lo na aplicação cliente.
            registry.rebind("Train" + train.getBlock(), train); 
            System.out.println("Server online"); //Informa que ocorreu tudo bem a inicialização do servidor. 
        
        //Caso alguma execeção seja lança algum desses catch capturará, exibindo no console qual foi o problema.
        } catch (RemoteException ex) {
            System.err.println("Error while starting server");
            System.err.println(ex.getLocalizedMessage());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  }
