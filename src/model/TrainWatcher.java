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

        this.comparator = new Comparator<ITrain>() {

            @Override
            public int compare(ITrain o1, ITrain o2) {
                try {
                    return o1.distanceToCriticalRegion().compareTo(o2.distanceToCriticalRegion());
                } catch (Exception ex) {
                    try {
                        return Integer.compare(o1.getBlock(), o2.getBlock());
                    } catch (RemoteException ex1) {
                        System.err.println(ex1.getMessage());
                    }

                }
                return 0;
            }
        };
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
