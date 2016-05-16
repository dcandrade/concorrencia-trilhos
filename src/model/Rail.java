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

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JPanel;
import util.ITrain;

/**
 * Controla a exibição dos trilhos e de um conjunto de pontos associados a eles.
 *
 * @see ITrain
 * @see Train
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class Rail extends JPanel implements Runnable {

    //Cor do trilho
    private final Color color;
    //Trens que estão circulando nos trilhos
    private final List<ITrain> trains;
    //Quadrante do trem local
    private final int trainBlock;
    //Taxa de atualização da posição dos trens (ms)
    public static int REFRESH_RATE = 100;

    /**
     * Construtor da classe Rail.
     *
     * @param color Cor dos Trilhos
     * @param trainBlock Quadrante do trem local
     * @throws IOException Em caso da inexistência do arquivo de configurações
     */
    public Rail(Color color, int trainBlock) throws IOException {
        super();
        setLayout(null);
        setSize(994, 672);
        this.trains = new ArrayList<>();
        repaint();
        this.color = color;
        this.trainBlock = trainBlock;
        //Recupera a taxa de atualização do arquivo de propriedades.
        Properties cfg = new Properties();
        cfg.load(new FileInputStream("data.properties"));
        Rail.REFRESH_RATE = Integer.parseInt(cfg.getProperty("fps"));
    }

    /**
     * Adiciona um trem nos trilhos.
     *
     * @param train Trem a ser adicionado.
     */
    public void insertTrain(ITrain train) {
        this.trains.add(train);
    }

    /**
     * Desenha os trilhos e os trens na tela.
     *
     * @param g Conjunto a ser atualizado.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.color);

        for (ITrain ponto : this.trains) {
            try {
                //Caso o trem seja local, o mesmo é desenhado em uma cor diferente
                if (ponto.getBlock() == this.trainBlock) {
                    g.setColor(Color.BLACK);
                    g.fillOval(ponto.getX(), ponto.getY(), 10, 10);
                    g.setColor(this.color);
                } //Os outros elementos são desenhados na cor padrão
                else {
                    g.fillOval(ponto.getX(), ponto.getY(), 10, 10);
                }
            } catch (RemoteException ex) {
                System.err.println(ex.getMessage());
            }
        }

        g.drawRect(400, 50, 200, 130);
        g.drawRect(300, 180, 200, 200);
        g.drawRect(500, 180, 200, 200);
        /*
         A região crítica é desenhata em vermelho para destacar do restante dos 
         trilhos
         */
        g.setColor(Color.RED);
        g.drawLine(500, 180, 500, 380);
        g.drawLine(400, 180, 600, 180);
    }

    @Override
    public void run() {
        /**
         * Periodicamente redesenha os elementos dos trilhos na tela.
         */
        try {
            while (true) {
                this.repaint();
                sleep(Rail.REFRESH_RATE);
            }

        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
