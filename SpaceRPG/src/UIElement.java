
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.color.*;
import java.awt.geom.*;
import java.net.*;
import java.util.*;

public class UIElement extends Obj {
	
	//These are screen coordinates in pixels relative to the top left
	//public double x=0;
	//public double y=0;
	
	//public double layer = 20;
	
	public int mouseOpacity = 2;
	
	public Sprite overlay;
	public Sprite underlay;
	
	public UIElement parent;
	public ArrayList<UIElement> children = new ArrayList(0);
	
    public UIElement(double x, double y, String image, URL spritecontext) {
    	if(image != null && spritecontext != null)
    	{
    		sprite = new Sprite(image, spritecontext, false);
	    	context = spritecontext;
	    	
	    	//Init();
	    	Global.state.newObjBuffer.add(this);
    	}
    	
    	layer = 20;
    	
    	move(x,y);
    }
    
    public void MovePercent(double x, double y){
    	if(x > 1) x/= 100;
    	if(y > 1) y/= 100;
    	
    	translate(x*Global.view.sizex, y*Global.view.sizey);
    }
    
    
    public boolean CheckClick(PointS clickedPt){
    	if(mouseOpacity == 0) return false;
    	
    	//if the coordinates are not close to the sprite's bounding box... return false
		double dx = clickedPt.x - x;//Relative X
		double dy = clickedPt.y - y;//Relative Y
		if(dx < 0) return false;
		if(dy < 0) return false;
		if(dx > sprite.frameX) return false;
		if(dy > sprite.frameY) return false;
		
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
    
    public void Init(){
    	
    	//Global.state.activeObjs.add(this);
    	if(CameraCanSee()){
    		Global.view.addDrawObject(this);
    	}
    	
    }
    
    public void Step(){
    }
    
    public void removeChild(UIElement O){
    	children.remove(O);
    	if(O.parent == this) O.parent = null;
    }
    
    public void addChild(UIElement O){
    	O.setParent(this);
    }
    
    public void removeParent(){
    	if(parent == null) return;
    	
    	parent.children.remove(this);
    	parent = null;
    }
    
    public boolean setParent(UIElement O){
    	if(parent == O && parent != null) return true;
    	if(this == O) return true;
    	
    	if(children.contains(O)) return false;
    	
    	if(parent != null){
    		removeParent();
    	}
    	
    	parent = O;
    	O.children.add(this);
    	return true;
    }
    
    public void move(double nx,double ny){
    	super.move(nx,ny);
    	for(UIElement O:children){
    		O.translate(nx,ny);
    	}
    }
    
    public void translate(double nx, double ny){
    	super.translate(nx,ny);
    	for(UIElement O:children){
    		O.translate(nx,ny);
    	}
    }
    
    public void translate(Point2D PointS){
    	super.translate(PointS);
    	for(UIElement O:children){
    		O.translate(PointS);
    	}
    }
    
    public void rotate(double theta){
    	super.rotate(theta);
    	for(UIElement O:children){
    		O.rotate(theta);
    	}
    }
    
    public void setAngle(double theta){
    	super.rotate(theta);
    	for(UIElement O:children){
    		O.setAngle(theta);
    	}
    }
    
    public void transform(){
    	
    	AffineTransform transform = new AffineTransform();
    	
    	transform.translate(x, y);
    	
    	transform.scale(dx, dy);
    	
    	sprite.setTransform(transform);
    	if(overlay != null) overlay.setTransform(transform);
    	if(underlay != null) underlay.setTransform(transform);
    }
    
    public void Draw(Graphics2D G, ImageObserver loc){
    	transform(); //Applies the object's transformations to the sprite
    	if(underlay != null) underlay.Draw(G,loc);
    	sprite.Draw(G,loc); //Draws the object's sprite
    	if(overlay != null) overlay.Draw(G,loc);
    }
}