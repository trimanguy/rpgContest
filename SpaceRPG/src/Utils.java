/**
 * @(#)Utils.java
 *
 *
 * @author 
 * @version 1.00 2012/7/2
 */

import java.io.*;
import java.util.*;

public class Utils {
	
	/*** These vars just holdon to info while we read from file ***/
	//Shared vars
	static String name;
	static String image;
	static boolean saveFlag = false;
	static String descrip;
	
	//ShipFile vars
	static String tempSpeed;
	static String tempAngSpeed;
	static String tempHitBoxes;
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
	static String tempMass;
	static boolean activateAble;
	
	
	/*** We store parsed data in these hashtables ***/
	static Hashtable shipTable = new Hashtable();
	static Hashtable weaponTable = new Hashtable();
    
    public static void storeShipInfo(){
    }
    
    public static void parseShipFile(File filename){
    	StringBuilder contents = new StringBuilder();
    	
    	try {
    		BufferedReader input = new BufferedReader(new FileReader(filename));
	    	try {
	    		String line = null;
	    		//for each line in the file
	    		while ((line=input.readLine()) != null){
	    			if(line.startsWith("#")){
	    				//System.out.print("Check1"+ System.getProperty("line.separator"));
	    				continue;
	    			}
	    			StringTokenizer st = new StringTokenizer(line);
	    			String identifier;
	    			
	    			while(st.hasMoreTokens()){
	    				identifier = st.nextToken();
	    				//System.out.print("Check2"+ System.getProperty("line.separator"));
	    				switch (identifier) {
	    					case "SHIP_NAME":	name = st.nextToken(); break;
	    					case "SHIP_MODEL":	image = st.nextToken(); break;
	    					case "SPEED":		tempSpeed = st.nextToken(); break;
	    					case "ANG_SPEED":	tempAngSpeed = st.nextToken(); break;
	    					case "HITBOXES":	tempHitBoxes += st.nextToken(); break;
	    					case "PYLONS":		tempPylons += st.nextToken(); break;
	    					case "END":			saveFlag = true; break;
	    				}	
	    			}
	    			
	    			//we finished reading info for a ship; need to save it
	    			if (saveFlag){
	    				//System.out.print("Check3"+ System.getProperty("line.separator"));
	    				//convert stuff to correct datatypes then call constructor in shipObj
	    				double speed = Double.valueOf(tempSpeed);
	    				double angSpeed = Double.valueOf(tempAngSpeed);
	    				
	    				/*** This part for Hitboxes ***/
	    				//remove parens for use with tokenizer
	    				tempHitBoxes = tempHitBoxes.replace('(', ' ');
	    				tempHitBoxes = tempHitBoxes.replace(')', ' ');
	    				StringTokenizer ship_st = new StringTokenizer(tempHitBoxes);
	    				
	    				//for each token of form 1.0,1.0,1.0
	    				while(ship_st.hasMoreTokens()){    				
	    					StringTokenizer hitbox_st = new StringTokenizer(ship_st.nextToken(), ",");
	    					while(hitbox_st.hasMoreTokens()){
	    						double x;
	    						double y;
	    						double radius;
	    					
	    						x=Double.valueOf(hitbox_st.nextToken());
	    						y=Double.valueOf(hitbox_st.nextToken());
	    						radius=Double.valueOf(hitbox_st.nextToken());
	    						HitCircle hc = new HitCircle(null,x,y,radius);
	    						hitboxes.add(hc);
	    					}
	    				}
	    				System.out.println("tempPylons before: "+tempPylons);
	    				/*** This part for Pylons ***/
	    				//remove parens for use with tokenizer
	    				tempPylons = tempPylons.replace('(', ' ');
	    				tempPylons = tempPylons.replace(')', ' ');
	    				StringTokenizer ship_st2 = new StringTokenizer(tempPylons);
	    				System.out.println("tempPylons after: "+tempPylons);
	    					
	    				//for each token of form 1.0,1.0
						while(ship_st2.hasMoreTokens()){
							StringTokenizer pylon_st = new StringTokenizer(ship_st2.nextToken(), ",");
							while(pylon_st.hasMoreTokens()){
								double x;
								double y;
								double centerAngle;
								double arcAngle;
								double pylonAngSpeed;
								double screenX;
								double screenY;
								String pylonImg="";
								//System.out.print("Check4"+ System.getProperty("line.separator"));
								x = Double.valueOf(pylon_st.nextToken());
								y = Double.valueOf(pylon_st.nextToken());
								centerAngle = Double.valueOf(pylon_st.nextToken());
								arcAngle = Double.valueOf(pylon_st.nextToken());
								pylonAngSpeed = Double.valueOf(pylon_st.nextToken());
								screenX = Double.valueOf(pylon_st.nextToken());
								screenY = Double.valueOf(pylon_st.nextToken());
								pylonImg = pylon_st.nextToken();
								//convert x and y into polar coord
								double[] polar = cartesianToPolar(x,y); //radius,angle
								
								System.out.println("radius: "+ polar[0]+" angle: "+ polar[1]+" centerAngle: "+centerAngle+" arcAngle: "+arcAngle+" angSpeed: "+pylonAngSpeed
									+" screenX: "+screenX+" screenY: "+screenY+" pylonImg: "+pylonImg);
								
								Pylon pl = new Pylon(null, polar[0],polar[1],centerAngle, arcAngle, screenX, screenY, pylonImg);
								//Pylon(ShipObj source, double radius, double angle, double centerAngle, double arcAngle, double angSpeed)
								pylons.add(pl);
								
								
								System.out.println("pylons size: "+ pylons.size());
							}
						}
	    				
	    				
	    				ShipObj ship = new ShipObj(image,speed,angSpeed,hitboxes,pylons); //remember to add pylon stuff to ship
	    				Utils.shipTable.put(Utils.name, ship);
	    				
	    				//clear vars for next ship
	    				name = "";
	    				image = "";
	    				tempSpeed = "";
	    				tempAngSpeed = "";
	    				tempHitBoxes = "";
	    				tempPylons = "";
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
    	System.out.print("Check5"+ System.getProperty("line.separator"));
    	ShipObj test = (ShipObj)shipTable.get("flak1");
    	double test_speed = test.velocity;
    	System.out.print("shipTable[flak1]'s speed is: "+ test_speed+ System.getProperty("line.separator"));
    	System.out.print("hitCircles size: " + test.hitCircles.size() + System.getProperty("line.separator"));
    	double test_hitbox0 = test.hitCircles.get(0).rx;
    	double test_hitbox1 = test.hitCircles.get(0).ry;
    	double test_hitbox2 = test.hitCircles.get(0).radius;
    	System.out.print("shipTable[flak1]'s hitbox is: ("+ test_hitbox0 + test_hitbox1 + test_hitbox2 +")" + System.getProperty("line.separator"));
    }
    
    public static void parseWeaponFile(File filename){
    	StringBuilder contents = new StringBuilder();
    	
    	try {
    		BufferedReader input = new BufferedReader(new FileReader(filename));
	    	try {
	    		String line = null;
	    		//for each line in the file
	    		while ((line=input.readLine()) != null){
	    			if(line.startsWith("#")){
	    				//System.out.print("Check1"+ System.getProperty("line.separator"));
	    				continue;
	    			}
	    			StringTokenizer st = new StringTokenizer(line);
	    			String identifier;
	    			
	    			while(st.hasMoreTokens()){
	    				identifier = st.nextToken();
	    				//System.out.print("Check2"+ System.getProperty("line.separator"));
	    				switch (identifier) {
	    					case "WEAPON_NAME":		name = st.nextToken(); break;
	    					case "WEAPON_MODEL":	image = st.nextToken(); break; //none for now, maybe use later for hangar screen
	    					case "MISSILE":			missile = st.nextToken(); break;
	    					case "ACCURACY":		tempAccuracy = st.nextToken(); break;
	    					case "DAMAGE":			tempDamage = st.nextToken(); break;
	    					case "TURNSPEED":		tempWeaponTurnSpeed = st.nextToken(); break; 
	    					case "HEALTH":			tempHealth = st.nextToken(); break;
	    					case "ARMOR":			tempArmor = st.nextToken(); break;
	    					case "DELAY":			tempDelay = st.nextToken(); break;
	    					case "MASS":			tempMass = st.nextToken(); break;//
	    					case "DESCRIP":			descrip = st.nextToken(); break;//
	    					case "ACTIVATEABLE":	activateAble = true; break;//
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
	    				double mass = Double.valueOf(tempMass);
	    				
	    				/*** This part for Missiles ***/
	    				//for token of form string,string,double,double,double,double
						StringTokenizer st_missile = new StringTokenizer(missile, ",");
						String img = st_missile.nextToken();
						String hitImg = st_missile.nextToken();
						int lifeTime = Integer.valueOf(st_missile.nextToken());
						double maxSpeed = Double.valueOf(st_missile.nextToken());
	    				double accel = Double.valueOf(st_missile.nextToken());
	    				double missileTurnSpeed = Double.valueOf(st_missile.nextToken());
	    				
	    				WeaponObj weapon = new WeaponObj(img,hitImg,lifeTime,maxSpeed,accel,missileTurnSpeed,accuracy,weaponTurnSpeed,
	    					tempDamage,health,armor,delay,mass,descrip,activateAble); 
	    				Utils.weaponTable.put(Utils.name, weapon);
	    				
	    				//clear vars for next ship
	    				name = "";
	    				image = "";
	    				missile = "";
	    				tempAccuracy = "";
	    				tempDamage = "";
	    				tempWeaponTurnSpeed = "";
	    				tempHealth = "";
	    				tempArmor = "";
	    				tempDelay = "";
	    				tempMass = "";
	    				activateAble = false;
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
    public static ShipObj createShip(String shipName){
    	
    	ShipObj template = (ShipObj)Utils.shipTable.get(shipName);
    	ArrayList<HitCircle> copiedHitCircles = new ArrayList(0);
    	ShipObj newShip = new ShipObj(template.imageName, Global.codeContext, template.maxVelocity, template.maxAngVel, copiedHitCircles);
    	
    	//copy over each HitCircle from hitCircles to copiedHitCircles
    	for(int x=0; x<(template.hitCircles.size()); x++){
    		HitCircle currTempHC = template.hitCircles.get(x); 
    		HitCircle newHitCirc = new HitCircle(newShip, currTempHC.rx, currTempHC.ry, currTempHC.radius);
    		//newShip.hitCircles.add(newHitCirc);
    	} 
    	
    	for(int x=0; x<template.pylons.size();x++){
    		Pylon currTempPylon = template.pylons.get(x);
    		Pylon newPylon = new Pylon(newShip, currTempPylon.polarRadius, currTempPylon.polarAngle, currTempPylon.centerAngle, currTempPylon.arcAngle,
    			currTempPylon.screenX, currTempPylon.screenY, currTempPylon.gui);
    		//, currTempPylon.maxAngVel);
    	}
    	
    	return newShip;
    }
    
    /*** Used for weapon creation ***/
    public static WeaponObj createWeapon(String weaponName){
    	
    	WeaponObj template = (WeaponObj)Utils.weaponTable.get(weaponName);
    	String damage = ""+template.damageToShield+","+template.damageThruShield+","+template.damageToHull+","+template.damageArmorPiercing;
    	WeaponObj newWeapon = new WeaponObj(template.missileImg,template.missileHitImg,template.missileLife,template.missileMaxSpeed,template.missileAcceleration,
    		template.missileTurnSpeed,template.angleSpread,template.turnSpeed,damage,template.health,template.armor,template.activateDelay,template.mass,template.descrip,template.canActivate);
    	
    	return newWeapon;
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
    
   
}