package seq;

import java.util.ArrayList;
import processing.core.*;
import processing.video.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import diewald_CV_kit.libraryinfo.*;
import diewald_CV_kit.utility.*;
import diewald_CV_kit.blobdetection.*;
import processing.opengl.PGraphicsOpenGL;
import seq.NUMBERS.Lettre;

public class FACE extends Sketch{
	
	//NUMBER WALL

	ArrayList lettre;
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
	int couleur2;
	int couleurEmoticon;
	int couleurBW = 0;
	int blockSize = 12;
	int numPixelsWide = p.width / blockSize;
	int numPixelsHigh = p.height / blockSize;
	int seqStep = 0;
	int imageAlpha = 0;
	int imageAlpha2 = 0;
	int pixelsAlpha = 0;
	int rawAlpha = 0;
	
	int shakeContamine;
	Change[] change = new Change[11];
	int counterRemplissage = 0; 
	public float threshold;
	PImage kinectRGBimage;


	
	//CV stuff
	PFont font;
	PImage sample_img;

	int[] isActive = new int[numPixelsWide*numPixelsHigh];
	int isActiveTotal = 0;
	int[] moveTo = new int[numPixelsWide*numPixelsHigh];
	int returnThere;
	
	//From NUMBER WALL
	int curtainMode;
	//int counter = lettre.length;
	int wormX;
	int wormX2;
	int wormY;
	int wormY2;
	int wordScroller = 0;
	int wordScroller2 = numPixelsWide*numPixelsHigh;
	int wordScrollerFinal = 0;
	int wordScrollerFinal2 = 0;
	int[] text = new int[6000];
	int wallFiller = 0;
	int shakeContamineX;
	int shakeContamineY;
	int wormCollisionSpeed;
	int index = 0;
	int counter = 0;
	int[] oneLineShakeList = new int[100];
	int[] oneLineShakeList2 = new int[100];
	int modulo = 100;
	int step = 0;
	int counterMax = 9;
	
	
	public FACE(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		lettre = new ArrayList();

		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}

	}

	public void reinit() {

		super.reinit(); 
		
		p.skeletonTracking = false;	

		p.shaderSelect = 0;
		lettre.clear();
		alpha = 0;
		pixelsAlpha = 0;
		rawAlpha = 0;
		
		for (int j = 0; j < numPixelsHigh; j++) {
		    for (int i = 0; i < numPixelsWide; i++) {
		    	lettre.add(new Lettre(0, i*blockSize, j*blockSize));	
		    }
		 }
		
		p.seqStep = 0;
		p.shaderSelect = 0;
		seqStep = 0;
		imageAlpha = 0;	
		imageAlpha2 = 0;
		isActiveTotal = 0;
		c.textFont(p.font,12);
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		
		for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
		}
		
		curtainMode = 0;
		curtainHigh = 0 ;
		curtainLow = 0 ;
		wordScroller = 0;
		counterMax = 9;
		counterRemplissage = 0;
		p.println("FACE");

	}

	public void draw() {
		
		super.draw();
		
		p.kinect.update(); 
		
		imageAlpha = p.constrain(imageAlpha, 0, 255);
		

		c.beginDraw();
		
		c.tint(255,imageAlpha);
		kinectRGBimage = p.kinect.rgbImage().get(0,160,640,320);
		c.textFont(p.font,blockSize);
		c.background((int) 0);
		
		partition();
		events();
		
		curtain(curtainMode);
		
		
		
		c.fill(0,255);
		c.noStroke();
		c.hint(p.ENABLE_DEPTH_TEST);
		c.stroke(0);
		c.strokeCap(100);
		c.endDraw();
	}

	public void keyPressed() {
	 if (p.key == 'n'){
		 p.seqStep++;
	 }
	}

	public void keyReleased() {
		}
	
	public void renderPixels(int contentSwicth, int alpha0, int alpha1, boolean rotation, float rotationEasing, float lineSize, float emoticonLevel){
			
			int counter = 0;
			for (int j = 0; j < numPixelsHigh; j++) {
				for (int i = 0; i < numPixelsWide; i++) {
					//l'image source est de 640x480, il faut faire des maths alors
					couleur = p.color( p.kinect.rgbImage().get((int)((i * blockSize )*(640/(float)p.width)), (int)((j * blockSize )*(320/(float)p.height)+160)));
					couleur2 = p.color(  p.kinect.rgbImage().get((int)(     p.constrain( ((((i+1) * blockSize ))*(640/(float)p.width)) ,0,p.width))  , (int)((j * blockSize )*(320/(float)p.height)+160)));
					
					Lettre l = (Lettre) lettre.get(counter);
					if( PixelColor.brighntess(couleur) > threshold){
						if(counter >= counterRemplissage){
							l.couleur(couleur);
							l.couleur2 = couleur2;
							l.couleurEmoticon = couleurEmoticon;
							l.afficheImage(false, i+(j*i),contentSwicth,alpha0, alpha1,rotation,rotationEasing, lineSize, emoticonLevel);
						}
					} //else isActive[counter] = 0;
					
					counter++;
					l.life=255;
				}
			}
			
	}
	
	public void renderRaw(int alpha){
			c.tint(alpha);
			c.image(kinectRGBimage,0,0,p.width,p.height);
			c.filter(p.GRAY);
	}
	
	public void partition(){
		//p.seqStep = 11;
		
		//pixels fade in
		int offset = 38608;
		int cue = 38608 - offset; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 0;
		} 
		
		//Pixels to raw
		cue = 57490-offset; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 1;
		}
	
		//Raw to circles
		cue = 66981-offset; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 2;
		}
		
		///Circles only
		cue = 86500-offset; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 4;
		}
		
		//Raw to circles + lines
		cue = 105426-offset; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 5;
		}
		

		
		//lines only
		cue = 124751-offset; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 6;
		}
		
		//smiley in only 135890
		cue = 140890-offset; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 9;

		}
		

		//smiley raw in 
		cue = 153623-offset;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 10;
		}
		
		cue = 166000-offset;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 12;
		}
		/*
		//smiley in only
		int cue = 0; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 9;
		}
		cue = 10001; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 10;
		}
		*/
	}

	public void events(){
		

		int offset = 38608;
		if (p.seqStep == 0){
			//pixels fade in
			if(change[0].test(p.seqStep)){
				pixelsAlpha = 0;
				rawAlpha = 0;
			}
			
			pixelsAlpha = (int) p.constrain(p.map(timeLine, 38608 - offset, 42608-offset, 0, 255),0,255);
			renderPixels(1,pixelsAlpha,0,false,0f,1 ,0f);
		}
		
		if (p.seqStep == 1){
			//Pixels to raw
			if(change[0].test(p.seqStep)){
				rawAlpha = 0;	
			}
				rawAlpha++;
				rawAlpha = p.constrain(rawAlpha,0,255);
				rawAlpha = (int) p.constrain(p.map(timeLine, 57490-offset, 62981-offset, 0, 255),0,255);
				//renderPixels(1,255,0,false,0f);
				renderRaw(rawAlpha);
				renderPixels(1,p.abs(255-rawAlpha),255,false,0f,1 ,0f);
		}
		
		
		if (p.seqStep == 2){
			//Raw to circles
			//ALPHA TIMING NEEDED HERE
			if(change[0].test(p.seqStep)){
				pixelsAlpha = 0;
			}
			pixelsAlpha = (int) p.constrain(p.map(timeLine, 66981 - offset, 86500-offset, 0, 255),0,255);
			renderRaw(p.abs(255-pixelsAlpha));
			renderPixels(2, pixelsAlpha, pixelsAlpha,false,0f,1,0f);
			renderPixels(1, 0,p.abs(255-pixelsAlpha),false,0f,1,0f);
			
		}
		
		if (p.seqStep == 3){
			//ALPHA TIMING NEEDED HERE
			//Circles only
			if(change[0].test(p.seqStep)){
				pixelsAlpha = 0;
			}
			pixelsAlpha++;
			pixelsAlpha = p.constrain(pixelsAlpha,0,255);
			renderPixels(2,255,255,false,0.1f,1,0f);
		}
		//p.println(p.seqStep);
		if (p.seqStep == 4){
			//ALPHA TIMING NEEDED HERE
			//Circles with rotation
			if(change[0].test(p.seqStep)){
			}
			renderPixels(2, 255,255,true,0.1f,1,0f);
			
		}
		
		if (p.seqStep == 5){
			//ALPHA TIMING NEEDED HERE
			//Circles with rotation + lines in
			if(change[0].test(p.seqStep)){
				pixelsAlpha = 0;
			}
			pixelsAlpha++;
			
			pixelsAlpha = (int) p.constrain(p.map(timeLine, 105426 - offset, 115290-offset, 0, 255),0,255);
			renderPixels(2, 255,255,true,0.1f,1,0f);
			renderPixels(3, pixelsAlpha,pixelsAlpha,true,0.1f,1,0f);
		}
		

		if (p.seqStep == 6){
			//ALPHA TIMING NEEDED HERE
			//Line with rotation - circles out
			if(change[0].test(p.seqStep)){
				pixelsAlpha = 0;
			}
			pixelsAlpha = (int) p.constrain(p.map(timeLine, 124751 - offset, 135000-offset, 0, 255),0,255);
			renderPixels(2, p.abs(255-pixelsAlpha),p.abs(255-pixelsAlpha),true,0.1f,1,0f);
			renderPixels(3, 255,255,true,0.1f,1,0f);
			
		}
		

		if (p.seqStep == 9){
			//ALPHA TIMING NEEDED HERE
			//rect + emoticon?
			if(change[0].test(p.seqStep)){
				pixelsAlpha = 0;
			}
			/*
			pixelsAlpha = (int) p.constrain(p.map(timeLine, 153169 - offset, 189500-offset, 0, 255),0,255);
			int pixelsAlpha2 = (int) p.constrain(p.map(timeLine, 153169 - offset, 159500-offset, 0, 255),0,255);
			float emoticonIn = p.constrain(p.map(timeLine, 153169 - offset, 159500-offset, 0.f, 0.1f),0,0.1f);
			//renderPixels(3,p.abs(255-pixelsAlpha2),p.abs(255-pixelsAlpha2),true,0.001f);
			//renderPixels(4,pixelsAlpha,pixelsAlpha,true,0.001f);
			p.println(emoticonIn);
			renderPixels(3,255,255,true,0.001f,emoticonIn);
			//renderPixels(4,0,0,true,0.00001f);
			*/
			
			//135890
			pixelsAlpha = (int) p.constrain(p.map(timeLine, 140890 - offset, 153600 - offset, 0, 255),0,255);
			int pixelsAlpha2 = (int) p.constrain(p.map(timeLine, 140890 - offset, 153600 - offset, 0, 255),0,255);
			float emoticonIn = 0; //p.constrain(p.map(timeLine, 140890 - offset, 153600 - offset, 0.f, 0.5f),0, 0.5f);
			//renderPixels(3,p.abs(255-pixelsAlpha2),p.abs(255-pixelsAlpha2),true,0.001f);
			//renderPixels(4,pixelsAlpha,pixelsAlpha,true,0.001f);
			renderPixels(3,255,255,true,0.1f,1,emoticonIn);
			//renderPixels(4,0,0,true,0.00001f);
			
		}
		
		if (p.seqStep == 10){		
			if(change[0].test(p.seqStep)){
				pixelsAlpha = 0;
			}

			int transpose = 0;
			int rawAlpha2 = 255;
			//int lineErase = 255;
			pixelsAlpha = (int) p.constrain(p.map(timeLine, 153623 - offset, 165000 - offset, 0, 255),0,255);
			int size = (int) p.constrain(p.map(timeLine, 153623 - offset , 165000 - offset, 0, 255),0,255);
			//lineErase = (int) p.constrain(p.map(timeLine, 20001, 30000, 0, 255),0,255);
			rawAlpha2 = (int) p.constrain(p.map(timeLine, 167204 - offset, 170940 - offset, 0, 255),0,255);
			float move = p.constrain(p.map(timeLine, 163204 - offset, 172940 - offset, 0, 1),0,1);
			
			c.pushMatrix();
			c.translate(p.width/2,p.height/2,0);
			c.scale(p.map(move,0,1,1,0.054f));
			transpose = (int) (p.map(move,0,1,0,-360));
			c.translate(-p.width/2 - p.map(move,-0,1,0,45),-p.height/2  + transpose,0);
			//c.tint(255,rawAlpha2);
			//c.image(emoticon.get(0,100,640,320),0,0,p.width,p.height);
			//renderPixels(4,size,p.abs(255-rawAlpha2),true,0.1f,0.1f,0f);
			//Fadeout
			renderPixels(3,255,255,true,0.1f,p.map(p.abs(255-pixelsAlpha),0,255,0,1f),0f);
			
			//No fade
			//renderPixels(3,255,255,true,0.1f,1f,0f);
			
			c.popMatrix();
			
			if ( timeLine > 172940- offset) p.switchToSketch = 5;
		}
		
		
		//Tool to match with emoticon seq
		if (p.seqStep == 11){		
			c.pushMatrix();
			c.translate(p.width/2,p.height/2,0);
			c.scale(p.map(1,0,1,1,0.054f));
			c.translate(-p.width/2 - p.map(1,-0,1,0,45), -p.height/2  + -360,0);
			c.tint(255,255);
			
			c.popMatrix();
			
			
		}
		
		if (p.seqStep == 12){		
			
		}
	}
 		
	public void mousePressed() {
		p.println(timeLine);
	}

	public void mouseDragged() {
	}

	public void mouseReleased() {
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

	public float easing(float a, float b, float time){
		return super.easing(a, b, time);
	}
	
	class Lettre {
		int couleurEmoticon;
		int couleur2;
		int counter = 0;
		int coordX;
		int coordY;
		int coordZ;
		int coordZeased;
		int id = (int)p.random(0, 100);
		int colore;
		int life;
		int life2;
		int life3;
		int texte;
		int colorR;
		int colorG;
		int colorB;
		int texteMax;
		float scalingEased;
		float scalingEased2;
		float rotation;
		float rotationEased;
		
		Lettre(int cStart, int oX, int oY) {
			counter = cStart;
			coordX = oX;
			coordY = oY;
			coordZ = 0;
			coordZeased = 0;
			colore = (int)p.random(0,255);
			life = 0;
			life2 = 0;
			scalingEased = 0;
			scalingEased2 = 0;
			couleur2 = 0;
			rotation = 0;
			rotationEased = 0;
		}

		void count(int cMin, int cMax) {
			counter=p.constrain((counter+1)%10, cMin, cMax);
		}

		void couleur(int c) {
			colore = c;
		}

		void couleur(int r, int g, int b) {
			colorR = r;
			colorG = g;
			colorB = b;
		}

		void afficheText(boolean zaxis, int colorMode) { 


			if(colorMode == 0){
				c.fill(colore,life);
			}else c.fill(colorR,colorG,colorB,life2);


			c.textAlign(p.CENTER, p.BASELINE);

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,100,-100);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}

			c.text((int)p.map(p.brightness(texte),0,255,9,0), coordX+6, coordY+10, coordZeased);

		}

		void afficheText2(boolean zaxis, int colorMode) { 


			if(colorMode == 0){
				c.fill(colore,life);
			}else c.fill(colorR,colorG,colorB,life2);


			c.textAlign(p.CENTER, p.BASELINE);

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,100,-100);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}

			c.text((int)p.map(p.brightness(texte),0,255,9,0), coordX+6, coordY+10, coordZeased);

		}

		void afficheText3(boolean zaxis, int colorMode) { 


			if(colorMode == 0){
				c.fill(colore,life3);
			}else c.fill(colorR,colorG,colorB,life3);


			c.textAlign(p.CENTER, p.BASELINE);

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,100,-100);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}
			texteMax = p.constrain(texteMax,0,9);
			c.text(p.constrain((int)p.map(p.brightness(texte),0,255,texteMax,0)+counter,0,9), coordX+6, coordY+10, coordZeased);

		}

		void affichePoint(boolean zaxis, int index) { 

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,600,-600);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}
			life = p.constrain(life, 0 , 255);
			c.strokeCap(p.SQUARE);
			c.strokeWeight(14);
			c.stroke(colore,life);
			c.fill(colore);
			coordX = (moveTo[index]%numPixelsWide) * blockSize; 
			coordY = (moveTo[index]/numPixelsHigh) * blockSize;
			c.point((coordX+6) *(p.width/640f),(coordY+6) *(p.height/480f),coordZeased);
			c.text(p.constrain((int)p.map(p.brightness(texte),0,255,texteMax,0)+counter,0,9), coordX+6, coordY+10, coordZeased);
			
		}
		
		void afficheImage(boolean zaxis, int index, int contentSwitch, int alpha0, int alpha1, boolean rotate, float rotateEaseLevel, float lineSize, float emotionLevel) { 

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,600,-600);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}
			life = p.constrain(life, 0 , 255);
			c.strokeCap(p.SQUARE);
			c.strokeWeight(blockSize);
			c.stroke(colore,life);
			c.noStroke();
			c.fill(colore);
			int greyscale =p.round((float) (p.red(colore)*0.222+p.green(colore)*0.707+p.blue(colore)*0.071));
			int greyscale2 =p.round((float) (p.red(couleur2)*0.222+p.green(couleur2)*0.707+p.blue(couleur2)*0.071));
			int greyscaleEmoticon = p.round((float) (p.red(couleurEmoticon)*0.222+p.green(couleurEmoticon)*0.707+p.blue(couleurEmoticon)*0.071));
			int webcamPlusEmoticon = greyscaleEmoticon; //(int) p.constrain(greyscale+(greyscaleEmoticon * (p.mouseX*0.01f)),0,255);
			greyscale = (int) ((greyscale*lineSize) + (greyscaleEmoticon*emotionLevel));
			
			float scaling = p.map(greyscale, 0, 125, 0.01f, 1.f);
			if (scaling>0){
				scalingEased = easing(scaling*100, scalingEased*100, 0.1f);
				scalingEased = scalingEased/100;
			}
			
			float scaling2 = p.map(greyscale2, 0, 125, 0.01f, 1.f);
			if (scaling2>0){
				scalingEased2= easing(scaling2*100, scalingEased2*100, 0.1f);
				scalingEased2 = scalingEased2/100;
			}
			
			
			//if (greyscale>170) greyscale = 0;
			//else greyscale = 255;
			
	
			c.noFill();
			c.pushMatrix();
			//int coordXnew = (moveTo[index]%numPixelsWide) * blockSize; 
			//int coordYnew = (moveTo[index]/numPixelsHigh) * blockSize;
			//c.point((coordX+5) *(p.width/640f),(coordY+14) *(p.height/480f),coordZeased);
			//c.point((coordX),(coordY+5),coordZeased);
			c.translate(coordX,coordY);

			if (rotate) rotation = scalingEased * p.TWO_PI;
			else rotation = 0;
						
			rotationEased= easing(rotation*100, rotationEased*100, rotateEaseLevel);
			rotationEased = rotationEased/100;
			c.rotate(rotationEased);
			
			c.stroke(webcamPlusEmoticon);
			
			float scaling1 = 0.8f;

			if(contentSwitch == 1){
					c.fill(greyscale,alpha0);
					c.strokeWeight(p.map(alpha1, 0, 255, 0f,0.5f));
					c.stroke(greyscale,alpha1);
					c.rectMode(p.CORNER);
					c.rect(0,0,blockSize,blockSize);
			}
			if(contentSwitch == 2){
					c.fill(greyscale,alpha0);
					c.noStroke();
					c.ellipse(0+blockSize,0,blockSize * scalingEased * scaling1,blockSize * scalingEased * scaling1);
					c.ellipse(0-blockSize,0,blockSize * scalingEased * scaling1,blockSize * scalingEased * scaling1);
			}
			if(contentSwitch == 3){
					c.fill(255, alpha0);
					//c.strokeWeight(scalingEased * 0.1f);
					c.strokeWeight(1f*lineSize);
					c.stroke(p.constrain(greyscale * 20f,0,255), alpha1);
					//c.stroke(255);
					float d1 = p.map(scalingEased, 0, 1, 0, 10f);
					float d2 = p.map(scalingEased2, 0, 1, 0, 10f);
					c.line(-d1, d1, blockSize-d2, d2);
					}
			if(contentSwitch == 4){
					if(greyscaleEmoticon > 250){
					scaling = p.map(greyscaleEmoticon, 0, 125, 0.01f, 1.f);
					if (scaling>0){
						scalingEased = easing(scaling*100, scalingEased*100, 0.1f);
						scalingEased = scalingEased/100;
					}
					 scaling2 = p.map(greyscaleEmoticon, 0, 125, 0.01f, 1.f);
					if (scaling2>0){
						scalingEased2= easing(scaling2*100, scalingEased2*100, 0.1f);
						scalingEased2 = scalingEased2/100;
					}
					
					c.strokeWeight((alpha0/(float)255) * 15);
					c.stroke(couleurEmoticon,alpha1);
					float d1 = p.map(scalingEased, 0, 1, 0, 10f);
					float d2 = p.map(scalingEased2, 0, 1, 0, 10f);
					c.line(-d1, d1, blockSize-d2, d2);
					}
			}
			
			
			c.popMatrix();
			
			//kinectRBGimage.copy((int) (coordX* (640/(float)p.width)), (int) (coordY * (480/(float)p.height)), 100, 100, (int) (coordXnew * (640/(float)p.width)), (int) (coordYnew * (480/(float)p.height)),100,100);
			//kinectRBGimage.copy((int)(((coordX) *(p.width/640f)) * (640/(float)p.width)),(int)(((coordY) *(p.height/480f)) * (480/(float)p.height)), 20,20,(int)(((coordXnew) *(p.width/640f)) * (640/(float)p.width)),(int)(((coordYnew) *(p.height/480f)) * (480/(float)p.height)), 20,20);	
			
		}

		int id() { 
			return id;  
		}
	}

	
	public void printlnFPS() {	
		//c.fill(100, 200, 255);
		//c.text("nana", 10, 20);
	}

	public void curtain(int param){
		
		if(param > 0){
		
			//Ouvre de bas en haut
			if(param == 1){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					//curtainLow++;
				}
			}
			
			//Ferme de haut en bas
			if(param == 2){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					//curtainLow--;
				}
			}

			//ouvre de bas en haut
			if(param == 3){
				curtainLow = p.height;
				if(p.frameCount%2 == 0){
					curtainHigh++;
				}
			}

			//Ouvre de haut en bas
			if(param == 4){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					curtainLow++;
				}
			}
			
			//Ferme de bas en haut doit caller curtainHigh = p.height avant l'appel de la fonction
			if(param == 5){
				curtainLow = p.height;
				if(p.frameCount%2 == 0){
					curtainHigh--;
				}
			}

			curtainLow = p.constrain(curtainLow, 0, p.height);
			curtainHigh = p.constrain(curtainHigh, 0, p.height);
			c.fill(0);
			c.rect(0,0+curtainHigh,p.width,0+curtainLow);
		}
	}
	
}