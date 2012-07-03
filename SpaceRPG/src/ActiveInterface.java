/**
 * @(#)ActiveInterface.java
 * The ActiveInterface class manages interface states for the interface elements for when
 * the player's vessel is active in space and not docked.
 *
 *
 * @author MistaLemur
 * @version 1.00 2012/7/2
 */

import java.util.ArrayList;
import java.net.URL;

public class ActiveInterface extends Obj {

	ArrayList <UIElement> elements = new ArrayList(0);

	UIElement background;
	UIElement shortcutTilde;
	UIElement shortcutOne;
	UIElement shortcutTwo;
	UIElement shortcutThree;
	UIElement shortcutFour;
	InterfaceSlider speedBar;
	UIElement powerBar;
	UIElement speedLevel;
	UIElement radar;
	UIElement shipDisplay;//This might be multiple objects in the future.
	
	double speedHeight; //This is the height of the speed bar
	double speedPower; //This is a double from 0 to 1

    public ActiveInterface() {
    	
    	URL spriteContext = Global.codeContext;
    	
    	//double x, double y, String image, URL spritecontext
    	background = new UIElement(377,674,"Resources/Interface/Active Plate.png",spriteContext);
    	shortcutTilde = new UIElement(429,701,"Resources/Interface/Shortcut ~.png",spriteContext);
    	shortcutOne = new UIElement(471,701,"Resources/Interface/Shortcut 1.png",spriteContext);
    	shortcutTwo = new UIElement(513,701,"Resources/Interface/Shortcut 2.png",spriteContext);
    	shortcutThree = new UIElement(555,701,"Resources/Interface/Shortcut 3.png",spriteContext);
    	shortcutFour = new UIElement(597,701,"Resources/Interface/Shortcut 4.png",spriteContext);
    	powerBar = new UIElement(425,681,"Resources/Interface/PowerBar Front.png",spriteContext);
    	powerBar.underlay = new Sprite("Resources/Interface/PowerBar Back.png",spriteContext, false);
    	speedBar = new InterfaceSlider(400,681,"Resources/Interface/SpeedBar Back.png",spriteContext,
    		"Resources/Interface/SpeedBar Slider.png",null,null,0,100,78);
    	/*
    	(double x, double y, String image, URL spritecontext,String slideImage, String cM, Obj cC, 
    	double lo, double hi, double h)
    	*/
    	
    	//computePowerNeutral();
    	
    	speedLevel = new UIElement(393,698,"Resources/Interface/SpeedBar Power.png",spriteContext);
    	
    	elements.add(background);
    	elements.add(shortcutTilde);
    	elements.add(shortcutOne);
    	elements.add(shortcutTwo);
    	elements.add(shortcutThree);
    	elements.add(shortcutFour);
    	elements.add(powerBar);
    	elements.add(speedBar);
    	elements.add(speedLevel);
    	
    	for(UIElement O:elements){
    		if(O!=background){
    			O.setParent(background);
    			O.layer = (21);
    		}else{
    			O.layer = (20);
    		}
    	}
    	
    }
    
    
    
}