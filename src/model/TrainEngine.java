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

package model;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Controla o movimento, velocidade e posição do Trem. Também possui definições
 * relacionadas ao quadrante, velocidade máxima e distância para se candidatar
 * ao acesso da região crítica.
 *
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class TrainEngine extends Thread {

    //Definição das variáveis representantes de cada quadrante
    public static final int DOWN_LEFT_BLOCK = 3;
    public static final int DOWN_RIGHT_BLOCK = 2;
    public static final int UPPER_BLOCK = 1;

    //Definição da velocidade máxima permitida
    public static final int MAX_SPEED = 10;

    //Definição da distância mínima para se candidatar ao acesso da região crítica
    public static final int WARNING_DISTANCE = 10;

    //Desvio da posição inicial do trem em relação a reta y=175
    private final int offset;
    //Quadrante do trem
    private final int block;
    //Posição atual do trem
    private int x, y;
    //Posição inicial do trem
    private int x0, y0;
    //Votos para que o trem entre na região crítica
    private int votesReceived;

    //Distância do trem ao ponto (x0,y0) seguindo as retas definidas pelo quadrante
    private double perimeterPosition;
    //Velocidade atual do trem
    private double speed;

    //Controle do bloqueio de alteração de velocidade do trem
    private boolean isLimited;
    //Controle do interesse de acesso a região crítica
    private boolean intentCriticalRegion;

    //Primeiro ponto da região crítica para o presente trem
    private final Point2D firstCriticalRegionPoint;
    //Último ponto da região crítica para o presente trem
    private final Point2D lastCriticalRegionPoint;

    /**
     * Construtor da classe Trem.
     *
     * @param block Quadrante pelo qual o trem se moverá.
     */
    public TrainEngine(int block) {
        this.isLimited = false;
        this.perimeterPosition = 0;

        if (block == TrainEngine.UPPER_BLOCK) {
            this.x0 = 395;
            this.y0 = 45;
            this.offset = 70;
            this.votesReceived = 0;
            this.intentCriticalRegion = false;
            this.firstCriticalRegionPoint = new Point(595, 175);
            this.lastCriticalRegionPoint = new Point(395, 175);

        } else if (block == TrainEngine.DOWN_LEFT_BLOCK) {
            this.x0 = 295;
            this.y0 = 175;
            this.offset = 0;
            this.votesReceived = 0;
            this.intentCriticalRegion = false;
            this.firstCriticalRegionPoint = new Point(395, 175);
            this.lastCriticalRegionPoint = new Point(495, 375);

        } else if (block == TrainEngine.DOWN_RIGHT_BLOCK) {
            this.x0 = 495;
            this.y0 = 175;
            /*
             A posição (x0,y0) dada acima coloca o trem na região crítica. Para
             que ele possa se mover, é preciso que o mesmo tenha permissão. Dessa
             forma, inicia com os votos dos dois outros trens e com intenção de
             entrar na região.
             */
            this.votesReceived = 2;
            this.intentCriticalRegion = true;
            this.offset = 0;
            this.firstCriticalRegionPoint = new Point(495, 375);
            this.lastCriticalRegionPoint = new Point(595, 175);

        } else {
            //Lança exceção caso o quadrante do trem seja inválido
            throw new IllegalArgumentException("Invalid block number");
        }

        this.x = x0;
        this.y = y0;
        this.block = block;
        this.speed = 1;
    }

    public int getBlock() {
        return block;
    }

    /**
     * Demonstra interesse no acesso a região crítica
     */
    private void intentCriticalRegion() {
        this.intentCriticalRegion = true;
    }

    public boolean hasIntentionCriticalRegion() {
        return this.intentCriticalRegion;
    }

    /**
     * Informa que o trem saiu da região crítica. Reseta as variáveis de
     * controle do acesso a região.
     */
    public void exitCriticalRegion() {
        this.intentCriticalRegion = false;
        this.votesReceived = 0;
    }

    /**
     * Vota para que este trem entre na região crítica.
     */
    public void voteForCriticalRegion() {
        this.votesReceived++;
    }

    /**
     * Verifica se o trem tem acesso a região crítica.
     *
     * @return True caso o trem tenha permissão, false caso contrário.
     */
    public boolean hasPermissionCriticalRegion() {
        return this.votesReceived >= 2;
    }

    /**
     * Verifica se o trem está na região crítica.
     *
     * @return True caso o trem esteja na região crítica, false caso contrário.
     */
    public boolean isOnCriticalRegion() {

        if (this.block == TrainEngine.UPPER_BLOCK) {
            return this.y == this.firstCriticalRegionPoint.getY();
        } else if (this.block == TrainEngine.DOWN_RIGHT_BLOCK) {

            return this.x == this.firstCriticalRegionPoint.getX()
                    || (this.y == this.lastCriticalRegionPoint.getY()
                    && this.x < this.lastCriticalRegionPoint.getX());
        } else {
            return this.x > this.firstCriticalRegionPoint.getX()
                    && this.y == this.firstCriticalRegionPoint.getY()
                    || this.x == this.lastCriticalRegionPoint.getX();
        }

    }

    /**
     * Calcula a distância aproximada entre a posição atual do trem e o início
     * da região crítica.
     *
     * @return A distância até a região crítica.
     */
    public double distanceToCriticalRegion() {
        if (this.isOnCriticalRegion()) {
            return 0;
        }

        return this.firstCriticalRegionPoint.distance(this.x, this.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getSpeed() {
        return this.speed;
    }

    /**
     * Limita a velocidade do trem a velocidade atual.
     */
    public void limit() {
        this.isLimited = true;
    }

    /**
     * Retira o limite de velocidade do trem.
     */
    public void unlimit() {
        this.isLimited = false;
    }

    /**
     * Verifica se o trem possui limite de velocidade.
     *
     * @return True caso a velocidade limite é menor que a velocidade máxima,
     * false caso contrário.
     */
    public boolean isLimited() {
        return isLimited;
    }

    /**
     * Muda a velocidade do trem
     *
     * @param speed Nova velocidade do trem.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Movimenta o trem para frente, seguindo as direções estabelecidas no
     * quadrante.
     *
     * @param distance Distância que o trem deverá percorrer.
     */
    private void move(double distance) {

        if (this.perimeterPosition < 200) {
            this.y = this.y0; //Ajusta possível desvio
            this.x += distance;
            this.perimeterPosition += distance;
        } else if (this.perimeterPosition < 400 - offset) {
            this.x = this.x0 + 200;
            this.y += distance;
            this.perimeterPosition += distance;
        } else if (this.perimeterPosition < 600 - offset) {
            this.y = y0 + 200 - offset;
            this.x -= distance;
            this.perimeterPosition += distance;
        } else if (this.perimeterPosition < 800 - offset) {
            this.x = x0;
            this.y -= distance;
            this.perimeterPosition += distance;

            if (this.perimeterPosition >= 800 - offset * 2) {
                this.perimeterPosition = 0;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            /*
             Caso o trem esteja fora da área de aviso, se move normalmente e reseta
             o status das variáveis de controle de acesso a região crítica.
             */
            if (this.distanceToCriticalRegion() > TrainEngine.WARNING_DISTANCE) {
                this.move(this.getSpeed());
                this.exitCriticalRegion();
            } /*
             Caso contrário, o trem só se move caso tenha permissão para adentrar
             a região crítica.
             */ else if (this.hasPermissionCriticalRegion()) {
                this.move(this.getSpeed());
            } /*
             Caso o trem esteja na área de aviso, a intenção de acesso a região 
             crítica é ativada.
             */ else if (!this.hasIntentionCriticalRegion()
                    && this.distanceToCriticalRegion() <= TrainEngine.WARNING_DISTANCE) {
                this.intentCriticalRegion();
            }

            try {
                /**
                 * Pausa o trem por um período igual a taxa de atualização da
                 * tela.
                 */
                sleep(Rail.REFRESH_RATE);
            } catch (Exception e) {
                System.out.println("Error on Point: " + e.getMessage());
            }
        }
    }
}
