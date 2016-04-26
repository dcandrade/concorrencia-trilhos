package model;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author solenir
 */
public class Point extends Thread {

    public static final int DOWN_LEFT_BLOCK = 3;
    public static final int DOWN_RIGHT_BLOCK = 2;
    public static final int UPPER_BLOCK = 1;

    private int numberComparison;
    private int x, x0, y, y0;
    private int stepSize;
    private JSlider slide;
    private final int block;
    private int logicValue;

    public Point(int block) {
        if (block == Point.UPPER_BLOCK) {
            this.x0 = 395;
            this.y0 = 45;
            this.numberComparison = 70;
        } else if (block == Point.DOWN_LEFT_BLOCK) {
            this.x0 = 295;
            this.y0 = 175;
            this.numberComparison = 0;
        } else if (block == Point.DOWN_RIGHT_BLOCK) {
            this.x0 = 495;
            this.y0 = 175;
            this.numberComparison = 0;
        }else{
             throw new IllegalArgumentException("Invalid block number");
        }
        
        this.block = block;
        this.x = x0;
        this.y = y0;
        this.setUp();
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

    public void setNumberComparison(int numberComparison) {
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
            } else if (distance < 600 - numberComparison) {
                x -= this.stepSize;
                distance += this.stepSize;
            } else if (distance < 800 - numberComparison) {
                y -= this.stepSize;
                distance += this.stepSize;
            }

            if (distance >= 800 - numberComparison * 2) {
                distance = 0;

                //Reset the initial position
                this.x = this.x0;
                this.y = this.y0;
            }

            try {
                sleep(Rail.REFRESH_RATE);
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }

    public int getSpeed() {
        return this.stepSize;
    }
}
