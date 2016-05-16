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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import util.ITrain;

/**
 * Implementação do Algoritmo de Eleição para o acesso dos trens a região
 * crítica.
 *
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class Election extends Thread {

    //Trens candidato

    private final List<ITrain> trains;
    //Trem eleitor
    private final ITrain myTrain;
    //Comparador que define em qual trem o candidato deve votar.
    private final Comparator<ITrain> comparator;

    /**
     * Construtor da classe Election
     *
     * @param candidates Trens candidatos
     * @param voter Trem eleitor
     */
    public Election(List<ITrain> candidates, ITrain voter) {
        this.trains = candidates;
        this.myTrain = voter;

        this.comparator = Train.getDistanceComparator();
    }

    @Override
    public void run() {
        ITrain train;

        /**
         * Vota nos trens de forma periódica.
         */
        try {
            while (true) {
                //Ordena os trens de acordo com o critério estabelecido
                Collections.sort(this.trains, this.comparator);
                //Recupera o trem que atende melhor aos critérios
                train = this.trains.get(0);

                /*Caso o trem eleitor não seja candidato e o trem escolhido seja, 
                 ele ganha o voto.
                 */
                if (!this.myTrain.hasIntentionCriticalRegion()
                        && train.hasIntentionCriticalRegion()) {
                    train.voteForCriticalRegion();
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
