package model;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author solenir
 */
public class Ponto extends Thread {
    private int numberComparison;
    private int x, y;
    private int stepSize;
    private JSlider slide;
    private int block;

    public Ponto(int x, int y, int numberComparison) {
        this.numberComparison = numberComparison;
        this.x = x;
        this.y = y;
        this.setUp();
    }
    //Coloquei o valor só para ter compatibilidade
    public Ponto(int block) {
        //TODO: block == 1? x=0. y=0;;
        this(0, 0,0);
        this.block = block;

    }

    private void setUp() {
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

    public int getBlock() {
        return block;
    }
    public void setNumberComparison(int numberComparison){
        this.numberComparison = numberComparison;
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
            
            if (distance < 200) {
                x += this.stepSize;
                distance += this.stepSize;
                
            } else if (distance < 400 - numberComparison) {
                y += this.stepSize;
                distance += this.stepSize;
            } else if (distance < 600-numberComparison) {
                x -= this.stepSize;
                distance += this.stepSize;
            } else if (distance < 800-numberComparison) {
                y -= this.stepSize;
                distance += this.stepSize;
            }
            
            if(distance >= 800-numberComparison*2){
                distance=0;
                 
            }

            try {
                sleep(Quadro.REFRESH_RATE);
            } catch (Exception e) {

            }

        }

    }

    public int getSpeed() {
        return this.stepSize;
    }
}
