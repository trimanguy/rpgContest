
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.color.*;
import java.awt.geom.*;
import java.net.*;

public class UIElement extends Obj {
	
	//These are screen coordinates in pixels relative to the top left
	//public double x=0;
	//public double y=0;
	
	public double layer = 20;
	
    public UIElement(double x, double y, String image, URL spritecontext) {
    	super(image,spritecontext);
    	move(x,y);
    	
    }
    
    public void MovePercent(double x, double y){
    	if(x > 100) x/= 100;
    	if(y > 100) y/= 100;
    	
    	translate(x*Global.view.sizex, y*Global.view.sizey);
    }
    
    public void Step(){
    }
    
}