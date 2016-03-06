package seq;

import java.util.ArrayList;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;
import seq.PIXELS.Change;
import toxi.geom.Vec2D;
import toxi.geom.Rect;
import toxi.physics.ParticlePath;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;


public class METABALLS extends Sketch{
	
	//Phys vars
	int NUM_PARTICLES;
	VerletPhysics2D physics;
	GravityBehavior gravity;
	float gravityForce = 0.0f; // entre 0. et 0.9
	float attractorForce = 3.0f; // * x to compensate for slow frameRate
	float attractorRadius = 200;
	int degagement = 0; //Empêcher les particules de se morfondre dans les coins
	//Timings
	int stepTimeLine;
	int stepTimeLine2;
	int stepTimeLineReset;
	int stepTimeLineReset2;
	//Kinect Hands
	AttractionBehavior handAttractor;
	AttractionBehavior handAttractor2;
	Vec2D handPos = new Vec2D(p.width/2, p.height/2);
	Vec2D handPos2 = new Vec2D(p.width/2, p.height/2);
	//Kinect Hands to OneHand
	int kinect1Xsub;
	int kinect1Ysub;
	int kinect1X2sub;
	int kinect1Y2sub;
	int kinect1Clicksub;
	int kinectOneHandX;
	int kinectOneHandY;
	int previouskinectOneHandY;
	//Sequence steps vars
	int step = 0;
	int hasRun = 0;
	int hasRun2 = 0;
	//Shader var
	float blobSize = 50000.f;
	//La sphère que Michel aime, exemple de processing
	int cuantos = 10000;
	float[] z = new float[cuantos]; 
	float[] phi = new float[cuantos]; 
	float[] largos = new float[cuantos]; 
	float radio;
	float rx = 0;
	float ry =0;
	int liveCue = 13;
	PImage img =p.createImage(100, 1, p.ARGB); //L'image qui contient les coords, le premier chiffre sera le diviseur dans le shader coordo;
	float coordy = 0;
	Change[] change = new Change[10]; //3
	

	public METABALLS(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		liveCue = 13;
		//Physics setup
		physics = new VerletPhysics2D(); 
		physics.setWorldBounds(new Rect(0+degagement, 0+degagement, p.width-degagement*2, p.height-degagement*2));
		gravity = new GravityBehavior(new Vec2D(0, gravityForce));
		physics.addBehavior(gravity);

		
		//Change
		 for (int i = 0; i < change.length; i++) {
			  change[i] = new Change();
		  }


		  p.noiseDetail(3);

	}

	public void reinit() {

		super.reinit();
		kill();
		p.metaballs.shader(p.metaball);
		
		NUM_PARTICLES = 0;
		
		
		blobSize = 50000.f;
		liveCue = 13;  // Cue de départ dans Ableton Live
		
		gravityForce = 0.f;
		step = 0;	
		hasRun = 0;
		hasRun2 = 0;
		
		   //Change reset
	    for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
	    }
	    
		//Zone de rebondissement
	    for(int x = degagement; x < p.width-degagement; x=x+10){
		    physics.addBehavior(new AttractionBehavior(new Vec2D(x,degagement), 20, -0.04f, 0.01f));
		    physics.addBehavior(new AttractionBehavior(new Vec2D(x,p.height-degagement), 20, -0.04f, 0.01f));
	    }
	    for(int x = degagement; x < p.height-degagement; x=x+10){
		    physics.addBehavior(new AttractionBehavior(new Vec2D(degagement,x), 20, -0.04f, 0.01f));
		    physics.addBehavior(new AttractionBehavior(new Vec2D(p.width-degagement,x), 20, -0.04f, 0.01f));
	    }
	     
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		
		stepTimeLine = 0;
		stepTimeLine2 = 0;
		p.shaderSelect = 3;
		
		for(int i = 0; i < NUM_PARTICLES; i++){ //physics.particles.size()	
			VerletParticle2D particule = physics.particles.get(i);
			//img.pixels[i] = p.color(p.map(particule.x,0,p.width,0f,255f),p.map(particule.y,0,p.height,0f,255f),0.f);
			//le B de vient le multiplicateur du R (coord x ) et le A mult du G (coord y) afin d'avoir une meilleur rsolution
			coordy = p.map(particule.y, 0.f, p.height, p.height, 0.f);
			img.pixels[i] = p.color(particule.x % 256, coordy % 256,(int) p.map((int)(particule.x/256),0,4,0,256),(int) p.map((int)(coordy/256),0,2,0,256)); 
		}
		
		p.metaball.set("blobSize", blobSize);
		p.metaball.set("texture", img); //On envoie l'image à titre de texture vers le shaders
		p.metaball.set("NUM_PARTICLES", NUM_PARTICLES);
		p.println("METABALLS");
	}

	public void draw() {
	
		p.skeletonTracking = false;
		
		super.draw();
		
		handsToOneHandRaw();
		stepTimeLine = timeLine - stepTimeLineReset;
		stepTimeLine2 = timeLine - stepTimeLineReset2;
		
		if(step >= 20){
			physics.update();
		}
		
		
		for(int i = 0; i<physics.particles.size();i++){
			VerletParticle2D particule = physics.particles.get(i);
			Vec2D vel = particule.getVelocity();
			if( vel.x < -1. || vel.x > 1. || vel.y < -1. || vel.y > 1.){
				particule.scaleVelocity(0.95f);
			} else particule.scaleVelocity(1.f);
		}
		
	
 
		gravityForce = 0.0f;
		gravity.setForce(new Vec2D(0, gravityForce));
		
		blobSize = p.constrain(blobSize, 1, 600000000);
		
		c.beginDraw();
		c.background(0);
		if(step<22){ //Shader needs a black rect...
			c.fill(0);
			c.noStroke();
			c.rect(0,0,p.width,p.height);
		}
		c.stroke(255);
		partition();
		c.endDraw(); 
		
		coordy = 0;
		img =p.createImage(100, 1, p.ARGB);
		for(int i = 0; i < NUM_PARTICLES; i++){ //physics.particles.size()	
			VerletParticle2D particule = physics.particles.get(i);
			//img.pixels[i] = p.color(p.map(particule.x,0,p.width,0f,255f),p.map(particule.y,0,p.height,0f,255f),0.f);
			//le B de vient le multiplicateur du R (coord x ) et le A mult du G (coord y) afin d'avoir une meilleur rsolution
			coordy = p.map(particule.y, 0.f, p.height, p.height, 0.f);
			img.pixels[i] = p.color(particule.x % 256, coordy % 256,(int) p.map((int)(particule.x/256),0,4,0,256),(int) p.map((int)(coordy/256),0,2,0,256)); 
		}
		
		p.metaball.set("blobSize", blobSize);
		p.metaball.set("texture", img); //On envoie l'image à titre de texture vers le shaders
		p.metaball.set("NUM_PARTICLES", NUM_PARTICLES);
		
	}

	public void partition() {
		
		if(change[5].test(p.seqStep) && p.seqStep > 0){
			if (p.seqStep == 1){
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(101); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.cueInterface);
			}
			
			step = p.seqStep+20;
		}
		
		if(kinect1Clicksub == 1.0f) {
			////////MOUSE PRESSED
			
			if (change[1].test(kinect1Clicksub) && step < 20) {
				
				if(hasRun2 == 0){ //TRIGGER
					stepTimeLineReset2 = timeLine; 
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); /* add an int to the osc message */
					p.oscP5.send(myMessage2, p.soundComputer);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); /* add an int to the osc message */
					p.oscP5.send(myMessage2, p.soundComputer);
					liveCue++;
					hasRun2 = 1;
				}
				
				addParticle(kinectOneHandX,kinectOneHandY);
				step++;
			
			}
		}
		if (kinect1Clicksub == 0f) change[1].test(kinect1Clicksub);

		
		//Voir la suite dans hands()
		hands();
		
		if(change[4].test(step)){
			stepTimeLine = 0;
			stepTimeLineReset = timeLine;
		}
		
		if(step == 20 && p.kinect1X >= p.kinect1X2 - 40){
			hasRun = 1;
		}
		
		if(step == 21 && p.kinect1click == 1.0f && p.kinect1click2 == 1.0f){
			
			if(blobSize > 9000){
				blobSize = blobSize - 30;
			} 
			if(blobSize <= 9000 && blobSize > 6000){
				blobSize = blobSize - 14;
			} 
			if(blobSize <= 6000 && blobSize > 1000){
				blobSize = blobSize - 8;
			}
			if(blobSize <= 1000){
				blobSize = blobSize - 2;
			}
		}

		if(step == 22){
			p.shaderSelect = 0;
		}

		if(step == 23){
			//p.skeletonTracking = true;
			p.shaderSelect = 1;
			c.lights();
			c.resetShader();
			//c.camera(p.width/2.0f, p.height/1.3f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
			//c.translate(p.map(p.rightHand.x, -1450,1450,0,p.width),p.map(p.rightHand.y, 400,-450,0,p.height), p.map(p.rightHand.z, 1000, 4000, 100, -1000));
		}
		
		if(stepTimeLine2 > 143215 && stepTimeLine2 < 144215 ){
			step = 21;
		
		}
		
	
		if(stepTimeLine2 > 253000){
		
			for(int i = 0; i < NUM_PARTICLES; i++){
			VerletParticle2D particule = physics.particles.get(i);
			particule.x = p.width/2;
			particule.y = p.height/2;
			}
			blobSize = blobSize * 1.018f;
			//blobSize = p.map(p.constrain(stepTimeLine,230000,264871), 230000, 264871, 1, 9000000);
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

	public void hands(){
		
		//HAND1
		if(p.kinect1click == 1.0f) {
			if (change[2].test(p.kinect1click)) {
				
				if(step == 20 && stepTimeLine > 2000){
					for(int i = 0; i < 10; i++){
						//Groupe 1
						VerletParticle2D particule = physics.particles.get(i);
						handAttractor = new AttractionBehavior(handPos, 2000, 0.6f); //0.6 si kinect pas connectée
						particule.addBehavior(handAttractor);
					}
				}

				if(step == 21){
					handAttractor = new AttractionBehavior(handPos, 600, 0.6f);
					physics.addBehavior(handAttractor);
					
				}

			}
			handPos.set(p.kinect1X, p.kinect1Y);
		}

		if (p.kinect1click == 0f){
			if (change[2].test(p.kinect1click)) {
				if(step == 20){
					for(int i = 0; i < 10; i++){
						//Groupe 1
						VerletParticle2D particule = physics.particles.get(i);
						particule.removeAllBehaviors();
						particule.addBehavior(new AttractionBehavior(particule, 100.f, 5.8f, 0.05f));
					}
				}
				
				if(step == 21){
					physics.removeBehavior(handAttractor);
					for(int i = 0; i < 10; i++){
						//Groupe 1
						VerletParticle2D particule = physics.particles.get(i);
						particule.removeAllBehaviors();
						particule.addBehavior(new AttractionBehavior(particule, 100.f, 5.8f, 0.05f));
					}
				}
			}
		}

		//HAND2
		if(p.kinect1click2 == 1.0f) {
			if (change[3].test(p.kinect1click2)) { 
				if(step == 20  && stepTimeLine > 2000){
					for(int i = 10; i < 20; i++){
						//Groupe 2
						VerletParticle2D particule2 = physics.particles.get(i);
						handAttractor2 = new AttractionBehavior(handPos2, 2000, 0.6f);
						particule2.addBehavior(handAttractor2);
						
					}
				}
				
				if(step == 21){
					handAttractor2 = new AttractionBehavior(handPos2, 600, 0.6f);
					physics.addBehavior(handAttractor2);

				}
			}
			handPos2.set(p.kinect1X2, p.kinect1Y2);
			if(step == 20){
				for(int i = 10; i < 20; i++){
					//Groupe 2
					VerletParticle2D particule2 = physics.particles.get(i);
				}
			}
		}
		if (p.kinect1click2 == 0f){
			if (change[3].test(p.kinect1click2)) { 
				if(step == 20){
					for(int i = 10; i < 20; i++){
						//Groupe 2
						VerletParticle2D particule2 = physics.particles.get(i);
						particule2.removeAllBehaviors();
						particule2.addBehavior(new AttractionBehavior(particule2, 100.f, 5.8f, 0.05f));
					}
				}
				
				if(step == 21){
					physics.removeBehavior(handAttractor2);
					for(int i = 10; i < 20; i++){
						//Groupe 2
						VerletParticle2D particule2 = physics.particles.get(i);
						particule2.removeAllBehaviors();
						particule2.addBehavior(new AttractionBehavior(particule2, 100.f, 5.8f, 0.05f));
					}
				}
			}
		}
	}

	public void addParticle(int x, int y) {

		VerletParticle2D particule = new VerletParticle2D(Vec2D.randomVector().scale(2).addSelf(x, y));
		physics.addParticle(particule);
		// negative attraction force field around the new particle
		particule.addBehavior(new AttractionBehavior(particule, 100.f, 5.8f, 0.05f));
		NUM_PARTICLES++;
	}

	public void kill() {
		physics.clear();
		for (VerletParticle2D particule : physics.particles) {
			particule.lock();
			particule.clearForce();
			particule.clearVelocity(); 
			particule.removeAllBehaviors();
			physics.removeParticle(particule);
			physics.removeBehavior(gravity);
			physics.removeBehavior(handAttractor);
			physics.removeBehavior(handAttractor2);
		}
		physics.behaviors.clear();
		p.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
	}

	
	public void keyPressed() {
		if(p.key == '1'){
			step = 21;
		}
		if(p.key == '2'){
			step = 22;
		}
		if(p.key == '3'){
			step = 23;
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