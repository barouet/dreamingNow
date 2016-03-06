 package seq;

//import dreaming.*;
import java.util.ArrayList;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
//import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;
import toxi.geom.Vec2D;
import toxi.geom.Rect;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import generativedesign.*;

public class FRACTALS extends Sketch{
	
	int stepTimeLine;
	int stepTimeLineReset;
	
	int stepTimeLine2;
	int stepTimeLineReset2;
	
	Path path;
	
	float curlx = -180;
	float curly = -180;
	float curlxEased = -180;
	float curlyEased = -180;
	
	float curlLegx = 0;
	float curlLegy = 0;
	float curlx2 = -180;
	float curly2 = -180;
	float curlxEased2 = -180;
	float curlyEased2 = -180;
	
	float curlx3 = 0;
	float curlx3Eased = 0;
	float curlx3v = 0;
	float curly3 = 0;
	float curly3Eased = 0;
	float curly3v = 0;
	float f = (float) (p.sqrt(2)/2.);
	float deley = 10;
	float growth = 0;
	float growthTarget = 0;
	float growthTargetEased = 0;
	float growthTargetv = 0;
	float growthTargetLeg = 0;
	float growthLeg = 0;
	float deleyLeg = 10;
	float deley2 = 10;
	float growth2 = 0;
	float growthTarget2 = 0;
	float deley3 = 10;
	float growth3 = 0;
	float growthTarget3 = 0;
	float growthTarget3Eased = 0;
	float growthTarget3v = 0;
	float tree3UngrowEased = 0;
	float tree3Ungrowv = 0;
	
	int pousse1 = 0;
	int pousse2 = 0;
	int pousse3 = 0;	
			
	Change[] change = new Change[10];
	float lengthMult = 0f;
	boolean lengthGrow = true;
	float lengthMult2 = 0f;
	boolean lengthGrow2 = true;
	float lengthMult3 = 0f;
	boolean lengthGrow3 = true;
	int previousNumMax = 0;
	float shoulderRightAngleEased;
	float shoulderLeftAngleEased;
	float torsoHeadDiffEased;
	float legRightAngleEased;
	float legLeftAngleEased;
	float torsoRightFootDiffEased;
	float torsoLeftFootDiffEased;
	float handsXDiffEased;
	float handsYDiffEased;
	float headYEased;
	float kinectOneHandDiffTree1Eased;
	float kinectOneHandDiffTree2Eased;
	
	float legRightAngleEasedLop;
	float legRightAngleEasedLopPrevious = 0;
	float filtre = 0.05f;
	
	//Deux mains = 1;
	int kinect1Xsub;
	int kinect1Ysub;
	int kinect1X2sub;
	int kinect1Y2sub;
	int kinect1Clicksub;
	int kinectOneHandX;
	int kinectOneHandY;
	int previouskinectOneHandY;
	//Deux mains = 1 Raw 
	int kinect1XsubRaw;
	int kinect1YsubRaw;
	int kinect1X2subRaw;
	int kinect1Y2subRaw;
	int kinect1ClicksubRaw;
	int kinectOneHandXRaw;
	int kinectOneHandYRaw;
	int previouskinectOneHandYRaw;


	boolean tree1BirthGrow = false;
	boolean tree2BirthGrow = false;
	boolean tree3BirthGrow = false;
	int tree1Birth = -100;
	int tree2Birth = -100;
	int tree3Birth = -100;
	int tree1Height =  p.height+20;
	int tree2Height =  p.height+20;
	int tree3Height=  p.height+20;
	boolean reduxMode = false;
	boolean tree1bodyGrow = false;
	boolean tree1died = false;
	int tree1Ungrow = 0;
	int tree2Ungrow = 0 ;
	int tree3Ungrow = 0;
	boolean tree1Ungrowing = false;
	
	
	int liveCue =24;
	int step = 0;
	int hasRun = 0;
	int hasRun2 = 0;
	int hasRun3 = 0;

	VerletPhysics2D physics;
	AttractionBehavior handAttractor;
	Vec2D handPos;
	ArrayList seve;
	public boolean debug = true;
	
	//Second tree vars
	// an array for the nodes
	Node[] nodes = new Node[100];
	// an array for the springs
	Spring[] springs = new Spring[0];
	// dragged node
	Node selectedNode = null;
	float nodeDiameter = 16;
	int numNodes = 0;
	
	
	public FRACTALS(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		liveCue = 24;
		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}
		seve = new ArrayList();
		physics = new VerletPhysics2D(); 
		physics.setWorldBounds(new Rect(-100, 0, p.width+100, p.height+2)); //box definition
		physics.addBehavior(new GravityBehavior(new Vec2D(0, 0.15f)));
		
		// get the name of this class and print it
		className = this.getClass().getSimpleName();

	}

	public void reinit() {
		
		super.reinit();
		p.kinectConnected = true;
		c.smooth(2);
		p.shaderSelect = 0;
		p.wallEasing = 0.18f;
		p.seqStep = 0;
		
		for (int i = 0; i < 1000; i++) {
			 seve.add(new Vehicle(new PVector(0, p.height), p.random(50,100), p.random(3.9f,10.3f)));
		}
	
		step = 0;
		hasRun = 0;
		hasRun2 = 0;
		hasRun3 = 0;
		
		for (int i = 0; i < change.length; i++) {
			change[i].reset();
		}
		
		
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);

		kinect1X2sub = (int) p.kinect1X;
		kinect1Ysub = (int) p.kinect1Y;
		kinect1X2sub = (int) p.kinect1X2;
		kinect1Y2sub = (int) p.kinect1Y2;

		for (int i = 0; i < 3; i++) {
			addParticle();

		}

		tree1Birth = -10;
		tree2Birth = -10;
		tree3Birth = -10;
		tree1Height = 550;
		tree2Height = 550;
		tree3Height = 550;
		growth =0f;
		growth2 = 0f;
		growth3 =0f;	
		tree1Ungrow = 0;
		tree2Ungrow = 0 ;
		tree3Ungrow = 0;
		reduxMode = false;
		tree1died = false;
		tree1bodyGrow = false;
		handsXDiffEased = 0f;
		tree1Ungrowing = false;

		curlx = -180;
		curly = -180;
		curlxEased = -180;
		curlyEased = -180;
		
		curlx2 = -180;
		curly2 = -180;
		curlxEased2 = -180;
		curlyEased2 = -180;

		curlLegx = 0;
		curlLegy = 0;
		curlx3 = 0;
		curly3 = 0;
		liveCue = 10;
		//initNodesAndSprings();
		p.println("FRACTALS");

	}
	
	public void addParticle() {

		VerletParticle2D particule = new VerletParticle2D(Vec2D.randomVector().scale(2).addSelf((int)(p.random(0, p.width)), p.height));
		physics.addParticle(particule);
	
	}
	
	public void draw() {
		
		//p.println(p.torso);
		super.draw(); 

		stepTimeLine = timeLine - stepTimeLineReset;
		stepTimeLine2 = timeLine - stepTimeLineReset2;

		//p.println(minutes+":"+seconds);
		handsToOneHand();
		physics.update();
		c.beginDraw();
		c.background(0);
		c.fill(0);
		c.noStroke();
		c.rect(0,0,p.width,p.height);
		c.strokeWeight(2);
		
	
		
		partition();
	
		c.endDraw();

	}

	void secondTreeRender(){
	
		  // let all nodes repel each other
		  for (int i = numNodes ; i < nodes.length; i++) {
		    nodes[i].attract(nodes);
		  } 
		  // apply spring forces
		  for (int i = numNodes ; i < springs.length; i++) {
		    springs[i].update();
		  } 
		  // apply velocity vector and update position
		  for (int i = numNodes; i < nodes.length; i++) {
		    nodes[i].update();
		  } 
		  nodes[nodes.length-1].x = p.width/2;
		  nodes[nodes.length-1].y = p.height/2;
		    
		  // draw nodes
		  c.stroke(255);
		  c.strokeWeight(1);
		  for (int i = numNodes ; i < springs.length; i++) {
			  c.line(springs[i].fromNode.x - p.width/2, springs[i].fromNode.y - p.height/2, springs[i].toNode.x- p.width/2, springs[i].toNode.y - p.height/2);
			  //c.bezier(springs[i].fromNode.x - p.width/2, springs[i].fromNode.y - p.height/2, springs[i].fromNode.x - p.width/2 + p.random(-10,10), springs[i].fromNode.y - p.height/2 + p.random(-10,10), springs[i].toNode.x- p.width/2+ p.random(-10,10), springs[i].toNode.y - p.height/2+ p.random(-10,10), springs[i].toNode.x- p.width/2, springs[i].toNode.y - p.height/2);
		  
		  }
		  // draw nodes
		  c.noStroke();
		  for (int i = numNodes ; i < nodes.length; i++) {
			c.fill (255);
		    c.ellipse(nodes[i].x - p.width/2 , nodes[i].y - p.height/2, nodeDiameter, nodeDiameter);
		    c.fill(255);
		    c.ellipse(nodes[i].x - p.width/2 , nodes[i].y -  p.height/2, nodeDiameter-4, nodeDiameter-4);
		    c.rectMode(p.CENTER);
		    c.rect(nodes[i].x - p.width/2 , nodes[i].y -  p.height/2, 5, 5);
		  }
		  
	}
	
	void initNodesAndSprings() {
		  // init nodes
		  float rad = nodeDiameter/2;
		  for (int i = 0; i < nodes.length; i++) {
		    nodes[i] = new Node(p.width/2+p.random(-2, 2), p.height/2+p.random(-2, 2));
		    nodes[i].setBoundary(rad, rad, p.width-rad, p.height-rad);
		    nodes[i].setRadius(100);
		    nodes[i].setStrength(-5);
		  } 

		  // set springs randomly
		  springs = new Spring[0];

		  for (int j = 0 ; j < nodes.length-1; j++) {
		    int rCount = p.floor(p.random(1, 2));
		    for (int i = 0; i < rCount; i++) {
		      int r = p.floor(p.random(j+1, nodes.length));
		      Spring newSpring = new Spring(nodes[j], nodes[r]);
		      newSpring.setLength(5);
		      newSpring.setStiffness(2);
		      springs = (Spring[]) p.append(springs, newSpring);
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
	
	public void handsToOneHand() {
		//Algo qui rassemble les deux mains en une.
		kinect1Xsub = (int) p.kinect1X;
		kinect1Ysub = (int) p.kinect1Y;
		kinect1X2sub = (int) p.kinect1X2;
		kinect1Y2sub = (int) p.kinect1Y2;

		

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
		
		kinect1Clicksub = p.constrain(p.kinect1click+p.kinect1click2,0,1);
		if(kinect1Clicksub == 1){
			
		//p.println("x " + kinectOneHandX);
		//p.println("y " + kinectOneHandY);
		}
		
		//Algo qui rassemble les deux mains en une.
				kinect1XsubRaw = (int) p.kinect1Xraw;
				kinect1YsubRaw = (int) p.kinect1Yraw;
				kinect1X2subRaw = (int) p.kinect1X2raw;
				kinect1Y2subRaw = (int) p.kinect1Y2raw;

				

				if(p.kinect1click2 == 0f){

					kinect1X2subRaw = 0;
					kinect1Y2subRaw = 0;
				}


				if(p.kinect1click == 0f){
					kinect1XsubRaw = 0;
					kinect1YsubRaw = 0;
				}

			
				kinectOneHandXRaw = p.constrain(kinect1XsubRaw + kinect1X2subRaw,0,p.width);
				kinectOneHandYRaw = p.constrain(kinect1YsubRaw + kinect1Y2subRaw,0,p.height);

				if(p.kinect1click == 1 && p.kinect1click2 == 1) {
					kinectOneHandXRaw = kinect1XsubRaw;
					kinectOneHandYRaw = kinect1YsubRaw;
				}
				
				kinect1ClicksubRaw = p.constrain(p.kinect1click+p.kinect1click2,0,1);
				if(kinect1ClicksubRaw == 1){
				//p.println("x " + kinectOneHandXRaw);
				//p.println("y " + kinectOneHandYRaw);
				}
	}
	
	public void partition() {
		/*
		PVector distRHandRKnee = PVector.sub( p.rightHand, p.leftKnee);
		PVector distLHandRKnee = PVector.sub( p.leftKnee, p.leftHand);
		PVector distRHandLHand = PVector.sub( p.rightHand, p.leftHand);

		//Get Angle shoulder on rightside
		PVector rightElbow2D = new PVector(p.rightElbow.x, p.rightElbow.y);
		PVector rightShoulder2D = new PVector(p.rightShoulder.x, p.rightShoulder.y);
		PVector rightHip2D = new PVector(p.rightHip.x, p.rightHip.y);
		PVector torsoOrientation = PVector.sub(rightShoulder2D,rightHip2D);

		float shoulderRightAngle = angleOf(rightElbow2D,rightShoulder2D,torsoOrientation);
		float dr = shoulderRightAngle - shoulderRightAngleEased;
		if(p.abs(dr) > 1) {
			shoulderRightAngleEased += dr * 0.1;
		}
		shoulderRightAngleEased = p.constrain(shoulderRightAngleEased,20,160);
		float shoulderRightAngleEasedMapped = p.map(shoulderRightAngleEased,20,160,0,180);
		
		//Get angle on left side
		PVector leftElbow2D = new PVector(p.leftElbow.x, p.leftElbow.y);
		PVector leftShoulder2D = new PVector(p.leftShoulder.x, p.leftShoulder.y);
		PVector leftHip2D = new PVector(p.leftHip.x, p.leftHip.y);
		PVector torsoLeftOrientation = PVector.sub(leftShoulder2D,leftHip2D);

		float shoulderLeftAngle = angleOf(leftElbow2D,leftShoulder2D,torsoLeftOrientation);
		float dl = shoulderLeftAngle - shoulderLeftAngleEased;
		if(p.abs(dl) > 1) {
			shoulderLeftAngleEased += dl * 0.1f;
		}
		shoulderLeftAngleEased = p.constrain(shoulderLeftAngleEased,20,160);
		float shoulderLeftAngleEasedMapped = p.map(shoulderLeftAngleEased,20,160,0,180);
	
		//Get Angle LEG on rightside
		PVector rightKnee2D = new PVector(p.rightKnee.x, p.rightKnee.y);
		PVector torso2D = new PVector(p.torso.x, p.torso.y);
		PVector inBetweenRight = PVector.sub(rightHip2D,leftHip2D);
		float legRightAngle = angleOf(rightHip2D,rightKnee2D,inBetweenRight);

		float dlla2 = legRightAngle - legRightAngleEased;
		if(p.abs(dlla2) > 1) {
			legRightAngleEased += dlla2 * 0.06f;
		}
		
		legRightAngleEased = p.constrain(legRightAngleEased,0,90);
		
		filtre = 0.5f;
		legRightAngleEasedLop = legRightAngleEasedLopPrevious + (filtre * (legRightAngleEased - legRightAngleEasedLopPrevious));
		legRightAngleEasedLopPrevious = legRightAngleEasedLop;
		
		
		float legRightAngleEasedMapped;
		if(legRightAngleEasedLop < 60) legRightAngleEasedMapped = p.map(legRightAngleEasedLop,60,0,0,180);
		else legRightAngleEasedMapped = 0;
	
		
		//Get Angle LEG on LEFT
		PVector leftKnee2D = new PVector(p.leftKnee.x, p.leftKnee.y);
		PVector inBetweenLeft = PVector.sub(leftHip2D,rightHip2D);
		float legLeftAngle = angleOf(leftHip2D,leftKnee2D,inBetweenLeft);

		float dlla = legLeftAngle - legLeftAngleEased;
		if(p.abs(dlla) > 1) {
			legLeftAngleEased += dlla * 0.06f;
		}
		legLeftAngleEased = p.constrain(legLeftAngleEased,0,90);
		float legLeftAngleEasedMapped;
		if(legLeftAngleEased < 60) legLeftAngleEasedMapped = p.map(legLeftAngleEased,60,0,0,180);
		else legLeftAngleEasedMapped = 0;
	
		
		c.fill(255);
		PVector diff = PVector.sub(p.head, p.torso);
		float dt = diff.x - torsoHeadDiffEased;
		if(p.abs(dt) > 1) {
			torsoHeadDiffEased += dt * 0.05f;
		}
		
		PVector handsDiff = PVector.sub(p.rightHand, p.leftHand);
		float hd = handsDiff.x - handsXDiffEased;
		if(p.abs(hd) > 1) {
			handsXDiffEased += hd * 0.05f;
		}
		
		PVector handsYDiff = PVector.sub(p.rightHand, p.leftHand);
		hd = handsDiff.y - handsYDiffEased;
		if(p.abs(hd) > 1) {
			handsYDiffEased += hd * 0.05f;
		}
		
		dlla = p.head.y - headYEased;
		if(p.abs(dlla) > 1) {
			headYEased += dlla * 0.06f;
		}
		
		*/
		
		//Get Angle between, tree1 and hand		
		PVector tree1BirthVector = new PVector(tree1Birth, p.height);
		PVector tree1HeightVector = new PVector(tree1Birth, tree1Height);
		PVector kinectOneHand = new PVector(kinectOneHandX, kinectOneHandY);
		PVector kinectOneHandDiff = PVector.sub(kinectOneHand,tree1HeightVector);
		float kinectOneHandDiffTree1 = angleOf(tree1BirthVector,tree1HeightVector,kinectOneHandDiff);
		if(kinectOneHandX < tree1Birth) kinectOneHandDiffTree1 = kinectOneHandDiffTree1 * -1f;  //Entre -180 et 180;
		//c.text(kinectOneHandDiffTree1,100,10); 
		float hd = kinectOneHandDiffTree1 - kinectOneHandDiffTree1Eased;
		if(p.abs(hd) > 1) {
			kinectOneHandDiffTree1Eased += hd * 0.15f;
		}
		
		
		//Get Angle between, tree1 and hand		
		PVector tree2BirthVector = new PVector(tree2Birth, p.height);
		PVector tree2HeightVector = new PVector(tree2Birth, tree2Height);
		PVector kinectOneHandDiff2 = PVector.sub(kinectOneHand,tree2HeightVector);
		float kinectOneHandDiffTree2 = angleOf(tree1BirthVector,tree1HeightVector,kinectOneHandDiff2);
		if(kinectOneHandX < tree2Birth) kinectOneHandDiffTree2 = kinectOneHandDiffTree2 * -1f;  //Entre -180 et 180;
		//c.text(kinectOneHandDiffTree2,100,10); 
		hd = kinectOneHandDiffTree2 - kinectOneHandDiffTree2Eased;
		if(p.abs(hd) > 1) {
			kinectOneHandDiffTree2Eased += hd * 0.15f;
		}

		

		//Jeu au mur: place sa main, arbre nait, il peut retirer sa main moins que 2 sec et continuer à faire pousser l'arbre, sinon on passe au suivant.
		if(kinect1Clicksub == 1.0f) {
			////////MOUSE PRESSED
			
			if (change[1].test(kinect1Clicksub)) {
				
				previouskinectOneHandY = kinectOneHandYRaw;
				
				if(step == 10){ //Ouverture premier arbre waits for a click
					step++;
					if(hasRun3 != 1) {
						stepTimeLine2 = 0;
						stepTimeLineReset2 = timeLine;
					}
					hasRun3 = 1;
				}

				if(step == 20){ //Ouverture deuxieme arbre waits for a click
					
					if(hasRun3 != 200) {
						
						initNodesAndSprings();
						numNodes = 0;
						stepTimeLine = 0;
						stepTimeLineReset = timeLine;
						step++;
					}
					hasRun3 = 200;
				}
				
				if(step == 22){ //Ouverture deuxieme arbre waits for a click
					
					if(hasRun3 != 20) {
						
						initNodesAndSprings();
						numNodes = 0;
						stepTimeLine = 0;
						stepTimeLineReset = timeLine;
						step++;
					}
					hasRun3 = 20;
				}
				
				if(step == 12){ //TRANSLATION MORTELLE DU PREMIER ARBRE
					if (hasRun!=120) {
						stepTimeLine2 = 0;
						stepTimeLineReset2 = timeLine;
					}
					step++;
					hasRun = 120;
					}
				
				if(step == 4){
					if (hasRun!=3) {  //Si on refait le step, il ne changera pas le X de l'arbre
						tree3Birth = kinectOneHandXRaw;//UTILISER LE RAW SINON EASING FAUSSE
						step++;
					}
					tree3BirthGrow = true;
					hasRun = 3;
				}

				if(step == 2){
					if (hasRun!=2){
						tree2Birth = kinectOneHandXRaw;//UTILISER LE RAW SINON EASING FAUSSE
						step++;
					}
					tree2BirthGrow = true;
					hasRun = 2;
				}

				if(step == 0){
					//p.print(kinectOneHandX);
					if(hasRun != 1){
						tree1Birth = kinectOneHandXRaw; //UTILISER LE RAW SINON EASING FAUSSE
						step++;
						OscMessage myMessage = new OscMessage("/cueNum");
						myMessage.add(liveCue); /* add an int to the osc message */
						p.oscP5.send(myMessage, p.soundComputer);
						myMessage = new OscMessage("/cueGo");
						myMessage.add(1); /* add an int to the osc message */
						p.oscP5.send(myMessage, p.soundComputer);
						liveCue++;
					}
					tree1BirthGrow = true;
					hasRun = 1;
				}


			}
			////MOUSE DRAGGED
			if(step == 1 && tree1BirthGrow){
				float dx = kinectOneHandY - tree1Height;
				if(p.abs(dx) > 1) {
					tree1Height += dx * 0.1f;
				}
			}
			if(step == 3 && tree1BirthGrow){
				float dx = kinectOneHandY - tree2Height;
				if(p.abs(dx) > 1) {
					tree2Height += dx * 0.1f;
				}
			}
			if(step == 5 && tree1BirthGrow){
				float dx = kinectOneHandY - tree3Height;
				if(p.abs(dx) > 1) {
					tree3Height += dx * 0.1f;
				}
			}

			//OUVERTURE DU PREMIER ARBRE
			if(step >= 11 && step <13){
				growthTarget = p.map(kinectOneHandDiffTree1Eased,-180, 180,-10,-8);
				curlx = p.map(p.constrain(kinectOneHandDiffTree1Eased,-180, 180),-180,180,-180,180);
				curly = p.map(p.constrain(kinectOneHandDiffTree1Eased,-180, 180),-180,180,-180,180);
		
			}
			

		}

		if (kinect1Clicksub == 0f){
			//////////////MOUSE RELEASED	
			if (change[1].test(kinect1Clicksub)) {
				if(p.seqStep <= 2){
					stepTimeLine = 0;
					stepTimeLineReset = timeLine;	
				}
			
			}
			
			
			
			if(p.seqStep == 1 && stepTimeLine > 2000 && step == 11){
				//vers la mort du premier arbre
				if(hasRun2 != 4) {
					step++;
				}
				hasRun2 = 4;
			}
			
			if(step == 5 && stepTimeLine > 2000){
				if(hasRun2 != 3) {
					step=10;
					p.seqStep = 1;
				}
				hasRun2 = 3;
			}
			if(step == 3 && stepTimeLine > 2000){
				if(hasRun2 != 2) step++;
				hasRun2 = 2;
			}
			if(step == 1 && stepTimeLine > 2000){
				if(hasRun2 != 1) step++;
				hasRun2 = 1;
			}
			
			
		}
		
		//EXPLOSION 1
		if(p.seqStep == 2 && stepTimeLine < 8000 && step == 21){
			c.pushMatrix();
			c.translate(tree2Birth,tree2Height+tree2Ungrow);
			c.scale(0.3f);
			secondTreeRender();
			if (stepTimeLine >= 2000){	
				numNodes = (int) p.map(stepTimeLine, 2000, 8000, 0, nodes.length);
			}
			c.popMatrix();
		}
		
		//IMPLOSION 1
		if(p.seqStep == 2 && stepTimeLine > 8000 && step == 21){
			if(hasRun2 != 5) {
				step++;
			}
			hasRun2 = 5;
		}
		
		//EXPLOSION 2
		if(p.seqStep == 2 && stepTimeLine <= 8000 && step == 23){
			c.pushMatrix();
			c.translate(tree2Birth,tree2Height);
			c.scale(0.3f);
			secondTreeRender();
			if (stepTimeLine >= 2000){	
				numNodes = (int) p.map(stepTimeLine, 2000, 8000, 0, nodes.length);	
			}
			if (stepTimeLine >= 5000){	
				tree2Ungrow = (int) p.map(stepTimeLine,5000,8000,0,p.height - tree2Height);
			}	
			c.popMatrix();
		}
		
		//IMPLOSION 2
		if(p.seqStep == 2 && stepTimeLine > 8000 && step == 23){
			tree2Ungrow = p.height - tree2Height;
			if(hasRun2 != 50) {
				step++;
			}
			hasRun2 = 50;
		}
		
		if(step == 24){ //MORT DU PREMIER ARBRE + CALL NEXT SEQ
			p.seqStep = 3;
			step = 30; 
		}
		
		//TIMING CHUTE DU PREMIER ARBRE
		if(step == 13){
			growthTarget = p.map(kinectOneHandDiffTree1Eased,-180, 180,-10,-8);
			if(stepTimeLine2 < 5000){
			curlx = p.map(p.constrain(kinectOneHandDiffTree1Eased,-180, 180),-180,180,(int) p.map(stepTimeLine2,0,5000,-180,0),(int) p.map(stepTimeLine2,0,5000,180,0));
			curly = p.map(p.constrain(kinectOneHandDiffTree1Eased,-180, 180),-180,180,(int) p.map(stepTimeLine2,0,5000,-180,0),(int) p.map(stepTimeLine2,0,5000,180,0));
			}
			tree1Ungrow = (int) p.map(stepTimeLine2,0,10000,0,tree1Height+100); 
			if(stepTimeLine2 > 10000){
				step=20;
				p.seqStep = 2;
			}
		}
		
		c.fill(255,255,255,255);
		c.stroke(255,255,255,255);

		if(p.seqStep == 1) {
			//PREMIER ARBRE QUI S'OUVRE
			pousse1 = 10;


			float dy = curly - curlyEased;
			if(p.abs(dy) > 1) {
				curlyEased += dy * 0.1f;
			}
			
			
			float dy2 = curlx - curlxEased;
			if(p.abs(dy2) > 1) {
				curlxEased += dy2 * 0.1f;
			}

			
			c.textSize(12);
		
			c.pushMatrix();
			c.translate(tree1Birth,tree1Height+tree1Ungrow);
			//path.addPoint((int)c.modelX(0,0,0),(int)c.modelY(0,0,0));
			branch((float) (p.height/12),pousse1,pousse1);
			growth += (growthTarget/10-growth+1.)/deley;
			//path.addPoint((int)c.modelX(0,0,0),(int)c.modelY(0,0,0));
			c.popMatrix();
		}
		


		//c.image(p.kinect.depthImage(), 0, 0);
		if(p.seqStep == 2) {
			
			pousse2 = 10;

			float dy = curly2 - curlyEased2;
			if(p.abs(dy) > 1) {
				curlyEased2 += dy * 0.1f;
			}
			
			
			float dy2 = curlx2 - curlxEased2;
			if(p.abs(dy2) > 1) {
				curlxEased2 += dy2 * 0.1f;
			}
			
		}
		
		if(p.seqStep == 3) {
			//Rien ici, standby pour le cue de l'arbre kinect
		}
		
		if(p.seqStep == 4) {
			
				if(hasRun3 != 3) {
					stepTimeLine = 0;
					stepTimeLineReset = timeLine;
					OscMessage myMessage = new OscMessage("/seqConfirm");
					myMessage.add(84); /* add an int to the osc message */
					p.oscP5.send(myMessage, p.cueInterface);
					curly3 = -3;
					curlx3 = 3;
					curly3Eased = -3;
					curlx3Eased = 3;
					growthTarget3 = -2.3f;
				}
				hasRun3 = 3;

				if(p.frameCount%100 == 0 ) pousse3++;
				pousse3 = p.constrain(pousse1, 0, 10);

				if(stepTimeLine > 1000) {
					//growthTargetLeg = p.constrain(p.map(p.abs(legRightAngleEasedMapped),0,180,-10,-6),-10,-6);
					growthTargetLeg = 0;

					//curlx3 = p.map(p.mouseX/ (float)p.width, 0.f, 1.f, 3.13f, 0f);
					//curly3 = p.map(p.mouseY/ (float) p.height, 0.f, 1.f, -3.13f, 0f);
					//growthTarget3 = p.map(p.frameCount % 100, 0.f, 100.f, -5.13f, 5f);
					
					
					curly3 = p.constrain(p.map(p.kinect2Y2,385,175, -3.13f, 0f),-3.13f,p.map(p.constrain(stepTimeLine,1000,4000), 1000, 4000, -3.13f, 0.f));
					curlx3 = p.constrain(p.map(p.kinect2Y * p.map(p.constrain(stepTimeLine,1000,4000), 1000, 4000, 0.f, 1.f),385,175, 2.4f, 0f),p.map(p.constrain(stepTimeLine,1000,4000), 1000, 4000, 2.4f, 0.f),2.4f);
					growthTarget3 = p.map(p.abs(p.kinect2X2-p.kinect2X),0,260, -5, 5);
				
					
					
					
					
					float k = 0.04f;
					  float d = 0.9f;
					  float accel = k * (curly3 - curly3Eased);
					  curly3v = d * (curly3v + accel);       
					  curly3Eased = curly3Eased + curly3v;       
					

					  
					  accel = k * (curlx3 - curlx3Eased);
					  curlx3v = d * (curlx3v + accel);       
					  curlx3Eased = curlx3Eased + curlx3v;     
					  
					  accel = k * (growthTarget3 - growthTarget3Eased);
					  growthTarget3v = d * (growthTarget3v + accel);       
					  growthTarget3Eased = growthTarget3Eased + growthTarget3v; 
					  
					 
					/*
					  // CERCLE BLANC
					  float k = 0.04;
					  float d = 0.9;
					  float accel = k * (targetX - x3);
					  v3 = d * (v3 + accel);       
					  x3 = x3 + v3;       
					 
					  fill(255);
					  stroke(0);
					  ellipse(x3,height/4*3,40,40);
					 */
					
					
					
					//curlLegx = p.radians(p.abs((p.map(p.constrain(stepTimeLine,1000,5000), 1000, 5000, 0.f, 1.f)*legRightAngleEasedMapped)-180));
					//curlLegy = p.radians(-p.abs((p.map(p.constrain(stepTimeLine,1000,5000), 1000, 5000, 0.f, 1.f)*legLeftAngleEasedMapped)-180));
					//p.println(p.kinect2Y+p.kinect2Y2);
					if(stepTimeLine > 10000) {
						//tree3Ungrow = (int) p.map(p.constrain(headYEased,0,300),0,300,p.map(p.constrain(stepTimeLine,10000,25000), 10000, 25000, 0, tree3Height+20),0);
						if(p.kinect2Y2 + p.kinect2Y  > (p.height+200)){
						tree3Ungrow = (int) p.map(p.constrain(p.kinect2Y2 + p.kinect2Y,761,990),761,900,0,tree3Height+20);
						}else tree3Ungrow = 0;
					}
					
					
					
					  accel = k * (tree3Ungrow - tree3UngrowEased);
					  tree3Ungrowv = d * (tree3Ungrowv + accel);       
					  tree3UngrowEased = tree3UngrowEased + tree3Ungrowv; 
					
					
					if (stepTimeLine > 25000 && tree3UngrowEased > tree3Height) p.seqStep = 4;

					c.textSize(12);
					//c.text(shoulderRightAngleEasedMapped,30,120);
					//c.text(tree3Ungrow,50,100);

					c.pushMatrix();
					c.translate(tree3Birth,tree3Height+tree3UngrowEased);
					//path.addPoint((int)c.modelX(0,0,0),(int)c.modelY(0,0,0));
					branch3((float) (p.height/p.map(p.abs(p.kinect2X2-p.kinect2X),0,200, 12, 4)),15,15);
					growth3 += (growthTarget3Eased/10-growth3+1.)/deley3;
					//growthLeg += (growthTargetLeg/10-growthLeg+1.)/deleyLeg;
					
					//path.addPoint((int)c.modelX(0,0,0),(int)c.modelY(0,0,0));
					c.popMatrix();

					c.pushMatrix();
					c.translate(tree3Birth,tree3Height+100+tree3UngrowEased);
					//branchLeg((float) (p.height/10.f),7,7);
					c.popMatrix();
				}
			}
		
		
		// LES TROIS TRONC
		c.stroke(255);
		c.strokeWeight(2);
		c.stroke(255);
		c.pushMatrix();
		
		c.translate(tree1Birth,tree1Height+tree1Ungrow);
		c.line(0,0,0,p.height*10);
		c.popMatrix();
		
		if (step <= 23){
			c.pushMatrix();
			c.translate(tree2Birth,0);
			c.line(0,p.height - tree2Ungrow,0,tree2Height);
			if (step >= 21){
				c.ellipse(0,tree2Height + 2,3,3);
				c.ellipse(0,p.height - tree2Ungrow + 3,3,3);
			}
		
			c.popMatrix();
		}
		
		c.pushMatrix();
		c.translate(tree3Birth,tree3Height+tree3UngrowEased);
		c.line(0,0,0,p.height*10);
		c.popMatrix();
	}
		
	float angleOf(PVector one, PVector two, PVector axis) {
		PVector limb = PVector.sub(two, one);
		return p.degrees(PVector.angleBetween(limb, axis));
	}
		
	void branch(float len,int num,int numMax) {
		len *= f;
		num -= 1;
	   
		
		if(change[0].test(numMax)){
			lengthMult = 0;
			lengthGrow = true;
			
		} 

		if(lengthGrow) { //Si l'arbre grandit
			lengthMult = lengthMult + 0.005f;
			if(lengthMult > 1) {
				lengthGrow = false;
				lengthMult = 1;
			}
		}
		
		
		if((len > 0) && (num > 0)) {
			c.stroke(255);
			c.pushMatrix();
			c.rotate(p.radians(curlxEased));
			if(num == 1) {
				//c.stroke((int)p.random(0,255),(int)p.random(0,255),(int)p.random(0,255));
				c.stroke(255);
				c.fill(255);
				//c.point(0,-len);
				//c.point(0,0);
				
				//path.addPoint((int)c.modelX(0,0,0),(int)c.modelY(0,0,0));
				c.line(0,0,0,-len); 
				//p.println("a");
				//utiliser un change sur num pour déclanger l'algo de réduc/aug de taille
			}
			else {
				//c.stroke((int)p.random(0,255),(int)p.random(0,255),(int)p.random(0,255));
				//c.point(0,-len);
				//c.point(0,0);
				c.stroke(255);
				//path.addPoint((int)c.modelX(0,0,0),(int)c.modelY(0,0,0));
				c.line(0,0,0,-len);
				
			}
			c.translate(0,-len,0);
			branch(len,num,numMax);
			c.popMatrix();
			len *= growth;
			
			//c.stroke(255,252,0);
			
			c.pushMatrix();
			c.rotate(p.radians(curlyEased));
			
			if(num == 1) { 
				//c.stroke(0,255,0);
				c.line(0,0,0,-len);
				c.stroke(255,255,255,100);
				c.point(0,-len);
				c.point(0,0);
				//path.addPoint((int)c.modelX(0,0,0),(int)c.modelY(0,0,0));
				//p.println("a");
				//utiliser un change sur num pour déclanger l'algo de réduc/aug de taille
			}
			else {
				c.stroke(255,255,255);
				//c.stroke((int)p.random(0,255),(int)p.random(0,255),(int)p.random(0,255));
				c.line(0,0,0,-len);
				c.stroke(255,255,255,100);
				c.point(0,-len);
				c.point(0,0);
				//path.addPoint((int)c.modelX(0,0,0),(int)c.modelY(0,0,0));
			}
			c.translate(0,-len,0);
			branch(len,num,numMax);
			c.popMatrix();
		}
		
	} 
	
	void branchLeg(float len,int num,int numMax) {
		len *= f;
		num -= 1;
		
		
		if(change[0].test(numMax)){
			lengthMult = 0;
			lengthGrow = true;
			
		} 

		if(lengthGrow) { //Si l'arbre grandit
			lengthMult = lengthMult + 0.005f;
			if(lengthMult > 1) {
				lengthGrow = false;
				lengthMult = 1;
			}
		}
		
		
		if((len > 0) && (num > 0)) {
			c.stroke(255);
			c.pushMatrix();
			c.rotate(curlLegx);
			if(num == 1) { 
				c.fill(255);
				c.line(0,0,0,-len); 
				//p.println("a");
				//utiliser un change sur num pour déclanger l'algo de réduc/aug de taille
			}
			else {
				
				c.line(0,0,0,-len);
				
			}
			c.translate(0,-len,0);
			branchLeg(len,num,numMax);
			c.popMatrix();
			len *= growthLeg;
			
			//c.stroke(255,252,0);
			
			c.pushMatrix();
			c.rotate(curlLegy);
			
			if(num == 1) { 
				//c.stroke(0,255,20);
				c.line(0,0,0,-len); 
				//p.println("a");
				//utiliser un change sur num pour déclanger l'algo de réduc/aug de taille
			}
			else {
				c.stroke(255,255,255);
				c.line(0,0,0,-len);
				
			}
			c.translate(0,-len,0);
			branchLeg(len,num,numMax);
			c.popMatrix();
		}
		
	} 
	
	void branch2(float len,int num,int numMax) {
		len *= f;
		num -= 1;
		
		if(change[8].test(numMax)){
			lengthMult2 = 0;
			lengthGrow2 = true;
			
		} 

		if(lengthGrow2) { //Si l'arbre grandit
			lengthMult2 = lengthMult2 + 0.005f;
			if(lengthMult2 > 1) {
				lengthGrow2 = false;
				lengthMult2 = 1;
			}
		}
		
		
		if((len > 0) && (num > 0)) {
			//c.stroke(255,0,0);
			c.pushMatrix();
			c.rotate(p.radians(curlxEased2));
			if(num == 1) { 
				
				c.line(0,0,0,-len); 
				//p.println("a");
				//utiliser un change sur num pour déclanger l'algo de réduc/aug de taille
			}
			else {
				
				c.line(0,0,0,-len * 1f);
				
			}
			c.translate(0,-len,0);
			branch2(len,num,numMax);
			c.popMatrix();
			len *= growth2;
			
			//c.stroke(255,252,0);
			
			c.pushMatrix();
			c.rotate(p.radians(curlxEased2));
			
			if(num == 1) { 
				
				c.line(0,0,0,-len); 
				//p.println("a");
				//utiliser un change sur num pour déclanger l'algo de réduc/aug de taille
			}
			else {
				
				c.line(0,0,0,-len);
				
			}
			c.translate(0,-len,0);
			branch2(len,num,numMax);
			c.popMatrix();
		}
		
	} 
	
	void branch3(float len,int num,int numMax) {
		len *= f;
		num -= 1;
		
		if(change[9].test(numMax)){
			lengthMult3 = 0;
			lengthGrow3 = true;
			
		} 

		if(lengthGrow3) { //Si l'arbre grandit
			lengthMult3 = lengthMult3 + 0.005f;
			if(lengthMult3 > 1) {
				lengthGrow3 = false;
				lengthMult3 = 1;
			}
		}
		
		
		if((len > 0) && (num > 0)) {
			//c.stroke(255,0,0);
			c.pushMatrix();
			c.rotate(curlx3Eased);
			if(num == 1) { 
				c.line(0,0,0,-len); 
				//p.println("a");
				//utiliser un change sur num pour déclanger l'algo de réduc/aug de taille
			}
			else {
				c.line(0,0,0,-len * 1f);
				
			}
			c.translate(0,-len,0);
			branch3(len,num,numMax);
			c.popMatrix();
			len *= growth3;
			
			//c.stroke(255,252,0);
			
			c.pushMatrix();
			c.rotate(curly3Eased);
			
			if(num == 1) { 
				c.line(0,0,0,-len); 
				//p.println("a");
				//utiliser un change sur num pour déclanger l'algo de réduc/aug de taille
			}
			else {
				c.line(0,0,0,-len * 1f);
				
			}
			c.translate(0,-len,0);
			branch3(len,num,numMax);
			c.popMatrix();
		}
		
	} 

	class Path {

		// A Path is an arraylist of points (PVector objects)
		ArrayList<PVector> points;
		// A path has a radius, i.e how far is it ok for the boid to wander off
		float radius;

		Path() {
			// Arbitrary radius of 20
			radius = 20;
			points = new ArrayList<PVector>();
		}

		// Add a point to the path
		void addPoint(float x, float y) {
			PVector point = new PVector(x, y);
			points.add(point);
		}

		// Draw the path
		void display() {
			// Draw thick line for radius
			c.stroke(175);
			c.strokeWeight(radius*2);
			c.noFill();
			c.beginShape();
			for (PVector v : points) {
				c.vertex(v.x, v.y);
			}
			c.endShape();
			// Draw thin line for center of path
			c.stroke(0);
			c.strokeWeight(1);
			c.noFill();
			c.beginShape();
			for (PVector v : points) {
				c.vertex(v.x, v.y);
			}
			c.endShape();
		}
	}

	class Vehicle {

	  // All the usual stuff
	  PVector location;
	  PVector velocity;
	  PVector acceleration;
	  float r;
	  float maxforce;    // Maximum steering force
	  float maxspeed;    // Maximum speed
	  PVector normal;

	    // Constructor initialize all values
	  Vehicle( PVector l, float ms, float mf) {
	    location = l.get();
	    r = 4.0f;
	    maxspeed = ms;
	    maxforce = mf;
	    acceleration = new PVector(0, 0);
	    velocity = new PVector(maxspeed, 0);
	  }

	  // Main "run" function
	  public void run() {
	    update();
	    borders();
	    render();
	  }


	  // This function implements Craig Reynolds' path following algorithm
	  // http://www.red3d.com/cwr/steer/PathFollow.html
	  void follow(Path p) {

	    // Predict location 25 (arbitrary choice) frames ahead
	    PVector predict = velocity.get();
	    predict.normalize();
	    predict.mult(25);
	    PVector predictLoc = PVector.add(location, predict);

	    // Now we must find the normal to the path from the predicted location
	    // We look at the normal for each line segment and pick out the closest one

	    normal = null;
	    PVector target = null;
	    float worldRecord = 1000000;  // Start with a very high record distance that can easily be beaten

	    // Loop through all points of the path
	    for (int i = 0; i < p.points.size()-1; i++) {

	      // Look at a line segment
	      PVector a = p.points.get(i);
	      PVector b = p.points.get(i+1);

	      // Get the normal point to that line
	      PVector normalPoint = getNormalPoint(predictLoc, a, b);
	      // This only works because we know our path goes from left to right
	      // We could have a more sophisticated test to tell if the point is in the line segment or not
	      if (normalPoint.x < a.x || normalPoint.x > b.x) {
	        // This is something of a hacky solution, but if it's not within the line segment
	        // consider the normal to just be the end of the line segment (point b)
	        normalPoint = b.get();
	      }

	      // How far away are we from the path?
	      float distance = PVector.dist(predictLoc, normalPoint);
	      // Did we beat the record and find the closest line segment?
	      if (distance < worldRecord) {
	        worldRecord = distance;
	        // If so the target we want to steer towards is the normal
	        normal = normalPoint;

	        // Look at the direction of the line segment so we can seek a little bit ahead of the normal
	        PVector dir = PVector.sub(b, a);
	        dir.normalize();
	        // This is an oversimplification
	        // Should be based on distance to path & velocity
	        dir.mult(10);
	        target = normalPoint.get();
	        target.add(dir);
	      }
	    }

	    // Only if the distance is greater than the path's radius do we bother to steer
	    if (worldRecord > p.radius) {
	      seek(target);
	    }


	  
	
	  }


	  // A function to get the normal point from a point (p) to a line segment (a-b)
	  // This function could be optimized to make fewer new Vector objects
	  PVector getNormalPoint(PVector p, PVector a, PVector b) {
	    // Vector from a to p
	    PVector ap = PVector.sub(p, a);
	    // Vector from a to b
	    PVector ab = PVector.sub(b, a);
	    ab.normalize(); // Normalize the line
	    // Project vector "diff" onto line by using the dot product
	    ab.mult(ap.dot(ab));
	    PVector normalPoint = PVector.add(a, ab);
	    return normalPoint;
	  }


	  // Method to update location
	  void update() {
	    // Update velocity
	    velocity.add(acceleration);
	    // Limit speed
	    velocity.limit(maxspeed);
	    location.add(velocity);
	    // Reset accelertion to 0 each cycle
	    acceleration.mult(0);
	  }

	  void applyForce(PVector force) {
	    // We could add mass here if we want A = F / M
	    acceleration.add(force);
	  }


	  // A method that calculates and applies a steering force towards a target
	  // STEER = DESIRED MINUS VELOCITY
	  void seek(PVector target) {
	    PVector desired = PVector.sub(target, location);  // A vector pointing from the location to the target

	    // If the magnitude of desired equals 0, skip out of here
	    // (We could optimize this to check if x and y are 0 to avoid mag() square root
	    if (desired.mag() == 0) return;

	    // Normalize desired and scale to maximum speed
	    desired.normalize();
	    desired.mult(maxspeed);
	    // Steering = Desired minus Velocity
	    PVector steer = PVector.sub(desired, velocity);
	    steer.limit(maxforce);  // Limit to maximum steering force

	      applyForce(steer);
	  }

	  void render() {
	    // Draw a triangle rotated in the direction of velocity
	    float theta = velocity.heading2D() + p.radians(90);
	    c.fill(175,p.map(location.y,500,0,0,255));
	    c.stroke(175,p.map(location.y,500,0,120,255));
	    c.pushMatrix();
	   // c.translate(normal.x, normal.y);
	    c.rotate(theta);
	   // c.beginShape(PConstants.TRIANGLES);
	    //c.vertex(0, -r*1);
	    //c.vertex(-r, r*1);
	    //c.vertex(r, r*1);
	    //ellipse(normal.x, normal.y, 4, 4);
	   c.point(0,0);
	    //c.endShape();
	    c.popMatrix();
	    
	 
	        c.stroke(255);
	        c.fill(255);
	        c.point(normal.x, normal.y);
	       
	      
	  }

	  // Wraparound
	  void borders() {
	    if (location.x < -r) location.x = p.width+r;
	    //if (location.y < -r) location.y = height+r;
	    if (location.x > p.width+r) location.x = -r;
	    //if (location.y > height+r) location.y = -r;
	  }
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