package seq;

import java.util.ArrayList;
import java.util.Iterator;

//import dreaming.*;
import seq.PIXELS.Change;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics.ParticlePath;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

public class CURSORS extends Sketch{
	
	int NUM_PARTICLES = 350;
	VerletPhysics2D physics;
	
	int graphWidth = p.width-100;
	int graphHeight =  p.height-100;
	
	float easing = 0.01f;
	
	boolean inverse = false;
	int inverseInt = 0;
	int kinect1xInvRelative;
	int kinect1x2InvRelative;
	
	boolean grosCurseur1 = false;
	boolean grosCurseur2 = false;
	int grosCurseur1x;
	int grosCurseur2x;
		
	ArrayList cursors;
	ArrayList noisers;
	PFont f;
	
	Change[] change = new Change[11];

	Vec2D mousePos;
	Vec2D handPos = new Vec2D(p.width, p.height);
	Vec2D handPos2;
	Vec2D handPos3;
	Vec2D handPos4;
	Vec2D handPos5;
	Vec2D handPos6;
	
	Vec2D flocking1 = new Vec2D(-200, p.height/2);
	Vec2D flocking2 = new Vec2D(p.width+200, p.height/2);
	Vec2D flocking3 = new Vec2D(p.width/2, p.height/2);
	Vec2D flocking4 = new Vec2D(p.width/2, p.height/2);
	
	AttractionBehavior mouseAttractor;
	AttractionBehavior handAttractor;
	AttractionBehavior handAttractor2;
	AttractionBehavior handAttractor3;
	AttractionBehavior handAttractor4;

	AttractionBehavior flocking1Attractor;
	AttractionBehavior flocking2Attractor;
	AttractionBehavior flocking3Attractor;
	AttractionBehavior flocking4Attractor;
	
	int flocking1x;
	int flocking1y;
	boolean flocking1Origin = false;
	int flocking2x;
	int flocking2y;
	boolean flocking2Origin = false;
	int flocking3x = p.width/2;
	int flocking3y = p.height/2;
	boolean flocking3Origin = false;
	boolean flocking4Origin = false;
	boolean flocking2OriginMofo = false;
	
	int hasRun = 0;
	int hasRun2 = 0;
	int hasRun3 = 0;
	
	int liveCue = 7;
	
	int mouseXsub;
	int mouseYsub;
	int kinect1Xsub;
	int kinect1Ysub;
	int kinect1X2sub;
	int kinect1Y2sub;
	int kinect1Clicksub;
	int kinectOneHandX;
	int kinectOneHandY;

	//urn vars
	int[] urn = new int[12]; //11 lettres + 1
	int[] urnBirthOrder = new int[11]; //11 lettres + 1 
	int picked;
	int urnOrder= 0;
	

	Vec2D[] gangLoc = new Vec2D[248];
	
	float SPEED = 1;
	int maxItems;
	int boidCounter = 0;
	int boidCounter2 = 0;
	PImage curseur;
	PImage doigt;
	PImage mad;
	PImage devil;
	PImage img2;
	PImage img3;
	PImage img4;

	int degagement= -100; //réduire les boundaries
	int step = 0;
	boolean form = false;
	boolean reset = false;
    int	counter;

	
	int stepTimeLine;
	int stepTimeLineReset;
	int cue2 = 0;
	
	ParticleSystem ps;

	public CURSORS(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);

		liveCue = 7;
		
		curseur = p.loadImage("cursor.png");
		doigt = p.loadImage("doigt.png");
		mad = p.loadImage("simley.png");
		devil = p.loadImage("devil2.png");
		img2 = p.loadImage("doigt.png");
		img3 = p.loadImage("bigHappy.png");
		img4 = p.loadImage("bigMad2.png");
		
		physics = new VerletPhysics2D();
		//physics.setDrag(0.00f);
		physics.setWorldBounds(new Rect(0+degagement, 0+degagement, p.width-degagement*3, p.height-degagement*3));
		// the NEW way to add gravity to the simulation, using behaviors
		physics.addBehavior(new GravityBehavior(new Vec2D(0, 0.f)));

		f = p.createFont("Georgia",48,true);
		c.textFont(f);
		c.textAlign(p.CENTER);

		cursors = new ArrayList();
		noisers = new ArrayList();

		// get the name of this class and print it
		className = this.getClass().getSimpleName();

		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}

		
	}

	public void reinit() {
		
		super.reinit();
		p.skeletonTracking = false;
		p.wallEasing = 0.2f; //Easing setting for wall Kinect
		p.shaderSelect = 0;
		c.tint(255,255);

		kill();
		
		liveCue = 7;  // Cue de départ dans Ableton Live
		p.seqStep =  0;
		
		for(int i=0; i<urn.length; i++){
			urn[i] = 0;	
		}
		
		urnOrder = 0;
		
		cursors.clear();
		noisers.clear();
	    boidCounter = 0;
	    boidCounter2 = 0;
	    counter = 0;
	    
	    
	    //Change reset
	    for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
	    }

	    //Déf de pos de particules de la gang finale (step 6);
	    String[] lines = p.loadStrings("cursorPos.txt");
	    for(int i = 0; i < lines.length; i++){
	    	String[] cursorPos = p.split(lines[i], ' ');
	    	if(cursorPos.length == 2){
	    		gangLoc[i] =  new Vec2D(Integer.parseInt(cursorPos[0]), Integer.parseInt(cursorPos[1]));
	    	}
	    	
	    }
	     
	   
	    //Attractors and flocking stuff
	    mouseXsub = p.mouseX;
	    mouseYsub = p.mouseY;
	    kinect1X2sub = (int) p.kinect1X;
	    kinect1Ysub = (int) p.kinect1Y;
	    kinect1X2sub = (int) p.kinect1X2;
	    kinect1Y2sub = (int) p.kinect1Y2;

	    //flocking1x = -200;
		//flocking1y = p.height/2;
		flocking1Origin = false;
	    flocking2x = p.width+200;
		flocking2y = p.height/2;
		flocking2Origin = false;
		flocking2OriginMofo = false;
		flocking3x = p.width/2;
		flocking3y = p.height/2;
		flocking3Origin = false;
		hasRun = 0;
		hasRun2 = 0;
		hasRun3 = 0;
		
		grosCurseur1 = false;
		grosCurseur2 = false;
		int grosCurseur1x = -400;
		int grosCurseur2x = p.width + 400;
		
		inverse = false;
		
		
	    c.textFont(p.font,60);

		//Cam reset
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		
		easing = 0.5f;
		
		
		step = 0;//step = 7;
		stepTimeLine = 0;
		stepTimeLineReset = timeLine;
		//liveCue = 6 + 7;
		/* uncomment if tu veux aller directement aux curseurs qui attaquent
		step = 3;
		for(int i = 0 ; i <3; i++){ 
			boidAdd(-100,250,30);
			Boid b = (Boid) lettres.get(i);
			b.changeSpeed(0f);
		}
		*/
	
		form = true;

		ps = new ParticleSystem(new PVector(p.width/2,50));

		p.println("CURSORS");
		
		
	}

	public void draw() {
		
		super.draw();
		
		stepTimeLine = timeLine - stepTimeLineReset;
		//p.seqStep = 3;
		c.beginDraw();
		c.hint(c.DISABLE_DEPTH_TEST);
		c.background(0);
		
		
		c.pushMatrix();
		
		hands();
		
		//ps.run(); //when bites!
		
		physics.update();
		
		//float oscil =p.sin(((p.frameCount/50%100) / 100.f ) * 3.1416f);
		
		for(int i = 0; i < boidCounter; i++){
			Boid b = (Boid) cursors.get(i);
			Noiser n = (Noiser) noisers.get(i);
			VerletParticle2D particule = physics.particles.get(i);
			Vec2D vel = particule.getVelocity();
			if( vel.x < -1. || vel.x > 1. || vel.y < -1. || vel.y > 1.){
			particule.scaleVelocity(0.9f);
			} else particule.scaleVelocity(1.f);

			b.arrive(new PVector(particule.x,particule.y));
			b.run();
			c.fill(255,255);
			//c.ellipse(particule.x,particule.y,10,10); //debug
		}
		
		
		events();
		partition();
		
		
		c.popMatrix();
		//c.text(kinectOneHandY,10,100);
		//c.image(img,0,0,160,160);
		c.fill(0,fade);
		c.rect(-10,-10,p.width+20,p.height+20);
		c.endDraw();
	
	}

	void partition(){
		
		//AUTO INVERSE
		if (step < 1 && stepTimeLine >= 25290 && stepTimeLine <= 26290){
			// à partir de l'entrée 1
			p.seqStep = 1;
			stepTimeLineReset = timeLine;
		}
		//AUTO INVERSE 2
		
		if (step < 2 && step >= 1 && stepTimeLine >= 27000){
			// à partir de l'entrée 1
			p.seqStep = 2;
			stepTimeLineReset = timeLine;
		}

		/*
		//Les cinqs de gauche se mettent en place et attaquent
		if(step == 4){
			
			int cue = 2000;
			
			if(stepTimeLine >= cue && stepTimeLine <=  cue*7){
				VerletParticle2D particule = physics.particles.get(3);
				particule.set(new Vec2D(100, 100));
				Boid b = (Boid) cursors.get(3);
				b.changeSpeed(8f);
				b.rotateToward = new PVector(p.width,p.height);
				b.rotate = true;
				
				if(b.loc.x > 0 && hasRun2 < 10){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 10;
				}
				
			}
			
			if(stepTimeLine >= cue*2 && stepTimeLine <=  cue*7){
				VerletParticle2D particule = physics.particles.get(4);
				particule.set(new Vec2D(80, 180));
				Boid b = (Boid) cursors.get(4);
				b.changeSpeed(8f);
				b.rotateToward = new PVector(p.width,p.height);
				b.rotate = true;
				
			
				if(b.loc.x > 0 && hasRun2 < 11){
					
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 11;
				}
			}
			
			if(stepTimeLine >= cue*3 && stepTimeLine <=  cue*7){
				VerletParticle2D particule = physics.particles.get(5);
				particule.set(new Vec2D(50, 260));
				Boid b = (Boid) cursors.get(5);
				b.changeSpeed(7f);
				b.rotateToward = new PVector(p.width,p.height);
				b.rotate = true;

				if(b.loc.x > 0  && hasRun2 < 12){
					
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 12;
				}
			}
			
			if(stepTimeLine >= cue*4 && stepTimeLine <=  cue*7){
				VerletParticle2D particule = physics.particles.get(6);
				particule.set(new Vec2D(70, 340));
				Boid b = (Boid) cursors.get(6);
				b.changeSpeed(7f);
				b.rotateToward = new PVector(p.width,p.height);
				b.rotate = true;
				
				if(b.loc.x > 0  && hasRun2 < 13){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 13;
				}
			}
			
			if(stepTimeLine >= cue*5 && stepTimeLine <=  cue*7){
				VerletParticle2D particule = physics.particles.get(7);
				particule.set(new Vec2D(100, 420));
				Boid b = (Boid) cursors.get(7);
				b.changeSpeed(6f);
				b.rotateToward = new PVector(p.width,p.height);
				b.rotate = true;
				
				if(b.loc.x > 0 && hasRun2 < 14){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 14;
				}
			}
			//ATTAQUENT
			if(stepTimeLine >= cue*7){
				//  vitesse plus lente 
				for(int i = 0; i < boidCounter; i++){
					Boid b = (Boid) cursors.get(i);
					b.changeSpeed(2f);
					b.rotateToward = new PVector(p.width,p.height);
					b.rotate = true;
					
					
				}
				flocking(1);
				//flocking1x=p.constrain(flocking1x+3,-100,p.width/2);
				flocking1.set(900,450);
				
				if(hasRun2 < 15){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 15;
				}
			}
			//RETOUR AU BERCAIL
			if(flocking1Origin){
				// retour vitesse plus rapide 
				for(int i = 0; i < boidCounter; i++){
					Boid b = (Boid) cursors.get(i);
					b.changeSpeed(10f);
					b.rotateToward = new PVector(-300, p.height/2);
					b.rotate = true;
				}
				flocking1.set(-300, p.height/2);
				////LIVE CUE 
				if(hasRun2 < 16){
					OscMessage myMessage = new OscMessage("/cueNum");
					myMessage.add(liveCue); 
					p.oscP5.send(myMessage, p.myRemoteLocation);
					myMessage = new OscMessage("/cueGo");
					myMessage.add(1); 
					p.oscP5.send(myMessage, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 16;
				}
			}
		}

		//Les cinqs de droite se mettent en place et attaquent
		if(step == 5){
			
			int cue = 2000;
			if(stepTimeLine >= cue && stepTimeLine <=  cue*9){
				VerletParticle2D particule = physics.particles.get(8);
				particule.set(new Vec2D(p.width-120, 100));
				Boid b = (Boid) cursors.get(8);
				b.changeSpeed(8f);
				b.rotateToward = new PVector(0, p.height-100);
				b.rotate = true;
				
				if(b.loc.x < p.width && hasRun2 < 17){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 17;
				}
			}
			
			if(stepTimeLine >= cue*2 && stepTimeLine <=  cue*9){
				VerletParticle2D particule = physics.particles.get(9);
				particule.set(new Vec2D(p.width-120, 420));
				Boid b = (Boid) cursors.get(9);
				b.changeSpeed(7f);
				b.rotateToward = new PVector(0, p.height-100);
				b.rotate = true;
				
				if(b.loc.x < p.width && hasRun2 < 18){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 18;
				}
			}
			
			if(stepTimeLine >= cue*3 && stepTimeLine <=  cue*9){
				VerletParticle2D particule = physics.particles.get(10);
				particule.set(new Vec2D(p.width-150, 180));
				Boid b = (Boid) cursors.get(10);
				b.changeSpeed(7f);
				b.rotateToward = new PVector(0, p.height-100);
				b.rotate = true;
				
				if(b.loc.x < p.width && hasRun2 < 19){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 19;
				}
			}
			if(stepTimeLine >= cue*4 && stepTimeLine <=  cue*9){
				VerletParticle2D particule = physics.particles.get(11);
				particule.set(new Vec2D(p.width-170, 340));
				Boid b = (Boid) cursors.get(11);
				b.changeSpeed(7f);
				b.rotateToward = new PVector(0, p.height-100);
				b.rotate = true;
				
				if(b.loc.x < p.width && hasRun2 < 20){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 =20;
				}
			}
			
			if(stepTimeLine >= cue*5 && stepTimeLine <=  cue*9){
				VerletParticle2D particule = physics.particles.get(12);
				particule.set(new Vec2D(p.width-220, 260));
				Boid b = (Boid) cursors.get(12);
				b.changeSpeed(7f);
				b.rotateToward = new PVector(0, p.height-100);
				b.rotate = true;
				
				if(b.loc.x < p.width && hasRun2 < 21){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 21;
				}
			}
			//BIG MOFO
			if(stepTimeLine >= cue*7 && stepTimeLine <=  cue*9){
				VerletParticle2D particule = physics.particles.get(13);
				particule.set(new Vec2D(p.width-150, 330));
				Boid b = (Boid) cursors.get(13);
				b.changeSpeed(5f);
				b.rotateToward = new PVector(0, p.height-90);
				b.rotate = true;
				
				if(b.loc.x < p.width+100 && hasRun2 < 22){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 22;
				}
			}
			
			//ATTAQUENT
			if(stepTimeLine >= cue*9){
				for(int i = 0; i < boidCounter; i++){
					Boid b = (Boid) cursors.get(i);
					b.changeSpeed(2f);
				}
				//BIG MOFO EST LENTE!
				Boid b = (Boid) cursors.get(13);
				b.changeSpeed(1f);
				
				flocking(2);
				flocking2.set(100,450);
				if(hasRun2 < 23){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 23;
				}
			}
		}
		if(step == 6){
			//RETOUR AU BERCAIL
			if(flocking2Origin){
				flocking2.set(p.width+400, p.height/2);
				for(int i = 0; i < boidCounter-1; i++){
					Boid b = (Boid) cursors.get(i);
					b.changeSpeed(10f);
					b.rotateToward = new PVector(p.width+300, p.height/2);
					b.rotate = true;
				}
				
				//BIG MOFO EST LENTE!
				Boid b = (Boid) cursors.get(13);
				b.changeSpeed(0f);
				
				
				if(hasRun2 < 24){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 24;
				}
			}
			
			if(flocking2OriginMofo){
				
				flocking2.set(p.width+400, p.height/2);
				Boid b = (Boid) cursors.get(13);
				b.changeSpeed(5f);
				b.rotateToward = new PVector(p.width+300, p.height/2);
				b.rotate = true;
				
				if(hasRun2 < 25){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 25;
				}
				//if(b.loc.x > p.width+ 100) p.seqStep = 5;
				
			}
			
		}
		*/
		
		//LA GIGA BANDE SE DÉPLOIE!
		int cue = 1000;
		
		if(step == 7){
			
			if(hasRun2 < 26 && stepTimeLine <= cue*2){
				
				hasRun2 = 26;
				for(int i = 0 ; i <248; i++){
					boidAdd(p.width / 2, -100,30);
					Boid b = (Boid) cursors.get(i);
					b.changeSpeed(0f);
					b.imageMode = 3; //devils!
				}
				
			}

			if(boidCounter2 < 1 && stepTimeLine <= cue*10 && stepTimeLine <= cue*2){
				stepTimeLineReset = timeLine; //remove if taking from the top
				//boidAdd(p.width / 2, -100,30);
				Boid b = (Boid) cursors.get(0);
				b.changeSpeed(3f);
				b.rotateToward = new PVector(p.width/2,p.height);
				b.rotate = false;
				boidCounter2++;
			}

			if(boidCounter2 < 248 && stepTimeLine <= cue*10 && stepTimeLine >= cue*3){
				//boidAdd(p.width / 2, -100,30);
				Boid b = (Boid) cursors.get(boidCounter2);
				b.changeSpeed(3f);
				b.rotateToward = new PVector(gangLoc[boidCounter2].x, gangLoc[boidCounter2].y);
				b.rotate = false;
				boidCounter2++;
			}

			if(stepTimeLine <= cue*10){
				for(int i = 0; i<boidCounter2; i++){
					VerletParticle2D particule = physics.particles.get(i);
					Boid b = (Boid) cursors.get(i);
					particule.set(gangLoc[i]);
					if(b.loc.x >= particule.x-10 &&  b.loc.x <= particule.x+10 && b.loc.y >= particule.y-10 &&  b.loc.y <= particule.y+10 ){
						b.rotateToward = new PVector(p.width/2,p.height);
					}
				}
			}

			if(stepTimeLine >= cue*10 && stepTimeLine <  cue*10+300){
				//Soriente vers
				for(int i = 0; i<boidCounter2; i++){
					Boid b = (Boid) cursors.get(i);
					b.changeSpeed(0.1f);
					b.rotateToward = new PVector(p.width/2,p.height);
					b.rotate = false;
					VerletParticle2D particule = physics.particles.get(i);	
					particule.clearForce();
					particule.scaleVelocity(0.f);
				}
				/*
				if(hasRun2 < 27){
					p.println("all");
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 27;
				}
				*/
			}

			if(stepTimeLine >= cue*10+300 && stepTimeLine <=  cue*12){
				for(int i = 0; i<boidCounter; i++){
					Boid b = (Boid) cursors.get(i);
					b.changeSpeed(0.1f);
					VerletParticle2D particule = physics.particles.get(i);	
					particule.clearForce();
					particule.scaleVelocity(0.f);
					//VerletParticle2D particule = physics.particles.get(i);	
					//particule.set(new Vec2D(p.width/2,500));
				}
			}
			
			if(stepTimeLine >= cue*12){ //CHANGE CUE TIME IN HAND STEP6 if CHANGED HERE
				//ATTAQUE?
				for(int i = 0; i<boidCounter; i++){
					Boid b = (Boid) cursors.get(i);
					b.changeSpeed(16f);
					b.rotate = false;
				}
				
				if(hasRun2 < 28){
					//Sound cue ATTAQUE
					
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue);  //add an int to the osc message
					p.oscP5.send(myMessage2, p.soundComputer);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); // add an int to the osc message 
					p.oscP5.send(myMessage2, p.soundComputer);
					liveCue++;
					
					hasRun2 = 28;
					
				}
				
			}
			
			

		}
		
		//CURSEURS EN FORME D'ÉMOTICON.... à partir de l'image d'une
		if(grosCurseur1){			
			/*
			int counter=0;
			int tableY=0;
			for (VerletParticle2D particule : physics.particles) {
				if(counter%37 == 0){
					tableY++;
				}
				particule.set(new Vec2D(((counter%36)+6)*24, (tableY*24)+12));
				particule.clearForce();
				particule.clearVelocity();
				counter++;
			}*/
			/*
			if(hasRun2 < 30){
				OscMessage myMessage2 = new OscMessage("/cueNum");
				myMessage2.add(liveCue); 
				p.oscP5.send(myMessage2, p.myRemoteLocation);
				myMessage2 = new OscMessage("/cueGo");
				myMessage2.add(1); 
				p.oscP5.send(myMessage2, p.myRemoteLocation);
				liveCue++;
				hasRun2 = 30;
			}
			*/
			int counter = 0;
			
			grosCurseur1x = p.constrain(grosCurseur1x+2,-1000,p.width/2-100);
			for(int j = 0; j < 128; j+=p.map(j,0,128,10,8)){
				for(int i = 0; i < 128; i+=5){
					int c = img3.get(i, j);
					if(c < 0){
						VerletParticle2D particule = physics.particles.get(counter);
						particule.set(new Vec2D(i*3+grosCurseur1x, j*3));
						particule.clearForce();
						particule.clearVelocity();
						counter++;
						counter=p.constrain(counter, 0, physics.particles.size() -1 );			
					}	
				}
			}
			
			
			//La moitié pointe à gauche l'autre à droite
			for(int i = 0; i<counter; i++){
				Boid b = (Boid) cursors.get(i);
				b.rotate = true;
				b.rotateToward = new PVector(p.width,p.height/2);
			}

			for(int i = counter; i<physics.particles.size(); i++){
				Boid b = (Boid) cursors.get(i);
				b.rotate = true;
				b.rotateToward = new PVector(0,p.height/2);
			}
			
			if(grosCurseur2){
				/*
				if(hasRun2 < 31){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue);
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 31;
				}
				*/
				grosCurseur2x = p.constrain(grosCurseur2x-2,0,p.width/2+1200);
				for(int j = 0; j < 128; j+=p.map(j,0,128,8,8)){
					for(int i = 128; i > 0; i-=4){
						int c = img4.get(i, j);
						if(c < 0){
							VerletParticle2D particule = physics.particles.get(counter);
							particule.set(new Vec2D(i*3 + grosCurseur2x , j*3));
							particule.clearForce();
							particule.clearVelocity();
							counter++;
							counter=p.constrain(counter, 0, physics.particles.size() -1 );
						}
					}	
				}
			}
			
			
			
			//Si collision, fusion et bataille de curseurs!
			if(grosCurseur2x <= grosCurseur1x){ 
				grosCurseur1 = false;
				step = 11;
				/*
				if(hasRun2 < 32){
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.myRemoteLocation);
					liveCue++;
					hasRun2 = 32;
				}
				*/
				for(int i = 0; i<counter; i++){
					Boid b = (Boid) cursors.get(i);
					b.rotate = false;
				}
			}
		}
		
	
		//Bataille de curseurs!
		if(step == 11){
			for (VerletParticle2D particule : physics.particles) {
				particule.removeAllBehaviors();
			}
			flocking(3);
			stepTimeLineReset = timeLine;
			step++;
			
		}
		cue = 1000;
		
		if(step == 12){
			if(stepTimeLine >= cue && stepTimeLine <=  cue*2){
				flocking3x=(int) (flocking3x + p.random(-20,20));
				flocking3y=(int) (flocking3y + p.random(-15,15));
				flocking3x = p.constrain(flocking3x,-450,p.width);
				flocking3.set(flocking3x,flocking3y);
			}
			
			if(stepTimeLine >= cue*2 + 800 && stepTimeLine <=  cue*3){
				for (VerletParticle2D particule : physics.particles) {
					particule.removeAllBehaviors();
				}
			}
			
			if(stepTimeLine >= cue*3 && stepTimeLine <=  cue*4){
				flocking(4);
				flocking3x=(int) (flocking3x + p.random(-20,15));
				flocking3y=(int) (flocking3y + p.random(-15,15));
				flocking3x = p.constrain(flocking3x,-1000,p.width);
				flocking4.set(flocking3x,flocking3y);
			}
		
			if(stepTimeLine >= cue*4 + 800 && stepTimeLine <=  cue*4+900){
				for (VerletParticle2D particule : physics.particles) {
					particule.removeAllBehaviors();
				}
			}
			
			if(stepTimeLine >= cue*5 && stepTimeLine <=  cue*10){
				flocking(3);
				flocking3x=(int) (-1000);
				flocking3y=(int) (flocking3y + p.random(-15,15));
				flocking3x = p.constrain(flocking3x,-1000,p.width);
				flocking3.set(flocking3x,flocking3y);
			}
		}
		
		if(step == 12 && flocking3x <= -450){
			//kill();
			//boidCounter = 0;
			step++;
		
		}
	}
	
	void particleHide(int particle){
		
		
		VerletParticle2D particule = physics.particles.get(particle);
		if(particle == 2) particule.addBehavior(new AttractionBehavior(new Vec2D(p.width/2, -1000), 2000, 10f)); 
		else{//si déjà à gauche va a gauche
			if(particule.x <= p.width/2) particule.addBehavior(new AttractionBehavior(new Vec2D(-100, p.height/2), 2000, 10f)); 
			//si déjà à droite va a droite
			if(particule.x > p.width/2) particule.addBehavior(new AttractionBehavior(new Vec2D(p.width+100, p.height/2), 2000, 10f));
		}
	}
	
	void flocking(int group){
		
		
		if(group == 1 && hasRun != 1){
			for(int i = 3; i <= 7; i++){
				VerletParticle2D particule = physics.particles.get(i);
				flocking1Attractor = new AttractionBehavior(flocking1, 2000, 5f);
				particule.addBehavior(flocking1Attractor);
			}
			hasRun = 1;
		}
		
		if(group == 2 && hasRun != 2){
			for(int i = 8; i <= 13; i++){
				VerletParticle2D particule = physics.particles.get(i);
				flocking2Attractor = new AttractionBehavior(flocking2, 2000, 5f);
				particule.addBehavior(flocking2Attractor);
			}
			hasRun = 2;
		}
		
		if(group == 3 && hasRun != 3){
			for(int i = 0; i < physics.particles.size(); i++){
				VerletParticle2D particule = physics.particles.get(i);
				flocking3Attractor = new AttractionBehavior(flocking3, 10000, 20f);
				particule.addBehavior(flocking3Attractor);
			}
			
			hasRun = 3;
		}
		
		
		if(group == 4 && hasRun != 4){
			for(int i = 0; i < physics.particles.size(); i++){
				VerletParticle2D particule = physics.particles.get(i);
				flocking4Attractor = new AttractionBehavior(flocking4, 2000, 10f);
				particule.addBehavior(flocking4Attractor);
			}
			
			hasRun = 4;
		}
		
	}
	
	void boidAdd(int birthX, int birthY, int size) {
	
				boidCounter++;
				VerletParticle2D particule = new VerletParticle2D(Vec2D.randomVector().scale(0).addSelf(birthX, birthY));
				physics.addParticle(particule);
				//lettres.add(new Boid(new PVector(birthX,birthY),p.random(8f,14f),p.random(0.8f,1.f),"akl"));
				cursors.add(new Boid(new PVector(birthX,birthY),20f,20f,"akl",size));
				noisers.add(new Noiser(0,0));
				
				
				// add a negative attraction force field around the new particle
				//physics.addBehavior(new AttractionBehavior(particule, 100, -1.0f, p.random(0.2f,0.3f)));
				if(boidCounter < 2) physics.addBehavior(new AttractionBehavior(particule, 100, -1.0f,0f));
				else 				physics.addBehavior(new AttractionBehavior(particule, 100, -1.0f,2f));

	}
	
	public void keyPressed() {
		
		if (p.key == '0'){
			step++;
			boidAdd(p.mouseX,p.mouseY,30);
		}
		
		if (p.key == '9' && kinect1Clicksub == 0 && step >= 3){
			flocking1Origin = false;
			//20 arrivent du côté gauche
			stepTimeLineReset = timeLine;

			for(int i = 0 ; i <5; i++){
				boidAdd(-1000,250,30);
				Boid b = (Boid) cursors.get(i+3);
				b.changeSpeed(3f);
			}
			step++;
		}
		
		if (p.key == '8' && kinect1Clicksub == 0 && step >= 4){
			flocking2Origin = false;
			//20 arrivent du côté droit, flocking2
			stepTimeLineReset = timeLine;
			
			for(int i = 0 ; i <5; i++){
				boidAdd(p.width + 1000,250,30);
				Boid b = (Boid) cursors.get(i+8);
				b.changeSpeed(3f);
				}
			
			//BIG MOFO
			boidAdd(p.width + 1500,250,250);
			Boid b = (Boid) cursors.get(13);
			b.changeSpeed(3f);

			step++;
		}

		
		if (p.key == '7' && kinect1Clicksub == 0){

			//Remove all behaviors, tout le monde est soumis au meme attractor
			for (VerletParticle2D particule : physics.particles) {
				particule.removeAllBehaviors();  	
			}

			//VerletParticle2D particule = physics.particles.get(step);
			//particule.removeBehavior(mouseAttractor);

			for(int i = 0 ; i <60; i++){
				boidAdd((int) p.random(-200, -20),(int) p.random(p.height/2 - 100, p.height/2+100),30);
			}
			for(int i = 0 ; i <60; i++){
				boidAdd((int) p.random(p.width + 20, p.width+20),(int) p.random(p.height/2 - 100, p.height/2+100),30);
			}
			for(int i = 0 ; i <60; i++){
				boidAdd((int) p.random(-20, p.width+20),(int) p.random( - 100, -10),30);
			}
			
			step++;

		}

		if (p.key == '6' && kinect1Clicksub == 0){
			//physics.removeBehavior(mouseAttractor);
			grosCurseur1 = true;
			grosCurseur2 = true;
			grosCurseur1x = -600;
			grosCurseur2x = p.width+600;
		}
		
		if (p.key == '5' && kinect1Clicksub == 0){
			grosCurseur2 = true;
			
		}
		
		if (p.key == '4' && kinect1Clicksub == 0){
			grosCurseur1 = false;
		}

	}

	public void events() {
	
		if (p.seqStep == 1){
			if(change[10].test(p.seqStep)){
				inverse = true;
			}
		}
		
		if (p.seqStep == 2){
			if(change[10].test(p.seqStep)){
				inverse = true;
			}
		}
	 
		// GROUPE 1 ARRIVE
		if (p.seqStep == 3 && kinect1Clicksub == 0 && step >= 3){
			if(change[10].test(p.seqStep)){
				stepTimeLine = 0; //Counter à zero sinon il prend celui de la seq précédente
				stepTimeLineReset = timeLine;

				flocking1Origin = false;
				//5 arrivent du côté gauche

				for(int i = 0 ; i <5; i++){
					boidAdd(-100,250,30);
					Boid b = (Boid) cursors.get(i+3);
					b.changeSpeed(0f);
				}
				step++; //==4
			}
		}
		
		// GROUPE 2 ARRIVE
		if (p.seqStep == 4 && kinect1Clicksub == 0 && step >= 4){
			if(change[10].test(p.seqStep)){
				stepTimeLine = 0;
				stepTimeLineReset = timeLine;
				flocking2Origin = false;
				//5 arrivent du côté droit, flocking2
				

				for(int i = 0 ; i <5; i++){
					boidAdd(p.width + 100,250,30);
					Boid b = (Boid) cursors.get(i+8);
					b.changeSpeed(0f);
				}

				//BIG MOFO
				boidAdd(p.width + 200,250,160);
				Boid b = (Boid) cursors.get(13);
				b.changeSpeed(0f);
				step++; //==5
			}
		}

		if(p.seqStep == 49){
			if(stepTimeLine > 2000) p.seqStep = 5;
		}
		// LA GANG EN HAUT
		if (p.seqStep == 5){
			if(change[10].test(p.seqStep)){
				kill();
				stepTimeLine = 0;
				stepTimeLineReset = timeLine;
				//Remove all behaviors, tout le monde est soumis au meme attractor
				
				for (VerletParticle2D particule : physics.particles) {
					particule.removeAllBehaviors();  	
				}
/*
				for(int i = 0 ; i <213; i++){
					boidAdd(p.width / 2, -100,30);
					Boid b = (Boid) lettres.get(i);
					b.changeSpeed(3f);
				}
			
			/*	
				for(int i = 0; i < physics.particles.size(); i++){
					VerletParticle2D particule = physics.particles.get(i);
					handPos5 = new Vec2D(p.width/2, p.height);
					handAttractor5 = new AttractionBehavior(handPos5, 350, -5f);
					particule.addBehavior(handAttractor5);
				}
			*/
				step++; //==6
			}
		}

		
		if (p.seqStep == 6 && kinect1Clicksub == 0){
			//physics.removeBehavior(mouseAttractor);
			grosCurseur1 = true;
			grosCurseur2 = true;
			grosCurseur1x = -600;
			grosCurseur2x = p.width+1200;
			
		}
		

		
	}
	
	public void hands(){
		

		//Handpos reverser
		if(inverse){
			inverseInt = 1;
			if(change[9].test(inverseInt)) {
				kinect1xInvRelative = (int) p.kinect1X;
				kinect1x2InvRelative = (int) p.kinect1X2;
			}
			mouseXsub = p.abs(p.mouseX - p.width);
			mouseYsub = p.mouseY;
			kinect1Xsub =  (int) (kinect1xInvRelative + (kinect1xInvRelative - p.kinect1X));
			kinect1Ysub =  (int)p.kinect1Y;
			kinect1X2sub = (int) (kinect1x2InvRelative + (kinect1x2InvRelative - p.kinect1X2));
			kinect1Y2sub = (int)p.kinect1Y2;	
		} else {
			inverseInt = 0;
			change[9].reset();
			mouseXsub = p.mouseX;
			mouseYsub = p.mouseY;
			kinect1Xsub = (int) p.kinect1X;
			kinect1Ysub = (int) p.kinect1Y;
			kinect1X2sub = (int) p.kinect1X2;
			kinect1Y2sub = (int) p.kinect1Y2;
		}
		
		//Algo qui rassemble les deux mains en une.
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
		
		
		//ONE HAND
		if(kinect1Clicksub == 1.0f) {
			////////MOUSE PRESSED
			
			if (change[1].test(kinect1Clicksub)) { 
				
				handPos.set(kinectOneHandX, kinectOneHandY);
				//LES TROIS PREMIERS CURSEURS IN
				if(step <= 2){
					if(change[0].test(step)) {
						boidAdd((int)handPos.x, (int)handPos.y,30);
						Boid b = (Boid) cursors.get(0);
						b.imageMode = 0;
						if (step == 1) {
							b = (Boid) cursors.get(1);
							b.imageMode = 1;
						}
						if (step == 2) {
							b = (Boid) cursors.get(2);
							b.imageMode = 2;
						}
						
						stepTimeLineReset = timeLine;
						////LIVE CUE 1 + LIVE CUE 3
						OscMessage myMessage = new OscMessage("/cueNum");
						myMessage.add(liveCue); /* add an int to the osc message */
						p.oscP5.send(myMessage, p.soundComputer);
						myMessage = new OscMessage("/cueGo");
						myMessage.add(1); /* add an int to the osc message */
						p.oscP5.send(myMessage, p.soundComputer);
						liveCue++;
					}

					VerletParticle2D particule = physics.particles.get(step);
					particule.set(handPos);
				}

				//LE PREMIER GROUPE SE CACHE
				if(step == 4 && stepTimeLine >= 2000*8){
					flocking1Origin = true;
					p.seqStep = 4;
				}

				//LE BIG MOFO SE CACHE  //Cette condition 6 avant la 5 sinon = bug
				if(step == 6  && stepTimeLine >= 2000*11){
					flocking2OriginMofo = true;
				}
				
				//LE DEUXIEME GROUPE SE CACHE sauf Big Mofo
				if(step == 5  && stepTimeLine >= 2000*11){
					flocking2Origin = true;
					step++;
				}
				
	
				//TOUTE LA GANG EST ATTIRÉE 
				if(step == 7) { //attendre que tout le monde soit arrivé (213)
					if(change[0].test(step)) {
						for(int i = 0; i < physics.particles.size(); i++){
							VerletParticle2D particule = physics.particles.get(i);
							handAttractor = new AttractionBehavior(handPos, 2000, 10f);
							particule.addBehavior(handAttractor);
						}
					}
				}

				/*
				if(step >= 7 && step <= 8 ) {

					for (VerletParticle2D particule : physics.particles) {
						particule.removeAllBehaviors();	
					}

					for(int i = 0; i < physics.particles.size(); i++){
						VerletParticle2D particule = physics.particles.get(i);
						handAttractor = new AttractionBehavior(handPos, 2000, 10f);
						particule.addBehavior(handAttractor);
					}

					if(change[0].test(step)) {
						OscMessage myMessage2 = new OscMessage("/cueNum");
						myMessage2.add(liveCue); 
						p.oscP5.send(myMessage2, p.myRemoteLocation);
						myMessage2 = new OscMessage("/cueGo");
						myMessage2.add(1); 
						p.oscP5.send(myMessage2, p.myRemoteLocation);
						liveCue++;
					}


				}
				 */


				//Gang se cache
				if(step == 10){
					if(change[0].test(step)) {

						OscMessage myMessage = new OscMessage("/cueNum");
						myMessage.add(liveCue); /* add an int to the osc message */
						p.oscP5.send(myMessage, p.soundComputer);
						myMessage = new OscMessage("/cueGo");
						myMessage.add(1); /* add an int to the osc message */
						p.oscP5.send(myMessage, p.soundComputer);
						liveCue++;

						grosCurseur1 = true;
						grosCurseur2 = true;
						grosCurseur1x = -700;
						grosCurseur2x = p.width+350;
						for(int i = 0; i<boidCounter; i++){
							Boid b = (Boid) cursors.get(i);
							b.rotate = false;
						}
					}
				}


				if(step == 14){
					boidAdd((int)p.kinect1X, (int)p.kinect1Y,30);
					VerletParticle2D particule = physics.particles.get(boidCounter - 1);
					particule.addBehavior(flocking3Attractor);
				}


			} //End of click
			
			//////////////MOUSE Dragged	
			if(boidCounter>=1 && step != 3 && step < 4) {
				VerletParticle2D particule = physics.particles.get(step);
				handPos.set(kinectOneHandX, kinectOneHandY);
				particule.set(handPos);
			}
			
			if(step >= 7) { //Suivre les déplacements de step 6 à 8
				handPos.set(kinectOneHandX, kinectOneHandY);
			}
			
			if(step == 9) {
				if(change[0].test(step)) {
					stepTimeLineReset = timeLine;
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.soundComputer);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.soundComputer);
					liveCue++;
				}

				if(step == 9 && stepTimeLine >= 2000 && kinectOneHandY > (2*p.height) / 3){
					if(hasRun3 != 80) {
						stepTimeLine = 0;
						stepTimeLineReset = timeLine;
						OscMessage myMessage2 = new OscMessage("/cueNum");
						myMessage2.add(liveCue); 
						p.oscP5.send(myMessage2, p.soundComputer);
						myMessage2 = new OscMessage("/cueGo");
						myMessage2.add(1); 
						p.oscP5.send(myMessage2, p.soundComputer);
						liveCue++;
					}
					for (VerletParticle2D particule : physics.particles) {
						particule.removeAllBehaviors();	
					}
					for(int i = 0; i < physics.particles.size(); i++){
						VerletParticle2D particule = physics.particles.get(i);
						handAttractor = new AttractionBehavior(handPos, 2000, 10f);
						particule.addBehavior(handAttractor);
					}
					hasRun3 = 80;
				}
			}
			
			if(step == 8) {
				
				if(change[0].test(step)) {
					stepTimeLine = 0;
					stepTimeLineReset = timeLine;
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.soundComputer);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.soundComputer);
					liveCue++;
				}

				if(step == 8 && stepTimeLine >= 2000 && kinectOneHandY > p.height / 2){
					if(hasRun3 != 70) {
						stepTimeLine = 0;
						stepTimeLineReset = timeLine;
						OscMessage myMessage2 = new OscMessage("/cueNum");
						myMessage2.add(liveCue); 
						p.oscP5.send(myMessage2, p.soundComputer);
						myMessage2 = new OscMessage("/cueGo");
						myMessage2.add(1); 
						p.oscP5.send(myMessage2, p.soundComputer);
						liveCue++;
					}
					for (VerletParticle2D particule : physics.particles) {
						particule.removeAllBehaviors();	
					}
					for(int i = 0; i < physics.particles.size(); i++){
						VerletParticle2D particule = physics.particles.get(i);
						handAttractor = new AttractionBehavior(handPos, 2000, 10f);
						particule.addBehavior(handAttractor);
					}
					hasRun3 = 70; //être certain que cette étape-ci est faite avant de faire ce qui suit
				}

				if(step == 8 && stepTimeLine >= 8000 && kinectOneHandY < (2*p.height) / 3 && hasRun3 == 70){
					for (VerletParticle2D particule : physics.particles) {
						particule.removeAllBehaviors();	
					}
					for(int i = 0; i < physics.particles.size(); i++){
						VerletParticle2D particule = physics.particles.get(i);
						handAttractor = new AttractionBehavior(handPos, 500, -5f);
						particule.addBehavior(handAttractor);
					}
					step++; //vers 9
				}
			}
			


			if(step == 7 && stepTimeLine >= 1000*12 && kinectOneHandY < (2*p.height) / 3){
				
				if(change[0].test(step)) {
					stepTimeLine = 0;
					stepTimeLineReset = timeLine;
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); 
					p.oscP5.send(myMessage2, p.soundComputer);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); 
					p.oscP5.send(myMessage2, p.soundComputer);
					liveCue++;
				}
				
				for (VerletParticle2D particule : physics.particles) {
					particule.removeAllBehaviors();
				}

				for(int i = 0; i < physics.particles.size(); i++){
					VerletParticle2D particule = physics.particles.get(i);
					handAttractor = new AttractionBehavior(handPos, 500, -5f);
					particule.addBehavior(handAttractor);
				}
				step++; // vers 8
			}

			if(step == 2) {
				int cueStep2 = 1700;
				if(stepTimeLine > cueStep2 && stepTimeLine < cueStep2+100) ps.addParticle(new PVector(handPos.x,handPos.y));
				cueStep2 = 2130;
				if(stepTimeLine > cueStep2 && stepTimeLine < cueStep2+150) ps.addParticle(new PVector(handPos.x,handPos.y));
				cueStep2 = 4340;
				if(stepTimeLine > cueStep2 && stepTimeLine < cueStep2+100) ps.addParticle(new PVector(handPos.x,handPos.y));
				cueStep2 = 4800;
				if(stepTimeLine > cueStep2 && stepTimeLine < cueStep2+150) ps.addParticle(new PVector(handPos.x,handPos.y));
				cueStep2 = 6960;
				if(stepTimeLine > cueStep2 && stepTimeLine < cueStep2+100) ps.addParticle(new PVector(handPos.x,handPos.y));
				cueStep2 = 7380;
				if(stepTimeLine > cueStep2 && stepTimeLine < cueStep2+150) ps.addParticle(new PVector(handPos.x,handPos.y));
			}

		}

		if (kinect1Clicksub == 0f){
			handPos3 = new Vec2D(300, 250);
			handPos4 = new Vec2D(p.width-300, 250);
			//////////////MOUSE RELEASED	

			if (change[1].test(kinect1Clicksub)) { 

				//respecter l'ordre sinon bug (à cause du step change immédiat)
				if(step == 2){
					stepTimeLine = 0;
					stepTimeLineReset = timeLine;
					particleHide(step);
					OscMessage myMessage2 = new OscMessage("/cueNum");
					myMessage2.add(liveCue); /* add an int to the osc message */
					p.oscP5.send(myMessage2, p.soundComputer);
					myMessage2 = new OscMessage("/cueGo");
					myMessage2.add(1); /* add an int to the osc message */
					p.oscP5.send(myMessage2, p.soundComputer);
					liveCue++;
					
					step = 6;
					p.seqStep = 49;
				}

				if(inverse){
					particleHide(step);
					step++;
					inverse=false;

					////LIVE CUE 2 + LIVE CUE 4
					OscMessage myMessage = new OscMessage("/cueNum");
					myMessage.add(liveCue); /* add an int to the osc message */
					p.oscP5.send(myMessage, p.soundComputer);
					myMessage = new OscMessage("/cueGo");
					myMessage.add(1); /* add an int to the osc message */
					p.oscP5.send(myMessage, p.soundComputer);
					liveCue++;
				}
				
				if(step == 5){
					for(int i = 0; i < physics.particles.size(); i++){
						VerletParticle2D particule = physics.particles.get(i);
						handAttractor = new AttractionBehavior(handPos, 350, -5f);
						particule.addBehavior(handAttractor);
					}
				}

				if(step == 9 && hasRun3 == 80 && stepTimeLine >= 2000) {
					
					for (VerletParticle2D particule : physics.particles) {
						particule.removeAllBehaviors();
					}

					if(hasRun2 < 29){
						OscMessage myMessage2 = new OscMessage("/cueNum");
						myMessage2.add(liveCue); 
						p.oscP5.send(myMessage2, p.soundComputer);
						myMessage2 = new OscMessage("/cueGo");
						myMessage2.add(1); 
						p.oscP5.send(myMessage2, p.soundComputer);
						liveCue++;
						hasRun2 = 29;
					}

					//deux groupes
					for(int i = 0; i < 116; i++){
						VerletParticle2D particule = physics.particles.get(i);
						handPos3 = new Vec2D(300, 250);
						handAttractor3 = new AttractionBehavior(handPos3, 2000, 10f);
						particule.addBehavior(handAttractor3);
					}

					for(int i = 116; i < physics.particles.size(); i++){
						VerletParticle2D particule = physics.particles.get(i);
						handPos4 = new Vec2D(p.width-300, 250);
						handAttractor4 = new AttractionBehavior(handPos4, 2000, 10f);
						particule.addBehavior(handAttractor4);
					}
					
					step++;
				}

				
				/*
				if(step == 8) {
					for (VerletParticle2D particule : physics.particles) {
						particule.removeAllBehaviors();
					}

					for(int i = 0; i < physics.particles.size(); i++){
						VerletParticle2D particule = physics.particles.get(i);
						handAttractor = new AttractionBehavior(handPos, 350, -5f);
						particule.addBehavior(handAttractor);
					}
					step++;
				}

				/*
				if(step == 7&& stepTimeLine >= 1000*12){
					for (VerletParticle2D particule : physics.particles) {
						particule.removeAllBehaviors();
					}

					for(int i = 0; i < physics.particles.size(); i++){
						VerletParticle2D particule = physics.particles.get(i);
						handAttractor = new AttractionBehavior(handPos, 350, -5f);
						particule.addBehavior(handAttractor);
					}
					step++;
				}
				 */

			}
		}

	
	}

	/*	
	public void mousePressed() {
		
		if(step <= 2){
			if(change[0].test(step)) boidAdd(p.mouseX,p.mouseY,30);
		}

		if(boidCounter>=1 && step <=2) {
			mousePos = new Vec2D(mouseXsub, mouseYsub);
			// create a new positive attraction force field around the mouse position (radius=250px)	
			VerletParticle2D particule = physics.particles.get(step);
			particule.set(mousePos);
			
		}

		if(step == 4){
			flocking1Origin = true;
		}

		if(step == 5){
			flocking2Origin = true;
		}

		if(step == 6) {
			
			for(int i = 0; i<180; i++){
				
				Boid b = (Boid) lettres.get(i);
				b.maxspeed = 20f;
			}
			
			for(int i = 0; i < physics.particles.size(); i++){
				VerletParticle2D particule = physics.particles.get(i);
				mouseAttractor = new AttractionBehavior(mousePos, 2000, 10f);
				particule.addBehavior(mouseAttractor);
			}

		}

		if(step > 6 && step < 51) {

			for (VerletParticle2D particule : physics.particles) {
				particule.removeAllBehaviors();	
			}

			for(int i = 0; i < physics.particles.size(); i++){
				VerletParticle2D particule = physics.particles.get(i);
				mouseAttractor = new AttractionBehavior(mousePos, 2000, 10f);
				particule.addBehavior(mouseAttractor);
			}

		}

		if(step == 52){
			boidAdd(p.mouseX,p.mouseY,30);
			VerletParticle2D particule = physics.particles.get(boidCounter - 1);
			particule.addBehavior(flocking3Attractor);
		}

	}

	public void mouseDragged() {
		if(boidCounter<=3) {
		// update mouse attraction focal point
		mousePos.set(mouseXsub, mouseYsub);
		VerletParticle2D particule = physics.particles.get(step);
		particule.set(mousePos);
		} else mousePos.set(mouseXsub, mouseYsub);
		
	}

	public void mouseReleased() {
		// remove the mouse attraction when button has been released
		//physics.removeBehavior(mouseAttractor);
		if(boidCounter>=1 && step <=2) {
			VerletParticle2D particule = physics.particles.get(step);	
			 
		}

		
		if(step >= 6 && step < 51) {
			for (VerletParticle2D particule : physics.particles) {
				particule.removeAllBehaviors();
			}
			
			for(int i = 0; i < physics.particles.size(); i++){
				VerletParticle2D particule = physics.particles.get(i);
				mouseAttractor = new AttractionBehavior(mousePos, 2000, -5f);
				particule.addBehavior(mouseAttractor);
			}
			step++;

		}

		if(step == 2){
			particleHide(step);
			step++;
		}

		if(inverse){
			particleHide(step);
			step++;
			inverse=false;
		}

	}
	*/
	
	public void controlEvent(ControlEvent theEvent) {
	}

	public void oscEvent(OscMessage theOscMessage) {


	}

	public void kill() {
		
		physics.clear();
		for (VerletParticle2D particule : physics.particles) {
			particule.lock();
			particule.clearForce();
			particule.clearVelocity();
			particule.removeAllBehaviors();  
			physics.removeParticle(particule);
			//physics.removeBehavior(handAttractor);
			//physics.removeBehavior(handAttractor2);
		}
		
		physics.behaviors.clear(); //Clear all attractors
		physics.clear();
		boidCounter= 0;
		cursors.clear();
		noisers.clear();
	}

	class Boid {

		PVector loc;
		PVector vel;
		PVector acc;
		float r;
		float d; //Distance with destination
		float maxforce;    // Maximum steering force
		float maxspeed;    // Maximum speed
		String letter;
		int moyenne = 5;
		int moyenne3 = 20;
		float[] recentValues = new float[moyenne3];
		int currentIndex = 0;
		int currentIndex3 = 0;
		float mx[] = new float[moyenne];
		float my[] = new float[moyenne];
		float mz[] = new float[moyenne];
		int age = 0;
		int imageSize;
		boolean rotate = false;
		PVector rotateToward = new PVector(0,0);
		int thetaBypass = 0;
		int imageMode;
		

		Boid(PVector l, float ms, float mf,String t, int size) {
			acc = new PVector(0,0);
			vel = new PVector(0,0);
			loc = l.get();
			
			r = 3.0f;
			maxspeed = ms;
			maxforce = mf;
			letter = t;
			imageSize = size;
			
			for(int i = 0; i < moyenne; i++){
				mx[i] = loc.x;
				my[i] = loc.y;
				}
			
			
		}
		void changeSpeed(float newSpeed){
			
			maxspeed = newSpeed;
		}
		
		void run() {
			update();
			//borders();
			render();
			age++;
		}

		// Method to update location
		void update() {
			// Update velocity
			vel.add(acc);
			// Limit speed
			vel.limit(maxspeed);
			loc.add(vel);
			// Reset accelertion to 0 each cycle
			acc.mult(0);
		}

		void seek(PVector target) {
			acc.add(steer(target,false));
		}

		void arrive(PVector target) {
			acc.add(steer(target,true));
		}

		// A method that calculates a steering vector towards a target
		// Takes a second argument, if true, it slows down as it approaches the target
		PVector steer(PVector target, boolean slowdown) {
			PVector steer;  // The steering vector
			PVector desired = PVector.sub(target,loc);  // A vector pointing from the location to the target
			d = desired.mag(); // Distance from the target is the magnitude of the vector
			// If the distance is greater than 0, calc steering (otherwise return zero vector)
			if (d > 0) {
				// Normalize desired
				desired.normalize();
				// Two options for desired vector magnitude (1 -- based on distance, 2 -- maxspeed)
				if ((slowdown) && (d <50.0f)) desired.mult(maxspeed*(d/50.0f)); // This damping is somewhat arbitrary
				else desired.mult(maxspeed);
				// Steering = Desired minus Velocity
				steer = PVector.sub(desired,vel);
				steer.limit(maxforce);  // Limit to maximum steering force
			} else {
				steer = new PVector(0,0);
			}
			return steer;
		}

		void render() {
			
			PVector pointAt = PVector.sub(rotateToward, loc);
			float theta = pointAt.heading2D() + p.radians(90);
			//float theta = PVector.angleBetween(rotateToward,loc);
			
			if(!rotate) theta = p.radians(0);
			// Smooth out theta values (quite unuseful)
			recentValues[currentIndex3] = theta;
			
			float thetaSmooth = 0;
			for(int i=0;i<moyenne;i++){
				thetaSmooth = thetaSmooth + recentValues[i];
			}
			thetaSmooth = thetaSmooth/moyenne;
			 

			//int delta = 1; //écart angle
			//float d1 = p.abs((d / p.sqrt(p.pow(graphWidth/2,2)+p.pow(graphHeight/2,2)))-1);
			//theta = p.constrain(theta, -delta + (delta*d1), delta - (delta*d1));


			for(int i=0;i<moyenne3;i++){
				thetaSmooth = thetaSmooth + recentValues[i];
			}
			thetaSmooth = thetaSmooth/moyenne3;

			mx[currentIndex] = loc.x;
			float locSmoothX = 0;
			for(int i=0;i<moyenne;i++){
				locSmoothX = locSmoothX + mx[i];
			}
			locSmoothX = locSmoothX/moyenne;

			my[currentIndex] = loc.y;
			float locSmoothY = 0;
			for(int i=0;i<moyenne;i++){
				locSmoothY = locSmoothY + my[i];
			}
			locSmoothY = locSmoothY/moyenne;


			c.pushMatrix();
			c.translate( locSmoothX,locSmoothY);
			c.rotate(thetaSmooth);
			if(imageMode == 0) c.image(curseur,0,0,imageSize,imageSize);
			else if(imageMode == 1) c.image(doigt,0,0,imageSize,imageSize);
			else if(imageMode == 2) c.image(mad,0,0,25,25);
			else if(imageMode == 3) c.image(devil,0,0,25,25);
			
			//c.fill(255, p.constrain((int)(age),0,255));
			c.popMatrix();

			currentIndex++;
			if(currentIndex > moyenne-1){
				currentIndex = 0;
			}
			
			currentIndex3++;
			if(currentIndex3 > moyenne3-1){
				currentIndex3 = 0;
			}
		}

		// Wraparound
		void borders() {
			if (loc.x < -r) loc.x = graphWidth+r;
			if (loc.y < -r) loc.y = graphHeight+r;
			if (loc.x > graphWidth+r) loc.x = -r;
			if (loc.y > graphHeight+r) loc.y = -r;
		}

	}

	class Noiser {
		//quand forme le mot
		int destinationX;
		int destinationY;
		int progFollow = 1;


		Noiser(int x, int y) {
			destinationX = x;
			destinationY = y;

		}

		void newDestination(int x, int y){
			destinationX = x;
			destinationY = y;
			if(destinationX == 0){
				progFollow = 0;
			} else{
				progFollow = 1;
			}
		}

		PVector destination() {

			PVector NoiserVector = new PVector(destinationX+260,destinationY);			
			return NoiserVector;
		}

		int toDestination() {

			return progFollow;
		}
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

	class Particle {
		
		PVector location;
		PVector velocity;
		PVector acceleration;
		float lifespan;
		int lettre;

		Particle(PVector l) {
			acceleration = new PVector(0,0.05f);
			velocity = new PVector(p.random(-1,1),p.random(-2,0));
			location = l.get();
			lifespan = 255.0f;
			lettre = 0;
		}

		void run() {
			update();
			display();
		}

		// Method to update location
		void update() {
			velocity.add(acceleration);
			location.add(velocity);
			lifespan -= 1.0;
		}

		// Method to display
		void display() {
			
			c.stroke(255,lifespan);
			c.fill(255,lifespan);
			c.textFont(p.font,12);
			if(lettre == 0){
			c.image(devil,location.x,location.y,18,18);
			}
			if(lettre == 1){
			c.text("step++;",location.x,location.y);
			}
			if(lettre == 8){
			c.text(".set ",location.x,location.y);
			}
			if(lettre == 3){
			c.text("draw()",location.x,location.y);
			}
			if(lettre == 7){
			c.text("c.endDraw();   ",location.x,location.y);
			}
			if(lettre == 10){
			c.text("i++",location.x,location.y);
			}
			if(lettre == 12){
			c.text(" float",location.x,location.y);
			}
		
		}

		// Is the particle still useful?
		boolean isDead() {
			if (lifespan < 0.0) {
				return true;
			} else {
				return false;
			}
		}
	}

	class ParticleSystem {
		
		ArrayList<Particle> particles;
		PVector origin;

		ParticleSystem(PVector location) {
			origin = location.get();
			particles = new ArrayList<Particle>();
		}

		void addParticle(PVector originReal) {
			particles.add(new Particle(originReal));
		}

		void run() {
			Iterator<Particle> it = particles.iterator();
			while (it.hasNext()) {
				Particle p = it.next();
				p.run();
				if (p.isDead()) {
					it.remove(); 
				}
			}
		}
	}
}
