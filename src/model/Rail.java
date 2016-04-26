package model;

import java.awt.BorderLayout;
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
 * @author solenir
 */
public class Rail extends JPanel implements Runnable {

    private final Color cor;
    private final List<ITrain> points;
    public static int REFRESH_RATE = 15;

    public Rail(Color color) {
        super(); //P/ visualizar o slider
        //setLayout(null);
        setSize(994, 672);
        this.points = new ArrayList<>();
        repaint();
        this.cor = color;
    }

    public void startPoints() throws RemoteException {
        for (ITrain ponto : this.points) {
            ponto.start();
            //System.out.println("O bloco correspondente foi "+ ponto.getBlock());
        }
    }

    public void insertPoint(ITrain point) {
        this.points.add(point);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(cor);

        for (ITrain ponto : this.points) {
            try {
                g.fillOval(ponto.getX(), ponto.getY(), 10, 10);
            } catch (RemoteException ex) {
                Logger.getLogger(Rail.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        g.drawRect(400, 50, 200, 130);
        g.drawRect(300, 180, 200, 200);
        g.drawRect(500, 180, 200, 200);
    }
    public List<ITrain> getList(){
    	return points;
    }

    @Override
    public void run() {
        try {
            this.startPoints();
            this.repaint();
            
            while (true) {
                sleep(Rail.REFRESH_RATE - 5);
                this.repaint();

            }
        } catch (InterruptedException | RemoteException ex) {
            Logger.getLogger(Rail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
