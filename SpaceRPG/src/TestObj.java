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
    
    public void Step(){
    	
    	rotate(-1);
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