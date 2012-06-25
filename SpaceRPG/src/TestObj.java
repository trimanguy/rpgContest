/**
 * @(#)TestObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/22
 */
 
import java.util.*;
import java.net.*;


public class TestObj extends Obj {
    
    double tang;
    double maxAngVel = 1;
    double velocity = 4;
    
    public void Step(){
    	if(angle == tang&& Math.random() > 0.997){
    		tang = Math.random()*360;
    	}
    	
    	double dang = (tang - angle);
    	if(dang < -180) dang += 360;
    	if(dang > 180) dang -= 360;
    	dang = Math.min(maxAngVel,Math.max(-maxAngVel,dang));
    	
    	rotate(dang);
    	int frame;
    	frame = (int) Math.round(angle/5)+1;
    	sprite.setFrame(frame);
    	
    	vx = velocity * Math.cos(angle/180*Math.PI);
    	vy = velocity * Math.sin(angle/180*Math.PI);
    	
    	move(vx,vy);
    	
    	Global.player.cx += vx;
    	Global.player.cy += vy;
    	
    }
    
    public TestObj(ArrayList<PointS> Pointss, String image) {
    	super(Pointss, image);
    }
    
    public TestObj(ArrayList<PointS> Pointss, String image, URL spritecontext) {
    	super(Pointss, image, spritecontext);
    }
}