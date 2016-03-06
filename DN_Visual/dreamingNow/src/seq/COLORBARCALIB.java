package seq;

import processing.core.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.opengl.PGraphicsOpenGL;
import SimpleOpenNI.*;

public class COLORBARCALIB extends Sketch{
	
	PImage colorbars;

	public COLORBARCALIB(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		colorbars = p.loadImage("pic/colorbars.png");
		
	}
	
	public void reinit() {

		super.reinit(); 
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);

		p.println("COLOR BAR CALIBRATE");
		colorbars = p.loadImage("pic/colorbars.png");
	}

	public void draw() {
		
		super.draw();
		c.beginDraw();
		c.tint(255,255);
		c.image(colorbars,0,0);	  
		c.endDraw();
	
		
		
	}

	public void controlEvent(ControlEvent theEvent) {
	}

	public void oscEvent(OscMessage theOscMessage) {

	}

}