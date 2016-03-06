
package seq;

//import dreaming.*;
import java.util.ArrayList;
import processing.core.*;
import processing.video.*;
import oscP5.OscMessage;
import controlP5.ControlEvent;
import dreaming.DreamingNow;
//import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;
import toxi.physics2d.VerletParticle2D;



public class DIGITALWALL1 extends Sketch{
	
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
	Change[] change = new Change[11];
	int modulo = 100;
	int step = 0;
	int counterMax = 9;
	int hasRun = 0;
	
	PFont font;
	public DIGITALWALL1(DreamingNow p, PGraphicsOpenGL c) {

		super(p, c);
		className = this.getClass().getSimpleName();
		lettre = new ArrayList();
		for (int i = 0; i < change.length; i++) {
			change[i] = new Change();
		}
	}

	public void reinit() {

		super.reinit(); 
		p.skeletonTracking = false;
		p.kinectConnected = false;
		p.shaderSelect = 0;
		step = 0;
		lettre.clear();
		alpha = 0;
		//p.mov.jump(0);
		//p.mov.jump(33);
		for (int j = 0; j < numPixelsHigh; j++) {
		    for (int i = 0; i < numPixelsWide; i++) {
		    	lettre.add(new Lettre(0, i*blockSize, j*blockSize));
		    }
		  }
		
		
		c.textFont(p.wallFont,12);
		c.camera(p.width/2.0f, p.height/2.0f, (p.height/2.0f) / p.tan(p.PI*60.0f / 360.0f), p.width/2.0f, p.height/2.0f, 0, 0, 1, 0);
	
		wallFiller = 0;
		
	    //Change reset
	    for (int i = 0; i < change.length; i++) {
	    	change[i].reset();
	    }
	    curtainMode = 2;
		curtainHigh = 0 ;
		curtainLow = p.height;
		wordScroller = 0;
		counterMax = 9;
	    hasRun = 0;
		
		String rawText = "  Enter  establishes the structures of society, rules for social behavior, and the ceremonies performed to ensure continuity of life and land. The Dreaming governs the laws of community, cultural lore and how people are required to behave in their communities. The condition that is The Dreaming is met when people live according to law, and live the lore: perpetuating initiations and Dreaming transmissions or lineages, singing the songs, dancing the dances, telling the stories, painting the songlines and Dreamings.The Creation was believed to be the work of culture heroes who traveled across a formless land, creating sacred sites and significant places of interest in their travels. In this way songlines were established, some of which could travel right across Australia, through as many as six to ten different language groupings. The songs and dances of a particular songline were kept alive and frequently performed at large gatherings, organized in good seasons. Waugals (yellow triangles with a black snake in the centre are the official Bibbulmun Track trailmarkers between Kalamunda and Albany in Western Australia. The Noongar believe that the Waugal, or Wagyl, created the Swan River and is represented by the Darling scarp.In the Aboriginal world view, every event leaves a record in the land. Everything in the natural world is a result of the past, present and future actions of the archetypal beings, whose actions are continuously creating the world. Whilst Europeans consider these cultural ancestors to be mythical, many Aboriginal people believe in their present and future literal existence. The meaning and significance of particular places and creatures is wedded to their origin in the Dreaming, and certain places have a particular potency, which the Aborigines call its dreaming. In this dreaming resides the sacredness of the earth. For example, in Perth, the Noongar beli the forest the Darling Scarp is said to represent the body of a Wagyl Ð a serpent being that meandered over the land creating rivers, waterways and lakes. It is taught that the Wagyl created the Swan River. In another example, the Gagudju people of Arnhemland, for which Kakadu National Park is named, believe that the sandstone escarpment that dominates the park's landscape was created in the Dreamtime when Ginga (the crocodile-man) was badly burned during a ceremony and jumped into the water to save himself. He turned to stone and became the escarpment. The common theme in these examples and similar ones is that topographical features are either the physical embodiments of creator beings or are the results of their activity.In one version (there are many Aboriginal cultures), Altjira was a spirit of the Dreamtime; he created the Earth and then retired as the Dreamtime vanished, with the coming of Europeans. Alternative names for Altjira in other Australian languages include Alchera (Arrernte), Alcheringa, Mura-mura (Dieri), and Tjukurpa (Pitjantjatjara).The dreaming and travelling trails of the Spirit Beings are the songlines. The signs of the Spirit Beings may be of spiritual essence, physical remains such as petrosomatoglyphs of body impressions or footprints, amongst natural and elemental simulacrae. To cite an example, people from a remote outstation called Yarralin, which is part of the Victoria River region, venerate the spirit Walujapi as the Dreaming Spirit of the black-headed python. Walujapi carved a snakelike track along a cliff-face and left an impression of her buttocks when she sat establishing camp. Both these dreaming signs are still discernible. In the Wangga genre, the songs and dances express themes related to death and regeneration They are performed publicly with the singer composing from their daily lives or enter h and regeneration They are performed publicly with the singer composing from their daily lives or enter ";
	    for (int e = 0; e < rawText.length(); e++) {
	      text[e] = (int) rawText.charAt(e); // Split the line into chars
	     
	    }
	    c.smooth(2);
		c.noStroke();
		c.rectMode(p.CORNER);
		p.println("DIGITAL WALL 2");
		
	}

	public void draw() {
		super.draw();
		
		c.beginDraw();
		
		c.background((int) 0);
		//timeLine = p.timecodeSlave;
		partition();
		events();
		c.ambientLight(255, 255, 255);
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
		c.endDraw();

	}

	public void partition() {	
		//counterRemplissage = lettre.size(); //uncomment si on veut fast fowarder
		int cue = 0; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+300){
			p.seqStep = 1;
		}

		cue = 17757; //Scroll
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 2;
		}
		
		cue = 30911; //Stop
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 3;
		}
		
		cue = 35300; //SUPER SCROLL
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 4;
		}
		
		
		cue = 39872; //0
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 50;
		}
		
		cue = 39872; //115000
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 51;
		}
		
		cue = 43995; //115000
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 7; 
		}
		
		cue = 52542; 
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 8;
		}
		
		
	}
	
	public void events() {
		
		//RANDOM STALL + CURTAIN UP
		if (p.seqStep == 1){
			if(change[0].test(p.seqStep)){
				//brasse les cartes!
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.counter = (int) p.random(0,10);
					l.life = 255;
				}
				counterRemplissage = lettre.size();	
			}
			
			if(hasRun != 1){
				
				curtainLow = p.height;
				curtainMode = 2;
				hasRun = 1;
			}
			
			digitalWall(1, 0);
			curtain(curtainMode,0,17757);
		}

		if (p.seqStep == 2){
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			digitalWall(1, 0);
		}
		
		if (p.seqStep == 3){
			//contentShake(0,0,10);
			//contentShake(0,0,10);
			digitalWall(1, 0);
		}
		
		
		if (p.seqStep == 4){
			//Shake the wall
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			digitalWall(1, 0);
		}
		
		
		if (p.seqStep == 50){
			//Juste le 0
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				l.counter = 0;
			}
			digitalWall(1,1);
		}
		
		if (p.seqStep == 51){
			//Juste le 0 et le centre Roll
			if(change[0].test(p.seqStep)) {

				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.counter = 0;
				}

			}
			
			contentShake(0,2,10);
			
			digitalWall(1,0);
		}

		if (p.seqStep == 5){
			//Tout le monde shake ensemble le meme numŽro
			if(change[0].test(p.seqStep)) {
				counter = 1;
			}
			int changer = 0;
			change[8].reset();
			if(p.frameCount % 30 == 0 ) {
				changer=1;
				//p.println(counter);
				if(change[8].test(changer)) {
					counter++;
					counter = counter%10;
					for (int i = 0; i < 100; i++){
						oneLineShakeList[i] = (int) p.random(0,42);
						oneLineShakeList2[i] = (int) p.random(0,80);
					}
				}
			}

			
			contentShake(0,4,counter);
			contentShake(0,4,counter);
			//numberWorms();
			/*
			counter = p.constrain(counter,0,99);
			for (int i = 0; i < counter; i++){
				int coord = oneLineShakeList[i];
				int coord2 = p.constrain(coord - 1,0,numPixelsHigh);
				int coord3 = p.constrain(coord + 1,0,numPixelsHigh);
				oneLineShake(0,coord,0);
				oneLineShake(0,coord2,0);
				oneLineShake(0,coord3,0);


			}

			for (int i = 0; i < counter; i++){
				int coord = oneLineShakeList2[i];
				oneLineShake(1,coord,0);
				oneLineShake(1,coord,0);

			}
			 */
			digitalWall(1, 0);
		}



		if (p.seqStep == 6){
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				l.counter = 0;
			}
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			contentShake(0,2,10);
			digitalWall(1,1);
		} 
		
		if (p.seqStep == 7){
			if(change[0].test(p.seqStep)) {
				shakeContamineX = 0;
				shakeContamineY = 0;
			}
			contentShake(0,3,10);
			contentShake(0,3,10);
			contentShake(0,3,10);
			contentShake(0,3,10);
			digitalWall(1, 0);
		}
		
		if (p.seqStep == 8){
			
			if(change[0].test(p.seqStep)){
				curtainHigh = 0;
				curtainLow = 0;
				curtainMode = 1;
			}
			contentShake(0,3,10);
			contentShake(0,3,10);
			contentShake(0,3,10);
			contentShake(0,3,10);
			digitalWall(1,1);
			curtain(curtainMode,52542,69750);
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
	
	public void curtain(int param, int start, int end){
		
		if(param > 0){
		
			//Ouvre de bas en haut
			if(param == 1){
				curtainHigh = 0;
				curtainLow = (int) p.map(p.constrain(timeLine,start,end), start, end, 0, p.height);
			}
			
			//Ferme de haut en bas
			if(param == 2){
				curtainHigh = 0;
				curtainLow = (int) p.map(p.constrain(timeLine,start,end), start, end, p.height, 0);		
			}

			//ouvre de bas en haut
			if(param == 3){
			
				curtainLow = p.height;
				if(p.frameCount%2 == 0){
					curtainHigh++;
					//p.println("allo");
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

			c.pushMatrix();
			c.translate(coordX+7, coordY+14, coordZeased);

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
				//c.text(counter,0,0,0);
				c.text(counter,0,0,0);
				//c.fill(counter*20);
				//c.rectMode(p.CENTER);
				//c.rect(0,0,12,12);
				//c.rectMode(p.CORNER);
			}

			if(afficheQuoi == 1){
				c.text(chiffreLettre, 0,0,0);
				c.rect(0,0,12,12);
			}

			if(afficheQuoi == 2){
				c.text((char)(counter+65), 0,0,0);
				c.rect(0,0,12,12);
			}

			if(afficheQuoi == 3){
				c.text((char)lettre, 0,0,0);
				c.rect(0,0,12,12);
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