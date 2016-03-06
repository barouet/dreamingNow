package seq;


import java.util.ArrayList;
import toxi.geom.Vec2D;
import toxi.physics2d.behaviors.*;
import pbox2d.*;
import org.jbox2d.common.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.core.PFont;
import processing.opengl.PGraphicsOpenGL;

public class EMOTICONS extends Sketch{
	
	// A reference to our box2d world
	PBox2D box2d;
	// A list we'll use to track fixed objects
	ArrayList<Emoticons> emoticons;
	ArrayList<Boundary> boundaries;
	ArrayList<Box> paddle;
	
	Vec2 emoticonAdd = new Vec2(-1, -1);
	int emoticonAddTime = 0;
	int emoticonAddTimeThresh = 300;
	int emoticonPosX = p.width/2;
	int emoticonPosY = (p.height/2)  ;
	int emoticonIdCount = 0 ;
	int emoticonBounceCount = 0;
	
	int graphWidth = p.width-100;
	int graphHeight =  p.height-100;
	
	float easing = 0.01f;
	boolean inverse = false;
	int inverseInt = 0;
	int kinect1xInvRelative;
	int kinect1x2InvRelative;

	PFont f;
	int[] emoticonsFont = { 56, 49, 58, 53, 59, 54, 65, 57, 66, 73, 67, 80, 68, 84, 69, 86, 70, 87, 72, 88, 75, 93, 85 };
	
	
	Change[] change = new Change[11];

	Vec2D mousePos;
	Vec2D handPos = new Vec2D(p.width, p.height);
	Vec2D handPos2;
	Vec2D handPos3;
	Vec2D handPos4;
	Vec2D handPos5;
	Vec2D handPos6;

	AttractionBehavior mouseAttractor;
	AttractionBehavior handAttractor;
	AttractionBehavior handAttractor2;
	AttractionBehavior handAttractor3;
	AttractionBehavior handAttractor4;

	AttractionBehavior flocking1Attractor;
	AttractionBehavior flocking2Attractor;
	AttractionBehavior flocking3Attractor;
	AttractionBehavior flocking4Attractor;
	
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

	int degagement= -100; //réduire les boundaries
	int step = 0;
	boolean reset = false;
    int	counter;

	int stepTimeLine;
	int stepTimeLineReset;
	int cue2 = 0;
	
	public EMOTICONS(DreamingNow p, PGraphicsOpenGL c) {
		
		super(p, c);
		// Initialize box2d physics and create the world
		box2d = new PBox2D(p);	
		box2d.createWorld();
		box2d.setGravity(0, 0);
		// Turn on collision listening!
		box2d.listenForCollisions();
		// Create ArrayLists
		emoticons = new ArrayList<Emoticons>();
		boundaries = new ArrayList<Boundary>();
		paddle = new ArrayList<Box>();

		// get the name of this class and print it
		className = this.getClass().getSimpleName();

		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}

		
	}

	public void reinit() {
		
		super.reinit();
		kill();
		c.textFont(p.emoticon,28);
		c.textAlign(p.CENTER, p.CENTER);
		c.smooth(2);
		//Add first emoticon
		box2d.setGravity(0, 0);
		hands();
		emoticonPosX = p.width/2;
		emoticonPosY = (p.height/2)  ;
		
		boundaries.add(new Boundary(p.width/2, p.height-5, p.width, 10));
		boundaries.add(new Boundary(p.width/2, 5, p.width, 10));
		boundaries.add(new Boundary(p.width-5, p.height/2, 10, p.height));
		boundaries.add(new Boundary(5, p.height/2, 10, p.height));
		
		p.skeletonTracking = false;
		p.wallEasing = 0.2f; //Easing setting for wall Kinect
		p.shaderSelect = 0;
		c.tint(255,255);

		liveCue = 7;  // Cue de départ dans Ableton Live
		p.seqStep =  0;
		
		for(int i=0; i<urn.length; i++){
			urn[i] = 0;	
		}
		
		urnOrder = 0;
		
	    counter = 0;
	    
	    
	    //Change reset
	    for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
	    }
	    
 
	    //Attractors and flocking stuff
	    mouseXsub = p.mouseX;
	    mouseYsub = p.mouseY;
	    kinect1Xsub = (int) p.kinect1X;
	    kinect1Ysub = (int) p.kinect1Y;
	    kinect1X2sub = (int) p.kinect1X2;
	    kinect1Y2sub = (int) p.kinect1Y2;
	    handPos.x = emoticonPosX;
	    handPos.y = emoticonPosY;
	    
	    emoticonIdCount = 0;
	    emoticonBounceCount = 0;
	    
	    c.textFont(p.emoticon,32);
		c.textAlign(p.CENTER, p.CENTER);

		//Cam reset
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		
		step = 0;//step = 7;
		stepTimeLine = 0;
		stepTimeLineReset = 0;

		p.println("EMOTICONS");
		
	}
	
	public void draw() {
		
		super.draw();
		box2d.step();

		c.fill(0);
		
		stepTimeLine = timeLine - stepTimeLineReset;
		
		c.beginDraw();
		c.background(0);
				
		hands();
		events();
		partition();
	
		if (emoticonAdd.x >= 0 && emoticonAddTime > emoticonAddTimeThresh){
			if(emoticons.size()< 400){
				int numAdd = (int) p.map(emoticonAddTimeThresh,100,10,1,4);
		    	if (numAdd == 1){
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,15));
					emoticonAddTime = 0;
		    	}
		    	else if(numAdd == 2) {
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,12));
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,12));
					emoticonAddTime = 0;
		    	}
		    	else if(numAdd == 3) {
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,12));
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,12));
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,12));
					emoticonAddTime = 0;
		    	}
		    	else if(numAdd == 4) {
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,12));
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,12));
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,12));
		    		emoticons.add(new Emoticons(emoticonAdd.x,emoticonAdd.y,12));
					emoticonAddTime = 0;
		    	}
			
			}
		}
		
		if(emoticons.size()>0){
			for (int i = emoticons.size()-1; i >= 0; i--) {
			    Emoticons e = emoticons.get(i);
			    e.display();
			}
		}
		c.endDraw();
		
		//reset emoticonAdd to -1 to avoid feedback
		emoticonAdd.x = -1;
		emoticonAddTime++;
		emoticonAddTimeThresh = (int)  p.map(p.constrain(emoticons.size(),0,30),0,30,100,10);
		
	}
	
	public float easing(float a, float b, float time){
		return super.easing(a, b, time);
	}
	
	public void events() {
		
		//
		//EMOTICON SUIT LA KINECT
		//
		if (p.seqStep == 0){
			if (change[8].test(p.seqStep)){
				
			
			}

			if(emoticons.size()>0){
				//Un seul émoticon qui suit la kinect
				//emoticonPosX = (int) easing(handPos.x, emoticonPosX, 0.1f);
				//emoticonPosY = (int) easing(handPos.y+3, emoticonPosY, 0.1f)+3;
				//Emoticon en place	
				
				emoticonPosX = p.width/2;
				emoticonPosY = (p.height/2);
				//Move the emoticon
				emoticons.get(0).setLocation(emoticonPosX,emoticonPosY);
				emoticons.get(0).setAgularVelocity(0);
				//Change de face quand click in
			}
			
			
			
			if (change[2].test(kinect1Clicksub) && kinect1Clicksub == 1) {
				
				if(timeLine > 6000 && emoticonIdCount == 0){
					//emoticonPosX = (int) handPos.x;
					//emoticonPosY = (int) handPos.y+3;
					emoticonPosX = p.width/2;
					emoticonPosY = (p.height/2);
					//emoticons.add(new Emoticons(handPos.x,handPos.y+3,12));
					emoticons.add(new Emoticons(emoticonPosX,emoticonPosY,12));
					
					emoticonIdCount++;
				}
				
				if(stepTimeLine > 1000 &&emoticonIdCount > 0){
					emoticonIdCount++;
				}
			}
			
			

						
			if(kinect1Clicksub == 1){
				stepTimeLineReset = timeLine;
				stepTimeLine = 0;
			}
			
			change[2].test(kinect1Clicksub);
			if(emoticons.size()>0){
				if (emoticonIdCount == 0)  emoticons.get(0).faceID = 10; //neutral10
				else if (emoticonIdCount == 1)  emoticons.get(0).faceID = 10; //smiley
				else if (emoticonIdCount == 2)  emoticons.get(0).faceID = 2;
				else if (emoticonIdCount == 3)  emoticons.get(0).faceID = 16;
				else if (emoticonIdCount == 4)  emoticons.get(0).faceID = 21;
				else if (emoticonIdCount == 5)  emoticons.get(0).faceID = 11;
			}
		}
	
		if (p.seqStep == 10){
			if (change[8].test(p.seqStep)){
				
			}
			//Un seul émoticon qui suit la kinect
			emoticonPosX = (int) easing(handPos.x, emoticonPosX, 0.1f);
			emoticonPosY = (int) easing(handPos.y, emoticonPosY, 0.1f);
			//Move the emoticon
			if(emoticons.size()>0){
				emoticons.get(0).setLocation(emoticonPosX,emoticonPosY);
				emoticons.get(0).setAgularVelocity(0);				
			}

			//Change de face quand click in
			
			

			if (change[2].test(kinect1Clicksub) && kinect1Clicksub == 1 && stepTimeLine > 2000) {
				emoticonIdCount++;
			}
			
			if(kinect1Clicksub == 1){
				stepTimeLineReset = timeLine;
				stepTimeLine = 0;
			}
			
			change[2].test(kinect1Clicksub);
			if (emoticonIdCount == 0)  emoticons.get(0).faceID = 10; //neutral10
			else if (emoticonIdCount == 1)  emoticons.get(0).faceID = 2; //smiley
			else if (emoticonIdCount == 2)  emoticons.get(0).faceID = 16;
			else if (emoticonIdCount == 3)  emoticons.get(0).faceID = 21;
			else if (emoticonIdCount == 4)  emoticons.get(0).faceID = 11;
		}
		
		//
		//PADDLE GAME
		//
		if (p.seqStep == 1){
			if (change[8].test(p.seqStep)) {
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(61); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.cueInterface);
				
				if(emoticons.size()>0){
					emoticons.get(0).setAgularVelocity(0);
				}
			}


			if(change[2].test(kinect1Clicksub) && kinect1Clicksub == 1){
				if(paddle.size() == 0){
					paddle.add(new Box(handPos.x, handPos.y));
				}

				if(emoticons.size()>0){
					emoticons.get(0).setAgularVelocity(3);
				}
				
				stepTimeLineReset = timeLine;
			}


			if(kinect1Clicksub == 0){
				
				if(paddle.size() == 1){
					paddle.get(0).die();
				}
			}
			
			if(paddle.size() > 0){
				paddle.get(0).setLocation(handPos.x,handPos.y);
				paddle.get(0).setAngularVelocity(p.map(p.constrain(stepTimeLine,0,3000), 0, 3000, 0, 12.1f));
			}
			if(paddle.size() > 0){
				paddle.get(0).display();
			}
		}
		
		
		//
		//GRAVITY DOWN
		//
		if (p.seqStep == 2){
			if (change[8].test(p.seqStep)) {
				OscMessage myMessage = new OscMessage("/seqConfirm");
				myMessage.add(62); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.cueInterface);	
			}
			
			for (int i = 0; i < boundaries.size(); i++) {
				Boundary e = boundaries.get(i);
				e.killBody();     	
			  }
			
			for (int i = boundaries.size()-1; i >= 0; i--) { 
				boundaries.remove(i); 
			  }
			
			if(paddle.size() > 0){
				paddle.get(0).killBody();
			}
			
			box2d.setGravity(0, -1.9f);
		}
		
		if (change[3].test(p.emoticonAdd) && p.emoticonAdd >= 1) {
			emoticons.add(new Emoticons(p.width/2,p.height/2,12));
		}

	}
	
	public void beginContact(Contact cp) {
	
		 // Get both fixtures
		  Fixture f1 = cp.getFixtureA();
		  Fixture f2 = cp.getFixtureB();
		  // Get both bodies
		  Body b1 = f1.getBody();
		  Body b2 = f2.getBody();
		  // Get our objects that reference these bodies
		  Object o1 = b1.getUserData();
		  Object o2 = b2.getUserData();

		  // If object 1 is a Box, then object 2 must be a particle
		  // Note we are ignoring particle on particle collisions
		  if (o1.getClass() == Box.class) {
		    Emoticons e = (Emoticons) o2;
		    e.change();
		 
		    if (emoticonBounceCount > 2 ){
		    	emoticonAdd = box2d.getBodyPixelCoord(b2);
		    	
		    }
		    emoticonBounceCount++;
		  } 
		  // If object 2 is a Box, then object 1 must be a particle
		  else if (o2.getClass() == Box.class) {
			Emoticons e = (Emoticons) o1;
		    e.change();

		  }
	}
	
	public void endContact(Contact cp) {
	
	}
	
	void partition(){

		if(timeLine >= 83398-36640) p.seqStep = 1;
		if(timeLine >= 205225-36640) p.seqStep = 2;
	}
		
	public void keyPressed() {

	}

	public void hands(){
		
	    //Kinect wall vars
	    kinect1Xsub = (int) p.kinect1X;
	    kinect1Ysub = (int) p.kinect1Y;
	    kinect1X2sub = (int) p.kinect1X2;
	    kinect1Y2sub = (int) p.kinect1Y2;
	    
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
			} //End of click
			handPos.set(kinectOneHandX, kinectOneHandY);
		}

		if (kinect1Clicksub == 0f){
			//////////////MOUSE RELEASED	
			if (change[1].test(kinect1Clicksub)) {
			}
		}
	}

	public void controlEvent(ControlEvent theEvent) {
	}

	public void oscEvent(OscMessage theOscMessage) {
	}

	public void kill() {
		
		for (int i = 0; i < emoticons.size(); i++) {
			Emoticons e = emoticons.get(i);
			e.killBody();     
			
		  }
		
		for (int i = emoticons.size()-1; i >= 0; i--) { 
			emoticons.remove(i); 
		  }
		
		for (int i = 0; i < paddle.size(); i++) {
			Box e = paddle.get(i);
			e.killBody();     
			
		  }
		
		for (int i = paddle.size()-1; i >= 0; i--) { 
			paddle.remove(i); 
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
	
	public class Emoticons {
		
		  // We need to keep track of a Body and a radius
		  Body body;
		  float r;
		  int faceID;
		  int col;


		  public Emoticons(float x, float y, float r_) {
		    r = r_;
		    // This function puts the particle in the Box2d world
		    makeBody(x, y, r);
		    body.setUserData(this);
		    col = p.color(175);
		    faceID = 2;
		  }

		  public void setAgularVelocity(int i) {
			  body.setAngularVelocity(i);
		}

		// This function removes the particle from the box2d world
		  void killBody() {
		    box2d.destroyBody(body);
		  }

		  // Change color when hit
		  public void change() {
			faceID = (int) p.random(0,20);
		    col = p.color(255, 0, 0);
		   
		  }
		  
		  public void setLocation(float x, float y){
			Vec2 target = box2d.coordPixelsToWorld(x,y);
			body.setTransform(target, 0);
		  }
		  
		  // Is the particle ready for deletion?
		  boolean done() {
		    // Let's find the screen position of the particle
		    Vec2 pos = box2d.getBodyPixelCoord(body);
		    // Is it off the bottom of the screen?
		    if (pos.y > p.height+r*2) {
		      killBody();
		      return true;
		    }
		    return false;
		  }


		  // 
		  void display() {
		    // We look at each body and get its screen position
		    Vec2 pos = box2d.getBodyPixelCoord(body);
		    // Get its angle of rotation
		    float a = body.getAngle();
		    c.pushMatrix();
		    c.translate(pos.x, pos.y);
		    c.rotate(a);
		    c.fill(255);
		    c.stroke(0);
		    c.strokeWeight(1);
		    //c.ellipse(0, 0, r*2, r*2);
		    //c.line(0, 0, r, 0);
		    c.text((char)(emoticonsFont[faceID]), 0,0,0);
		    // Let's add a line so we can see the rotation
		    
		    c.popMatrix();
		  }

		  // Here's our function that adds the particle to the Box2D world
		  void makeBody(float x, float y, float r) {
		    // Define a body
		    BodyDef bd = new BodyDef();
		    // Set its position
		    bd.position = box2d.coordPixelsToWorld(x, y);
		    bd.type = BodyType.DYNAMIC;
		    body = box2d.createBody(bd);

		    // Make the body's shape a circle
		    CircleShape cs = new CircleShape();
		    cs.m_radius = box2d.scalarPixelsToWorld(r);

		    FixtureDef fd = new FixtureDef();
		    fd.shape = cs;
		    // Parameters that affect physics
		    fd.density = 1;
		    fd.friction = 0.01f;
		    fd.restitution = 0.9f;

		    // Attach fixture to body
		    body.createFixture(fd);

		    body.setAngularVelocity(p.random(-10, 10));
		  }
		}

	public class Box {
	
	  // We need to keep track of a Body and a width and height
	  Body body;
	  float w;
	  float h;
	  float hDie;
	  float wAnim;
	  float hAnim;
	  boolean hasRun;
	  boolean isDying;
	  boolean dragged = false;
	
	  // Constructor
	  public Box(float x_, float y_) {
	    float x = x_;
	    float y = y_;
	    w = 12;
	    h = 150;
	    hDie = 0;
	    isDying = false; 
	    wAnim = 0;
	    hAnim = 0;
	    // Add the box to the box2d world
	    makeBody(new Vec2(x,y),w,h);
	    body.setUserData(this);
	  }
	
	  // This function removes the particle from the box2d world
	  void killBody() {
	    box2d.destroyBody(body);
	  }
	
	  boolean contains(float x, float y) {
	    Vec2 worldPoint = box2d.coordPixelsToWorld(x, y);
	    Fixture f = body.getFixtureList();
	    boolean inside = f.testPoint(worldPoint);
	    return inside;
	  }
	  
	  void setAngularVelocity(float a) {
	    body.setAngularVelocity(a); 
	  }
	  void setVelocity(Vec2 v) {
	     body.setLinearVelocity(v);
	  }
	  
	  void setLocation(float x, float y) {
		Vec2 target = box2d.coordPixelsToWorld(x,y);
		Vec2 pos = body.getWorldCenter();
	    Vec2 diff = new Vec2(target.x-pos.x,target.y-pos.y);
	    diff.mulLocal(90);
	    setVelocity(diff);
	    setAngularVelocity(0);
	    //body.setTransform(target, 0);
	  }
	
	  // Drawing the box
	  void display() {
	    // We look at each body and get its screen position
	    Vec2 pos = box2d.getBodyPixelCoord(body);
	    // Get its angle of rotation
	    float a = body.getAngle();
	
	    if(!isDying){
		    if(h != hAnim){
		    	//easing
		    	wAnim = easing(w, wAnim, 0.2f);
		    	hAnim = easing(h, hAnim, 0.09f);
		    }
		 } else {
			 if(hDie != hAnim){
				 hAnim = easing(hDie, hAnim, 0.5f);
			 }
		 }
	    c.rectMode(p.CENTER);
	    c.pushMatrix();
	    c.translate(pos.x,pos.y);
	    c.rotate(a);
	    c.fill(255);
	    c.stroke(0);
	    c.rect(0,0,w,hAnim);
	    c.popMatrix();
	  }
	  
	  void die(){
		 isDying = true;
		 if(hAnim < 1) {
			 Box e = paddle.get(0);
				e.killBody(); 
			 paddle.remove(0);
		 }
	  }
	  
	  // This function adds the rectangle to the box2d world
	  void makeBody(Vec2 center, float w_, float h_) {
	    // Define and create the body
	    BodyDef bd = new BodyDef();
	    bd.type = BodyType.KINEMATIC;
	    bd.position.set(box2d.coordPixelsToWorld(center));
	    bd.fixedRotation = false;
	    body = box2d.createBody(bd);
	
	    // Define a polygon (this is what we use for a rectangle)
	    PolygonShape ps = new PolygonShape();
	    float box2dW = box2d.scalarPixelsToWorld(w_/2);
	    float box2dH = box2d.scalarPixelsToWorld(h_/2);
	    ps.setAsBox(box2dW, box2dH);
	
	    // Define a fixture
	    FixtureDef fd = new FixtureDef();
	    fd.shape = ps;
	    // Parameters that affect physics
	    fd.density = 1;
	    fd.friction = 0.3f;
	    fd.restitution = 0.4f;
	
	    body.createFixture(fd);
	  }
	
	}

	class Boundary {
	
	  // A boundary is a simple rectangle with x,y,width,and height
	  float x;
	  float y;
	  float w;
	  float h;
	  
	  // But we also have to make a body for box2d to know about it
	  Body b;
	
	  Boundary(float x_,float y_, float w_, float h_) {
	    x = x_;
	    y = y_;
	    w = w_;
	    h = h_;
	
	    // Define the polygon
	    PolygonShape ps = new PolygonShape();
	    // Figure out the box2d coordinates
	    float box2dW = box2d.scalarPixelsToWorld(w/2);
	    float box2dH = box2d.scalarPixelsToWorld(h/2);
	    // We're just a box
	    ps.setAsBox(box2dW, box2dH);
	
	
	    // Create the body
	    BodyDef bd = new BodyDef();
	    bd.type = BodyType.STATIC;
	    bd.position.set(box2d.coordPixelsToWorld(x,y));
	    b = box2d.createBody(bd);
	    b.setUserData(this);
	    // Attached the shape to the body using a Fixture
	    b.createFixture(ps,1);
	  }
	  
	  void killBody() {
		    box2d.destroyBody(b);
		  }
	  // Draw the boundary, if it were at an angle we'd have to do something fancier
	  void display() {
	    c.fill(0);
	    c.stroke(0);
	    c.rectMode(p.CENTER);
	    c.rect(x,y,w,h);
	  }

}


}