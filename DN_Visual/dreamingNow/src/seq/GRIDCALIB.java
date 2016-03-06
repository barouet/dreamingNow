package seq;

import processing.core.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.opengl.PGraphicsOpenGL;
import SimpleOpenNI.*;

public class GRIDCALIB extends Sketch{
	
	PImage grid;

	public GRIDCALIB(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		grid = p.loadImage("pic/dn_grid.png");
		
	}
	
	public void reinit() {

		super.reinit(); 
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		
		p.println("COLOR BAR CALIBRATE");
		grid = p.loadImage("pic/dn_grid.png");
	}

	public void draw() {
		
		super.draw();
		c.tint(255,255);
		c.beginDraw();
		
		c.imageMode(p.CORNER);
		c.image(grid,0,0,grid.width-1,grid.height);	  
		c.endDraw();
	
		
		
	}

	public void controlEvent(ControlEvent theEvent) {
	}

	public void oscEvent(OscMessage theOscMessage) {

	}

}