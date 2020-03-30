package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import static com.jogamp.opengl.GL2.*;


/**
 * Represents the terrain, to be implemented according to the Assignments.
 */
class Terrain {

    // set the size of our terrain ( here a square of 50x50)  
    private final static float start = -50;
    private final static float stop = 50;
    private final static float stepbystep = 0.5f; 
    
    
    public Terrain() {
        
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        gl.glPushMatrix();
        // Active the texture
        ShaderPrograms.terrainShader.useProgram(gl);
        
        //Here by doing a double for loop we map all the coordinate of our square (with interval of stepbystep btw each drawing)
        for(float x = start; x < stop; x += stepbystep) {
            gl.glBegin(GL_TRIANGLE_STRIP);	
            for(float y = start; y <= stop; y += stepbystep) {
                gl.glVertex3f(x, y, 0 ); 
                gl.glVertex3f(x + stepbystep, y, 0); 
            }
            gl.glEnd();
        }
        gl.glPopMatrix();
    }
    
    // WE DIDNT DO THE WATER
    
}
