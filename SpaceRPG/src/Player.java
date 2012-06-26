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
    	PointS clickedPt = new PointS(e.getXOnScreen(), e.getYOnScreen());
    	PointS realPt = clickedPt.toWorld(clickedPt);
    	
    	//determine what obj was being clicked
    	Obj clickedObj = new Obj();
    	for(int x = Global.view.drawObjects.size()-1; x>=0; x--){ //check from obj closest to front
    		Obj currentObj = Global.view.drawObjects.get(x);
    		if (currentObj.contains(realPt)) { //if click was on this obj
    			clickedObj = currentObj;
    			break;
    		}
    	}
    	
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