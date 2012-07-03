/**
 * @(#)HitCircle.java
 *
 *
 * @author 
 * @version 1.00 2012/6/26
 */
import java.util.*;

public class HitCircle {
	GameObj source;
	
	double rx,ry; //The hitcircles should only keep track of relative coordinates, to keep things simple.
	double radius;
	double r2;
	
	String tag;//My thought is you assign a tag to a pylon and then a tag to the associated hitCircles
	
	
    public HitCircle(GameObj s, double nx, double ny, double r) {
    	rx = nx; ry = ny;
    	source = s;
    	if(s != null) s.hitCircles.add(this);
    	radius = r;
    	r2 = r*r;
    }
    
    public void addSource(GameObj s){
    	source = s;
    }
    
}