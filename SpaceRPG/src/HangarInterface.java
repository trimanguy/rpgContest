/**
 * @(#)HangarInterface.java
 *
 *
 * @author 
 * @version 1.00 2012/7/31
 */

import java.awt.Color;

public class HangarInterface extends InterfaceManager{
	
	String currPane;
	UIElement paneObject;//This is the parent "window"
	
    String path = "Resources/Interface/";

    public HangarInterface() {
    	//create the background
    	//double x, double y, String image, URL spritecontext, double nlayer
    	
    	
    	elements.put("background",new UIElement(0,0,path+"HangarInterfaceBack.png",1));
    	
    	double hx = 40, hy = 137;
    	
    	//Create the side menu
    	//InterfaceButton(double x, double y, String image, URL spritecontext, String cM, Obj cC
    	InterfaceButton hangarButton = new InterfaceButton(hx,hy,path+"HangarButtonHangar.png", 20,
    		"openNewPane",this,"Hangar");
    	hy += 65;
    		
    	InterfaceButton shopButton = new InterfaceButton(hx,hy,path+"HangarButtonShop.png", 20,
    		"openNewPane",this,"Shop");
    	hy += 65;
    		
    	InterfaceButton marketButton = new InterfaceButton(hx,hy,path+"HangarButtonMarket.png", 20,
    		"openNewPane",this,"Market");
    	hy += 65;
    		
    	InterfaceButton shipyardButton = new InterfaceButton(hx,hy,path+"HangarButtonShipyard.png", 20,
    		"openNewPane",this,"Shipyard");
    	hy += 65;
    		
    	InterfaceButton missionsButton = new InterfaceButton(hx,hy,path+"HangarButtonMissions.png", 20,
    		"openNewPane",this,"Missions");
    	hy += 65;
    		
    	InterfaceButton barButton = new InterfaceButton(hx,hy,path+"HangarButtonBar.png", 20,
    		"openNewPane",this,"Bar");
    	hy += 65;
    		
    	InterfaceButton undockButton = new InterfaceButton(hx,hy,path+"HangarButtonUndock.png", 20,
    		"undock",this,null);
    		
    	elements.put("hangarButton",hangarButton);
    	elements.put("shopButton",shopButton);
    	elements.put("marketButton",marketButton);
    	elements.put("shipyardButton",shipyardButton);
    	elements.put("missionsButton",missionsButton);
    	elements.put("barButton",barButton);
    	elements.put("undockButton",undockButton);
    	
    	//Create the money display
    	elements.put("money",new UIElement(10,560,null,20));
    	elements.get("money").text = "Credits: $133700";
    	elements.get("money").color = Color.green;
    	
    	//Create the initial pane
    	currPane = "description";
    	paneObject = new UIElement(0,0,path+"HangarPane.png",10, true);
    	paneObject.translate(0,-paneObject.sprite.frameY);
    	paneObject.targetY = 0;
    	paneObject.targetX = 0;
    	elements.put("pane",paneObject);
    	
    }
    
    public void closeCurrPane(){
    	
    	while(paneObject.y + paneObject.sprite.frameY>0.1){
    		try{
    			Thread.currentThread().wait(100);
    		}catch(InterruptedException e){
    			//gay.
    		}
    		paneObject.move(0, 0.1 * (-paneObject.sprite.frameY - paneObject.y));
    	}
    	
    	paneObject.addDelete();
    	
    	paneObject = null;
    	currPane = null;
    }
    
    public void openNewPane(String nPane){
    	if(paneObject!= null || currPane != nPane){
    		closeCurrPane();
    	}
    	
    	//create the pane object
    	paneObject = new UIElement(0, 0, path+"HangarPane.png", 20,true);
    	paneObject.translate(0,-paneObject.sprite.frameY);
    	currPane = nPane;
    	
    	elements.put("pane",paneObject);
    	
    	//might do something different here, like a pane subclass for UIElement with cool sliding shit
    	paneObject.targetX = 0;
    	paneObject.targetY = 0;
    	
    	//else if string to populate with content
    	if(nPane.compareTo("Hangar")==0){
    		
    	}else if(nPane.compareTo("Shop")==0){
    		
    	}else if(nPane.compareTo("Market")==0){
    		
    	}else if(nPane.compareTo("Shipyard")==0){
    		
    	}else if(nPane.compareTo("Missions")==0){
    		
    	}else if(nPane.compareTo("Bar")==0){
    		
    	}
    	
    	
    }
    
    public void undock(){
    	//change the game state yo
    }
    
}