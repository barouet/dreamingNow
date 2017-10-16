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
import seq.WEBCAM.Lettre2;




public class ENTER extends Sketch{
	
	//NUMBER WALL
	
	int scrollStart = 38435-24000;
	
	ArrayList lettre;
	int alpha;
	int alphaVideo;
	int curtainHigh= 0 ;
	int curtainLow= 0 ;
	int redWordSize = 10;
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
	int liveCue = 5;
	int shakeContamine;
	Change[] change = new Change[11];
	int counterRemplissage = 0; 
	
	PImage kinectRGBimage;
	PImage kinectRGBimage2;
	//CV stuff
	PFont font;
	PImage sample_img;

	public float threshold;
	public float threshold2;
	int[] isActive = new int[numPixelsWide*numPixelsHigh];
	int isActiveTotal = 0;
	int[] moveTo = new int[numPixelsWide*numPixelsHigh];
	int returnThere;
	
	//Show options
	boolean showPixels;
	boolean showLetters;
	boolean showRaw;
	
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
	
	public ENTER(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		lettre = new ArrayList();
		lettre2 = new ArrayList();
		
		 for (int i = 0; i < change.length; i++) {
			  change[i] = new Change();
		  }


	}
	
	public void reinit() {

		super.reinit();
		fade = 0;
		p.kinectConnected = true;
		p.skeletonTracking = false;	
		liveCue = 5;
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
		
		//font = p.loadFont("fonts/PixelSquare-Bold-12.vlw");
		//font = p.loadFont("fonts/Golden-Sun-48.vlw");
		//font = p.loadFont("fonts/HiraKakuStd-W8-12.vlw");
		//font = p.loadFont("fonts/ArialMT-12.vlw");
		//font = p.loadFont("fonts/Futura-Medium-12.vlw");
		font = p.loadFont("fonts/HelveticaNeue-12.vlw");
		//font = p.loadFont("fonts/MicrosoftSansSerif-12.vlw");
		
		c.textFont(font,12);
		
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
		

		for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
	    }
		showPixels = false;
		showLetters = false;
		showRaw = false;

		
	    curtainMode = 0;
		curtainHigh = 0 ;
		curtainLow = 0 ;
		wordScroller = 0;
		counterMax = 9;
		counterRemplissage = 0;
		
		//english version
	    //String rawText = "Enter     I press a key and the screen lights up. A sound. Desktop. Screensaver. Menu bar. I click. Options. Preferences. System. Enter passwordÊ: ********. SearchÊ: binary code. I click. SearchÊ: social media. I click. Save as. Delete. Move to trash. Are you sure you want to quit? Cancel.  I sit in front of the screen. For hours. For days. Forever. The world around me disappears. I see a new world. A world of pixels blinking at me. I see a new world. Virtual. Brighter. BIGGER. Faster. I click. An endless stream of information. Of possibilities. I stare at the screen. A brilliant world of pixels blinking at me. ItÕs like IÕm hypnotized. De-sensitized. I click. I can go anywhere. I can do anything. I can be anyone. I stare. ItÕs like IÕm hypnotized. De-sensitized. I stare at the screen, transfixed. I want to stay here forever. I transform, transfixed. ItÕs like the world I live in. It IS the world I live in. I click. Unlimited access. Streaming. Download. Live feed. Scroll down. I click. Video content not available. I click. Surfing endlessly on a sea/waves of data. I can go anywhere. I can do anything. I can be anyone. I want to stay here forever. I am transformed, transfixed. ItÕs like the world I live in. It IS the world I live in. An endless stream of information. I click. I see a sea of data/information, facts. I click. Search. Enter. I press a key and the screen lights up. A sound. Desktop. Screensaver. Menu bar. I click. Options. Preferences. System. Enter passwordÊ: ******** . I click. Surfing endlessly on a sea/waves of data.  I click.  Floating in an ever-expanding cosmos of facts. I click. An endless stream of information. Of possibilities. A new world. Virtual. Brighter. I want to stay here forever  ENTER    forever here stay to want I .Brighter .Virtual .world new A  .possibilities Of .information of stream endless An .facts of cosmos expanding-ever an in Floating .click I  .data of waves/sea a on endlessly Surfing click I Sea .******** :ÊpasswoRd Enter .System .Preferences .Options .click I .bar Menu .Screensaver .Desktop .sound A .up lights screen the and key a press I Enter .Search .click I .facts of cosmos expanding-ever an in Floating .click I .information of stream endless An .in live I world the IS It .in live I world the like sÕIt .transfixed ,transformed am I .forever here stay to want I .anyone be can I .anything do can I .anywhere go can I Inp .data of waves/sea a on endlessly Surfing .click I .available not content Video .click I .down Scroll .feed Live .Download .Streaming .access Unlimited .click I .in live I world the IS It .in live I world the like sÕIt .transfixed ,transform I .forever here stay to.anything do .click I .screen the at stare I .sensitized-De .hypnotized mÕI like sÕIt wa I .anyone be can I .anything do can I .anywhere go can I .click I .sensitized-De .hypnotized mÕI like sÕIt .me at blinking pixels of world brilliant A .screen the at stare I .possibilities Of .information of stream endless An .click I .Faster .BIGGER .Brighter .Virtual .world new a see I .me at blinking pixels of world A .world new a see I .disappears me around world The .Forever .days For .hours For .screen the of front in sit I  .Cancel ?quit to want you sure you Are .trash to Move .Delete .as Save .click I .media social :ÊSearch .click I .code binary :ÊSearch .******** :Êpassword Enter .System .Preferences .Options .click I .bar Menu .Screensaver .Desktop .sound A .up lights screen the and key a press I .forever here stay to want I .Brighter .Virtual .world new A  .possibilities Bright   Enter";
	    //french version
		String rawText = "Cliquez J'appuie une touche et l'Žcran s'allume. Un son. Fichier. Fond d'Žcran. Un menu. Je clique. Paramtres. PrŽfŽrences. Systme. Mot de passeÊ: ********. RechercheÊ: code numŽrique. Je clique. RechercheÊ: mŽdias sociaux. Je clique. Sauvegarder. Supprimer. ætes-vous certain de vouloir quitter?  Je suis devant l'Žcran depuis des heures. Des jours.  Toujours. Le monde autour de moi disparait. Je vois un nouveau monde. Un monde de pixels. Je vois un nouveau monde. Virtuel. LUMINEUX. InstantanŽ. Je clique. De l'information sans fin. Des possibilitŽs. Je fixe l'Žcran. Un monde de pixels scintille devant moi. Je suis hypnotisŽ. DŽsensibilisŽ. Je clique. Je peux aller partout. Je peux tout faire. Me transformer. Je fixe l'Žcran. HypnotisŽ. DŽsensibilisŽ. Je fixe l'Žcran, obnubilŽ. Je veux demeurer ici Žternellement. Je suis transformŽ, transfigurŽ. C'est comme le monde dans lequel je vis. C'est le monde dans lequel je vis. Je clique. Accs illimitŽ. Diffusion. TŽlŽcharger. En direct. Je clique. Contenu non disponible. Je clique. Je navique un ocŽan de donnŽes. Je peux aller partout. Je suis tout puissant. J'invente qui je suis. Je veux demeurer ici Žternellement. Je suis transformŽ, transfigurŽ. C'est le monde dans lequel je vis. Un flux infini de donnŽes. Je clique. J'observe un ocŽan de donnŽes/informations. Je clique. Recherche. Cliquez. J'appuie une touche et l'Žcran s'allume. Un son. Fichier. Fond d'Žcran. Un menu. Je clique. Paramtres. PrŽfŽrences. Systme. Mot de passeÊ: ******** . Je clique. Je navique un ocŽan de donnŽes. En suspension dans un cosmos d'informations. Un flux infini de donnŽes. De possibilitŽs. Un nouveau monde. Virtuel. Lumineux. InstantanŽ. HypnotisŽ, je veux demeurer ici Žternellement CLIQUEZ  Žternellement ici demeurer veux je ,hypnotisŽ .instantanŽ .Lumineux .Virtuel .Monde nouveau un .possibilitŽs de .donnŽes de infini flux un .d'informations cosmos un dans suspension en .donnŽes de ocŽan un navigue je .clique je .******** : passe de mot. systme. prŽfŽrences. paramtres. clique je. menu un. d'Žcran fond. fichier. son un. s'allume l'Žcran et touche une j'appuie. Cliquez. recherche. clique je. informations/donnŽes de ocŽan un j'observe. clique je. donnŽes de infini flux un. vis je lequel dans monde le c'est. transfigurŽ, transformŽ suis je.  Žternellement ici demeurer veux je .suis je qui j'invente. puissant tout suis je .partout aller peux je .donnŽes de ocŽan un navigue je. clique je. disponible-non contenu. clique je. direct en. tŽlŽcharger. diffusion. limitŽ accs. clique je. vis je lequel dans monde le c'est. vis je lequel dans monde le comme c'est. transfigurŽ, transformŽ suis je. Žternellement ici demeurer veux je. obnubilŽ, l'Žcran fixe je. dŽsensibilisŽ.  hypnotisŽ.  l'Žcran fixe je. transformer me. faire tout peux je. partout aller peux je .clique je .dŽsensibilisŽ. hypnotisŽ suis je  .moi devant scintille pixels de monde un .l'Žcran fixe je .possibilitŽs des . fin sans l'information de clique je .instannŽ .lumineux .virtuel .monde nouveau un vois je .pixels de monde un .monde nouveau un vois je .disparait moi de autour monde le .toujours .jours des .heures des depuis l'Žcran devant suis je ?quitter vouloir certain vous tes .supprimer .sauvegarder .clique je .sociaux mŽdias :recherche .clique je .numŽrique code :recherche . ******** : passe de mot .systme .prŽfŽrences  .Paramtres .donnŽes de infini flux un .transfigurŽ ,transformŽ suis je .moi devant scintille pixels de monde un .menu un .d'Žcran fond .fichier .son un .s'allume l'Žcran et touche une j'appuie cliquez";
	    //rawText = rawText.toLowerCase();
	    rawText = rawText.toUpperCase();
	    for (int e = 0; e < rawText.length(); e++) {
	      text[e] = (int) rawText.charAt(e); // Split the line into chars
	     
	    }
		p.println("ENTER part 1");
		
	}

	public void draw() {
		
		super.draw();
		
		
		imageAlpha = p.constrain(imageAlpha, 0, 255);
		c.tint(255,imageAlpha);
		
		//threshold = p.map(p.knob1.y, 0.99f, 0.009f, 0, 100);
		//threshold2 = p.map(p.knob5.y, 0.98f, 0.009f, 0, 100);
		
		//detection_resolution = (int) p.map(p.knob2.y, 0.f, 1.f, 0, 10);
		//timeLine = timeLine + 60000;
		//p.println(timeLine);
		handsToOneHandRaw();

		c.beginDraw();
		c.textFont(font,blockSize);
		c.background((int) 0);
		//c.fill(0,20);
		//c.rect(0,0,p.width,p.height);
		
		events();
		keyframes();
		curtain(curtainMode);


				
	
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
		
		
		

	
		
		c.fill(0,255);
		c.noStroke();

		c.hint(p.ENABLE_DEPTH_TEST);

		c.stroke(0);
		c.strokeCap(100);
		
		//Fadein
		c.rectMode(p.CORNER);
		c.fill(0,fade);
		c.rect(-10,-10,p.width+20,p.height+20);

		
		c.endDraw();

	}

	public void keyPressed() {
	}

	public void keyReleased() {
		
	}
	
	public void keyframes(){
		//p.println(timeLine);
		//Opening of sequence + black, slow fadein
		//p.seqStep = 30;
		int cue = 0;
	

		if (timeLine > cue){
			fade = (int) p.constrain(p.map(timeLine, 0f,scrollStart,255f,0f), 0, 255);
		}
		
		//THIS NEEDS TO BE DONE WITH MUSIC TRACK
		cue = 0; //
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 16;//ENTER
			redWordSize = 0;
		}

		cue = scrollStart; //215739
		if (timeLine > cue && timeLine < cue+500){
			redWordSize = 10;
			p.seqStep = 17;//GO
		}
		
		cue = 4895+scrollStart; //
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 18;
		}

		cue = 14505+scrollStart; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 19;
		}
		
		cue = 19385+scrollStart; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 20;
		}
		
		cue = 24075+scrollStart;
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 21;
		}
		
		cue = 28850+scrollStart; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 22;
		}
		
		cue = 43205+scrollStart; 
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 23;
		}
		
		
		cue = 50586+scrollStart; //prepare for end
		if (timeLine > cue && timeLine < cue+500){
			p.seqStep = 25;
		}
		
		cue = 67197+scrollStart; //stops at enter
		if (timeLine > cue && timeLine < cue+500) {
			p.seqStep = 27;
			
		}
		
		if(kinect1Clicksub == 1){ //&& p.seqStep == 27) {
			p.seqStep = 30;
		}
		
	
			
	}

	public void events(){
		
		if (p.seqStep == 0){ 
			 showPixels = false;
			 showLetters = false;
			 showRaw = false;
			 counterRemplissage = 0;
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
				//p.println("fast" + wordScroller);
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
				//p.println("read" + wordScroller);
			}
			wordScroller(4);
			wordScrollerBackward(4);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 19){
			if(change[0].test(p.seqStep)){
				//p.println("fast" + wordScroller);
			}
			wordScroller(1);
			wordScrollerBackward(1);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 20){
			if(change[0].test(p.seqStep)){
				//tln("read" + wordScroller);
			}
			wordScroller(4);
			wordScrollerBackward(4);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 21){
			if(change[0].test(p.seqStep)){
				//p.println("fast" + wordScroller);
			}
			wordScroller(1);
			wordScrollerBackward(1);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}

		if (p.seqStep == 22){
			if(change[0].test(p.seqStep)){
				//p.println("read" + wordScroller);
			}
			wordScroller(4);
			wordScrollerBackward(4);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}

		if (p.seqStep == 23){
			if(change[0].test(p.seqStep)){
				//p.println("fast" + wordScroller);
			}
			wordScroller(2);
	
			wordScrollerBackward(1);

			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 24){
			if(change[0].test(p.seqStep)){
				//p.println("fast" + wordScroller);
			}
			
			wordScroller(2);
			wordScrollerBackward(2);
			contentShake(1,0,10);
			digitalWall(1, 2);
		}
		
		if (p.seqStep == 25){
			if(change[0].test(p.seqStep)){
				//p.println("read" + wordScroller);
				wordScrollerFinal = wordScroller;
				wordScrollerFinal2 = wordScroller2;
			}
			wordScroller(1300);
			wordScrollerBackward(1000);
			contentShake(1,0,10);
			digitalWall(1, 2);
			
			if(timeLine <= 307432+20220) {
				wordScroller = (int) p.map(timeLine, 50586+scrollStart, 67197+scrollStart, wordScrollerFinal, (lettre2.size()/2) - 42 - 5);
				wordScroller2= (int) p.map(timeLine, 50586+scrollStart, 67197+scrollStart, wordScrollerFinal2, (lettre2.size()/2) -42  + 5);
			}
		}

		if (p.seqStep == 27){
			
			for(int i = 0; i < lettre2.size(); i++){
				Lettre2 l2 = (Lettre2) lettre2.get(i);
				if(p.frameCount % 4 == 0 ) l2.life --;
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
		
		if (p.seqStep == 30){
			OscMessage myMessage2 = new OscMessage("/cueNum");
			myMessage2.add(liveCue);//add an int to the osc message
			p.oscP5.send(myMessage2, p.soundComputer);
			myMessage2 = new OscMessage("/cueGo");
			myMessage2.add(1);//add an int to the osc message 
			p.oscP5.send(myMessage2, p.soundComputer);
			p.switchToSketch = 3;
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
	
		if(change[5].test(kinect1Clicksub) && kinect1Clicksub == 1){
			kinect1Clicksub = 1;
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



	public void printlnFPS() {	
		//c.fill(100, 200, 255);
		//c.text("nana", 10, 20);
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
					if (i>wordScroller2 - redWordSize && i < wordScroller2){
						l2.lettre = text[i];
						l2.afficheText(false,3,2,false);
						l2.life = 255;
					}
					else if(i>=wordScroller && i < wordScroller+redWordSize){
						l2.lettre = text[i];
						l2.afficheText(false,3,2,false);
						l2.life = 255;
					} 
					else l2.afficheText(false,3,1,false);
				}
				
				if(mode == 3){ //lettres + counterFilling[0-9]
					if(i>=wordScroller && i < wordScroller+redWordSize){
						l2.lettre = text[i];
						l2.afficheText(false,3,3,false);
						l2.life = 255;
					} else if(i>wordScroller2 && i < wordScroller2+redWordSize){
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
			
			counterRemplissage = (int) p.map(timeLine, 0, 0, 0, lettre2.size()); // CUE 1 et 2 ˆ Timing MATCH
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
		int textAlpha;
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
			textAlpha = 0;
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


			if(fillBypass) c.fill(255,0,0,textAlpha);

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