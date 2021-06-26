#version 120

varying vec2 texcoord;

uniform vec3 sunPosition;
uniform vec3 moonPosition;

uniform int worldTime;

varying vec3 lightVector;
varying vec3 specMultiplier;

uniform int heldItemId;
uniform int heldBlockLightValue;

varying vec3 heldLightSpecMultiplier;
varying float heldLightMagnitude;

varying float TimeMidnight;
varying float TimeSunset;
varying float TimeNoon;
varying float TimeSunrise;

uniform float rainStrength;
uniform float wetness;

varying vec3 reflected_color;
varying vec3 sunlight_color;
varying vec3 ambfill_color;
varying vec3 ambient_color;
varying vec3 skycolor;

float rainx = clamp(rainStrength, 0.0f, 0.2f);
float wetx  = clamp(wetness, 0.0f, 1.0f);

void main() {
	gl_Position = ftransform();
	
	texcoord = (gl_TextureMatrix[0] * gl_MultiTexCoord0).xy;

	if (worldTime < 12700 || worldTime > 23250) {
		lightVector = normalize(sunPosition);
		specMultiplier = vec3(1.0, 1.0, 1.0);
	} else {
		lightVector = normalize(moonPosition);
		specMultiplier = vec3(0.5, 0.5, 0.5);
	}
	
	specMultiplier *= clamp(abs(float(worldTime) / 500.0 - 46.0), 0.0, 1.0) * clamp(abs(float(worldTime) / 500.0 - 24.5), 0.0, 1.0);
	
	heldLightMagnitude = float(heldBlockLightValue);

	if (heldItemId == 50) {
		// torch
		heldLightSpecMultiplier = vec3(1.0, 0.9, 0.5);
	} else if (heldItemId == 76 || heldItemId == 94) {
		// active redstone torch / redstone repeater
		heldLightSpecMultiplier = vec3(1.0, 0.0, 0.0);
	} else if (heldItemId == 89) {
		// lightstone
		heldLightSpecMultiplier = vec3(1.0, 1.0, 0.4);
	} else if (heldItemId == 10 || heldItemId == 11 || heldItemId == 51) {
		// lava / lava / fire
		heldLightSpecMultiplier = vec3(1.0, 0.5, 0.0);
	} else if (heldItemId == 91) {
		// jack-o-lantern
		heldLightSpecMultiplier = vec3(1.0, 0.5, 0.0);
	} else if (heldItemId == 326) {
		// water bucket
		heldLightMagnitude = 2.0;
		heldLightSpecMultiplier = vec3(0.0, 0.0, 0.3);
	} else if (heldItemId == 327) {
		// lava bucket
		heldLightMagnitude = 15.0;
		heldLightSpecMultiplier = vec3(1.0, 0.5, 0.3);
	} else {
		heldLightSpecMultiplier = vec3(0.0);
	}
	
	
		float timefract = float(worldTime);

	 TimeSunrise  = ((clamp(timefract, 23000.0, 24000.0) - 23000.0) / 1000.0) + (1.0-(clamp(timefract, 0.0, 6000.0)/6000.0));
		  
	 TimeNoon     = ((clamp(timefract, 0.0, 6000.0)) / 6000.0) - ((clamp(timefract, 6000.0, 12000.0) - 6000.0) / 6000.0);
	  
	 TimeSunset   = ((clamp(timefract, 6000.0, 12000.0) - 6000.0) / 6000.0) - ((clamp(timefract, 12000.0, 12750.0) - 12000.0) / 750.0);
		  
	 TimeMidnight = ((clamp(timefract, 12000.0, 12750.0) - 12000.0) / 750.0) - ((clamp(timefract, 23000.0, 24000.0) - 23000.0) / 1000.0);
	
	
	const float rayleigh = 0.25f;

	
	vec3 sunrise_sun;
	 sunrise_sun.r = 1.0 * TimeSunrise;
	 sunrise_sun.g = 0.8 * TimeSunrise;
	 sunrise_sun.b = 0.45*TimeSunrise;
	
	vec3 sunrise_amb;
	 sunrise_amb.r = 0.85*TimeSunrise;
	 sunrise_amb.g = 0.85*TimeSunrise;
	 sunrise_amb.b = 0.95*TimeSunrise;
	 
	
	vec3 noon_sun;
	 noon_sun.r = 1.00 * TimeNoon;
	 noon_sun.g = 0.97 * TimeNoon;
	 noon_sun.b = 0.90 * TimeNoon;	 
	
	
	vec3 noon_amb;
	 noon_amb.r = 0.9 * TimeNoon;
	 noon_amb.g = 0.9 * TimeNoon;
	 noon_amb.b = 0.95 * TimeNoon;
	
	vec3 sunset_sun;
	 sunset_sun.r = 1.0 * TimeSunset;
	 sunset_sun.g = 0.8 * TimeSunset;
	 sunset_sun.b = 0.45* TimeSunset;
	
	vec3 sunset_amb;
	 sunset_amb.r = 0.85*TimeSunset;
	 sunset_amb.g = 0.85*TimeSunset;
	 sunset_amb.b = 0.95*TimeSunset;
	
	vec3 midnight_sun;
	 midnight_sun.r = 0.2  * TimeMidnight;
	 midnight_sun.g = 0.24  * TimeMidnight;
	 midnight_sun.b = 0.37  * TimeMidnight;
	
	vec3 midnight_amb;
	 midnight_amb.r =  0.4* TimeMidnight;
	 midnight_amb.g =  0.5* TimeMidnight;
	 midnight_amb.b =  TimeMidnight;


	
	 sunlight_color.r = sunrise_sun.r + noon_sun.r + sunset_sun.r + midnight_sun.r;
	 sunlight_color.g = sunrise_sun.g + noon_sun.g + sunset_sun.g + midnight_sun.g;
	 sunlight_color.b = sunrise_sun.b + noon_sun.b + sunset_sun.b + midnight_sun.b;
	

	 ambient_color.r = sunrise_amb.r + noon_amb.r + sunset_amb.r + midnight_amb.r;
	 ambient_color.g = sunrise_amb.g + noon_amb.g + sunset_amb.g + midnight_amb.g;
	 ambient_color.b = sunrise_amb.b + noon_amb.b + sunset_amb.b + midnight_amb.b;
	 
	
	 reflected_color = mix(sunlight_color, ambient_color, 0.1f);

	 
	
	 ambfill_color = mix(sunlight_color, ambient_color, 0.25f);
	 
	 float sun_fill = 0.25f;
	
	 ambient_color = mix(ambient_color, sunlight_color, sun_fill);
	 vec3 ambient_color_rain = vec3(1.2, 1.2, 1.2) * (1.0f - TimeMidnight * 0.95f); //rain
	 ambient_color = mix(ambient_color, ambient_color_rain, rainx); //rain
	

	skycolor =  vec3(0.533,0.8,0.878)*TimeNoon+vec3(0.878,0.8,0.6)*(TimeSunrise+TimeSunset)+vec3(0.05,0.07,0.2)*TimeMidnight;
	skycolor = mix(skycolor,vec3(0.5,0.5,0.5),0.2);
}
