#version 120
varying vec3 P;
void main()
{
    if (P.z < 0.0) {
        gl_FragColor = vec4(0.46, 0.78, 0.90, 1); 
    }
    else if (P.z < 0.5){
        gl_FragColor = vec4(0.90, 0.85, 0.73, 1);
    }
    else {
        gl_FragColor = vec4(0.0, 0.53, 0.04, 1);
    }
}