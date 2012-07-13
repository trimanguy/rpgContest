
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
	boolean drawObjectsLock = false;
	
	long nextSecond = System.currentTimeMillis() + 1000;
	int framesInLastSecond = 0;
	int framesInCurrentSecond = 0;
	
	BufferedImage background;
	
	StarField starField;
	
	//Debugging stuff
	Obj Clicked;
	
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
    	//buffergraphics = (Graphics2D) buffer.getGraphics();
    	starField = new StarField();
    }
    
	public void run(){
		
		while(true){
			//Execute processing for animated sprites and so forth
			
			
			//Draw the current frame
			Draw((Graphics2D) game.board.getGraphics(), game.board);			
		}
	}
    
    
    public void Draw(Graphics2D G, ImageObserver I){
    	frame++;
    	
    	//Sort the drawing list by object layer if necessary...
    	//With proper insertion and removal of objects into this list, sorting may never be necessary.
    	/*
    	if(!checkSortedLayers()) 
    		sortLayers();
    	*/	
    	//Initialize and fill the drawing buffer here.
    	
    	buffergraphics = (Graphics2D) buffer.getGraphics();
    	
		buffergraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	buffergraphics.setColor(bgColor);
    	
    	buffergraphics.fillRect(0,0,sizex,sizey);
    	
    	
    	//Draw background stuff here. Things like planets, nebulae, and scrolling starfields.
    	if(background!= null){
    		buffergraphics.drawImage(background,0,0,I);
    	}
    	
    	if(starField!=null){
    		starField.Draw(buffergraphics,I);
    	}
    	
    	
    	drawObjectsLock = true;
    	//Draw active objects here. Things like ships, particles, asteroids, projectiles, etc...
    	for(int i=0;i<drawObjects.size();i++){
    		Obj O = (Obj) drawObjects.get(i);
    		O.Draw(buffergraphics,I);
    		
    		/*
    		//Debug Message to list objects and their draw layers
    		buffergraphics.setColor(O.color);
    		buffergraphics.drawString(""+O.getClass()+" - DrawLayer: "+O.layer+
    			" ObjectAngle: "+O.currAngle+" SpriteFrame: "+O.sprite.frame
    		 	,5, sizey-10-(i*15));
    		*/
    	}
    	drawObjectsLock = false;
    	
    	
    	
    	//Debug message here for the drawn objects list size
    	buffergraphics.setColor(Color.white);
    	buffergraphics.drawString("Objects drawn: "+drawObjects.size(), 5, 10);
    	
    	
    	
    	//Compute and display the current framerate
	    long currentTime = System.currentTimeMillis();
	    if (currentTime > nextSecond) {
	        nextSecond += 1000;
	        framesInLastSecond = framesInCurrentSecond;
	        framesInCurrentSecond = 0;
	    }
	    framesInCurrentSecond++;
	
	    buffergraphics.drawString(framesInLastSecond + " fps", 5, 25);
	    
	    //Draw Debugging Clicked Object
    	if(Clicked!=null){
    		buffergraphics.setColor(Clicked.color);
    		buffergraphics.drawString("Clicked Object: "+Clicked.getClass(), 5, 40);
    	}
	    
    	
    	//Draw the buffer onto the screen
    	G.drawImage(buffer,0,0,I);
    	buffergraphics.dispose();
    	G.dispose();
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
    
    public void addDrawObject(Obj A){
    	if(drawObjects.contains(A)) return;
    	if(drawObjectsLock) return;
    	
    	int start = 0;
    	int end = drawObjects.size()-1;
    	int index=0;
    	
    	if(drawObjects.size() > 0){
    		for(int i=0;i<=end;i++){
    			Obj B = drawObjects.get(i);
    			if(A.compareTo(B)>0){
    				index = i+1;
    			}
    		}
    	}
    	if(index <= end){
    		drawObjects.add(index,A);
    	}else{
    		drawObjects.add(A);
    	}
    	
    }
    
    public void removeDrawObject(Obj A){
    	if(drawObjectsLock) return;
    	drawObjects.remove(A);
    }
}