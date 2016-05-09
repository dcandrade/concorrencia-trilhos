/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import util.ITrain;

/**
 *
 * @author dcandrade
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
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                } catch (IllegalArgumentException ex2) {
                    try {
                        return Integer.compare(o1.getBlock(), o2.getBlock());
                    } catch (RemoteException ex3) {
                        ex3.printStackTrace();
                    }
                }
                return 0;
            }
        };
    }

    @Override
    @SuppressWarnings("empty-statement")
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
            ex.printStackTrace();
        }
    }

}
