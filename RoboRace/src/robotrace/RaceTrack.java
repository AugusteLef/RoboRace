package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import static com.jogamp.opengl.GL2.*;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
abstract class RaceTrack {
    
    //Number of vertices used each time
    private final int nbrVertrices=40;
    //Number of tracks
    private final int nbrTracks=4;
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;
    
    
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }


    
    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        
        int tmin=0;        
        double dt = Math.pow(nbrVertrices, -1);
        
        //Draw the trakcs
        drawTrack(gl, glu, glut, tmin, dt, nbrVertrices);
        //Darw the 
        drawFundation(gl, glu,glut, tmin, dt, nbrVertrices);
        
        // Here we will draw the line betweens each track
        for (int i=0; i < nbrTracks; i++){
            gl.glBegin(GL_LINE_LOOP);
            gl.glColor3f(0, 0, 0);   
            
            //for each vertrices of each tracks
            for (int j=0; j <= nbrVertrices; j++){
                Vector P = getPoint(tmin+j*dt);
                Vector Normal = new Vector(0,0,1);
                Vector Tangent = getTangent(tmin+j*dt);
                Vector Bitangent = Normal.cross(Tangent);
                gl.glVertex3d(P.x+(i-3)*laneWidth*Bitangent.x,P.y+(i-3)*laneWidth*Bitangent.y,P.z+(i-3)*laneWidth*Bitangent.z);     
            }
            
            gl.glEnd();
            gl.glFlush();
        }
    }
    
    private void drawFundation(GL2 gl, GLU glu, GLUT glut, int tmin, double dt, int nbrVertrices) {
        // Draw the inner side of the fundation
        gl.glPushMatrix();
        //Textures does not work...
        Textures.brick.enable(gl);
        Textures.brick.bind(gl);
        
        gl.glBegin(GL_TRIANGLE_STRIP);
        
        //For each vertrices
        for (int i = 0; i <= nbrVertrices; i++){
            Vector P = getPoint(tmin+i*dt);
            Vector tan = getTangent(tmin+i*dt);
            Vector norm = new Vector(0,0,1);
            Vector Bitangent = norm.cross(tan);
            
            gl.glVertex3d(P.x+1*laneWidth*Bitangent.x,P.y+1*laneWidth*Bitangent.y,1);                
            gl.glVertex3d(P.x+1*laneWidth*Bitangent.x,P.y+1*laneWidth*Bitangent.y,-1); 
        }
        
        gl.glEnd();
        gl.glFlush();
        
        //disable texture (does not work anyway)
        Textures.brick.disable(gl);
        gl.glPopMatrix();
     
        
        
        //Draw the outer surface of the dunfation (same process except that we change 1 parameter in the gl.glVertex3d (st we draw at the correct place)
        gl.glPushMatrix();
        Textures.brick.enable(gl);
        Textures.brick.bind(gl);
        
        gl.glBegin(GL_TRIANGLE_STRIP);
        
        for (int i=0; i <= nbrVertrices; i++){
            Vector P = getPoint(tmin+i*dt);
            Vector tan = getTangent(tmin+i*dt);
            Vector norm = new Vector(0,0,1);
            Vector Bitangent = norm.cross(tan);
            
            gl.glVertex3d(P.x-3*laneWidth*Bitangent.x,P.y-3*laneWidth*Bitangent.y,1);                
            gl.glVertex3d(P.x-3*laneWidth*Bitangent.x,P.y-3*laneWidth*Bitangent.y,-1);  
        }
        
        gl.glEnd();
        gl.glFlush();
        
        Textures.brick.disable(gl);
        gl.glPopMatrix();

    }
    
    public void drawTrack(GL2 gl, GLU glu, GLUT glut, int tmin, double dt, int nbrVertrices){
        // HERE WE GLOBALLY FIND THE SAME PROCESS THAN IN DRAWFUNDATION EXCEPT THAT WE CHANGE THE COORDINATES ST WE DRAW AT THE CORRECT PLACE
        
        
        // Draw (top) surface of the track
        gl.glPushMatrix();
        Textures.track.enable(gl);
        Textures.track.bind(gl);
        gl.glBegin(GL_TRIANGLE_STRIP);
        gl.glColor3f(255, 0, 0);   
        for (int i=0; i <= nbrVertrices; i++) {
            Vector P = getPoint(tmin+i*dt);
            Vector tan = getTangent(tmin+i*dt);
            Vector norm = new Vector(0,0,1);
            Vector Bitangent = norm.cross(tan);
            
            gl.glVertex3d(P.x+1*laneWidth*Bitangent.x,P.y+1*laneWidth*Bitangent.y,1);                
            gl.glVertex3d(P.x-3*laneWidth*Bitangent.x,P.y-3*laneWidth*Bitangent.y,1);                
        }
        
        gl.glEnd();
        gl.glFlush();
        Textures.track.disable(gl);
        gl.glPopMatrix();
        
        // Darw the down surface (at z =-1)
        gl.glBegin(GL_TRIANGLE_STRIP);
        for (int i=0; i <= nbrVertrices; i++){
            Vector P = getPoint(tmin+i*dt);
            Vector tan = getTangent(tmin+i*dt);
            Vector norm = new Vector(0,0,1);
            Vector Bitangent = norm.cross(tan);
            
            gl.glVertex3d(P.x+1*laneWidth*Bitangent.x,P.y+1*laneWidth*Bitangent.y,-1);                
            gl.glVertex3d(P.x-3*laneWidth*Bitangent.x,P.y-3*laneWidth*Bitangent.y,-1);  
        }
        
        gl.glEnd();
        gl.glFlush();
    }
    
    
    // add in RaceTrack.jave as told in the .pdf (even if in our opinion it should ne in BezierTrack.java)
    protected Vector getCubicBezierPnt(double t,Vector P0,Vector P1,Vector P2,Vector P3) {
        double it = 1 - t;
        return P0.scale(Math.pow(it,3)).add(P1.scale(3*t*Math.pow(it,2))).add(P2.scale(3*Math.pow(t,2)*it)).add(P3.scale(Math.pow(t,3)));    }
    
    protected Vector getCubicBezierTng(double t, Vector P0, Vector P1,Vector P2, Vector P3) {
        double it = 1 - t;
        return P0.scale(-3*Math.pow(it,2)).add(P1.scale(3*(t-1)*(3*t-1))).add(P2.scale(6*t - 9*Math.pow(t,2))).add(P3.scale(3*Math.pow(t,2))).normalized();
    }
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t){

        //Applys formula
        Vector tangent = getLaneTangent(lane, t);
        Vector normal = tangent.cross(Vector.Z).normalized();
        return getPoint(t).add(normal.scale(laneWidth * lane));


    }
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t){
        
        return getTangent(t);

    }
    
    
    
    // Returns a point on the test track at 0 <= t < 1.
    protected abstract Vector getPoint(double t);

    // Returns a tangent on the test track at 0 <= t < 1.
    protected abstract Vector getTangent(double t);
}
