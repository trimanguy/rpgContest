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
    
    /*** Does computation and perfoms pewpew; deprecated, was used for testing ***/
    /*
    public void fireOn(ShipObj target, double missileSpeed, double inaccuracy){
    	
    	if(Global.state.time < fireTimer) return; //weapon cooling down
    	
    	if( (target==null)||(target==this) ) return; //Ship can't fire on itself
    	
    	//Compute the firing vector
    	double a,b,c,d,distance,t0,t1,t;
    	Vector2D V = new Vector2D(target.velX,target.velY);
    	Vector2D PO = new Vector2D(target.x-x,target.y-y);
    	Vector2D P = null;
    	
    	distance = PO.length;
    	
    	a = (V.dot(V))-(missileSpeed*missileSpeed);
    	b = PO.dot(V.multiply(2));
    	c = PO.dot(PO);
    	d = b*b - 4*a*c; //Doesn't this look familiar?
    	
    	if(d < 0) {
    		System.out.println("NO TARGET LEADING SOLUTION: "+d);
    		return; //No solution.
    	//d < 0 means that any bullet travelling at missileSpeed will never hit the target
    	
    	}
    	
    	t0 = (-b - Math.sqrt(d))/(2*a);
    	t1 = (-b + Math.sqrt(d))/(2*a);
    	
    	if(t0 < 0){ t = t1;
    	}else{
    		if(t1 < 0){ t = t0;
    		}else{
    			t = Math.min(t0,t1);
    		}
    	}
    	
    	P = PO.add(V.multiply(t));
    	
    	//Convert vector to angle in degrees
    	double fireAngle = P.toAngle();
    	
    	//Then create a missle shooting with that angle
    	//leaving the rate-of-fire stuff for a little later
    	
    	String image;
    	URL spriteContext;
    	
    	spriteContext = Global.codeContext;
    	image = "Resources/Sprites/PlasmaSmall.png";
    	String delImage = "Resources/Sprites/explode_2.png";
    	
    	MissileObj missile = new MissileObj(image,delImage,spriteContext,this,null,
	    	0, 0, 0, 0,
	    	this.x, this.y, 0,missileSpeed,missileSpeed,0,15);
	    	
    	missile.setAngle(fireAngle+ (Math.random()*2-1)*inaccuracy);
    	
    	fireTimer = Global.state.time + fireDelay;
    }
    */
    
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
    	double deltaAng = (targetAngle - currAngle);
    	double maxDeltaAng = maxAngVel*Global.state.dtt;
    	
    	if(deltaAng < -180) deltaAng += 360;
    	if(deltaAng > 180) deltaAng -= 360;
    	deltaAng = Math.min(maxDeltaAng,Math.max(-maxDeltaAng,deltaAng));
    	return deltaAng;
    }
    
    public void Step(){
    	//Combat 
    	//this.fireOn( this.findTarget() , 250, 0);
    	this.fireOn(this.findTarget());
    	
    	//Process power generation
    	
    	//Process shield actions
    	
    	//Pylon actions
    	
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
    		Global.player.cx += velX*Global.state.dtt;
    		Global.player.cy += velY*Global.state.dtt;
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