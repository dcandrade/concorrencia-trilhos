package model;

/**
 *
 * @author solenir
 */
public class TrainEngine extends Thread {

    public static final int DOWN_LEFT_BLOCK = 3;
    public static final int DOWN_RIGHT_BLOCK = 2;
    public static final int UPPER_BLOCK = 1;

    private int numberComparison;
    private int x, x0, y, y0;
    private int stepSize;
    private final int block;
    private int distance;

    public TrainEngine(int block) {
             
        if (block == TrainEngine.UPPER_BLOCK) {
            this.x0 = 395;
            this.y0 = 45;
            this.distance = 0;
            this.numberComparison = 70;
        } else if (block == TrainEngine.DOWN_LEFT_BLOCK) {
            this.x0 = 295;
            this.y0 = 375;
            this.distance = 600;
            this.numberComparison = 0;
        } else if (block == TrainEngine.DOWN_RIGHT_BLOCK) {
            this.x0 = 695;
            this.y0 = 175;
            this.distance = 200;
            this.numberComparison = 0;
        }else{
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    
    @Override
    public void run() {
      
        while (true) {

            if (distance < 200) {
                x += this.stepSize;
                distance += this.stepSize;
            } else if (distance < 400 - numberComparison) {
                y += this.stepSize;
                distance += this.stepSize;
            } else if (distance < 600 - numberComparison) {
                x -= this.stepSize;
                distance += this.stepSize;
            } else if (distance < 800 - numberComparison) {
                y -= this.stepSize;
                distance += this.stepSize;
            }

            if (distance >= 800 - numberComparison * 2) {
                distance = 0;
                if(getBlock() == 1){
	        this.x = this.x0;
	        this.y = this.y0;
                }
                else if(getBlock() == 2){
	            this.x = 495;
	            this.y = 175;
                }
                else if(getBlock() == 3){
	             this.x = 295;
	             this.y = 175;
	        }
                     
            }
           
            try {
                sleep(Rail.REFRESH_RATE);
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }

    public int getSpeed() {
        return this.stepSize;
    }
}
