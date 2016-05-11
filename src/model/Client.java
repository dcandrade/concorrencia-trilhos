package model;

import java.awt.Color;

import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.ITrain;

/**
 *
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class Client implements Runnable {

    private final Rail railFrame;
    private final ITrain myTrain;
    private final JSlider[] sliders;

    public Client(ITrain myTrain) throws AlreadyBoundException, IOException {
        super();
        this.myTrain = myTrain;
        this.railFrame = new Rail(Color.blue, myTrain.getBlock());
        this.sliders = new JSlider[Train.NUM_TRAINS];
        this.addTrain(myTrain);
    }

    public Rail getRail() {
        return railFrame;
    }

    public void repaintRail() {
        this.railFrame.repaint();
    }

    private JSlider buildSlider(final ITrain train) throws RemoteException {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, TrainEngine.MAX_SPEED, 1);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                if (!j.getValueIsAdjusting()) {
                    try {
                        train.setSpeed((int) j.getValue());
                    } catch (RemoteException ex) {
                        System.err.println(ex.getLocalizedMessage());
                    }
                }
            }
        });
        slider.setBounds((320 * (train.getBlock() - 1)) + 2, 530, 300, 80);
        slider.setEnabled(true);
        return slider;
    }

    public void addTrain(final ITrain train) throws RemoteException {
        this.railFrame.insertTrain(train);
        JSlider slider = this.buildSlider(train);
        this.railFrame.add(slider);
        this.sliders[train.getBlock() - 1] = slider;
    }

    public void start() {
        Thread thread = new Thread(this.railFrame);
        thread.start();
        thread = new Thread(this);
        thread.start();
    }

    public JPanel getSliderFrame() {
        return railFrame;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (int i = 0; i < this.sliders.length; i++) {
                    if (i == this.myTrain.getBlock() - 1) {
                        this.sliders[i].setEnabled(true);
                    } else {
                        this.sliders[i].setEnabled(this.myTrain.hasPermissionCriticalRegion());
                    }
                }
            }
        } catch (RemoteException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
