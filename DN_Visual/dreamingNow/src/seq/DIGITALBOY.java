package seq;

import java.util.ArrayList;

import dreaming.DreamingNow;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;
import toxi.geom.*;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;


public class DIGITALBOY extends Sketch {

	  int NUM_PARTICLES = 15000;
	  int[] kinectToParticules = new int[307200];

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
	  float camPosXEased, camPosYEased, camPosZEased, camLookXEased, camLookYEased, camLookZEased;
	  int fakePointsAlpha = 255;
	  
	  float[] pX = new float[NUM_PARTICLES];
	  float[] pY = new float[NUM_PARTICLES];
	  float[] pZ = new float[NUM_PARTICLES];
	  float[] pZeased = new float[NUM_PARTICLES];
	  PImage img;
	  Change[] change = new Change[10];

	  //urn vars.
	  int[] urn = new int[NUM_PARTICLES]; 
	  int picked;
	  int urnOrder= 0;

	  float rot = 0;
	  boolean camTravel;
	  int timerStart = 0;
	  int[] pLocked = new int[NUM_PARTICLES];
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
	  
	  int positionImageX = 400;
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
	  int counterMax = 9;
	  int counterTemp = 0;
	  PImage deeper;
	  int deeperCouleur = 0;
	  int texti = 0;
	  int curtainDown = 0;
	  float easing = 0.01f;
	  float easingEase = 0.1f;
	  int opacity;
	  
	  public DIGITALBOY(DreamingNow p, PGraphicsOpenGL c) {

		  super(p, c);
		  physics = new VerletPhysics2D(); 
		  physics.setWorldBounds(new Rect(-100, 0, p.width+100, p.height+100)); //box definition
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

		  speeder = 2; 
		  gravityForce = 0.0f * speeder;
		  attractorForce = 0.6f * speeder;
		  attractorRadius = 2000;

		  p.shaderSelect = 0;

		  for (int i = 0; i < NUM_PARTICLES; i++) {
			 
			  pLocked[i] = 0;
			  addParticle();
			  pZ[i] = p.random(-1000, 0);
		  }

		  //REMPLIR L'ARRAY DE CORRESPONDANCES
		  for (int i = 0; i < urn.length; i++) { 
			  urn[i] = 0;
		  }

		  int urnAllocation = 0;

		  for (int i = 0; i < kinectToParticules.length; i++) {
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

		  }

		  for (int i = 0; i < pX.length; i++) {
			  pX[i] = p.width/2;
		  }

		  for (int i = 0; i < pY.length; i++) {
			  pY[i] = p.height/2;
		  }

		  //Change reset
		  for (int i = 0; i < change.length; i++) {
			  change[i].reset();
		  }
		  
		  hasRun = 0;
		  hasRun3 = 0;
		  throwAndRotate = false;
		  rotationYEased= 0f;
		  p.seqStep = 3;
		  step = 0;
		  shakeContamineX=0;
		  curtainDown = 0;

		  pause = false;
		  numbers = true;
		  pixels = false;
		  webCamPoint = false;
		  couleurPixel = true;
		  kinectMatcher = true;
		  camTravel = false;
		  wallMaker = false;
		  centerPoint = true;

		  camPosXEased = p.width/2.0f;
		  camPosYEased = p.height/2.0f;
		  camPosZEased = (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f);
		  camLookXEased = p.width/2.0f;
		  camLookYEased = p.height/2.0f;
		  camLookZEased = 0;

		  easing = 0.01f;
		  opacity = 255;
		  c.strokeCap(p.SQUARE);
		  c.hint(p.DISABLE_DEPTH_TEST);
		  p.println("DIGITALBOY");
		  
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

	  public void draw() {
		 
		  super.draw(); 
		  partition();
		  events();
		  stepTimeLine = timeLine - stepTimeLineReset;

		 
		  float dirY = p.mouseY ;
		  float dirX = p.mouseX ;		

		 
		  //physics.update();
		  c.beginDraw();
		  c.background(0);
		  c.lights();
		  c.ambientLight(255, 255, 255);
	
		  p.kinectConnected = true;
		  
		  
		  //KINECT MATCHER
		  if(kinectMatcher){
			
			  p.kinect.update();
			  PVector realWorldPoint;
			  int     index;
			  int[]   depthMap = p.kinect.depthMap();

			  PVector[] realWorldMap = p.kinect.depthMapRealWorld();

			  countea = 0;  
			  for (int y = p.kinect.depthHeight(); y > 0; y = y - 1){
				  for (int x = p.kinect.depthWidth(); x > 0;x = x - 1){

					  y = p.constrain(y, 0, 479);
					  x = p.constrain(x, 0, 639);
					  index = x + y * p.kinect.depthWidth(); 
					  if (depthMap[index] > 10 && depthMap[index] < 2600) //Conditions
					  {   

						  realWorldPoint = p.kinect.depthMapRealWorld()[index];
						  realWorldPoint = realWorldMap[index];
						  //c.stroke(rgbImage.get(x,y));
						  if(webCamPoint) c.point(realWorldPoint.x+p.width/2,(240-realWorldPoint.y)+(p.height/2-240),1100 - realWorldPoint.z);
						  VerletParticle2D particule = physics.particles.get(kinectToParticules[index]);
						  if(kinectToParticules[index] != constellation1 && kinectToParticules[index] != constellation2) {
							  particule.setPreviousPosition(new Vec2D(realWorldPoint.x+p.width/2, (240-realWorldPoint.y)+(p.height/2-240) ));
							  particule.set(new Vec2D(realWorldPoint.x+p.width/2, (240-realWorldPoint.y)+(p.height/2-240)));
							  //pZ[kinectToParticules[index]] = 0; //- realWorldPoint.z;
							  pZ[kinectToParticules[index]] = 1100 - realWorldPoint.z;
							  //p.println(rgbImage.get(x,y));
							  pLocked[kinectToParticules[index]] = 1;
							  if(countea==0){
								  imagePos.set(realWorldPoint.x+p.width/2,(240-realWorldPoint.y)+(p.height/2-240));
							  }
							  countea++;
						  }
					  }
				  }
			  }
			  
		  }

		
		  VerletParticle2D p1 = physics.particles.get(constellation1);
		  VerletParticle2D p2 = physics.particles.get(constellation2);
		
		  /////////////////////////////////////////////////////////////CAMERA/////////////////////////////////////////////////////////////////////////////////////////////
		  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		  
		  if (camTravel == true) {
			  //c.camera(p.map(p1.x,0.f,p.width,p.width-1000,p.width+1000),p.map(p1.y,0,p.height,p.height-1000,p.height+1000),p.map(pZ[1],-1000,0,-2000,1000),p2.x,p2.y,pZ[2],0,-1,0);
			  camPosX = p1.x;
			  camPosY = p1.y;
			  camPosZ = pZ[constellation1];
			  camLookX = p2.x;
			  camLookY = p2.y;
			  camLookZ = pZ[constellation2];
		  } 
		  else {
			  //c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
			  camPosX = p.width/2.0f;
			  camPosY = p.height/2.0f;
			  camPosZ = (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f);
			  camLookX = p.width/2.0f;
			  camLookY = p.height/2.0f;
			  camLookZ = 0;
		  } 
		  

		  if (p.seqStep == 5){ 
			  easing = 0.01f;
		  } else easing = 0.004f;

		  if (p.seqStep == 6){ 
			  int index = 0;
			  for (VerletParticle2D particule : physics.particles) {
				  
				  //particule.setPreviousPosition(new Vec2D(p.random(0,1000),p.random(0,1000)));
				  //particule.set(new Vec2D(p.random(0,1000),p.random(0,1000)));
				  //pZ[index] = p.random(0,1000);
				  particule.setPreviousPosition(new Vec2D(p.width/2,p.height/2));
				  particule.set(new Vec2D(p.width/2,p.height/2));
				  pZ[index] = 0;
				  index++;
			  }
		  }
		  change[6].test(p.seqStep);
		  float dx = camPosX - camPosXEased;
		  if (p.abs(dx) > 1) {
			  camPosXEased += dx * easing;
		  }
		  float dx1 = camPosY - camPosYEased;
		  if (p.abs(dx1) > 1) {
			  camPosYEased += dx1 * easing;
		  }
		  float dx2 = camPosZ - camPosZEased;
		  if (p.abs(dx2) > 1) {
			  camPosZEased += dx2 * easing;
		  }
		  float dx3 = camLookX - camLookXEased;
		  if (p.abs(dx3) > 1) {
			  camLookXEased += dx3 * easing;
		  }
		  float dx4 = camLookY - camLookYEased;
		  if (p.abs(dx4) > 1) {
			  camLookYEased += dx4 * easing;
		  }
		  float dx5 = camLookZ - camLookZEased;
		  if (p.abs(dx5) > 1) {
			  camLookZEased += dx5 * easing;
		  }
		  
		 
		  c.camera(camPosXEased, camPosYEased, camPosZEased, camLookXEased, camLookYEased, camLookZEased, 0, 1, 0);
		  //Cam @ 0 
		  //c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);

		  /////////////////////////PARTICLE RENDERER
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
			  float z;
			  if (camTravel == true) z = pZ[i];
			  else z  = pZ[i];
			  if(wallMaker) z = 0;
			  
			  if (kinectMatcher == false) easing = 0.006f;
			  else easing = 0.2f;
			  if(wallMaker) easing = 0.004f;
			  
			  
			  float dE = easing - easingEase;
			  if (p.abs(dE) > 0.01f) {
				  easingEase += dE * 0.1f;
			  }
			  
			  if (p.seqStep == 6){
				  easing = p.random(0.001f,0.07f);
			  }
			  if (p.seqStep == 3){
				  easing = p.random(0.002f,0.1f);
			  }
			  //INTERPOLATIONS  
			  if(pLocked[i] == 1) {

				  float dX = particule.x - pX[i];
				  if (p.abs(dX) > 1) {
					  pX[i] += dX * easing;
				  } 
				  if (p.abs(dX) <= 1 && wallMaker) pX[i] =  particule.x;
				  
				  float dY = particule.y - pY[i];
				  if (p.abs(dY) > 1) {
					  pY[i] += dY * easing;
				  }  
				  if (p.abs(dX) <= 1 && wallMaker)  pY[i] =  particule.y;

				  float dz = z - pZeased[i];
				  if (p.abs(dz) > 1) {
					  pZeased[i] += dz * easing;
				  }
				  
			  } else {

				  if(p.seqStep < 4) z = 800;
				  //easing = 1f;
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
				  float dz = z - pZeased[i];
				  if (p.abs(dz) > 1) {
					  pZeased[i] += dz * easing;
				  }
			  } 

			  ///RENDER ICI

			  //on screen POINT
			  //Lettre l = (Lettre) lettre.get(i);
			  counterTemp = 0;

			  if(numbers){
				  c.stroke(255,opacity);
				  c.fill(255,opacity);
				  c.text(counterTemp, pX[i]+7, pY[i]+14, pZeased[i]);
			  }
			  if(pixels){
			
				  if(timeLine < 3000) c.stroke(255,p.map(p.constrain(timeLine,0,3000), 0, 3000, 0, 255));
				  else c.stroke(255,255);  
				  c.point(pX[i], pY[i], pZeased[i]);
			  }

		  }
		
		  c.endDraw();
		  //digitalWall(1, 0);
		 
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
					
				}
				kinectMatcher = true;
				couleurPixel = false;
				numbers = false;
				pixels = true;
				wallMaker = false;
			}
			
			//Numbers
			if (p.seqStep == 4){ 
				if(change[6].test(p.seqStep)){
					
				}
				kinectMatcher = true;
				couleurPixel = false;
				numbers = true;
				pixels = false;
				wallMaker = false;
	
			}
			
			//Perspective in
			if (p.seqStep == 5){ 
				if(change[6].test(p.seqStep)){
				
				}
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
			
			}
			//p.println(p.seqStep);
			//Randomness mvt
			if (p.seqStep == 6){
				if(change[6].test(p.seqStep)){
					stepTimeLineReset = timeLine;
					stepTimeLine = 0;
				}
				
				kinectMatcher = false;
				opacity = (int) p.map(p.constrain(stepTimeLine,0,5000), 0, 5000, 255, 0);
			}

			
			if (p.seqStep == 7){ 
				pause = true;
				if(change[9].test(7)){
					constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
					constellation2 = (int) p.random(0, NUM_PARTICLES);
				}

				VerletParticle2D p1 = physics.particles.get(constellation1);

				pZ[constellation1] = 0;
				wallMaker = true;
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
				curtainDown = (int) p.map(p.constrain(timeLine, 265000, 297000),265000,297000,0, p.height); 
				contentShake(0,3,10);
				contentShake(0,3,10);
				contentShake(0,3,10);
			}
			  
	  }

	  public void partition() {
		 
		  int cue = 9020; //init avec les lettres sinon bug
		  if (timeLine > cue && timeLine < cue+500){
			  p.seqStep = 3;
		  }
		 
		  cue = 50250;  //TO NUMBERS
		  if (timeLine > cue && timeLine < cue+500){
			  p.seqStep = 4;
		  }
		  
		  //cue = 181793; //CAM IN THE BOY
		  //if (timeLine > cue && timeLine < cue+500){
		  //p.seqStep = 5;

		  //}
		 

		  cue = 89000; //CAM IN THE BOY
		  if (timeLine > cue && timeLine < cue+500){
			  p.seqStep = 6;

		  }
		  /*
		  cue = 196929; //CAM MOUVEMENT //196929
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 1){
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  hasRun3++;
		  }
		  */
		  cue = 204103; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 2){
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  hasRun3++;
		  }
		  
		  cue = 211252; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 3){
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  hasRun3++;
		  }
		  
		  cue = 218455; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 4){
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  hasRun3++;
		  }
		  
		  cue = 225691; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 5){
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  hasRun3++;
		  }
		  
		  cue = 232854; //CAM MOUVEMENT
		  if (timeLine > cue && timeLine < cue+500 && hasRun3 != 6){
			  constellation1 = (int) p.random(numPixelsHigh*numPixelsWide, NUM_PARTICLES);
			  constellation2 = (int) p.random(0, NUM_PARTICLES);
			  hasRun3++;
		  }
		  
		  cue = 240038; //STOP
		  if (timeLine > cue && timeLine < cue+500){
			  p.seqStep = 7;
		  }
		  
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
		  
	  }
	  
	  public  void mousePressed() {
		  p.println(timeLine);
	  }

	  public void mouseDragged() {
	
	  }

	  public void mouseReleased() {
		
	  }

	  public void keyPressed() {
		/*

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
		  */
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
				//p.println(rotatePoint);
				physics.behaviors.clear();
				hasRun = 1;
			}
			
			if(kinect1Clicksub == 1.0f && hasRun == 1 && throwAndRotate) {
				if(change[3].test(kinect1Clicksub)){
					rotationStartPoint = kinectOneHandX;
					
				}
				int diff;
				if(kinectOneHandX - rotationStartPoint < 15  && kinectOneHandX - rotationStartPoint > -15){
					diff = 0;
					p.println(kinectOneHandX - rotationStartPoint);
				} else diff = (int) (kinectOneHandX - rotationStartPoint);
				
				if(diff < 0) rotationY = p.map(p.constrain(diff,-100,-15),-15,-100,-.0f,-0.001f);
				else rotationY = p.map(p.constrain(diff,15,100),15,100,.0f,0.001f);
				
				p.println(diff);
				p.println(rotationY);
			}
			
			if(kinect1Clicksub == 0) {
				if(change[3].test(kinect1Clicksub)){
				//nothing
				}
				//if (rotationY > 0.01) rotationY=rotationY - 0.001f;
				//if (rotationY < 0.01) rotationY=rotationY + 0.001f;
				
			}
	  }

	  public void hands(){

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

		  /*
		  //HAND3
		  if(p.kinect2click == 1.0f) {
			  if (change[3].test(p.kinect2click)) { 
				  handPos3 = new Vec2D(p.kinect2X, p.kinect2Y);
				  handAttractor3 = new AttractionBehavior(handPos3, attractorRadius, attractorForce);
				  physics.addBehavior(handAttractor3);
			  }
			  handPos3.set(p.kinect2X, p.kinect2Y);
		  }

		  if (p.kinect2click == 0f){
			  if (change[3].test(p.kinect2click)) { 
				  physics.removeBehavior(handAttractor3);
			  }
		  }

		  //HAND4
		  if(p.kinect2click2 == 1.0f) {
			  if (change[4].test(p.kinect2click2)) { 
				  handPos4 = new Vec2D(p.kinect2X2, p.kinect2Y2);
				  handAttractor4 = new AttractionBehavior(handPos4, attractorRadius, attractorForce);
				  physics.addBehavior(handAttractor4);
			  }
			  handPos4.set(p.kinect2X2, p.kinect2Y2);
		  }

		  if (p.kinect2click2 == 0f){
			  if (change[4].test(p.kinect2click2)) { 
				  physics.removeBehavior(handAttractor4);
			  }
		  }

		  //HAND5
		  if(p.kinect3click == 1.0f) {
			  if (change[5].test(p.kinect3click)) { 
				  handPos5 = new Vec2D(p.kinect3X, p.kinect3Y);
				  handAttractor5 = new AttractionBehavior(handPos5, attractorRadius, attractorForce);
				  physics.addBehavior(handAttractor5);
			  }
			  handPos5.set(p.kinect3X, p.kinect3Y);
		  }

		  if (p.kinect3click == 0f){
			  if (change[5].test(p.kinect3click)) { 
				  physics.removeBehavior(handAttractor5);
			  }
		  }

		  //HAND6
		  if(p.kinect3click2 == 1.0f) {
			  if (change[6].test(p.kinect3click2)) { 
				  handPos6 = new Vec2D(p.kinect3X2, p.kinect3Y2);
				  handAttractor6 = new AttractionBehavior(handPos6, attractorRadius, attractorForce);
				  physics.addBehavior(handAttractor6);
			  }
			  handPos6.set(p.kinect3X2, p.kinect3Y2);
		  }

		  if (p.kinect3click2 == 0f){
			  if (change[6].test(p.kinect3click2)) { 
				  physics.removeBehavior(handAttractor6);
			  }
		  }
		   */
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
							
							l.count(0,9);
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
								l.count(0,9);
							
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
