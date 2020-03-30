#version 120
varying vec3 P;
void main()
{
    float x = gl_Vertex.x;
    float y =  gl_Vertex.y;
    float z = 0.6 * cos(0.3 * x + 0.2 * y) + 0.4 * cos(x - 0.5 * y);
    P = vec3(x,y,z);
    gl_Position    = gl_ModelViewProjectionMatrix * vec4(x, y, z,1);
}