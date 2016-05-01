/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import util.ITrain;

/**
 *
 * @author dcandrade
 */
public class TrainWatcher extends Thread {

    private final List<ITrain> trains;
    private final Comparator<ITrain> comparator;

    public TrainWatcher(List<ITrain> trains) {
        this.trains = trains;

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

        try {
            TreeSet<ITrain> candidates = new TreeSet<>(this.comparator);
            ITrain trainOnCriticalRegion = null;
            while (true) {
                if (trainOnCriticalRegion != null) {
                    while (trainOnCriticalRegion.isOnCriticalRegion()); //espera trem sair
                    trainOnCriticalRegion.exitCriticalRegion();
                    trainOnCriticalRegion = null;
                }
                for (ITrain t : this.trains) {
                    if (t.hasIntentionCriticalRegion()) {
                        candidates.add(t);
                    }
                }

                if (!candidates.isEmpty() && trainOnCriticalRegion == null) {
                    trainOnCriticalRegion = candidates.pollFirst();
                    trainOnCriticalRegion.allowCriticalRegion();
                    synchronized (this) {
                        wait(50);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
