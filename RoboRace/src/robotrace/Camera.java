package robotrace;

/**
 * Implementation of a camera with a position and orientation. 
 */
class Camera {

    /** The position of the camera. */
    public Vector eye = new Vector(3f, 6f, 5f);

    /** The point to which the camera is looking. */
    public Vector center = Vector.O;

    /** The up vector. */
    public Vector up = Vector.Z;

    /**
     * Updates the camera viewpoint and direction based on the
     * selected camera mode.
     */
    public void update(GlobalState gs, Robot focus) {

        switch (gs.camMode) {
            
            // First person mode    
            case 1:
                setFirstPersonMode(gs, focus);
                break;
                
            // Default mode    
            default:
                setDefaultMode(gs);
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     */
    private void setDefaultMode(GlobalState gs) {
        //compute the Eye postion using the spherical coordinate
        double theta = gs.theta ;
        double phi = gs.phi ;
        double x = gs.vDist * Math.cos(theta) * Math.cos(phi) ; 
        double y = gs.vDist * Math.sin(theta) * Math.cos(phi) ;
        double z = gs.vDist * Math.sin(phi) ;
        
        // Now we update the eye position
        eye = new Vector(x, y, z) ;

        // Then we set set the center to gs.cnt (GlobalState) that give us the center point (.pdf)
        center = gs.cnt;

        //As in the variable of this class, we set up to Vector.Z because "the Z-axis must pointing up"
        up = Vector.Z;      
    }

    /**
     * Computes eye, center, and up, based on the first person mode.
     * The camera should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus) {
        
        //Here we compute the view from the perspective or the robot (focus)
        Vector x = new Vector(0,0,2);
        Vector y = new Vector(focus.direction.x, focus.direction.y, focus.direction.z);
      
        eye = focus.position.add(x).add(y); // update the eye parameter
        center = focus.position.add(focus.direction.scale(100)).add(x); //update the center position of the camera
    }
}
