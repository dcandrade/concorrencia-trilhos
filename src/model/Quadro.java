package model;


import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author solenir
 */
public class Quadro extends JPanel implements Runnable {

    private final Color cor;
    private final List<Ponto> points;
    public static int REFRESH_RATE = 15;

    public Quadro(Color blue) {
        this.points = new ArrayList<>();
        repaint();
        this.cor = blue;
    }

    public void startPoints() {
        for (Ponto ponto : this.points) {
            ponto.start();
        }
    }
    
    public void insertPoint(int x, int y, int numberComparison){
        this.points.add((new Ponto(x,y, numberComparison)));
    }
    
    
    public void insertPoint(Ponto point){
        this.points.add(point);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(cor);
        
        for (Ponto ponto : this.points) {
            g.fillOval(ponto.getX(), ponto.getY(), 10, 10);
        }
        g.drawRect(400, 50, 200, 130);
        g.drawRect(300,180, 200, 200);
        g.drawRect(500,180, 200, 200);
    }

    @Override
    public void run() {
        this.startPoints();
        this.repaint();
        while(true){
            try {
                sleep(Quadro.REFRESH_RATE-5);
                this.repaint();
            } catch (InterruptedException ex) {
            }
        }
    }

}
