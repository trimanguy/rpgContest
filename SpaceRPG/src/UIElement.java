
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
	
	public double layer = 20;
	
	public int mouseOpacity = 1;
	
	public UIElement parent;
	public ArrayList<UIElement> children = new ArrayList(0);
	
    public UIElement(double x, double y, String image, URL spritecontext) {
    	super(image,spritecontext);
    	move(x,y);
    	
    }
    
    public void MovePercent(double x, double y){
    	if(x > 1) x/= 100;
    	if(y > 1) y/= 100;
    	
    	translate(x*Global.view.sizex, y*Global.view.sizey);
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
    	if(parent == O) return true;
    	
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
}