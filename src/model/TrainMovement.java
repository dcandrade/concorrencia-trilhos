
package model;

import static java.lang.Thread.sleep;

/**
 *
 * @author solenir
 */
public class TrainMovement {
    private int distance;
    private TrainEngine train;
    
    public TrainMovement(TrainEngine train){
        this.distance = 0;
        this.train = train;
        startMovement();
    }
    
    private void startMovement(){
          while (true) {
          
                if (distance < 200) {
                    if (train.distanceToCriticalRegion() > train.getSpeed()+ train.getSafeDistance()) {
                        train.setAX();
                        distance += train.getSpeed();
                    } else if (train.hasPermissionCriticalRegion()) {
                        train.setAX();
                        distance += train.getSpeed();
                    }
                } else if (distance < 400 - train.getNumberComparison()) {
                    if (train.distanceToCriticalRegion() > train.getSpeed() + train.getSafeDistance()) {
                        train.setAY();
                        distance += train.getSpeed();
                    } else if (train.hasPermissionCriticalRegion()) {
                        train.setAY();
                        distance += train.getSpeed();
                    }
                } else if (distance < 600 - train.getNumberComparison()) {
                    if (train.distanceToCriticalRegion() > train.getSpeed() + train.getSafeDistance()) {
                        train.setDX();
                        distance += train.getSpeed();
                    } else if (train.hasPermissionCriticalRegion()) {
                        train.setDX();
                        distance += train.getSpeed();
                    }
                } else if (distance < 800 - train.getNumberComparison()) {
                    if (train.distanceToCriticalRegion() > train.getSpeed() + train.getSafeDistance()) {
                        train.setDY();
                        distance += train.getSpeed();
                    } else if (train.hasPermissionCriticalRegion()) {
                        train.setDY();
                        distance += train.getSpeed();
                    }
                }

                if (distance >= 800 - train.getNumberComparison() * 2) {

                    distance = 0;
                    //Reset the initial position
                    train.setX();
                    train.setY();

                }

                //Rever algoritmo abaixo, precisa melhoorar
                if (!train.hasIntentionCriticalRegion()
                        && train.distanceToCriticalRegion() > 0
                        && train.distanceToCriticalRegion() <= train.getWarningDistance()) {
                    train.intentCriticalRegion();
                } else if (!train.isOnCriticalRegion()
                        && train.distanceToCriticalRegion() > train.getWarningDistance()) {
                    train.exitCriticalRegion();
                }

                try {
                    sleep(Rail.REFRESH_RATE);
                } catch (Exception e) {
                    System.out.println("Error on Point: " + e.getMessage());
                }
            }
   
    }
    
}
