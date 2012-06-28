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
    
    double destAngle;
    double maxAngVel = 0.5;
    double velocity = 0;
    ShipObj aimTarget = null;
    
    public ShipObj(String image, URL spritecontext, double speed) { 
    	if(image != null && spritecontext != null)
    	{
    		sprite = new Sprite(image, spritecontext, true);
	    	context = spritecontext;
	    	Init(speed);
    	}
    }
    
    public void Init(double speed){
    	velocity = speed;
    	Global.state.activeObjs.add(this);
    	//Global.state.playerObj = this;
    	if(CameraCanSee()){
    		Global.view.addDrawObject(this);
    	}
    }
    
    public ShipObj findTarget(){
    	//autotarget if no aimTarget or aimTarget too far
    	if (aimTarget==null){
    		//TODO: autotarget, do auto-targetting here
    		return null;
    	} else {
    		//clicked-target
    		return aimTarget;
    	}
    }
    
    public void fireOn(ShipObj target){
    	//First find angle to shoot towards
    	
    	//Then create a missle shooting with that angle
    	//leaving the rate-of-fire stuff for a little later
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
    	this.fireOn( this.findTarget() );
    	
    	//Ship Rotation
    	rotate( this.findDeltaAng() );
    	
    	//Setting Ship Frame
    	int frame;
    	frame = (int) Math.round(currAngle/5)+1;
    	sprite.setFrame(frame);
    	
    	//Ship Movement
    	velX = velocity * Math.cos(currAngle/180*Math.PI);
    	velY = velocity * Math.sin(currAngle/180*Math.PI);
    	move(velX,velY);
    	
    	//Move camera if Ship is Player's Ship
    	if (this == Global.state.playerObj){
    		Global.player.cx += velX;
    		Global.player.cy += velY;
    	}
    }
    
    public ShipObj(String image) {
    	super(image);
    }
    
    public ShipObj(String image, URL spritecontext) {
    	super(image, spritecontext);
    }
    
}