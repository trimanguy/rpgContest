/**
 * @(#)Background.java
 *
 *
 * @author 
 * @version 1.00 2012/8/1
 */
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.geom.AffineTransform;

public class Background {

	double x=0;
	double y=0;
	double z=20;
	
	Sprite sprite;

    public Background(String spritePath) {
    	//Global.view.background = this;
    	sprite = new Sprite(spritePath, false);
    	x = Utils.randomNumberGen(-2000,2000);
    	y = Utils.randomNumberGen(-2000,2000);
    }
    
    public void transform(){
    	AffineTransform transform = new AffineTransform();
    	
    	double rx, ry;
    	
    	rx = x - Global.player.cx-Global.player.offsetX;
    	ry = Global.player.cy+Global.player.offsetY - y;
    	
    	rx /= z; ry /= z;
    	
    	double zoomFactor = (1.5+Global.player.zoom/z)/2;
    	
    	
        rx = rx * zoomFactor + Global.view.sizex/2;
        ry = ry * zoomFactor * Global.xyRatio + Global.view.sizey/2;
    	
    	if(sprite == null) return;
    	
    	//rx /= z;
    	//ry /= z;
    	
    	
    	rx -= (sprite.frameX/2) * zoomFactor;
    	ry -= (sprite.frameY/2) * zoomFactor;
    	
    	
    	transform.translate(rx, ry);
    	
    	transform.scale(zoomFactor, zoomFactor);
    	
    	sprite.setTransform(transform);
    }
    
    public void Draw(Graphics2D G, ImageObserver loc){
    	transform();
    	sprite.Draw(G,loc);
    }
}