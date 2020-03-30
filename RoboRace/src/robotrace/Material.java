package robotrace;

/**
* Materials that can be used for the robots.
*/
public enum Material {

    /**
     * Gold material properties.
     * Modify the default values to make it look like gold.
     */
    GOLD (

            new float[] {0.75164f, 0.60648f, 0.22648f, 1},
            new float[] {0.628281f, 0.555802f, 0.366065f, 1},
            0.4f*128.0f

    ),

    /**
     * Silver material properties.
     * Modify the default values to make it look like silver.
     */
    SILVER (
            new float[] {0.780392f, 0.568627f, 0.113725f, 1},
            new float[] {0.992157f, 0.941176f, 0.807843f, 1},
            0.21794872f*128f
    ),

    /**
     * Orange material properties.
     * Modify the default values to make it look like orange.
     */
    ORANGE (

            new float[] {0.5f, 0.5f, 0.4f, 1},
            new float[] {0.7f, 0.7f, 0.04f, 1},
            0.078215f*128f

    ),

    /**
     * Wood material properties.
     * Modify the default values to make it look like Wood.
     */
    WOOD (

            new float[] {050754f, 0.50754f, 0.50754f, 1},
            new float[] {0.508273f, 0.508273f, 0.508273f, 1},
            0.4f*128.0f



    );

    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] diffuse, float[] specular, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}
