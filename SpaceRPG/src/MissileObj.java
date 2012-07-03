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
	
	GameObj source = null;
	GameObj target=null;
	double maxAngVel = 0.5;
	double maxVelocity = 2.5;
    double velocity = 2.5;
    double accel = 1;
    int damage = 0;
    double timeLeft = 0; //how long the missle flies before it dies
    
    public MissileObj(String image, URL spritecontext, GameObj shipObj, GameObj targetObj, 
    	int dmg, double turnRate, double speed, double maxSpeed, double acceleration, double time) {
    	
    	super(image, spritecontext);
    	
    	x = shipObj.x;
    	y = shipObj.y;
    	
    	source = shipObj;
    	
    	target = targetObj;
    	timeLeft = time;
    	damage = dmg;
    	maxAngVel = turnRate;
    	velocity = speed;
    	maxVelocity = maxSpeed;
    	accel = acceleration;
    }
    
    public void Init(){
    	
    	velX = velocity * Math.cos(currAngle/180*Math.PI);
    	velY = velocity * Math.sin(currAngle/180*Math.PI);
    	
    	move(velX*Global.state.dt/1000,velY*Global.state.dt/1000);
    	
    	layer = 10;
    	
    	super.Init();
    }
    
    public void Step(){
    	if (timeLeft <= 0){
    		//if missle is out of time, delete it here
    		addDelete();
    	} else{
    		timeLeft -= Global.state.dt/1000;
        }
        
        
        PointS thisMissile = new PointS(this.x, this.y);
        //check to see if this missle hit any objects
        for(int x=0; x<(ShipObj.allShips.size()); x++){
        	if(ShipObj.allShips.get(x)==source) continue;
        	
        	if (ShipObj.allShips.get(x).contains(thisMissile) !=null) {
        		//NOTE: this is where you process damage logic, ie which hitbox got hit
        		
        		new AnimatedParticle("Resources/Sprites/explode_2.png", Global.codeContext, 
        			0.05, this.x, this.y);
        		
        		
        		addDelete();
        		return;
        	}
        }
    	
    	if (target!=null) {
    		//Calculate destAngle from missle to target
    		//Ant: make getAngle in Obj!!
    		double destAngle=0.0;
    		destAngle = getAngle((Obj)target);
    	
    		//Then rotate missle towards target
    		double deltaAng = (destAngle - currAngle);
    		if(deltaAng < -180) deltaAng += 360;
    		if(deltaAng > 180) deltaAng -= 360;
    		deltaAng = Math.min(maxAngVel,Math.max(-maxAngVel,deltaAng));
    	
    		rotate(deltaAng);
    	}
    	
    	int frame;
    	frame = (int) Math.round(currAngle/5)+1;
    	sprite.setFrame(frame);
    	
    	velX = velocity * Math.cos(currAngle/180*Math.PI);
    	velY = velocity * Math.sin(currAngle/180*Math.PI);
    	
    	move(velX*Global.state.dt/1000,velY*Global.state.dt/1000);
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
    
    
}