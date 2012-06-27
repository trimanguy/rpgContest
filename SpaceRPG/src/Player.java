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
	
	Obj mouseObj;
	Obj dropObj;
	
    public Player() {
    	Global.player = this;
    }
    
    public void mouseClicked(MouseEvent e){
    }
    
    public void mouseEntered(MouseEvent e){
    	
    }
    
    public void mouseExited(MouseEvent e){
    	
    }
    
    public void mousePressed(MouseEvent e){
    	//Convert screen coordinates to relative coordinates to world coordinates, and then go through
    	//The layered objects list to determine what was being clicked.
    	
    	//determine REAL coord of the click 
    	PointS clickedPt = new PointS(e.getX(), e.getY());
    	//PointS realPt = clickedPt.toWorld();
    	
    	//determine what obj was being clicked
    	mouseObj = null;
    	
    	for(int x = Global.view.drawObjects.size()-1; x>=0; x--){ //check from obj closest to front
    		Obj currentObj = Global.view.drawObjects.get(x);
    		
    		if(currentObj.CheckClick(clickedPt)){
    			mouseObj = currentObj;
    			break;
    		}
    		else {
    			continue;
    		}
    	}
    	
    	//System.out.print("Component is: " + e.getComponent().getClass().getName() + System.getProperty("line.separator"));
    	
    	//do stuff depending on if we clicked something
    	if(mouseObj != null){ 
    		//clicked on something; nothing here for now (testing purposes)
    	} else { 
    		//clicked on nothing
    		Global.view.Clicked = null; //this for testing
    	}
    	
    	//check if right or left clicked
	    switch (e.getButton()) {
	    	case 1: 	this.leftClick(e, mouseObj);  //left-button click 
	    				break;
	    	case 2:		this.middleClick(e, mouseObj);  //right-button click
	    				break;
	    	case 3: 	this.rightClick(e, mouseObj);  //middle-button click
	    				break;
	    }
	    
    }
    
    public void mouseReleased(MouseEvent e){
    	
    	PointS clickedPt = new PointS(e.getX(), e.getY());
    	
    	dropObj = null;
    	
    	for(int x = Global.view.drawObjects.size()-1; x>=0; x--){ //check from obj closest to front
    		Obj currentObj = Global.view.drawObjects.get(x);
    		
    		if(currentObj.CheckClick(clickedPt)){
    			dropObj = currentObj;
    			break;
    		}
    		else {
    			continue;
    		}
    	}
    	
    	if(dropObj == null) return;
    	
    	if(dropObj == mouseObj){ //A click has occurred
    		
    		Global.view.Clicked = mouseObj;//Draw debugging info on the clicked object
    		
	    	//check if right or left clicked
	    	switch (e.getButton()) {
	    		case 1: 	this.leftClick(e, mouseObj);  //left-button click 
	    					break;
	    		case 2:		this.middleClick(e, mouseObj);  //right-button click
	    					break;
	    		case 3: 	this.rightClick(e, mouseObj);  //middle-button click
	    					break;
	    	}
    	}else{
    		//A drag and drop has occurred
    	}
    	
    	
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
    	//make player rotate towards click-location 	
    	
		PointS clickedPt = new PointS(e.getX(), e.getY());
		PointS realPt = clickedPt.toWorld();
		
		double rotateTo = 0.0;
		if (Math.toDegrees((Math.atan2(realPt.y-cy,realPt.x-cx ))) < 0){
			rotateTo = Math.toDegrees((Math.atan2(realPt.y-cy,realPt.x-cx )))+360;
		} else {
			rotateTo = Math.toDegrees((Math.atan2(realPt.y-cy,realPt.x-cx )));
		}
		
		System.out.print("Previous Direction is: " + Global.state.playerObject.angle + System.getProperty("line.separator"));
		//Global.state.playerObject.rotate( rotateTo - Global.state.playerObject.angle);
		Global.state.playerObject.tang = rotateTo;
		System.out.print("Theta is: " + rotateTo + System.getProperty("line.separator"));
		System.out.print("Current Direction is: " + Global.state.playerObject.angle + System.getProperty("line.separator"));
		
    	
    }
    
    private void middleClick(MouseEvent e, Obj clickedObj){
    	
    }
    
    
    
}