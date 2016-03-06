#define PROCESSING_TEXTURE_SHADER
uniform sampler2D textureSampler;

uniform float time;
uniform float arg1;
uniform float arg2;
uniform vec2 resolution;
const float PI = 3.1415926535897932;

//speed
uniform float speed;
uniform float speed_x;
uniform float speed_y;

// geometry
uniform float intensity;
uniform int steps;
uniform float frequency;
uniform int angle; // better when a prime

// reflection and emboss
uniform float delta;
uniform float intence;
uniform float emboss;

//---------- crystals effect

  float col(vec2 coord)
  {
    float delta_theta = 2.0 * PI / float(angle);
    float col = 0.0;
    float theta = 0.0;
    for (int i = 0; i < steps; i++)
    {
      vec2 adjc = coord;
      theta = delta_theta*float(i);
      adjc.x += cos(theta)*time*speed + time * speed_x;
      adjc.y -= sin(theta)*time*speed - time * speed_y;
      col = col + cos( (adjc.x*cos(theta) - adjc.y*sin(theta))*frequency)*intensity;
    }

    return cos(col);
  }

//---------- main

void main(void)
{
vec2 p = gl_FragCoord.xy / resolution.xy , c1 = p, c2 = p;
float cc1 = col(c1);

c2.x += resolution.x;
float dx = emboss*(cc1-col(c2))/delta;

c2.x = p.x;
c2.y += resolution.y;
float dy = emboss*(cc1-col(c2))/delta;

c1.x += dx;
c1.y = (c1.y+dy);

float alpha = 1.+dot(dx,dy)*intence;
gl_FragColor = texture2D(textureSampler,c1)*(alpha);
}