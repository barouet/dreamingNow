//
//  ball.glsl.c
//  
//
//  Created by youthiMac on 12-08-30.
//
//

#ifdef GL_ES
precision highp float;
#endif

#define PROCESSING_COLOR_SHADER

uniform sampler2D texture;

uniform float time;
uniform vec2 resolution;
uniform vec2 mouse;
uniform int NUM_PARTICLES;
uniform float blobSize;


float sumXY() {
    
    float sum = 0.0;
    for (int i=0; i<NUM_PARTICLES; i++) {
        vec4 p = texture2D(texture,  vec2((float(i)+0.5)/100.,0.)).rgba;
        //Le z et le a multiplient le x et le y pour meilleure résolution
        p.x = ((p.x/4.) + p.z);
        p.y = ((p.y/2.) + p.a);
         //To fit my resolution
         //yaxis = center 0, top 0.5 xaxis= center 0, right 1
        p.x = (p.x - 0.5) * 2.;
        p.y = (p.y - 0.5);
        vec2 move;
        move.x = p.x;
        move.y = p.y;
        
        //Screen coordinate
        vec2 pScreen=(gl_FragCoord.xy/resolution.x)*2.0-vec2(1.0,resolution.y/resolution.x);
        //radius for each blob
        float r =(dot(pScreen-move,pScreen-move))*blobSize;
        float metaball = 1.0/r;
        //alter the cut-off power
        float col = pow(metaball,0.7);
        sum += col;
    }
    return sum;
}

float sumXY2() {
    
    float sum = 0.0;
    for (int i=NUM_PARTICLES/2; i<NUM_PARTICLES; i++) {
        vec4 p = texture2D(texture,  vec2((float(i)+0.5)/100.,0.)).rgba;
        p.x = ((p.x/4.) + p.z);
        p.y = ((p.y/2.) + p.a);
        //To fit my resolution
        p.x = (p.x - 0.5) * 2.;
        p.y = (0.5 - p.y);
        vec2 move;
        move.x = p.x;
        move.y = p.y;
        
        //Screen coordinate
        vec2 pScreen=(gl_FragCoord.xy/resolution.x)*2.0-vec2(1.0,resolution.y/resolution.x);
        //radius for each blob
        float r =(dot(pScreen-move,pScreen-move))*50000.0;
        float metaball = 1.0/r;
        //alter the cut-off power
        float col = pow(metaball,0.9);
        sum += col;
    }
    return sum;
}

void main() {
    float s = sumXY();
    gl_FragColor = vec4(s,s,s,1.0);
    float s2 = sumXY2();
    //gl_FragColor = vec4(s+(s2*0.8), s*(s2*0.8) + s2, s*(s2*1.) + s2,1.0);
    //gl_FragColor = vec4(s*s2, s*s2, s*s2,1.0);
    
}


