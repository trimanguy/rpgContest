/**
 * This is a physical object that is displayed on the game screen.
 *
 * Physically represented by a polygon, and graphically represented by a sprite.
 *
 * Provides functionality for collision detection with other polygons
 *
 * Note: Keep the AffineTransform for the Obj and the Sprite separate.
 * 
 * The Obj uses the transform() method to transform it's vertices.
 *     When a transformation is valid for whatever it's been used for, transform() is called
 *     to transform the vertices. This is a must for collision detection. Game Errors WILL occur 
 *     if collision detection is performed before the transformation.
 *
 * The sprite has no such feature, and cannot save transformations. 
 *     It depends on the AffineTransform object to keep track of all transformations, 
 *     and the deletion of the AffineTransform or inconsistent change of reference for the 
 *     AffineTransform will result in the incorrect rendering of the sprite.
 */

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.net.*;
import java.awt.geom.*;
import java.util.*;

public class Obj implements Comparable{
	
	public Sprite sprite;
	public Color color = Color.white;
	
	public URL context;
	
	public double x;
	public double y;
	public double currAngle;//Angle
	public double dx=1;//draw width (For scaling)
	public double dy=1;//draw height (For scaling)
	
	public double layer=3; //Drawing layer
	
	public boolean mouseOpacity = true; //Set to false if this object can never be clicked or dragged
	
	public Obj(){
		
	}
	
    public Obj(String image) {
    	if(image != null) {
	    	sprite = new Sprite(image, true);
	    	Init();
    	}
    }
    
    public Obj(String image, URL spritecontext) {
    	if(image != null && spritecontext != null)
    	{
    		sprite = new Sprite(image, spritecontext, true);
	    	context = spritecontext;
	    	Init();
    	}
    }
    
    public void Init(){
    	Global.state.activeObjs.add(this);
    	if(CameraCanSee()){
    		Global.view.addDrawObject(this);
    	}
    }
    
    public void setColor(Color C){
    	color = C;
    }
    
    public void setSprite(Sprite s){
    	sprite = s;
    }
    
	public void move(double nx, double ny){
		translate(nx+x,ny+y);
	}
	
	public void translate(double nx, double ny){
		x=nx;
		y=ny;
	}
	
	public void translate(Point2D PointS){
		double nx = PointS.getX();
		double ny = PointS.getY();
		x=nx;
		y=ny;
	}
	
	public void rotate(double theta){
		currAngle += theta;
		
		while(currAngle >= 360) currAngle-=360;
		while(currAngle < 0) currAngle+=360;
	}
	
	public void setAngle(double theta){
		double delta = theta-currAngle;
		rotate(delta);
	}
    
    public void transform(){
    	AffineTransform transform = new AffineTransform();
    	
    	transform.translate(x, y);
    	
    	transform.scale(dx, dy);
    	
    	sprite.setTransform(transform);
    }
    
    public void Draw(Graphics2D G, ImageObserver loc){
    	transform(); //Applies the object's transformations to the sprite
    	sprite.Draw(G,loc); //Draws the object's sprite
    }
	
    public int compareTo(Obj O){
    	//I couldn't get my mergesort to work right? I don't know.
    	//This is a workaround for that.
    	double l = layer;
    	double ol = O.layer;
    	if(ol > l) return -1;
    	if(ol < l) return 1;
    	return 0;
    }
    
    public int compareTo(Object O){
    	return O.toString().compareTo(this.toString());
    }
    
    public boolean CameraCanSee(){
    	return true;
    }
    
    public void Step(){ //This is what will be called by the gamestate every tick
    }
    
    public boolean CheckClick(PointS clickedPt){
    	if(mouseOpacity == false) return false;
    	
    	//if the coordinates are not close to the sprite's bounding box... return false
		double dx = clickedPt.x - x;//Relative X
		double dy = clickedPt.y - y;//Relative Y
		if(Math.abs(dx) > sprite.frameX/2) return false;
		if(Math.abs(dy) > sprite.frameY/2) return false;
		
		//Now from relative coordinates to local sprite coordinates...
		//Sprite coordinates, I believe, are relative to the top left corner of the sprite
		dx += sprite.frameX/2;
		dy = sprite.frameY/2 - dy;
		
		dx = Math.max(0,Math.min(dx,sprite.frameX-1));
		dy = Math.max(0,Math.min(dy,sprite.frameY-1));
		
		//Grab the RGBA
		int RGBA = sprite.img.getRGB((int)dx,(int)dy);
		int alpha = (RGBA  >> 24) & 0xFF;
		
		return (alpha > 0);
    }
    
    public double getAngle(Obj O){
    	double ang = Math.atan2(O.y-y,O.x-x);
    	ang *= 180/Math.PI;
    	if(ang < 0) ang += 360;
    	if(ang > 360) ang -= 360;
    	return ang;
    }
    public double getAngle(PointS P){
    	double ang = Math.atan2(P.y,P.x);
    	ang *= 180/Math.PI;
    	if(ang < 0) ang += 360;
    	if(ang > 360) ang -= 360;
    	return ang;
    }
    
    public void delete(){ //Note: Not guaranteed to clear this obj from memory
    	Global.state.activeObjs.remove(this);
    	Global.view.drawObjects.remove(this);
    	System.gc();
    }
}