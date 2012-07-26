/**
 * @(#)ShipObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/22
 */
 
import java.util.*;
import java.net.*;


public class ShipObj extends GameObj {
    static ArrayList<ShipObj> allShips = new ArrayList(0);
	ArrayList<Pylon> pylons = new ArrayList(0);
	
    //double destAngle;
    String imageName;
    double maxAngVel = 10; // Degrees per second
    double velocity = 0; //Pixels per second
    double maxVelocity;
    ShipObj aimTarget = null; //ship's target
    
    //both the timer and delay are in seconds.
    double fireTimer;
    double fireDelay = 1;//25;
    
    //shield health!
    double shieldForward;
    double shieldLeft;
    double shieldRight;
    double shieldRear;
    double shieldChargeTimer;
    
	//ship's faction, defaults to pirate
	String faction = "pirate";
	
	//AI related fields
	boolean hasAI = true;
	double minIdealRange = 600;
	double maxIdealRange = 800;
	double tweenFactor = 0.05;
	double idealTargetAng = 0;
    
    public double getSpeed(){
    	return this.velocity;
    }
    
    /*** Ship Constructor, puts ship on screen ***/
    public ShipObj(String image, URL spritecontext, double maxSpeed, double maxAngVel, ArrayList<HitCircle> hitboxes) { 
    	if(image != null && spritecontext != null)
    	{
    		sprite = new Sprite(image, spritecontext, true);
	    	context = spritecontext;
	    	
	    	velocity = maxSpeed;
	    	maxVelocity = maxSpeed;
	    	this.maxAngVel = maxAngVel;
	    	hitCircles = hitboxes;
	    	allShips.add(this);
	    	Global.state.newObjBuffer.add(this);
	    	size = 32;
    	}
    }
    
    /*** Ship Data Constructor, similar to C++ struct ***/
    public ShipObj(String image, double maxSpeed, double maxAngVel, ArrayList<HitCircle> hitboxes, ArrayList<Pylon> newPylons){
    	//don't need to add to allShips cuz this just template obj
    	imageName = image;
    	maxVelocity = maxSpeed;
    	this.maxAngVel = maxAngVel;
    	hitCircles = hitboxes;
    	pylons = newPylons;	
    	
    }
    
    public ShipObj findTarget(){
    	//autotarget if no aimTarget or aimTarget too far
    	
    	if (aimTarget==null){
    		//TODO: autotarget, do auto-targetting here
    		return null;
    	} else {
    		//if we already have target, continue to fire on it
    		return aimTarget;
    	}
    }
    
    public boolean isHostile(GameObj target){
    	//hostility stuff has to be processed here?
    	return this.faction != ((ShipObj)target).faction;
    }
    
    public void fireOn(ShipObj target){
    	for(int x = 0; x<pylons.size(); x++){
    		Pylon currPylon = pylons.get(x);
    		if (currPylon.type == "Weapon"){
    			currPylon.setTarget = target;
    		}
    	}
    }
    
    /*** Find how much to rotate this step ***/
    public double findDeltaAng(double targetAngle){
    	if(targetAngle >= 360) targetAngle -= 360;
    	if(targetAngle <0) targetAngle += 360;
    	double deltaAng = (targetAngle - currAngle);
    	double maxDeltaAng = maxAngVel*Global.state.dtt;
    	
    	if(deltaAng < -180) deltaAng += 360;
    	if(deltaAng > 180) deltaAng -= 360;
    	deltaAng = Math.min(maxDeltaAng,Math.max(-maxDeltaAng,deltaAng));
    	return deltaAng;
    }
    
    public void takeDmg(double amount){
    	this.currCoreHealth -= amount;
    	/*
    	if (this.currCoreHealth<=0){
    		this.pylons=null;
    		this.delete();
    	}
    	*/
    }
    
    public void Step(){
    	//Ship alive?
    	if(this.currCoreHealth>0){
			
    	
    	
    	//Combat 
    	//this.fireOn( this.findTarget() , 250, 0);
    	this.fireOn(this.findTarget());
    	
    	//Process power generation
    	
    	//Process shield actions
    	
    	//Pylon actions
    	/*
    	for(Pylon P:pylons){
    		P.Step();
    	}
    	*/
    	
    	for(int x=0;x<this.pylons.size();x++){
    		pylons.get(x).Step();
    	}
    	
    	//Ship Rotation
    	rotate( this.findDeltaAng(destAngle) );
    	
    	//Setting Ship Frame
    	int frame;
    	frame = (int) Math.round(currAngle/5)+1;
    	sprite.setFrame(frame);
    	
    	//Ship Movement
    	velX = velocity * Math.cos(currAngle/180*Math.PI);
    	velY = velocity * Math.sin(currAngle/180*Math.PI);
    	move(velX*Global.state.dtt,velY*Global.state.dtt);
    	
    	//Move camera if Ship is Player's Ship
    	if (this == Global.state.playerObj){
    		Global.player.cx=x; //+= velX*Global.state.dtt;
    		Global.player.cy=y; // += velY*Global.state.dtt;
    	}
    	
    	for(Obj O:Global.state.activeObjs){
    		if(!density) break;
    		if(!(O instanceof ShipObj)) continue;
    		if(O == this) continue;
    		
    		GameObj object = (GameObj) O;
    		
    		if(!object.density) continue;
    		
    		if(getDistance(object).length <=size + object.size){
    		//if(checkCollision(object)!=null){
    			//push eachother out of the way
    			
    			double radii = object.size+size;
    			
    			//first get the unit vector between the two objects
    			Vector2D difPos = this.getDistance(object);
    			Vector2D unit = difPos.unit();
    			
    			//then apply the push
    			move(-radii * tweenFactor * tweenFactor * unit.x, -radii*tweenFactor * unit.y);
    			
    			O.move(radii * tweenFactor * tweenFactor * unit.x, radii*tweenFactor * unit.y);
    		}
    	}
    	
    	}
    }
    
    public ShipObj(String image) {
    	super(image);
    }
    
    public ShipObj(String image, URL spritecontext) {
    	super(image, spritecontext);
    }
    
    public double getShields(HitCircle H){//This will return the ship's shields that affects the Hitcircle object
    	double shield=0;
    	
    	PointS point = new PointS(x+H.rx,y+H.ry);
    	
    	double deltaAngle = getAngle(point) - currAngle;
    	if(deltaAngle >= 360) deltaAngle -= 360;
    	if(deltaAngle < 0) deltaAngle += 360;
    	
    	if(deltaAngle >=45 && deltaAngle <135) shield = shieldLeft;
    	if(deltaAngle >=135 && deltaAngle < 225) shield = shieldRear;
    	if(deltaAngle >=225 && deltaAngle < 315) shield = shieldRight;
    	if(deltaAngle < 45 || deltaAngle > 315) shield = shieldForward;
    	
    	return shield;
    }
    
    public void setShields(double value, HitCircle H){
    	value = Math.max(0,value);
    	PointS point = new PointS(x+H.rx,y+H.ry);
    	
    	double deltaAngle = getAngle(point) - currAngle;
    	if(deltaAngle >= 360) deltaAngle -= 360;
    	if(deltaAngle < 0) deltaAngle += 360;
    	
    	if(deltaAngle >=45 && deltaAngle <135) shieldLeft = value;
    	if(deltaAngle >=135 && deltaAngle < 225) shieldRear = value;
    	if(deltaAngle >=225 && deltaAngle < 315) shieldRight = value;
    	if(deltaAngle < 45 || deltaAngle > 315) shieldForward = value;
    }
}