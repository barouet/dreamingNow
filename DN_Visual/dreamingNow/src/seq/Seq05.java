package seq;

//import dreaming.*;
import java.util.ArrayList;

import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
//import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;

public class Seq05 extends Sketch{
	
	int colonnes = 32;
	int rangees = 32;
	int[][] cellules2d;
	int[][] cellules2dtext;
	
	public Seq05(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		cellules2d = new int[colonnes][rangees];
		cellules2dtext = new int[colonnes][rangees];
	}

	public void reinit() {
		
		super.reinit(); 
		p.shaderSelect = 0;
		p.println("PUNCHWALL");
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
	}

	public void draw() {
		super.draw();

		c.beginDraw();
		c.background((int) 0);
		c.fill(255);
		//SHOW HANDS
		for ( int c =0; c < colonnes; c=c+1 ) {
			for ( int r =0; r < colonnes; r=r+1 ) {
				cellules2d[c][r] = p.constrain(cellules2d[c][r] - 3,0,255);
				cellules2dtext[c][r] = p.constrain((cellules2dtext[c][r] - 1),0,90);
			}
		}

		float largeur = p.width/colonnes;
		float hauteur = p.height/rangees;
		for ( int c =0; c < colonnes; c=c+1 ) {
			for ( int r =0; r < rangees; r=r+1 ) {

				if ( p.mouseX > c * largeur && p.mouseX < (c+1) * largeur
						&& p.mouseY > r * hauteur && p.mouseY < (r+1) * hauteur ) {
					cellules2d[c][r] = 255;
					cellules2dtext[c][r] = 90;
				}
				if ( p.kinect1X > c * largeur && p.kinect1X < (c+1) * largeur
						&& p.kinect1Y > r * hauteur && p.kinect1Y < (r+1) * hauteur ) {
					cellules2d[c][r] = 255;
					cellules2dtext[c][r] = 90;
				}

				if ( p.kinect1X2 > c * largeur && p.kinect1X2 < (c+1) * largeur
						&& p.kinect1Y2 > r * hauteur && p.kinect1Y2 < (r+1) * hauteur ) {
					cellules2d[c][r] = 255;
					cellules2dtext[c][r] = 90;
				} 
			}
		}

		for ( int c1 = 0; c1 < colonnes; c1=c1+1 ) {
			for ( int r =0; r < rangees; r=r+1 ) {
				c.fill(cellules2d[c1][r]);
				c.text(cellules2dtext[c1][r]/10,largeur * c1, hauteur * r); 
			}
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