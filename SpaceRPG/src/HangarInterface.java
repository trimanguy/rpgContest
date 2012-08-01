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
	String subPane;
	UIElement paneObject;//This is the parent "window"
	
	ArrayList<String> tempPaneKeys = new ArrayList(0);
	
    String path = "Resources/Interface/";
    
    HangarGameState hangarState;

    public HangarInterface() {
    	
    	hangarState = (HangarGameState) Global.state;
    	
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
    	//elements.put("missionsButton",missionsButton);
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
    	
    	selectedObject= null;
    	subPane = null;
    	
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
    		
    		//pylons interface
    		drawPylons(295, 50);
    		
    		//hangar storage
    		addTempElement("hangarBack",new InterfaceList(232,280,342,400,25));
    		
    		//cargo space
    		addTempElement("cargoBack",new InterfaceList(590,280,342,400,25));
    		
    		//trash button
    		InterfaceButton trashButton = new InterfaceButton(520,688,path+"InterfaceButton.png",25,
    			"trashItem",this,null);
    		trashButton.text = "Trash Item";
    		trashButton.scaleTo(128,46);
    		addTempElement("trashButton",trashButton);
    		
    	}else if(nPane.compareTo("Shop")==0){
    		//shop tabs!
    		
    		//shop inventory
    		addTempElement("shopBack",new InterfaceList(270,384,300,325,25));
    		
    		//hangar storage
    		addTempElement("hangarBack",new InterfaceList(600,384,300,325,25));
    		
    		//selection details subpane
    		addTempElement("detailsBack",new UIElement(220,55,path+"InterfaceItem.png",25));
    		elements.get("detailsBack").scaleTo(733,225);
    		
    		//buy button, sell button
    		InterfaceButton shopButton = new InterfaceButton(840,283,path+"InterfaceButton.png",25,
    			null,this,null);
    		shopButton.scaleTo(128,46);
    		addTempElement("shopButton",shopButton);
    		
    	}else if(nPane.compareTo("Market")==0){
    		//shop inventory
    		addTempElement("marketBack",new InterfaceList(220,75,735,280,25));
    		
    		//hangar storage
    		addTempElement("hangarBack",new InterfaceList(220,400,735,280,25));
    		
    		//buy button, sell button
    		InterfaceButton buyButton = new InterfaceButton(853,355,path+"InterfaceButton.png",25,
    			"buyItem",this,null);
    		buyButton.scaleTo(96,36);
    		addTempElement("buyButton",buyButton);
    		
    		InterfaceButton sellButton = new InterfaceButton(853,685,path+"InterfaceButton.png",25,
    			"sellItem",this,null);
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
    			"buyShip",this,null);
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
    			null,this,null);
    		missionButton.scaleTo(96,36);
    		addTempElement("missionButton",missionButton);
    		
    		
    	}else if(nPane.compareTo("Bar")==0){
    		
    		addTempElement("crewBack",new InterfaceList(220,75,290,565,25));
    		
    		//selection details subpane
    		addTempElement("detailsBack",new UIElement(540,100,path+"InterfaceItem.png",25));
    		elements.get("detailsBack").scaleTo(415,635);
    		
    		//buy button
    		InterfaceButton hireButton = new InterfaceButton(418,640,path+"InterfaceButton.png",25,
    			"buyItem",this,null);
    		hireButton.scaleTo(96,36);
    		addTempElement("hireButton",hireButton);
    		
    	}
    	
    	updatePane();
    	
    }
    
    public void trashItem(){
    	if(selectedObject == null) return;
    	
    	if(!hangarState.playerCargo.contains(selectedObject) && !hangarState.playerVault.contains(selectedObject)) return;
    	
    	hangarState.playerCargo.remove(selectedObject);
    	hangarState.playerVault.remove(selectedObject);
    }
    
    public void buyItem(){
    	if(selectedObject == null) return;
    	
    	ItemObj base = (ItemObj) selectedObject;
    	
    	double value = base.value;
    	if(hangarState.commodityDemand.get(base.name)!=null && hangarState.commodityDemand.get(base.name)>0){
    		value *= hangarState.commodityDemand.get(base.name);
    	}
    	if(hangarState.playerMoney >= base.value){
    		hangarState.playerMoney -= base.value;
    	}else{
    		return;
    	}
    	
    	ItemObj item = Utils.createItem(base.type,base.name);
    	
    	hangarState.playerVault.add(item);
    	
    	selectedObject = null;
    	
    	updatePane();
    }
    
    public void sellItem(){
    	if(selectedObject == null) return;
    	
    	ItemObj base = (ItemObj) selectedObject;
    	
    	double value = base.value;
    	if(hangarState.commodityDemand.get(base.name)!=null && hangarState.commodityDemand.get(base.name)>0){
    		value *= hangarState.commodityDemand.get(base.name);
    	}
    	hangarState.playerMoney += value*0.75;
    	
    	hangarState.playerVault.remove(base);
    	hangarState.playerCargo.remove(base);
    	
    	selectedObject = null;
    	
    	updatePane();
    }
    
    public void buyShip(){
    	if(selectedObject == null) return;
    	
    	ShipObj base = (ShipObj) selectedObject;
    	
    	if(hangarState.playerMoney >= base.value){
    		hangarState.playerMoney -= base.value;
    		hangarState.playerMoney += hangarState.playerObj.value*0.75;
    	}else{
    		return;
    	}
    	
    	for(Pylon P:hangarState.playerObj.pylons){
    		ItemObj I = P.equipped;
    		CharacterObj slave = P.crew;
    		P.unequipCrew();
    		P.unequipItem();
    		hangarState.playerVault.add(I);
    		hangarState.playerVault.add(slave);
    		
    	}
    	hangarState.playerObj.addDelete();
    	
    	hangarState.playerObj = Utils.createShip(base.name,"allied");
    }
    
    public void acceptMission(){
    	if(selectedObject == null) return;
    	
    }
    
    public void abortMission(){
    	if(selectedObject == null) return;
    	
    }
    
    public void drawPylons(double x, double y){
    	if(hangarState.playerObj!=null){
    		ShipObj ship = hangarState.playerObj;
    		for(int i=0;i<ship.pylons.size();i++){
    			Pylon P = ship.pylons.get(i);
    			
    			if(P == null) continue;
    			if(P.gui == null) continue;
    			
    			InterfacePylon gui = new InterfacePylon(P,x,y);
    			
    			gui.dx = 1.5;
    			gui.dy = 1.5;
    			
    			addTempElement("Pylon"+P.tag+""+i,gui);
    		}
    	}
    }
    
    public void updatePane(){
    	
    	if(currPane.compareTo("Hangar")==0){
    		((InterfaceList) elements.get("hangarBack")).values = new ArrayList(hangarState.playerVault);
    		((InterfaceList) elements.get("cargoBack")).values = new ArrayList(hangarState.playerCargo);
    		
	    	if(hangarState.playerObj!=null){
	    		ShipObj ship = hangarState.playerObj;
	    		for(int i=0;i<ship.pylons.size();i++){
	    			Pylon P = ship.pylons.get(i);
	    			
	    			if(P == null) continue;
	    			if(P.gui == null) continue;
	    			String key = "Pylon"+P.tag+""+i;
	    			InterfacePylon gui = (InterfacePylon) getElement(key);
	    			
	    			if(gui.pylon.equipped!=null && subPane == "pylons"){
	    				if(gui.item == null) gui.item = new InterfaceItem(gui.pylon.equipped);
	    				gui.item.base = gui.pylon.equipped;
	    				gui.item.getIcon(gui.item.base);
	    			}else if(gui.pylon.crew!=null && subPane == "crew"){
	    				if(gui.item == null) gui.item = new InterfaceItem(gui.pylon.crew);
	    				gui.item.base = gui.pylon.crew;
	    				gui.item.getIcon(gui.item.base);
	    			}else{
	    				gui.item = null;
	    			}
	    		}
	    	}
    		
    		
    	}else if(currPane.compareTo("Shop")==0){
    		((InterfaceList) elements.get("hangarBack")).values = new ArrayList(hangarState.playerVault);
    		((InterfaceList) elements.get("shopBack")).values = new ArrayList(hangarState.equipShop);
    		
    		if(selectedObject != null && selectedObject instanceof ItemObj){
    			//Update the descriptions pane
    			ItemObj item = (ItemObj) selectedObject;
    			String text = "\n\n"+item.name;
    			text += "\n"+item.descrip;
    			text += "\n\n"+item.getDescription();
    			
    			//If the selected Object is in cargo, then change the shopButton to sell
    			
    			//Otherwise change shopButton to buy
    			InterfaceButton shopButton = (InterfaceButton) getElement("shopButton");
    			if(hangarState.playerCargo.contains(selectedObject)){
    				shopButton.text = " Sell";
    				shopButton.callMethod = "sellItem";
    				
    			}else{
    				shopButton.text = " Buy";
    				shopButton.callMethod = "buyItem";
    			}
    		}
    			
    	}else if(currPane.compareTo("Market")==0){
    		((InterfaceList) elements.get("hangarBack")).values = new ArrayList(hangarState.playerVault);
    		((InterfaceList) elements.get("marketBack")).values = new ArrayList(hangarState.marketShop);
    		
    		//Nothing special here!
    		
    	}else if(currPane.compareTo("Shipyard")==0){
    		((InterfaceList) elements.get("shipsBack")).values = new ArrayList(hangarState.shipShop);
    		
    		if(selectedObject != null && selectedObject instanceof ShipObj){
    			ShipObj ship = (ShipObj) selectedObject;
    			String text = "\n\n"+ship.name+"\n\n";
    			
    			for(Pylon P:ship.pylons){
    				String pylonType="";
    				if(P.allowedType.indexOf("w")>=0) pylonType = "Weapon";
    				if(P.allowedType.indexOf("s")>=0) pylonType = "Shield";
    				if(P.allowedType.indexOf("c")>=0) pylonType = "PowerCore";
    				if(P.allowedType.indexOf("e")>=0) pylonType = "Engine";
    				if(P.allowedType.indexOf("p")>=0) pylonType = "Support";
    					
    				text += "\n"+pylonType+" Pylon: "+P.baseHealth+" BaseHealth, "+P.arcAngle+" Angular Range";
    			}
    			
    			elements.get("detailsBack").text = text;
    		}
    		
    	}else if(currPane.compareTo("Missions")==0){
    		//((InterfaceList) elements.get("shopBack")).values = new ArrayList(hangarState.missions);
    		
    		//Update the interfacelists for missions
    		
    		//Next, if a mission is selected, update the details bit
    		
    	}else if(currPane.compareTo("Bar")==0){
    		((InterfaceList) elements.get("crewBack")).values = new ArrayList(hangarState.crewShop);
    		
    		if(selectedObject != null && selectedObject instanceof CharacterObj){
    			CharacterObj crew = (CharacterObj) selectedObject;
    			String text = "\n\n"+crew.name;
    			text += "\n\n\t Profession: "+crew.job;
    			text += "\n\t Level: "+crew.level;
    			text += "\n\n\t Gunnery: \t"+crew.gunnery;
    			text += "\n\t Accuracy: \t"+crew.accuracy;
    			text += "\n\t Efficiency: \t"+crew.efficiency;
    			text += "\n\t Damage Control: \t"+crew.dmgControl;
    			text += "\n\t Calibration: \t"+crew.calibration;
    			text += "\n\t Engineering: \t"+crew.engineering;
    			
    			text += "\n\n Hire Cost: \t$"+(crew.level*150);
    			
    			elements.get("detailsBack").text = text;
    		}
    		
    	}
    	
    }
    
    public void undock(){
    	//change the game state yo
    	
    	//Again, this needs to seed game state parameters so that the same sector is consistent
    }
    
}