/**
 * @(#)HangarInterface.java
 *
 *
 * @author 
 * @version 1.00 2012/7/31
 */

import java.awt.Color;
import java.util.ArrayList;

public class HangarInterface extends InterfaceManager{
	
	String currPane;
	UIElement paneObject;//This is the parent "window"
	
	ArrayList<String> tempPaneKeys = new ArrayList(0);
	
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
    			Thread.currentThread().wait(10);
    		}catch(InterruptedException e){
    			//gay.
    		}
    		paneObject.move(0, 0.1 * (-paneObject.sprite.frameY - paneObject.y));
    	}
    	
    	paneObject.addDelete();
    	
    	paneObject = null;
    	currPane = null;
    	
    	removeTempElements();
    }
    
    public void addTempElement(String key, UIElement E){
    	tempPaneKeys.add(key);
    	setElement(key,E);
    }
    
    public void removeTempElement(String key){
    	removeElement(key);
    	tempPaneKeys.remove(key);
    }
    
    public void removeTempElements(){
    	for(String S:tempPaneKeys){
    		removeElement(S);
    	}
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
    	
    	while(paneObject.y + paneObject.sprite.frameY<0){
    		try{
    			Thread.currentThread().wait(10);
    		}catch(InterruptedException e){
    			//gay.
    		}
    		paneObject.move(0, 0.1 * (0 - paneObject.y));
    	}
    	
    	//else if string to populate with content
    	if(nPane.compareTo("Hangar")==0){
    		
    		//pylons pane
    		addTempElement("pylonsBack",new UIElement(295,50,path+"InterfaceItem.png",25));
    		elements.get("pylonsBack").scaleTo(576,209);
    		
    		//hangar storage
    		addTempElement("hangarBack",new InterfaceList(232,280,342,400,25));
    		
    		//cargo space
    		addTempElement("cargoBack",new InterfaceList(590,280,342,400,25));
    		
    		//trash button
    		InterfaceButton trashButton = new InterfaceButton(520,688,path+"InterfaceButton.png",25,
    			null,null,null);
    		trashButton.text = "Trash Item";
    		trashButton.scaleTo(128,46);
    		addTempElement("trashButton",trashButton);
    		
    	}else if(nPane.compareTo("Shop")==0){
    		//shop tabs
    		
    		//shop inventory
    		addTempElement("shopBack",new InterfaceList(270,384,300,325,25));
    		
    		//hangar storage
    		addTempElement("hangarBack",new InterfaceList(600,384,300,325,25));
    		
    		//selection details subpane
    		addTempElement("detailsBack",new UIElement(220,55,path+"InterfaceItem.png",25));
    		elements.get("detailsBack").scaleTo(733,225);
    		
    		//buy button, sell button
    		InterfaceButton shopButton = new InterfaceButton(840,283,path+"InterfaceButton.png",25,
    			null,null,null);
    		shopButton.scaleTo(128,46);
    		addTempElement("shopButton",shopButton);
    		
    	}else if(nPane.compareTo("Market")==0){
    		//shop inventory
    		addTempElement("marketBack",new InterfaceList(220,75,735,280,25));
    		
    		//hangar storage
    		addTempElement("hangarBack",new InterfaceList(220,400,735,280,25));
    		
    		//buy button, sell button
    		InterfaceButton buyButton = new InterfaceButton(853,355,path+"InterfaceButton.png",25,
    			null,null,null);
    		buyButton.scaleTo(96,36);
    		addTempElement("buyButton",buyButton);
    		
    		InterfaceButton sellButton = new InterfaceButton(853,685,path+"InterfaceButton.png",25,
    			null,null,null);
    		sellButton.scaleTo(96,36);
    		addTempElement("sellButton",sellButton);
    		
    	}else if(nPane.compareTo("Shipyard")==0){
    		//shop inventory
    		addTempElement("shipsBack",new InterfaceList(220,75,290,565,25));
    		
    		//selection details subpane
    		addTempElement("detailsBack",new UIElement(540,100,path+"InterfaceItem.png",25));
    		elements.get("detailsBack").scaleTo(415,635);
    		
    		//buy button
    		InterfaceButton buyButton = new InterfaceButton(418,640,path+"InterfaceButton.png",25,
    			null,null,null);
    		buyButton.scaleTo(96,36);
    		addTempElement("buyButton",buyButton);
    		
    	}else if(nPane.compareTo("Missions")==0){
    		//missions available
    		addTempElement("missionsBack",new InterfaceList(280,380,280,335,25));
    		
    		//current missions
    		addTempElement("currMissionsBack",new InterfaceList(592,380,280,335,25));
    		
    		//mission details subpane
    		addTempElement("detailsBack",new UIElement(220,55,path+"InterfaceItem.png",25));
    		elements.get("detailsBack").scaleTo(735,275);
    		
    		InterfaceButton missionButton = new InterfaceButton(870,335,path+"InterfaceButton.png",25,
    			null,null,null);
    		missionButton.scaleTo(96,36);
    		addTempElement("missionButton",missionButton);
    		
    		
    	}else if(nPane.compareTo("Bar")==0){
    		
    		addTempElement("crewBack",new InterfaceList(220,75,290,565,25));
    		
    		//selection details subpane
    		addTempElement("detailsBack",new UIElement(540,100,path+"InterfaceItem.png",25));
    		elements.get("detailsBack").scaleTo(415,635);
    		
    		//buy button
    		InterfaceButton hireButton = new InterfaceButton(418,640,path+"InterfaceButton.png",25,
    			null,null,null);
    		hireButton.scaleTo(96,36);
    		addTempElement("hireButton",hireButton);
    		
    	}
    	
    	updatePane();
    	
    }
    
    public void updatePane(){
    	
    }
    
    public void undock(){
    	//change the game state yo
    	
    	//Again, this needs to seed game state parameters so that the same sector is consistent
    }
    
}