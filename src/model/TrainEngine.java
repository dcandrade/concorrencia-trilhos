package model;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author Daniel Andrade 
 * @author Solenir Figuerêdo
 */
public class TrainEngine extends Thread {

    public static final int DOWN_LEFT_BLOCK = 3;
    public static final int DOWN_RIGHT_BLOCK = 2;
    public static final int UPPER_BLOCK = 1;
    public static final int MAX_SPEED = 10;
    
    public static final int WARNING_DISTANCE = 10;

    private final int offset;
    private final int block;
    private double perimeterPosition;
    private int x, x0, y, y0;
    private double speed;
    private int permissionsCriticalRegion;
    private boolean isLimited;

    private boolean intentCriticalRegion;

    private final Point2D firstCriticalRegionPoint;
    private final Point2D lastCriticalRegionPoint;

    public TrainEngine(int block) {
        this.isLimited = false;

        this.perimeterPosition = 0;

        if (block == TrainEngine.UPPER_BLOCK) {
            this.x0 = 395;
            this.y0 = 45;
            this.offset = 70;
            this.permissionsCriticalRegion = 0;
            this.intentCriticalRegion = false;
            this.firstCriticalRegionPoint = new Point(595, 175);
            this.lastCriticalRegionPoint = new Point(395, 175);

        } else if (block == TrainEngine.DOWN_LEFT_BLOCK) {
            this.x0 = 295;
            this.y0 = 175;
            this.offset = 0;
            this.permissionsCriticalRegion = 0;
            this.intentCriticalRegion = false;
            this.firstCriticalRegionPoint = new Point(395, 175);
            this.lastCriticalRegionPoint = new Point(495, 375);

        } else if (block == TrainEngine.DOWN_RIGHT_BLOCK) {
            this.x0 = 495;
            this.y0 = 175;
            this.permissionsCriticalRegion = 2;
            this.intentCriticalRegion = true;
            this.offset = 0;
            this.firstCriticalRegionPoint = new Point(495, 375);
            this.lastCriticalRegionPoint = new Point(595, 175);

        } else {
            throw new IllegalArgumentException("Invalid block number");
        }

        this.block = block;
        this.x = x0;
        this.y = y0;
        this.speed = 1;
    }

    public int getBlock() {
        return block;
    }

    private void intentCriticalRegion() {
        this.intentCriticalRegion = true;
    }

    public boolean hasIntentionCriticalRegion() {
        return this.intentCriticalRegion;
    }

    public void exitCriticalRegion() {
        this.intentCriticalRegion = false;
        this.permissionsCriticalRegion = 0;
    }

    public void allowCriticalRegion() {
        this.permissionsCriticalRegion++;
    }

    public boolean hasPermissionCriticalRegion() {
        return permissionsCriticalRegion >= 2;
    }

    public float distanceLeftExitCriticalRegion() {
        double dist = this.lastCriticalRegionPoint.distance(this.x, this.y);
        dist += (dist * 0.1);
        return (float) dist;
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

    public double getSpeed() {
        return this.speed;
    }

    public void limit() {
        this.isLimited = true;
    }
    
    public void unlimit() {
        this.isLimited = false;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private void move(double distance) {

        if (this.perimeterPosition < 200) {
            this.y = this.y0; //Fix possible deviation
            this.x += distance;
            this.perimeterPosition += distance;
        } else if (this.perimeterPosition < 400 - offset) {
            this.x = this.x0 + 200;
            this.y += distance;
            this.perimeterPosition += distance;
        } else if (this.perimeterPosition < 600 - offset) {
            this.y = y0 + 200 - offset;
            this.x -= distance;
            this.perimeterPosition += distance;
        } else if (this.perimeterPosition < 800 - offset) {
            this.x = x0;
            this.y -= distance;
            this.perimeterPosition += distance;

            if (this.perimeterPosition >= 800 - offset * 2) {
                this.perimeterPosition = 0;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            if (this.distanceToCriticalRegion() > TrainEngine.WARNING_DISTANCE) {
                this.move(this.getSpeed());
                this.exitCriticalRegion();
            } else if (this.hasPermissionCriticalRegion()) {
                this.move(this.getSpeed());
            } else if (!this.hasIntentionCriticalRegion()
                    && this.distanceToCriticalRegion() <= TrainEngine.WARNING_DISTANCE) {
                this.intentCriticalRegion();
            }

            try {
                sleep(Rail.REFRESH_RATE);
            } catch (Exception e) {
                System.out.println("Error on Point: " + e.getMessage());
            }
        }
    }

}
