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
	int cx = 0;
	int cy = 0;
	
	//Camera zoom. Any value > 1 will magnify.
	//This should not affect interface rendering.
	double zoom = 1;

    public Player() {
    	Global.player = this;
    }
    
    public void mouseClicked(MouseEvent e){
    	//Convert screen coordinates to relative coordinates to world coordinates, and then go through
    	//The layered objects list to determine what was being clicked.
    	
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
    
}