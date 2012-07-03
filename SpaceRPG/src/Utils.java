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
	static ArrayList<HitCircle> hitboxes = new ArrayList(0);
	static boolean saveFlag = false;
		

	static Hashtable shipTable = new Hashtable();
	
	/*** Used for ship creation ***/
    public static void createShip(){
    	
    }
    
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
	    					case "END":			saveFlag = true; break;
	    				}	
	    			}
	    			
	    			//we finished reading info for a ship; need to save it
	    			if (saveFlag){
	    				System.out.print("Check3"+ System.getProperty("line.separator"));
	    				//convert stuff to correct datatypes then call constructor in shipObj
	    				double speed = Double.valueOf(tempSpeed);
	    				double angSpeed = Double.valueOf(tempAngSpeed);
	    				
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
	    				
	    				ShipObj ship = new ShipObj(image,speed,angSpeed,hitboxes);
	    				Utils.shipTable.put(Utils.name, ship);
	    				
	    				//clear vars for next ship
	    				name = "";
	    				image = "";
	    				tempSpeed = "";
	    				tempAngSpeed = "";
	    				tempHitBoxes = "";
	    				hitboxes = new ArrayList(0);
	    				saveFlag = false;
	    				System.out.print("Check4"+ System.getProperty("line.separator"));
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
    
    
}