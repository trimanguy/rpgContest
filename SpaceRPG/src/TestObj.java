/**
 * @(#)TestObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/22
 */
 
import java.util.*;
import java.net.*;


public class TestObj extends GameObj {
    
    double tang;
    double maxAngVel = 1;
    double velocity = 2.5;
    
    public void Init(){
    	Global.state.activeObjects.add(this);
    	Global.state.playerObject = this;
    	if(CameraCanSee()){
    		Global.view.addDrawObject(this);
    	}
    }
    
    public void Step(){
    	/**
    	if(angle == tang&& Math.random() > 0.997){
    		tang = Math.random()*360;
    	}
    	
    	double dang = (tang - angle);
    	if(dang < -180) dang += 360;
    	if(dang > 180) dang -= 360;
    	dang = Math.min(maxAngVel,Math.max(-maxAngVel,dang));
    	
    	rotate(dang);
    	**/
    	int frame;
    	frame = (int) Math.round(angle/5)+1;
    	sprite.setFrame(frame);
    	
    	velX = velocity * Math.cos(angle/180*Math.PI);
    	velY = velocity * Math.sin(angle/180*Math.PI);
    	
    	move(velX,velY);
    	
    	Global.player.cx += velX;
    	Global.player.cy += velY;
    	
    }
    
    public TestObj(String image) {
    	super(image);
    }
    
    public TestObj(String image, URL spritecontext) {
    	super(image, spritecontext);
    }
}