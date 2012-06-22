
import java.awt.*;
import java.util.*;
import java.awt.image.*;

public class GameView implements Runnable{

  	Thread thread = new Thread(this);
  	GameApplet game;
  	
	Image buffer;
	Graphics2D buffergraphics;
	
    long frame = 0;
    int sizex, sizey;
    
	static Color bgColor = Color.black;
	
	ArrayList <Obj> drawObjects = new ArrayList(0);
	
    public GameView() {
    	
    }
    
    public GameView(GameApplet ngame, int sx, int sy, Image buff) {
    	game = ngame;
    	sizex = sx;
    	sizey = sy;
    	
    	buffer = buff;
    	initialize();
    }
    
    public void initialize(){
    	buffergraphics = (Graphics2D) buffer.getGraphics();
    }
    
	public void run(){
		
		while(true){
			//Execute processing for animated sprites and so forth
			
			
			//Draw the current frame
			Draw((Graphics2D) game.board.getGraphics(), game.board);
			
			
			try{
				thread.sleep(1); // capped at 1000fps hurrdurr
			} catch(Exception E){
				
			}
			
		}
	}
    
    
    public void Draw(Graphics2D G, ImageObserver I){
    	frame++;
    	
    	//Sort the drawing list by object layer if necessary...
    	//With proper insertion and removal of objects into this list, sorting may never be necessary.
    	if(!checkSortedLayers()) 
    		sortLayers();
    		
    	//Initialize and fill the drawing buffer here.
		buffergraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	buffergraphics.setColor(bgColor);
    	
    	buffergraphics.fillRect(0,0,sizex,sizey);
    	
    	
    	//Draw background stuff here. Things like planets, nebulae, and scrolling starfields.
    	
    	
    	
    	//Draw active objects here. Things like ships, particles, asteroids, projectiles, etc...
    	for(int i=0;i<drawObjects.size();i++){
    		Obj O = (Obj) drawObjects.get(i);
    		O.Draw(buffergraphics,I);
    		
    		//buffergraphics.drawString(""+O.getClass()+" : "+O.layer, 0, sizey-10-(i*10));
    		//This is a debug message of sorts that displays every object being drawn and its layer
    	}
    	
    	//Draw UI stuff here
    	
    	
    	
    	//Draw the buffer onto the screen
    	G.drawImage(buffer,0,0,I);
    	
    	
    }
    
    public boolean checkSortedLayers(){
    	for(int i=0;i<drawObjects.size()-1;i++){
    		Comparable A = drawObjects.get(i);
    		Comparable B = drawObjects.get(i+1);
    		if(A == null || B == null){
    			continue;
    		}
    		if(A.compareTo(B)>0) return false;
    	}
    	return true;
    }
    
    public void sortLayers(){
    	//mergeSort.mergeSort(drawObjects);
    	//I'm getting some strange polymorphism problems with this.
    	//My Java is more rusty than I thought it'd be.
    }
}