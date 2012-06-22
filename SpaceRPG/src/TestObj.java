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
    double maxAngVel = 1.5;
    
    public void Step(){
    	if(angle == tang&& Math.random() > 0.985){
    		tang = Math.random()*360;
    	}
    	
    	double dang = (tang - angle);
    	if(dang < -180) dang += 360;
    	if(dang > 180) dang -= 360;
    	dang = Math.min(maxAngVel,Math.max(-maxAngVel,dang));
    	
    	rotate(dang);
    	int frame;
    	frame = (int) Math.floor(angle/5)+1;
    	sprite.setFrame(frame);
    	
    }
    
    public TestObj(ArrayList<PointS> Pointss, String image) {
    	super(Pointss, image);
    }
    
    public TestObj(ArrayList<PointS> Pointss, String image, URL spritecontext) {
    	super(Pointss, image, spritecontext);
    }
}