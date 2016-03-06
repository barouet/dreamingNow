package seq;

import processing.core.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.opengl.PGraphicsOpenGL;


public class ENTER2 extends Sketch{
	
	//NUMBER WALL
	
	int alpha;
	int alphaVideo;
	int curtainHigh= 0 ;
	int curtainLow= 0 ;
	boolean curtainHighUp= false ;
	boolean curtainHighDown= false ;
	boolean curtainLowUp= false ;
	boolean curtainLowDown= false ;
	//int counter = lettre.length;
	boolean counterUp;
	boolean counterDown;
	boolean counterReset;
	boolean counterZero;
	int counter2 = 0;
	int couleur;
	int seqStep = 0;
	int imageAlpha = 0;
	int imageAlpha2 = 0;

	int shakeContamine;
	Change[] change = new Change[11];
	int counterRemplissage = 0; 
	
	PImage sample_img;

	
	//Show options
	boolean showPixels;
	boolean showLetters;
	boolean showRaw;
	
	
	//Kinect Hands to OneHand
	int kinect1Xsub;
	int kinect1Ysub;
	int kinect1X2sub;
	int kinect1Y2sub;
	int kinect1Clicksub;
	int kinectOneHandX;
	int kinectOneHandY;
	int previouskinectOneHandY;

	public ENTER2(DreamingNow p, PGraphicsOpenGL c) {
		super(p, c);
		className = this.getClass().getSimpleName();
		 for (int i = 0; i < change.length; i++) {
			  change[i] = new Change();
		  }
	}
	
	public void reinit() {

		super.reinit();
		fade = 0;
		p.kinectConnected = true;

		
		p.shaderSelect = 0;
		alpha = 0;
		p.seqStep = 0;
		p.shaderSelect = 0;
		seqStep = 0;
		imageAlpha = 0;	
		imageAlpha2 = 0;

		
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);

		for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
	    }
	
		p.println("ENTER part 2");
	}

	public void draw() {
		
		super.draw();
	
		imageAlpha = p.constrain(imageAlpha, 0, 255);
		c.tint(255,imageAlpha);
		handsToOneHandRaw(); 
		c.beginDraw();
		c.background((int) 0);
		events();
		keyframes();

		c.fill(0,255);
		c.noStroke();
		c.hint(p.ENABLE_DEPTH_TEST);
		c.endDraw();

	}

	public void keyPressed() {
	}

	public void keyReleased() {
		
	}
	
	public void keyframes(){
		//p.println(timeLine);
		//Opening of sequence + black, slow fadein
		//p.seqStep = 30;
		int cue = 0;
/*
		//THIS NEEDS TO BE DONE WITH MUSIC TRACK
		cue = 0; //
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 30;//to red rect
		}
*/
		cue = 0; //
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 31;//opening
		}
		
		cue = 9630; //
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 32;//translate down
		}
		
		cue = 28720; //
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 33;//fermeture au centre
		}
		
		cue = 38487; //
		if (timeLine > cue && timeLine < cue+500){
			p.switchToSketch = 4;
		}
	}

	public void events(){
		
		if (p.seqStep == 30){
			if(change[0].test(p.seqStep)){
			}
			c.rectMode(p.CENTER);
			c.pushMatrix();
			c.fill(255,0,0,255);
			c.rect(p.width/2 -2, p.height/2-6, 58, 12);
			
			c.popMatrix();
		}
		
		if (p.seqStep == 31){
			if(change[0].test(p.seqStep)){
			}
			c.rectMode(p.CENTER);
			c.pushMatrix();
			c.fill(255,0,0,255);
			int widthAnimation = p.constrain((int) p.map(timeLine, 0, 9630, 0, 1100), 0,1100);
			c.rect(p.width/2 -2, p.height/2-6, widthAnimation, 12);
			c.popMatrix();
		}
		
		if (p.seqStep == 32){
			if(change[0].test(p.seqStep)){
			}
			c.rectMode(p.CENTER);
			c.pushMatrix();
			c.fill(255,0,0,255);
			int translateYAnimation = p.constrain((int) p.map(timeLine, 9630, 26271, 0, p.height/2),0,p.height/2);
			c.rect(p.width/2 -2, p.height/2-6+translateYAnimation, 1100, 12);
			c.popMatrix();
		}
		
		if (p.seqStep == 33){
			if(change[0].test(p.seqStep)){
			}
			c.rectMode(p.CENTER);
			c.pushMatrix();
			c.fill(255,0,0,255);
			int widthAnimation = p.constrain((int) p.map(timeLine, 28720, 38487, p.width, 0), 0,1100);
			c.rect(p.width/2 -2, p.height-6, widthAnimation, 12);
			c.popMatrix();
		}
		
	}
 	
	public void mousePressed() {	
		p.println(timeLine);
	}

	//////////////////////EVENT HANDLERS
	public float easing(float a, float b, float time){
		//easing
		float dx = a - b;
		if(p.abs(dx) > 1) {
			b += dx * time;
		}
		return b;
	}
	
	public void controlEvent(ControlEvent theEvent) {
	}

	public void oscEvent(OscMessage theOscMessage) {

	}

	class Change {

		//If number has changed, test() outputs true, change() outputs the new number

		float prevTest;
		float change;
		boolean test;

		Change() {
			prevTest = (float) Math.random() * 99;
		}

		boolean test(float f){
			if(f == prevTest){
				test = false;
				change = f;
			} else test = true;

			prevTest = f;
			return test;
		}

		float change(float f){
			test(f);
			return change;	
		}

		void reset(){
			prevTest = (float) Math.random() * 99;
		}
	}

	public void handsToOneHandRaw() {
		//Algo qui rassemble les deux mains en une.
		kinect1Xsub = (int) p.kinect1Xraw;
		kinect1Ysub = (int) p.kinect1Yraw;
		kinect1X2sub = (int) p.kinect1X2raw;
		kinect1Y2sub = (int) p.kinect1Y2raw	;

		kinect1Clicksub = p.constrain(p.kinect1click+p.kinect1click2,0,1);

		if(p.kinect1click2 == 0f){
			kinect1X2sub = 0;
			kinect1Y2sub = 0;
		}


		if(p.kinect1click == 0f){
			kinect1Xsub = 0;
			kinect1Ysub = 0;
		}


		kinectOneHandX = p.constrain(kinect1Xsub + kinect1X2sub,0,p.width);
		kinectOneHandY = p.constrain(kinect1Ysub + kinect1Y2sub,0,p.height);

		if(p.kinect1click == 1 && p.kinect1click2 == 1) {
			kinectOneHandX = kinect1Xsub;
			kinectOneHandY = kinect1Ysub;
		}
	
		
		
	}

}