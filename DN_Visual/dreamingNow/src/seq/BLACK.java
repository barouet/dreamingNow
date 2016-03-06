package seq;

//import dreaming.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
//import processing.core.PApplet;
import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;


public class BLACK extends Sketch{
	

	
	public BLACK(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();

	}
	
	public void reinit() {
		PApplet.println("BLACK");
		super.reinit();
		
	}

	public void draw() {
		super.draw();
		c.beginDraw();
		c.background(0);
		c.endDraw();
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