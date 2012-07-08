/**
 * @(#)NavPointerObj.java
 *
 *
 * @author 
 * @version 1.00 2012/7/6
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class NavPointerObj extends GameObj {

	Color color = Color.green;

	double frame;
	double frames;
	double maxRadius = 20;

	public NavPointerObj(double nx, double ny, double nf, Color nCol, double rad){
		x = nx;
		y = ny;
		frames = nf;
		color = nCol;
		maxRadius = rad;
		mouseOpacity = 0;
		density = false;
		
		Global.state.newObjBuffer.add(this);
	}
	
	
	public NavPointerObj(double nx, double ny, double nf){
		x = nx;
		y = ny;
		frames = nf;
		mouseOpacity = 0;
		density = false;
		
		Global.state.newObjBuffer.add(this);
	}
	

    public void Draw(Graphics2D G, ImageObserver loc){
    	
    	
    	
    	double dx,dy;
    	dx = x-Global.player.cx;
    	dy = Global.player.cy-y;
    	
    	dx *= Global.player.zoom;
    	dy *= Global.player.zoom;
    	
    	dx += Global.view.sizex/2;
    	dy += Global.view.sizey/2;
    	
    	double radius = frame/frames*maxRadius;
    	
    	int alpha = (int) (192 * (1-frame/frames) + 64);
    	alpha = Math.min(alpha,255);
    	alpha = Math.max(0,alpha);
    	
    	G.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha));
    	G.drawOval((int) dx-1,(int) dy-1,(int) 2,(int) 2);
    	
    	double radiusY = Global.xyRatio*radius*2/3, radiusX = radius;
    	
    	G.drawOval((int) (dx-radiusX+0.5),(int) (dy-radiusY+0.5),(int) radiusX*2,(int) radiusY*2);
    }
    
    public void Step(){
    	if(frame >= frames) addDelete();
    	else frame += Global.state.dt/1000;
    }
}