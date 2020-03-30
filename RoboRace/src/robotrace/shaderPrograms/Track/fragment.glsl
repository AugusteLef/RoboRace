#version 120
uniform sampler2D baseImage;
//simple fragment shader

//'time' contains seconds since the program was linked.
uniform float time;
varying vec3 N;
varying vec3 v;

void main()
{	
	vec3 L = normalize(gl_LightSource[0].position.xyz - v);//Light vector
	vec3 E = normalize(-v); // Eyeposition is (0,0,0)
	vec3 R = normalize(-reflect(L,N)); //Light Out vector

	//Ambient
	vec4 Iamb = gl_LightSource[0].ambient*gl_FrontMaterial.ambient;
	//Diffuse
	vec4 Idiff = gl_FrontLightProduct[0].diffuse*max(dot(N,L),0.0);
	//Specular
	vec4 Ispec = gl_FrontLightProduct[0].specular * pow(max(dot(R,E),0.0),0.3*gl_FrontMaterial.shininess);
	Ispec = clamp(Ispec,0.0,1.0);


	gl_FragColor = 0.6*(texture2D( baseImage, gl_TexCoord[0].xy)) + 0.4*(gl_FrontLightModelProduct.sceneColor + Iamb + Idiff + Ispec);
	
}
