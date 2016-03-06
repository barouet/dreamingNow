package seq;

import java.util.ArrayList;

//import dreaming.*;
import seq.FRACTALS.Change;
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

public class LETTERS extends Sketch{
	
	//LETTRES FLOTTANTES
	/*b = fade with blur
	 *c= clear blur
	 *1=add boid
	 *2,3,4,5,6= form words
	 */

	PGraphics pg;
	int NUM_PARTICLES =11;
	VerletPhysics2D physics;
	int graphWidth = p.width-100;
	int graphHeight =  p.height-100;
	float easing = 0.0001f;


	ArrayList lettres;
	ArrayList noisers;
	String[] dreamingNow;
	PFont f;
	int stepTimeLine;
	int stepTimeLineReset;

	
	int smoothIndex;
	float[] recentValues = new float[100];

	//urn vars
	int[] urn = new int[12]; //11 lettres + 1
	int[] urnBirthOrder = new int[11]; //11 lettres + 1 
	int picked;
	int urnOrder= 0;

	float SPEED = 1;
	int maxItems;
	int boidCounter=0;

	int degagement= 15; //réduire les boundaries
	int step = 0;
	boolean form = false;
	boolean reset = false;
    int	counter;
	private boolean blur = false;
	private boolean emboss = false;
	private boolean fadeOut = false;
	Change[] change = new Change[10];

	public LETTERS(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);

		
		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}
		
		physics = new VerletPhysics2D();
		//physics.setDrag(0.00f);
		physics.setWorldBounds(new Rect(0+degagement, 0+degagement, p.width-degagement*2, p.height-degagement*2));
		// the NEW way to add gravity to the simulation, using behaviors
		physics.addBehavior(new GravityBehavior(new Vec2D(0, 0.f)));

		f = p.createFont("Georgia",48,true);
		c.textFont(f);
		c.textAlign(p.CENTER);

		dreamingNow = p.loadStrings("i.txt");
		maxItems = dreamingNow.length;

		lettres = new ArrayList();
		noisers = new ArrayList();

		// get the name of this class and print it
		className = this.getClass().getSimpleName();

	}

	public void reinit() {
		
		super.reinit();
		p.skeletonTracking = false;	
		kill();
		for(int i=0; i<urn.length; i++){
			urn[i] = 0;	
		}
		urnOrder = 0;
		lettres.clear();
		noisers.clear();
	    boidCounter=0;
	    counter = 0;
	    for(int x = degagement; x < p.width-degagement; x=x+10){
		    physics.addBehavior(new AttractionBehavior(new Vec2D(x,degagement), 20, -0.04f, 0.01f));
		    physics.addBehavior(new AttractionBehavior(new Vec2D(x,p.height-degagement), 20, -0.04f, 0.01f));
	    }
	    for(int x = degagement; x < p.height-degagement; x=x+10){
		    physics.addBehavior(new AttractionBehavior(new Vec2D(degagement,x), 20, -0.04f, 0.01f));
		    physics.addBehavior(new AttractionBehavior(new Vec2D(p.width-degagement,x), 20, -0.04f, 0.01f));
	    }
	    
		c.textFont(p.font,60);
		//form = true;
		
		
		blur = false;
		emboss = false;
		p.shaderSelect = 1;
		p.blurSize = 0;
		fade = 0;
		fadeOut  = false;
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		easing = 0.01f;
		step = 0;
		form = true;
		p.seqStep = 0;
		
		dreamingNow = p.loadStrings("i.txt");
		wordUpdate();
		
		for(step = 0; step<11; step++){
			boidAdd();
		}
		change[0].reset();

		p.println("FLOATING LETTERS");
		
		
	}

	public void draw() {
		
		super.draw();
		
		
		//timeLine = p.timecodeSlave;
		stepTimeLine = timeLine - stepTimeLineReset;
		
		
		//if(p.frameCount%30 == 0) p.println(timeLine + " " + timecode);
		
		smoothIndex++;

		if(smoothIndex > 100-1){
			smoothIndex = 0;
		} 

		c.beginDraw();
		c.background(0);
		c.pushMatrix();
		
		
		//Bastard way to set speed of the letters
		if(p.frameCount%1 == 0)physics.update();
		
		//float oscil =p.sin(((p.frameCount/50%100) / 100.f ) * 3.1416f);
		
		
		if(form){
			if(reset){
				easing = 0.000006f;
			}
			counter++;
			if(counter>300 && counter<700){
				easing=easing+0.0000005f;
				reset = false;
			}
			if(counter>700&& counter<800){
				easing=easing+0.000008f;
				reset = false;
			}
			if(counter>800){
				easing=easing+0.00007f;
				reset = false;
			}
		}
		easing= p.constrain(easing,0.f,0.1f);

		
	

		
		int ordre = (int) p.map(counter, 0, 700, 0, boidCounter);
		ordre = p.constrain(ordre,0,boidCounter);
	
		for(int i = 0; i < ordre; i++){
			Boid b = (Boid) lettres.get(i);
			Noiser n = (Noiser) noisers.get(i);
			VerletParticle2D particule = physics.particles.get(i);
			Vec2D vel = particule.getVelocity();
			if( vel.x < -1. || vel.x > 1. || vel.y < -1. || vel.y > 1.){
				
				particule.scaleVelocity(0.8f);
			} else particule.scaleVelocity(1.f);
		
			if(n.toDestination() == 1){
				particule.clearForce();
				float dx = n.destination().x - particule.x;
				if(p.abs(dx) > 1) {
					particule.x += dx * easing;
				}
				float dy = n.destination().y - particule.y;
				if(p.abs(dy) > 1) {
					particule.y += dy * easing;
				}

				particule.set(new Vec2D(particule.x, particule.y));
			}
		}
		
		
		for(int i = 0; i < boidCounter; i++){
			Boid b = (Boid) lettres.get(i);
			Noiser n = (Noiser) noisers.get(i);
			VerletParticle2D particule = physics.particles.get(i);
			Vec2D vel = particule.getVelocity();
			if( vel.x < -1. || vel.x > 1. || vel.y < -1. || vel.y > 1.){
				if(p.seqStep == 0) {
					particule.scaleVelocity(0.1f);
				} else particule.scaleVelocity(0.999f);
			} else particule.scaleVelocity(1.f);

			if(boidCounter < 11){
				if(i == 0){
					particule.x = n.destination().x;
					particule.y = n.destination().y;
				}
			}

			
			b.arrive(new PVector(particule.x,particule.y));
			b.run();
			//c.ellipse(particule.x,particule.y,2,2);
		}
		
		
		
		c.popMatrix();
		c.fill(0,fade);
		c.rect(-10,-10,p.width+20,p.height+20);
		c.endDraw();
		
		if(step!=boidCounter){
			boidAdd();
		}
		

		
	
		partition();


		if(blur && (p.frameCount%(int)(p.map(p.blurSize,0,100,2,4))) == 0f) p.blurSize = p.blurSize+1f;
		p.blurSize = p.constrain(p.blurSize,0f,100f);
		if(fadeOut)fade = 255;
		
		
	}
	
	void partition(){
		/*
		int cue = 6400;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 0){
				boidAdd();
			}
			dreamingNow = p.loadStrings("i.txt");
			wordUpdate();
			step=1;
		}

		cue = 23000;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 1){
				boidAdd();
			}
			step=2;
		}
		cue = 35800;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 2){
				boidAdd();
			}
			step=3;
		}
		cue = 49800;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 3){
				boidAdd();
			}
			step=4;
		}
		cue = 50800;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 4){
				boidAdd();
			}
			step=5;
		}
		cue = 51300;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 5){
				boidAdd();
			}
			step=6;
		}
		cue = 66000;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 6){
				boidAdd();
			}
			step=7;
		}
		cue = 82000;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 7){
				boidAdd();
			}
			step=8;
		}
		cue = 92500;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 8){
				boidAdd();
			}
			step=9;
		}
		cue = 99000;
		if(timeLine > cue && timeLine < cue+100){
			if(step == 9){
				boidAdd();
			}
			step=10;
		}
		*/
		
		if(p.seqStep == 1){
			
			if(change[0].test(p.seqStep)){
				stepTimeLineReset = timeLine;
				OscMessage myMessage = new OscMessage("/cueNum");
				myMessage.add(3); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.soundComputer);
			    myMessage = new OscMessage("/cueGo");
				myMessage.add(1); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.soundComputer);
			}

			/*
			int cue = 12000;
			if(stepTimeLine > cue && stepTimeLine < cue+100){
				dreamingNow = p.loadStrings("free.txt");
				wordUpdate();
				form = false;
				reset = true;
				counter = 0;
				step=11;
			}
			*/
			//TOWARD DREAMING NOW
			int cue = 12000;
			if(stepTimeLine > cue && stepTimeLine < cue+100){

				if(step == 11){
					form = true;
					reset = true;
					counter = 0;
					dreamingNow = p.loadStrings("dreamingNow.txt");
					wordUpdate();
				}
				step=12;
			}

			cue = 30755;
			if(stepTimeLine > cue && stepTimeLine < cue+100){
				if(step == 12){
					dreamingNow = p.loadStrings("free.txt");
					wordUpdate();
					form = true;
					reset = true;
					blur = true;
					fadeOut = true;
					counter = 0;

				}
				step=13;
			}
		}
	}

	void boidAdd() {
	
		if(boidCounter < 11){
			int newLetter = 0;
			int urnAllocation = 0;
			for(int i=0; i<urn.length; i++){
				urnAllocation += urn[i];
			}

			if(urnAllocation<=10){
				newLetter = urn();
				//boidAdd(newLetter);
				urnBirthOrder[boidCounter] = newLetter-1;
				boidCounter++;
			}

			String[] pieces = p.split(dreamingNow[newLetter-1], '\t');
			//println(pieces);
			if(pieces.length == 3){
				
				int birthX = (int) p.random(0+20, p.width-20);
				int birthY = (int) p.random(0+20, p.height-20);
				if(boidCounter == 1){
					birthX = 485;
					birthY = 250;
				}
				
				
				lettres.add(new Boid(new PVector(birthX,birthY),p.random(1.5f,1.9f),p.random(0.9f,2.f),pieces[0]));
				noisers.add(new Noiser(Integer.parseInt(pieces[1]),Integer.parseInt(pieces[2])));
				
				VerletParticle2D particule = new VerletParticle2D(Vec2D.randomVector().scale(0).addSelf(birthX, birthY));
				physics.addParticle(particule);
				// add a negative attraction force field around the new particle
				physics.addBehavior(new AttractionBehavior(particule, 200, -0.01f, p.random(0.9f,0.9f)));
				
			
				  

					

			}
		}
		wordUpdate();
	}
	
	void wordUpdate() {

		for (int i = 0; i < boidCounter; i++) {
			int index = urnBirthOrder[i];

			String[] pieces = p.split(dreamingNow[index], '\t');
			if(pieces.length == 3){
				Noiser n = (Noiser) noisers.get(i);
				n.newDestination(Integer.parseInt(pieces[1]),Integer.parseInt(pieces[2]));
			}
		}

	}
	
	int urn(){

		int urnCheck = (int) p.random(1, urn.length);
		int urnAllocation = 0;
		for(int i=0; i<urn.length; i++){
			urnAllocation += urn[i];
		}

		if(urnAllocation==0){ //always starts with letter 6 (i)
			urn[6] = 1;
			picked = 6;
			return picked;
		}
		
		if(urnAllocation==1){ //always starts with letter 6 (i)
			urn[1] = 1;
			picked = 1;
			return picked;
		}

		if(urnAllocation <= 10 && urnAllocation > 1){
			if(urn[urnCheck]==0){
				urnOrder++;
				urn[urnCheck] = 1;
				picked = urnCheck;
				//println(urn);
			} else {
				urn();
			}
		} else {
			picked = 0;
		}

		return picked;
	}

	public void keyPressed() {

		if (p.key == '0'){
			p.println("BOID ADD"+ timecode + " "+ timecode);
			boidAdd();

		}
		if (p.key == '1'){
			p.println("1"+ timecode + " "+ timecode);
			form = true;
			reset = true;
			counter = 0;
			dreamingNow = p.loadStrings("i.txt");
			wordUpdate();

		}

		if (p.key == '2'){
			p.println("2"+ timecode + " "+ timecode);
			form = true;
			reset = true;
			counter = 0;
			//dreamingNow = p.loadStrings("wandering.txt");
			wordUpdate();
		}

		if (p.key == '3'){
			p.println("3"+ timecode + " "+ timecode);
			form = true;
			reset = true;
			counter = 0;
			dreamingNow = p.loadStrings("danger.txt");
			wordUpdate();
		}

		if (p.key == '4'){
			p.println("4"+ timecode + " "+ timecode);
			form = true;
			reset = true;
			counter = 0;
			dreamingNow = p.loadStrings("ignore.txt");
			wordUpdate();
		}
		if (p.key == '5'){
			p.println("5"+ timecode + " "+ timecode);
			form = true;
			reset = true;
			counter = 0;
			dreamingNow = p.loadStrings("amWondering.txt");
			wordUpdate();
		}
		if (p.key == '6'){
			p.println("6"+ timecode + " "+ timecode);
			form = true;
			reset = true;
			counter = 0;
			dreamingNow = p.loadStrings("dreamingNow.txt");
			wordUpdate();
		}
		
		if (p.key == '7'){
			p.println("7"+ timecode + " "+ timecode);
			form = false;
			reset = true;
			counter = 0;
			dreamingNow = p.loadStrings("free.txt");
			wordUpdate();
		}
		if (p.key == 'b'){

			blur  = true;
		}

		if (p.key == 'c'){
			blur  = false;
			p.blurSize=0;
		}
		
		if (p.key == 'f'){
			fadeOut  = true;
		}
		
		if (p.key == 'g'){
			fade = 0;
			fadeOut  = false;
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

	class Boid {

		PVector loc;
		PVector vel;
		PVector acc;
		float r;
		float d; //Distance with destination
		float maxforce;    // Maximum steering force
		float maxspeed;    // Maximum speed
		String letter;
		int moyenne = 200;
		float[] recentValues = new float[moyenne];
		int currentIndex = 0;
		int num = 20;
		float mx[] = new float[num];
		float my[] = new float[num];
		float mz[] = new float[num];
		int age = 0;


		Boid(PVector l, float ms, float mf,String t) {
			acc = new PVector(0,0);
			vel = new PVector(0,0);
			loc = l.get();
			//println(loc);
			r = 3.0f;
			maxspeed = ms;
			maxforce = mf;
			letter = t;
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

			float theta = vel.heading2D() + p.radians(90);

			// Smooth out theta values (quite unuseful)
			//recentValues[currentIndex] = theta;


			int delta = 1; //écart angle
			float d1 = p.abs((d / p.sqrt(p.pow(graphWidth/2,2)+p.pow(graphHeight/2,2)))-1);
			theta = p.constrain(theta, -delta + (delta*d1), delta - (delta*d1));
			
			recentValues[currentIndex] = theta;
			float thetaSmooth = 0;
			for(int i=0;i<moyenne;i++){
				thetaSmooth = thetaSmooth + recentValues[i];
			}
			thetaSmooth = thetaSmooth/moyenne;
			
			
			c.pushMatrix();
			c.translate(loc.x,loc.y);
			c.rotate(thetaSmooth);
			//pg.textFont(f);
			c.fill(255, p.constrain((int)(age),0,255));
			//pg.textAlign(CENTER);
			c.text(letter,0,0);
			c.popMatrix();

			currentIndex++;
			if(currentIndex > moyenne-1){
				currentIndex = 0;
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
}