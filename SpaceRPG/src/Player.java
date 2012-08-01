/**
 * Player Class
 * Thus far, this is the listener for mouse and key events
 *
 * Later on, it may be necessary to add a new Character class for character stats and such.
 */
import java.awt.event.*;
import java.io.*;



public class Player implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
  
	//Camera X and Camera Y. These are in 1:1 game world coordinates.
	//This should not affect interface rendering.
	double cx = 0;
	double cy = 0;
	double offsetX=0;
	double offsetY=0;
	
	//Camera zoom. Any value > 1 will magnify.
	//This should not affect interface rendering.
	double zoom =  0.75;
	
	Obj mouseObj;
	Obj dropObj;
	
	String activeButtons=""; //which buttons currently pressed atm
	
    public Player() {
    	Global.player = this;
    }
    
    public void mouseMoved(MouseEvent e){
    	//System.out.println("Mouse detected on "+ e.getComponent().getClass().getName()+ "at coords "+e.getX()+","+e.getY()+".");
    }
    
    public void mouseDragged(MouseEvent e){
    	//System.out.println("Mouse dragged "+e);
    	//System.out.println("mouseDragged at " +e.getX()+"," +e.getY()+ "activeButtons "+activeButtons);
    	if((activeButtons.indexOf("r")>=0)){
    		if (!(mouseObj instanceof UIElement)){	
				rotateToClick(e);	
				PointS point = (new PointS(e.getX(),e.getY())).toWorld();
			
    		}
    	}
    }
    
    
    public void mouseWheelMoved(MouseWheelEvent e){
    	int notches = e.getWheelRotation()*(-1);
    	
    	if (notches<0){
    		zoom=Math.max(0.25,(zoom+notches*0.05));
    	} else {
    		zoom=Math.min(2.0,(zoom+notches*0.05));
    	}
    }
    
    public void mouseClicked(MouseEvent e){
    }
    
    public void mouseEntered(MouseEvent e){
    	
    }
    
    public void mouseExited(MouseEvent e){
    	
    }
    
    public void mousePressed(MouseEvent e){
    	/**	Convert screen coordinates to relative coordinates 
    	 *	to world coordinates, and then go through The layered 
    	 *	objects list to determine what was being clicked. 
    	 **/
    	
    	//determine REAL coord of the click 
    	PointS clickedPt = new PointS(e.getX(), e.getY());
    	
    	
    	//determine what obj was being clicked
    	mouseObj = null;
    	mouseObj = findClickedObj(clickedPt);
    	
    	//do stuff depending on if we clicked something
    	//might not need this part
    	if(mouseObj == null){ 
    		//clicked on nothing
    	} else { 
    		//clicked on something; nothing here for now (testing purposes)
    		mouseObj.mouseClicked(e);
    	}
    	
    	//check if right or left clicked
	    switch (e.getButton()) {
	    	case 1: 	activeButtons+="l";
	    				this.leftClick(e, mouseObj);  //left-button click 
	    				break;
	    	case 2:		activeButtons+="m";
	    				this.middleClick(e, mouseObj);  //right-button click
	    				break;
	    	case 3: 	activeButtons+="r";
	    				this.rightClick(e, mouseObj);  //middle-button click
	    				break;
	    }
	    
    }
    
    public void mouseReleased(MouseEvent e){
    	
    	PointS clickedPt = new PointS(e.getX(), e.getY());
    	
    	switch (e.getButton()){
    		case 1:		activeButtons=activeButtons.replaceAll("l","");break;
    		case 2:		activeButtons=activeButtons.replaceAll("m","");break;
    		case 3:		activeButtons=activeButtons.replaceAll("r","");break;
    	}
    	
    	dropObj = null;
    	dropObj = findClickedObj(clickedPt);
    	
    	if(dropObj == null) return;
    	
    	if(mouseObj != null && dropObj != null)
    		mouseObj.mouseDropped(e,dropObj);
    	
    }
    
    public void keyTyped(KeyEvent e){
    	
    }
    
    public void keyPressed(KeyEvent e){
    	char key = e.getKeyChar();
    	int modNum = e.getModifiers();
    	String modifiers = KeyEvent.getKeyModifiersText(modNum);
    	//System.out.print("Key pressed: "+ key +" with mod "+ modifiers +" modNum: "+modNum+ " with effect: ");
    	switch (key){
    		case '`':	System.out.println("All Ctrl groups selected"); break;
    		case '1':	System.out.println("Ctrl Group1 selected"); break;
    		case '2':	System.out.println("Ctrl Group2 selected"); break;
    		case '3':	System.out.println("Ctrl Group3 selected"); break;
    		case '4':	System.out.println("Ctrl Group4 selected"); break;
    		case 'r':  	increaseSpeed(); //System.out.println("Speed increased!"); 
    					break;
    		case 'f':	decreaseSpeed(); //System.out.println("Speed decreased!"); 
    					break;
    		case 'W':	shieldBoost("front"); break;
    		case 'A':	shieldBoost("left"); break;
    		case 'S':	shieldBoost("rear"); break;
    		case 'D':	shieldBoost("right"); break;
    		case '~':   weaponBoost("~"); break;
    	}
    	
    }
    
    public void keyReleased(KeyEvent e){
    	
    }
    
    private void leftClick(MouseEvent e, Obj clickedObj){
    	
    	if (clickedObj==null){
    		Global.view.Clicked = null; //this for testing
    		Global.state.playerObj.aimTarget = null;
    	} else {
    		//if we L-Clicked on ship, set it as target
    		if(mouseObj instanceof ShipObj){
    			Global.state.playerObj.aimTarget = (ShipObj) mouseObj;
    			Global.view.Clicked = clickedObj;
    		}
    	}
    }
    
    private void rightClick(MouseEvent e, Obj clickedObj){
    	//System.out.println("TEST: "+ Global.playerObj.getSpeed());
    	//make player rotate towards click-location 
    	if (!(clickedObj instanceof UIElement)){	
			rotateToClick(e);	
			PointS point = (new PointS(e.getX(),e.getY())).toWorld();
			
			new NavPointerObj(point.x, point.y, 0.75);
    	}
    }
    
    private void middleClick(MouseEvent e, Obj clickedObj){
    	System.out.println("AHHH");
    }
    
    private void rotateToClick(MouseEvent e){
    	PointS clickedPt = new PointS(e.getX(), e.getY());
		PointS realPt = clickedPt.toWorld();
		realPt.x -= cx; realPt.y -= cy;
		Global.state.playerObj.rotateTowards(realPt);
    }
    
    public Obj findClickedObj(PointS clickedPt){
    	for(int x = Global.view.drawObjects.size()-1; x>=0; x--){ //check from obj closest to front
    		Obj currentObj = Global.view.drawObjects.get(x);
    		if(currentObj.CheckClick(clickedPt)){
    			return currentObj;
    		}
    		else {
    			continue;
    		}
    	}
    	return null;	
    }
    
    private void increaseSpeed(){
    	double currSpeed = Global.state.playerObj.velocity;
    	double maxSpeed = Global.state.playerObj.maxVelocity;
    	Global.state.playerObj.velocity = Math.min(maxSpeed, currSpeed+(maxSpeed/30)); 
    	//System.out.println("Current speed: "+Global.state.playerObj.velocity);
    	//System.out.println("Current Power regen: "+Global.state.playerObj.powerMade+" max power: "+Global.state.playerObj.maxPower);
    }
    
    private void decreaseSpeed(){
    	double currSpeed = Global.state.playerObj.velocity;
    	double maxSpeed = Global.state.playerObj.maxVelocity;
    	Global.state.playerObj.velocity = Math.max(0, currSpeed-(maxSpeed/30)); 
    	//System.out.println("Current speed: "+Global.state.playerObj.velocity);
    }
    
    private void shieldBoost(String side){
    	ShipObj playerShip = Global.state.playerObj;
    	
    	if(playerShip.shieldBoostDelay!=0){
    		System.out.println("cannot boost, cd not over. Time left: "+playerShip.shieldBoostDelay);
    		return;
    	}
    	if(playerShip.shieldBoostCost>playerShip.currPower){
    		//System.out.println("cannot boost, not enough power");
    		return;
    	}
    	
    	if(side.equals("front")){
    		playerShip.shieldForward = Math.max(playerShip.maxShield,playerShip.shieldForward+playerShip.maxShield/4);
    		
    	}
    	else if(side.equals("left")){
    		playerShip.shieldLeft = Math.max(playerShip.maxShield,playerShip.shieldLeft+playerShip.maxShield/4);
    	}
    	else if(side.equals("rear")){
    		playerShip.shieldRear = Math.max(playerShip.maxShield,playerShip.shieldRear+playerShip.maxShield/4);
    	}
    	else if(side.equals("right")){
    		playerShip.shieldRight = Math.max(playerShip.maxShield,playerShip.shieldRight+playerShip.maxShield/4);
    	}
    	
    	playerShip.currPower-=playerShip.shieldBoostCost;
    	playerShip.shieldBoostDelay = playerShip.shieldChargeTimer;
    	
    }
    	
    Boolean boosted;
    double timeBoostedLeft;
    	
    private void weaponBoost(String group){
    
       ShipObj playerShip = Global.state.playerObj;
       if(playerShip.currPower<(playerShip.maxPower/5)){  //not enough power?
               System.out.println("cannot boost, lack energy");
               return;
       }
       if(playerShip.boostCD>0){ //cd not over?
               System.out.println("cannot boost, cd not over");       
               return;
       }
          
           for(Pylon P:playerShip.pylons){
               if(P.equipped instanceof WeaponObj){
                       ((WeaponObj)(P.equipped)).boost();
               }
               }
               playerShip.currPower=Math.max(0,playerShip.currPower-playerShip.maxPower/5);
               playerShip.boosted=true;
               playerShip.timeBoostedLeft = 8;
               playerShip.boostCD = 16;
              
    }
    	
    	
    //i hate retyping print lines...
    //System.out.print("Component is: " + e.getComponent().getClass().getName() + System.getProperty("line.separator"));
}