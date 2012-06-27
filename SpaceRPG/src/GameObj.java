/**
 * @(#)GameObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/26
 */

import java.util.*;
import java.awt.geom.*;
import java.net.*;
import java.awt.image.*;
import java.awt.*;

public class GameObj extends Obj {

	//These are world coordinates in pixels
	//public double x=0;
	//public double y=0;
	
	ArrayList<HitCircle> hitCircles = new ArrayList(0);
	
	public boolean density = true;//Set to false if this object cannot collide
	public boolean passThroughTag; //Objects with the same passThroughTag will never register hits with eachother
	
	public double velX, velY; //X and Y components for velocity
	
    public GameObj(String image) {
    	if(image != null) {
	    	sprite = new Sprite(image, true);
	    	Init();
    	}
    }
    
    public GameObj(String image, URL spritecontext) {
    	if(image != null && spritecontext != null)
    	{
    		sprite = new Sprite(image, spritecontext, true);
	    	context = spritecontext;
	    	Init();
    	}
    }
    
    public HitCircle contains(PointS P){
    	double px = P.getX(), py = P.getY();
    	px -= x; py-= y;
    	
    	for(HitCircle O:hitCircles){
    		double d2 = (px-O.rx)*(px-O.rx)+(py-O.ry)*(py-O.ry);
    		if(d2 <= O.r2) return O;
    	}
    	
    	return null;
    }
    
    public HitCircle checkCollision(GameObj O){
    	if(!O.density || !this.density) return null;
    	if(O.passThroughTag == this.passThroughTag) return null;
    	
    	for(HitCircle A:hitCircles){
    		for(HitCircle B:O.hitCircles){
    			double d = (O.x-x+B.rx-A.rx)*(O.x-x+B.rx-A.rx)+(O.y-y+B.ry-A.ry)*(O.y-y+B.ry-A.ry);
    			d = Math.sqrt(d);
    			if(A.radius+B.radius>=d) return A;
    		}
    	}
    	return null;
    }
	
    public boolean CameraCanSee(){
    	if(Global.player == null) return false;
    	
    	double rx, ry;
    	rx = Math.abs(x - Global.player.cx);
    	ry = Math.abs(y - Global.player.cy);
    	
    	if(sprite != null){
    		rx -= sprite.frameX/2;
    		ry -= sprite.frameY/2;
    		if(rx < 0) rx = 0;
    		if(ry < 0) ry = 0;
    	}
    	
    	if(rx * Global.player.zoom > Global.view.sizex/2) return false;
    	if(ry * Global.player.zoom * Global.xyRatio > Global.view.sizey/2) return false;
    	return true;
    }
    
    public void rotateTowards(PointS coord){
    	
    	
    }
    
	public void rotate(double theta){
		angle += theta;
		
		while(angle >= 360) angle-=360;
		while(angle < 0) angle+=360;
		
		for(HitCircle O:hitCircles){
			double r,a;
			r = Math.sqrt(O.rx*O.rx+O.ry*O.ry);
			a = Math.atan2(O.ry,O.rx);
			a += theta*Math.PI/180;
			O.rx = r*Math.cos(a);
			O.ry = r*Math.sin(a);
		}
	}
	
    public void transform(){
    	AffineTransform transform = new AffineTransform();
    	
    	double rx, ry;
    	PointS R = (new PointS(x,y)).toScreen();
    	
    	rx = R.x; ry = R.y;
    	R = null;
    	
    	rx -= (sprite.frameX/2) * Global.player.zoom;
    	ry -= (sprite.frameY/2) * Global.player.zoom;
    	
    	transform.translate(rx, ry);
    	
    	transform.scale(dx * Global.player.zoom, dy * Global.player.zoom);
    	
    	sprite.setTransform(transform);
    }
    
    public void Draw(Graphics2D G, ImageObserver loc){
    	transform(); //Applies the object's transformations to the sprite
    	sprite.Draw(G,loc); //Draws the object's sprite
    }
    
    public boolean CheckClick(PointS clickedPt){
    	if(mouseOpacity == false) return false;
    	
    	PointS realPt = clickedPt.toWorld();
    	
    	//if the coordinates are not close to the sprite's bounding box... return false
		double dx = realPt.x - x;//Relative X
		double dy = realPt.y - y;//Relative Y
		realPt = null;
		
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
}