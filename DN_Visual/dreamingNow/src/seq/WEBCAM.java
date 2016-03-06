package seq;

import java.util.ArrayList;
import processing.core.*;
import processing.video.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import diewald_CV_kit.libraryinfo.*;
import diewald_CV_kit.utility.*;
import diewald_CV_kit.blobdetection.*;
import processing.opengl.PGraphicsOpenGL;
import seq.NUMBERS.Lettre;




public class WEBCAM extends Sketch{
	
	//NUMBER WALL

	ArrayList lettre;
	int alpha;
	int alphaVideo;
	int curtainHigh= 0 ;
	int curtainLow= 0 ;
	boolean curtainHighUp= false ;
	boolean curtainHighDown= false ;
	boolean curtainLowUp= false ;
	boolean curtainLowDown= false ;
	//int counter = lettre.length;
	boolean counterUp;
	boolean counterDown;
	boolean counterReset;
	boolean counterZero;
	int counter2 = 0;
	int couleur;
	int couleurBW = 0;
	int blockSize = 12;
	int numPixelsWide = p.width / blockSize;
	int numPixelsHigh = p.height / blockSize;
	int seqStep = 0;
	int imageAlpha = 0;
	int imageAlpha2 = 0;

	int shakeContamine;
	Change[] change = new Change[11];
	int counterRemplissage = 0; 
	
	PImage kinectRGBimage;
	PImage kinectRGBimage2;
	//CV stuff
	PFont font;
	PImage sample_img;

	BlobDetector blob_detector;
	BoundingBox detection_area;
	int detection_resolution = 1;
	boolean draw_blobs_boundingsbox  = true;
	boolean draw_filled_blobs        = true;
	public float threshold;
	public float threshold2;
	int[] isActive = new int[numPixelsWide*numPixelsHigh];
	int isActiveTotal = 0;
	int[] moveTo = new int[numPixelsWide*numPixelsHigh];
	int returnThere;
	
	//SlitScan
	float signal;
	float signalThickness;
	int direction = 1 ;
	float signal2;
	int direction2 = 1 ;
	
	//Show options
	boolean showPixels;
	boolean showLetters;
	boolean showRaw;
	boolean showBlob;
	boolean showScan;
	boolean showScanPixels;
	boolean showBlobLines;
	
	//From NUMBER WALL
	ArrayList lettre2;
	int curtainMode;
	//int counter = lettre.length;
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
	
	//Kinect Hands to OneHand
	int kinect1Xsub;
	int kinect1Ysub;
	int kinect1X2sub;
	int kinect1Y2sub;
	int kinect1Clicksub;
	int kinectOneHandX;
	int kinectOneHandY;
	int previouskinectOneHandY;
	
	public WEBCAM(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		lettre = new ArrayList();
		lettre2 = new ArrayList();
		
		 for (int i = 0; i < change.length; i++) {
			  change[i] = new Change();
		  }

		  blob_detector = new BlobDetector(p.kinect.rgbWidth(), p.kinect.rgbHeight());
		  blob_detector.setResolution(detection_resolution);
		  blob_detector.computeContours(true);
		  blob_detector.computeBlobPixels(!true);
		  blob_detector.setMinMaxPixels(10*10, p.kinect.rgbWidth() * p.kinect.rgbHeight());
		  PImage kinectRBGimage = p.kinect.rgbImage();
		  kinectRGBimage2 = p.createImage(p.width,p.height,p.ARGB);
		  kinectRGBimage2.updatePixels();
		  blob_detector.setBLOBable(new BLOBable_GRADIENT(p, kinectRBGimage));

		  detection_area = new BoundingBox(0, 0, p.kinect.rgbWidth(), p.kinect.rgbHeight());
		  blob_detector.setDetectingArea(detection_area);

		
	}
	
	public void reinit() {

		super.reinit(); 
		p.kinectConnected = true;
		p.skeletonTracking = false;	
		
		p.shaderSelect = 0;
		lettre.clear();
		lettre2.clear();
		alpha = 0;
		
		
		for (int j = 0; j < numPixelsHigh; j++) {
		    for (int i = 0; i < numPixelsWide; i++) {
		    	lettre.add(new Lettre(0, i*blockSize, j*blockSize));	
		    }
		 }
		
		for (int j = 0; j < numPixelsHigh; j++) {
		    for (int i = 0; i < numPixelsWide; i++) {
		    	lettre2.add(new Lettre2(0, i*blockSize, j*blockSize));	
		    }
		 }
		
		p.seqStep = 0;
		p.shaderSelect = 0;
		seqStep = 0;
		imageAlpha = 0;	
		imageAlpha2 = 0;
		isActiveTotal = 0;
		c.textFont(p.font,12);
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		
		kinectRGBimage2 = p.createImage(p.width,p.height,p.ARGB);
		kinectRGBimage2.updatePixels();

		for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
	    }
		showPixels = false;
		showLetters = false;
		showRaw = false;
		showBlob = false;
		showScan = false;
		showScanPixels = false;
		showBlobLines = false;
		
		signal = 0;
		
	    curtainMode = 0;
		curtainHigh = 0 ;
		curtainLow = 0 ;
		wordScroller = 0;
		counterMax = 9;
		counterRemplissage = 0;
	    String rawText = " Enter    I press a key and the screen lights up. A sound. Desktop. Screensaver. Menu bar. I click. Options. Preferences. System. Enter passwordÊ: ********. SearchÊ: binary code. I click. SearchÊ: social media. I click. Save as. Delete. Move to trash. Are you sure you want to quit? Cancel.  I sit in front of the screen. For hours. For days. Forever. The world around me disappears. I see a new world. A world of pixels blinking at me. I see a new world. Virtual. Brighter. BIGGER. Faster. I click. An endless stream of information. Of possibilities. I stare at the screen. A brilliant word of pixels blinking at me. ItÕs like IÕm hypnotized. De-sensitized. I click. I can go anywhere. I can do anything. I can be anyone. I wa I stare. ItÕs like IÕm hypnotized. De-sensitized. I stare at the screen, transfixed. to stay here forever. I transform, transfixed. ItÕs like the world I live in. It IS the world I live in. I click. Unlimited access. Streaming. Download. Live feed. Scroll down. I click. Video content not available. I click. Surfing endlessly on a sea/waves of data. Inp I can go anywhere. I can do anything. I can be anyone. I want to stay here forever. I am transformed, transfixed. ItÕs like the world I live in. It IS the world I live in. An endless stream of information. I click. I see a sea of data/information    of facts. I click. Search. Enter I press a key and the screen lights up. A sound. Desktop. Screensaver. Menu bar. I click. Options. Preferences. System. Enter passwordÊ: ******** . Sea I click Surfing endlessly on a sea/waves of data.  I click.  Floating in an ever-expanding cosmos of facts. An endless stream of information. Of possibilities. A new world. Virtual. Brighter. I want to stay here forever.  ENTER   .forever here stay to want I .Brighter .Virtual .world new A  .possibilities Of .information of stream endless An .facts of cosmos expanding-ever an in Floating .click I  .data of waves/sea a on endlessly Surfing click I Sea .******** :ÊpasswoRd Enter .System .Preferences .Options .click I .bar Menu .Screensaver .Desktop .sound A .up lights screen the and key a press I Enter .Search .click I .facts of cosmos expanding-ever an in Floating .click I .information of stream endless An .in live I world the IS It .in live I world the like sÕIt .transfixed ,transformed am I .forever here stay to want I .anyone be can I .anything do can I .anywhere go can I Inp .data of waves/sea a on endlessly Surfing .click I .available not content Video .click I .down Scroll .feed Live .Download .Streaming .access Unlimited .click I .in live I world the IS It .in live I world the like sÕIt .transfixed ,transform I .forever here stay to.anything do .click I .screen the at stare I .sensitized-De .hypnotized mÕI like sÕIt wa I .anyone be can I .anything do can I .anywhere go can I .click I .sensitized-De .hypnotized mÕI like sÕIt .me at blinking pixels of word brilliant A .screen the at stare I .possibilities Of .information of stream endless An .click I .Faster .BIGGER .Brighter .Virtual .world new a see I .me at blinking pixels of world A .world new a see I .disappears me around world The .Forever .days For .hours For .screen the of front in sit I  .Cancel ?quit to want you sure you Are .trash to Move .Delete .as Save .click I .media social :ÊSearch .click I .code binary :ÊSearch .******** :Êpassword Enter .System .Preferences .Options .click I .bar Menu .Screensaver .Desktop .sound A .up lights screen the and key a press I .forever here stay to want I .Brighter .Virtual .world new A  .possibilities Bright    Enter";

	    for (int e = 0; e < rawText.length(); e++) {
	      text[e] = (int) rawText.charAt(e); // Split the line into chars
	     
	    }
		p.println("FACE");
		
	}

	public void draw() {
		
		super.draw();
		
		if(p.seqStep < 16) p.kinect.update(); //Superimportant sinon a gobe le CPU
		
		//timeLine = p.timecodeSlave;
	
		imageAlpha = p.constrain(imageAlpha, 0, 255);
		c.tint(255,imageAlpha);
		
		//threshold = p.map(p.knob1.y, 0.99f, 0.009f, 0, 100);
		//threshold2 = p.map(p.knob5.y, 0.98f, 0.009f, 0, 100);
		
		//detection_resolution = (int) p.map(p.knob2.y, 0.f, 1.f, 0, 10);
		//timeLine = timeLine + 60000;
		//p.println(timeLine);
		handsToOneHandRaw(); 
		c.beginDraw();
		c.textFont(p.font,blockSize);
		c.background((int) 0);
		//c.fill(0,20);
		//c.rect(0,0,p.width,p.height);
		
		events();
		partition();
		curtain(curtainMode);
				
		kinectRGBimage = p.kinect.rgbImage();
		PImage slitScan = p.kinect.rgbImage();
		PImage slitScan1 = p.kinect.rgbImage();
		
		if(showRaw){
			c.image(p.kinect.rgbImage(),0,0,p.width,p.height);
		}
		
		if(showScan){
			signal = p.constrain(signal,0,kinectRGBimage.height-1);
			/*
			if (signal >= kinectRGBimage.height-1 || signal <= 0) { 
				direction = direction * -1;
			}
			if (signal2 >= kinectRGBimage.height-1 || signal2 <= 0) { 
				direction2 = direction2 * -1;
			}

			signal += (3.0*direction); 
			signal = p.constrain(signal,0,kinectRGBimage.height-1);

			signal2 += (2.0*direction2); 
			signal2 = p.constrain(signal2,0,kinectRGBimage.height-1);
			 */

			int signalOffset = (int)(signal)*kinectRGBimage.width;
			int signalOffset2 = (int)(signal2)*kinectRGBimage.width;

			for (int y = (int) p.constrain(signal-signalThickness,0,kinectRGBimage.height); y < signal; y++) {
				p.arrayCopy(kinectRGBimage.pixels, signalOffset, slitScan.pixels, y*slitScan.width, kinectRGBimage.width);
			}
			for (int y = (int) p.constrain(signal2-30,0,kinectRGBimage.height); y < signal2; y++) {
				//p.arrayCopy(kinectRBGimage.pixels, signalOffset2, slitScan2.pixels, y*slitScan2.width, kinectRBGimage.width);
			}
			slitScan.updatePixels();

			//c.image(slitScan,0,0,p.width,p.height);
			//c.updatePixels();
		}
		
		

		if(showLetters){
			/*
			kinectRGBimage2 = kinectRGBimage.get();
			kinectRGBimage2.resize(p.width, p.height);
			
			int counter3 = 0;
			for (int j = 0; j < numPixelsHigh; j++) {
				for (int i = 0; i < numPixelsWide; i++) {
					couleur = p.color( kinectRGBimage2.get((i*blockSize), (j*blockSize)));
					Lettre l = (Lettre) lettre.get(counter3);
					l.couleur((int) p.map(PixelColor.brighntess(couleur),0,90,255,0));
					l.texte = couleur;
					l.afficheText(false,0);
					counter3++;
					l.life = 255;
					
				}
			}
			*/
		}
	
		if(showPixels){
			int counter = 0;
			for (int j = 0; j < numPixelsHigh; j++) {
				for (int i = 0; i < numPixelsWide; i++) {
					//l'image source est de 640x480, il faut faire des maths alors
					couleur = p.color( p.kinect.rgbImage().get((int)((i * blockSize )*(640/(float)p.width)), (int)((j * blockSize )*(480/(float)p.height))));
					Lettre l = (Lettre) lettre.get(counter);
					if( PixelColor.brighntess(couleur) > threshold){
						if(counter >= counterRemplissage){
						l.couleur(couleur);
						l.afficheImage(false, i+(j*i));
						//isActive[counter] = 1;
						}
					} //else isActive[counter] = 0;
					if( j * blockSize >signal && showScanPixels){
						l.couleur(couleur);
						l.afficheImage(false, i+(j*i));
						//isActive[counter] = 1;

					} //else isActive[counter] = 0;
					counter++;

					l.life=255;
					//if(p.frameCount%10 == 0) l.life=(int) p.map(timeLine, 66000, 67500, 255, 0);
				}
			}
		}
		
		
		counter = 0;
		for (int j = 0; j < numPixelsHigh; j++) {
			for (int i = 0; i < numPixelsWide; i++) {
				couleur = p.color( p.kinect.rgbImage().get((int)((i * blockSize )*(640/(float)p.width)), (int)((j * blockSize )*(480/(float)p.height))));
				Lettre2 l2 = (Lettre2) lettre2.get(counter);
				l2.counter = (int) p.map(PixelColor.brighntess(couleur),0,90,9,0);
				l2.colore = couleur;
				counter++;	
			}	
		}
		
		
		if(showBlob){ 
			//blob_detector.setBLOBable(new BLOBable_GRADIENT(p, kinectRBGimage));
			// set resolution - improves speed a lot
			blob_detector.setResolution(detection_resolution);
			//update the blob-detector with the new pixelvalues
			blob_detector.update();


			// get a list of all the blobs
			ArrayList<Blob> blob_list = blob_detector.getBlobs();

			// iterate through the blob_list
			for (int blob_idx = 0; blob_idx < blob_list.size(); blob_idx++ ) {

				// get the current blob from the blob-list
				Blob blob = blob_list.get(blob_idx);

				// get the list of all the contours from the current blob
				ArrayList<Contour> contour_list = blob.getContours();

				// iterate through the contour_list
				for (int contour_idx = 0; contour_idx < contour_list.size(); contour_idx++ ) {

					// get the current contour from the contour-list
					Contour contour = contour_list.get(contour_idx);

					// get the current boundingbox from the current contour
					BoundingBox bb = contour.getBoundingBox();

					// handle the first contour (outer contour = contour_idx == 0) different to the inner contours
					if ( contour_idx == 0) {

						// draw the boundingbox and blob-id as text
						if ( draw_blobs_boundingsbox ) {
							drawBoundingBox(bb, p.color(0, 200, 200), 1);
							c.fill(255);
							//c.text("body["+blob_idx+"]", bb.xMin() * (p.width/640f), (bb.yMin()- p.textDescent()*2) * (p.height/480f));

							//c.line(0,(int) ((bb.yMin()- p.textDescent()*2) * (p.height/480f)),p.width, (int) ((bb.yMin()- p.textDescent()*2) * (p.height/480f)));
							//c.line((int) (bb.xMin() * (p.width/640f)), 0, (int) (bb.xMin() * (p.width/640f)), p.height);

						}

						// draw the contour
						drawContour(contour.getPixels(), p.color(255, 0, 0), p.color(255, 0, 255, 100), draw_filled_blobs, 1);
					} 
					else {


						//kinectRBGimage.copy((int) (bb.xMin() * (p.width/640f)), (int) ((bb.yMin()- p.textDescent()*2) * (p.height/480f)), 100, 100, 10, 10, 100, 100);

						// draw the inner contours, if they have more than 20 vertices
						if ( contour.getPixels().size() > 25) {
							drawContour(contour.getPixels(), p.color(255, 150, 0), p.color(0, 100), false, 1);
						}
					}
				}
			}
		}

	
		
		c.fill(0,255);
		c.noStroke();

		c.hint(p.ENABLE_DEPTH_TEST);

		c.stroke(0);
		c.strokeCap(100);
		c.endDraw();

	}

	public void keyPressed() {
		
	}

	public void keyReleased() {

		}
	
	public void partition(){
		

		int cue = 35500+20220; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 1;
			p.println(imageAlpha);
		}
		
		cue = 48110+20220; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 2;
		}
		
		cue = 77005+20220; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 3;
		}

		cue = 124990+20220; //115345
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 4;
		}
		
		cue = 144135+20220; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 5;
		}

		
		cue = 182450+20220; 
		if (timeLine > cue && timeLine < cue+500){
			//p.seqStep = 6;
		}
		
		cue = 201710+20220;
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 7;
		}
		
		cue = 220810+20220;
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 8;
		}
		
		cue = 259337+20220;
		if (timeLine > cue && timeLine < cue+500){
			//p.seqStep = 9;
		}

		
		cue = 240239+20220;
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 16;//ENTER
		}

		cue = 245000+20220; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 17;//GO
		}
		
		cue = 249895+20220; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 18;
		}

		cue = 259505+20220; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 19;
		}
		
		cue = 264385+20220; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 20;
		}
		
		cue = 269075+20220;
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 21;
		}
		
		cue = 273850+20220; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 22;
		}
		
		cue = 288205+20220; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 23;
		}
		
		
		cue = 295586+20220; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 25;
		}
		
		cue = 307432+20220;
		if (timeLine > cue && timeLine < cue+500) {
			p.seqStep = 27;
			
		}
		
	}

	public void events(){
		
		if (p.seqStep == 0){ 
			 showPixels = false;
			 showLetters = false;
			 showRaw = false;
			 showBlob = false;
			 showScan = false;
			 showScanPixels = false;
			 showBlobLines = false;
			 signalThickness = 60;
			 detection_resolution = 0;
			 counterRemplissage = 0;
			 
		}
		if (p.seqStep == 1){
			if(change[0].test(p.seqStep)){
				imageAlpha = 0;
			
			}
			//if(p.frameCount % 2 == 0){
				imageAlpha++;
			//}
			showRaw = true;		
			
		}
		
		if (p.seqStep == 2){
			//if(p.frameCount % 2 == 0){
				imageAlpha++;
			//}
			showScan = true;
			/*
			if(timeLine >= 48110+20220 && timeLine < 57645+20220) {
				signal = p.map(timeLine, 48110+20220, 57645+20220, 0, p.height);
				if(signal < p.height - 100) signalThickness = 100;
				else signalThickness = p.map(signal, p.height - 100, p.height, 100, 0);
			}
			if(timeLine >= 57645+20220 && timeLine < 67290+20220) {
				signal = p.map(timeLine, 57645+20220, 67290+20220, p.height, 0);
				if(signal < p.height - 100) signalThickness = 100;
				else signalThickness = p.map(signal, p.height - 100, p.height, 100, 0);
			}
			
			if(timeLine >= 67290+20220 && timeLine < 72065+20220) {
				signal = p.map(timeLine, 67290+20220, 72065+20220, 0, p.height);
				if(signal < p.height - 100) signalThickness = 100;
				else signalThickness = p.map(signal, p.height - 100, p.height, 100, 0);
			}
			
			
			if(timeLine >= 72065+20220 && timeLine < 76870+20220) {
				signal = p.map(timeLine, 72065+20220, 76870+20220, p.height, 0);
				if(signal < p.height - 100) signalThickness = 100;
				else signalThickness = p.map(signal, p.height - 100, p.height, 100, 0);
			}
			*/
			if(timeLine >= 67290+20220 && timeLine < 76870+20220) {
				signal = p.map(timeLine, 67290+20220, 76870+20220, 0, p.height);
				if(signal < p.height - 100) signalThickness = 100;
				else signalThickness = p.map(signal, p.height - 100, p.height, 100, 0);
			}
			signal = p.constrain(signal,0,kinectRGBimage.height-1);
		}
		
		
		if (p.seqStep == 3){
			showRaw = true;
			imageAlpha = 255;
			showScanPixels = false;
			showPixels = true;
			showScan = false;
			if (timeLine >= 72005+20220 && timeLine <= 96090+20220) {
				threshold = p.map(timeLine, 77005+20220, 96090+20220, 100, 80);
			}
			if (timeLine >= 96091+20220 && timeLine <= 124990+20220) {
				threshold = p.map(timeLine, 96091+20220, 124990+20220, 80, 0);
			}
			if (timeLine >= 124990+20220) {
				threshold = 0;
			}
			//p.println(threshold);
			//p.println(timeLine);
		}
		
		if (p.seqStep == 4){
			//BLOBS
			threshold = 100;
		
			if (timeLine >= 124990+20220 && timeLine <= 129544+20220) {
				threshold2 = p.map(timeLine, 124990+20220, 129544+20220, 100, 40);
			}
			
			if (timeLine >= 129544+20220 && timeLine < 144135+20220) {
				threshold2 = p.map(timeLine, 129544+20220, 144135+20220, 40, 70);
			}
			//RETOUR VERS 70 INTERPOLER
			if ( timeLine >= 144135+20220) {
				threshold2 = 70;
			}
			//p.println(timeLine);
			//p.println(threshold2);
			showBlob = true;
			showPixels = false;
			showBlobLines = true;
			
		}
		
		if (p.seqStep == 5){
			//BLOBS vers LEGOMAN
			//p.println(threshold);
			 counterRemplissage = 0;
			showBlob = true;
			showScan = false;
			showBlobLines = true;
			showPixels = true;
			
			threshold2 = 70;
					
			if(change[0].test(p.seqStep)){
				imageAlpha = 0;
				detection_resolution = 0;
			}
			
			//Scans
			if(timeLine >= 182450+20220) showScan = true;
			
			if(timeLine >= 182450+20220 && timeLine < 191905+20220) {
				signal = p.map(timeLine, 182450+20220, 191905+20220, 0, p.height);
				if(signal < p.height - 100) signalThickness = 100;
				else signalThickness = p.map(signal, p.height - 100, p.height, 100, 0);
			}
			
			if(timeLine >= 191905+20220 && timeLine < 201615+20220) {
				signal = p.map(timeLine, 191905+20220, 201615+20220,  p.height, 0);
				if(signal < p.height - 100) signalThickness = 100;
				else signalThickness = p.map(signal, p.height - 100, p.height, 100, 0);
			}
			
			if(timeLine >= 163300+20220 && timeLine < 168010+20220) {
			detection_resolution = (int) p.map(timeLine, 163300+20220, 168010+20220, 0, 8);
			}
			
			//Legoman
			if(timeLine > 168010+20220) {
				detection_resolution = 8;
			}

			if(timeLine >= 168010+20220 && timeLine < 177685+20220) {
				signal = p.map(timeLine, 168010+20220, 177685+20220, 0, p.height);
				if(signal < p.height - 100) signalThickness = 100;
				else signalThickness = p.map(signal, p.height - 100, p.height, 100, 0);
			}
			
			
			if(timeLine >= 177685+20220 && timeLine < 182450+20220) {
				signal = p.map(timeLine, 177685+20220, 182450+20220, 0, p.height);
				if(signal < p.height - 100) signalThickness = 100;
				else signalThickness = p.map(signal, p.height - 100, p.height, 100, 0);
			}
				
		
			
			if (timeLine >= 163300+20220 && timeLine <= 182450+20220) {
				threshold = p.constrain(p.map(timeLine, 163300+20220, 182450+20220, 100, 0),0,100);
				
			}
		
			if (timeLine >= 182450+20220 && timeLine <= 201710+20220) {
				threshold2 = p.map(timeLine, 182450+20220, 201710+20220, 70, 100);
			}
			
		}
		
		if (p.seqStep == 6){ //RetirŽe tout en 5
			showBlob = true;
			showBlobLines = true;
			showScan = false;
			if(timeLine > 182450+20220) {
				detection_resolution = 8;
			}
			/*
			showPixels = true;
			if (timeLine >= 182450 && timeLine <= 201710) {
				threshold = p.constrain(p.map(timeLine, 182450, 201710, 100, 0),0,100);
				
			}
			
			
			if (timeLine >= 182450 && timeLine <= 201710) {
				threshold2 = p.map(timeLine, 182450, 201710, 6, 100);
			}
			*/
			
		}
		
	
		if (p.seqStep == 7){
			
			showBlob = false;
			threshold = 0;
			threshold2 = 100; 
			if(change[0].test(p.seqStep)){
				//brasse les cartes!
				for(int i = 0; i < lettre2.size(); i++){
					Lettre2 l2 = (Lettre2) lettre2.get(i);
					l2.counter = (int) p.random(0,9);
					l2.life = 255;
				}
				counterRemplissage = 0;
				
			}
			digitalWall(1, 0);
			
		}
		
		
		if (p.seqStep == 8){
			if(change[0].test(p.seqStep)){
				curtainMode = 1;
			}
			curtainLow = (int) p.constrain(p.map(timeLine,220810+20220,239997+20220,0,p.height), 0, p.height);
			digitalWall(1, 0);
		}
		
	
		if (p.seqStep == 9){ 
			if(change[0].test(p.seqStep)){
				curtainMode = 2;
			}
			curtainLow = (int) p.constrain(p.map(timeLine,259337+20220,268385+20220,p.height,0), 0, p.height);
			digitalWall(1, 0);
		}
		if (p.seqStep == 10){ 

		}

		if (p.seqStep == 16){

			if(change[0].test(p.seqStep)){
				counterRemplissage = lettre2.size();
				wordScroller = 0;
				wordScroller2 = numPixelsWide*numPixelsHigh;
				for(int i = 0; i < lettre2.size(); i++){
					Lettre2 l2 = (Lettre2) lettre2.get(i);
					l2.counter = (int) p.random(0,9);
					l2.life = 0;
					l2.lifeEasing = 1f;
				}
			}
			wordScroller = 0;
			wordScroller2 = numPixelsWide*numPixelsHigh;
			curtainHigh = 0;
			curtainLow = 0;
			curtainMode = 0;
			digitalWall(1, 2);
		}

		if (p.seqStep == 17){
			if(change[0].test(p.seqStep)){
				p.println("fast" + wordScroller);
				counterRemplissage = lettre2.size();
				wordScroller = 0;
				wordScroller2 = numPixelsWide*numPixelsHigh;
				for(int i = 0; i < lettre2.size(); i++){
					Lettre2 l2 = (Lettre2) lettre2.get(i);
					l2.counter = (int) p.random(0,9);
					l2.life = 0;
				}
			}
		
			curtainHigh = 0;
			curtainLow = 0;
			curtainMode = 0;
			wordScroller(1);
			wordScrollerBackward(1);
			contentShake(1,0,10);
			digitalWall(1, 2);
			
		}

		if (p.seqStep == 18){
			if(change[0].test(p.seqStep)){
				p.println("read" + wordScroller);
			}
			wordScroller(4);
			wordScrollerBackward(4);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 19){
			if(change[0].test(p.seqStep)){
				p.println("fast" + wordScroller);
			}
			wordScroller(1);
			wordScrollerBackward(1);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 20){
			if(change[0].test(p.seqStep)){
				p.println("read" + wordScroller);
			}
			wordScroller(4);
			wordScrollerBackward(4);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 21){
			if(change[0].test(p.seqStep)){
				p.println("fast" + wordScroller);
			}
			wordScroller(1);
			wordScrollerBackward(1);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}

		if (p.seqStep == 22){
			if(change[0].test(p.seqStep)){
				p.println("read" + wordScroller);
			}
			wordScroller(4);
			wordScrollerBackward(4);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}

		if (p.seqStep == 23){
			if(change[0].test(p.seqStep)){
				p.println("fast" + wordScroller);
			}
			wordScroller(2);
	
			wordScrollerBackward(1);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 24){
			if(change[0].test(p.seqStep)){
				p.println("fast" + wordScroller);
			}
			
			wordScroller(2);
			wordScrollerBackward(2);
			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 25){
			if(change[0].test(p.seqStep)){
				p.println("read" + wordScroller);
				wordScrollerFinal = wordScroller;
				wordScrollerFinal2 = wordScroller2;
			}
			wordScroller(1300);
			wordScrollerBackward(1000);
			contentShake(1,0,10);
			digitalWall(1, 2);
			
			if(timeLine <= 307432+20220) {
				
				wordScroller = (int) p.map(timeLine, 295586+20220, 307432+20220, wordScrollerFinal, (lettre2.size()/2) - 42 - 5);
				wordScroller2= (int) p.map(timeLine, 295586+20220, 307432+20220, wordScrollerFinal2, (lettre2.size()/2) -42  + 5);
			}
		}

		if (p.seqStep == 27){
			
			for(int i = 0; i < lettre2.size(); i++){
				Lettre2 l2 = (Lettre2) lettre2.get(i);
				if(p.frameCount % 6 == 0 ) l2.life --;
			}
			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 28){
			if(change[0].test(p.seqStep)){
				OscMessage myMessage = new OscMessage("/cueNum");
				myMessage.add(5); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.soundComputer);
				myMessage = new OscMessage("/cueGo");
				myMessage.add(1); /* add an int to the osc message */
				p.oscP5.send(myMessage, p.soundComputer);
			}
		}
		
	}
 	
	public void contentShake(int numLettre){
		
		int rand = (int)p.random(0, 100);
		int rand2 = (int)p.random(0, 100);
		int rand3 = (int)p.random(0, 100);
		int rand4 = (int)p.random(0, 100);

		Lettre2 l2 = (Lettre2) lettre2.get(numLettre); //Map pour centrer l'opŽration

		if(l2.id() == rand || l2.id() == rand2 || l2.id() == rand3 || l2.id() == rand4){
			//Lettres ou chiffres
				l2.count(0,9);
		}

	}
	
	public void fullContentShake() {
		
		int rand = (int)p.random(0, 100);
		int rand2 = (int)p.random(0, 100);
		int rand3 = (int)p.random(0, 100);
		int rand4 = (int)p.random(0, 100);
		
		for(int i = 0; i < lettre2.size(); i++){
			Lettre2 l2 = (Lettre2) lettre2.get(i); //Map pour centrer l'opŽration

			if(l2.id() == rand || l2.id() == rand2 || l2.id() == rand3 || l2.id() == rand4){
				//Lettres ou chiffres
				l2.count(0,9);
			}
		}
	}
		
	public void mousePressed() {
		
		p.println(timeLine);
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
	
		if(kinect1Clicksub == 1 && p.seqStep == 27) {
			p.seqStep = 28;
		}
			
		
	}

	class Lettre {
		int counter = 0;
		int coordX;
		int coordY;
		int coordZ;
		int coordZeased;
		int id = (int)p.random(0, 100);
		int colore;
		int life;
		int life2;
		int life3;
		int texte;
		int colorR;
		int colorG;
		int colorB;
		int texteMax;

		Lettre(int cStart, int oX, int oY) {
			counter = cStart;
			coordX = oX;
			coordY = oY;
			coordZ = 0;
			coordZeased = 0;
			colore = (int)p.random(0,255);
			life = 0;
			life2 = 0;
		}

		void count(int cMin, int cMax) {
			counter=p.constrain((counter+1)%10, cMin, cMax);
		}

		void couleur(int c) {
			colore = c;
		}

		void couleur(int r, int g, int b) {
			colorR = r;
			colorG = g;
			colorB = b;
		}

		void afficheText(boolean zaxis, int colorMode) { 


			if(colorMode == 0){
				c.fill(colore,life);
			}else c.fill(colorR,colorG,colorB,life2);


			c.textAlign(p.CENTER, p.BASELINE);

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,100,-100);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}

			c.text((int)p.map(p.brightness(texte),0,255,9,0), coordX+6, coordY+10, coordZeased);

		}

		void afficheText2(boolean zaxis, int colorMode) { 


			if(colorMode == 0){
				c.fill(colore,life);
			}else c.fill(colorR,colorG,colorB,life2);


			c.textAlign(p.CENTER, p.BASELINE);

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,100,-100);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}

			c.text((int)p.map(p.brightness(texte),0,255,9,0), coordX+6, coordY+10, coordZeased);

		}

		void afficheText3(boolean zaxis, int colorMode) { 


			if(colorMode == 0){
				c.fill(colore,life3);
			}else c.fill(colorR,colorG,colorB,life3);


			c.textAlign(p.CENTER, p.BASELINE);

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,100,-100);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}
			texteMax = p.constrain(texteMax,0,9);
			c.text(p.constrain((int)p.map(p.brightness(texte),0,255,texteMax,0)+counter,0,9), coordX+6, coordY+10, coordZeased);

		}

		void affichePoint(boolean zaxis, int index) { 

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,600,-600);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}
			life = p.constrain(life, 0 , 255);
			c.strokeCap(p.SQUARE);
			c.strokeWeight(14);
			c.stroke(colore,life);
			c.fill(colore);
			coordX = (moveTo[index]%numPixelsWide) * blockSize; 
			coordY = (moveTo[index]/numPixelsHigh) * blockSize;
			c.point((coordX+6) *(p.width/640f),(coordY+6) *(p.height/480f),coordZeased);
			c.text(p.constrain((int)p.map(p.brightness(texte),0,255,texteMax,0)+counter,0,9), coordX+6, coordY+10, coordZeased);
			
		}
		
		void afficheImage(boolean zaxis, int index) { 

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,600,-600);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}
			life = p.constrain(life, 0 , 255);
			c.strokeCap(p.SQUARE);
			c.strokeWeight(blockSize);
			c.stroke(colore,life);
			c.fill(colore);
			
			//int coordXnew = (moveTo[index]%numPixelsWide) * blockSize; 
			//int coordYnew = (moveTo[index]/numPixelsHigh) * blockSize;
			//c.point((coordX+5) *(p.width/640f),(coordY+14) *(p.height/480f),coordZeased);
			c.point((coordX+7),(coordY+10),coordZeased);
			
			c.fill(255);
			//c.ellipse((coordX+6) *(p.width/640f),(coordY+6) *(p.height/480f),blockSize,blockSize);
			//kinectRBGimage.copy((int) (coordX* (640/(float)p.width)), (int) (coordY * (480/(float)p.height)), 100, 100, (int) (coordXnew * (640/(float)p.width)), (int) (coordYnew * (480/(float)p.height)),100,100);
			//kinectRBGimage.copy((int)(((coordX) *(p.width/640f)) * (640/(float)p.width)),(int)(((coordY) *(p.height/480f)) * (480/(float)p.height)), 20,20,(int)(((coordXnew) *(p.width/640f)) * (640/(float)p.width)),(int)(((coordYnew) *(p.height/480f)) * (480/(float)p.height)), 20,20);	
			
		}

		int id() { 
			return id;  
		}
	}

	//new BLOBable class, that implements the BLOBable-interface.
	public final class BLOBable_GRADIENT implements BLOBable{
		int width_, height_;
		private float hsb_[] = new float[3];
		private float mousex_val_, mousey_val_;
		private String name_; 
		private PApplet papplet_;
		private PImage img_;
		public boolean trackWhite;

		public BLOBable_GRADIENT(PApplet papplet, PImage img){
			papplet_ = papplet;
			img_ = img;
			trackWhite = true;
		}

		//@Override
		public final void init() {
			name_ = this.getClass().getSimpleName(); 
		}

		//@Override
		public final void updateOnFrame( int width, int height) {
			width_ = p.width;
			height_ = p.height;
			//mousex_val_ = PApplet.map(papplet_.mouseX, 0, papplet_.width,  0, 99);
			mousey_val_ = PApplet.map(papplet_.mouseY, 0, papplet_.height, 0, 360);
			mousex_val_ = threshold2;
			if (mousex_val_ > 99 ) mousex_val_ = 99;
			//System.out.println("MY NAME IS: " +this.getClass().getSimpleName());
		}
		//@Override
		public final boolean isBLOBable(int pixel_index, int x, int y) {
			//THRESHOLD SETTING
			//if (  PixelColor.brighntess(img_.pixels[pixel_index]) > mousex_val_){
			if (  PixelColor.brighntess(img_.pixels[pixel_index]) > mousex_val_){
				return true;
			} else {
				return false;
			}
		}
	}

	public void printlnFPS() {	
		//c.fill(100, 200, 255);
		//c.text("nana", 10, 20);
	}

	public void printlnNumberOfBlobs(BlobDetector blob_detector) {
		//c.fill(100, 200, 255);
		//c.text("number of blobs: "+blob_detector.getBlobs().size(), 100, 40);
	}

	// draw convex-hull - as polyline
	public void drawConvexHull(ConvexHullDiwi convex_hull, int stroke_color, float stroke_weight) {
		c.noFill();
		c.stroke(0); 
		c.strokeWeight(stroke_weight);
		DoubleLinkedList<Pixel> convex_hull_list = convex_hull.get();
		convex_hull_list.gotoFirst();
		c.beginShape();
		for (int cvh_idx = 0; cvh_idx < convex_hull_list.size()+1; cvh_idx++, convex_hull_list.gotoNext() ) {
			Pixel r = convex_hull_list.getCurrentNode().get();
			c.vertex(r.x_*(p.width/640f), r.y_*(p.height/480f));
		}
		c.endShape();
	}

	// draw convex-hull-points - only points
	public void drawConvexHullPoints(ConvexHullDiwi convex_hull, int stroke_color, float stroke_weight) {
		c.noFill();
		c.stroke(0); 
		c.strokeWeight(stroke_weight);
		DoubleLinkedList<Pixel> convex_hull_list = convex_hull.get();
		convex_hull_list.gotoFirst();

		for (int cvh_idx = 0; cvh_idx < convex_hull_list.size(); cvh_idx++, convex_hull_list.gotoNext() ) {
			Pixel r = convex_hull_list.getCurrentNode().get();
			c.point(r.x_*(p.width/640f), r.y_*(p.height/480f));
		}
	}

	// draw boundingsbox
	public void drawBoundingBox(BoundingBox bb, int stroke_color, float stroke_weight) {
		c.noFill();
		c.stroke(0); 
		c.strokeWeight(stroke_weight);
		c.rect(0,0, p.width, p.height);
	}

	// draw contour
	public void drawContour(ArrayList<Pixel> pixel_list, int stroke_color, int fill_color, boolean fill, float stroke_weight) {
		if ( !fill)
			c.noFill();
		else
			c.fill(0);
		c.stroke(255);
		if(!showBlobLines){
			c.noStroke();
		}
		c.strokeWeight(stroke_weight);
		c.beginShape();
		for (int idx = 0; idx < pixel_list.size(); idx++) {
			Pixel r = pixel_list.get(idx);
			c.vertex(r.x_*(p.width/640f), r.y_*(p.height/480f));
		}
		c.endShape();
	}

	// draw points
	public void drawPoints(ArrayList<Pixel> pixel_list, int stroke_color, float stroke_weight) {
		c.stroke(stroke_color);
		c.strokeWeight(stroke_weight);

		for (int idx = 0; idx < pixel_list.size(); idx++) {
			Pixel r = pixel_list.get(idx);
			c.point(r.x_*(p.width/640f), r.y_*(p.height/480f));
		}
	}

	//FROM THE DIGITAL WALL
	public void wordScroller(int speed){
		
		int rand = (int)p.random(0, 100);
		int rand2 = (int)p.random(0, 100);
		int rand3 = (int)p.random(0, 100);
		int rand4 = (int)p.random(0, 100);
		if(speed < 1000){
			if(p.frameCount%p.constrain(speed, 1, 20) == 0) wordScroller++;
		} 
		
		if(wordScroller > 4000) wordScroller = 0;
		
		for(int i = 0; i< lettre2.size(); i++){
			Lettre2 l2 = (Lettre2) lettre2.get(i);
			if(l2.id() == rand || l2.id() == rand2 || l2.id() == rand3 || l2.id() == rand4){
				l2.count(0,48);
				l2.lettre = (l2.lettre+1);
				if(l2.lettre > 122) l2.lettre = 65;
				if(l2.lettre > 90 && l2.lettre < 97)  l2.lettre = 97;
				l2.life--;
			}
		}
	}
	
	public void wordScrollerBackward(int speed){
		
		int rand = (int)p.random(0, 100);
		int rand2 = (int)p.random(0, 100);
		int rand3 = (int)p.random(0, 100);
		int rand4 = (int)p.random(0, 100);
		
		if(speed < 1000){
			if(p.frameCount%p.constrain(speed, 1, 20) == 0) wordScroller2--;
		}
		if(wordScroller2 <= 0) wordScroller2 = 0;
		
		for(int i = lettre2.size()-1; i > 0; i--){
			Lettre2 l2 = (Lettre2) lettre2.get(i);
			if(l2.id() == rand || l2.id() == rand2 || l2.id() == rand3 || l2.id() == rand4){
				l2.count(0,48);
				l2.lettre = (l2.lettre+1);
				if(l2.lettre > 122) l2.lettre = 65;
				if(l2.lettre > 90 && l2.lettre < 97)  l2.lettre = 97;
				l2.life--;
			}
		}
	}
	
	public void curtain(int param){
		
		if(param > 0){
		
			//Ouvre de bas en haut
			if(param == 1){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					//curtainLow++;
				}
			}
			
			//Ferme de haut en bas
			if(param == 2){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					//curtainLow--;
				}
			}

			//ouvre de bas en haut
			if(param == 3){
				curtainLow = p.height;
				if(p.frameCount%2 == 0){
					curtainHigh++;
				}
			}

			//Ouvre de haut en bas
			if(param == 4){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					curtainLow++;
				}
			}
			
			//Ferme de bas en haut doit caller curtainHigh = p.height avant l'appel de la fonction
			if(param == 5){
				curtainLow = p.height;
				if(p.frameCount%2 == 0){
					curtainHigh--;
				}
			}

			curtainLow = p.constrain(curtainLow, 0, p.height);
			curtainHigh = p.constrain(curtainHigh, 0, p.height);
			c.fill(0);
			c.rect(0,0+curtainHigh,p.width,0+curtainLow);
		}
	}
	
	public boolean digitalWall(int direction, int mode){
		
		if(direction == 1){
			
			for(int i = 0; i< counterRemplissage; i++){
				
				Lettre2 l2 = (Lettre2) lettre2.get(i);
				l2.couleur(255);
				
				if(mode == 0){ //Normal Mode
					l2.lettre = text[i];
					l2.afficheText(false,0,0,false);
					l2.life = 255;
					
				}
				
				if(mode == 1){ //Teinte de gris Mode
					l2.lettre = text[i];
					l2.afficheText(false,0,1,false);
					l2.life = 255;
					
				}

				if(mode == 2){ //wordScroller Mode
					if (i>wordScroller2 -10 && i < wordScroller2){
						l2.lettre = text[i];
						l2.afficheText(false,3,2,false);
						l2.life = 255;
					}
					else if(i>wordScroller && i < wordScroller+10){
						l2.lettre = text[i];
						l2.afficheText(false,3,2,false);
						l2.life = 255;
					} 
					else l2.afficheText(false,3,1,false);
				}
				
				if(mode == 3){ //lettres + counterFilling[0-9]
					if(i>wordScroller && i < wordScroller+10){
						l2.lettre = text[i];
						l2.afficheText(false,3,3,false);
						l2.life = 255;
					} else if(i>wordScroller2 && i < wordScroller2+10){
							l2.lettre = text[i];
							l2.afficheText(false,3,3,false);
							l2.life = 255;
						} else l2.afficheText(false,3,3,false);
				}
				
				if(mode == 4){ //lettres + counterFilling[0-9]
					
					if( i <= p.map(shakeContamineX,-1911,1911,0,3826) && i > 1911){
						l2 = (Lettre2) lettre2.get(i); //Map pour centrer l'opŽration
						l2.afficheText(false,3,4,false);
					} else if ( i > 1911) l2.afficheText(false,0,1,false);


					if(i >= p.map(0-shakeContamineX,-1911,1911,0,3826) && i < 1911){
						l2 = (Lettre2) lettre2.get(i); //Map pour centrer l'opŽration
						l2.afficheText(false,3,4,false);
					} else if( i<= 1911) l2.afficheText(false,0,1,false);

				}

				if(mode == 5){ //Normal Mode with 3d
					l2.lettre = text[i];
					l2.afficheText(true,0,0,false);
					l2.life = 255;
					
				}
				
				if(mode == 6){ //Gray scale Mode with 3d
					l2.lettre = text[i];
					l2.afficheText(true,0,1,false);
					l2.life = 255;
				}
				
				if(mode == 8){ //Gray scale Mode with 3d FREE LIFE
					l2.lettre = text[i];
					l2.afficheText(true,0,1,false);
				}
				
				if(mode == 7){ //Normal Mode with Axis ROtation
					l2.lettre = text[i];
					l2.afficheText(false,0,0,true);
					l2.life = 255;
					
				}
			}
			
			counterRemplissage = (int) p.map(timeLine, 201710+20220, 220810+20220, 0, lettre2.size()); // CUE 1 et 2 ˆ Timing MATCH
			counterRemplissage = p.constrain(counterRemplissage, 0,lettre2.size());
			
		}
		
		if(direction == -1){
			c.hint(p.ENABLE_DEPTH_TEST);
			for(int i = 0; i< counterRemplissage; i++){
				Lettre2 l2 = (Lettre2) lettre2.get(i);
				l2.couleur(255);

				if(mode == 0){ //Normal Mode
					l2.lettre = text[i];
					l2.afficheText(false,3,0,false);
					l2.life = 255;
				}

				if(mode == 2){ //wordScroller Mode
					if(i>wordScroller && i < wordScroller+10){
						l2.lettre = text[i];
						l2.afficheText(false,3,2,false);
						l2.life = 255;
					} else l2.afficheText(false,3,1,false);
				}
				counterRemplissage = p.constrain(counterRemplissage +=4, 0,lettre2.size());
			}
		}
		
		boolean completed;
		if(counterRemplissage == lettre2.size()) completed = true;
		else completed = false;
		
		return completed;
	}

	public void contentShake(int numLettre, int shakeWhat, int isolateNumber){
		
		switch (shakeWhat) {

		case 0: //TOUT LE WALL SHAKE

			int rand = (int)p.random(0, 100);
			int rand2 = (int)p.random(0, 100);
			int rand3 = (int)p.random(0, 100);
			int rand4 = (int)p.random(0, 100);
			for(int i = 0; i< lettre2.size(); i++){
				Lettre2 l2 = (Lettre2) lettre2.get(i);
				if(l2.id() == rand || l2.id() == rand2 || l2.id() == rand3 || l2.id() == rand4){
					//Lettres ou chiffres
					if(numLettre == 0) l2.count(0,9);
					else { 
						l2.lettre = (l2.lettre+1);
						if(l2.lettre > 122) l2.lettre = 65;
						if(l2.lettre > 90 && l2.lettre < 97)  l2.lettre = 97;
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
			
		
			Lettre2 l2 = (Lettre2) lettre2.get(numPixelsWide/2+((numPixelsHigh/2)*numPixelsWide));
		
				//Lettres ou chiffres
				if(numLettre == 0) {
					if(p.frameCount%4 == 0) {
						
						l2.count(0,9);
					}
				}
				
			break;


		case 3: //UN SEUL CHIFFRE CONTAMINE PROGRESSIVEMENT
			
			l2 = (Lettre2) lettre2.get(numPixelsWide/2+((numPixelsHigh/2)*numPixelsWide));
			l2.fillBypass = false;
			rand = (int)p.random(0, 100);
			rand2 = (int)p.random(0, 100);
			rand3 = (int)p.random(0, 100);
			rand4 = (int)p.random(0, 100);
			for(int x = 0; x < shakeContamineX; x++){
				//for(int y = 0; y < shakeContamineY; y++){
					l2 = (Lettre2) lettre.get(lettre2.size()/2+x); //Map pour centrer l'opŽration
					
					if(l2.id() == rand || l2.id() == rand2 || l2.id() == rand3 || l2.id() == rand4){
						//Lettres ou chiffres
						
						if(numLettre == 0) {
							l2.count(0,9);
						}
						if(x == shakeContamineX-1) {
							l2.counter = 0;
							//l.fillBypass = true;
						}
						else { 
							l2.fillBypass = false;
							l2.lettre = (l2.lettre+1);
							if(l2.lettre > 122) l2.lettre = 65;
							if(l2.lettre > 90 && l2.lettre < 97)  l2.lettre = 97;
						}
					
				}
					
			}
			for(int x = 0; x > -shakeContamineX; x--){
					
					l2 = (Lettre2) lettre.get(lettre.size()/2+x); //Map pour centrer l'opŽration
					if(l2.id() == rand || l2.id() == rand2 || l2.id() == rand3 || l2.id() == rand4){
						//Lettres ou chiffres
						
						if(numLettre == 0) {
							l2.count(0,9);
						
						}
						if(x == shakeContamineX){
							l2.counter = 0;
							//l.fillBypass = true;
						}
						
						else { 
							l2.fillBypass = false;
							l2.lettre = (l2.lettre+1);
							if(l2.lettre > 122) l2.lettre = 65;
							if(l2.lettre > 90 && l2.lettre < 97)  l2.lettre = 97;
						}
					}
				
			}
			
			if(p.frameCount % 2 == 0) {
				shakeContamineX=shakeContamineX+2;
				shakeContamineY++;
			}
			//shakeContamine=0;
			shakeContamineX = p.constrain(shakeContamineX, 0, 1913); //error!
			shakeContamineY = p.constrain(shakeContamineY, 0, numPixelsWide/2);
			break;
			
		case 4: //Tout shake vers 0

			rand = (int)p.random(0, 100);
			rand2 = (int)p.random(0, 100);
			rand3 = (int)p.random(0, 100);
			rand4 = (int)p.random(0, 100);
			for(int i = 0; i< lettre.size(); i++){
				l2 = (Lettre2) lettre.get(i);
				if(l2.id() == rand || l2.id() == rand2 || l2.id() == rand3 || l2.id() == rand4){
					//Lettres ou chiffres


					if(numLettre == 0) {
						if(l2.counter != isolateNumber)  l2.count(0,10);
					}
					else { 

						l2.lettre = (l2.lettre+1);
						if(l2.lettre > 122) l2.lettre = 65;
						if(l2.lettre > 90 && l2.lettre < 97)  l2.lettre = 97;
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
				l2 = (Lettre2) lettre.get(i);
				if(l2.id() == rand || l2.id() == rand2 || l2.id() == rand3 || l2.id() == rand4){
					//Lettres ou chiffres
					if(numLettre == 0) l2.count(0,counterMax);
					else { 
						l2.lettre = (l2.lettre+1);
						if(l2.lettre > 122) l2.lettre = 65;
						if(l2.lettre > 90 && l2.lettre < 97)  l2.lettre = 97;
					}
				}
			}
			break;
		}
		
	}
	
	class Lettre2 {
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

		Lettre2(int cStart, int oX, int oY) {
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
			afficheQuoi = quoi;
			//c.fill((int)p.map(counter,0,9,0,255));
			//c.fill(255);


			c.textAlign(p.CENTER, p.BASELINE);

			if(zaxis){
				coordZ = (int)p.map(counter,0,9,mapZ,-mapZ);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * easing;
			}

			float fx = life - lifeEased;
			if(p.abs(fx) > 1) {
				lifeEased += fx * lifeEasing;
			}


			if(fillQuoi == 1){
				if(afficheQuoi < 2) c.fill((int)p.map(counter,9,0,mapMin,255),(int)p.map(counter,9,0,mapMin,255),(int)p.map(counter,9,0,mapMin,255),lifeEased);
				else c.fill((int)p.map(lettre,64,120,50,255),(int)p.map(lettre,64,120,50,255),(int)p.map(lettre,64,120,50,255),lifeEased);
			} else if (fillQuoi == 2) c.fill(255,0,0,lifeEased);
			else if (fillQuoi == 0) c.fill(255,lifeEased);
			else if(fillQuoi == 3) c.fill((int)p.map(counter,9,0,mapMin,255),(int)p.map(counter,9,0,mapMin,255),(int)p.map(counter,9,0,mapMin,255),lifeEased);
			else if(fillQuoi == 4){
				c.fill((int)p.map(lettre,50,120,0,255),(int)p.map(lettre,50,120,0,255),(int)p.map(lettre,50,120,0,255),lifeEased+p.map(shakeContamineX,0,3860,0,255));
			}


			if(fillBypass) c.fill(255,0,0,lifeEased);

			c.pushMatrix();
			c.translate(coordX+7, coordY+14, coordZeased);

			if(rotateOnItself){
				if(timeLine <= 124200+20220){
					//c.rotateY(p.radians((p.map(timeLine, 115000, 124200,0,360))));
					//c.rotateZ(p.radians((p.map(timeLine, 115000, 124200,0,360))));
					//c.rotateX(p.radians((p.map(timeLine, 115000, 124200,0,360))));
				}
				else {
					c.rotateY(0);
					c.rotateZ(0);
					c.rotateX(0);
				}
			}

			if(afficheQuoi == 0){
				//c.ellipse(coordX+6,coordY+10,counter,counter);
				c.text(counter,0,0,0);
				
			}

			if(afficheQuoi == 1){
				c.text(chiffreLettre, 0,0,0);
			}

			if(afficheQuoi == 2){
				c.text((char)(counter+65), 0,0,0);
			}

			if(afficheQuoi == 3){
				c.text((char)lettre, 0,0,0);
				c.text((char)lettre, 0,0,0);
			}

			//c.text((int)p.map(p.brightness(colore),0,255,9,0), coordX+6, coordY+10, coordZeased);

			c.popMatrix();
		}

		void affichePoint(boolean zaxis) { 

			if(zaxis){
				coordZ = (int)p.map(p.brightness(colore),0,255,600,-600);
			} else coordZ = 0;


			float dx = coordZ - coordZeased;
			if(p.abs(dx) > 1) {
				coordZeased += dx * 0.03;
			}

			c.strokeCap(p.SQUARE);
			c.strokeWeight(blockSize);
			c.stroke(colore,255);
			c.point(coordX+6,coordY+6,coordZeased);


		}

		int id() { 
			return id;  
		}
	}

}