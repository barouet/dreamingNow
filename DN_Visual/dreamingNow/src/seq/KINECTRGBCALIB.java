package seq;

import processing.core.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.opengl.PGraphicsOpenGL;


public class KINECTRGBCALIB extends Sketch{
	
	public float threshold;
	PImage kinectRGBimage;



	public KINECTRGBCALIB(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();	
	}
	
	public void reinit() {

		super.reinit(); 
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		p.println("KINECTCALIB");

	}

	public void draw() {
		
		super.draw();
		p.kinect.update(); 
		c.tint(255,255);
		c.beginDraw();
		kinectRGBimage = p.kinect.rgbImage().get(0,160,640,320);
		c.image(kinectRGBimage,0,0,p.width,p.height);
		c.endDraw();
	}

	public void controlEvent(ControlEvent theEvent) {
	}

	public void oscEvent(OscMessage theOscMessage) {

	}

}