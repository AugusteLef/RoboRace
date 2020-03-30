
package robotrace;

/**
 * Implementation of RaceTrack, creating a track from control points for a 
 * cubic Bezier curve
 */
public class BezierTrack extends RaceTrack {
    
    private Vector[] controlPoints;

    
    BezierTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
       
    }
    
       @Override
    protected Vector getPoint(double t) {
        t %= 1; // to only allow inputs 0..1
        int n_fragments = controlPoints.length / 4;
        int fragment = (int)Math.floor(t * n_fragments)*4;
        t *= n_fragments;
        t %= 1;
        return getCubicBezierPnt(t, controlPoints[fragment], controlPoints[fragment + 1], controlPoints[fragment + 2], controlPoints[fragment + 3]);
    }

    @Override
    protected Vector getTangent(double t) {
        t %= 1; // only allow input 0..1
        int n_fragments = controlPoints.length / 4;
        int fragment = (int)Math.floor(t * n_fragments)*4;
        t *= n_fragments;
        t %= 1;
        return getCubicBezierTng(t, controlPoints[fragment], controlPoints[fragment + 1], controlPoints[fragment + 2], controlPoints[fragment + 3]);
    }
}
