/**
 * There will probably be multiple game states.
 * Game states I have in mind:
 * - Undocked and flying in space and possibly in combat
 * - Docked to a station and navigating UI or walking around station
 * - Navigating main menu UI
 *
 * The whole point is that in these distinct gamestates, different types of objects will be coming into play
 * and the same objects may behave differently under different states.
 * With the different gamestate subclasses, we can change how the game behaves by overriding Tick().
 */

import java.util.*;
import java.io.*;

public class GameState {

	double lastTime;
	double time = 0.0;
	double timeOfNeedGC = 0.0;
	double dt = 0.0;
	double dtt = 0.0; //this is dtt
	Boolean needGC = false;
	
	double speedMultiplier = 1;
	
	
	ArrayList<Obj> activeObjs = new ArrayList(0);
	ArrayList<Obj> newObjBuffer = new ArrayList(0);
	ArrayList<Obj> deleteBuffer = new ArrayList(0);
	AI activeAI = new AI();

	//savefile stuff
	ShipObj playerObj = null;
	String playerName = "";
	double playerMoney = 0;
	double playerProgress = 0;
	ArrayList<ItemObj> playerCargo = new ArrayList(0);
	ArrayList<ItemObj> playerVault = new ArrayList(0);

    public GameState() {
    	
    	Global.state = this;
    	ArrayList<PointS> Pointss = new ArrayList(0);
    	
    	Utils.parseShipFile("Data/ShipFile.txt");
    	
    	Utils.parseWeaponFile("Data/WeaponFile.txt");
    	
    	Utils.parseEngineFile("Data/EngineFile.txt");
    	
    	Utils.parsePowerCoreFile("Data/PowerCoreFile.txt");
    	
    	Utils.parseShieldFile("Data/ShieldFile.txt");
    	
    	//need to add method for player to change ship
    	//playerObj = createEscort1(500,"player");
    	/*
    	for(int z=0; z<Global.state.playerObj.pylons.size();z++){
    		Pylon P = Global.state.playerObj.pylons.get(z);
    		P.crew=new CharacterObj();
    	}
    	*/
    	//Global.state.playerObj.pylons.get(6).equipItem(Utils.createEngine("imbaEngine"));
		Utils.filepath=Utils.getDataFolder();
		//Utils.writeToFile("hello's savefile","helloooooo");
		//Utils.writeToFile("goodbye's savefile","gooooooodbye");
		
        createEscort1(500,"ally");
        createEscort1(500,"ally");
        createEscort1(500,"none");
        createEscort1(500,"none");
        createFlak1(500,"ally");
        createFlak1(500,"ally");
        createFlak1(500,"ally");
        createFlak1(500,"none");
        createFlak1(500,"none");
        createFlak1(500,"none");
        createFlak1(500,"none");
        createFlak1(500,"none");
        Utils.loadGame("Triman's savefile.txt");
        System.out.println("playername: "+playerName+ " money "+playerMoney+ " progress "+playerProgress);
        /*
        playerName = "Triman";
        playerMoney = 99;
        playerProgress = 9;
        playerCargo.add(Utils.createWeapon("testBlaster"));
        playerCargo.add(Utils.createPowerCore("testCore"));
        playerCargo.add(Utils.createPowerCore("testCore"));
        playerVault.add(Utils.createShield("testShield"));
        playerVault.add(Utils.createWeapon("testBlaster"));
        Utils.saveGame(playerName);
        */
        
        Global.GUI = new ActiveInterface();
    }
    
    public ShipObj createEscort1(int placeSize, String faction){
    	ShipObj ship = Utils.createShip("escort1",faction);
    	ship.y = (Math.random()-0.5)*2*placeSize;ship.x=(Math.random()-0.5)*2*placeSize;
    	
    	ship.pylons.get(0).equipItem(Utils.createWeapon("testBlaster"));
    	ship.pylons.get(1).equipItem(Utils.createWeapon("testBlaster"));
    	ship.pylons.get(2).equipItem(Utils.createWeapon("testBlaster"));
    	ship.pylons.get(3).equipItem(Utils.createWeapon("testMissile"));
    	ship.pylons.get(4).equipItem(Utils.createPowerCore("testCore"));
    	ship.pylons.get(5).equipItem(Utils.createShield("testShield"));
    	
    	//ship.pylons.get(6).equipItem(Utils.createEngine("testEngine"));
    	
    	ship.setAngle(Math.random()*360);
    	
    	return ship;
    }
    
    
    public ShipObj createEscort2(int placeSize, String faction){
    	ShipObj ship = Utils.createShip("escort2", faction);
    	ship.y = (Math.random()-0.5)*2*placeSize;ship.x=(Math.random()-0.5)*2*placeSize;
    	ship.pylons.get(0).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(4).equipItem(Utils.createPowerCore("testCore"));
    	ship.pylons.get(5).equipItem(Utils.createWeapon("testMissile"));
    	ship.pylons.get(6).equipItem(Utils.createEngine("imbaEngine"));
    	
    	ship.setAngle(Math.random()*360);
    	
    	return ship;
    }
    
    public ShipObj createFlak1(int placeSize,String faction){
    	ShipObj ship = Utils.createShip("flak1",faction);
    	ship.y = (Math.random()-0.5)*2*placeSize;ship.x=(Math.random()-0.5)*2*placeSize;
		ship.pylons.get(0).equipItem(Utils.createWeapon("testMissile"));
    	ship.pylons.get(1).equipItem(Utils.createEngine("testEngine"));
    	
    	ship.setAngle(Math.random()*360);
    	
    	return ship;
    }
    
    public ShipObj createCerberus(int placeSize, String faction){
    	ShipObj ship = Utils.createShip("Cerberus",faction);
    	ship.y = (Math.random()-0.5)*2*placeSize;ship.x=(Math.random()-0.5)*2*placeSize;
    	ship.pylons.get(0).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(1).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(2).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(3).equipItem(Utils.createWeapon("testMissile"));
    	ship.pylons.get(4).equipItem(Utils.createShield("testShield"));
    	ship.pylons.get(5).equipItem(Utils.createShield("testShield"));
    	ship.pylons.get(6).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(7).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(8).equipItem(Utils.createEngine("imbaEngine"));
    	ship.pylons.get(9).equipItem(Utils.createPowerCore("testCore"));
    	ship.pylons.get(9).equipCrew(new CharacterObj());
    	
    	ship.setAngle(Math.random()*360);
    	
    	return ship;
    }
    
    public ShipObj createValiant(int placeSize, String faction){
    	ShipObj ship = Utils.createShip("Valiant",faction);
    	ship.y = (Math.random()-0.5)*2*placeSize;ship.x=(Math.random()-0.5)*2*placeSize;
    	ship.pylons.get(0).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(1).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(2).equipItem(Utils.createShield("testShield"));
    	ship.pylons.get(3).equipItem(Utils.createPowerCore("testCore"));
    	ship.pylons.get(4).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(5).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(6).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(7).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(8).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(9).equipItem(Utils.createWeapon("testMissile"));
    	ship.pylons.get(10).equipItem(Utils.createWeapon("testMissile"));
    	ship.pylons.get(11).equipItem(Utils.createEngine("imbaEngine"));
    	ship.pylons.get(9).equipCrew(new CharacterObj());
    	
    	ship.setAngle(Math.random()*360);
    	
    	return ship;
    }
    
    //ship.pylons.get(1).equipItem(Utils.createEngine("imbaEngine"));
    //ship.pylons.get(0).equipItem(Utils.createWeapon("testBlaster2"));
    //ship.pylons.get(2).equipItem(Utils.createShield("testShield"));
    
    public ShipObj createBloodHawk(int placeSize, String faction){
    	ShipObj ship = Utils.createShip("Blood Hawk",faction);
    	ship.y = (Math.random()-0.5)*2*placeSize;ship.x=(Math.random()-0.5)*2*placeSize;
    	ship.pylons.get(0).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(1).equipItem(Utils.createEngine("imbaEngine"));
    	ship.pylons.get(2).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(3).equipItem(Utils.createPowerCore("testCore"));
    	ship.pylons.get(4).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(5).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(6).equipItem(Utils.createShield("testShield"));
    	
    	ship.setAngle(Math.random()*360);
    	
    	return ship;
    }
    
    public ShipObj createReaver(int placeSize, String faction){
    	ShipObj ship = Utils.createShip("Reaver",faction);
    	ship.y = (Math.random()-0.5)*2*placeSize;ship.x=(Math.random()-0.5)*2*placeSize;
    	ship.pylons.get(0).equipItem(Utils.createEngine("imbaEngine"));
    	ship.pylons.get(1).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(2).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(3).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(4).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(5).equipItem(Utils.createWeapon("testBlaster2"));
    	ship.pylons.get(6).equipItem(Utils.createPowerCore("testCore"));
    	ship.pylons.get(7).equipItem(Utils.createWeapon("testBlaster2"));
    	
    	ship.setAngle(Math.random()*360);
    	
    	return ship;
    }
   
    public ShipObj createClipper(int placeSize, String faction){
    	ShipObj ship = Utils.createShip("Clipper",faction);
    	ship.y = (Math.random()-0.5)*2*placeSize;ship.x=(Math.random()-0.5)*2*placeSize;
    	ship.pylons.get(0).equipItem(Utils.createWeapon("imbaBlaster"));
    	ship.pylons.get(1).equipItem(Utils.createWeapon("imbaBlaster"));
    	ship.pylons.get(2).equipItem(Utils.createWeapon("imbaBlaster"));
    	ship.pylons.get(3).equipItem(Utils.createShield("imbaShield"));
    	ship.pylons.get(4).equipItem(Utils.createPowerCore("testCore"));
		ship.pylons.get(5).equipItem(Utils.createEngine("imbaEngine"));
    	
    	ship.setAngle(Math.random()*360);
    	
    	return ship;
    }
    
    public ShipObj createPrometheus(int placeSize, String faction){
    	ShipObj ship = Utils.createShip("Prometheus",faction);
    	ship.y = (Math.random()-0.5)*2*placeSize;ship.x=(Math.random()-0.5)*2*placeSize;
    	ship.pylons.get(0).equipItem(Utils.createWeapon("imbaBlaster"));
    	ship.pylons.get(1).equipItem(Utils.createWeapon("imbaBlaster"));
    	ship.pylons.get(2).equipItem(Utils.createWeapon("imbaBlaster"));
    	ship.pylons.get(3).equipItem(Utils.createWeapon("imbaBlaster"));
    	ship.pylons.get(4).equipItem(Utils.createShield("imbaShield"));
    	ship.pylons.get(5).equipItem(Utils.createWeapon("imbaBlaster"));
    	ship.pylons.get(6).equipItem(Utils.createPowerCore("testCore"));
		ship.pylons.get(7).equipItem(Utils.createEngine("imbaEngine"));
    	
    	ship.setAngle(Math.random()*360);
    	
    	return ship;
    }
    
    public void Tick(){
    	
    	dt = (System.currentTimeMillis() - lastTime) * speedMultiplier;
    	dtt = dt/1000;
    	lastTime = System.currentTimeMillis();
    	
    	
    	time += dtt;
    	
    //	System.out.println("TIME: "+time);
    	
    	activeAI.Step();
    	
    	for(Obj O:activeObjs){
    		O.Step();
    		if(O.CameraCanSee()){
    			Global.view.addDrawObject(O);
    			//System.out.println("ADD DRAW OBJECT "+O);
    		}else{
    			Global.view.removeDrawObject(O);
    			//System.out.println("REMOVE DRAW OBJECT "+O);
    		}
    	}
    	
    	//System.out.println("NEW OBJECT BUFFER");
    	for(Obj O:newObjBuffer){
    		//System.out.println("NEW OBJECT "+O);
    		O.Init();
    	}
    	newObjBuffer.clear();
    	
    	//System.out.println("DELETE OBJECT BUFFER");
    	for(Obj O:deleteBuffer){
    		//System.out.println("DELETE OBJECT "+O);
    		O.delete();
    	}
    	deleteBuffer.clear();
    	
    	//garbage collection when needGC flag is on
    	if ( (needGC==true)&&(time>=timeOfNeedGC+1) ){
    		//System.out.println("GARBAGE COLLECTION INVOKATION");
    		System.gc();
    		needGC = false;
    		timeOfNeedGC = time;
    	}
    }
    
}