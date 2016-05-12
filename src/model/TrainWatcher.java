package model;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import util.ITrain;

/**
 *
 * @author Daniel Andrade
 * @author Solenir Figuerêdo
 */
public class TrainWatcher extends Thread {

    private final List<ITrain> trains;
    private final ITrain myTrain;
    private final Comparator<ITrain> comparator;

    public TrainWatcher(List<ITrain> trains, ITrain myTrain) {
        this.trains = trains;
        this.myTrain = myTrain;

        this.comparator = Train.getDistanceComparator();
    }

    @Override
    public void run() {
        ITrain train;

        try {
            while (true) {
                Collections.sort(this.trains, this.comparator);
                train = this.trains.get(0);

                if (!this.myTrain.hasIntentionCriticalRegion()
                        && train.hasIntentionCriticalRegion()) {
                    train.allowCriticalRegion();
                }

            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

}
