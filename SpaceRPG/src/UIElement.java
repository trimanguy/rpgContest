
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.color.*;
import java.awt.geom.*;

public class UIElement implements Comparable {

	public Sprite sprite;
	double layer;
	Color color;
	
	double screenX,screenY;
	
	
	
    public UIElement(double x, double y) {
    	Move(x,y);
    	
    }
    
    public int compareTo(UIElement O){
    	double l = layer;
    	double ol = O.layer;
    	if(ol > l) return -1;
    	if(ol < l) return 1;
    	return 0;
    }
    
    
    public int compareTo(Object O){
    	return O.toString().compareTo(this.toString());
    }
    
    public void Move(double x, double y){
    	screenX = x;
    	screenY = y;
    	
    	AffineTransform transform = new AffineTransform();
    	transform.translate(x,y);
    }
    
    public void MovePercent(double x, double y){
    	if(x > 100) x/= 100;
    	if(y > 100) y/= 100;
    	
    	Move(x*Global.view.sizex, y*Global.view.sizey);
    }
    
    public void Draw(Graphics2D G, ImageObserver loc){
    	sprite.Draw(G,loc); //Draws the object's sprite
    }
    
    
    public void Step(){ //This can be called by gamestate or gameview
    }
    
}