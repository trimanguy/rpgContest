/**
 * @(#)MissileObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/27
 */
 
import java.util.*;
import java.net.*;
import java.io.*;

public class MissileObj extends GameObj{
	
	String delImage; //Filepath to the "explode" image.
	String hitSoundType; //what type of sound to play on hit
	
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
    
    double lastx;
    double lasty;
    
    public MissileObj(String image, String killImage, String hitSound, GameObj shipObj, GameObj targetObj, 
    	double dmgTS, double dmgThS, double dmgTH, double dmgAP, 
    	double nx, double ny, double turnRate, double speed, double maxSpeed, double acceleration, double time,
    	double nAngle) {
    	
    	super(image);
    	
    	delImage = killImage;
    	hitSoundType = hitSound;
    	
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
        for(int i=0; i<(ShipObj.allShips.size()); i++){
        	if(ShipObj.allShips.get(i)==source) continue;
        	if(!ShipObj.allShips.get(i).isHostile(source)) continue;
        	
        	ShipObj ship = ShipObj.allShips.get(i);
        	
        	if(ship.currCoreHealth <=0) continue;
        	
        	HitCircle gotHit = ship.contains(thisMissile);
        	
        	
        	
        	if (gotHit !=null) {
        		//NOTE: this is where you process damage logic, ie which hitbox got hit
        		ArrayList<Pylon> affected = new ArrayList(0);
        		
        		for(int j=0; j<((ShipObj)gotHit.source).pylons.size(); j++){ //make a list of all affected pylons
        			Pylon currPylon = ((ShipObj)gotHit.source).pylons.get(j);
        			//System.out.println("currPylon health: "+currPylon.realHealth);
        			//System.out.println("currPylon tag: "+currPylon.tag+" gotHit tag: "+gotHit.tag );
        			if( (currPylon.tag.equals(gotHit.tag) )&&(currPylon.realHealth>0)) { affected.add(currPylon);}
        		}
        		
        		//TODO: damage ship shield first
        		//damage effected pylon (if theres remaning dmg or shieldpierce dmg
        		if(affected.size()>0){
        			
        			Double h = Math.floor(Math.random()*affected.size());
        			int hit = h.intValue();
        			Pylon pylonHit = affected.get(hit);
        			
        			//check the shields for the pylon
        			
        			if(pylonHit.source==Global.state.playerObj){
        				new SoundObj(false, this.hitSoundType);
        			}
        			pylonHit.takeDamageFrom(this);
        			
        		} else { //damage the core if all pylons destroyed
        		
        			((ShipObj)gotHit.source).takeDmg(this.damageToHull+this.damageArmorPiercing);
        		}
        		
        		if(delImage!=null){
	        		new AnimatedParticle(delImage, 
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
    	
    	lastx = x;
    	lasty = y;
    	
    	move(velX*Global.state.dtt,velY*Global.state.dtt);
    }
    
    
    public MissileObj(String image) {
    	super(image);
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