package seq;

import processing.core.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.opengl.PGraphicsOpenGL;
import SimpleOpenNI.*;

public class KINECTDEPTHCALIB extends Sketch{

	public KINECTDEPTHCALIB(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		
		
	}
	
	public void reinit() {

		super.reinit(); 
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		p.println("KINECTCALIB DEPTH");

	}

	public void draw() {
		
		super.draw();
		p.kinect.update();
		c.beginDraw();
		c.smooth(2);
		c.tint(255,255);
		c.background(0);
		int	index;
		int[] depthMap = p.kinect.depthMap();
		PVector realWorldPoint;
		PVector[] realWorldMap = p.kinect.depthMapRealWorld();
		for (int y = p.kinect.depthHeight(); y > 0; y = y - 2){
			for (int x = p.kinect.depthWidth(); x > 0;x = x - 2){
				y = p.constrain(y, 0, 479);
				x = p.constrain(x, 0, 639);
				index = x + y * p.kinect.depthWidth(); 
				if (depthMap[index] > 10 && depthMap[index] < 2800){
					realWorldPoint = realWorldMap[index];
					c.stroke(255);
					c.point(realWorldPoint.x+p.width/2,(240-realWorldPoint.y)+(p.height/2-240),1100 - realWorldPoint.z);
					//Conditions
				}
				
				}
			}		  
		c.endDraw();
	
		
		
	}

	public void controlEvent(ControlEvent theEvent) {
	}

	public void oscEvent(OscMessage theOscMessage) {

	}

}