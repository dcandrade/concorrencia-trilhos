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

import java.awt.Color;

import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;


import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.Rail;
import model.Train;
import model.TrainEngine;

import util.ITrain;

/**
 * Classe que representa o Cliente da aplicação. Ela é responsável principalmente
 * por inserir os trens na interface da aplicação, bem como inserir os JSliders.
 * Além disso controla a diponibilidade dos JSlider. Ou seja, quandoe para quem
 * ele está visivel.
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class Client implements Runnable {

    private final Rail railFrame;//Armazena uma representação de um trilho.
    private final ITrain myTrain; //Armazena o trem que está sendo executado localmente.
    private final ITrain[] trains; //Armazena os trens remotos.
    private final JSlider[] sliders; //Armazena os JSliders.

    /**
     * Construtor da classe Client. Ele é responsável por inicar os principais objetos
     * que será utlizados pela classe para o correto funcionamento da aplicação.
     * @param myTrain trem que está sendo executado localmente.
     * @throws AlreadyBoundException Exceção se já tiver algum registro repetido.
     * @throws IOException problemas no fluxo de entrada e saída de dados.
     */
    public Client(ITrain myTrain) throws AlreadyBoundException, IOException {
        super();
        this.myTrain = myTrain; //Atribui o trem local.
        this.railFrame = new Rail(Color.blue, myTrain.getBlock()); //Cria arepresentação do trem no trilho.
        this.trains = new ITrain[Train.NUM_TRAINS]; //Instancia do vetor de trens remotos.
        this.sliders = new JSlider[Train.NUM_TRAINS]; //Instancia dos JSlider dos trens remotos.
        this.addTrain(myTrain); //Adiciona o trem local da lista geral de trens.
    }

    /**
     * Método reponsável por "pegar" um objeto doo tipo Rail.
     * @return um objeto Rail da aplicação.
     */
    public Rail getRail() {
        return railFrame;
    }

    /**
     * Método responsável por chamara um método do objeto railframe que redesenhará 
     * os componentes em novas posições.
     */
    public void repaintRail() {
        this.railFrame.repaint();
    }
    
    /**
     * Método reponsável por criar os JSlider que serão utilizados para modificar
     * a velocidade do trem.
     * @param train trem que será criado o JSlider.
     * @return retorna o Slider criado.
     * @throws RemoteException lança exeção caso o objeto remoto não esteja disponível.
     * 
     */
    private JSlider buildSlider(final ITrain train) throws RemoteException {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, TrainEngine.MAX_SPEED, 1); //Cria Jslider
        slider.setMajorTickSpacing(1); //Configura JSlider.
        slider.setMinorTickSpacing(1); //Configura JSlider.
        slider.setPaintTicks(true); //Configura JSlider.
        slider.setPaintLabels(true); //Configura JSlider.
        //Adiciona um Slider na condição de evento, utilizando inclusive uma classe anônima.
        slider.addChangeListener(new ChangeListener() {

            
            @Override
            public void stateChanged(ChangeEvent e) { //Executa quando algum evento é lançado pelo JSlider em questão.
                JSlider j = (JSlider) e.getSource(); //Pega o objeto fonte do evento.
                if (!j.getValueIsAdjusting()) { 
                    try {
                        train.setSpeed((int) j.getValue()); //Modifica a velocidade JSlider
                       //Adiciona um limite na velocidade do trem que não está na região crítica.
                        if (myTrain.hasPermissionCriticalRegion()
                                && Integer.compare(train.getBlock(), myTrain.getBlock()) != 0) {
                            train.limit();
                        }
                    } catch (RemoteException ex) {
                        System.err.println(ex.getLocalizedMessage());
                    }
                }
            }
        });
        //Posiciona o JSlider numa posição adequada na tela.
        slider.setBounds((320 * (train.getBlock() - 1)) + 2, 530, 300, 80);
        slider.setEnabled(true); //Habilita o JSlider.
        this.trains[train.getBlock() - 1] = train; //Adiciona o trem em questão num vetor.
        this.sliders[train.getBlock() - 1] = slider; //Adiciona o JSlider em questão num vetor.

        return slider; //Retorna o JSlider que foi adicioando;
    }

    /**
     * Método responsável por adicionar o trem no trilho. Além disso chama o método
     * reponsável por criar o JSlider, o qual é responsável por modificar a 
     * velocidade dos trens. 
     * @param train trem que será inserido.
     * @throws RemoteException  lança exceção caso o objeto remoto não esteja disponével.
     */
    public void addTrain(final ITrain train) throws RemoteException {
        this.railFrame.insertTrain(train); //Insere trem no JPanel que representa o trilho.
        JSlider slider = this.buildSlider(train); //Chamada para o método que constroi o JSlider.
        this.railFrame.add(slider); //Adiciona o JSlider no JPanel que representa o trilho.
    }

    /**
     * Método reponsável por inicializar as principais threads da aplicação.
     */
    public void start() {
        Thread thread = new Thread(this.railFrame); //Cria Thread do Trilho.
        thread.start(); //Inicializa Thread do trilho.
        thread = new Thread(this); //Cria Thread Client.
        thread.start(); //Inicializa Thread Client.
        thread = new Thread(new SliderRefresher(this.trains, this.sliders)); //Cria Thread SliderRefresher.
        thread.start(); //Inicializa Thread SliderRefresher.
    }

    /**
     * Método responsável por "pegar" um objeto railFrame.
     * @return um JPanel que representa um Rail.
     */
    public JPanel getSliderFrame() {
        return railFrame;
    }

    /**
     * Método de execução da Thread Client. Ele é responsável por fazer o controle
     * da disponibilidade dos JSliders.
     */
    @Override
    public void run() {
        try {
            //Laço de repetição continuo. Só encerrará ao término da execução da aplicação.
            while (true) {
                //Percorre a lista de trens,deixando ou não os JSlider disponiveis.
                for (ITrain train : this.trains) {
                    
                    
                     //Esse bloco de comparação é responsável por habilitar/desabilitar os JSlider
                    
                    if (Integer.compare(this.myTrain.getBlock(), train.getBlock()) != 0) {
                        this.sliders[train.getBlock() - 1].setEnabled(myTrain.isOnCriticalRegion());
                    }else{
                        this.sliders[train.getBlock() - 1].setEnabled(!train.isLimited());
                    }
                    if (!myTrain.isOnCriticalRegion() && myTrain.hasPermissionCriticalRegion()) {
                        train.unlimit();
                    }
                    
                }
            }
        } catch (RemoteException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
