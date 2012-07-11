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
	static String name;
	static String image;
	static String tempSpeed;
	static String tempAngSpeed;
	static String tempHitBoxes;
	static String tempPylons;
	static ArrayList<Pylon> pylons = new ArrayList(0);
	static ArrayList<HitCircle> hitboxes = new ArrayList(0);
	static boolean saveFlag = false;
		

	static Hashtable shipTable = new Hashtable();
	
    
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
	    				System.out.print("Check1"+ System.getProperty("line.separator"));
	    				continue;
	    			}
	    			StringTokenizer st = new StringTokenizer(line);
	    			String identifier;
	    			
	    			while(st.hasMoreTokens()){
	    				identifier = st.nextToken();
	    				System.out.print("Check2"+ System.getProperty("line.separator"));
	    				switch (identifier) {
	    					case "SHIP_NAME":	name = st.nextToken(); break;
	    					case "SHIP_MODEL":	image = st.nextToken(); break;
	    					case "SPEED":		tempSpeed = st.nextToken(); break;
	    					case "ANG_SPEED":	tempAngSpeed = st.nextToken(); break;
	    					case "HITBOXES":	tempHitBoxes = st.nextToken(); break;
	    					case "PYLONS":		tempPylons = st.nextToken(); break;
	    					case "END":			saveFlag = true; break;
	    				}	
	    			}
	    			
	    			//we finished reading info for a ship; need to save it
	    			if (saveFlag){
	    				System.out.print("Check3"+ System.getProperty("line.separator"));
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
	    				
	    				/*** This part for Pylons ***/
	    				//remove parens for use with tokenizer
	    				tempPylons = tempPylons.replace('(', ' ');
	    				tempPylons = tempPylons.replace(')', ' ');
	    				StringTokenizer ship_st2 = new StringTokenizer(tempPylons);
	    				System.out.println("tempPylons: "+tempPylons);
	    					
	    				//for each token of form 1.0,1.0
						while(ship_st2.hasMoreTokens()){
							StringTokenizer pylon_st = new StringTokenizer(ship_st2.nextToken(), ",");
							while(pylon_st.hasMoreTokens()){
								double x;
								double y;
								double centerAngle;
								double arcAngle;
								double pylonAngSpeed;
								//System.out.print("Check4"+ System.getProperty("line.separator"));
								x = Double.valueOf(pylon_st.nextToken());
								y = Double.valueOf(pylon_st.nextToken());
								centerAngle = Double.valueOf(pylon_st.nextToken());
								arcAngle = Double.valueOf(pylon_st.nextToken());
								pylonAngSpeed = Double.valueOf(pylon_st.nextToken());
								//convert x and y into polar coord
								double[] polar = cartesianToPolar(x,y); //radius,angle
								
								System.out.println("radius: "+ polar[0]+" angle: "+ polar[1]+" centerAngle: "+centerAngle+" arcAngle: "+arcAngle);
								//+" angSpeed: "+pylonAngSpeed);
								
								Pylon pl = new Pylon(null, polar[0],polar[1],centerAngle, arcAngle);
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
    		Pylon newPylon = new Pylon(newShip, currTempPylon.polarRadius, currTempPylon.polarAngle, currTempPylon.centerAngle, currTempPylon.arcAngle);
    		//, currTempPylon.maxAngVel);
    	}
    	
    	return newShip;
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