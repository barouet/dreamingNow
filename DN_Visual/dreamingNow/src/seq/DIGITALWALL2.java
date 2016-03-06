package seq;

//import dreaming.*;
import java.util.ArrayList;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
import processing.opengl.PGraphicsOpenGL;




public class DIGITALWALL2 extends Sketch{
	
	//NUMBER WALL
	private ArrayList lettre;
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
	int blockSize = 24;
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
	Change[] change = new Change[11];
	int modulo = 100;
	int step = 0;
	int counterMax = 9;
	int hasRun = 0;
	int stepTimeLine = 0;
	int stepTimeLineReset = 0 ;
	int stepTimeLine2 = 0;
	int stepTimeLineReset2 = 0;
	
	public DIGITALWALL2(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		lettre = new ArrayList();
		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}
		reinit();
	}

	public void reinit() {

		super.reinit(); 
		p.skeletonTracking = false;
		p.kinectConnected = false;
		p.skeletonFound = false;
		p.shaderSelect = 0;
		step = 0;
		lettre.clear();
		alpha = 0;
	
		for (int j = 0; j < numPixelsHigh; j++) {
		    for (int i = 0; i < numPixelsWide; i++) {
		    	lettre.add(new Lettre(0, i*blockSize, j*blockSize));
		    }
		}
		
		p.shaderSelect = 0;
	
		c.textFont(p.emoticon,31);
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
	
		wallFiller = 0;
		
	    //Change reset
	    for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
	    }
	    curtainMode = 0;
		curtainHigh = 0 ;
		curtainLow = 0 ;
		wordScroller = 0;
		counterMax = 9;
	    hasRun = 0;
		p.seqStep = 0;
		p.println("WALL 1 - EMOTICONS");
		c.rectMode(p.CORNER);
	}

	public void draw() {
		
		super.draw();
		stepTimeLine = timeLine - stepTimeLineReset;
		if (p.seqStep >= 3) stepTimeLine2 = timeLine - stepTimeLineReset2;
		else stepTimeLine2 = 0;
		c.beginDraw();
		//c.resetShader();
		//c.hint(p.ENABLE_DEPTH_TEST);
	    //c.textFont(p.emoticon,blockSize);
		c.background(0);
		partition();
		events();
		
	    c.fill(0,80);
			
		int counter = 0;
		
		for (int j = 0; j < numPixelsHigh; j++) {
			for (int i = 0; i < numPixelsWide; i++) {
				//couleur = p.color( p.mov.get((i*5), (j*8)));
				//if(counter == 0) couleur = 0;
				//else couleur = 255;
				//if(counter == 1000) couleur = 0;
				Lettre l = (Lettre) lettre.get(counter);
				l.couleur(couleur);
				//l.couleur((int)p.random(0,255));
				//if(p.frameCount%500 > 250) l.afficheText(true);		
				//l.affichePoint(false);	
				counter++;
			}
		}
		
		
		c.stroke(0);
		c.strokeCap(100);
		curtain(curtainMode);
		c.fill(255);
		c.noStroke();
		c.endDraw();

	}

	public void partition() {	
		
		int cue = 8600; //Curtain down
		if (stepTimeLine > cue && stepTimeLine < cue+300){
			p.seqStep = 1;
		}
		
		cue = 17200; //Curtain up devils
		if (stepTimeLine > cue && stepTimeLine < cue+300){
			p.seqStep = 2;
		}
		
		
		cue = 25800; //Stop
		if (stepTimeLine > cue && stepTimeLine < cue+100){
			p.seqStep = 3;
		}
		
		cue = 22800; //SUPER SCROLL + FADE OUT
		if (stepTimeLine2 > cue && stepTimeLine2 < cue+100){
			p.seqStep = 4;
		}
		/*
		cue = 36287; //0
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 5;
		}
		
		cue = 38287; //115000
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 6;
		}
		
		cue = 40000; //115000
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 7; 
		}
	*/
	}
	
	public void events() {
		
		//RANDOM STALL + CURTAIN UP
		if (p.seqStep == 0){
			
			if(hasRun != 1){
				stepTimeLineReset=timeLine;
				//brasse les cartes par rangŽs
				for(int i = 0; i < lettre.size(); i++){
					int counter4 = i / numPixelsWide;
					Lettre l = (Lettre) lettre.get(i);
					if(counter4 == 0) l.counter = 56;
					if(counter4 == 1) l.counter = 58;
					if(counter4 == 2) l.counter = 59;
					if(counter4 == 3) l.counter = 65;
					if(counter4 == 4) l.counter = 66;
					if(counter4 == 5) l.counter = 67;
					if(counter4 == 6) l.counter = 68;
					if(counter4 == 7) l.counter = 69;
					if(counter4 == 8) l.counter = 70;
					if(counter4 == 9) l.counter = 72;
					if(counter4 == 10) l.counter = 75;
					if(counter4 == 11) l.counter = 79;
					if(counter4 == 12) l.counter = 85;
					if(counter4 == 13) l.counter = 90;
					if(counter4 == 14) l.counter = 94;
					if(counter4 == 15) l.counter = 99;
					if(counter4 == 16) l.counter = 103;
					if(counter4 == 17) l.counter = 67;
					if(counter4 == 18) l.counter = 56;
					if(counter4 == 19) l.counter = 75;
					if(counter4 == 20) l.counter = 65;
					l.life = 255;
				}
				counterRemplissage=lettre.size()-1;	
				hasRun = 1;
			}
			digitalWall(-1, 0);
			
		}

		
		if (p.seqStep == 1){
			if(change[0].test(p.seqStep)){
				curtainMode = 1;
			}
			digitalWall(-1, 0);
		}

		//Devils + curtain up
		if (p.seqStep == 2){
			if(change[0].test(p.seqStep)){
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.counter = 5;
					l.life = 255;
				}
				curtainLow = p.height;
				curtainMode = 2;
			}
			
			//Celui du centre est diffŽrent
			int i = lettre.size()/2;
			Lettre l = (Lettre) lettre.get(i);
			l.counter = 22 ;

			digitalWall(1, 1);
		}

		
		if (p.seqStep == 3){
			//Tout le monde shake ensemble le meme numŽro
			if(change[0].test(p.seqStep)) {
				counter = 5;
				stepTimeLineReset2 = 0;
				stepTimeLineReset2 = timeLine;
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.counter = 5 ;
				}
			
			}
			int changer = 0;
			counterMax = 21;
			change[8].reset();
			if(stepTimeLine2 % 2150 == 0 ) {
				changer=1;
				//p.println(counter);
				if(change[8].test(changer)) {
					counter++;
				
					
					for (int i = 0; i < 100; i++){
						//oneLineShakeList[i] = (int) p.random(0,42);
						//oneLineShakeList2[i] = (int) p.random(0,80);
					}
				}
			}
		
			counter = (int) p.map(p.constrain(stepTimeLine2, 0, 17200),0,17200,5,21);
			
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			digitalWall(1, 1);
		}


		if (p.seqStep == 4){
			if(change[0].test(p.seqStep)){
				curtainHigh = 0;
				curtainLow = 0;
				curtainMode = 1;
				OscMessage myMessage2 = new OscMessage("/cueNum");
				myMessage2.add(9); /* add an int to the osc message */
				p.oscP5.send(myMessage2, p.soundComputer);
				myMessage2 = new OscMessage("/cueGo");
				myMessage2.add(1);
				p.oscP5.send(myMessage2, p.soundComputer);
			}
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			digitalWall(1, 1);
			
		}
		
	}
	
	public void oneLineShake(int horzVert, int whichLine, int numLettre) {
		//FONCTION FAIT SHAKER UNE LIGNE HOR OU VERT 

		int rand = (int)p.random(0, 100);
		int rand2 = (int)p.random(0, 100);
		int rand3 = (int)p.random(0, 100);
		int rand4 = (int)p.random(0, 100);
		
		switch (horzVert) {

		case 0 :

			for(int x = 0; x < numPixelsWide; x++){
				Lettre l = (Lettre) lettre.get(p.constrain(x+((whichLine)*numPixelsWide),0,lettre.size()));
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

		case 1:
			
			for(int y = 0; y < numPixelsHigh; y++){
				Lettre l = (Lettre) lettre.get(p.constrain((y*numPixelsWide)+whichLine,0,lettre.size()));
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


		case 3: //UN SEUL CHIFFRE CONTAMINE PROGRESSIVEMENT
			
			l = (Lettre) lettre.get(p.constrain(numPixelsWide/2+((numPixelsHigh/2)*numPixelsWide),0,lettre.size()));
			l.fillBypass = false;
			rand = (int)p.random(0, 100);
			rand2 = (int)p.random(0, 100);
			rand3 = (int)p.random(0, 100);
			rand4 = (int)p.random(0, 100);
			for(int x = 0; x < shakeContamineX; x++){
				//for(int y = 0; y < shakeContamineY; y++){
					l = (Lettre) lettre.get(p.constrain(lettre.size()/2+x,0,lettre.size())); //Map pour centrer l'opŽration
					
					if(l.id() == rand || l.id() == rand2 || l.id() == rand3 || l.id() == rand4){
						//Lettres ou chiffres
						
						if(numLettre == 0) {
							l.count(0,9);
						}
						if(x == shakeContamineX-1) {
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
			for(int x = 0; x > -shakeContamineX; x--){
					
					l = (Lettre) lettre.get(p.constrain(lettre.size()/2+x,0,lettre.size())); //Map pour centrer l'opŽration
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
				shakeContamineY++;
			}
			//shakeContamine=0;
			shakeContamineX = (int) p.constrain(shakeContamineX, 0, lettre.size()/2f); 
			shakeContamineY = p.constrain(shakeContamineY, 0, numPixelsWide/2);
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
						if(l.counter != isolateNumber)  l.count(0,21);
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
	
	public void wordScroller(int speed){
		
		int rand = (int)p.random(0, 100);
		int rand2 = (int)p.random(0, 100);
		int rand3 = (int)p.random(0, 100);
		int rand4 = (int)p.random(0, 100);
		if(speed < 1000){
			if(p.frameCount%p.constrain(speed, 1, 20) == 0) wordScroller++;
		} 
		
		if(wordScroller > 4000) wordScroller = 0;
		
		for(int i = 0; i< lettre.size(); i++){
			Lettre l = (Lettre) lettre.get(i);
			if(l.id() == rand || l.id() == rand2 || l.id() == rand3 || l.id() == rand4){
				l.count(0,48);
				l.lettre = (l.lettre+1);
				if(l.lettre > 122) l.lettre = 65;
				if(l.lettre > 90 && l.lettre < 97)  l.lettre = 97;
				l.life--;
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
		
		for(int i = lettre.size()-1; i > 0; i--){
			Lettre l = (Lettre) lettre.get(i);
			if(l.id() == rand || l.id() == rand2 || l.id() == rand3 || l.id() == rand4){
				l.count(0,48);
				l.lettre = (l.lettre+1);
				if(l.lettre > 122) l.lettre = 65;
				if(l.lettre > 90 && l.lettre < 97)  l.lettre = 97;
				l.life--;
			}
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
				
				if(mode == 1){ //COUNTING ALTERNATE
					l.lettre = text[i];
					l.afficheText(false,3,0,false);
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
		
			//counterRemplissage = p.constrain(counterRemplissage += numPixelsWide, 0,lettre.size());
			counterRemplissage = (int) p.map(stepTimeLine, 0, 8600, 0, lettre.size()); // CUE 1 et 2 ˆ Timing MATCH
			//counterRemplissage = counterRemplissage * numPixelsWide;
			counterRemplissage = p.constrain(counterRemplissage, 0,lettre.size());
		}
		
		if(direction == -1){
			c.hint(p.ENABLE_DEPTH_TEST);
			for(int i = lettre.size()-1; i>=counterRemplissage; i--){
				
				Lettre l = (Lettre) lettre.get(i);
			
				l.couleur(255);

				if(mode == 0){ //Normal Mode
					l.lettre = text[i];
					
					l.afficheText(false,0,0,false);
					l.life = 255;
				}

				if(mode == 2){ //wordScroller Mode
					if(i>wordScroller && i < wordScroller+10){
						l.lettre = text[i];
						l.afficheText(false,3,2,false);
						l.life = 255;
					} else l.afficheText(false,3,1,false);
				}
				counterRemplissage = (int) p.map(stepTimeLine, 0, 8600, lettre.size()-1, 0);
				//counterRemplissage = p.constrain(counterRemplissage +=4, 0,lettre.size());
				counterRemplissage = p.constrain(counterRemplissage, 0,lettre.size()-1);
			}
		}
		
		boolean completed;
		if(counterRemplissage == lettre.size()) completed = true;
		else completed = false;
		
		return completed;
	}
	
	public void curtain(int param){
		
		if(param > 0){
		
			//Ouvre de bas en haut
			if(param == 1){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					curtainLow = curtainLow + 2;
				}
			}
			
			//Ferme de haut en bas
			if(param == 2){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					curtainLow = curtainLow - 2;
				}
			}

			//ouvre de bas en haut
			if(param == 3){
			
				curtainLow = p.height;
				if(p.frameCount%2 == 0){
					curtainHigh = curtainHigh + 2;
				}
			}

			//Ouvre de haut en bas
			if(param == 4){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					curtainLow = curtainLow + 2;
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
	
	public void numberWorms( ){
		
		for (int j = 0; j < numPixelsHigh; j++) {
			for (int i = 0; i < numPixelsWide; i++) {
				if(i > wormX / blockSize -1  && i < wormX / blockSize + p.map(wormX, 0, p.width, 5, 20) && j > wormY / blockSize -1 && j < wormY / blockSize +100){
					
					Lettre l = (Lettre) lettre.get(numPixelsWide*j+i);
					l.counter = 0;
				}
			}
		}
		
		
		for (int j = 0; j < numPixelsHigh; j++) {
			for (int i = 0; i < numPixelsWide; i++) {
				if(i > wormX2 / blockSize - p.map(wormX2, 0, p.width, 20, 5) && i < wormX2 / blockSize && j > wormY / blockSize -1 && j < wormY / blockSize +100){
					
					Lettre l = (Lettre) lettre.get(numPixelsWide*j+i);
					l.counter = 0;
				}
			}
		}
		
		//wormX2=wormX2-6;
		

		
		/*
		 for (int j = 0; j < numPixelsHigh; j++) {
				for (int i = 0; i < numPixelsWide; i++) {
					if(i > p.mouseX / blockSize -10 && i < p.mouseX / blockSize +10 && j > p.mouseY / blockSize -10 && j < p.mouseY / blockSize +10){
						
						Lettre l = (Lettre) lettre.get(numPixelsWide*j+i);
						l.counter = 0;
					}
				}
			}
		*/
		 
	}
	
	public void keyPressed() {
	}
	
	public void mousePressed() {
		//counter = lettre.length;
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
			  
			  
			  int[] emoticons = { 56, 49, 58, 53, 59, 54, 65, 57, 66, 73, 67, 80, 68, 84, 69, 86, 70, 87, 72, 88, 75, 93, 85 };
			  
			  c.pushMatrix();
			  c.translate(coordX+20, coordY+28, coordZeased);
			  
			  if(rotateOnItself){
				  if(timeLine <= 124200){
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
				  c.text((char) (counter),0,0,0);
				  //pas content 49 53 (54diable) 57 73 80 84 86 87 88 93 
				  //content 56 58 59 65 66 67 68 69 70 72 75 79 85 90 94 99 103
				  //dŽbut 48
				 
			
			  }
			  
			  if(afficheQuoi == 1){
				  c.text(chiffreLettre, 0,0,0);
				 
			  }
			  
			  if(afficheQuoi == 2){
				  c.text((char)(counter+65), 0,0,0);
				
			  }
			  
			  if(afficheQuoi == 3){
				  c.text((char)(emoticons[counter]), 0,0,0);
				  //c.text((char)(counter), 0,0,0);
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