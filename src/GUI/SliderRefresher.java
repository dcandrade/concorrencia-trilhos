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

import java.rmi.RemoteException;

import javax.swing.JSlider;
import util.ITrain;

/**
 * Classe responsável por atualizar o poscicionamento do "ponteiro" do JSlider
 * Assim, quando for modificado por um trem remoto, o mesmo será perceptivel
 * na aplicação que está executando em outras máquinas. Assim, a sensação de
 * tempo real é alcançada. Ela armzena um vetor de trens, bem como um vetor de 
 * JSliders.
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class SliderRefresher implements Runnable {

    private final ITrain[] trains; //Vetor de trens.
    private final JSlider[] sliders; //Vetor de JSliders.

    /**
     * Construtor da classe SLiderRefresher. 
     * @param trains vetor de trens.
     * @param sliders vetor de JSliders.
     */
    public SliderRefresher(ITrain[] trains, JSlider[] sliders) {
        this.trains = trains; //Atribuição do vetor de trens.
        this.sliders = sliders; //Atribuição do vetor de JSliders.
    }

    /**
     * Método reponsável por executar a atualização do "ponteiro" do JSlider.
     */
    @Override
    public void run() {
        try {
            //Laço continuo. Sua execução dura enquanto a aplicação estiver em execução.
            while (true) {
                for (ITrain train : this.trains) {
                    this.sliders[train.getBlock() - 1].setValue((int) train.getSpeed()); //Atualiza o "ponteiro"
                }
                Thread.sleep(2000); //Pausa por 2 segundos.
            }
        } catch (RemoteException | InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
