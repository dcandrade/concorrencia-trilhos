package GUI;

import Controller.Controller;
import model.Rail;
import java.awt.Color;
import java.awt.GridLayout;
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
 * @author solenir
 */
public class Client {

    private final Rail railFrame;
    private final JPanel sliderFrame;

    public Client() throws AlreadyBoundException, IOException {
        super();
        this.railFrame = new Rail(Color.blue);
        this.sliderFrame = new JPanel(new GridLayout(Controller.NUM_TRAINS, 1));
        this.sliderFrame.setVisible(true);
    }

    public Rail getRail() {
        return railFrame;
    }
    
    public void repaintRail(){
        this.railFrame.repaint();
    }
  

    public void addTrain(final ITrain train) {
        this.railFrame.insertPoint(train);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
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

        this.sliderFrame.add(slider);
    }

    public void start() {
        Thread thread = new Thread(this.railFrame);
        thread.start();
    }

    public JPanel getSliderFrame() {
        return sliderFrame;
    }

}
