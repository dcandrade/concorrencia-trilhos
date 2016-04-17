package model;


import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author solenir
 */
public class Quadro extends JPanel implements Runnable {

    private final Color cor;
    private final List<Ponto> points;
    public static int REFRESH_RATE = 10;
    private final int x0, y0, w, h;
    
    public Quadro(Color color, int x0, int y0, int w, int h) {
        this.points = new ArrayList<>();
        repaint();
        this.cor = color;
        
        this.x0=x0;
        this.y0=y0;
        this.w=w;
        this.h=h;
    }

    public void startPoints() {
        for (Ponto ponto : this.points) {
            ponto.start();
        }
    }
    
    public void insertPoint(int x, int y){
        this.points.add((new Ponto(x,y)));
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
        g.drawRect(x0, y0, w, h);
    }

    @Override
    public void run() {
        this.startPoints();
        this.repaint();
        while(true){
            try {
                sleep(Quadro.REFRESH_RATE);
                this.repaint();
            } catch (InterruptedException ex) {
            }
        }
    }

}
