/**
 * @(#)Vector2D.java
 *
 *
 * @author 
 * @version 1.00 2012/6/28
 */


public class Vector2D {
	
	double x;
	double y;
	double length;
	
    public Vector2D(double nx, double ny) {
    	x = nx;
    	y = ny;
    	length = Math.sqrt(x*x+y*y);
    }
    
    public Vector2D add(Vector2D A){
    	return new Vector2D(A.x+x,A.y+y);
    }
    public Vector2D subtract(Vector2D A){
    	return new Vector2D(x-A.x,y-A.y);
    }
    public double dot(Vector2D A){
    	return A.x*x+A.y*y;
    }
    public Vector2D multiply(double a){
    	return new Vector2D(x*a,y*a);
    }
    
    public Vector2D multiply(Vector2D A){
    	return new Vector2D(A.x*x,A.y*y);
    }
    
    public Vector2D unit(){
    	return multiply(1/length);
    }
    
    public double toAngle(){
    	double a = Math.atan2(y,x)*180/Math.PI;
    	if(a < 0) a += 360;
    	if(a >= 360) a -= 360;
    	return a;
    }
    
}