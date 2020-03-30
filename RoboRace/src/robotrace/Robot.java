package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

/**
* Represents a Robot, to be implemented according to the Assignments.
*/
class Robot {
    
    /** The position of the robot. */
    public Vector position = new Vector(0, 0, 0);
    
    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1, 0, 0);

    /** The material from which this robot is built. */
    private final Material material;
    
    // Variable use in the construction of a robot (to avoid MAGIC NUMBER)
    
    //Body variable
    private final static float BODY_HEIGHT = 0.5f;
    private final static float BODY_WIDTH = 0.4f;
    private final static float BODY_THICKNESS = 0.2f;
    
    //Head variable
    private final static float HEAD_RADIUS = 0.15f;
    
    //Leg variable
    private final static float LEG_WIDTH = 0.15f;
    private final static float UPPER_LEG_LEGTH = 0.35f;
    private final static float LOWER_LEG_LENGTH = 0.45f;
    
    //Arm variable
    private final static float ARM_WIDTH = 0.1f;
    private final static float ARM_UP_LENGTH = 0.25f;
    private final static float ARM_LOW_LENGTH = 0.35f;
   
    //Set the speed mouvement (public so we can modify it)
    public float speed = 50;
        

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material) {
        this.material = material;   
    }

     /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        
        // Set the material/lights properties 
        gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, this.material.diffuse, 0);
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, this.material.specular, 0);
        gl.glMaterialf(GL_FRONT, GL_SHININESS, this.material.shininess);
        
        //draw the body and then the head (hierchay)
        drawBody(gl,glu,glut,tAnim);
        
        // Compute offset to draw arm and leg at the correct position
        float armOff = ARM_WIDTH/2.0f + BODY_WIDTH/2.0f;
        float legOff = BODY_WIDTH/2.0f - LEG_WIDTH/2.0f;
        
        // Now we will draw the legs and then the arms of the robot
        //draw right leg
        drawLeg(gl,glu,glut,tAnim,legOff);
        //draw legt leg
        drawLeg(gl,glu,glut,tAnim,-legOff);
        
        
        //draw right arm
        drawArm(gl,glu,glut,tAnim,armOff);
        //draw left arm
        drawArm(gl,glu,glut,tAnim,-armOff);
     
    }
    
    public void drawBody(GL2 gl, GLU glu, GLUT glut, float tAnim){
        
        gl.glPushMatrix();
        //Texture does not work.. but material does
        ShaderPrograms.robotShader.useProgram(gl);
        Textures.torso.enable(gl);
        Textures.torso.bind(gl);

        //Scale using the body dimension
        gl.glScalef(BODY_WIDTH,BODY_THICKNESS,BODY_HEIGHT);
        
        //draw the body/torso
        glut.glutSolidCube(1f);
        
        //undo the scaling
        gl.glScalef(1.0f/BODY_WIDTH,1.0f/BODY_THICKNESS,1.0f/BODY_HEIGHT);
        
        //Then we set coordinates at the top of the body, so we can then draw the head
        gl.glTranslatef(0f, 0f,BODY_HEIGHT/2.0f + 0.1f);
        
        //disable the dorso texture (does not work anyway)
        Textures.torso.disable(gl);

        // Draw the head (call the correct method to draw it)
        drawHead(gl,glu,glut,tAnim);
        
        gl.glPopMatrix();        
    }
   
    public void drawHead(GL2 gl, GLU glu, GLUT glut, float tAnim){
      
        //Draw a sphere that represents the head
        gl.glPushMatrix();
        //Texture does not work.. but materials does
        ShaderPrograms.robotShader.useProgram(gl);
        Textures.head.enable(gl);
        Textures.head.bind(gl);
        
        glut.glutSolidSphere(HEAD_RADIUS, 20, 20); // draw the sphere/head
        
        //disable the head texture (does not work anyway)
        Textures.head.disable(gl);
        
        gl.glPopMatrix();
      
    }
    
    public void drawArm(GL2 gl, GLU glu, GLUT glut, float tAnim,float offset){
      
        gl.glPushMatrix();
        
        //We set our position to the correct place (the place where we want to draw the arm
        gl.glTranslatef(offset, 0f,BODY_HEIGHT/2.0f);
        
        // Here we check the value of the offset (positive or negative) to know if we are drawing the right ou left arm
        // and the apply a correct rotation
        
        // if the offset is positive, we draw the left arm
        if(offset > 0) {
            rotationArm(gl,tAnim/10, Vector.X,-1);
        }
        // else, if it's negative we draw the right arm
        else {
            rotationArm(gl,tAnim/10, Vector.X,1);
        }
        
        //Finally we draw the arm (the first part)
        drawArm2(gl,glu,glut,tAnim,offset);
    
        gl.glPopMatrix();        
        
    }
    
    
    private void rotationArm(GL2 gl, float tAnim, Vector axis, int direction) {
        
        //Here, depending of wich arm we are drawing we do a rotation in the correct direction
        gl.glRotated((45*direction)*Math.sin(tAnim*speed), axis.x(), axis.y(), axis.z());
        
    }
     
     
    private void drawArm2(GL2 gl, GLU glu, GLUT glut, float tAnim, float offset){
        
        // We draw the upper part of the arm
        gl.glTranslatef(0f, 0f,-ARM_UP_LENGTH/2);
        gl.glScalef(ARM_WIDTH,ARM_WIDTH,ARM_UP_LENGTH);
        glut.glutSolidCube(1f);
        gl.glScalef(1.0f/ARM_WIDTH,1.0f/ARM_WIDTH,1.0f/ARM_UP_LENGTH);
        
        //Then we put the position to the end of the upper part of our arm, so we can then draw the lower part (2nd part)
        gl.glTranslatef(0f, 0f,-ARM_UP_LENGTH/2);
        gl.glRotated(30, 1.0, 0.0, 0.0); //apply a rotation so it create an "elbow" effect
        
        //And we continue to draw the arm (the second part)
        drawArm3(gl,glu,glut,tAnim,offset);
       
    }
    
    private void drawArm3(GL2 gl, GLU glu, GLUT glut, float tAnim, float offset){
        
        // We draw the lower part of our arm
        gl.glTranslatef(0f, 0f,-ARM_LOW_LENGTH/2);
        gl.glScalef(ARM_WIDTH,ARM_WIDTH,ARM_LOW_LENGTH);
        glut.glutSolidCube(1f);
    }
    
    private void drawLeg(GL2 gl, GLU glu, GLUT glut, float tAnim,float offset){

        gl.glPushMatrix();
        
        // Set te position where we want to draw the leg
        gl.glTranslatef(offset, 0f,-BODY_HEIGHT/2.0f);
        
        
        // (same process than with the arm)
        // Here we check the value of the offset (positive or negative) to know if we are drawing the right ou left leg
        // and the apply a correct rotation effect
        // if the offset is positive, we draw the left leg
        if(offset > 0){
                rotationLeg(tAnim/10, Vector.X,gl,1);
        }
        // else we draw the right leg
        else{
            rotationLeg(tAnim/10, Vector.X,gl,-1);
        }
        
        //Finally we draw the leg (upper part first)
        drawLeg2(gl,glu,glut,tAnim,offset);
    
        gl.glPopMatrix();
    }
    
    private void rotationLeg(float t, Vector axis, GL2 gl, int direction)
    {
        gl.glRotated(Math.sin(t * speed) * (45*direction), axis.x(), axis.y(), axis.z());
    
    }
    
    
    private void drawLeg2(GL2 gl, GLU glu, GLUT glut, float tAnim, float offset){
        
        //Draw the upper part of the leg
        gl.glTranslatef(0f, 0f,-UPPER_LEG_LEGTH/2);
        gl.glScalef(LEG_WIDTH,LEG_WIDTH,UPPER_LEG_LEGTH);
        glut.glutSolidCube(1f);
        gl.glScalef(1.0f/LEG_WIDTH,1.0f/LEG_WIDTH,1.0f/UPPER_LEG_LEGTH);
        
        //Set the position to the correct place (end of the upper leg part) so we can draw the rest
        gl.glTranslatef(0f, 0f,-UPPER_LEG_LEGTH/2);
        
        gl.glRotated(-30, 1.0, 0.0, 0.0); // do a rotation so it seems like there is a knee and not just stick for the leg
        
        // draw the lower part of the leg
        drawLeg3(gl,glu,glut,tAnim,offset);
       
    }
    
    public void drawLeg3(GL2 gl, GLU glu, GLUT glut, float tAnim,float offset){
        //Finally we draw the lower part of the leg
        gl.glTranslatef(0f, 0f,-LOWER_LEG_LENGTH/2);
        gl.glScalef(LEG_WIDTH,LEG_WIDTH,LOWER_LEG_LENGTH);
        glut.glutSolidCube(1f);
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public float getSpeed(){
        return this.speed;
    }
    
}