/**
 * @(#)HangarGameState.java
 *
 *
 * @author 
 * @version 1.00 2012/8/1
 */

import java.util.ArrayList;

public class HangarGameState extends GameState{
	
	//equipment sold. PREFERRABLY IN DIFFERENT LISTS SO I CAN USE TABS?
	ArrayList<ItemObj> equipShop = new ArrayList(0);
		
	//commodities sold
	ArrayList<Commodity> marketShop = new ArrayList(0);
	
	//ships sold
	ArrayList<ShipObj> shipShop = new ArrayList(0);
	
	//missions available(?)
	
	//crew-members hireable
	ArrayList<CharacterObj> crewShop = new ArrayList(0);
	
	
    public HangarGameState() {
    	
    	//The parameters need to be seeded...
    	
    	//And then processed to generate shop inventories and interactivity.
    	
    	//lastly, create the hangarInterface
    	Global.GUI = new HangarInterface();
    }
    
    public void Tick(){
    	dt = (System.currentTimeMillis() - lastTime) * speedMultiplier;
    	dtt = dt/1000;
    	lastTime = System.currentTimeMillis();
    	
    	time += dtt;
    	
    	activeAI.Step();
    	
    	for(Obj O:activeObjs){
    		O.Step();
    	}
    	
    	for(Obj O:newObjBuffer){
    		O.Init();
    	}
    	newObjBuffer.clear();
    	
    	for(Obj O:deleteBuffer){
    		O.delete();
    	}
    	deleteBuffer.clear();
    	
    	if ( (needGC==true)&&(time>=timeOfNeedGC+1) ){
    		System.gc();
    		needGC = false;
    		timeOfNeedGC = time;
    	}
    }
    
}