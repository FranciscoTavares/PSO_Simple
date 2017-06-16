

/**
 *
 * @author gian
 */
public class Params {
    
    static Params _this;

    private Params() {
    }
    
    /* Design Pattern Singleton */
    public static Params getInstance() {
        if(_this == null) {
           _this = new Params();
        }
        return _this;
    }
    
    public final boolean compareAccordingProblemType(double a, double b) {
        if(isMinimization()) {
            return (a < b);
        } else {
            return (a > b);
        }
    }
    
    
    public int getMaxIterations() {
        return 10000;
    }
    public int getSwarmSize() {
        return 30;
    }
    public int getDimension() {
        return 30;
    }    
    public boolean isMinimization() {
        return true;
    }
    public double getStopCriterium() {
        return 0.001;
    }
    
    
    public boolean isLimitPosition() {
        return false;
    }
    public double getMinPos() {
        return -30.0;
    }
    public double getMaxPos() {
        return 30.0;
    }
    
    public boolean isLimitVelocity() {
        return true;
    }
    public double getMinVel() {
        return -100000.0;
    }
    public double getMaxVel() {
        return 100000.0;
    }   
    
    
    public double getInertia() {
        return 0.729;
    }
    public double getC1() {
        return 1.49445;
    }
    public double getC2() {
        return 1.49445;
    }

}
