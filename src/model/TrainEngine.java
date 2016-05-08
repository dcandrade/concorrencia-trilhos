package model;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author solenir
 */
public class TrainEngine extends Thread {
    
    public static final int DOWN_LEFT_BLOCK = 3;
    public static final int DOWN_RIGHT_BLOCK = 2;
    public static final int UPPER_BLOCK = 1;
    public static final int MAX_SPEED = 5;
    
    private final int warningDistance;
    private final int safeDistance;
    private int numberComparison;
    private int perimeterPosition;
    private int x, x0, y, y0;
    private int stepSize;
    private final int block;
    private final Point2D firstCriticalRegionPoint;
    private final Point2D lastCriticalRegionPoint;
    private boolean intentCriticalRegion;
    private boolean permissionCriticalRegion;
    
    public TrainEngine(int block) {
        this.warningDistance = 50 * block;
        this.safeDistance = warningDistance / 4;
        
        this.intentCriticalRegion = false;
        this.permissionCriticalRegion = false;
        this.perimeterPosition = 0;
        
        if (block == TrainEngine.UPPER_BLOCK) {
            this.x0 = 395;//395, 45
            this.y0 = 45;
            this.numberComparison = 70;
            
            this.firstCriticalRegionPoint = new Point(595, 175);
            this.lastCriticalRegionPoint = new Point(395, 175);
            
        } else if (block == TrainEngine.DOWN_LEFT_BLOCK) {
            this.x0 = 295;
            this.y0 = 175;
            this.numberComparison = 0;
            
            this.firstCriticalRegionPoint = new Point(395, 175);
            this.lastCriticalRegionPoint = new Point(495, 375);
            
        } else if (block == TrainEngine.DOWN_RIGHT_BLOCK) {
            this.x0 = 495;
            this.y0 = 175;
            this.numberComparison = 0;
            this.permissionCriticalRegion = true;
            this.firstCriticalRegionPoint = new Point(495, 375);
            this.lastCriticalRegionPoint = new Point(595, 175);
            
        } else {
            throw new IllegalArgumentException("Invalid block number");
        }
        
        this.block = block;
        this.x = x0;
        this.y = y0;
        this.stepSize = 1;
    }
    
    public int getBlock() {
        return block;
    }
    
    public void setNumberComparison(int numberComparison) {
        this.numberComparison = numberComparison;
    }
    
    protected synchronized void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }
    
    private void intentCriticalRegion() {
        this.intentCriticalRegion = true;
    }
    
    public boolean hasIntentionCriticalRegion() {
        return this.intentCriticalRegion;
    }
    
    public void exitCriticalRegion() {
        this.intentCriticalRegion = false;
        this.permissionCriticalRegion = false;
    }
    
    public void allowCriticalRegion() {
        this.permissionCriticalRegion = true;
    }
    
    public boolean hasPermissionCriticalRegion() {
        return permissionCriticalRegion;
    }
    
    public boolean isOnCriticalRegion() {
        
        if (this.block == TrainEngine.UPPER_BLOCK) {
            return this.y == this.firstCriticalRegionPoint.getY();
        } else if (this.block == TrainEngine.DOWN_RIGHT_BLOCK) {
            
            return this.x == this.firstCriticalRegionPoint.getX()
                    || (this.y == this.lastCriticalRegionPoint.getY()
                    && this.x < this.lastCriticalRegionPoint.getX());
        } else {
            return this.x > this.firstCriticalRegionPoint.getX()
                    && this.y == this.firstCriticalRegionPoint.getY()
                    || this.x == this.lastCriticalRegionPoint.getX();
        }
        
    }
    
    public double distanceToCriticalRegion() {
        if (this.isOnCriticalRegion()) {
            return 0;
        }
        
        return this.firstCriticalRegionPoint.distance(this.x, this.y);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    private void move(int distance) {
        
        if (this.perimeterPosition < 200) {
            this.x += distance;
            this.perimeterPosition += distance;
        } else if (this.perimeterPosition < 400 - numberComparison) {
            this.y += distance;
            this.perimeterPosition += distance;
        } else if (this.perimeterPosition < 600 - numberComparison) {
            this.x -= distance;
            this.perimeterPosition += distance;
        } else if (this.perimeterPosition < 800 - numberComparison) {
            this.y -= distance;
            this.perimeterPosition += distance;
            
            if (this.perimeterPosition >= 800 - numberComparison * 2) {
                this.perimeterPosition = 0;
                this.x = this.x0;
                this.y = this.y0;
            }
        }
    }
    
    @Override
    public void run() {
        while (true) {
                this.move(this.stepSize);            
            try {
                sleep(Rail.REFRESH_RATE);
            } catch (Exception e) {
                System.out.println("Error on Point: " + e.getMessage());
            }
        }
    }
    
    public int getSpeed() {
        return this.stepSize;
    }
}
