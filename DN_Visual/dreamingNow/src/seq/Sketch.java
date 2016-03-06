package seq;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.opengl.PGraphicsOpenGL;
import seq.EMOTICONS.Box;
import seq.EMOTICONS.Emoticons;

public class Sketch {
	
	public DreamingNow p;
	public String className;
	public PGraphicsOpenGL c;
	int seconds;
	int minutes;
	int millis;
	int timecode;
	int seqStartTime;
	int fade;
	long now;
    int timeLine;
	
	// simple constructor, taking a PApplet reference as a
	// reference so that we can address the parent object.
	public Sketch(DreamingNow p, PGraphicsOpenGL c) {
		this.p = p;
		this.c = c;
		// get the name of this class and print it
		className = this.getClass().getSimpleName();
		p.println("\n------------");
		p.println(className + " constructor. (In Sketch)");
	}

	// reinit() should take care of any initialization required
	// for a sketch to run. it might be called several times if
	// the user switches back and forth between sketches.
	public void reinit() {
		// println(className+".reinit()");
		now = System.nanoTime();
		p.seqStep = 0;
		p.shaderSelect = 0;
		p.frameRate(60);
	}

	public void draw() {
		c.beginDraw();
		c.resetShader();
		c.endDraw();
		
		timeLine = (int) ((System.nanoTime() - now)/1000000);
		seconds = (int) (((timeLine)/1000)%60);
		minutes = (int) ((((timeLine)/1000)%3600)/60);
		timecode = (int) (seconds + minutes * 100 + (millis/100%10)/10.f);
	}

	// /////////////////////////////////////////////////////////
	// Sketch defines standard event handler functions so that
	// we can tell it about events it might want to respond to.

	public void keyPressed() {
	}

	public void mousePressed() {
	}
	
	public void events() {
	}

	public void mouseDragged() {
	}

	public void mouseReleased() {
	}
	
	public float easing(float a, float b, float time){
		//easing
		float dx = a - b;
		if(p.abs(dx) > 1) {
			b += dx * time;
		}
		return b;
	}
	// Collision event functions!
	public void beginContact(Contact cp) {
		  
		}

		// Objects stop touching each other
	public void endContact(Contact cp) {
		}
	
	// controlEvent() is used for controlP5 events
	public void controlEvent(ControlEvent theEvent) {
	}

	public void oscEvent(OscMessage theOscMessage) {

	}

}