/**
 * @(#)InterfaceBar.java
 *
 *
 * @author 
 * @version 1.00 2012/7/29
 */


import java.net.URL;
import java.awt.event.*;

public class InterfaceBar extends UIElement{

	int borderX=0;
	
	double value=0;
	boolean update = false;

    public InterfaceBar(double x, double y, String image, URL spritecontext, int bx, double nv) {
    	super(x,y,image,spritecontext);
    	
    	borderX = bx;
    	value = nv;
    }
    
    public void setValue(double percent){
    	if(Double.isNaN(percent)) percent = 0.0;
    	
    	percent = Math.max(0,Math.min(1,percent));
    	if(percent != value){
	    	value = percent;
	    	update = true;
    	}
    }
    
    public void transform(){
    	super.transform();
    	
    	if(sprite != null && update){
    		update = false;
	    	double width = sprite.frameX-borderX*2;
	    	width *= value;
	    	
	    	if(width <=0){
	    		sprite.crop(0,0,borderX,sprite.frameY);
	    	}else{
	    		sprite.crop(0,0,(int) width+borderX,sprite.frameY);
	    	}
    	}
    }
}