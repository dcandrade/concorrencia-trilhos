package GUI;

import java.awt.Color;

import java.io.IOException;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.Rail;
import model.Train;
import model.TrainEngine;

import util.ITrain;

/**
 *
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class Client implements Runnable {

    private final Rail railFrame;
    private final ITrain myTrain;
    private final ITrain[] trains;
    private final JSlider[] sliders;

    public Client(ITrain myTrain) throws AlreadyBoundException, IOException {
        super();
        this.myTrain = myTrain;
        this.railFrame = new Rail(Color.blue, myTrain.getBlock());
        this.trains = new ITrain[Train.NUM_TRAINS];
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
        this.trains[train.getBlock() - 1] = train;
        this.sliders[train.getBlock() - 1] = slider;

        return slider;
    }

    public void addTrain(final ITrain train) throws RemoteException {
        this.railFrame.insertTrain(train);
        JSlider slider = this.buildSlider(train);
        this.railFrame.add(slider);
    }

    public void start() {
        Thread thread = new Thread(this.railFrame);
        thread.start();
        thread = new Thread(this);
        thread.start();
        thread = new Thread(new SliderRefresher(this.trains, this.sliders));
        thread.start();
    }

    public JPanel getSliderFrame() {
        return railFrame;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (ITrain train : this.trains) {
                    if (Integer.compare(this.myTrain.getBlock(), train.getBlock()) != 0) {
                        this.sliders[train.getBlock() - 1].setEnabled(myTrain.isOnCriticalRegion());
                    }
                }
            }
        } catch (RemoteException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
