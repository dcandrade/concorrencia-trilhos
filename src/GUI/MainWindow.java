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

package GUI;

import Controller.Facade;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.TrainEngine;

/**
 * Classe responsável por criar a janela de execução principal.
 * Ela tem como atributos uma janela principal, um conteiner de conteudo bem como a facade.
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class MainWindow {

    private final JFrame frame; //Janela principal 
    private final Container container; //Panel de conteudo principal.
    private final Facade facade; //Armzena uma instancia da facade.

    /**
     * Construtor da classe MainWindow, responsável por inicializar os principais objetos utilizados por essa classe.
     * @param title titulo do frame principal.
     * @param trainBlock identificador do trem.
     * @throws AlreadyBoundException exceção caso já exista um registro RMI já cadastrado.
     * @throws IOException problemas no fluxo de entrada e saída de dados.
     * @throws RemoteException problemas com o objeto remoto.
     * @throws NotBoundException  problemas com o registro RMI
     */
    public MainWindow(String title, int trainBlock) throws AlreadyBoundException, IOException, RemoteException, NotBoundException {
      

        this.frame = new JFrame(title); // Cria frame.
        this.facade = new Facade(trainBlock); //Cria facade.
        this.container = new JPanel(new CardLayout()); //Cria panel de conteudo principal, adicionado o gerenciador de layout flexivel.
        //Adiciona o panel que representa o trilho no panel de conteudo principal
        this.container.add(facade.getFrame(), "initialPanel"); 

        /*
          Este bloco de código faz as configurações mais gerais do frame principal
          definindo o tamnaho por exemplo.
        */
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this.container);
        frame.setResizable(false);
        setWindowSize(1000, 700);
        setupPanel();

    }

    /**
     * Método reponsável por exibir a janela principal no centro da tela do computador.
     * @param width dimensão horizontal.
     * @param height dimensão vertical.
     */
    private void setWindowSize(int width, int height) {
        this.frame.setSize(width, height); //Dimensões da tela principal.
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setLocation((tela.width - width) / 2, (tela.height - height) / 2);
    }

    /**
     * Método responsável por fazer as configurações minimas do panel.
     * @throws RemoteException
     * @throws NotBoundException 
     */
    private void setupPanel() throws RemoteException, NotBoundException {
        ((CardLayout) container.getLayout()).show(container, "initialPanel"); //Invoca o container da aplicação.

        JButton buttonInitial = new JButton("Iniciar"); //Cria um botão para iniciar movimentação do trem;
        buttonInitial.setBounds(850, 80, 100, 50); //Dimensiona o botão.
        buttonInitial.setBorder(null); //Retira a borda do botão.

        //Adiciona um evento para o botão utilizando para isso uma classe anônima.
        buttonInitial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    facade.startMyTrain(); //inicia execução do trem local.
                    ((JButton) ae.getSource()).setEnabled(false); //Desabilita botão.
                } catch (RemoteException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });

        JButton load = new JButton("Carregar Trens"); //Cria um botão para carregar os trens remotos.
        load.setBounds(850, 180, 120, 50); //Dimensiona o botão.
        load.setBorder(null); //Retira a borda do botão.

        //Adiciona um evento para o botão utilizando para isso uma classe anônima.
        load.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    facade.loadTrains(); //Invoca o método responsável por carregar os trens remotos.
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.facade.getFrame().add(load); //Adiciona botão no Panel da aplicação.
        this.facade.getFrame().add(buttonInitial); //Adiciona botão no Panel da aplicação.

        this.frame.setVisible(true); //Torna o frame Vísivel.

    }

    /**
     * Método responsável por inicializar a execução da aplicação. Na aplicação foi utilizado
     * criptografia ssl, bem como certificados digitais para autenticação. Desta forma,
     * nesse método temos também a definição de propriedade que garante a execução correta da
     * aplicação, inserido as devidas informações para o reconhecimento do certificado que
     * foi gerado.
     * @param args
     * @throws AlreadyBoundException
     * @throws IOException
     * @throws RemoteException
     * @throws NotBoundException 
     */
    public static void main(String[] args) throws AlreadyBoundException, IOException, RemoteException, NotBoundException {
        System.setProperty("javax.net.ssl.keyStore", "seguro.keystore"); //Identifica a Keystore das chaves publicas e privadas.
        System.setProperty("javax.net.ssl.trustStore", "seguro.keystore"); //Identifica relação de confiança entre cliente e servidor.
        System.setProperty("javax.net.ssl.keyStorePassword", "123456"); //Identifica a senha de acesso a keyStore.
        try {
            MainWindow mainWindow; //Atributo da mainWindows.
            Properties cfg = new Properties(); //Instancia de objeto responsavel por ler algumas informações uteis para aplicação.
            cfg.load(new FileInputStream("data.properties")); //Carrega algums informções da aplicação.
            //Cria um objeto mainWindows, passando como parametro o titulo 
            //da janela princiapal, bem como o identificador do trem local, lido apartir do objeto do tipo Properties.
            mainWindow = new MainWindow("Compartilhamento de Trilhos", Integer.parseInt(cfg.getProperty("myTrain"))); 
        } catch (RemoteException ex) {
        }
    }

}
