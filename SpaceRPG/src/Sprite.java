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
	
	double x;
	double y;
	double angle;
	double dx;//draw width
	double dy;//draw height
	
	//Hot spot coordinates. Allows me to define the center of a sprite for rotations and transformations.
	double hotx=0;
	double hoty=0; 
	
	public Sprite(String dir, URL context){
		
		try {
			URL url = new URL(context, dir);
			img = ImageIO.read(url);
			hotx = img.getWidth()/2;
			hoty = img.getHeight()/2;
		} catch (IOException e) {
		}
		
	}
	
	public Sprite(String dir){
		
		try {
			img = ImageIO.read(new File(dir));
			hotx = img.getWidth()/2;
			hoty = img.getHeight()/2;
		} catch (IOException e) {
		}
		
	}
	
	public void setHotspot(double nx, double ny){
		hotx = nx;
		hoty = ny;
	}
	
	public void setTransform(AffineTransform at){
		transform = at;
	}
	
	public void setImage(BufferedImage image){
		img = image;
	}
	
	public void Draw (Graphics2D graphic, ImageObserver loc){
		graphic.drawImage(img, transform, loc);
	}
}