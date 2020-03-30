#version 120

uniform float time;
varying vec3 N;
varying vec3 v;

void main()
{	
        
        // L is the light vector
	vec3 L = normalize(gl_LightSource[0].position.xyz - v);
        // E is the eye position vector
	vec3 E = normalize(-v);
        // R is the reflection (light out vector)
	vec3 R = normalize(-reflect(L,N));

	//compute ambiant light
	vec4 Iamb = gl_LightSource[0].ambient*gl_FrontMaterial.ambient;
	//compute diffuse light
	vec4 Idiff = gl_FrontLightProduct[0].diffuse*max(dot(N,L),0.0);
	//compute specular light
	vec4 Ispec = gl_FrontLightProduct[0].specular * pow(max(dot(R,E),0.0),0.3*gl_FrontMaterial.shininess);
	Ispec = clamp(Ispec,0.0,1.0);


	gl_FragColor =gl_FrontLightModelProduct.sceneColor + Iamb + Idiff + Ispec;
	
}