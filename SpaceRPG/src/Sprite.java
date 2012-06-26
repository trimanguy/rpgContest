/**
 * This is a super class that provides basic functionality for displaying sprites.
 */

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.imageio.*;
import java.net.*;

public class Sprite {
	public AffineTransform transform = new AffineTransform();
	BufferedImage img;
	BufferedImage source;
	
	boolean hasFrames;
	int frame;
	int frameX;
	int frameY;
	
	public Sprite(String dir, URL context, boolean hF){
		
		hasFrames = hF;
		
		try {
			URL url = new URL(context, dir);
			source = ImageIO.read(url);
			
			frameY = source.getHeight();
			frameX = frameY;
			
			setFrame(1);
		} catch (IOException e) {
		}
		
	}
	
	public Sprite(String dir, boolean hF){
		
		hasFrames = hF;
		
		try {
			source = ImageIO.read(new File(dir));
			
			frameY = source.getHeight();
			frameX = frameY;
			
			setFrame(1);
		} catch (IOException e) {
		}
		
	}
	
	public void setFrame(int f){
		
		frame = f;
		if(!hasFrames) {
			img = source;
			return;
		}
		//if(source == null) return;
		int gx,gy;
		gx = (frame-1)*frameX;
		gy = 0;
		
		while(gx+frameX > source.getWidth()){
			frame--;
			gx = (frame-1)*frameX;
		}
		//Garbage Collector Sanity shit
		img = null;
		
		//Set the current image to the new frame
		img = source.getSubimage(gx, gy, frameX, frameY);
		
	}
	
	public void setTransform(AffineTransform at){
		transform = at;
	}
	
	public void setImage(BufferedImage image){
		source = image;
	}
	
	public void Draw (Graphics2D graphic, ImageObserver loc){
		graphic.drawImage(img, transform, loc);
	}
}