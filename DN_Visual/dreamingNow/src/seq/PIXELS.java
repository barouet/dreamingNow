package seq;


import oscP5.OscMessage;
import dreaming.DreamingNow;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;
import toxi.geom.*;
import seq.PIXELS.Change;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;

public class PIXELS extends Sketch {


	int NUM_PARTICLES = 16000;
	//int[] kinectToParticules = new int[307200];
	int IMAGE_PIXELS = 71824;
	int[] imageFilled = new int[IMAGE_PIXELS];
	
	int stepTimeLine;
	int stepTimeLineReset;

	float speeder;//Compense pour la perte de fps - CHANGER DANS LE REINIT
	float attractorForce = 1.0f * speeder; // * x to compensate for slow frameRate
	float gravityForce = 0.15f * speeder; // entre 0. et 0.9
	float attractorRadius = 200;

	float seq3Progression = 1.f;
	int time = 0 ;
	int countea;
	int maxCountea;
	float camPosX, camPosY, camPosZ, camLookX, camLookY, camLookZ;
	float camPosXEased, camPosYEased, camPosZEased, camLookXEased, camLookYEased, camLookZEased;

	int[] pColor = new int[NUM_PARTICLES];
	float[] pSize = new float[NUM_PARTICLES];
	int fakePointsAlpha = 255;

	float[] pX = new float[NUM_PARTICLES];
	float[] pY = new float[NUM_PARTICLES];
	float[] pZ = new float[NUM_PARTICLES];
	float[] pZeased = new float[NUM_PARTICLES];
	int j = 0;
	boolean consta = false;
	PImage imgD;

	int step = 0;

	Change[] change = new Change[15];

	//urn vars.
	int[] urn = new int[NUM_PARTICLES]; 
	int picked;
	int urnOrder= 0;

	float rot = 0;
	boolean camTravel;
	int timerStart = 0;
	float imageFilledPercent = 0;
	int[] pLocked = new int[NUM_PARTICLES];
	int pLockedSum = 0;
	boolean imageMatcher = false;

	int positionImageX = 400;
	int positionImageEX = 400;
	int positionImageE2X = 400;
	int positionImagePX = 400;
	int positionImageE3X = 400;
	int positionImageRX = 400;
	int positionImageY = 200;
	int imageRadius = 100; 

	VerletPhysics2D physics;

	AttractionBehavior mouseAttractor;
	AttractionBehavior cornerAttractor;

	AttractionBehavior handAttractor;
	AttractionBehavior handAttractor2;
	AttractionBehavior handAttractor3;
	AttractionBehavior handAttractor4;
	AttractionBehavior handAttractor5;
	AttractionBehavior handAttractor6;

	AttractionBehavior imageAttractor;

	GravityBehavior gravity;

	Vec2D mousePos;
	Vec2D handPos;
	Vec2D handPos2;
	Vec2D rotatePoint = new Vec2D(0,0);

	//Kinect Hands to OneHand
	int kinect1Xsub;
	int kinect1Ysub;
	int kinect1X2sub;
	int kinect1Y2sub;
	int kinect1Clicksub;
	int kinectOneHandX;
	int kinectOneHandY;
	int previouskinectOneHandY;

	Vec2D imagePos;

	int constellation1, constellation2;

	int prevSequenceNumber = 0;

	//Rotation stuff
	boolean throwAndRotate = false;
	float rotationY = 0f;
	float rotationYEased = 0f;
	float rotationStartPoint = 0;
	int hasRun = 0;
	int hasRun2 = 0;
	int hasRun3 = 0;
	int hasRun4 = 0;
	boolean gravityPlay = false;
	boolean lines = false;
	boolean pause = false;
	boolean numbers = false;
	boolean timeStopLines = false;
	boolean timeStopFlicker = false;
	int timeStopClickCounter = 0;
	boolean seen = false;
	float bottomWorldBound;
	int imageIndex = 0;
	int pixelDimmer = 255;
	
	public PIXELS(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		physics = new VerletPhysics2D();
		bottomWorldBound = 6;
		physics.setWorldBounds(new Rect(0, -4, p.width, p.height+bottomWorldBound)); //box definition
		gravityForce = 0.15f;
		gravity = new GravityBehavior(new Vec2D(0, gravityForce));
		physics.addBehavior(gravity);

		imgD = p.loadImage("pic/boy1.png");
		imagePos = new Vec2D(0,0);
		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}
		
		
		
	}

	public void reinit() {

		super.reinit();
		kill();
		c.resetShader();
		p.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		//Boolean reset
		p.kinectConnected = false;
		p.skeletonTracking = false;
		imageMatcher = false;
		throwAndRotate = false;
		pause = false;
		gravityPlay = false;
		lines = false;
		timeStopLines = false;
		timeStopFlicker = false;
		seen = false;
		timeStopClickCounter = 0;
		
		//physic reset
		gravity = new GravityBehavior(new Vec2D(0, gravityForce));
		physics.addBehavior(gravity);
		speeder = 1f; 
		gravityForce = 0.12f * speeder;
		attractorForce = 0.5f * speeder;
		attractorRadius = 200;
		
		p.wallEasing = 0.2f; // Easing setting for wall kinect;
		imgD = p.loadImage("pic/boy1.png");
		p.shaderSelect = 0;
		for (int i = 0; i < NUM_PARTICLES; i++) {
			pColor[i] = 255;
			pLocked[i] = 0;
			pSize[i] = 2f;
			addParticle();
			pZ[i] = p.random(-1100, 100);
		}

		//REMPLIR L'ARRAY DE CORRESPONDANCES
		for (int i = 0; i < urn.length; i++) { 
			urn[i] = 0;
		}

		int urnAllocation = 0;

		/*for (int i = 0; i < kinectToParticules.length; i++) {
			if(urnAllocation == NUM_PARTICLES-1){
				urnAllocation=0;
				for (int j = 0; j < urn.length; j++) { 
					urn[j] = 0;
				}
			}
			int number = urn();	
			while (number == 0) {
				number = urn();
			}
			urnAllocation++;
			kinectToParticules[i] = number;
		}*/

		for (int i = 0; i < pX.length; i++) {
			pX[i] = 0;
		}

		for (int i = 0; i < pY.length; i++) {
			pY[i] = 0;
		}

		p.textAlign(p.CENTER, p.CENTER);
		p.textFont(p.font, 8);

		//Change reset
		for (int i = 0; i < change.length; i++) {
			change[i].reset();
		}

		step = 7;
		hasRun = 0;
		hasRun2 = 0;
		hasRun3 = 0;
		hasRun4 = 0;
		rotationYEased= 0f;
		p.seqStep = 0;
		stepTimeLineReset = 0;
		stepTimeLineReset = timeLine;
		imageIndex = 0;
		pixelDimmer = 255;
		c.hint(c.DISABLE_DEPTH_TEST);
		c.hint(p.DISABLE_DEPTH_MASK);
		p.println("OCEAN");
		bottomWorldBound = 6;
		physics.setWorldBounds(new Rect(0, -4, p.width, p.height+bottomWorldBound)); //box definition
		
	}

	public void kill() {
		
		
		physics.clear();
		for (VerletParticle2D particule : physics.particles) {
			particule.lock();
			particule.clearForce();
			particule.clearVelocity();  
			physics.removeParticle(particule);
		}
		physics.behaviors.clear();
		
	}

	public void draw() {
	
		super.draw();
		gravityForce = 0.5f;
		stepTimeLine = timeLine - stepTimeLineReset;
		if(throwAndRotate == false && gravityPlay == false){
			hands();
		}
		
		handsToOneHandRaw();
		events();
		partition();
		c.beginDraw();
		c.lights();
		c.ambientLight(255, 255, 255);
		c.strokeCap(100);
		float dirY = p.mouseY ;
		float dirX = p.mouseX ;		

		
		c.background(0);
		
		// to change gravity
		gravity.setForce(new Vec2D(0, gravityForce));
		physics.update();
		
		if(timeLine > 1000) renderPixels();

		c.tint(255,255);

		c.endDraw();

		
	}

	public void renderPixels(){
		countea=0;

		float easing = 0.5f;
		
		
		///RENDERER
		for (int i=0;i<NUM_PARTICLES;i++) {
			VerletParticle2D particule = physics.particles.get(i);

			if (pause) { 
				particule.clearForce();
				particule.clearVelocity();
			}

			
			if(!imageMatcher) {
				pColor[i]=255;
			} else{

				positionImageX = 350;
				positionImageY = 150;
				imageFilledPercent = 0;
				//du centre vers l'extŽrieur !!! Peut-tre serait-ce plus beau si c'Žtait un cercle?
				if (particule.x> (positionImageX+(imageFilledPercent*(imgD.width/2))) 
						&& particule.x < p.constrain(((imgD.width+positionImageX)-(imageFilledPercent*(imgD.width/2))+20), 0, imgD.width+positionImageX) 
						&& particule.y> (positionImageY+(imageFilledPercent*(imgD.height/2))) 
						&& particule.y < p.constrain(((imgD.height+positionImageY)-(imageFilledPercent*(imgD.height/2))+20), 0, imgD.height+positionImageY)) {	
					//situer le pixel dans l'array photo
					int particlePosArray = ( (int) (particule.x  - positionImageX) + (  (int) ( particule.y - positionImageY ) * imgD.width)); 
					int couleur = (int) imgD.pixels[particlePosArray];
					if (imageFilled[particlePosArray] == 0){
						//p.println(couleur);
						if (couleur < -131587) {
							pColor[i] = couleur;	        				
							particule.lock();	
							pLocked[i] = 1;
							pSize[i] = 2f;
							imageFilled[particlePosArray] = 1;
							//fill the rest with fakes
							imageFilled[p.constrain(particlePosArray-1, 0, IMAGE_PIXELS-1)] = 2;
							imageFilled[p.constrain(particlePosArray+1, 0, IMAGE_PIXELS-1)] = 2;
							imageFilled[p.constrain(particlePosArray-imgD.width, 0, IMAGE_PIXELS-1)] = 2;
							imageFilled[p.constrain(particlePosArray+imgD.width, 0, IMAGE_PIXELS-1)] = 2;
						}
					} else if (imageFilled[particlePosArray] != 2) imageFilled[particlePosArray] = 0;
				} 
			}

		

			float z;
			if (camTravel == true) z = pZ[i];
			else z = 0;

			//INTERPOLATIONS  
			if(pLocked[i] == 1) {
				easing = 0.3f;
				float dX = particule.x - pX[i];
				if (p.abs(dX) > 1) {
					pX[i] += dX * easing;
				}

				float dY = particule.y - pY[i];
				if (p.abs(dY) > 1) {
					pY[i] += dY * easing;
				}

				float dz = z - pZeased[i];
				if (p.abs(dz) > 1) {
					//pZeased[i] += dz * easing;
				}
			} else {
				easing = 1f;
				float dX = particule.x - pX[i];
				if (p.abs(dX) > 1) {
					pX[i] += dX * easing;
				}

				easing = 1f;
				float dY = particule.y - pY[i];
				if (p.abs(dY) > 1) {
					pY[i] += dY * easing;
				}

				easing = 0.022f;
				float dz = z - pZeased[i];
				if (p.abs(dz) > 1) {
					pZeased[i] += dz * easing;
				}
			}

			if (particule.isLocked()==true) {
				c.pushMatrix();
				c.translate(particule.x, particule.y, pZeased[i]);
				c.strokeWeight(1);
				c.stroke(255,0,0);
				//c.point(0, 0, 0);	  
				c.popMatrix();
			}

			if (throwAndRotate == false) { 
				if (numbers) {
					int texti = 1;
					if (p.random(0, 5)>=0) texti = (i+(p.frameCount)/2)%10;
					//else texti = i%10;
					c.stroke(255);
					c.fill(255);
					c.textAlign(p.LEFT);
					c.text(texti, pX[i], pY[i], pZeased[i]);
				} else if(lines){
					if(particule.y < p.height-10){
						for (int j=0;j<200;j++) {
							VerletParticle2D r = physics.particles.get(j);
							if(r.distanceTo(particule) < i%100+70){
								if(i < 100){
									//c.stroke(p.map(r.distanceTo(particule),0,i%80+70,255,0),p.map(r.distanceTo(particule),0,i%80+70,255,0));
									//c.stroke(p.map(r.distanceTo(particule),0,i%80+70,89,0),p.map(r.distanceTo(particule),0,i%80+70,123,0),p.map(r.distanceTo(particule),0,i%80+70,180,0),p.map(r.distanceTo(particule),0,i%80+70,255,0));
									c.stroke(255,20);
									c.strokeWeight(1f);
									c.line(particule.x,particule.y, 0, r.x,r.y, 0);
									c.strokeWeight(1);
									c.stroke(255,pixelDimmer);
									c.point(particule.x,particule.y, pZeased[i]);
								}
							}
						}
					}
				}else { 
					c.strokeWeight(1.f);
					//c.stroke(p.random(0,255),90);
					c.stroke(255,pixelDimmer);
					//c.stroke(255,255);
					//c.stroke(255,80);
					/*
					if(pLocked[i] == 1){
						c.stroke(255,0,0,255);
						c.point(pX[i], pY[i], pZeased[i]);
					} else 	c.point(pX[i], pY[i], pZeased[i]);
					*/
					c.point(pX[i], pY[i], pZeased[i]);
					//c.shape(p.pixel);
					if (p.seqStep >= 8){
						int j = (int) p.random(0,10000);
						if(pLocked[j] == 1 && pLocked[i] == 1){
							VerletParticle2D r = physics.particles.get(j);
							if(r.distanceTo(particule) < 100){
								c.stroke(255,20);
								c.line(pX[i],pY[i], 0,r.x,r.y, 0);		
							}
						}
					}
				}
			}

		

			if (throwAndRotate == true) { 
				if(pY[i] < p.height-100){
					
					
					if(rotationY != 0){
						rotationYEased = rotationYEased + rotationY; 
					}

					easing = 0.01f;
					float dz =  pZ[i] - pZeased[i];
					if (p.abs(dz) > 1) {
						pZeased[i] += dz * easing;
					}

					gravityForce = 0.0f;
					c.pushMatrix();
					if(hasRun == 1){ //S'assurer qu'on a le point de transposition provenant de la main out
						c.strokeWeight(1);
		
						c.stroke(255);
						c.translate(rotatePoint.x, rotatePoint.y); 
						//c.rotateZ((p.mouseX/(float) p.width)*p.PI);
						c.rotateY(rotationYEased/400.f);
						c.point(pX[i]-rotatePoint.x, pY[i]-rotatePoint.y, pZ[i]/5f);
				
						c.strokeWeight(1);
						c.stroke(255,10,10,60);
						
						
					} else {
						c.strokeWeight(1);
						c.stroke(255);
						c.point(pX[i], pY[i], pZ[i]/5f);
					}

					c.popMatrix();
				}

			}

		}
		
		
	}

	public void aiguilleur(int index, float x1,float  y1,float  z1,float x2,float  y2,float  z2){
		c.strokeWeight(1);
		c.stroke(p.random(0,255));
		//c.stroke(255);

		c.point(x1, y1,z1);
		
		int indexAig = (p.frameCount/1) % 10000;
		VerletParticle2D r = physics.particles.get(indexAig);
		c.strokeWeight(2f);
		c.stroke(255,0,0,200);
		if(index == 0){
			if (r.y < p.height - 10 && y1 < p.height-10){
				c.strokeWeight(5);
				c.stroke(255,0,0);
				//c.stroke(255);
				c.point(r.x,r.y, 0);
				c.strokeWeight(1);
				c.line(x1, y1, z1,r.x,r.y, 0);
			}
		}	
	}
	
	
	public int urn(){

		int urnCheck = (int) p.random(1, urn.length);
		if(urn[urnCheck] == 0){
			urnOrder++;
			urn[urnCheck] = 1;
			picked = urnCheck;
		} else {
			return 0;
		}


		return picked;
	}

	public void addParticle() {

		VerletParticle2D particule = new VerletParticle2D(Vec2D.randomVector().scale(2).addSelf((int)(p.random(0, p.width)), p.height+2));
		physics.addParticle(particule);
		// negative attraction force field around the new particle
		particule.addBehavior(new AttractionBehavior(particule, 0.2f, -1.8f, 0.05f));
	}

	public void partition() {
		int offset = 0;
		if(p.seqStep == 0){
			//le drag va s'estomper en Xsecondes
			speeder = 1;
			
			if(timeLine < 103759 - offset){
				if (change[10].test(kinect1Clicksub) && kinect1Clicksub == 1.0f){
					/*
					if (kinectOneHandX/(float)p.width > 0.4f && kinectOneHandX/(float)p.width < 0.6f && kinectOneHandY/(float)p.height > 0.7f){
						bottomWorldBound = 0;		
						physics.removeBehavior(handAttractor);
						physics.removeBehavior(handAttractor2);
					}
					*/
				}
				/*
				if (kinectOneHandX/(float)p.width < 0.4f  && p.kinect1click2 == 1){
					//Avec son pied, traverse l'Žcran
					handPos2.set(p.map(p.kinect2X,0,0.4f*p.width,0,p.width), p.kinect1Y2);
				}
				if (kinectOneHandX/(float)p.width > 0.6f  && p.kinect2click == 1){
					handPos2.set(p.map(p.kinect2X,0.6f*p.width,p.width,0,p.width), p.kinect2Y2);
				}
				*/

				physics.setDrag(p.map(p.constrain(timeLine,0,103759- offset), 0, 103759- offset, 0.05f, 0.01f));
				gravityForce =  0.15f;
				attractorForce = p.map(p.constrain(timeLine,0,103759- offset), 0, 103759- offset, 0.5f, 2.f);
				attractorRadius = 300;
				attractorForce = 0.4f * speeder;
				
				
			}
			if(timeLine < 137063- offset && timeLine > 103759- offset){
				//FX de coup de gravitŽ
				/*
				if (change[10].test(kinect1Clicksub) && kinect1Clicksub == 1.0f){
				
					 if (kinectOneHandX/(float)p.width > 0.4f && kinectOneHandX/(float)p.width < 0.6f && kinectOneHandY/(float)p.height > 0.7f){
					 
						bottomWorldBound = 0;		
						physics.removeBehavior(handAttractor);
						physics.removeBehavior(handAttractor2);
					}
					
				}
				*/
				physics.setDrag(p.map(p.constrain(timeLine,103759- offset,137063- offset), 103759- offset, 137063- offset, 0.01f, 0.f));
				gravityForce =  0.15f;
				attractorForce = p.map(p.constrain(timeLine,103759- offset,137063- offset), 103759- offset, 137063- offset, 2f, 10f) * speeder;
				attractorRadius = p.map(p.constrain(timeLine,103759- offset,137063- offset), 103759- offset, 137063- offset, 200f, 300.f);
			}
			if(timeLine > 137063- offset){
				
				physics.setDrag(0.f);
				gravityForce = 0.16f; //* speeder;
				attractorForce = 1.f;
				attractorRadius = 200f;
			}
		}
		
		
		if(timeLine > 143129 && timeLine < 145129){
			p.seqStep = 1;
		}
		
		//Sucker
		if (p.seqStep == 1){ 
			
			if(change[8].test(p.seqStep)){ 
				stepTimeLineReset = timeLine;
				imageMatcher = false;
				pixelDimmer = 255;
			}
			throwAndRotate = false;
			gravityForce = 0.1f;
			pause = true;
			attractorForce = 4f;
			//Augmentation de la boule
			/*
			if (stepTimeLine > 30000 && stepTimeLine <= 50000) {	
				attractorForce = p.constrain(p.map(stepTimeLine, 30000, 50000, 4, 20),4,20);	
			}
			//Fermeture de la boule wait for cue seqStep2
			if (stepTimeLine > 50000) {
				attractorForce = p.constrain(p.map(stepTimeLine, 50000, 70000, 20, 4),4,20);	
			}
			*/
			attractorRadius = 200f;
			
		}
		
		//Sucker explosion
		if(p.seqStep == 2){
			if(change[8].test(p.seqStep)){ 
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(122); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.cueInterface);
				stepTimeLineReset = timeLine;
				imageMatcher = false;
				pixelDimmer = 255;
			}
			physics.setDrag(0.f);
			gravityForce = 0.048f; //* speeder;
			attractorRadius = 200f;
			pause = false;
			physics.setDrag(0f);
			lines = false;
			
			if(stepTimeLine >= 4000){
				gravityForce = 0.12f * speeder;
			}
		}
		
		//Stall
		if (p.seqStep == 3){
			gravityForce = 0.12f * speeder;
			attractorForce = 0.5f * speeder;
			attractorRadius = 200;

			if(change[8].test(p.seqStep)){ 
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(123); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.cueInterface);
				stepTimeLineReset = timeLine;
				imageMatcher = false;
				pixelDimmer = 255;
			}
			if(kinect1Clicksub == 0){
				throwAndRotate = true;
				pause =true;
				physics.behaviors.clear();
			}
		
			if(change[12].test(kinect1Clicksub)){ 
				if(kinect1Clicksub == 1){	
				timeStopClickCounter++;
				}
			}
			
			if ( timeStopClickCounter >= 5){
				timeStopFlicker = true;
				timeStopLines = false;
			}
			if ( timeStopClickCounter >= 6){
				timeStopFlicker = true;
				timeStopLines = true;
			}
		}
		
		//Stall off
		if (p.seqStep == 4 ){
			throwAndRotate = true;
			pause = false;
			gravityForce = 0.04f;
			if(change[4].test(p.seqStep)){
				imageMatcher = false;
				pixelDimmer = 255;
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(124); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.cueInterface);
				
				OscMessage myMessage2 = new OscMessage("/cueNum");
				myMessage2.add(17); /* add an int to the osc message */
				p.oscP5.send(myMessage2, p.soundComputer);
				myMessage2 = new OscMessage("/cueGo");
				myMessage2.add(1); /* add an int to the osc message */
				p.oscP5.send(myMessage2, p.soundComputer);
				stepTimeLineReset = timeLine; 
				stepTimeLine = 0;
				gravity = new GravityBehavior(new Vec2D(0, gravityForce));
				physics.addBehavior(gravity);
			}
			
			if(stepTimeLine > 11000) p.seqStep = 5; 
	}

		//Gravity up LIVE CUE
		if(p.seqStep == 5){	
			
			if(change[8].test(p.seqStep)){
				imageMatcher = false;
				pixelDimmer = 255;
			}
			throwAndRotate = false;
			pause = false;
			physics.setDrag(0f);
			lines = false;
			gravityPlay = true;
			gravityForce = p.constrain(p.map(kinectOneHandY, p.height, 450, 0.15f, 0.f),0.f,0.15f);

			if(kinectOneHandY == 0) gravityForce = 0.15f;
			if(kinectOneHandY < p.height/2 && kinect1Clicksub == 1) p.seqStep = 40;
		}
		
		if(p.seqStep == 40){
			if(change[8].test(p.seqStep)){
				stepTimeLineReset = timeLine;
				stepTimeLine = 0;
			}
			gravityForce = 0f;
			/*
			if (change[10].test(kinect1Clicksub) && kinect1Clicksub == 0.f){
				p.seqStep = 6;
				pixelDimmer = 255;
			}
			*/
		}
		
	
		if(p.seqStep == 6){
	
			//trigger the drums
			if(kinect1Clicksub == 1 && hasRun4 != 1){
				pixelDimmer = 255;
				hasRun4 = 1;
				//SOUND CUE DRUMES
				OscMessage myMessage2 = new OscMessage("/cueNum");
				myMessage2.add(18); 
				p.oscP5.send(myMessage2,  p.soundComputer);
				myMessage2 = new OscMessage("/cueGo");
				myMessage2.add(1); 
				p.oscP5.send(myMessage2,  p.soundComputer);
				hasRun4 = 1;
			}
			gravityForce = 0f;
			gravityPlay = false;
			attractorForce = 0.9f;
			attractorRadius = 200f;	
		}
		
		
		if(p.seqStep == 7){
			//gravity up
			if(change[4].test(p.seqStep)){
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(125); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.cueInterface);
				pixelDimmer = 255;
				stepTimeLineReset = timeLine;
				stepTimeLine = 0;
			}
			gravityForce = -0.03f;
			gravityPlay = false;
			attractorForce = 1.6f;
			attractorRadius = 400f;
			imageMatcher = false;
			if (stepTimeLine > 6000) p.seqStep = 8;
			
		}
		
		if(p.seqStep == 8){
			//Gravity up
			if(change[4].test(p.seqStep)){
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(126); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.cueInterface);
				pixelDimmer = 255;
				stepTimeLine = 0;
				stepTimeLineReset = timeLine;
				
			}
			gravityForce = -0.04f;
			gravityPlay = false;
			attractorForce = 1.6f;
			attractorRadius = 400f;
			pixelDimmer = (int) p.map(p.constrain(stepTimeLine,0,5000),0,5000,255,200);
			if (stepTimeLine> 5000) imageMatcher = true;
			else imageMatcher = false; 
		}
		
		if(p.seqStep == 9){
			//KILL SILHOUETTE
			if(change[4].test(p.seqStep)){
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(127); /* add an int to the osc message */
				//imageMatcher = false;
				p.oscP5.send(myMessage, p.cueInterface);
				pixelDimmer = 70;
				stepTimeLineReset = timeLine;
				stepTimeLine= 0;
				killSilhouette();
				imageIndex =1;
				imgD = p.loadImage("pic/boy" + imageIndex + ".png");	
			}
			
			
			gravityForce = -0.039f;
			gravityPlay = false;
			attractorForce = 1.6f;
			attractorRadius = 400f;

			pixelDimmer = (int) p.map(p.constrain(stepTimeLine,0,10000),0,10000,200,130);
			if (stepTimeLine> 10000) imageMatcher = true;
			else imageMatcher = false;	
			
			
		}
		
		if(p.seqStep == 10){
			if(change[4].test(p.seqStep)){
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(128); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.cueInterface);
				pixelDimmer = 130;
				stepTimeLineReset = timeLine;
				imageIndex =1;
				imgD = p.loadImage("pic/boy" + imageIndex + ".png");
				attractorForce = 0.3f;
				attractorRadius = 400f;
				gravityForce = -0.04f;
			}
			
			if(kinect1Clicksub == 1) {
				imageMatcher = false;	
				killSilhouette();	
			}
			gravityForce = -0.04f;
			
			if (change[10].test(kinect1Clicksub) && kinect1Clicksub == 0){
				/*
				OscMessage myMessage2 = new OscMessage("/cueNum");
				myMessage2.add(20); 
				p.oscP5.send(myMessage2, p.soundComputer);
				myMessage2 = new OscMessage("/cueGo");
				myMessage2.add(1); 
				p.oscP5.send(myMessage2, p.soundComputer);
				p.switchToSketch = 12;
				*/
				
			}
			
		}
		
		if (kinect1Clicksub == 0f) change[5].test(kinect1Clicksub);
	}

	public void killSilhouette() {
		int countea = 0;
		for (VerletParticle2D particule : physics.particles) {
			if (particule.isLocked() == true) {
				particule.clearForce();
				particule.clearVelocity();
				particule.unlock();
			}
			if(pLocked[countea] == 1){
				//pLocked[countea] = 0;
			}
			countea++;
		}
	}

	public void events(){
	}

	public  void mousePressed() {
		mousePos = new Vec2D(p.mouseX, p.mouseY);
		// create a new positive attraction force field around the mouse position (radius=250px)
		mouseAttractor = new AttractionBehavior(mousePos, attractorRadius, attractorForce);
		physics.addBehavior(mouseAttractor);
	}

	public void mouseDragged() {
		// update mouse attraction focal point
		mousePos.set(p.mouseX, p.mouseY);
	}

	public void mouseReleased() {
		// remove the mouse attraction when button has been released
		physics.removeBehavior(mouseAttractor);

	}

	public void keyPressed() {
		
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


		if(kinect1Clicksub == 1.0f && hasRun != 1 && throwAndRotate) {
			rotatePoint = new Vec2D(kinectOneHandX,kinectOneHandY);
			physics.behaviors.clear();
			hasRun = 1;
		}
		
		if(kinect1Clicksub == 1.0f) {
			
			if(change[3].test(kinect1Clicksub)){
				
				if(hasRun == 1 && throwAndRotate){
					rotationStartPoint = kinectOneHandX;
				}
			
				if(p.seqStep == 40){
						p.seqStep = 6;
						
				}
				
				if(p.seqStep == 6){
					/*
					gravityForce = 0.039f;
					//gravityForce = 0.0f;
					if(hasRun2 != 3 ){ 
						OscMessage myMessage2 = new OscMessage("/cueNum");
						myMessage2.add(38); add an int to the osc message 
						p.oscP5.send(myMessage2, p.soundComputer);
						myMessage2 = new OscMessage("/cueGo");
						myMessage2.add(1); add an int to the osc message
						p.oscP5.send(myMessage2, p.soundComputer);
						hasRun2++; //3
					}
				
					*/
				}
				
					

			}
			if(hasRun == 1 && throwAndRotate){
				int diff;
				if(kinectOneHandX - rotationStartPoint < 15  && kinectOneHandX - rotationStartPoint > -15){
					diff = 0;
				
				} else diff = (int) (kinectOneHandX - rotationStartPoint);

				if(diff < 0) rotationY = p.map(p.constrain(diff,-200,-15),-15,-100,.0f,0.005f);
				else rotationY = p.map(p.constrain(diff,15,200),15,100,.0f,-0.005f);
				
			}
		} 
			
			
		if(kinect1Clicksub == 0) {
			
			if(change[3].test(kinect1Clicksub)){
				//nothing
				if(step == 8) step = 9;
			}
			//if (rotationY > 0.01) rotationY=rotationY - 0.001f;
			//if (rotationY < 0.01) rotationY=rotationY + 0.001f;

		}
	}

	public void hands(){
		
		if(step > 6){ //pas pendant l'apparition du mot deeper

			//HAND1
			if(p.kinect1click == 1.0f) {
				if (change[1].test(p.kinect1click)) { 
					
					handPos = new Vec2D(p.kinect1X, p.kinect1Y);
					handAttractor = new AttractionBehavior(handPos, attractorRadius, attractorForce);
					physics.addBehavior(handAttractor);
				}
				handPos.set(p.kinect1X, p.kinect1Y);
			}

			if (p.kinect1click == 0f){

				if (change[1].test(p.kinect1click)) { 
					physics.removeBehavior(handAttractor);
				}
			}

			//HAND2
			if(p.kinect1click2 == 1.0f) {
				if (change[2].test(p.kinect1click2)) { 
					handPos2 = new Vec2D(p.kinect1X2, p.kinect1Y2);
					handAttractor2 = new AttractionBehavior(handPos2, attractorRadius, attractorForce);
					physics.addBehavior(handAttractor2);
				}
				handPos2.set(p.kinect1X2, p.kinect1Y2);
			}
			if (p.kinect1click2 == 0f){
				if (change[2].test(p.kinect1click2)) { 
					physics.removeBehavior(handAttractor2);
				}
			}

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

}
