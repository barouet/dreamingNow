package seq;

import java.util.ArrayList;

import dreaming.DreamingNow;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;
import toxi.geom.*;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;


public class FUTURE extends Sketch {

	  int NUM_PARTICLES = 8000;

	  int stepTimeLine;
	  int stepTimeLineReset;

	  int speeder;//Compense pour la perte de fps - CHANGER DANS LE REINIT
	  float attractorForce = 1.0f * speeder; // * x to compensate for slow frameRate
	  float gravityForce = 0.15f * speeder; // entre 0. et 0.9
	  float attractorRadius = 200;
	  
	  int time = 0 ;
	  int countea;
	  int maxCountea;
	  float camPosX, camPosY, camPosZ, camLookX, camLookY, camLookZ;
	  Vec3D camPos, camLook;
	  float camPosXEased, camPosYEased, camPosZEased, camLookXEased, camLookYEased, camLookZEased;
	
	  int fakePointsAlpha = 255;
	  
	  float[] pX = new float[NUM_PARTICLES];
	  float[] pY = new float[NUM_PARTICLES];
	  float[] pZ = new float[NUM_PARTICLES];
	  float[] pZeased = new float[NUM_PARTICLES];
	  float[] fadeIn = new float[NUM_PARTICLES];
	  Change[] change = new Change[10];

	  //urn vars.
	  int[] urn = new int[NUM_PARTICLES]; 
	  int picked;
	  int urnOrder= 0;

	  float rot = 0;
	  boolean camTravel;
	  int timerStart = 0;
	  int pLockedSum = 0;
	  boolean imageMatcher = false;
	  boolean wallMaker = true;
	  boolean pause = false;
	  boolean numbers = true;
	  boolean pixels = false;
	  boolean webCamPoint = false;
	  boolean couleurPixel = true;
	  boolean kinectMatcher = true;
	  boolean centerPoint = true;
	  boolean lookAtFirst = true;
	  boolean physicUpdate = true;
	  int positionImageX = 400;
	  int positionImageY = 200;
	  int imageRadius = 100; 

	  VerletPhysics2D physics;

  
	  GravityBehavior gravity;

	  Vec2D rotatePoint = new Vec2D(0,0);

	  Vec2D imagePos;
	  float particuleSpeed;
	  int constellation1, constellation2, constellation3, constellation4, constellation5, constellation6;

	  int prevSequenceNumber = 0;
	  
	  //Rotation stuff
	  boolean throwAndRotate = false;
	  float rotationY = 0f;
	  float rotationYEased = 0f;
	  float rotationStartPoint = 0;
	  int hasRun = 0;
	  int hasRun3 = 0;
	  
	  //From number wall
	  //NUMBER WALL
	  ArrayList lettre;
	  int alpha;
	  int alphaVideo;
	  int curtainHigh= 0 ;
	  int curtainLow= 0 ;
	  int curtainMode;
	  //int counter = lettre.length;
	  boolean counterUp;
	  boolean counterDown;
	  boolean counterReset;
	  boolean counterZero;
	  int counterRemplissage = 0;
	  int couleur;
	  int blockSize = 12;
	  int numPixelsWide = p.width / blockSize;
	  int numPixelsHigh = p.height / blockSize;
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
	  int counterMax = 10;
	  int counterTemp = 0;
	  PImage deeper;
	  int deeperCouleur = 0;
	  int texti = 0;
	  int curtainDown = 0;
	  float easing = 0.01f;
	  float easingEase = 0.1f;
	  int opacity;
	  int entrance = 0;
	  float camEasing;
	  int physicsUpdate = 1;
	  
	  public FUTURE(DreamingNow p, PGraphicsOpenGL c) {

		  super(p, c);
		  physics = new VerletPhysics2D(); 
		  physics.setWorldBounds(new Rect(-100, 0, p.width*10, p.height*10)); //box definition
		  gravity = new GravityBehavior(new Vec2D(0, gravityForce));
		  physics.addBehavior(gravity);
		  imagePos = new Vec2D(0,0);
		  lettre = new ArrayList();
		  for (int i = 0; i < change.length; i++) {
			  change[i] = new Change();
		  }
		  
	  }

	  public void reinit() {
		  
		  super.reinit(); 
		  kill();
		  c.resetShader();
		  c.textFont(p.font, 12);
		  
		  c.textAlign(p.CENTER, p.BASELINE);  
		  lettre.clear();
		  for (int j = 0; j < NUM_PARTICLES; j++) {
				  lettre.add(new Lettre(0, 0, 0));
		  }
		
		
		  stepTimeLineReset = 0;
		  stepTimeLine = 0;
		  particuleSpeed = 0.6f;
		  gravity = new GravityBehavior(new Vec2D(0, gravityForce));
		  physics.addBehavior(gravity);
		  
		  p.wallEasing = 0.2f; // Easing setting for wall kinect;
		  physicsUpdate = 1;
		  speeder = 2; 
		  gravityForce = 0.0f * speeder;
		  attractorForce = 0.6f * speeder;
		  attractorRadius = 2000;

		  p.shaderSelect = 0;

		  for (int i = 0; i < NUM_PARTICLES; i++) {
			  addParticle();
			  pZ[i] = p.random(-1000, 0);
		  }

		  //REMPLIR L'ARRAY DE CORRESPONDANCES
		  for (int i = 0; i < urn.length; i++) { 
			  urn[i] = 0;
		  }

		  int urnAllocation = 0;


		  for (int i = 0; i < fadeIn.length; i++) {
			  fadeIn[i] = 0;
			 
		  }
		  
		  for (int i = 0; i < pX.length; i++) {
			  pX[i] = p.random(0,1000);
			  if (i < 1) pZ[i] =  0;
		  }

		  for (int i = 0; i < pY.length; i++) {
			  pY[i] = p.random(0,1000);
			  if (i < 1) pZ[i] =  0;
		  }

		  for (int i = 0; i < pZ.length; i++) {
			  pZ[i] =  p.random(-1200,1200);
			  if (i < 2) pZ[i] =  0;
		  }
		  
		  int index = 0;
		  for (VerletParticle2D particule : physics.particles) {
			  
			  //particule.setPreviousPosition(new Vec2D(p.random(0,1000),p.random(0,1000)));
			  //particule.set(new Vec2D(p.random(0,1000),p.random(0,1000)));
			  //pZ[index] = p.random(0,1000);
			  particule.setPreviousPosition(new Vec2D(pX[index],pY[index]));
			  particule.set(new Vec2D(pX[index],pY[index]));
			 // pZ[index] = p.random(-3000,3000);
			  index++;
		  }
		  
		  
		  
		  p.textAlign(p.CENTER, p.CENTER);
		  p.textFont(p.font, 8);

		  //Change reset
		  for (int i = 0; i < change.length; i++) {
			  change[i].reset();
		  }
		  c.rectMode(p.CORNER);
		  hasRun = 0;
		  hasRun3 = 0;
		  throwAndRotate = false;
		  rotationYEased= 0f;
		  p.seqStep = 3;
		  step = 0;
		  shakeContamineX=0;
		  curtainDown = 0;
		  camEasing = 0.001f; 
		  pause = false;
		  numbers = true;
		  pixels = false;
		  webCamPoint = false;
		  couleurPixel = true;
		  kinectMatcher = false;
		  camTravel = true;
		  wallMaker = false;
		  centerPoint = false;
		  lookAtFirst = true;
		  physicUpdate = true;
		  camPos = new Vec3D(p.width/2.0f, p.height/2.0f,(p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f));
		  camLook = new Vec3D(p.width/2.0f, p.height/2.0f, 0);

		  
		  camPosXEased = p.width/2.0f;
		  camPosYEased = p.height/2.0f;
		  camPosZEased = (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f);
		  camLookXEased = p.width/2.0f;
		  camLookYEased = p.height/2.0f;
		  camLookZEased = 0;
		  entrance = 0;
		  easing = 0.01f;
		  opacity = 255;
		  c.strokeCap(p.SQUARE);
		  c.hint(p.DISABLE_DEPTH_TEST);
		  c.hint(p.DISABLE_DEPTH_MASK);
		  p.println("FUTURE");
		  p.frameRate(30);
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
		  p.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
	  }

	  public float linearEasing(float speed, float target, float output){
		  float vitesse = speed;
		  float xd = target - output;
		  if ( xd > 0 ) {
			  output = p.min(xd, vitesse) + output;
		  }
		  else {
			  output = p.max(xd, -vitesse) + output;
		  }
		  return output;
	  }
	  
	  public void draw() {
		  
		  physics.setWorldBounds(new Rect(0, 0, 1200, 1200)); //box definition
		  
		  super.draw(); 
		 
		  events();
		  partition();
		  stepTimeLine = timeLine - stepTimeLineReset;
		  c.beginDraw();
		  c.lights();
		  c.ambientLight(255, 255, 255);

		  c.background(0);
		  if(physicsUpdate == 5){
			  physics.update();
			  physics.update();
			  physics.update();
			
		  } else if (physicsUpdate == 1)physics.update();

			  float dx = camPos.x - camPosXEased;
			  if (p.abs(dx) > 1) {
				  camPosXEased += dx * camEasing;
			  }
			  float dx1 = camPos.y - camPosYEased;
			  if (p.abs(dx1) > 1) {
				  camPosYEased += dx1 * camEasing;
			  }
			  float dx2 = camPos.z - camPosZEased;
			  if (p.abs(dx2) > 1) {
				  camPosZEased += dx2 * camEasing;
			  }
			  float dx3 = camLook.x - camLookXEased;
			  if (p.abs(dx3) > 1) {
				  camLookXEased += dx3 * camEasing;
			  }
			  float dx4 = camLook.y - camLookYEased;
			  if (p.abs(dx4) > 1) {
				  camLookYEased += dx4 * camEasing;
			  }
			  float dx5 = camLook.z - camLookZEased;
			  if (p.abs(dx5) > 1) {
				  camLookZEased += dx5 * camEasing;
			  }
		 
		 
		  c.camera(camPosXEased, camPosYEased, camPosZEased, camLookXEased, camLookYEased, camLookZEased, 0, 1, 0);
		 
		  ////////////////////////PARTICLE RENDERER
		  //////////////////////////////////////////
		  for (int i=0;i<NUM_PARTICLES;i++) {
			  VerletParticle2D particule = physics.particles.get(i);
			  //p.setPreviousPosition(new Vec2D(0+mouseX,0+mouseY));
			  
			  if (pause) { 
				  particule.clearForce();
				  particule.clearVelocity();
			  }

			  if( i != constellation1){
				  ///WALL MAKER
				  if (wallMaker == true) { //on/off sur l'image
					  if (particule.isLocked()==false) {
						  int blockSize = 12;
						  particule.x = 12 * (i%(p.width/blockSize));
						  if( i/(p.width/blockSize) < p.height/blockSize) {
							  particule.y = 12 * (i/(p.width/blockSize));
						  } else particule.y = p.height +100;
					  }
				  }
			  }
			  //float z;
			 // z = pZ[i];
			  easing = 0.2f;
			  if(wallMaker){ 
				  //z = 0;
				  pZ[i] = 0;
				  easing = 0.2f;
			  
			  }
			  			  
			  float dE = easing - easingEase;
			  if (p.abs(dE) > 0.01f) {
				  easingEase += dE * 0.1f;
			  }
			  
		////////INTERPOLATIONS  PIXELS  
			  if (wallMaker){
				
				  float vitesse = particuleSpeed;
				  //X
				  float xd = particule.x - pX[i];
				  if ( xd > 0 ) {
					  pX[i] = p.min(xd, vitesse) + pX[i];
				  }
				  else {
					  pX[i] = p.max(xd, -vitesse) + pX[i];
				  }
				  //Y
				  float yd = particule.y - pY[i];
				  if ( yd > 0 ) {
					  pY[i] = p.min(yd, vitesse) + pY[i];
				  }
				  else {
					  pY[i] = p.max(yd, -vitesse) + pY[i];
				  }
				  //Z
				  float zd = pZ[i] - pZeased[i];
				  if ( zd > 0 ) {
					  pZeased[i] = p.min(zd, vitesse) + pZeased[i];
				  }
				  else {
					  pZeased[i] = p.max(zd, -vitesse) + pZeased[i];
				  }
				
			}
			  else {	  
				  easing = 0.008f;
				  float dX = particule.x - pX[i];
				  if (p.abs(dX) > 1) {
					  pX[i] += dX * easing;
				  }

				  //easing = 1f;
				  float dY = particule.y - pY[i];
				  if (p.abs(dY) > 1) {
					  pY[i] += dY * easing;
				  }

				  //easing = 0.01f;
				  float dz = pZ[i] - pZeased[i];
				  if (p.abs(dz) > 1) {
					  pZeased[i] += dz * easing;
				  } 
			  } 

			  ///RENDER ICI
			  //on screen POINT
			  Lettre l = (Lettre) lettre.get(i);
			  counterTemp = l.counter;
			  int fadeIn2 = (int) p.map(i, 0, 17999, 255,0);

			  if(i < entrance) fadeIn[i] = fadeIn[i] + 1;
			  else fadeIn2 = 0;
			

			  c.stroke(255);
			  if(step == 0) c.fill(0);
			  else c.fill(255);
			 				
			  c.stroke(255,fadeIn[i]);
			  c.fill(255,fadeIn[i]);
			  c.text(counterTemp, pX[i]+7, pY[i]+14, pZeased[i]);
		  
		  }
		  
		  
		  
		  c.fill(0);
		  c.pushMatrix();
		  c.translate(0, 0, 100);
		  c.noStroke();
		  c.rect(0,0,p.width,0+curtainDown);
		  c.popMatrix();
		  c.endDraw();

		  digitalWall(1, 0);
		 
	  }

	  public void addParticle() {

		  VerletParticle2D particule = new VerletParticle2D(Vec2D.randomVector().scale(2).addSelf((int)(p.random(0, p.width)), p.height));
		  physics.addParticle(particule);
		  // negative attraction force field around the new particle
		  particule.addBehavior(new AttractionBehavior(particule, 0.2f, -1.8f, 0.05f));
	  }

	  public void events(){

			//Pixels
		  if (p.seqStep == 3){ 
				if(change[6].test(p.seqStep)){
					 constellation1 = 0;
					 constellation2 = 0;
				}
			  kinectMatcher = false;
			  couleurPixel = false;
			  numbers = true;
			  pixels = false;
			  wallMaker = false;
			  camTravel = true;
			  pause = false;
			  lookAtFirst = true;
				 contentShake(0,3,10);
				 contentShake(0,3,10);
				 contentShake(0,3,10);
			 
			}
			
			//Numbers
			if (p.seqStep == 4){ 
				if(change[6].test(p.seqStep)){
					 constellation1 = 0;
					 constellation2 = 0;
					 //constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
					 //constellation2 = (int) p.random(0, NUM_PARTICLES);
				}
			  kinectMatcher = false;
			  couleurPixel = false;
			  numbers = true;
			  pixels = false;
			  wallMaker = false;
			  camTravel = true;
			  pause = false;
			  lookAtFirst = false;
				 contentShake(0,3,10);
				 contentShake(0,3,10);
				 contentShake(0,3,10);
			}
			
			//Perspective in
			if (p.seqStep == 5){ 
				 easing = 0.01f; 
				 
				 VerletParticle2D p1 = physics.particles.get(constellation1);
				 VerletParticle2D p2 = physics.particles.get(constellation2);
				 p1.setPreviousPosition(new Vec2D(p.width/2.0f,  p.height/2.0f ));
				 p1.set(new Vec2D(p.width/2.0f,  p.height/2.0f ));
				 pZ[constellation1] = -400;
				 p2.setPreviousPosition(new Vec2D(p.width/2.0f,  p.height/2.0f ));
				 p2.set(new Vec2D(p.width/2.0f,  p.height/2.0f ));
				 pZ[constellation2] = -800;
				
				 camTravel = true;
				 contentShake(0,3,10);
				 contentShake(0,3,10);
				 contentShake(0,3,10);
			}
		
			//Randomness mvt
			if (p.seqStep == 6){
				if(change[6].test(p.seqStep)){
					stepTimeLineReset = timeLine;
					stepTimeLine = 0;
				}
				
				kinectMatcher = false;
				contentShake(0,3,10);
				contentShake(0,3,10);
				contentShake(0,3,10);
			
				opacity = (int) p.map(stepTimeLine, 0, 4000, 255, 0);
			}

			
			if (p.seqStep == 7){ 
				if(change[6].test(p.seqStep)){
					
				}
				lookAtFirst = false;
				pause = true;
				camTravel = false;
				if(change[9].test(7)){
					constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
					constellation2 = (int) p.random(0, NUM_PARTICLES);
				}

				wallMaker = true;
				contentShake(0,3,10);
				contentShake(0,3,10);
				contentShake(0,3,10);
				contentShake(0,3,10);
			}

			if (p.seqStep == 8){ 
				camTravel = false; 
				wallMaker = true;
				contentShake(0,3,10);
				contentShake(0,3,10);
				contentShake(0,3,10);
			}

			if (p.seqStep == 9){ 
				contentShake(0,3,10);
				contentShake(0,3,10);
				contentShake(0,3,10);
			}
		
			if (p.seqStep == 10){
				if(change[6].test(p.seqStep)){
					stepTimeLineReset = timeLine;
					stepTimeLine = 0;
				}
				curtainDown = (int) p.map(p.constrain(stepTimeLine, 0, 36000),0,36000,0, p.height); 
				contentShake(0,3,10);
				contentShake(0,3,10);
				contentShake(0,3,10);
				contentShake(0,3,10);
			}
			 
			if (p.seqStep == 11){
				if(change[6].test(p.seqStep)){
					stepTimeLineReset = timeLine;
					stepTimeLine = 0;
				}
				curtainDown = (int) p.map(p.constrain(stepTimeLine, 0, 36000),0,36000,p.height, 0); 
				contentShake(0,3,10);
				contentShake(0,3,10);
				contentShake(0,3,10);
			}
			
	  }

	  public void partition() {  
		  if(timeLine > 5000 && timeLine < 10000) entrance = 5;
		  if(timeLine > 10000 && timeLine < 40000) entrance = (int) p.map(p.constrain(timeLine, 10000, 40000), 10000, 40000, 5, 200);
		  if(timeLine > 40000 && timeLine < 60000) entrance = (int) p.map(p.constrain(timeLine, 40000, 60000), 40000, 60000, 200, 2000);
		  if(timeLine > 60000 && timeLine < 90000) entrance = (int) p.map(p.constrain(timeLine, 60000, 90000), 60000, 90000, 2000, 15000);
		  
		  int cue = 79498; //CAM MOUVEMENT //196929
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 1){
			  camEasing = 0.001f;
			  p.println("CUE1");
			  p.seqStep = 4;
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  VerletParticle2D p1 = physics.particles.get(constellation1);
			  VerletParticle2D p2 = physics.particles.get(constellation2);
			  camPos.set(p1.x,p1.y,pZ[constellation1]);
			  camLook.set(p2.x,p2.y,pZ[constellation2]);
			  hasRun3++;
			  particuleSpeed = 8;
			  physicsUpdate = 5;
		  }
		
		  cue = 101756; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 2){
			  physicsUpdate = 0;
			  camEasing = 0.001f;
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  VerletParticle2D p1 = physics.particles.get(constellation1);
			  VerletParticle2D p2 = physics.particles.get(constellation2);
			  camLook.set(p2.x,p2.y,pZ[constellation2]);
			  camPos.set(p1.x,p1.y,pZ[constellation1]);
			  hasRun3++;
			 
			  particuleSpeed = 0.01f;
		  }
		  
		  cue = 122961; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 3){
			  camEasing = 0.001f;
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  VerletParticle2D p1 = physics.particles.get(constellation1);
			  VerletParticle2D p2 = physics.particles.get(constellation2);
			  camLook.set(p2.x,p2.y,pZ[constellation2]);
			  camPos.set(p1.x,p1.y,pZ[constellation1]);
			  hasRun3++;
			  physicsUpdate = 0;
			  particuleSpeed = 8;
		  }
		
		  cue = 145008; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 4){
			  camEasing = 0.001f;
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  VerletParticle2D p2 = physics.particles.get(constellation2);
			  camPos.set(p.width-100, p.height - 100,100);
			  camLook.set(p2.x,p2.y,pZ[constellation2]);
			  
			  camPos.set(1094.8944f,10.454194f, -424.11432f);
			  camLook.set(353.43307f, 800.8917f,-57.290283f);
			  
			  
			  hasRun3++;
			  physicsUpdate = 1;
			  particuleSpeed = 1;
		  }
		  
		  cue = 170994; //TO WALL
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 5){
			  camEasing = 0.004f;
			  particuleSpeed = 0.8f;
			  hasRun3++;
			  camPos = new Vec3D(p.width/2.0f, p.height/2.0f,(p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f));
			  camLook = new Vec3D(p.width/2.0f, p.height/2.0f, 0);
			  p.seqStep = 7;
		  }
		  
		  cue = 218033; //CURTAIN DOWN
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 6){
			  p.seqStep = 10;
		  }
		  
		   /*
		  cue = 240038; //STOP
		  if (timeLine > cue && timeLine < cue+500){
			  p.seqStep = 7;
		  }
		  /*
		  cue = 245648; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 7){
			  //constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  //constellation2 = (int) p.random(0, NUM_PARTICLES);
			  hasRun3++;
			  p.seqStep = 8;
		  }
		  
		  cue = 251915; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 8){
			  //constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  //constellation2 = (int) p.random(0, NUM_PARTICLES);
			  hasRun3++;
			 
		  }
		  
		  cue = 254357; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500){
			  //p.seqStep = 8;
		  }
		  
		  cue = 254387; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500){
			  //p.seqStep = 9;
		  }
		  
		  cue = 265000; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500){
			  p.seqStep = 10;
		  }
		    */
	  }
	  
	  public  void mousePressed() {
		  p.println(timeLine);
		  p.println(camPos);
		  p.println(camLook);
	  }

	  public void mouseDragged() {
	  }

	  public void mouseReleased() {
	  }

	  public void keyPressed() {
		

		  if (p.keyEvent.isShiftDown()) {
			  
			  if (p.keyCode == 'C') {
				  camTravel = true;
				  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
				  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  }

			  if(p.keyCode == 'Q') {
				  throwAndRotate = !throwAndRotate;
			  }
			  
			  if (p.keyCode == 'G') {
				  gravityForce = 0.0f;
			  }
			  
			  if (p.keyCode == 'H') {
				  gravityForce = 0.5f;
			  }
			  
			  if (p.keyCode == 'N') {
				  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
				  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  }

			  if (p.keyCode == 'M') {
				  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  }

			  if (p.keyCode == 'L')
				  pause = true;
			  
			  if(p.keyCode == 'W')
				  wallMaker = !wallMaker;

			  if (p.keyCode == 'U')
				  pause = false;
			  
			  if (p.keyCode == 'P')
				  couleurPixel = true;
			  
			  if (p.keyCode == 'T')
				  numbers = !numbers;

			  if (p.keyCode == 'R')
				  pixels = !pixels;
			  
			  if (p.keyCode == 'Y')
				  webCamPoint = !webCamPoint;
			  
			  if (p.keyCode == 'E'){
				  camTravel = !camTravel;
				 
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

	  class Lettre {
			public int afficheQuoi;
			public boolean fillBypass;
			int counter = 0;
			int coordX;
			int coordY;
			int coordZ;
			int coordZeased;
			int id = (int)p.random(0, 100);
			int colore;
			int chiffreLettre;
			int max;
			int min;
			int lettre = 50;
			int life = 100;
			int mapMin = 255;
			int mapZ = 0;
			float easing = 0.02f;
			float lifeEasing = 1f;
			int lifeEased;

			Lettre(int cStart, int oX, int oY) {
				counter = cStart;
				coordX = oX;
				coordY = oY;
				coordZ = 0;
				coordZeased = 0;
				colore = (int)p.random(0,255);
			}

			void count(int cMin, int cMax) {
				counter=p.constrain((counter+1)%cMax, cMin, cMax);
				max = cMax;
				min = cMin;
			}

			void couleur(int c) {
				colore = c;
			}

			void afficheText(boolean zaxis, int quoi, int fillQuoi, boolean rotateOnItself) { 

			}

			void affichePoint(boolean zaxis) { 
			}

			int id() { 
				 return id;  
			  }
			}
	  
	  public void contentShake(int numLettre, int shakeWhat, int isolateNumber){
			
			switch (shakeWhat) {

			case 0: //TOUT LE WALL SHAKE

				int rand = (int)p.random(0, 100);
				int rand2 = (int)p.random(0, 100);
				int rand3 = (int)p.random(0, 100);
				int rand4 = (int)p.random(0, 100);
				for(int i = 0; i< lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					if(l.id() == rand || l.id() == rand2 || l.id() == rand3 || l.id() == rand4){
						//Lettres ou chiffres
						if(numLettre == 0) l.count(0,9);
						else { 
							l.lettre = (l.lettre+1);
							if(l.lettre > 122) l.lettre = 65;
							if(l.lettre > 90 && l.lettre < 97)  l.lettre = 97;
						}
					}
				}
				break;
				
			case 1: //UNE SEULE LIGNE SHAKE
				/*
				for(int i = 0; i < numPixelsHigh; i=i+11){
					oneLineShake(0,i,0);
				}
				

				for(int i = 0; i < numPixelsWide; i=i+12){
					oneLineShake(1,i,0);
				}
				*/
				break;

			case 2: //UN SEUL CHIFFRE SHAKE
				
			
				Lettre l = (Lettre) lettre.get(p.constrain(numPixelsWide/2+((numPixelsHigh/2)*numPixelsWide),0,lettre.size()));
			
					//Lettres ou chiffres
					if(numLettre == 0) {
						if(p.frameCount%4 == 0) {
							
							l.count(0,10);
						}
					}
					
				break;


			case 3: //DU HAUT VERS LE BAS
				
				l = (Lettre) lettre.get(p.constrain(numPixelsWide/2+((numPixelsHigh/2)*numPixelsWide),0,lettre.size()));
				l.fillBypass = false;
				rand = (int)p.random(0, 100);
				rand2 = (int)p.random(0, 100);
				rand3 = (int)p.random(0, 100);
				rand4 = (int)p.random(0, 100);
			
				for(int x = 0; x < shakeContamineX; x++){
						
						l = (Lettre) lettre.get(p.constrain(x,0,lettre.size())); //Map pour centrer l'opŽration
						if(l.id() == rand || l.id() == rand2 || l.id() == rand3 || l.id() == rand4){
							//Lettres ou chiffres
							
							if(numLettre == 0) {
								l.count(0,10);
							
							}
							if(x == shakeContamineX){
								l.counter = 0;
								//l.fillBypass = true;
							}
							
							else { 
								l.fillBypass = false;
								l.lettre = (l.lettre+1);
								if(l.lettre > 122) l.lettre = 65;
								if(l.lettre > 90 && l.lettre < 97)  l.lettre = 97;
							}
						}
					
				}
			
				if(p.frameCount % 2 == 0) {
					shakeContamineX=shakeContamineX+2;
				}
			
				shakeContamineX = (int) p.constrain(shakeContamineX, 0, lettre.size()); 
				break;
				
			case 4: //Tout shake vers 0

				rand = (int)p.random(0, 100);
				rand2 = (int)p.random(0, 100);
				rand3 = (int)p.random(0, 100);
				rand4 = (int)p.random(0, 100);
				for(int i = 0; i< lettre.size(); i++){
					l = (Lettre) lettre.get(i);
					if(l.id() == rand || l.id() == rand2 || l.id() == rand3 || l.id() == rand4){
						//Lettres ou chiffres


						if(numLettre == 0) {
							if(l.counter != isolateNumber)  l.count(0,10);
						}
						else { 

							l.lettre = (l.lettre+1);
							if(l.lettre > 122) l.lettre = 65;
							if(l.lettre > 90 && l.lettre < 97)  l.lettre = 97;
						}
						
					}
				}
				break;
				
			case 5: //LESS ZEROS

				rand = (int)p.random(0, 100);
				rand2 = (int)p.random(0, 100);
				rand3 = (int)p.random(0, 100);
				rand4 = (int)p.random(0, 100);
				for(int i = 0; i< lettre.size(); i++){
					l = (Lettre) lettre.get(i);
					if(l.id() == rand || l.id() == rand2 || l.id() == rand3 || l.id() == rand4){
						//Lettres ou chiffres
						if(numLettre == 0) l.count(0,counterMax);
						else { 
							l.lettre = (l.lettre+1);
							if(l.lettre > 122) l.lettre = 65;
							if(l.lettre > 90 && l.lettre < 97)  l.lettre = 97;
						}
					}
				}
				break;
			}
			
		}
	  
	  public boolean digitalWall(int direction, int mode){
			
			if(direction == 1){
				
				for(int i = 0; i < counterRemplissage; i++){
					
					Lettre l = (Lettre) lettre.get(i);
					l.couleur(255);
					
					if(mode == 0){ //Normal Mode
						l.lettre = text[i];
						l.afficheText(false,0,0,false);
						l.life = 255;
						
					}
					
					if(mode == 1){ //Teinte de gris Mode
						l.lettre = text[i];
						l.afficheText(false,0,1,false);
						l.life = 255;
						
					}

					if(mode == 2){ //wordScroller Mode
						if (i>wordScroller2 -10 && i < wordScroller2){
							l.lettre = text[i];
							l.afficheText(false,3,2,false);
							l.life = 255;
						}
						else if(i>wordScroller && i < wordScroller+10){
							l.lettre = text[i];
							l.afficheText(false,3,2,false);
							l.life = 255;
						} 
						else l.afficheText(false,3,1,false);
					}
					
					if(mode == 3){ //lettres + counterFilling[0-9]
						if(i>wordScroller && i < wordScroller+10){
							l.lettre = text[i];
							l.afficheText(false,3,3,false);
							l.life = 255;
						} else if(i>wordScroller2 && i < wordScroller2+10){
								l.lettre = text[i];
								l.afficheText(false,3,3,false);
								l.life = 255;
							} else l.afficheText(false,3,3,false);
					}
					
					if(mode == 4){ //lettres + counterFilling[0-9]
						
						if( i <= p.map(shakeContamineX,-1911,1911,0,lettre.size()) && i > 1911){
							l = (Lettre) lettre.get(i); //Map pour centrer l'opŽration
							l.afficheText(false,3,4,false);
						} else if ( i > 1911) l.afficheText(false,0,1,false);


						if(i >= p.map(0-shakeContamineX,-1911,1911,0,lettre.size()) && i < 1911){
							l = (Lettre) lettre.get(i); //Map pour centrer l'opŽration
							l.afficheText(false,3,4,false);
						} else if( i<= 1911) l.afficheText(false,0,1,false);

					}

					if(mode == 5){ //Normal Mode with 3d
						l.lettre = text[i];
						l.afficheText(true,0,0,false);
						l.life = 255;
						
					}
					
					if(mode == 6){ //Gray scale Mode with 3d
						l.lettre = text[i];
						l.afficheText(true,0,1,false);
						l.life = 255;
					}
					
					if(mode == 8){ //Gray scale Mode with 3d FREE LIFE
						l.lettre = text[i];
						l.afficheText(true,0,1,false);
					}
					
					if(mode == 7){ //Normal Mode with Axis ROtation
						l.lettre = text[i];
						l.afficheText(false,0,0,true);
						l.life = 255;
						
					}
				}
				
				
			}
			
			if(direction == -1){
				c.hint(p.ENABLE_DEPTH_TEST);
				for(int i = 0; i< counterRemplissage; i++){
					Lettre l = (Lettre) lettre.get(i);
					l.couleur(255);

					if(mode == 0){ //Normal Mode
						l.lettre = text[i];
						l.afficheText(false,3,0,false);
						l.life = 255;
					}

					if(mode == 2){ //wordScroller Mode
						if(i>wordScroller && i < wordScroller+10){
							l.lettre = text[i];
							l.afficheText(false,3,2,false);
							l.life = 255;
						} else l.afficheText(false,3,1,false);
					}
					counterRemplissage = p.constrain(counterRemplissage +=4, 0,lettre.size());
				}
			}
			
			boolean completed;
			if(counterRemplissage == lettre.size()) completed = true;
			else completed = false;
			
			return completed;
		}
}
