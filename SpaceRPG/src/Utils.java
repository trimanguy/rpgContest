/**
 * @(#)Utils.java
 *
 *
 * @author 
 * @version 1.00 2012/7/2
 */

import java.io.*;
import java.util.*;
import java.util.Random; 
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Utils {
	
	/*** These vars just holdon to info while we read from file ***/
	//Shared vars
	static String name;
	static String image;
	static boolean saveFlag = false;
	static String descrip;
	static String tempSize;
	
	//ShipFile vars

	static String tempHitBoxes="";
	static String tempPylons="";
	static ArrayList<Pylon> pylons = new ArrayList(0);
	static ArrayList<HitCircle> hitboxes = new ArrayList(0);

	//ItemFile vars
	static String missile;	
	static String tempAccuracy;
	static String tempDamage;
	static String tempWeaponTurnSpeed;
	static String tempHealth;
	static String tempArmor;
	static String tempDelay;
	static String soundType;
	
	//EngineFile vars
	static String tempSpeed;
	static String tempAngSpeed;
	static String tempPwr;
	
	//ShieldFile vars
	static String tempShield;
	static String tempBoostCost;
	
	//PowerCoreFile vars
	static String tempRegen;
	
	static boolean activateAble=true;
	
	
	/*** We store parsed data in these hashtables ***/
	static Hashtable shipTable = new Hashtable();
	static Hashtable weaponTable = new Hashtable();
	static Hashtable engineTable = new Hashtable();
	static Hashtable powerCoreTable = new Hashtable();
	static Hashtable shieldTable = new Hashtable();
	
	static Hashtable transitionData = new Hashtable();
    
	public static BufferedImage imageToBufferedImage(Image im) {
		BufferedImage bi = new BufferedImage
			(im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(im, 0, 0, null);
		bg.dispose();
		return bi;
	}
	  
    public static void storeShipInfo(){
    }
    
    public static void parseShipFile(String filename){
    	StringBuilder contents = new StringBuilder();
    	
    	try {
    		InputStream is = Utils.class.getResourceAsStream(filename);
    		BufferedReader input = new BufferedReader(new InputStreamReader(is));
    		
    		
	    	try {
	    		String line = null;
	    		//for each line in the file
	    		while ((line=input.readLine()) != null){
	    			if(line.startsWith("#")){
	    				//System.out.print("Check1"+ System.getProperty("line.separator"));
	    				continue;
	    			}
	    			StringTokenizer st = new StringTokenizer(line,"\t");
	    			String identifier;
	    			
	    			while(st.hasMoreTokens()){
	    				identifier = st.nextToken();
	    				switch (identifier) {
	    					case "SNAME":		name = st.nextToken(); break;
	    					case "SMODEL":		image = st.nextToken(); break;
	    					case "HB":			tempHitBoxes += st.nextToken(); break;
	    					case "PYLONS":		tempPylons += st.nextToken(); break;
	    					case "DESCRIP":		descrip = st.nextToken(); break;//
	    					case "END":			saveFlag = true; break;
	    				}	
	    			}
	    			
	    			//we finished reading info for a ship; need to save it
	    			if (saveFlag){
	    				//System.out.print("Check3"+ System.getProperty("line.separator"));
	    				//convert stuff to correct datatypes then call constructor in shipObj
	    				//double speed = Double.valueOf(tempSpeed);
	    				//double angSpeed = Double.valueOf(tempAngSpeed);
	    				
	    				/*** This part for Hitboxes ***/
	    				//remove parens for use with tokenizer
	    				tempHitBoxes = tempHitBoxes.replace('(', ' ');
	    				tempHitBoxes = tempHitBoxes.replace(')', ' ');
	    				StringTokenizer ship_st = new StringTokenizer(tempHitBoxes);
	    				
	    				//for each token of form 1.0,1.0,1.0
	    				while(ship_st.hasMoreTokens()){    				
	    					StringTokenizer hitbox_st = new StringTokenizer(ship_st.nextToken(), ",");
	    					while(hitbox_st.hasMoreTokens()){
	    						String tag;
	    						double x;
	    						double y;
	    						double radius;
	    						
	    						tag=hitbox_st.nextToken();
	    						x=Double.valueOf(hitbox_st.nextToken());
	    						y=Double.valueOf(hitbox_st.nextToken());
	    						radius=Double.valueOf(hitbox_st.nextToken());
	    						HitCircle hc = new HitCircle(null,tag,x,y,radius);
	    						hitboxes.add(hc);
	    					}
	    				}
	    				//System.out.println("tempPylons before: "+tempPylons);
	    				/*** This part for Pylons ***/
	    				//remove parens for use with tokenizer
	    				tempPylons = tempPylons.replace('(', ' ');
	    				tempPylons = tempPylons.replace(')', ' ');
	    				StringTokenizer ship_st2 = new StringTokenizer(tempPylons);
	    					
	    					//System.out.println("pylon "+tempPylons);
	    				//for each token of form 1.0,1.0
						while(ship_st2.hasMoreTokens()){
							StringTokenizer pylon_st = new StringTokenizer(ship_st2.nextToken(), ",");
							while(pylon_st.hasMoreTokens()){
								String hbTag;
								String typeTag;
								double health;
								double x;
								double y;
								double centerAngle;
								double arcAngle;
								//double pylonAngSpeed;
								double screenX;
								double screenY;
								int size;
								String pylonImg="";
								hbTag = pylon_st.nextToken();
								typeTag = pylon_st.nextToken();
								health = Double.valueOf(pylon_st.nextToken());
								x = Double.valueOf(pylon_st.nextToken());
								y = Double.valueOf(pylon_st.nextToken());
								centerAngle = Double.valueOf(pylon_st.nextToken());
								arcAngle = Double.valueOf(pylon_st.nextToken());
								
								screenX = Double.valueOf(pylon_st.nextToken());
								screenY = Double.valueOf(pylon_st.nextToken());
								
								/*
								System.out.println("health: "+health);
								System.out.println("centerAng: "+centerAngle);
								System.out.println("arcAng: "+arcAngle);
								System.out.println("screenX: "+screenX);
								System.out.println("screenY: "+screenY);
								*/
								size = Integer.valueOf(pylon_st.nextToken());
								if(pylon_st.hasMoreTokens())
									pylonImg = pylon_st.nextToken();
								//convert x and y into polar coord
								double[] polar = cartesianToPolar(x,y); //radius,angle
								
								Pylon pl = new Pylon(null, hbTag, typeTag, health, polar[0],polar[1],centerAngle, arcAngle, screenX, screenY, size, pylonImg);

								pylons.add(pl);	
							}
						}
	    				
	    				ShipObj ship = new ShipObj(image,hitboxes,pylons,descrip); //remember to add pylon stuff to ship
	    				Utils.shipTable.put(Utils.name, ship);
	    				
	    				//clear vars for next ship
	    				name = "";
	    				image = "";
	    				tempHitBoxes = "";
	    				tempPylons = "";
	    				descrip = "";
	    				pylons = new ArrayList(0);
	    				hitboxes = new ArrayList(0);
	    				saveFlag = false;
	    				
	    			}
	    			
	    		}
	    	}
	    	finally {
	    		input.close();
	    	}
    	}
    	catch (IOException ex){
    		ex.printStackTrace();
    	}
    }
    
    public static void parseWeaponFile(String filename){
    	StringBuilder contents = new StringBuilder();
    	
    	try {
    		InputStream is = Utils.class.getResourceAsStream(filename);
    		BufferedReader input = new BufferedReader(new InputStreamReader(is));
    		
	    	try {
	    		String line = null;
	    		//for each line in the file
	    		while ((line=input.readLine()) != null){
	    			if(line.startsWith("#")){
	    				//System.out.print("Check1"+ System.getProperty("line.separator"));
	    				continue;
	    			}
	    			StringTokenizer st = new StringTokenizer(line, "\t");
	    			String identifier;
	    			
	    			while(st.hasMoreTokens()){
	    				identifier = st.nextToken();
	    				//System.out.print("Check2"+ System.getProperty("line.separator"));
	    				switch (identifier) {
	    					case "WNAME":			name = st.nextToken(); break;
	    					case "WMODEL":			image = st.nextToken(); break; //none for now, maybe use later for hangar screen
	    					case "WSOUND":			soundType = st.nextToken(); break;
	    					case "MISSILE":			missile = st.nextToken(); break;
	    					case "ACCR":			tempAccuracy = st.nextToken(); break;
	    					case "DAMAGE":			tempDamage = st.nextToken(); break;
	    					case "TURNSPD":			tempWeaponTurnSpeed = st.nextToken(); break; 
	    					case "HEALTH":			tempHealth = st.nextToken(); break;
	    					case "ARMOR":			tempArmor = st.nextToken(); break;
	    					case "DELAY":			tempDelay = st.nextToken(); break;
	    					case "SIZE":			tempSize = st.nextToken(); break;//
	    					case "DESCRIP":			descrip = st.nextToken(); break;//
	    					case "PASSIVE":			activateAble = false; break;//
	    					case "END":				saveFlag = true; break;
	    				}	
	    			}
	    			
	    			//we finished reading info for a weapon; need to save it
	    			if (saveFlag){
	    				//convert stuff to correct datatypes then call constructor in shipObj
	    				double accuracy = Double.valueOf(tempAccuracy);
	    				double health = Double.valueOf(tempHealth);
	    				double armor = Double.valueOf(tempArmor);
	    				double delay = Double.valueOf(tempDelay);
	    				double weaponTurnSpeed = Double.valueOf(tempWeaponTurnSpeed);
	    				int size = Integer.valueOf(tempSize);
	    				
	    				/*** This part for Missiles ***/
	    				//for token of form string,string,double,double,double,double
						StringTokenizer st_missile = new StringTokenizer(missile, ",");
						String img = st_missile.nextToken();
						String hitImg = st_missile.nextToken();
						int lifeTime = Integer.valueOf(st_missile.nextToken());
						double maxSpeed = Double.valueOf(st_missile.nextToken());
	    				double accel = Double.valueOf(st_missile.nextToken());
	    				double missileTurnSpeed = Double.valueOf(st_missile.nextToken());
	    				
	    				WeaponObj weapon = new WeaponObj(name,image,img,hitImg,soundType,lifeTime,maxSpeed,accel,missileTurnSpeed,accuracy,weaponTurnSpeed,
	    					tempDamage,health,armor,delay,size,descrip,activateAble); 
	    				Utils.weaponTable.put(Utils.name, weapon);
	    				
	    				//clear vars for next ship
	    				name = "";
	    				image = "";
	    				soundType = "";
	    				missile = "";
	    				tempAccuracy = "";
	    				tempDamage = "";
	    				tempWeaponTurnSpeed = "";
	    				tempHealth = "";
	    				tempArmor = "";
	    				tempDelay = "";
	    				tempSize = "";
	    				activateAble = true;
	    				descrip = "";
	    				saveFlag = false;
	    				
	    			}
	    		}
	    	}
	    	finally {
	    		input.close();
	    	}
    	}
    	catch (IOException ex){
    		ex.printStackTrace();
    	}
    }
    
    public static void parseEngineFile(String filename){
    	StringBuilder contents = new StringBuilder();
    	try {
    		InputStream is = Utils.class.getResourceAsStream(filename);
    		BufferedReader input = new BufferedReader(new InputStreamReader(is));
    		
	    	try {
	    		String line = null;
	    		//for each line in the file
	    		while ((line=input.readLine()) != null){
	    			if(line.startsWith("#")){
	    				continue;
	    			}
	    			StringTokenizer st = new StringTokenizer(line, "\t");
	    			String identifier;
	    			
	    			while(st.hasMoreTokens()){
	    				identifier = st.nextToken();
	    				switch (identifier) {
	    					case "ENAME":		name = st.nextToken(); break;
	    					case "EMODEL":		image = st.nextToken(); break; //none for now, maybe use later for hangar screen
	    					case "MAXSPD":		tempSpeed = st.nextToken(); break;
	    					case "ROTSPD":		tempAngSpeed = st.nextToken(); break;
	    					case "HEALTH":		tempHealth = st.nextToken(); break;
	    					case "ARMOR":		tempArmor = st.nextToken(); break;
	    					case "PWRREQ":		tempPwr = st.nextToken(); break;
	    					case "SIZE":		tempSize = st.nextToken(); break;//
	    					case "DESCRIP":		descrip = st.nextToken(); break;//
	    					case "PASSIVE":		activateAble = false; break;//
	    					case "END":			saveFlag = true; break;
	    				}	
	    			}
	    			
	    			//we finished reading info; need to save it
	    			if (saveFlag){
	    				//convert stuff to correct datatypes then call constructor in EngineObj
	    				double maxSpd = Double.valueOf(tempSpeed);
	    				double rotSpd = Double.valueOf(tempAngSpeed);
	    				double health = Double.valueOf(tempHealth);
	    				double armor = Double.valueOf(tempArmor);
	    				double powerReq = Double.valueOf(tempPwr);
	    				int size = Integer.valueOf(tempSize);
	    			
	    				EngineObj engine = new EngineObj(name,image,maxSpd,rotSpd,health,armor,powerReq,size,descrip);
	    					
	    				//stick it in EngineTable	
	    				//System.out.println("before");
	    				Utils.engineTable.put(Utils.name, engine);
	    				
	    				//clear vars for next engine
	    				name = "";
	    				image = "";
	    				tempSpeed = "";
	    				tempAngSpeed = "";
	    				tempHealth = "";
	    				tempArmor = "";
	    				tempPwr = "";
	    				tempRegen = "";
	    				tempSize = "";
	    				activateAble = true;
	    				descrip = "";
	    				saveFlag = false;	
	    			}
	    		}
	    	}
	    	finally {
	    		input.close();
	    	}
    	}
    	catch (IOException ex){
    		ex.printStackTrace();
    	}
    }
    
    public static void parsePowerCoreFile(String filename){
    	StringBuilder contents = new StringBuilder();
    	
    	try {
    		InputStream is = Utils.class.getResourceAsStream(filename);
    		BufferedReader input = new BufferedReader(new InputStreamReader(is));
    		
	    	try {
	    		String line = null;
	    		//for each line in the file
	    		while ((line=input.readLine()) != null){
	    			if(line.startsWith("#")){
	    				continue;
	    			}
	    			StringTokenizer st = new StringTokenizer(line, "\t");
	    			String identifier;
	    			
	    			while(st.hasMoreTokens()){
	    				identifier = st.nextToken();
	    				switch (identifier) {
	    					case "PNAME":		name = st.nextToken(); break;
	    					case "PMODEL":		image = st.nextToken(); break; //none for now, maybe use later for hangar screen
	    					case "POWER":		tempPwr = st.nextToken(); break;
	    					case "REGEN":		tempRegen = st.nextToken(); break;
	    					case "HEALTH":		tempHealth = st.nextToken(); break;
	    					case "ARMOR":		tempArmor = st.nextToken(); break;
	    					case "SIZE":		tempSize = st.nextToken(); break;//
	    					case "DESCRIP":		descrip = st.nextToken(); break;//
	    					case "END":			saveFlag = true; break;
	    				}	
	    			}
	    			
	    			//we finished reading info; need to save it
	    			if (saveFlag){
	    				//convert stuff to correct datatypes then call constructor 
	    				double health = Double.valueOf(tempHealth);
	    				double armor = Double.valueOf(tempArmor);
	    				double power = Double.valueOf(tempPwr);
	    				double regen = Double.valueOf(tempRegen);
	    				int size = Integer.valueOf(tempSize);
	    			
	    				PowerCoreObj powerCore = new PowerCoreObj(name,image,power,regen,health,armor,size,descrip);
	    					
	    				//stick it in PowerCoreTable	
	    				Utils.powerCoreTable.put(Utils.name, powerCore);
	    				
	    				//clear vars for next powerCore
	    				name = "";
	    				image = "";
	    				tempPwr = "";
	    				tempHealth = "";
	    				tempArmor = "";
	    				tempSize = "";
	    				descrip = "";
	    				saveFlag = false;	
	    			}
	    		}
	    	}
	    	finally {
	    		input.close();
	    	}
    	}
    	catch (IOException ex){
    		ex.printStackTrace();
    	}
    }
    
    public static void parseShieldFile(String filename){
    	StringBuilder contents = new StringBuilder();
    	
    	try {
    		InputStream is = Utils.class.getResourceAsStream(filename);
    		BufferedReader input = new BufferedReader(new InputStreamReader(is));
    		
	    	try {
	    		String line = null;
	    		//for each line in the file
	    		while ((line=input.readLine()) != null){
	    			if(line.startsWith("#")){
	    				continue;
	    			}
	    			StringTokenizer st = new StringTokenizer(line, "\t");
	    			String identifier;
	    			
	    			while(st.hasMoreTokens()){
	    				identifier = st.nextToken();
	    				switch (identifier) {
	    					case "SNAME":		name = st.nextToken(); break;
	    					case "SMODEL":		image = st.nextToken(); break; //none for now, maybe use later for hangar screen
	    					case "SAMT":		tempShield = st.nextToken(); break;
	    					case "RDELAY":		tempDelay = st.nextToken(); break;
	    					case "HEALTH":		tempHealth = st.nextToken(); break;
	    					case "ARMOR":		tempArmor = st.nextToken(); break;
	    					case "PWRREQ":		tempPwr = st.nextToken(); break;
	    					case "BSTCOST":		tempBoostCost = st.nextToken(); break;
	    					case "SIZE":		tempSize = st.nextToken(); break;//
	    					case "DESCRIP":		descrip = st.nextToken(); break;//
	    					case "END":			saveFlag = true; break;
	    				}	
	    			}
	    			
	    			//we finished reading info; need to save it
	    			if (saveFlag){
	    				//convert stuff to correct datatypes then call constructor 
	    				double shieldAmt = Double.valueOf(tempShield);
	    				double regenDelay = Double.valueOf(tempDelay);
	    				double boostCost = Double.valueOf(tempBoostCost);
	    				double health = Double.valueOf(tempHealth);
	    				double armor = Double.valueOf(tempArmor);
	    				double powerReq = Double.valueOf(tempPwr);
	    				int size = Integer.valueOf(tempSize);
	    			
	    				ShieldObj newShield = new ShieldObj(name,image,shieldAmt,regenDelay,boostCost,health,armor,powerReq,size,descrip);
	    					
	    				//stick it in PowerCoreTable	
	    				Utils.shieldTable.put(Utils.name, newShield);
	    				
	    				//clear vars for next powerCore
	    				name = "";
	    				image = "";
	    				tempShield = "";
	    				tempDelay = "";
	    				tempBoostCost = "";
	    				tempHealth = "";
	    				tempArmor = "";
	    				tempPwr = "";
	    				tempSize = "";
	    				descrip = "";
	    				saveFlag = false;	
	    			}
	    		}
	    	}
	    	finally {
	    		input.close();
	    	}
    	}
    	catch (IOException ex){
    		ex.printStackTrace();
    	}
    }
    
    
    /*** convert parsed hitbox string to arraylist ***/
    public static void convertHitboxes(String hitboxes){
    	
    }
    
    /*** Used for ship creation ***/
    public static ShipObj createShip(String shipName, String faction){
    	ShipObj newShip = createShip(shipName);
    	newShip.faction = faction;
    	return newShip;
    }
    
    public static ShipObj createShip(String shipName){
    	
    	ShipObj template = (ShipObj)Utils.shipTable.get(shipName);
    	ArrayList<HitCircle> copiedHitCircles = new ArrayList(0);
    	ArrayList<Pylon> copiedPylons = new ArrayList(0);
    	ShipObj newShip = new ShipObj(shipName,template.imageName, copiedHitCircles, copiedPylons, template.descrip, true);
    	
    	//copy over each HitCircle from hitCircles to copiedHitCircles
    	for(int x=0; x<(template.hitCircles.size()); x++){
    		HitCircle currTempHC = template.hitCircles.get(x); 
    		HitCircle newHitCirc = new HitCircle(newShip, currTempHC.tag, currTempHC.rx, currTempHC.ry, currTempHC.radius);
    	}
    	
    	for(Pylon P:template.pylons){
    		Pylon newPylon = new Pylon(newShip, P.tag, P.allowedType, P.baseHealth, P.polarRadius, P.polarAngle, P.centerAngle, P.arcAngle,
    			P.screenX, P.screenY, P.size, P.gui);
    	}
    	return newShip;
    }
    
    /*** Shield Creator ***/
    public static ShieldObj createShield(String shieldName){
    	ShieldObj template = (ShieldObj)Utils.shieldTable.get(shieldName);
    	ShieldObj newShield = new ShieldObj(template.name,template.model,template.maxShield,template.regenDelay,template.boostCost,
    		template.baseHealth,template.armor,template.shieldPowerConsumption,template.size,template.descrip);
    	return newShield;
    	
    }
    
    /*** PowerCore Creator ***/
    public static PowerCoreObj createPowerCore(String powerCoreName){
    	PowerCoreObj template = (PowerCoreObj)(Utils.powerCoreTable.get(powerCoreName));
    	PowerCoreObj newPowerCore = new PowerCoreObj(template.name,template.model,template.maxPower,template.regenRate,template.baseHealth,template.armor,template.size,template.descrip);
   		return newPowerCore;
   		
    }
    
    /*** Weapon Creator ***/
    public static WeaponObj createWeapon(String weaponName){
    	
    	WeaponObj template = (WeaponObj)Utils.weaponTable.get(weaponName);
    	String damage = ""+template.damageToShield+","+template.damageThruShield+","+template.damageToHull+","+template.damageArmorPiercing;
    	WeaponObj newWeapon = new WeaponObj(template.name, template.model, template.missileImg,template.missileHitImg,template.missileSoundType,template.missileLife,template.missileMaxSpeed,template.missileAcceleration,
    		template.missileTurnSpeed,template.angleSpread,template.turnSpeed,damage,template.baseHealth,template.armor,template.activateDelay,template.size,template.descrip,template.isActive);
    	
    	return newWeapon;
    }
    
    /*** Engine Creator ***/
    public static EngineObj createEngine(String engineName){
    	EngineObj template = (EngineObj)Utils.engineTable.get(engineName);
    	//System.out.println("got here "+engineTable.size());
    	EngineObj newEngine = new EngineObj(template.name, template.model,template.maxVelocity,template.maxAngVelocity,template.baseHealth,template.armor,template.maxPowerConsumption,template.size,template.descrip);
    	return newEngine;
    }
    
    /*** conversion assumes x and y are coords relative to 0,0 ***/
    public static double[] cartesianToPolar(double x,double y){
    	double[] polar = new double[2];
    	double radius = Math.sqrt(x*x + y*y);
    	double angle = Math.toDegrees(Math.atan2(y,x));
    	polar[0]=radius;
    	polar[1]=angle;
    	
    	return polar;
    }
    
    //Pick a number >= lower and < higher
    public static int randomNumberGen(int lower, int higher){ 
    	Random rand = new Random(); 
 		int pickedNumber = rand.nextInt(higher-lower) + lower; 
    	return pickedNumber;
    }
    
    public static double randomNumberGen(double lower, double higher){
    	return (higher-lower)*Math.random()+lower;
    }
    
   	//Used for game saving/loading
   	public static String filepath;
   
	public static String getDataFolder(){
		String folder = System.getProperty("user.home");
	   	String os = System.getProperty("os.name").toLowerCase();
	   
	   	if(os.contains("win")){
	    	if(os.contains("xp"))
	        folder += "\\Application Data\\SpaceRPG\\";
	    else if(os.contains("7") || os.contains("vista"))
	        folder += "\\AppData\\Roaming\\SpaceRPG\\";
	   	}
	   	else if(os.contains("mac"))
	      	folder += "/Library/Application Support/SpaceRPG/";
	      	
	    //System.out.println("folder: "+folder);
	   	return folder;
	}
   
   
  	public static void writeToFile(String filename, String text){
  		try{
  			File ourFolder = new File(Utils.filepath);
  			
  			if(!ourFolder.exists()){new File(Utils.filepath).mkdir();}
  			
		  	Writer output = null;
		 	File file = new File(Utils.filepath+filename+".txt");
		 	output = new BufferedWriter(new FileWriter(file));
		 	output.write(text);
		  	output.close();
		  	System.out.println("Your file has been written"); 
  		} 
  		catch(IOException e){
  		}
  	}
  	
  	public static void saveGame(String name){
  		String contents="";
  		GameState state = Global.state;
  		String nl = System.getProperty("line.separator"); 
  		contents+="NAME	"+state.playerName+nl;
  		contents+="MONEY	"+state.playerMoney+nl;
  		contents+="PROGR	"+state.playerProgress+nl;
  		contents+="SHIP	"+state.playerObj.name+nl;
  		for(Pylon P:state.playerObj.pylons){
  			if(P.equipped==null){contents+="PYLONS	null|"+nl;continue;}
  			
  			contents+="PYLONS	"+P.equipped.name+","+P.equipped.type;
  			if(P.crew!=null){
  				contents+=","+P.crew.name+","+P.crew.gender+","+P.crew.job+","+P.crew.level+","
  					+P.crew.gunnery+","+P.crew.accuracy+","+P.crew.efficiency+","
  						+P.crew.dmgControl+","+P.crew.calibration+","+P.crew.engineering+"|"+nl;
  			} else {
  				contents+="|"+nl;
  			}
  		}
  		for(ItemObj I:state.playerCargo){
  			contents+="CARGO	"+I.name+","+I.type+","+I.quantity+"|"+nl;
  		}
  		for(ItemObj I:state.playerVault){
  			contents+="VAULT	"+I.name+","+I.type+","+I.quantity+"|"+nl;
  		}
  		
  		
  		Utils.writeToFile(name+"'s savefile",contents);
  	}
  	
  	public static void loadGame(String filename){
  		
    	StringBuilder contents = new StringBuilder();
    	String money="";
    	String progress="";
    	String ship="";
    	String tempCargo="";
    	String tempVault="";
    	
    	
    	try {
    		BufferedReader input = new BufferedReader(new FileReader(filepath+filename));
	    	try {
	    		String line = null;
	    		//for each line in the file
	    		while ((line=input.readLine()) != null){
	    			if(line.startsWith("#")){
	    				continue;
	    			}
	    			//System.out.println("line "+line);
	    			StringTokenizer st = new StringTokenizer(line, "\t");
	    			String identifier;
	    			
	    			while(st.hasMoreTokens()){
	    				identifier = st.nextToken();
	    				switch (identifier) {
	    					case "NAME":		name = st.nextToken(); break;
	    					case "MONEY":		money = st.nextToken(); break; 
	    					case "PROGR":		progress = st.nextToken(); break;
	    					case "SHIP":		ship = st.nextToken(); break;
	    					case "PYLONS":		tempPylons += st.nextToken(); break;
	    					case "CARGO":		tempCargo += st.nextToken(); break;
	    					case "VAULT":		tempVault += st.nextToken();break;
	    				}	
	    			}
	    			
	    					
	    		}
	    		
	    		//convert strings to game info now
				Global.state.playerName=name;
				Global.state.playerMoney=Double.valueOf(money);
				Global.state.playerProgress=Double.valueOf(progress);
				Global.state.playerObj=Utils.createShip(ship,"player");

				StringTokenizer tempPylons_st = new StringTokenizer(tempPylons,"|");
				int pylonNum=0;
				while(tempPylons_st.hasMoreTokens()){
					String nextPylon=tempPylons_st.nextToken();
					if (nextPylon.equals("null")){
						continue;
					}
					StringTokenizer pylon_st = new StringTokenizer(nextPylon,",");
					String itemName = pylon_st.nextToken();
					String itemType = pylon_st.nextToken();
					Global.state.playerObj.pylons.get(pylonNum).equipItem(Utils.createItem(itemType,itemName));
					if(pylon_st.hasMoreTokens()){ //pretty much theres a crew member
						String cname=pylon_st.nextToken();
						String gender=pylon_st.nextToken();
						String job=pylon_st.nextToken();
						int lv=Integer.valueOf(pylon_st.nextToken());
						double gun = Double.valueOf(pylon_st.nextToken());
						double acc = Double.valueOf(pylon_st.nextToken());
						double eff = Double.valueOf(pylon_st.nextToken());
						double dmgControl = Double.valueOf(pylon_st.nextToken());
						double cali = Double.valueOf(pylon_st.nextToken());
						double eng = Double.valueOf(pylon_st.nextToken());
						Global.state.playerObj.pylons.get(pylonNum).crew=new CharacterObj(cname,gender,job,lv,gun,acc,eff,dmgControl,cali,eng);
					}
					pylonNum++;
				}	
				
				StringTokenizer tempCargo_st = new StringTokenizer(tempCargo,"|");
				while(tempCargo_st.hasMoreTokens()){
					String nextCargo = tempCargo_st.nextToken();
					StringTokenizer cargo_st = new StringTokenizer(nextCargo,",");
					String cname = cargo_st.nextToken();
					String type = cargo_st.nextToken();
					int quant = Integer.valueOf(cargo_st.nextToken());
					Global.state.playerCargo.add(Utils.createItem(type,cname));
					//System.out.println("added to cargo "+type+" " +cname);
				}
				
				StringTokenizer tempVault_st = new StringTokenizer(tempVault,"|");
				while(tempVault_st.hasMoreTokens()){
					String nextVault = tempVault_st.nextToken();
					StringTokenizer vault_st = new StringTokenizer(nextVault,",");
					String vname = vault_st.nextToken();
					String type = vault_st.nextToken();
					Global.state.playerVault.add(Utils.createItem(type,vname));
					//System.out.println("added to vault "+type+" " +vname);
				}
				    			
	    			
	    		//clear vars
	    		name = "";
	    		tempPylons = "";
	    	}
	    	
	    	finally {
	    		input.close();
	    	}
    	}
    	catch (IOException ex){
    		ex.printStackTrace();
    	}
    

  	}
  	
  	public static ItemObj createItem(String type, String name){
  		if(type.equals("Weapon")){
  			return Utils.createWeapon(name);
  		}
  		else if(type.equals("PowerCore")){
  			return Utils.createPowerCore(name);
  		}
  		else if(type.equals("Shield")){
  			return Utils.createShield(name);
  		}
  		else if(type.equals("Engine")){
  			return Utils.createEngine(name);
  		}
  		
  		return null;
  	}
  	
}
   
