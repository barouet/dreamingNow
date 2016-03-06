package seq;

//import dreaming.*;
import java.util.ArrayList;

import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
//import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;
import seq.LETTERS.Boid;
import seq.LETTERS.Noiser;

public class CALIBRATRE extends Sketch{
	
	int seq = 0;
	int allo = 0;


	public CALIBRATRE(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		
	
		// get the name of this class and print it
		className = this.getClass().getSimpleName();
	}

	public void reinit() {
		
		super.reinit();
		p.wallEasing = 0.2f; //Easing setting for wall kinect
		p.shaderSelect = 0;
		seq = 0;
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		
		OscMessage myMessage = new OscMessage("/seqConfirm");
		myMessage.add(50); /* add an int to the osc message */
		p.oscP5.send(myMessage, p.cueInterface);
		p.println("CALIBRATRE");
	}

	public void draw() {
		super.draw(); 
		
		p.wallEasing = 0.1f;
		c.beginDraw();
		c.background(0);
		c.stroke(255);
		c.line(p.width/2, 0, p.width/2,p.height);
		c.fill(0);
		
		if(p.kinect1click == 1f){
			c.ellipseMode(p.CENTER);
			c.ellipse(p.kinect1X,p.kinect1Y,20,20);
			c.textFont(p.font, 12);
			c.fill(255);
			c.text("1",p.kinect1X,p.kinect1Y);
		}
		if(p.kinect1click2 == 1f){
			c.fill(255);
			c.ellipse(p.kinect1X2,p.kinect1Y2,20,20);
			c.text("2",p.kinect1X2,p.kinect1Y2);
		}
		if(p.kinect2click == 1f){
			c.ellipseMode(p.CENTER);
			c.ellipse(p.kinect2X,p.kinect2Y,20,20);
			c.textFont(p.font, 12);
			c.fill(0,255,0);
			c.text("1",p.kinect2X,p.kinect2Y);
		}
		if(p.kinect2click2 == 1f){
			c.fill(0,255,255);
			c.ellipse(p.kinect2X2,p.kinect2Y2,20,20);
			c.text("2",p.kinect2X2,p.kinect2Y2);
		}
		
		
		c.endDraw();

	}

	
	
	public void keyPressed() {
	}

	public void mousePressed() {
	}

	public void mouseDragged() {
	}

	public void mouseReleased() {
	}

	public void controlEvent(ControlEvent theEvent) {
	}

	public void oscEvent(OscMessage theOscMessage) {

	}

}