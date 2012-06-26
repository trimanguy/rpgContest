/**
 * Player Class
 * Thus far, this is the listener for mouse and key events
 *
 * Later on, it may be necessary to add a new Character class for character stats and such.
 */
import java.awt.event.*;


public class Player implements MouseListener, KeyListener {
	
	
	//Camera X and Camera Y. These are in 1:1 game world coordinates.
	//This should not affect interface rendering.
	double cx = 0;
	double cy = 0;
	
	//Camera zoom. Any value > 1 will magnify.
	//This should not affect interface rendering.
	double zoom = 1;
	
    public Player() {
    	Global.player = this;
    }
    
    public void mouseClicked(MouseEvent e){
    	//Convert screen coordinates to relative coordinates to world coordinates, and then go through
    	//The layered objects list to determine what was being clicked.
    	
    	//determine REAL coord of the click 
    	PointS clickedPt = new PointS(e.getX(), e.getY());
    	PointS realPt = clickedPt.toWorld(clickedPt);
    	
    	//determine what obj was being clicked
    	Obj clickedObj = null;
    	for(int x = Global.view.drawObjects.size()-1; x>=0; x--){ //check from obj closest to front
    		Obj currentObj = Global.view.drawObjects.get(x);
    		
    		//First skip objects whose sprite dimensions are not even close
    		double dx = realPt.x - currentObj.x;//Relative X
    		double dy = realPt.y - currentObj.y;//Relative Y
    		if(Math.abs(dx) > currentObj.sprite.frameX/2) continue;
    		if(Math.abs(dy) > currentObj.sprite.frameY/2) continue;
    		
    		//Now from relative coordinates to local sprite coordinates...
    		//Sprite coordinates, I believe, are relative to the top left corner of the sprite
    		dx += currentObj.sprite.frameX/2;
    		dy = currentObj.sprite.frameY/2 - dy;
    		
    		//Grab the RGBA
    		int RGBA = currentObj.sprite.img.getRGB((int)dx,(int)dy);
    		int alpha = (RGBA  >> 24) & 0xFF;
    		if(alpha == 0) continue;
    		else clickedObj = currentObj;
    	}
    	
    	Global.view.Clicked = clickedObj;
    	if(clickedObj == null) return;
    	
    	//check if right or left clicked
    	switch (e.getButton()) {
    		case 1: 	this.leftClick(e, clickedObj);  //left-button click 
    					break;
    		case 2:		this.rightClick(e, clickedObj);  //right-button click
    					break;
    		case 3: 	this.middleClick(e, clickedObj);  //middle-button click
    					break;
    	}
    }
    
    public void mouseEntered(MouseEvent e){
    	
    }
    
    public void mouseExited(MouseEvent e){
    	
    }
    
    public void mousePressed(MouseEvent e){

    }
    
    public void mouseReleased(MouseEvent e){
    	
    }
    
    public void keyTyped(KeyEvent e){
    	
    }
    
    public void keyPressed(KeyEvent e){
    	
    }
    
    public void keyReleased(KeyEvent e){
    	
    }
    
    private void leftClick(MouseEvent e, Obj clickedObj){
    	
    }
    
    private void rightClick(MouseEvent e, Obj clickedObj){
    	
    }
    
    private void middleClick(MouseEvent e, Obj clickedObj){
    	
    }
    
    
    
}