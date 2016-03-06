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



public class NUMBERS extends Sketch{
	
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
	
	public NUMBERS(DreamingNow p, PGraphicsOpenGL c) {

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
		
		p.shaderSelect = 0;
	
		c.textFont(p.font,12);
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
	    String rawText = "  Enter  establishes the structures of society, rules for social behavior, and the ceremonies performed to ensure continuity of life and land. The Dreaming governs the laws of community, cultural lore and how people are required to behave in their communities. The condition that is The Dreaming is met when people live according to law, and live the lore: perpetuating initiations and Dreaming transmissions or lineages, singing the songs, dancing the dances, telling the stories, painting the songlines and Dreamings.The Creation was believed to be the work of culture heroes who traveled across a formless land, creating sacred sites and significant places of interest in their travels. In this way songlines were established, some of which could travel right across Australia, through as many as six to ten different language groupings. The songs and dances of a particular songline were kept alive and frequently performed at large gatherings, organized in good seasons. Waugals (yellow triangles with a black snake in the centre are the official Bibbulmun Track trailmarkers between Kalamunda and Albany in Western Australia. The Noongar believe that the Waugal, or Wagyl, created the Swan River and is represented by the Darling scarp.In the Aboriginal world view, every event leaves a record in the land. Everything in the natural world is a result of the past, present and future actions of the archetypal beings, whose actions are continuously creating the world. Whilst Europeans consider these cultural ancestors to be mythical, many Aboriginal people believe in their present and future literal existence. The meaning and significance of particular places and creatures is wedded to their origin in the Dreaming, and certain places have a particular potency, which the Aborigines call its dreaming. In this dreaming resides the sacredness of the earth. For example, in Perth, the Noongar beli the forest the Darling Scarp is said to represent the body of a Wagyl Ð a serpent being that meandered over the land creating rivers, waterways and lakes. It is taught that the Wagyl created the Swan River. In another example, the Gagudju people of Arnhemland, for which Kakadu National Park is named, believe that the sandstone escarpment that dominates the park's landscape was created in the Dreamtime when Ginga (the crocodile-man) was badly burned during a ceremony and jumped into the water to save himself. He turned to stone and became the escarpment. The common theme in these examples and similar ones is that topographical features are either the physical embodiments of creator beings or are the results of their activity.In one version (there are many Aboriginal cultures), Altjira was a spirit of the Dreamtime; he created the Earth and then retired as the Dreamtime vanished, with the coming of Europeans. Alternative names for Altjira in other Australian languages include Alchera (Arrernte), Alcheringa, Mura-mura (Dieri), and Tjukurpa (Pitjantjatjara).The dreaming and travelling trails of the Spirit Beings are the songlines. The signs of the Spirit Beings may be of spiritual essence, physical remains such as petrosomatoglyphs of body impressions or footprints, amongst natural and elemental simulacrae. To cite an example, people from a remote outstation called Yarralin, which is part of the Victoria River region, venerate the spirit Walujapi as the Dreaming Spirit of the black-headed python. Walujapi carved a snakelike track along a cliff-face and left an impression of her buttocks when she sat establishing camp. Both these dreaming signs are still discernible. In the Wangga genre, the songs and dances express themes related to death and regeneration They are performed publicly with the singer composing from their daily lives or enter h and regeneration They are performed publicly with the singer composing from their daily lives or enter ";
	    for (int e = 0; e < rawText.length(); e++) {
	      text[e] = (int) rawText.charAt(e); // Split the line into chars
	     
	    }
	    p.println(rawText.length());
		p.println("NUMBERS");
		
	}

	public void draw() {
		super.draw();
		
		//p.println(minutes + " :" + seconds);
		p.blurSize = 9;
		c.beginDraw();
		c.resetShader();
		c.hint(p.ENABLE_DEPTH_TEST);
	    c.textFont(p.font,blockSize);
		c.background((int) 0);
		timeLine = p.timecodeSlave;
		partition();
		events();
	    c.fill(0,80);
		//c.rect(0,0,p.width,p.height);
		
		//c.fill(0);
		//float oscil =p.sin(((p.frameCount%100) / 100.f ) * 3.1416f) * 255f;
		//alpha =(int) oscil;
		
		
		//c.fill(0);
		//c.fill(0,255);
		//c.noStroke();

		
	//	p.mov.read();
		//p.mov.play();
		//p.mov.jump(33);
		//p.mov.loadPixels();
		//p.mov.speed(0.6f);
		//p.mov.pause();
		
		
		
	
		//c.tint(255,255);
		//c.image(p.mov,0,0,p.width+130,p.height); // l,image doit tre affichŽe aprs les pixel-textes
		
		
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
				
				//sl.affichePoint(false);	
				counter++;
			}
		}
		
		
		c.stroke(0);
		c.strokeCap(100);
		//c.point(p.mouseX, p.mouseY);
		curtain(curtainMode);
		c.endDraw();

	}

	public void partition() {	
		//counterRemplissage = lettre.size(); //uncomment si on veut fast fowarder
		int cue = 38500; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 1;
		}

		cue = 57600; //if you change, must change counterRemplissage interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 2;
		}
		
		cue = 86500; //bit early
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 3;
		}
		
		cue = 105800; 
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 4;
		}
		
		cue = 115000; //if you change, must change rotations interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 40;
		}
		
		cue = 124834; //if you change, must change rotations interpolation
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 50;
		}
		
		cue = 129609; //115000
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 51;
		}
		
		cue = 134417; //115000
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 7; 
		}
		
		cue = 144250; 
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 100;
		}
		
		cue = 153770; 
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 8;
		}
		//WAVES
		cue = 158675; 
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 11;
		}
		
		cue = 163505; 
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 110;
		}
	
		cue = 168235;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 11;
			wormCollisionSpeed = 6;
		}
		
		cue = 172980;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 110;
			wormCollisionSpeed = 8;
		}
		
		cue = 175455;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 11;
			wormCollisionSpeed = 10;
		}
		
		cue = 176725;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 110;
			wormCollisionSpeed = 12;
		}
		
		cue = 177895;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 11;
			wormCollisionSpeed = 12;
		}
		
		cue = 179065;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 110;
			wormCollisionSpeed = 12;
		}
		
		cue = 180280;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 11;
			wormCollisionSpeed = 14;
		}
		
		cue = 180915;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 110;
			wormCollisionSpeed = 16;
		}
		
		cue = 181500;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 11;
			wormCollisionSpeed = 18;
		}
	
		
		cue = 182000;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 110;
			wormCollisionSpeed = 20;
		}
		
		cue = 182290;
		if (timeLine > cue && timeLine < cue+100){
			//p.seqStep = 11;
			wormCollisionSpeed = 22;
		}
		
		
		//END WAVES
		cue = 182727;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 12;
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				l.easing = 0.03f;
				l.life = 0;
			}
		}

		cue = 182900;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 13;
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				l.life++;
			}
		}
		
		cue = 201874;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 14;//lettres
			
		}
		
		cue = 221002;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 15;//CURTAIN UP
		}

		cue = 240239;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 16;//ENTER
		}

		cue = 245000;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 17;//GO
		}
		
		cue = 249895;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 18;
		}

		cue = 259505;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 19;
		}
		
		cue = 264385;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 20;
		}
		
		cue = 269075;//283515
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 21;
		}
		
		cue = 273850;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 22;
		}
		
		cue = 288205;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 23;
		}
		
		
		cue = 295586;
		if (timeLine > cue && timeLine < cue+100){
			p.seqStep = 25;
		}
		
		cue = 307432;
		if (timeLine > cue && timeLine < cue+100) {
			p.seqStep = 27;
			
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
			
			counterRemplissage = (int) p.map(timeLine, 38500, 57600, 0, lettre.size()); // CUE 1 et 2 ˆ Timing MATCH
			counterRemplissage = p.constrain(counterRemplissage, 0,lettre.size());
			
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
	
	public void curtain(int param){
		
		if(param > 0){
		
			//Ouvre de bas en haut
			if(param == 1){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					curtainLow++;
				}
			}
			
			//Ferme de haut en bas
			if(param == 2){
				curtainHigh = 0;
				if(p.frameCount%2 == 0){
					curtainLow--;
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
	
	public void events() {
		
		if (p.seqStep == 1){
			if(change[0].test(p.seqStep)){
				//brasse les cartes!
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.counter = (int) p.random(0,9);
					l.life = 255;
				}
				counterRemplissage = 0;
				
			}
			digitalWall(1, 0);

		}

		if (p.seqStep == 2){
			//contentShake(0,0,10);
			//contentShake(0,0,10);
			//contentShake(0,0,10);
			if(change[0].test(p.seqStep)){
				curtainMode = 1;
			}
			digitalWall(1, 0);
		}
		
		if (p.seqStep == 3){
			//contentShake(0,0,10);
			//contentShake(0,0,10);
			if(change[0].test(p.seqStep)){
				curtainMode = 2;
			}
			digitalWall(1, 0);
		}
		
		
		if (p.seqStep == 4){
			//Shake the wall
			contentShake(0,0,10);
			contentShake(0,0,10);
			digitalWall(1, 0);
		}
		
		if (p.seqStep == 40){
			//Rotation
			contentShake(0,0,10);
			contentShake(0,0,10);
			digitalWall(1, 7);
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
			if(change[0].test(p.seqStep)) {
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.mapZ = 40;
					l.easing = 1f;
				}
			}
			if(p.frameCount%20 == 0){
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.easing = 0.1f;
					l.mapZ = l.mapZ+1;
					l.mapZ = p.constrain(l.mapZ, 0, 60);
				}
			}
			
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			digitalWall(1, 5);
			
		}

		if (p.seqStep == 81){

			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			digitalWall(1, 5);
			if(p.frameCount%20 == 0){
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.easing = 0.1f;
					l.mapZ = l.mapZ+1;
					//p.println(l.mapZ);
					l.mapZ = p.constrain(l.mapZ, 0, 100);
				}
			}
		}
		
		
		if (p.seqStep == 9){
			contentShake(0,0,10);
			contentShake(0,0,10);
			numberWorms();
			if(change[0].test(p.seqStep)){
				wormX = -200;
				wormX2 = p.width + 200;
			}
			
			
			//p.println(wormX);
			//if(wormX>= p.width/2 - 100){
			//	wormX = 0-2000;
			//}
			digitalWall(1,5);
			wormX= (int) (wormX+(4*1.2));
		}
		
		if (p.seqStep == 10){
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			numberWorms();
			if(change[0].test(p.seqStep)){
			wormX = p.width;
			
			}
			
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				l.mapMin = 255;
			}
			
			wormX2=(int) (wormX2-(4*1.2));
			
			digitalWall(1,5);
		}
		
		if (p.seqStep == 11){
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			numberWorms();
			if(change[0].test(p.seqStep)){
				wormX = 0;
				wormX2 = p.width;
				wormCollisionSpeed = 4 ;
				index = 0;
			}

			if(wormX>=(p.width/2-120)) {
				if(change[4].test(index)){
					for(int i = 0; i < lettre.size(); i++){
						Lettre l = (Lettre) lettre.get(i);
						//l.mapMin = 255;
						//l.mapMin=p.constrain(l.mapMin - 130,-5000,255);
					}
					//wormCollisionSpeed=(int) (wormCollisionSpeed*2);
					//wormCollisionSpeed = p.constrain(wormCollisionSpeed, 0 ,60);
				}	
			} else index++;
			wormX=wormX+wormCollisionSpeed;
			wormX2=wormX2-wormCollisionSpeed;


			if(wormX>= p.width/2 - 55){
				wormX = 0-2000;
			}

			if(wormX2<= p.width/2 + 55){
				wormX2 = p.width+2000;
			}
			
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				l.mapMin=p.constrain(l.mapMin - 3,-5000,255);
			}
			
			digitalWall(1,6);
			
		}

		if (p.seqStep == 110){
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			contentShake(0,0,10);
			numberWorms();
			if(change[0].test(p.seqStep)){
				wormX = 0;
				wormX2 = p.width;
				wormCollisionSpeed = 4 ;
				index = 0;
			}

			if(wormX>=(p.width/2-120)) {
				if(change[4].test(index)){
					for(int i = 0; i < lettre.size(); i++){
						Lettre l = (Lettre) lettre.get(i);
						//l.mapMin = 255;
						//l.mapMin=p.constrain(l.mapMin - 130,-5000,255);
					}
					//wormCollisionSpeed=(int) (wormCollisionSpeed*2);
					//wormCollisionSpeed = p.constrain(wormCollisionSpeed, 0 ,60);
				}	
			} else index++;
			wormX=wormX+wormCollisionSpeed;
			wormX2=wormX2-wormCollisionSpeed;


			if(wormX>= p.width/2 - 55){
				wormX = 0-2000;
			}

			if(wormX2<= p.width/2 + 55){
				wormX2 = p.width+2000;
			}
			
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				l.mapMin=p.constrain(l.mapMin - 2,-5000,255);
			}
			digitalWall(1,6);
		}


		if (p.seqStep == 12){
			
			if(change[0].test(p.seqStep)) {
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					if(l.counter == 0) l.counter = 0;
					else l.counter = (int) p.random(2,40);
					l.lifeEasing = 1f;
					if(l.counter == 0) {
						l.life=l.life+3;
					} else {
						l.life = 0;
						l.lifeEased = 0;
					}

				}
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.lifeEasing = 0.03f;
				}
				counterMax = 40;

			}
			
			//RŽduit le Z
			if(p.frameCount%30 == 0){
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.mapZ = l.mapZ-6;
					//l.life = 0;
					l.mapZ = p.constrain(l.mapZ, 0, 100);
				}
			}
			
			
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				if(l.counter == 0) {
					l.life=l.life+3;
				} else l.life = 0;
			}
			contentShake(0,5,10);
			digitalWall(1, 8);
			//l.life++;
		}

		if (p.seqStep == 13){
			//Replace tout le monde ˆ 0
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				l.coordZeased = 0;
				if(l.counter == 0) {
					l.life=l.life+3;
				} else l.life = 0;
				
			}
			contentShake(0,5,10);
			digitalWall(1, 8);
			if(p.frameCount%20 == 0) counterMax--;
			counterMax = p.constrain(counterMax,9,40);
		}
		
		if (p.seqStep == 14){
			
			//Affiche prog les lettres
			if(change[0].test(p.seqStep)) {
				shakeContamineX = 0;
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.coordZeased = 0;
				}
			}
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				if(l.counter == 0) {
					l.life =l.life+8;
				} else l.life = 0;
			}
			contentShake(1,5,10);
			contentShake(0,5,10);
			digitalWall(1, 4);
			shakeContamineX=shakeContamineX+3;
			shakeContamineX = p.constrain(shakeContamineX, 0, 1911);
		}


		if (p.seqStep == 15){
			contentShake(1,5,10);
			contentShake(0,5,10);
			
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				if(l.counter == 0) {
					l.life=l.life+8;
				} else l.life = 0;
			}
			
			if(change[0].test(p.seqStep)){
				curtainHigh = p.height;
				curtainMode = 5;
			}
			digitalWall(1, 4);
		}

		if (p.seqStep == 16){
			
			if(change[0].test(p.seqStep)){
				counterRemplissage = lettre.size();
				wordScroller = 0;
				wordScroller2 = numPixelsWide*numPixelsHigh;
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.counter = (int) p.random(0,9);
					l.life = 0;
					l.lifeEasing = 1f;
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
			}
			if(change[0].test(p.seqStep)){
				counterRemplissage = lettre.size();
				wordScroller = 0;
				wordScroller2 = numPixelsWide*numPixelsHigh;
				for(int i = 0; i < lettre.size(); i++){
					Lettre l = (Lettre) lettre.get(i);
					l.counter = (int) p.random(0,9);
					l.life = 0;
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
			wordScroller(1);
	
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
			wordScroller(1000);
			wordScrollerBackward(1000);

			contentShake(1,0,10);
			digitalWall(1, 2);
			
			if(timeLine <= 307432) {
				wordScroller = (int) p.map(timeLine, 295586, 307432, wordScrollerFinal, lettre.size()/2 - 6);
				wordScroller2= (int) p.map(timeLine, 295586, 307432, wordScrollerFinal2, lettre.size()/2 + 5);
			}
		}

		if (p.seqStep == 27){
			
			for(int i = 0; i < lettre.size(); i++){
				Lettre l = (Lettre) lettre.get(i);
				if(p.frameCount % 6 == 0 ) l.life --;
			}
			digitalWall(1, 2);
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
			  c.translate(coordX+6, coordY+10, coordZeased);
			  
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