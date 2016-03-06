package dreaming;
import processing.core.*;
import processing.data.XML;
import processing.opengl.*;
import oscP5.*;
import netP5.*;
import SimpleOpenNI.*;
import toxi.geom.*;
import org.jbox2d.dynamics.contacts.Contact;
import seq.BLACK;
import seq.ENTER;
import seq.DIGITALBOY;
import seq.DIGITALWALL1;
import seq.DIGITALWALL3;
import seq.DIGITALWALL2;
import seq.EMOTICONS;
import seq.ENTER2;
import seq.FACE;
import seq.FRACTALS;
import seq.PIXELS;
import seq.CALIBRATRE;
import seq.METABALLS;
import seq.Sketch;
import seq.KINECTRGBCALIB;
import seq.KINECTDEPTHCALIB;
import seq.FUTURE;
import seq.INTRO;
import seq.GRIDCALIB;
import seq.COLORBARCALIB;
import seq.END;

public class DreamingNow extends PApplet {
	
	//OSC VARIABLES
	public OscP5 oscP5;
	public NetAddress soundComputer;
	public NetAddress soundComputerClips;
	public NetAddress cueInterface;
	public NetAddress kinectWallComputer;
	public NetAddress kinectWallComputer2;
	
	//KINECT VARIABLES
	public SimpleOpenNI kinect;
	//KINECT CONNECTED?
	public boolean kinectConnected = true;
	//SKELTON?
	public boolean autocalib = false;
	public boolean skeletonFound = false;
	public boolean skeletonTracking = false;
	//RGB?
	public boolean kinectRGB = false;
	//public SyphonServer server;

	//Shared kinect var. from OSC
	public float wallEasing = 0.2f;
	int hasRunHand1 = 0;
	int hasRunHand2 = 0;
	int hasRunHand1W2 = 0;
	int hasRunHand2W2 = 0;
	public float kinect1X = 0;
	public float kinect1Y = 0;
	public float kinect1Xraw = 0;
	public float kinect1Yraw = 0;
	public int kinect1click = 0;
	public float kinect1X2 = 0;
	public float kinect1Y2 = 0;
	public float kinect1X2raw = 0;
	public float kinect1Y2raw = 0;
	public int kinect1click2 = 0;
	
	public float kinect2X = 0;
	public float kinect2Y = 0;
	public float kinect2Xraw = 0;
	public float kinect2Yraw = 0;
	public int kinect2click = 0;
	public float kinect2X2 = 0;
	public float kinect2Y2 = 0;
	public float kinect2X2raw = 0;
	public float kinect2Y2raw = 0;
	public int kinect2click2 = 0;
	
	public float kinect3X = 0;
	public float kinect3Y = 0;
	public float kinect3Xraw = 0;
	public float kinect3Yraw = 0;
	public int kinect3click = 0;
	public float kinect3X2 = 0;
	public float kinect3Y2 = 0;
	public float kinect3X2raw = 0;
	public float kinect3Y2raw = 0;
	public int kinect3click2 = 0;
	
	public PVector knob1 = new PVector();
	public PVector knob2 = new PVector();
	public PVector knob3 = new PVector();
	public PVector knob4 = new PVector();
	public PVector knob5 = new PVector();
	public PVector knob6 = new PVector();
	public PVector knob7 = new PVector();
	public PVector knob8 = new PVector();
	
	public int currentSketch = 0;
	
	//For frame control
	// camera view target
	public Vec3D target=new Vec3D();
	// camera offset
	public Vec3D eyeMod=new Vec3D(0,0,200);
	
	public Sketch sketch[];
	public Sketch currSketch;
	public int numSketch;
	public int switchToSketch=0;
	public int timecodeSlave;
	
	Change[] change = new Change[100];
	
	int down = 0;
	
	//public Movie mov;
	
	public int seqStep = 0;
	public int emoticonAdd = 0;
	public float gravityForce;
	
	public PFont font;
	public PFont emoticon;
	public PFont wallFont;
	private PGraphicsOpenGL canvas;

	public PShader blur;
	public float blurSize = 0;
	
	public PShader metaball;
	public PGraphics src;
	public PGraphics pass1, pass2, metaballs;

	//public PShader glossy;
	public PShape pixel;
	public int shaderSelect;
	
	public XML xml;
	String outputScreenPositionX;
	String outputScreenPositionY;
	
	boolean recording;

	public void init() {
		/// to make a frame not displayable, you can
		// use frame.removeNotify()
		frame.removeNotify();
		frame.setUndecorated(true);

		// addNotify, here i am not sure if you have  
		// to add notify again.  
		frame.addNotify();
		super.init();
	}

	public void setup() {
		
		size(1024, 512, P3D);
		
		
		xml = loadXML("DN_Setup.xml");
		String incomingPort = xml.getChildren("listeningPort")[0].getContent();
		String soundComputerIP = xml.getChildren("soundComputerIP")[0].getContent();
		String soundComputerPort = xml.getChildren("soundComputerPort")[0].getContent();	
		String cueComputerIP = xml.getChildren("cueInterfaceIP")[0].getContent();
		String cueComputerPort = xml.getChildren("cueInterfacePort")[0].getContent();
		String kinectWallComputerIP = xml.getChildren("kinectWallIP")[0].getContent();
		String kinectWallComputerPort = xml.getChildren("kinectWallPort")[0].getContent();
		outputScreenPositionX = xml.getChildren("outputScreenPositionX")[0].getContent();
		outputScreenPositionY = xml.getChildren("outputScreenPositionY")[0].getContent();
		
		
		
		oscP5 = new OscP5(this,parseInt(incomingPort));
		soundComputer = new NetAddress(soundComputerIP,parseInt(soundComputerPort));
		cueInterface = new NetAddress(cueComputerIP, parseInt(cueComputerPort));
		soundComputerClips = new NetAddress(soundComputerIP, parseInt(7333));
		kinectWallComputer = new NetAddress(kinectWallComputerIP, parseInt(kinectWallComputerPort));
		kinectWallComputer2 = new NetAddress(kinectWallComputerIP, parseInt(kinectWallComputerPort)+1);
		
		//SHADERS STUFF
		blur = loadShader("shaders/blur.glsl");
		blur.set("blurSize", 10);
		blur.set("sigma", 8.0f);

		pass1 = createGraphics(width, height, P2D);
		pass1.noSmooth();
		pass1.shader(blur);

		pass2 = createGraphics(width, height, P2D);
		pass2.noSmooth();
		pass2.shader(blur);
		
		metaball = loadShader("shaders/ball3.glsl");
		metaball.set("resolution", (float)(width), (float)(height));
		metaballs = createGraphics(width, height, P2D);
		metaballs.noSmooth();

		// P3D CONTEXT + SYPHON
		canvas = (PGraphicsOpenGL) createGraphics(width, height, P3D);
		//syphon = new SyphonServer(this, "Processing Syphon");
		//canvas.hint(ENABLE_NATIVE_FONTS);
		
		//Font loading
		font = createFont("fonts/AppleSDGothicNeo-Bold.otf",80);
		emoticon = createFont("fonts/emoticons.ttf",28);
		wallFont = loadFont("fonts/PixelSquare-Bold-12.vlw");
		canvas.textFont(font, 8);
		
		//Smooth
		canvas.smooth(4);
		
		// KINECT FLOOR INIT SKEL AND DEPTH
		if (kinectConnected){
			
			kinect = new SimpleOpenNI(this, SimpleOpenNI.RUN_MODE_MULTI_THREADED);
			
			//kinect.setMirror(false);
			kinect.enableDepth();
			kinect.enableRGB();
			kinect.alternativeViewPointDepthToImage();
			
			if(kinect.enableDepth() == false)
			{
				println("Can't open the depthMap, maybe the camera is not connected!"); 
				exit();
				return;
			}
		}
		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}	
		
		pixel = createShape(ELLIPSE, 0, 0,1,1);
		recording = false;
		noCursor();
		initSketches();
		frame.setLocation(parseInt(outputScreenPositionX),parseInt(outputScreenPositionY)); //SHOW SECONDARY monitor
	}

	public void draw() {
		
		kinectWallEasing();
		/*
		if(frameCount%60 == 0 ){
			//OscMessage myMessage = new OscMessage("/ping");
			//myMessage.add(0); 
			//oscP5.send(myMessage, cueInterface);
			
			println((int) frameRate);
		}
		*/
	
		if(currSketch!=null) { // calling current sketch
			currSketch.draw();
			//drawDebug();
		}

		if(switchToSketch>-1) {
			switchSketch(switchToSketch);
			switchToSketch=-1;
		}
		
		imageMode(CORNER);

		/*	
		 // Applying the blur shader along the vertical direction   
		 blur.set("horizontalPass", 0);
		 pass1.beginDraw();                
		 pass1.image(canvas, 0, 0);
		 pass1.endDraw();
		 // Applying the blur shader along the horizontal direction      
		 blur.set("horizontalPass", 1);
		 pass2.beginDraw();                
		 pass2.image(pass1, 0, 0);
		 pass2.endDraw();
		*/
	
		switch(shaderSelect){
		case 0:
			image(canvas, 0, 0);
			//syphon.sendImage(canvas);
			break;
		case 1:
			tint(255,255);
			image(canvas,0,0);
			// Applying the blur shadr along the vertical direction  
			//blur.set("verticalPass", 5);
			//pass1.beginDraw();                
			//pass1.image(canvas, 0, 0);
			//pass1.endDraw();
			// Applying the blur shader along the horizontal direction      
			//blur.set("horizontalPass", 5);
			//pass2.beginDraw();                
			//pass2.image(pass1, 0, 0);
			//pass2.endDraw();
			tint(255,200);
			//image(pass2, 0, 0);
			//syphon.sendImage(pass2);
			
			break;
		case 2:
			image(pass2, 0, 0);
			//syphon.sendImage(pass2);
			break;

		case 3:
			
			metaballs.beginDraw();
			metaballs.fill(0);
			metaballs.shader(metaball);
			metaballs.rect(0,0,width,height);
			metaballs.endDraw();
			tint(255,255);
			image(metaballs, 0, 0);
			//syphon.sendImage(metaballs);
			break;

		case 4:
			image(canvas, 0, 0);
			break;

		}
	
		if(recording){
			saveFrame("output/frames####.png");
		}
		if(recording) println("Recording Frames!");
		//if (frameCount%20==0) println(frameRate);
	}

	////////////////////////// SKETCHES INITIALIZATION
	public void initSketches() {
		sketch = new Sketch[100];
		// addSketch(new SeqWallTester(this));
		addSketch(new BLACK(this,canvas));
		addSketch(new BLACK(this,canvas));
		addSketch(new ENTER(this, canvas));
		addSketch(new ENTER2(this, canvas));
		addSketch(new FACE(this, canvas));
		addSketch(new EMOTICONS(this, canvas));
		addSketch(new DIGITALWALL2(this, canvas));
		addSketch(new FRACTALS(this, canvas));
		addSketch(new DIGITALWALL1(this, canvas));
		addSketch(new METABALLS(this, canvas));
		addSketch(new DIGITALWALL3(this, canvas));
		addSketch(new PIXELS(this,canvas));
		addSketch(new DIGITALBOY(this,canvas));
		addSketch(new CALIBRATRE(this, canvas));
		addSketch(new KINECTRGBCALIB(this, canvas));
		addSketch(new KINECTDEPTHCALIB(this, canvas));
		addSketch(new FUTURE(this,canvas));
		addSketch(new INTRO(this,canvas));
		addSketch(new GRIDCALIB(this,canvas));
		addSketch(new COLORBARCALIB(this,canvas));
		addSketch(new END(this,canvas));
		// set currSketch to an instance of Sketch
		currSketch = new Sketch(this,canvas);
		println(numSketch + " sketches added.");
	}

	public void addSketch(Sketch theSketch) {
		sketch[numSketch] = theSketch;
		numSketch++;
	}

	public void switchSketch(int sketchID) {
		if (sketch[sketchID] == null){
			return;
		}
		currSketch = sketch[sketchID];
		currSketch.reinit();
		//Feedingback the message to the interface to confirm the seqChange
		println(sketchID);
		OscMessage myMessage = new OscMessage("/seqConfirm");
		myMessage.add(sketchID); 
		oscP5.send(myMessage, cueInterface);

	}

	//////////////////////EVENT HANDLERS
	public float easing(float a, float b, float time){
		//easing
		float dx = a - b;
		if(abs(dx) > 1) {
			b += dx * time;
		}
		return b;
	}
	
	public void keyPressed() {
		//Don't allow escape key to quit
		if (key == ESC) key = 0;
		
		if (keyEvent.isControlDown()) {
			if (keyCode == '1')
				switchToSketch = 0;
			if (keyCode == '2')
				switchToSketch = 0;
			if (keyCode == '3')
				switchToSketch = 1;
			if (keyCode == '4')
				switchToSketch = 2;
			if (keyCode == '5')
				switchToSketch = 3;
			if (keyCode == '6')
				switchToSketch = 4;
			if (keyCode == '7')
				switchToSketch = 5;
			if (keyCode == '8')
				switchToSketch = 6;
			if (keyCode == '9')
				switchToSketch = 7;
			if (keyCode == '0')
				switchToSketch = 8;
			if (keyCode == '0'){
				switchToSketch = 9;
			}
		}
		// give sketches a chance to respond to events
		if (currSketch != null)
			currSketch.keyPressed();
	}

	public void mousePressed() {
		if (currSketch != null)
			currSketch.mousePressed();
		
		//recording = !recording;
	}

	public void mouseDragged() {
		if (currSketch != null)
			currSketch.mouseDragged();
	}

	public void mouseReleased() {
		if (currSketch != null)
			currSketch.mouseReleased();
	}

	public void oscEvent(OscMessage theOscMessage) {
	
		// PING TO KINECT WALLS
		if (theOscMessage.checkAddrPattern("/ping") == true) {
			if (theOscMessage.checkTypetag("i")) {
				OscMessage myMessage = new OscMessage("/ping");
				myMessage.add(0); 
				oscP5.send(myMessage, kinectWallComputer);
				oscP5.send(myMessage, kinectWallComputer2);
				return;
			}
		}
		
		// PING TO KINECT WALLS
		if (theOscMessage.checkAddrPattern("/ctrlPing") == true) {
			if (theOscMessage.checkTypetag("i")) {
				OscMessage myMessage = new OscMessage("/ping");
				myMessage.add(0); 
				oscP5.send(myMessage, cueInterface);
				return;
			}
		}
		
		// FROM KINECT 1
		if (theOscMessage.checkAddrPattern("/w1Click") == true) {
			if (theOscMessage.checkTypetag("i")) {
				kinect1click = theOscMessage.get(0).intValue();
				//println(" click: "+kinect1click);
				return;
			}
		}
		
		if (theOscMessage.checkAddrPattern("/w1Click2") == true) {
			if (theOscMessage.checkTypetag("i")) {
				kinect1click2 = theOscMessage.get(0).intValue();
				//println(" click: "+kinect1click2);
				return;
			}
		}
		if (theOscMessage.checkAddrPattern("/w1PosRightHand") == true) {
			if (theOscMessage.checkTypetag("ff")) {
				kinect1Xraw = (int) (width * theOscMessage.get(0).floatValue()); // extended interaction mode
				kinect1Yraw = (int) (height * theOscMessage.get(1).floatValue()); 
				kinect1Xraw = (int) map(theOscMessage.get(0).floatValue(),0.13f,0.87f,(width/2)-(width/4.7f),(width/2)+(width/4.7f)); //AJUSTER EN FONCTION DU 20 MéTRES DE PROJO
			    //println(" values: " + theOscMessage.get(0).floatValue() + theOscMessage.get(1).floatValue());
				return;
			}
		}

		if (theOscMessage.checkAddrPattern("/w1PosLeftHand") == true) {
			if (theOscMessage.checkTypetag("ff")) {
				kinect1X2raw = (int) (width * theOscMessage.get(0).floatValue()); // extended interaction mode
				kinect1Y2raw = (int) (height * theOscMessage.get(1).floatValue()); 
				kinect1X2raw = (int) map(theOscMessage.get(0).floatValue(),0.13f,0.87f,(width/2-width/4.7f),(width/2+width/4.7f)); //AJUSTER EN FONCTION DU 20 MéTRES DE PROJO
				//println(" values: "+kinect1X2raw+", "+kinect1Y2raw+"");
				return;
			}
		}
		
		// FROM KINECT 2
				if (theOscMessage.checkAddrPattern("/w2Click") == true) {
					if (theOscMessage.checkTypetag("i")) {
						kinect2click = theOscMessage.get(0).intValue();
						//println(" click: "+kinect1click);
						return;
					}
				}
				
				if (theOscMessage.checkAddrPattern("/w2Click2") == true) {
					if (theOscMessage.checkTypetag("i")) {
						kinect2click2 = theOscMessage.get(0).intValue();
						//println(" click: "+kinect1click2);
						return;
					}
				}
				if (theOscMessage.checkAddrPattern("/w2PosRightHand") == true) {
					if (theOscMessage.checkTypetag("ff")) {
						kinect2Xraw = (int) (width * theOscMessage.get(0).floatValue()); // extended interaction mode
						kinect2Yraw = (int) (height * theOscMessage.get(1).floatValue()); 
						kinect2Xraw = (int) map(theOscMessage.get(0).floatValue(),0.13f,0.87f,(width/2)-(width/4.7f),(width/2)+(width/4.7f)) +15; //AJUSTER EN FONCTION DU 20 MéTRES DE PROJO
					    //println(" values: " + theOscMessage.get(0).floatValue() + theOscMessage.get(1).floatValue());
						return;
					}
				}

				if (theOscMessage.checkAddrPattern("/w2PosLeftHand") == true) {
					if (theOscMessage.checkTypetag("ff")) {
						kinect2X2raw = (int) (width * theOscMessage.get(0).floatValue()); // extended interaction mode
						kinect2Y2raw = (int) (height * theOscMessage.get(1).floatValue()); 
						kinect2X2raw = (int) map(theOscMessage.get(0).floatValue(),0.13f,0.87f,(width/2-width/4.7f),(width/2+width/4.7f))+15; //AJUSTER EN FONCTION DU 20 MéTRES DE PROJO
						//println(" values: "+kinect1X2raw+", "+kinect1Y2raw+"");
						return;
					}
				}
		
		// MAX CONTROL INTERFACE
		
		// seq dŽcide de quelle sŽquence
		if (theOscMessage.checkAddrPattern("/seq") == true) {
			if (theOscMessage.checkTypetag("i")) {
				int s = theOscMessage.get(0).intValue();
				if (s == 0)
					switchToSketch = 1;
				if (s == 1)
					switchToSketch = 0;
				if (s == 2)
					switchToSketch = 1;
				if (s == 3)
					switchToSketch = 2;
				if (s == 4)
					switchToSketch = 3;
				if (s == 5)
					switchToSketch = 4;
				if (s == 6)
					switchToSketch = 5;
				if (s == 7)
					switchToSketch = 6;
				if (s == 8)
					switchToSketch = 7;
				if (s == 9)
					switchToSketch = 8;
				if (s == 10)
					switchToSketch = 9;
				if (s == 11)
					switchToSketch = 10;
				if (s == 12)
					switchToSketch = 11;
				if (s == 13)
					switchToSketch = 12;
				if (s == 14)
					switchToSketch = 13;
				if (s == 15)
					switchToSketch = 14;
				if (s == 16)
					switchToSketch = 15;
				if (s == 17)
					switchToSketch = 16;
				if (s == 18)
					switchToSketch = 17;
				if (s == 19)
					switchToSketch = 18;
				if (s == 20)
					switchToSketch = 19;
				if (s == 21)
					switchToSketch = 20;
				return;
			}
		}
		
	
		//seqStep dŽcide quelle Žtape dans la sequence
		if (theOscMessage.checkAddrPattern("/seqStep") == true) {
			if (theOscMessage.checkTypetag("i")) {
				int p = theOscMessage.get(0).intValue();
				seqStep = p;
				println("SEQSTEP : " + seqStep);
				return;
			}
		}
		
		//seqStep dŽcide quelle Žtape dans la sequence
		if (theOscMessage.checkAddrPattern("/emoticonAdd") == true) {
			if (theOscMessage.checkTypetag("i")) {
				int p = theOscMessage.get(0).intValue();
				emoticonAdd = p;
				println("EmoticonAdd : " + emoticonAdd);
				return;
			}
		}
		
		//seqStep dŽcide quelle Žtape dans la sequence
		if (theOscMessage.checkAddrPattern("/seqInc") == true) {
			if (theOscMessage.checkTypetag("i")) {
				int p = theOscMessage.get(0).intValue();
				seqStep = seqStep + 1;
				println("SEQSTEP : " + seqStep);
				return;
			}
		}

		//seqStep dŽcide quelle Žtape dans la sequence
		if (theOscMessage.checkAddrPattern("/ping") == true) {
			if (theOscMessage.checkTypetag("i")) {
				int p = theOscMessage.get(0).intValue();
				if(p == 2) println("PING FROM BACK KINECT");
				if(p == 3){
					println("PING FROM CUE INTERFACE");
					OscMessage myMessage = new OscMessage("/ping");
					myMessage.add(0); 
					oscP5.send(myMessage, cueInterface);
				}
				if(p == 1) println("PING FROM FRONT KINECT");
				return;
			}
		}
	}
	
	public void kinectWallEasing(){
		
		//HAND1
		if(hasRunHand1 == 0 && kinect1click == 1){
			kinect1X = kinect1Xraw;
			kinect1Y = kinect1Yraw;
			hasRunHand1 = 1;
			
		}
		
		if(kinect1click == 1){
			//kinect1X = kinect1Xraw;
			//kinect1Y = kinect1Yraw;
			
			float dx = kinect1Xraw - kinect1X;
			if(abs(dx) > 1) {
				kinect1X += dx * wallEasing;
			}
			float dy = kinect1Yraw - kinect1Y;
			if(abs(dy) > 1) {
				kinect1Y += dy * wallEasing;
			}
			
		}
		
		if(kinect1click == 0){
			hasRunHand1 = 0;
		}

		//HAND2
		if(hasRunHand2 == 0 && kinect1click2 == 1){
			kinect1X2 = kinect1X2raw;
			kinect1Y2 = kinect1Y2raw;
			hasRunHand2 = 1;
		}

		if(kinect1click2 == 1){
			//kinect1X2 = kinect1X2raw;
			//kinect1Y2 = kinect1Y2raw;
			
			float dx2 = kinect1X2raw - kinect1X2;
			if(abs(dx2) > 1) {
				kinect1X2 += dx2 * wallEasing;
			}
			float dy2 = kinect1Y2raw - kinect1Y2;
			if(abs(dy2) > 1) {
				kinect1Y2 += dy2 * wallEasing;
			}
				
		}
		
		if(kinect1click2 == 0){
			hasRunHand2 = 0;
		}
		
		
		
		//HAND1 - WALL2
		if(hasRunHand1W2 == 0 && kinect2click == 1){
			kinect2X = kinect2Xraw;
			kinect2Y = kinect2Yraw;
			hasRunHand1W2 = 1;
			
		}
		
		if(kinect2click == 1){
			//kinect1X = kinect1Xraw;
			//kinect1Y = kinect1Yraw;
			
			float dx = kinect2Xraw - kinect2X;
			if(abs(dx) > 1) {
				kinect2X += dx * wallEasing;
			}
			float dy = kinect2Yraw - kinect2Y;
			if(abs(dy) > 1) {
				kinect2Y += dy * wallEasing;
			}
			
		}
		
		if(kinect2click == 0){
			hasRunHand1W2 = 0;
		}

		//HAND2
		if(hasRunHand2W2 == 0 && kinect2click2 == 1){
			kinect2X2 = kinect2X2raw;
			kinect2Y2 = kinect2Y2raw;
			hasRunHand2W2 = 1;
		}

		if(kinect2click2 == 1){
			//kinect1X2 = kinect1X2raw;
			//kinect1Y2 = kinect1Y2raw;
			
			float dx2 = kinect2X2raw - kinect2X2;
			if(abs(dx2) > 1) {
				kinect2X2 += dx2 * wallEasing;
			}
			float dy2 = kinect2Y2raw - kinect2Y2;
			if(abs(dy2) > 1) {
				kinect2Y2 += dy2 * wallEasing;
			}
				
		}
		
		if(kinect2click2 == 0){
			hasRunHand2W2 = 0;
		}
	}
	
	public void drawDebug() {
		frame.setTitle((int)(frameRate)+"fps");
		rectMode(CORNER);
		
		fill(220, 200);
		noStroke();
		
		String s="Current: "+currSketch.className;
		float w=textWidth(s);
		rect(width-(w+10),0, width-(w+10),26);
		fill(33);
		text(s,width-(w+10)+6,18);
	
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { DreamingNow.class.getName() });
	}

	public void beginContact(Contact cp) {
		currSketch.beginContact(cp);
		}

	public void endContact(Contact cp) {
		}
	
	private class Change {

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