package robotrace;

import static com.jogamp.opengl.GL2.*;
import static robotrace.ShaderPrograms.*;
import static robotrace.Textures.*;

//to generate random number
import java.util.Random;

/**
 * Handles all of the RobotRace graphics functionality,
 * which should be extended per the assignment.
 * 
 * OpenGL functionality:
 * - Basic commands are called via the gl object;
 * - Utility commands are called via the glu and
 *   glut objects;
 * 
 * GlobalState:
 * The gs object contains the GlobalState as described
 * in the assignment:
 * - The camera viewpoint angles, phi and theta, are
 *   changed interactively by holding the left mouse
 *   button and dragging;
 * - The camera view width, vWidth, is changed
 *   interactively by holding the right mouse button
 *   and dragging upwards or downwards; (Not required in this assignment)
 * - The center point can be moved up and down by
 *   pressing the 'q' and 'z' keys, forwards and
 *   backwards with the 'w' and 's' keys, and
 *   left and right with the 'a' and 'd' keys;
 * - Other settings are changed via the menus
 *   at the top of the screen.
 * 
 * Textures:
 * Place your "track.jpg", "brick.jpg", "head.jpg",
 * and "torso.jpg" files in the folder textures. 
 * These will then be loaded as the texture
 * objects track, bricks, head, and torso respectively.
 * Be aware, these objects are already defined and
 * cannot be used for other purposes. The texture
 * objects can be used as follows:
 * 
 * gl.glColor3f(1f, 1f, 1f);
 * Textures.track.bind(gl);
 * gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0);
 * gl.glVertex3d(0, 0, 0);
 * gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0);
 * gl.glTexCoord2d(1, 1);
 * gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1);
 * gl.glVertex3d(0, 1, 0);
 * gl.glEnd(); 
 * 
 * Note that it is hard or impossible to texture
 * objects drawn with GLUT. Either define the
 * primitives of the object yourself (as seen
 * above) or add additional textured primitives
 * to the GLUT object.
 */
public class RobotRace extends Base {
    
    /** Array of the four robots. */
    private final Robot[] robots;
    
    /** Instance of the camera. */
    private final Camera camera;
    
    /** Instance of the race track. */
    private final RaceTrack[] raceTracks;
    
    /** Instance of the terrain. */
    private final Terrain terrain;
        
    //Usefule variable
    // Number of robots in the race
    private static final int NBR_ROBOTS = 4;
    
    //distance travelled by each robots
    private double[] distances = new double[] {0,0,0,0};
    
    //speed of each robots
    private double[] speeds = new double[] {0.01,0.01,0.01,0.01};
    
    private static final double MIN_SPEED = 0.01;
    private static final double MAX_SPEED = 0.015;
    
    private boolean speedChange = true;
    /**
     * Constructs this robot race by initializing robots,
     * camera, track, and terrain.
     */
    public RobotRace() {
        
        // Create a new array of four robots
        robots = new Robot[4];
        
        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD
                
        );
        
        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER
              
        );
        
        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD
              
        );

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE
                
        );
        
        // Initialize the camera
        camera = new Camera();
        
        // Initialize the race tracks
        raceTracks = new RaceTrack[2];
        
        // Track 1
        raceTracks[0] = new ParametricTrack();
        
        // Track 2
        float g = 3.5f;
        raceTracks[1] = new BezierTrack(
                
             new Vector[] {
                        new Vector(20,0,1),
                        new Vector(20,5,1),
                        new Vector(20,10,1),
                        new Vector(20,15,1),

                        new Vector(20,15,1),
                        new Vector(20,20,1),
                        new Vector(15,20,1),
                        new Vector(10,20,1),

                        new Vector(10,20,1),
                        new Vector(7,20,1),
                        new Vector(3,20,1),
                        new Vector(0,20,1),

                        new Vector(0,20,1),
                        new Vector(-5,20,1),
                        new Vector(-5,15,1),
                        new Vector(-5,10,1),

                        new Vector(-5,10,1),
                        new Vector(-5,6,1),
                        new Vector(-2,3,1),
                        new Vector(-2,0,1),

                        new Vector(-2,0,1),
                        new Vector(-2,-1,1),
                        new Vector(-2,-2,1),
                        new Vector(-2,-4,1),

                        new Vector(-2,-4,1),
                        new Vector(-2,-9,1),
                        new Vector(-4,-11,1),
                        new Vector(-9, -11, 1),

                        new Vector(-9,-11,1),
                        new Vector(-14,-11,1),
                        new Vector(-16,-9,1),
                        new Vector(-16, -4,1),

                        new Vector(-16,-4,1),
                        new Vector(-16, 1,1),
                        new Vector(-14, 3,1),
                        new Vector(-9, 3,1),

                        new Vector(-9,3,1),
                        new Vector(-4, 3,1),
                        new Vector(-1, 3,1),
                        new Vector(4, 3,1),

                        new Vector(4,3,1),
                        new Vector(9, 3,1),
                        new Vector(11, 1,1),
                        new Vector(11, -4,1),

                        new Vector(11,-4,1),
                        new Vector(11, -9,1),
                        new Vector(20, -7,1),
                        new Vector(20, 0,1),
                });

        
        // Initialize the terrain
        terrain = new Terrain();
    }
    
    /**
     * Called upon the start of the application.
     * Primarily used to configure OpenGL.
     */
    @Override
    public void initialize() {
		
        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                
        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);
		
        // Enable face culling for improved performance
        // gl.glCullFace(GL_BACK);
        // gl.glEnable(GL_CULL_FACE);
        
	    // Normalize normals.
        gl.glEnable(GL_NORMALIZE);
        
	// Try to load four textures, add more if you like in the Textures class         
        Textures.loadTextures();
        reportError("reading textures");
        
        // Try to load and set up shader programs
        ShaderPrograms.setupShaders(gl, glu);
        reportError("shaderProgram");
        
    }
   
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);
        
        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        glu.gluPerspective(45, (float)gs.w / (float)gs.h, 0.1*gs.vDist, 10*gs.vDist);
        
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // Add light source
        gl.glLightfv(GL_LIGHT0, GL_POSITION, new float[]{0f,0f,0f,1f}, 0);
               
        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots[0]);
        glu.gluLookAt(camera.eye.x(),    camera.eye.y(),    camera.eye.z(),
                      camera.center.x(), camera.center.y(), camera.center.z(),
                      camera.up.x(),     camera.up.y(),     camera.up.z());
    }
    
    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        
        gl.glUseProgram(defaultShader.getProgramID());
        reportError("program");
        
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);
        
        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);
        
        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        

        //Light sources
        // Use smooth shading
        gl.glShadeModel(GL_SMOOTH);
        // Enable lighting
        gl.glEnable(GL_LIGHTING);
        // Enable light source #0
        gl.glEnable(GL_LIGHT0);
        
        
    // Draw hierarchy example.
        //drawHierarchy();
        
        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }
        
        //drawTree(0,0,0);
        drawTree(25,25,0);
        drawTree(-25,-25,0);
        drawTree(-25,25,0);
        drawTree(25,-25,0);
                
        // Set parameters for each robots (speed, position etc.)
        for(int i =0; i< NBR_ROBOTS;i++){
        
            //first we update the distance traveled
            distances[i] += speeds[i]/5;
            
            //if the distance traveled is > 1
            if(distances[i] >= 1){
                
                //We reset the distance
                distances[i] -=Math.floor(distances[i]);
                
            }
            
            // We set a random speed to each ro
            if (gs.tAnim == 0){
                
                Random random = new Random();
                speeds[i] = MIN_SPEED + (MAX_SPEED-MIN_SPEED)*random.nextDouble();
            }
            
            // Here we set the position and direction of our robots
            if(gs.trackNr == 0) {
                robots[i].position =  raceTracks[0].getLanePoint(i, speeds[i] * (double)gs.tAnim);
                robots[i].direction = raceTracks[0].getLaneTangent(i, speeds[i] * (double)gs.tAnim).normalized();
            } else {
                robots[i].position =  raceTracks[1].getLanePoint(i, speeds[i] * (double)gs.tAnim);
                robots[i].direction = raceTracks[1].getLaneTangent(i, speeds[i] * (double)gs.tAnim).normalized();
            }
        }
        
        
        gl.glUseProgram(robotShader.getProgramID());                 
        
        // We position the robots and draw them
        for(int i =0; i < NBR_ROBOTS; i++){

            gl.glPushMatrix();
            
            //First we get info about the position
            double x = robots[i].position.x;
            double y = robots[i].position.y;
            double z = robots[i].position.z;
            
            //Then we compute the direction that we will use later
            Vector direction = robots[i].direction.normalized();
            
            // The angle of rotation we will apply
            double alpha;
            
            //Depending of the actual direction of the robots we campute the angle of rotation alpha that we should apply
            if(robots[i].direction.x() >=0){
                alpha = -Math.acos(direction.dot(Vector.Y));
            }else{
                alpha = Math.acos(direction.dot(Vector.Y));
            }
            
            // Now we set the robots on the track, and rotate it around the z-axis of alpha degrees
            gl.glTranslated(x, y, z+1.05);
            gl.glRotated(Math.toDegrees(alpha), 0, 0, 1);
            
            // Finally we draw the robots
            robots[i].draw(gl, glu, glut, gs.tAnim);
            
            gl.glPopMatrix();  
        }
        
        
        
        
        // Draw the race track.
        gl.glUseProgram(trackShader.getProgramID());
        raceTracks[gs.trackNr].draw(gl, glu, glut);
        
        // Draw the terrain.
        gl.glUseProgram(terrainShader.getProgramID());
        terrain.draw(gl, glu, glut);
        reportError("terrain:");    
        
        
    }
    
    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue),
     * and origin (yellow).
     */
    public void drawAxisFrame() {
        
        float sphereRadius = 0.10f;
        
        gl.glColor3f(1f, 1f, 0f); // yellow color for the sphere
        glut.glutSolidSphere(sphereRadius, 24, 12); // draw the sphere at the origin
        
        // Draw the first arrow (x-axis in red)
        gl.glColor3f(1f,0f,0f); // red color
        // Put the arrow on a matrix so we can rotate it
        gl.glPushMatrix();
        gl.glRotatef(90f, 0f, 1f, 0f);
        drawArrow();
        gl.glPopMatrix();
        
        // Draw the second arrow (y-axis in green)
        gl.glColor3f(0f, 1f, 0f); // green color
        gl.glPushMatrix();
        gl.glRotatef(-90f, 1f, 0f, 0f);
        drawArrow();
        gl.glPopMatrix();
        
        // Draaw the third arrow (z-axis in blue)
        // (no need to rotate the arrow so no need to put it in a matrix)
        gl.glColor3f(0f, 0f, 1f); // blue color
        drawArrow();
    }
    
    /**
     * Draws a single arrow
     */
    public void drawArrow() { 
        
        //Useful variable to draw the cylinder and the cone of the arraw
        float heightCylinder = 1f;
        float heightCone = heightCylinder/3;
        float radius = 0.03f;
        float base = 0.1f;
         
        // We construct a cylinder and a cone that we put together to create an arrow
        glut.glutSolidCylinder(radius, heightCylinder, 12, 6); //the cylinder
        gl.glTranslatef(0f, 0f, heightCylinder);
        glut.glutSolidCone(base, heightCone, 12, 6);//the cone
        
    }
    
    // Method that draw a tree at position (x,y,z)
    public void drawTree(int x, int y, int z) {
        // The tree is compose of a cylinder for the stem, and a cone for the foliage, so it looks like a christmas tree
        
        gl.glPushMatrix();
        gl.glTranslated(x,y,z); // set the position where we want to draw the tree
        gl.glColor3f(0,0,0); // black for the stem
        glut.glutSolidCylinder(1, 4, 12, 6); //the cylinder
        gl.glColor3f(0,1f,0); //green for the foliage
        gl.glTranslatef(0f, 0f, 4); // set the position where we want to draw the foliage
        glut.glutSolidCone(4, 10, 12, 6); //the cone
        gl.glPopMatrix();
    }
    
 
    /**
     * Drawing hierarchy example.
     * 
     * This method draws an "arm" which can be animated using the sliders in the
     * RobotRace interface. The A and B sliders rotate the different joints of
     * the arm, while the C, D and E sliders set the R, G and B components of
     * the color of the arm respectively. 
     * 
     * The way that the "arm" is drawn (by calling {@link #drawSecond()}, which 
     * in turn calls {@link #drawThird()} imposes the following hierarchy:
     * 
     * {@link #drawHierarchy()} -> {@link #drawSecond()} -> {@link #drawThird()}
     */
    private void drawHierarchy() {
        gl.glColor3d(gs.sliderC, gs.sliderD, gs.sliderE);
        gl.glPushMatrix(); 
            gl.glScaled(1, 0.5, 0.5);
            glut.glutSolidCube(1);
            gl.glScaled(1, 2, 2);
            gl.glTranslated(0.5, 0, 0);
            gl.glRotated(gs.sliderA * -90.0, 0, 1, 0);
            drawSecond();
        gl.glPopMatrix();
    }
    
    private void drawSecond() {
        gl.glTranslated(0.5, 0, 0);
        gl.glScaled(1, 0.5, 0.5);
        glut.glutSolidCube(1);
        gl.glScaled(1, 2, 2);
        gl.glTranslated(0.5, 0, 0);
        gl.glRotated(gs.sliderB * -90.0, 0, 1, 0);
        drawThird();
    }
    
    private void drawThird() {
        gl.glTranslated(0.5, 0, 0);
        gl.glScaled(1, 0.5, 0.5);
        glut.glutSolidCube(1);
    }
    
    
    /**
     * Main program execution body, delegates to an instance of
     * the RobotRace implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    }
}
