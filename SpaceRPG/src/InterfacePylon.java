/**
 * @(#)InterfacePylon.java
 *
 *
 * @author 
 * @version 1.00 2012/7/10
 */


import java.net.URL;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Color;

public class InterfacePylon extends UIElement{

	Pylon pylon;
	double percent= -1.0;
	double updateTimer;
	double offX,offY;
	
    public InterfacePylon(double nx, double ny, String img){
    	super(nx,ny,img);
    }
    
    public InterfacePylon(Pylon P) {
    	double cornerX = 650, cornerY = 643;
    	
    	pylon = P;
    	
    	//Grab the pylon GUI image
    	String image = P.gui;
    	
    	//Set the graphic coordinates
    	offX = P.screenX; offY = P.screenY;
    	
    	//initialize other attributes
    	mouseOpacity = 0;
    	
    	layer = 25;
    	
    	
    	if(image != null)
    	{
    		sprite = new Sprite(image, false);
	    	
	    	//Init();
	    	Global.state.newObjBuffer.add(this);
    	}
    	move(cornerX,cornerY);
    	transform();
    }
    
    public void Draw(Graphics2D g, ImageObserver I){
    	//Compute the color stuff
    	int cr=-32,cg=-32,cb=-32,ca = -64;
    	
    	double newPercent = percent;
    	
    	if(pylon!=null){
    		newPercent = pylon.realHealth/(pylon.baseHealth + (pylon.equipped!=null? pylon.equipped.baseHealth : 0));
    	}
    	
    	if(pylon!=null && newPercent != percent && newPercent > 0.05){
    		newPercent = Math.min(newPercent,0.95);
    		int colorIncrement = 64;
    		
    		ca = 0;
    		cb = -32;
    		if(percent >= 0.5) {
    			cr = (int) ((1-newPercent)*2*colorIncrement);
    			cg = (int) (colorIncrement);
    		}
    		else {
    			cr = (int) (colorIncrement);
    			cg = (int) (newPercent*2*colorIncrement);
    		}
    	}
    	
    	if(newPercent != percent && Global.state.time >= updateTimer){
	    	//Call sprite.addColors()
	    	
	    	sprite.addColors(cr,cg,cb,ca);
	    	updateTimer = Global.state.time +  0.5;
    	}
    	percent = newPercent;
    	
    	
    	//Draw the sprite
    	sprite.Draw(g,I);
    	
    	//Draw the pylon pointer
    	if(pylon!=null && percent > 0.05){
    		double dx,dy;
    		dx = x+sprite.frameX/2 + offX;
    		dy = y+sprite.frameY/2 - offY;
    		
    		//This will be the center of whereever the thing is drawn.
    		
    		int radius = 8;
    		
    		g.setColor(Color.yellow);
    		g.drawOval((int) (dx-radius), (int) (dy-radius), radius*2, radius*2);
    		g.drawOval((int) (dx-radius), (int) (dy-radius), radius*2, radius*2);
    		
    		if(pylon.equipped!=null){
	    		if(pylon.allowedType.indexOf("w")>=0){ //weapon
					double fx = dx+radius*2*Math.cos(Math.toRadians(pylon.selfAngle));
					double fy = dy-radius*2*Math.sin(Math.toRadians(pylon.selfAngle));
					g.drawLine((int) dx, (int) dy, (int) fx, (int) fy);
					g.drawLine((int) dx, (int) dy, (int) fx, (int) fy);
	    		}
	    		else if(pylon.allowedType.indexOf("s")>=0){ //shield
	    			g.setColor(new Color(0,255,255,128));
	    			g.fillOval((int)(dx-radius),(int) (dy-radius), radius*2+1, radius*2+1);
	    		}
	    		else if(pylon.allowedType.indexOf("c")>=0){ //power generator
	    			g.setColor(new Color(255,164,0,128));
	    			g.fillOval((int)(dx-radius),(int) (dy-radius), radius*2+1, radius*2+1);
	    		}
	    		else if(pylon.allowedType.indexOf("e")>=0){ //engines
	    			g.setColor(new Color(0,255,0,128));
	    			g.fillOval((int)(dx-radius),(int) (dy-radius), radius*2+1, radius*2+1);
	    		}
    		}
    		
    	}
    	
    }
    
}
