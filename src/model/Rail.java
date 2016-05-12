package model;


import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import util.ITrain;

/**
 *
 * @author Daniel Andrade 
 * @author Solenir Figuerêdo
 */
public class Rail extends JPanel implements Runnable {

    private final Color color;
    private final List<ITrain> trains;
    private final int trainBlock;
    public static int REFRESH_RATE = 100;

    public Rail(Color color, int trainBlock) {
        super();
        setLayout(null);
        setSize(994, 672);
        this.trains = new ArrayList<>();
        repaint();
        this.color = color;
        this.trainBlock = trainBlock;
    }

    private void startTrains() throws RemoteException {
        for (ITrain train : this.trains) {
            train.start();
            //System.out.println("O bloco correspondente foi "+ ponto.getBlock());
        }
    }

    public void insertTrain(ITrain point) {
        this.trains.add(point);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(this.color);

        for (ITrain ponto : this.trains) {
            try {
                if (ponto.getBlock() == this.trainBlock) {
                    g.setColor(Color.BLACK);
                    g.fillOval(ponto.getX(), ponto.getY(), 10, 10);
                    g.setColor(this.color);
                } else {
                    g.fillOval(ponto.getX(), ponto.getY(), 10, 10);
                }
            } catch (RemoteException ex) {
                System.err.println(ex.getMessage());
            }
        }

        g.drawRect(400, 50, 200, 130);
        g.drawRect(300, 180, 200, 200);
        g.drawRect(500, 180, 200, 200);
        g.setColor(Color.RED);
        g.drawLine( 500, 180, 500, 380 );
        g.drawLine( 400, 180, 600, 180 );
    }

    @Override
    public void run() {
        try {
            this.repaint();

            while (true) {
                sleep(Rail.REFRESH_RATE);
                this.repaint();

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Rail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
