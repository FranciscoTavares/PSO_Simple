

/**
 *
 * @author gian
 */
public class Particula {
    
    public Double[] pos;
    public Double[] vel;
    public Double fitness;
    
    public Double[] pBest;
    public Double fitpbest;

    public Particula() {
        pos = new Double[Params.getInstance().getDimension()];
        vel = new Double[Params.getInstance().getDimension()];
        pBest = new Double[Params.getInstance().getDimension()];
        fitness = new Double(0);
        fitpbest = new Double(0);        
    }

}
