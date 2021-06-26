#version 120




//to increase shadow draw distance, edit SHADOWDISTANCE and SHADOWHPL below. Both should be equal. Needs decimal point.
//disabling is done by adding "//" to the beginning of a line.





//ADJUSTABLE VARIABLES

#define BLURFACTOR 3.5
#define SHADOW_DARKNESS 0.625
#define SUNLIGHTAMOUNT 1.1
#define SHADOWDISTANCE 110.0 
#define SHADOW_CLAMP 0.5
#define SHADOW_RES 1024

/* SHADOWRES:1024 */
/* SHADOWHPL:75.0 */


  #define NightVision
  #define SSAO
  #define SSAO_LUMINANCE 0.0				// At what luminance will SSAO's shadows become highlights.
  #define SSAO_STRENGTH 1.75               // Too much strength causes white highlights on extruding edges and behind objects
  #define SSAO_LOOP 1						// Integer affecting samples that are taken to calculate SSAO. Higher values mean more accurate shadowing but bigger performance impact
  #define SSAO_MAX_DEPTH 0.75				// View distance of SSAO
  #define SSAO_SAMPLE_DELTA 0.4			// Radius of SSAO shadows. Higher values cause more performance hit.
  #define CORRECTSHADOWCOLORS				// Colors sunlight and ambient light correctly according to real-life. 
  #define SHADOWOFFSET 0.0				// Shadow offset multiplier. Values that are too low will cause artefacts.
  //#define GODRAYS
  #define GODRAYS_EXPOSURE 0.10
  #define GODRAYS_SAMPLES 6
  #define GODRAYS_DECAY 0.95
  #define GODRAYS_DENSITY 0.65

  #define SECONDLPASS

  
  #define BUMPMAPPWR 0.3			//fine for x64 texturepack, better if you lower this when using lower res texturepacks
  //#define CLAY_RENDER

//#define PRESERVE_COLOR_RANGE


//END OF ADJUSTABLE VARIABLES






uniform sampler2D gcolor;
uniform sampler2D gdepth;
uniform sampler2D gnormal;

uniform sampler2D shadow;
uniform sampler2D gaux1;
uniform sampler2D gaux2;
uniform sampler2D gaux3;

varying vec4 texcoord;
varying vec4 lmcoord;
varying vec3 lightVector;

uniform int worldTime;

uniform mat4 gbufferProjection;
uniform mat4 gbufferProjectionInverse;
uniform mat4 gbufferModelViewInverse;
uniform mat4 shadowProjection;
uniform mat4 shadowModelView;
uniform vec3 sunPosition;

//attribute vec4 mc_Entity;

uniform float near;
uniform float far;
uniform float viewWidth;
uniform float viewHeight;
uniform float rainStrength;
uniform float wetness;
uniform float aspectRatio;
uniform float nightVision;


//Calculate Time of Day

varying float TimeMidnight;
varying float TimeSunset;
varying float TimeNoon;
varying float TimeSunrise;

//colors
varying vec3 reflected_color;
varying vec3 sunlight_color;
varying vec3 ambfill_color;
varying vec3 ambient_color;
varying vec3 skycolor;
float totalspec;

float edepth(vec2 coord) {
return texture2D(gdepth,coord).z;
}
float luma(vec3 color) {
return dot(color.rgb,vec3(0.299, 0.587, 0.114));
}
float ld(float depth) {
    return (2.0 * near) / (far + near - depth * (far - near));
}

vec2 texel = vec2(1.0/viewWidth,1.0/viewHeight);
//Auxilliary variables
vec3 aux1 = texture2D(gaux1, texcoord.st).rgb;
vec3 specularity = texture2D(gaux3, texcoord.st).rgb;
float	land 			 = aux1.b;
//float	noblur 			 = texture2D(gaux1, texcoord.st).r;
vec3	sunPos			 = sunPosition;
vec2 	Texcoord2		 = texcoord.st;
float 	iswater			 = aux1.g;
vec3 	normal         	 = texture2D(gnormal, texcoord.st).rgb * 2.0f - 1.0f;
float 	translucent		 = aux1.r;
float   texshading       = 1.0f;
vec2  pixeldepth = texture2D(gdepth,texcoord.xy).xz;

float   isice 			 = specularity.b;
float	specularityDry   = specularity.r;
float 	specularityWet   = specularity.g;

//Crossfading conditionals

float rainx = clamp(rainStrength, 0.0f, 1.0f);
float wetx  = clamp(wetness, 0.0f, 1.0f);
float landx = land+translucent;

//Lightmaps

vec3 lmap = texture2D(gaux2,texcoord.st).rgb;
float sky_lightmap = lmap.g;
float torch_lightmap = pow(lmap.r,1.5);
float lightning_lightmap = 0.0;








#ifdef SSAO

// Alternate projected depth (used by SSAO, probably AA too)
float getProDepth(vec2 coord) {
	float depth = texture2D(gdepth, coord).x;
	return ( 2.0f * near ) / ( far + near - depth * ( far - near ) );
}

float znear = near; //Z-near
float zfar = far; //Z-far

float diffarea = 0.6f; //self-shadowing reduction
float gdisplace = 0.35f; //gauss bell center

//bool noise = SSAO_NOISE; //use noise instead of pattern for sample dithering?
bool onlyAO = false; //use only ambient occlusion pass?

vec2 texCoord = texcoord.st;



float compareDepths(in float depth1, in float depth2) {  
  float garea = 8.5f; //gauss bell width    
  float diff = (depth1 - depth2) * 100.0f; //depth difference (0-100)
  //reduce left bell width to avoid self-shadowing 
  
  if (diff < gdisplace) {
    garea = diffarea;
  } 


  float gauss = pow(2.7182f,-2.0f*(diff-gdisplace)*(diff-gdisplace)/(garea*garea));
  return gauss;
} 

float calAO(float depth, vec2 coord) {  
  float temp = 0.0f;
  vec2 coord2 = texcoord.xy + coord/(depth*0.2f + 0.1f);

    temp = compareDepths(depth, getProDepth(coord2));


  return temp;  
}  



float getSSAOFactor() {

  vec2 inc = texel*0.8;
  
	float depth = ld(pixeldepth.x);
	
  if (depth > SSAO_MAX_DEPTH) {
    return 1.0f;
  }
  float cdepth = pixeldepth.x;
	
	float ao = 0.0;
	float s;
	


  float aoMult = SSAO_STRENGTH;


    vec2 np = inc/cdepth;
	
	float noiseX4 = clamp(fract(sin(dot(Texcoord2 ,vec2(16.9898f,38.633f))) * 41178.5453f),0.0f,1.0f)*2.0f-1.0f;
						

	

    ao += calAO(depth, np) * aoMult;
    ao += calAO(depth, vec2(np.x,-np.y)) * aoMult;
    ao += calAO(depth, vec2(-np.x,np.y)) * aoMult;
    ao += calAO(depth, -np) * aoMult;
	
	ao += calAO(depth, np*2.0) * aoMult/1.5;
    ao += calAO(depth, vec2(np.x,-np.y)*2.0) * aoMult/1.5;
    ao += calAO(depth, vec2(-np.x,np.y)*2.0) * aoMult/1.5;
    ao += calAO(depth, -np*2.0) * aoMult/1.5;
	
    ao += calAO(depth, np*3.0*noiseX4) * aoMult/2.0;
    ao += calAO(depth, vec2(np.x,-np.y)*3.0) * aoMult/2.0;
    ao += calAO(depth, vec2(-np.x,np.y)*3.0) * aoMult/2.0;
    ao += calAO(depth, -np*3.0*noiseX4) * aoMult/2.0;
	
	
	
	ao /= 16.0f;
	ao = 1.0f-ao;	
  ao = clamp(ao, 0.0f, 0.5f) * 2.0f;
	
  return ao;
}

#endif


#ifdef GODRAYS



	float addGodRays(in float nc, in vec2 tx, in float noise, in float noise2, in float noise3, in float noise4, in float noise5) {
			float GDTimeMult = 0.0f;
			if (sunPos.z > 0.0f) {
				sunPos.z = -sunPos.z;
				sunPos.x = -sunPos.x;
				sunPos.y = -sunPos.y;
				GDTimeMult = TimeMidnight;
			} else {
				GDTimeMult = TimeSunrise + TimeNoon + TimeSunset;
			}
			vec4 tpos = vec4(sunPos,1.0)*gbufferProjection;
			tpos = vec4(tpos.xyz/tpos.w,1.0);
			vec2 lightPos = tpos.xy/tpos.z;
			lightPos = (lightPos + 1.0f)/2.0f;
			if (lightPos.x > 1.0f && lightPos.x < 0.0f && lightPos.y > 1.0f && lightPos.y < 0.0f) return 0.0;
			//vec2 coord = tx;
			vec2 delta = (tx - lightPos) * GODRAYS_DENSITY / 2.0;
			delta *= -sunPos.z*0.01f;
			//delta *= -sunPos.z*0.01;
			float decay = -sunPos.z / 100.0f;
				 // decay *= -sunPos.z*0.01;
			float colorGD = 0.0f;
			for (int i = 0; i<2;i++) {
			

				
			
			
				tx -= delta;
				float sample = 0.0f;

					sample = step(texture2D(gaux1, clamp(tx + delta*noise,0.000001,0.999999)).b,0.01);
					sample += step(texture2D(gaux1, clamp(tx + delta*noise2,0.000001,0.999999)).b,0.01);
					sample += step(texture2D(gaux1, clamp(tx + delta*noise3,0.000001,0.999999)).b,0.01);
					sample += step(texture2D(gaux1, clamp(tx + delta*noise4,0.000001,0.999999)).b,0.01);
					sample += step(texture2D(gaux1, clamp(tx + delta*noise5,0.000001,0.999999)).b,0.01);
				sample *= decay;

					colorGD += sample;
					decay *= GODRAYS_DECAY;
			
			}
			float bubble = distance(vec2(delta.x*aspectRatio, delta.y), vec2(0.0f, 0.0f))*4.0f;
				  bubble = clamp(bubble, 0.0f, 1.0f);
				  bubble = 1.0f - bubble;
				  
			return (nc + GODRAYS_EXPOSURE * (colorGD*bubble))*GDTimeMult;
       
	}
#endif 


//
float callwaves(vec3 pos) {
float wsize = 2.5;
float wspeed = 0.3f;

float rs0 = abs(sin((worldTime*wspeed/5.0) + (pos.s*wsize) * 20.0 + (pos.z*4.0))+0.2);
float rs1 = abs(sin((worldTime*wspeed/7.0) + (pos.t*wsize) * 27.0));
float rs2 = abs(sin((worldTime*wspeed/2.0) + (pos.t*wsize) * 60.0 - sin(pos.s*wsize) * 13.0)+0.4);
float rs3 = abs(sin((worldTime*wspeed/1.0) - (pos.s*wsize) * 20.0 + cos(pos.t*wsize) * 83.0)+0.1);

float wsize2 = 1.2;
float wspeed2 = 0.2f;

float rs0a = abs(sin((worldTime*wspeed2/4.0) + (pos.s*wsize2) * 24.0));
float rs1a = abs(sin((worldTime*wspeed2/11.0) + (pos.t*wsize2) * 77.0  - (pos.z*6.0))+0.3);
float rs2a = abs(sin((worldTime*wspeed2/6.0) + (pos.s*wsize2) * 50.0 - (pos.t*wsize2) * 23.0)+0.12);
float rs3a = abs(sin((worldTime*wspeed2/14.0) - (pos.t*wsize2) * 4.0 + (pos.s*wsize2) * 98.0));

float wsize3 = 0.28;
float wspeed3 = 0.3f;

float rs0b = abs(sin((worldTime*wspeed3/4.0) + (pos.s*wsize3) * 14.0));
float rs1b = abs(sin((worldTime*wspeed3/11.0) + (pos.t*wsize3) * 37.0 + (pos.z*1.0)));
float rs2b = abs(sin((worldTime*wspeed3/6.0) + (pos.t*wsize3) * 47.0 - cos(pos.s*wsize3) * 33.0 + rs0a + rs0b));
float rs3b = abs(sin((worldTime*wspeed3/14.0) - (pos.s*wsize3) * 13.0 + sin(pos.t*wsize3) * 98.0 + rs0 + rs1));

float waves = (rs1 * rs0 + rs2 * rs3)/2.0f;
float waves2 = (rs0a * rs1a + rs2a * rs3a)/2.0f;
float waves3 = (rs0b + rs1b + rs2b + rs3b)*0.25;


return (waves + waves2 + waves3)/3.0f;
}





///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void main() {


float noiseamp = 0.3f;
					
						float width2 = 1.0f;
						float height2 = 1.0f;
						float noiseX2 = ((fract(1.0f-Texcoord2.s*(width2/2.0f))*0.25f)+(fract(Texcoord2.t*(height2/2.0f))*0.75f))*2.0f-1.0f;
						float noiseY2 = ((fract(1.0f-Texcoord2.s*(width2/2.0f))*0.75f)+(fract(Texcoord2.t*(height2/2.0f))*0.25f))*2.0f-1.0f;

						
							noiseX2 = clamp(fract(sin(dot(Texcoord2 ,vec2(12.9898f,78.233f))) * 43758.5453f),0.0f,1.0f)*2.0f-1.0f;
							noiseY2 = clamp(fract(sin(dot(Texcoord2 ,vec2(12.9898f,78.233f)*2.0f)) * 43758.5453f),0.0f,1.0f)*2.0f-1.0f;
						
						noiseX2 *= (0.0005f*noiseamp);
						noiseY2 *= (0.0005f*noiseamp);
						
						float width3 = 2.0f;
						float height3 = 2.0f;
						float noiseX3 = ((fract(1.0f-Texcoord2.s*(width3/2.0f))*0.25f)+(fract(Texcoord2.t*(height3/2.0f))*0.75f))*2.0f-1.0f;
						float noiseY3 = ((fract(1.0f-Texcoord2.s*(width3/2.0f))*0.75f)+(fract(Texcoord2.t*(height3/2.0f))*0.25f))*2.0f-1.0f;

						
							noiseX3 = clamp(fract(sin(dot(Texcoord2 ,vec2(18.9898f,28.633f))) * 4378.5453f),0.0f,1.0f)*2.0f-1.0f;
							noiseY3 = clamp(fract(sin(dot(Texcoord2 ,vec2(11.9898f,59.233f)*2.0f)) * 3758.5453f),0.0f,1.0f)*2.0f-1.0f;
						
						noiseX3 *= (0.0005f*noiseamp);
						noiseY3 *= (0.0005f*noiseamp);
						
						float width4 = 3.0f;
						float height4 = 3.0f;
						float noiseX4 = ((fract(1.0f-Texcoord2.s*(width4/2.0f))*0.25f)+(fract(Texcoord2.t*(height4/2.0f))*0.75f))*2.0f-1.0f;
						float noiseY4 = ((fract(1.0f-Texcoord2.s*(width4/2.0f))*0.75f)+(fract(Texcoord2.t*(height4/2.0f))*0.25f))*2.0f-1.0f;

						
							noiseX4 = clamp(fract(sin(dot(Texcoord2 ,vec2(16.9898f,38.633f))) * 41178.5453f),0.0f,1.0f)*2.0f-1.0f;
							noiseY4 = clamp(fract(sin(dot(Texcoord2 ,vec2(21.9898f,66.233f)*2.0f)) * 9758.5453f),0.0f,1.0f)*2.0f-1.0f;
						
						noiseX4 *= (0.0005f*noiseamp);
						noiseY4 *= (0.0005f*noiseamp);
						
												float width5 = 4.0f;
						float height5 = 4.0f;
						float noiseX5 = ((fract(1.0f-Texcoord2.s*(width5/2.0f))*0.25f)+(fract(Texcoord2.t*(height5/2.0f))*0.75f))*2.0f-1.0f;
						float noiseY5 = ((fract(1.0f-Texcoord2.s*(width5/2.0f))*0.75f)+(fract(Texcoord2.t*(height5/2.0f))*0.25f))*2.0f-1.0f;

						
							noiseX5 = clamp(fract(sin(dot(Texcoord2 ,vec2(11.9898f,68.633f))) * 21178.5453f),0.0f,1.0f)*2.0f-1.0f;
							noiseY5 = clamp(fract(sin(dot(Texcoord2 ,vec2(26.9898f,71.233f)*2.0f)) * 6958.5453f),0.0f,1.0f)*2.0f-1.0f;
						
						noiseX5 *= (0.0005f*noiseamp);
						noiseY5 *= (0.0005f*noiseamp);

//



	vec4 fragposition = gbufferProjectionInverse * vec4(texcoord.s * 2.0f - 1.0f, texcoord.t * 2.0f - 1.0f, 2.0f * pixeldepth.y - 1.0f, 1.0f);
	fragposition /= fragposition.w;
	
	#ifdef SHADOWDISTANCE
	float drawdistance = SHADOWDISTANCE;
	float drawdistancesquared = pow(drawdistance, 2.0f);
	#endif
	
	float dist = length(fragposition.xyz);

	float shading = 1.0f;
	float shadingsharp = 1.0f;
	float diffthresh = SHADOW_CLAMP;
	
	vec4 worldposition = vec4(0.0);
	vec4 worldpositionraw = vec4(0.0);
			
	worldposition = gbufferModelViewInverse * fragposition;	
	
	float xzDistanceSquared = worldposition.x * worldposition.x + worldposition.z * worldposition.z;
	float yDistanceSquared  = worldposition.y * worldposition.y;
	
	worldpositionraw = worldposition;
	
			worldposition = shadowModelView * worldposition;
			float comparedepth = -worldposition.z;
			worldposition = shadowProjection * worldposition;
			worldposition /= worldposition.w;
			
			worldposition.st = worldposition.st * 0.5f + 0.5f;
			
			float sample = 0.0;
			float isoccluded = 0.0;


	  
	 
float isshadow = 0.0;
	
	if (dist < drawdistance) {
		
		
		if (yDistanceSquared < drawdistancesquared) {
			

				
			if (comparedepth > 0.0f && worldposition.s < 1.0f && worldposition.s > 0.0f && worldposition.t < 1.0f && worldposition.t > 0.0f){
	  if (land > 0.1 && land < 0.3) { 
	  shading = 0.0;
	  }
				float shadowMult = clamp((drawdistance * 0.85f) - dist , 0.0f, 1.0f);
					 
				
					if (landx > 0.6) {
					//shadow filtering
					
					

			
					
						float smres = 1.0/SHADOW_RES;
						


					
							
					//determine shadow depth
					float shaddepth = 0.0;
					int sds = 0;
					
					vec2 coords;
					
					
					for (int i = 0; i < 3; i+=1) {
							for (int j = 0; j < 3; j+=1) {
							coords = worldposition.st + smres*vec2(i-1,j-1);
								sample = comparedepth - (0.05 + (texture2D(shadow, coords).z) * (256.0 - 0.05));
								 shaddepth += (clamp(sample, 0.0, 20.0f)/20.0f);
								 sample = 1.0-step((clamp(sample - 0.1, 0.0, 0.5) * 0.6 - 0.1),0.1);
								 isoccluded += sample;
								sds += 1;
							}
							
					}
					shaddepth /= sds;
					
					diffthresh = 3.9f * shaddepth  + 0.75f;
					shaddepth = shaddepth*0.5+0.025;
				
					
					int ssamp = 0;
					float shadspread = 1.7f ;
					float stx = -2.0*smres * shadspread;
					float sty = -2.0*smres * shadspread;
					float nx = 1.0;
					if (isoccluded > 0.1) {
					for (int i = 0; i < 5; ++i) {
						
						
							for (int j = 0; j < 5; ++j) {
								shadingsharp +=   clamp(shadowMult * (clamp(comparedepth - (0.05 + (texture2D(shadow, worldposition.st + vec2( stx+noiseX2,  sty+noiseX2)*shaddepth ).z) * (256.0 - 0.05)), 0.0, diffthresh)/diffthresh),0.0,1.0);
								ssamp += 1;
								sty += smres * shadspread;
							}
						sty = -2.0*smres * shadspread;
						stx += smres*shadspread;
					}
					
					shadingsharp /= ssamp;
					isshadow = 1.0-shadingsharp;				
										shading = isshadow;
}
						
}


			}
		}
	}
	
		  


	//////////////////DIRECTIONAL LIGHTING WITH NORMAL MAPPING////////////////////
	//////////////////DIRECTIONAL LIGHTING WITH NORMAL MAPPING////////////////////
	//////////////////DIRECTIONAL LIGHTING WITH NORMAL MAPPING////////////////////
float AO=1.0;

#ifdef SSAO
	if (land > 0.01) {

  AO = getSSAOFactor();
 
  
  AO = max(AO, 0.0f);
  }

#endif


			float g_spec = min(pow(specularity.r, 1.5f)*1.8 + pow(specularity.g, 1.5f) * wetx*0.85, 1.0f);
			float g_irr = specularity.b;
			
			float totalspec = g_spec + g_irr;
			
			
landx = 1.0-step(land,0.01);

float sunlight_direct = 0.0;
float sunlight_reflected = 0.0;
float spec = 0.0;
float sky_spec = 0.0;
float fresnel = 0.0;
if (landx > 0.9 && iswater < 0.9) {
					vec3 npos = normalize(fragposition.xyz);

					
					vec3 specular = reflect(npos, normal);
					//simulating 3 (lights spots for sky fresnel fake
					 fresnel = (max(dot(specular, lightVector), 0.0f) + max(dot(specular, normalize(lightVector+vec3(0.1,-0.6,0.0))), 0.0f) + max(dot(specular, normalize(-lightVector-vec3(0.26,0.0,-1.0))), 0.0f))/3.0;			
						fresnel = clamp(pow(fresnel, 6.0f)*4.0,0.0,1.1);
					
					float sunlight = dot(normal, lightVector);
	

					float direct  = clamp(sin(sunlight * 3.141579f/2.0f - 0.0f) + 0.00f, 0.0, 1.00f);
						  
					float reflected = clamp(-sin(sunlight * 3.141579f/2.0f - 0.0f) + 0.95, 0.0f, 2.1f) * 0.5f;
						  reflected = pow(reflected, 3.0f);					
						   
					if (dist < 60.0) {	  
						  spec = max(dot(specular, lightVector), 0.0f);
						  spec = pow(spec, 20.0f);
						  spec *= 3.0;
						  spec = mix(0.0f, spec, clamp(shading, 0.0, 1.0));
						  spec *= totalspec;
						  sky_spec = fresnel*totalspec;
						  }

						  sunlight_direct = mix(1.0,direct,BUMPMAPPWR);
						  sunlight_direct = mix(sunlight_direct, 0.9, translucent);		//1.0 is looking weird on transparent stuff
						  
						  
					
					shading *= sunlight_direct;
				}
				
				
				shading = mix(shading,0.0,rainx);
 
//Albedo
vec3 color = texture2D(gcolor, texcoord.st).rgb;


vec3 torchcolor = vec3(1.0,0.675,0.415);


vec3 Specular_lightmap = clamp(spec * sunlight_color * shading * (1.0-rainx) + sky_spec*pow(sky_lightmap,4.0)*skycolor,0.0,1.1);
	 Specular_lightmap *= sky_lightmap;

	 
#ifdef NightVision	 
vec3 Sunlight_lightmap = (sunlight_color*shading*sky_lightmap*SUNLIGHTAMOUNT + ambient_color*(1.0-shading)*(1.0-SHADOW_DARKNESS)*sky_lightmap) * 1 + nightVision / 1.7;
#else
vec3 Sunlight_lightmap = (sunlight_color*shading*sky_lightmap*SUNLIGHTAMOUNT + ambient_color*(1.0-shading)*(1.0-SHADOW_DARKNESS)*sky_lightmap);
#endif
 
	 
vec3 Torchlight_lightmap = torch_lightmap *  torchcolor;

	 
vec3 LightningFlash_lightmap = vec3(lightning_lightmap *  0.8f, lightning_lightmap *  0.7f, lightning_lightmap *  1.0f);


//RAINWET
			float dampmask = clamp(sky_lightmap * 4.0f - 1.0f, 0.0f, 1.0f) * landx * wetx;
			
			
			color = pow(color, vec3(mix(1.0f, 1.35f, dampmask)));

			
			
			
			
			
//Specular highlight








//Apply different lightmaps to image
if (landx > 0.9 ) {
vec3 color_sunlight = color * Sunlight_lightmap;
vec3 color_torchlight = color * Torchlight_lightmap;

color_sunlight *= mix(1.0,0.6,TimeMidnight);


//Add all light elements together
color = color_sunlight + color_torchlight + Specular_lightmap;
}
else color.rgb *= 0.75;


//Godrays
float GRa = 0.0f;

#ifdef GODRAYS
	const float grna = 3300.0f;

	 GRa = addGodRays(0.0f, Texcoord2, noiseX3*grna, noiseX4*grna, noiseY4*grna, noiseX2*grna, noiseY2*grna)/2.0;
	 GRa = GRa;

#endif
color.rgb *= AO;

//we don't need to fill more than 3 samplers for next
    gl_FragData[0] = vec4(iswater,callwaves(worldposition.xyz),GRa,1.0);
	gl_FragData[1] = vec4(texture2D(gdepth,texcoord.xy));
	gl_FragData[3] = vec4(color, landx);
}
