
package robotrace;

/**
 * Implementation of RaceTrack, creating a track from a parametric formula
 */
public class ParametricTrack extends RaceTrack {
    
    @Override
    protected Vector getPoint(double t) {
        // Apply the formula
        return new Vector(10*Math.cos(2*Math.PI*t),14*Math.sin(2*Math.PI*t),1);
    }

    @Override
    protected Vector getTangent(double t) {
        // Apply the formula
        Vector tan = new Vector(-20*Math.PI*Math.sin(2*Math.PI*t),28*Math.PI*Math.cos(2*Math.PI*t),0);
        // normalized the result
        return tan.normalized();

    }
    
}
