package model;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author solenir
 */
public class Ponto extends Thread {

    private int x, y;
    private int stepSize;
    private final JSlider slide;

    public Ponto(int x, int y) {
        this.x = x;
        this.y = y;
        this.stepSize = 1;
        this.slide = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);

        this.slide.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                if (!j.getValueIsAdjusting()) {
                    setStepSize((int) j.getValue());
                }

            }
        });

        slide.setMajorTickSpacing(1);
        slide.setMinorTickSpacing(1);
        slide.setPaintTicks(true);
        slide.setPaintLabels(true);
    }

    public JSlider getSlider() {
        return this.slide;
    }

    protected synchronized void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    private synchronized int getStepSize() {
        return this.stepSize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void run() {
        int distance = 0;
        while (true) {

            if (distance < 250) {
                x += this.stepSize;
                distance += this.stepSize;
            } else if (distance < 500) {
                y += this.stepSize;
                distance += this.stepSize;
            } else if (distance < 750) {
                x -= this.stepSize;
                distance += this.stepSize;
            } else if (distance < 1000) {
                y -= this.stepSize;
                distance += this.stepSize;
            }

            if (distance >= 1000) {
                distance = 0;
                x = 0;
                y = 0;
            }

            try {
                sleep(Quadro.REFRESH_RATE);
            } catch (Exception e) {

            }

        }

    }
}
