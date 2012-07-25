/**
 * @(#)MissileObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/27
 */
 
import java.util.*;
import java.net.*;

public class MissileObj extends GameObj{
	
	String delImage; //Filepath to the "explode" image.
	
	GameObj source = null;
	GameObj target=null;
	double maxAngVel = 0.5;//Maximum angular velocity in Degrees per second.
	double maxVelocity = 2.5; //Maximum velocity in Pixels per second.
    double velocity = 2.5; //Current velocity in Pixels per second.
    double accel = 0; // Acceleration in Pixels per second per second. 
    //	Appropriate for actual missiles and rockets and torpedoes.
    
	double damageToShield;
	double damageThruShield;
	double damageToHull;
	double damageArmorPiercing;
    
    double timeLeft = 0; //how long the missle flies before it dies
    
    public MissileObj(String image, String killImage, URL spritecontext, GameObj shipObj, GameObj targetObj, 
    	double dmgTS, double dmgThS, double dmgTH, double dmgAP, 
    	double nx, double ny, double turnRate, double speed, double maxSpeed, double acceleration, double time,
    	double nAngle) {
    	
    	super(image, spritecontext);
    	
    	delImage = killImage;
    	
    	x = nx;
    	y = ny;
    	
    	source = shipObj;
    	currAngle = nAngle;
    	
    	target = targetObj;
    	timeLeft = time;
    	
    	damageToShield = dmgTS;
    	damageThruShield = dmgThS;
    	damageToHull = dmgTH;
    	damageArmorPiercing = dmgAP;
    	
    	maxAngVel = turnRate;
    	velocity = speed;
    	maxVelocity = maxSpeed;
    	accel = acceleration;
    }
    
    public void Init(){
    	
    	velocity = Math.max(0,Math.min(maxVelocity, velocity + accel * Global.state.dtt));
    	
    	velX = velocity * Math.cos(currAngle/180*Math.PI);
    	velY = velocity * Math.sin(currAngle/180*Math.PI);
    	
    	move(velX*Global.state.dtt,velY*Global.state.dtt);
    	
    	layer = 10;
    	
    	super.Init();
    }
    
    public void Step(){
    	if (timeLeft <= 0){
    		//if missle is out of time, delete it here
    		addDelete();
    	} else{
    		timeLeft -= Global.state.dtt;
        }
        
        
        PointS thisMissile = new PointS(this.x, this.y);
        //check to see if this missle hit any objects
        for(int x=0; x<(ShipObj.allShips.size()); x++){
        	if(ShipObj.allShips.get(x)==source) continue;
        	if(!ShipObj.allShips.get(x).isHostile(source)) continue;
        	HitCircle gotHit = ShipObj.allShips.get(x).contains(thisMissile);
        	
        	if (gotHit !=null) {
        		//NOTE: this is where you process damage logic, ie which hitbox got hit
        		if(delImage!=null){
	        		new AnimatedParticle(delImage, Global.codeContext, 
	        			0.05, this.x, this.y);
        		}
        		
        		addDelete();
        		return;
        	}
        }
    	
    	if (target!=null) {
    		//Calculate destAngle from missle to target
    		double destAngle=0.0;
    		destAngle = getAngle((Obj)target);
    		
    		rotate(findDeltaAng(destAngle));
    	}
    	
    	int frame;
    	frame = (int) Math.round(currAngle/5)+1;
    	sprite.setFrame(frame);
    	
    	velocity = Math.max(0,Math.min(maxVelocity, velocity + accel * Global.state.dtt));
    	
    	velX = velocity * Math.cos(currAngle/180*Math.PI);
    	velY = velocity * Math.sin(currAngle/180*Math.PI);
    	
    	move(velX*Global.state.dtt,velY*Global.state.dtt);
    	/*
    	Global.player.cx += velX;
    	Global.player.cy += velY;
    	*/
    }
    
    
    public MissileObj(String image) {
    	super(image);
    }
    
    public MissileObj(String image, URL spritecontext) {
    	super(image, spritecontext);
    }
    
    
    public double findDeltaAng(double targetAngle){
    	double deltaAng = (targetAngle - currAngle);
    	double maxDeltaAng = maxAngVel*Global.state.dtt;
    	
    	if(deltaAng < -180) deltaAng += 360;
    	if(deltaAng > 180) deltaAng -= 360;
    	deltaAng = Math.min(maxDeltaAng,Math.max(-maxDeltaAng,deltaAng));
    	return deltaAng;
    }
    
}