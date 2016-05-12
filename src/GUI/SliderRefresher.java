package GUI;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JSlider;
import util.ITrain;

/**
 *
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class SliderRefresher implements Runnable {

    private final ITrain[] trains;
    private final JSlider[] sliders;

    public SliderRefresher(ITrain[] trains, JSlider[] sliders) {
        this.trains = trains;
        this.sliders = sliders;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (ITrain train : this.trains) {
                    this.sliders[train.getBlock() - 1].setValue((int) train.getSpeed());
                }
                Thread.sleep(2000);
            }
        } catch (RemoteException | InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
