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
    //double destAngle;
    String imageName;
    double maxAngVel = 0.5;
    double velocity = 0;
    ShipObj aimTarget = null; //ship's target
    
    //both the timer and delay are in seconds.
    double fireTimer;
    double fireDelay = 0.25;
    
    /*** Ship Constructor, puts ship on screen ***/
    public ShipObj(String image, URL spritecontext, double speed, double maxAngVel, ArrayList<HitCircle> hitboxes) { 
    	if(image != null && spritecontext != null)
    	{
    		sprite = new Sprite(image, spritecontext, true);
	    	context = spritecontext;
	    	
	    	velocity = speed;
	    	maxAngVel = maxAngVel;
	    	hitCircles = hitboxes;
	    	allShips.add(this);
	    	Global.state.newObjBuffer.add(this);
    	}
    }
    
    /*** Ship Data Constructor, similar to C++ struct ***/
    public ShipObj(String image, double speed, double maxAngVel, ArrayList<HitCircle> hitboxes){
    	//don't need to add to allShips cuz this just template obj
    	imageName = image;
    	velocity = speed;
    	maxAngVel = maxAngVel;
    	hitCircles = hitboxes;	
    	
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
    
    /*** Does computation and perfoms pewpew ***/
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
    	
    	P = (new Vector2D(target.x-x,target.y-y)).add(V.multiply(t));
    	
    	//Convert vector to angle in degrees
    	double fireAngle = P.toAngle();
    	
    	//Then create a missle shooting with that angle
    	//leaving the rate-of-fire stuff for a little later
    	
    	String image;
    	URL spriteContext;
    	
    	spriteContext = Global.codeContext;
    	image = "Resources/Sprites/PlasmaSmall.png";
    	
    	MissileObj missile = new MissileObj(image,spriteContext,this,null,69,0,missileSpeed,missileSpeed,0,15);
    	missile.setAngle(fireAngle+ (Math.random()*2-1)*inaccuracy);
    	
    	fireTimer = Global.state.time + fireDelay;
    }
    
    /*** Find how much to rotate this step ***/
    public double findDeltaAng(){
    	double deltaAng = (destAngle - currAngle);
    	if(deltaAng < -180) deltaAng += 360;
    	if(deltaAng > 180) deltaAng -= 360;
    	deltaAng = Math.min(maxAngVel,Math.max(-maxAngVel,deltaAng));
    	return deltaAng;
    }
    
    public void Step(){
    	//Combat 
    	this.fireOn( this.findTarget() , 200, 0);
    	
    	//Ship Rotation
    	rotate( this.findDeltaAng() );
    	
    	//Setting Ship Frame
    	int frame;
    	frame = (int) Math.round(currAngle/5)+1;
    	sprite.setFrame(frame);
    	
    	//Ship Movement
    	velX = velocity * Math.cos(currAngle/180*Math.PI);
    	velY = velocity * Math.sin(currAngle/180*Math.PI);
    	move(velX*Global.state.dt/1000,velY*Global.state.dt/1000);
    	
    	//Move camera if Ship is Player's Ship
    	if (this == Global.state.playerObj){
    		Global.player.cx += velX*Global.state.dt/1000;
    		Global.player.cy += velY*Global.state.dt/1000;
    	}
    	
    	
    }
    
    public ShipObj(String image) {
    	super(image);
    }
    
    public ShipObj(String image, URL spritecontext) {
    	super(image, spritecontext);
    }
    
}