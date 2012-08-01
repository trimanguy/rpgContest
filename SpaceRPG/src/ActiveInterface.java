/**
 * @(#)ActiveInterface.java
 * The ActiveInterface class manages interface states for the interface elements for when
 * the player's vessel is active in space and not docked.
 *
 *
 * @author MistaLemur
 * @version 1.00 2012/7/2
 */

import java.net.URL;

public class ActiveInterface extends InterfaceManager {
	double speedHeight; //This is the height of the speed bar
	double speedPower; //This is a double from 0 to 1

    public ActiveInterface() {
    	
//    	URL spriteContext = Global.codeContext;
    	
    	//double x, double y, String image, URL spritecontext
    	setElement("background", new UIElement(377,674,"Resources/Interface/Active Plate.png",20));
    	setElement("shortcutTilde", new UIElement(429,701,"Resources/Interface/Shortcut ~.png",21));
    	setElement("shortcutOne", new UIElement(471,701,"Resources/Interface/Shortcut 1.png",21));
    	setElement("shortcutTwo", new UIElement(513,701,"Resources/Interface/Shortcut 2.png",21));
    	setElement("shortcutThree", new UIElement(555,701,"Resources/Interface/Shortcut 3.png",21));
    	setElement("shortcutFour", new UIElement(597,701,"Resources/Interface/Shortcut 4.png",21));
    	
    	InterfaceBar powerBar = new InterfaceBar(425,681,"Resources/Interface/PowerBar Front.png",6,1);
    	powerBar.underlay = new Sprite("Resources/Interface/PowerBar Back.png", false);
    	
    	powerBar.setLayer(22);
    	setElement("powerBar", powerBar);
    	
    	InterfaceSlider speedBar = new InterfaceSlider(400,681,"Resources/Interface/SpeedBar Back.png",
    		"Resources/Interface/SpeedBar Slider.png","setVelocity",Global.state.playerObj,0,100,78);
    	setElement("speedBar",speedBar);
    	
    	speedBar.setLayer(22);
    	
    	setElement("speedLevel", new UIElement(393,698,"Resources/Interface/SpeedBar Power.png",21));
    	Init();
    }
    
    public void Init(){
    	if(Global.state.playerObj!=null){
    		//oh baby
    		ShipObj ship = Global.state.playerObj;
    		for(int i=0;i<ship.pylons.size();i++){
    			Pylon P = ship.pylons.get(i);
    			
    			System.out.println("PYLON GUI: "+i +"sprite: "+P.gui);
    			
    			if(P == null) continue;
    			if(P.gui == null) continue;
    			
    			
    			InterfacePylon gui = new InterfacePylon(P);
    			
    			setElement("Pylon"+P.tag+""+i,gui);
    		}
    	}
    }
}