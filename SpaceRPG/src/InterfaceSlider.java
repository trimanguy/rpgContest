/**
 * @(#)InterfaceSlider.java
 *
 *
 * @author 
 * @version 1.00 2012/7/2
 */

import java.net.URL;
import java.awt.event.MouseEvent;
import java.lang.reflect.*;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Color;

public class InterfaceSlider extends UIElement{
	
	double low,high;
	double height;
	
	double powerPosition = 0.75;
	double position; //Decimal from low to hi
	
	Object callContext;
	String callMethod;
	
	boolean drawPosition = true;
	
	Sprite slider;
	
	int dir = 1;//1 for north, 2 for east, 3 for south, 4 for west
	
    public InterfaceSlider(double x, double y, String image,String slideImage, String cM, Object cC, 
    	double lo, double hi, double h) {
    	super(x,y,image);
    	
    	if(slideImage != null){
    		slider = new Sprite(slideImage, false);
    	}
    	
    	low = lo;
    	high = hi;
    	height = h;
    	//String cM, Obj cC
    	callContext = cC;
    	callMethod = cM;
    }
    
    public void setPosition(double yCoord){
    	yCoord -= y+height;
    	double npos = -yCoord/(height*dy);
    	
    	npos = Math.min(1,Math.max(0,npos));
    	if(npos != position){
    		position = npos;
    		callMethod();
    	}
    }
    
    public void setValue(double nValue){
    	nValue = Math.min(1,Math.max(0,nValue));
    	
    	if(position != nValue){
	    	position = nValue;
	    	callMethod();
    	}
    }
    
    public void mouseClicked(MouseEvent e){
    	if(e.getButton()==1){
	    	double my = e.getY();
	    	setPosition(my);
    	}
    }
    
    public void mouseDropped(MouseEvent e){
    	if(e.getButton()==1){
	    	double my = e.getY();
	    	setPosition(my);
    	}
    }
    
    public void callMethod(){
		
		if(callMethod == null || callMethod == "") {
			return;
		}
		if(callContext == null) {
			return;
		}
		
		
		Method method=null;
		try {
		  	method = callContext.getClass().getMethod(callMethod,double.class);
		} catch (SecurityException e) {
		 	// ...
		} catch (NoSuchMethodException e) {
			// ...
		}
		try {
			method.invoke(callContext,position);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
    }
    
    public void sliderTransform(double ry){
    	if(slider == null) return;
    	if(sprite == null) return;
    	
    	AffineTransform transform = new AffineTransform();
    	
    	double rx = x;
    	
    	rx -= (slider.img.getWidth()-sprite.img.getWidth())/2-1;
    	ry -= (slider.img.getHeight())/2;
    	
    	transform.translate(rx, ry);
    	transform.scale(dx,dy);
    	
    	slider.setTransform(transform);
    }
    
    public void transform(){
    	super.transform();
    	sliderTransform((1-position) * height * dy + y);
    }
    
    public void Draw(Graphics2D G, ImageObserver loc){
    	super.Draw(G,loc);
    	
    	if(drawPosition){
    		//draw the rectangle
	    	G.setColor(getColor());
	    	G.fillRect((int)(x*dx)+1,(int)Math.ceil(y+dy*height*(1-position)-2),
	    		(int) (sprite.img.getWidth()*dx)-2,(int)Math.floor(position*height*dy+1));
    	}
    	
    	slider.Draw(G,loc);
    }
    
    public Color getColor(){
    	int r=0,g=0,b=0;
    	double np = 1-powerPosition, dp = position-powerPosition;
    	r = (int)((position)/(powerPosition)*255);
    	g = (int)((1-(dp)/(np))*255);
    	
    	r = Math.max(0,Math.min(255,r));
    	g = Math.max(0,Math.min(255,g));
    	b = Math.max(0,Math.min(255,b));
    	
    	return new Color(r,g,b,192);
    }
}