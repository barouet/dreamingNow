package seq;

//import dreaming.*;
import java.util.ArrayList;
import processing.core.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
//import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;


public class END extends Sketch{
	int move = 0;
	int accel = 0;
	char[] dreamingNow = { 'D', 'R', 'E', 'A', 'M', 'I', 'N', 'G', 'N', 'O', 'W'};
	int charIndex = 0;
	PFont font;
	int part;
	int step;
	Change[] change = new Change[10];
	//public PGraphics letterCut1, letterCut2;
	int stepTimeLine;
	int stepTimeLineReset;
	int hasRun;
	int pauseTime;
	int blackInTime;
	float textMoveIndex;
	
	PImage letterBlack;
	PImage letterWhite;
	
	public END(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		//font = p.createFont("fonts/AppleSDGothicNeo-Bold.otf", 600);
		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}
	
	}
	
	public void reinit() {
		super.reinit();
		p.shaderSelect = 0;
		p.skeletonTracking = false;
		move = 0;
		accel = 0;
		charIndex = -1;
		//letterCut1 = p.createGraphics(p.width, p.height, PConstants.P2D);
		//letterCut2 = p.createGraphics(p.width, p.height, PConstants.P2D);
		part = 1;
		step = 0;
		for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
		}
		p.seqStep = 0;
		pauseTime = 2000;
		blackInTime = 1000;
		textMoveIndex = 0;
		PApplet.println("END");
	}

	public void draw() {
		super.draw();
		
		stepTimeLine = timeLine - stepTimeLineReset;
		partition();
		/*	
		if(change[2].test(charIndex)){
			if(charIndex == 0){
				letterBlack = p.loadImage("pic/show/0.png");
				letterWhite = p.loadImage("pic/show/0.png");
			} else if(charIndex == 1){
				letterBlack = p.loadImage("pic/show/9.png");
				letterWhite = p.loadImage("pic/show/9.png");
			}else if(charIndex == 2){
				letterBlack = p.loadImage("pic/show/10.png");
				letterWhite = p.loadImage("pic/show/2.png");
			}else if(charIndex == 3){
				letterBlack = p.loadImage("pic/show/12.png");
				letterWhite = p.loadImage("pic/show/12.png");
			}else if(charIndex == 4){
				letterBlack = p.loadImage("pic/show/13.png");
				letterWhite = p.loadImage("pic/show/4.png");
			}else if(charIndex == 5){
				letterBlack = p.loadImage("pic/show/5.png");
				letterWhite = p.loadImage("pic/show/3.png");
			}else if(charIndex == 6){
				letterBlack = p.loadImage("pic/show/6.png");
				letterWhite = p.loadImage("pic/show/6.png");
			}else if(charIndex == 7){
				letterBlack = p.loadImage("pic/show/7.png");
				letterWhite = p.loadImage("pic/show/7.png");
			}else if(charIndex == 8){
				letterBlack = p.loadImage("pic/show/8.png");
				letterWhite = p.loadImage("pic/show/9.png");
			}else if(charIndex == 9){
				letterBlack = p.loadImage("pic/show/11.png");
				letterWhite = p.loadImage("pic/show/11.png");
			}else if(charIndex == 10){
				letterBlack = p.loadImage("pic/show/3.png");
				letterWhite = p.loadImage("pic/show/3.png");
			}
		}
		 */
		
		int fadeIN = 255;

		c.beginDraw();
		c.background(0);
		c.fill(255);


		if (part == 0){

		}

		if (part == 1){
			//Rectangles in
			move = (int) p.map(timeLine, 0, 25000, -10, p.width/2);
			move = p.constrain(0 + move,-10,p.width/2);
			int thickness = p.height/32;

			//LEFT
			int fillOffset = 1;
			if( move > p.width/2-1){
				step = 1;
				fillOffset = 0;
			}

			//c.background((p.random(0,1.1f))*255*p.map(fillOffset,0,1,1,0));
			c.noStroke();
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(0,0,p.constrain(0 + move,0,p.width/2),thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(0,thickness * 4,move,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(0,thickness * 8,move,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(0,thickness * 16,move,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(0,thickness * 24,move,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(0,thickness * 10,move,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(0,thickness * 28,move,thickness);
			//c.line(0, thickness * 10, move, thickness);

			//RIGHT
			c.noStroke();
			int move2 = (int) p.map(move, 0, p.width/2,p.width,p.width/2);
			//if(move2 < p.width/2+1) c.translate(0, p.random(-100,100));
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(move2,16,p.width,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(move2,thickness * 3,p.width,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(move2,thickness * 7,p.width,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(move2,thickness * 13,p.width,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(move2,thickness * 17,p.width,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(move2,thickness * 21,p.width,thickness);
			c.fill(255, (int)(p.random(0,1.1f) + fillOffset) * 255);	
			c.rect(move2,thickness * 27,p.width,thickness);
		}
		
		if (part == 2){
			c.fill(255, (int)(p.random(0,1.1f)) * 255);	
			c.rect(0,0,p.width/2,p.height);
			c.fill(255, (int)(p.random(0,1.1f)) * 255);	
			c.rect(p.width/2,0,p.width,p.height);
		
			//if((int) p.random(0,2f) >= 1) c.image(letterBlack,0,0);	
			//else c.image(letterWhite,0,0);


		}

		
		if (part == 4){
			if (p.frameCount % 40 == 0) {
				accel = (int) p.random(50,180);
			}

			if (move > p.width) move = 0;
			move = move + accel;

			if (p.frameCount % 100 < 3) {
				//if((int) p.random(0,2f) >= 1) c.image(letterBlack,0,0);	
				//else c.image(letterWhite,0,0);
			} 

			if (p.frameCount % 100 > 40) {
				c.rect(p.width - move,0,100,p.height);
			}
		}
		
		if (part == 5){

			if (p.frameCount % 40 == 0) {
				accel = (int) p.random(70,180);
			}

			if (move > p.width) move = 0;
			move = move + accel;

			if (charIndex >= dreamingNow.length) charIndex = 10;
			if (p.frameCount % 30 < 1) {
				//if((int) p.random(0,2f) >= 1) c.image(letterBlack,0,0);	
				//else c.image(letterWhite,0,0);
			} 
			if (p.frameCount % 100 > 20) {
				c.rect(0 + move,0,10 + (p.frameCount % 100),p.height);
			}
		}

		if (part == 6){
			//END ACTION
			if (p.frameCount % 50 == 0) {
				accel = (int) p.random(70,180);
			}

			if (move > p.width) move = 0;

			move = move + accel;

			if (charIndex >= dreamingNow.length) charIndex = 10;

			if (p.frameCount % 100 > 10) {
				c.rect(0 + move,0,10 + (p.frameCount % 10000),p.height);
			}


		}

		if (part == 7){
			//END ACTION
			if (p.frameCount % 50 == 0) {
				accel = (int) p.random(70,180);
			}

			if (move > p.width) move = 0;
			move = move + accel;

			if (charIndex >= dreamingNow.length) charIndex = 10;

			if (p.frameCount % 100 > 10) {
				c.rect(0,0,p.width - move,p.height);
			}

		}


		if (part == 9){
			//silence!	
		}
		//c.background(0);
		c.fill(255);
		//c.rect(0,0, p.width/2, p.height);
		c.endDraw();

	}

	public void partition(){
		
		if(change[1].test(part)){

			if(part != 0 && part != 9){
				OscMessage myMessage = new OscMessage("/introClips");
				myMessage.add(p.random(0,17)); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.soundComputerClips);
			}

		}

		if(p.seqStep <= 12){
			if(change[0].test(p.seqStep)){
				stepTimeLineReset = timeLine;
				stepTimeLine = 0;
				charIndex++;
				charIndex=p.constrain(charIndex,0,10);
				hasRun = 0;
				pauseTime = (int) p.random(1000,2000);
				blackInTime = (int) p.random(500,1000);
			}

			if (stepTimeLine % 100 < 60) part = (int) p.random(0,6);

			//part = 1;
			if (stepTimeLine > blackInTime) {
				part = 0;
			}
			//part = 1;
			if (stepTimeLine > pauseTime && hasRun == 0) {
				p.seqStep++;
				hasRun = 1;

			}
		}
		if(p.seqStep > 9 && p.seqStep < 14){
			if(change[0].test(p.seqStep)){
				stepTimeLineReset = timeLine;
				stepTimeLine = 0;
			}


			if (stepTimeLine % 100 < 10 && stepTimeLine < 2500) part = (int) p.random(6,9);
			else  part = (int) p.random(6,8);

			if (stepTimeLine > 1000){
				textMoveIndex = p.constrain(p.map(stepTimeLine,1000,5000,0,1),0,1);
			}

			if(stepTimeLine > 7000){
				p.seqStep = 14;
			}
		}

		if(p.seqStep == 14){
			if(change[0].test(p.seqStep)){
				OscMessage myMessage = new OscMessage("/cueNum");
				myMessage.add(22); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.soundComputer);
				myMessage = new OscMessage("/cueGo");
				myMessage.add(1); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.soundComputer);
			}
			part = 0;
		}

	
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
}