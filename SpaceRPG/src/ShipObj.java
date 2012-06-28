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
    
    double targetAng;
    double maxAngVel = 0.5;
    double velocity = 2.5;
    
    public void Init(){
    	Global.state.activeObjs.add(this);
    	Global.state.playerObj = this;
    	if(CameraCanSee()){
    		Global.view.addDrawObject(this);
    	}
    }
    
    public void Step(){
    	
    	double deltaAng = (targetAng - angle);
    	if(deltaAng < -180) deltaAng += 360;
    	if(deltaAng > 180) deltaAng -= 360;
    	deltaAng = Math.min(maxAngVel,Math.max(-maxAngVel,deltaAng));
    	
    	rotate(deltaAng);
    	
    	int frame;
    	frame = (int) Math.round(angle/5)+1;
    	sprite.setFrame(frame);
    	
    	velX = velocity * Math.cos(angle/180*Math.PI);
    	velY = velocity * Math.sin(angle/180*Math.PI);
    	
    	move(velX,velY);
    	
    	Global.player.cx += velX;
    	Global.player.cy += velY;
    	
    }
    
    public ShipObj(String image) {
    	super(image);
    }
    
    public ShipObj(String image, URL spritecontext) {
    	super(image, spritecontext);
    }
}