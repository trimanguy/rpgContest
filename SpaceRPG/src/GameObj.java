
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
	
	public double destAngle;//Destination angle for ShipObj and MissileObj
	
	public GameObj(){} //triman added
	
    public GameObj(String image) {
    	if(image != null) {
	    	sprite = new Sprite(image, true);
	    	
	    	//Init();
	    	Global.state.newObjBuffer.add(this);
    	}
    }
    
    public GameObj(String image, URL spritecontext) { 
    	if(image != null && spritecontext != null)
    	{
    		sprite = new Sprite(image, spritecontext, true);
	    	context = spritecontext;
	    	
	    	//Init();
	    	Global.state.newObjBuffer.add(this);
    	}
    }
    
    public HitCircle contains(PointS P){
    	double px = P.getX(), py = P.getY();
    	px -= x; py-= y;
    	
    	//System.out.println("# OF HIT CIRCLES: "+hitCircles.size());
    	
    	for(HitCircle O:hitCircles){
    		double d2 = (px-O.rx)*(px-O.rx)+(py-O.ry)*(py-O.ry);
    		//System.out.println("PROCESSING HIT DETECTION: "+d2);
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
    	double rotateTo = 0.0;
    	
		if (Math.toDegrees((Math.atan2(coord.y,coord.x ))) < 0){
			rotateTo = Math.toDegrees( Math.atan2(coord.y,coord.x) ) + 360;
		} else {
			rotateTo = Math.toDegrees( Math.atan2(coord.y,coord.x) );
		}
		
		destAngle = rotateTo;	
    	
    }
    
	public void rotate(double theta){
		currAngle += theta;
		
		while(currAngle >= 360) currAngle-=360;
		while(currAngle < 0) currAngle+=360;
		
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
    	/*
    	PointS realPt = clickedPt.toWorld();
    	return super.CheckClick(realPt);
    	*/
    	
    	clickedPt = clickedPt.toWorld();
    	
    	if(mouseOpacity == 0) return false;
    	
    	//if the coordinates are not close to the sprite's bounding box... return false
		double dx = clickedPt.x - x;//Relative X
		double dy = clickedPt.y - y;//Relative Y
		if(Math.abs(dx) > sprite.frameX/2) return false;
		if(Math.abs(dy) > sprite.frameY/2) return false;
		
		if(mouseOpacity == 2) return true;
		
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