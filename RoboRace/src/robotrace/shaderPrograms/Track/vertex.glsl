#version 120
uniform bool ambient, diffuse, specular;
uniform vec4 facecolor;
varying vec3 N;
varying vec3 v;

void main() {
    
	v = vec3(gl_ModelViewMatrix * gl_Vertex);
	N = normalize(gl_NormalMatrix * gl_Normal);
	
    gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
	gl_FrontColor = facecolor*gl_Color;
	gl_TexCoord[0] = gl_MultiTexCoord0;
} 
