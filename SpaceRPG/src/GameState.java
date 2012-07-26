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

	ShipObj playerObj = null;

    public GameState() {
    	
    	Global.state = this;
    	ArrayList<PointS> Pointss = new ArrayList(0);
    	
    	String image = "Resources/Sprites/EscortFrigate1-Thrust.png";
    	
    	File shipFile = new File("Data/ShipFile.txt");
    	Utils.parseShipFile(shipFile);
    	
    	File weaponFile = new File("Data/WeaponFile.txt");
    	Utils.parseWeaponFile(weaponFile);
    	
    	Global.state.playerObj = Utils.createShip("escort1","player");
    	Global.GUI = new ActiveInterface ();
    	for(Pylon P:Global.state.playerObj.pylons){
    		P.equipItem(Utils.createWeapon("testBlaster"));
    	}
    	
        double placeSize = 500;
    	
    	ShipObj ship1 = Utils.createShip("flak1"); 
        ship1.y = (Math.random()-0.5)*2*placeSize; ship1.x=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship1.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        
        ShipObj ship2 = Utils.createShip("escort1", "alliance");
        ship2.y = (Math.random()-0.5)*2*placeSize; ship2.x=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship2.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship2.velocity = 50;
        
        ShipObj ship3 = Utils.createShip("flak1");
        ship3.y = (Math.random()-0.5)*2*placeSize;ship3.x=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship3.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship3.velocity = 50;
        
        ShipObj ship4 = Utils.createShip("escort1", "alliance");
        ship4.x = (Math.random()-0.5)*2*placeSize; ship4.y=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship4.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship4.velocity = 50;
        
        ShipObj ship5 = Utils.createShip("escort1", "alliance");
        ship5.x = (Math.random()-0.5)*2*placeSize; ship5.y=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship5.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship5.velocity = 50;
        
        ShipObj ship6 = Utils.createShip("escort1","alliance");
        ship6.x = (Math.random()-0.5)*2*placeSize; ship6.y=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship6.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship6.velocity = 50;
        
        ShipObj ship7 = Utils.createShip("escort1","alliance");
        ship7.x = (Math.random()-0.5)*2*placeSize; ship7.y = (Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship7.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship7.velocity = 50;
        
        ShipObj ship8 = Utils.createShip("flak1");
        ship8.y = (Math.random()-0.5)*2*placeSize;ship8.x=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship8.pylons){
        	P.equipItem(Utils.createWeapon("testMissile"));
        }
        ship8.velocity = 50;
        
        ShipObj ship9 = Utils.createShip("flak1");
        ship9.y = (Math.random()-0.5)*2*placeSize;ship9.x=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship9.pylons){
        	P.equipItem(Utils.createWeapon("testMissile"));
        }
        ship9.velocity = 50;
        
        ShipObj ship10 = Utils.createShip("flak1");
        ship10.y = (Math.random()-0.5)*2*placeSize;ship10.x=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship10.pylons){
        	P.equipItem(Utils.createWeapon("testMissile"));
        }
        ship10.velocity = 50;
        
        ShipObj ship11 = Utils.createShip("flak1");
        ship11.y = (Math.random()-0.5)*2*placeSize;ship11.x=(Math.random()-0.5)*2*placeSize;
        for(Pylon P:ship11.pylons){
        	P.equipItem(Utils.createWeapon("testMissile"));
        }
        ship11.velocity = 50;
    }
    
    public void Tick(){
    	
    	dt = (System.currentTimeMillis() - lastTime) * speedMultiplier;
    	dtt = dt/1000;
    	lastTime = System.currentTimeMillis();
    	
    	//System.out.println("DELTA TIME: "+dt);
    	
    	time += dtt;
    	
    	activeAI.Step();
    	
    	for(Obj O:activeObjs){
    		O.Step();
    		if(O.CameraCanSee()){
    			Global.view.addDrawObject(O);
    		}else{
    			Global.view.removeDrawObject(O);
    		}
    	}
    	
    	for(Obj O:newObjBuffer){
    		O.Init();
    	}
    	newObjBuffer.clear();
    	
    	for(Obj O:deleteBuffer){
    		O.delete();
    	}
    	deleteBuffer.clear();
    	
    	//garbage collection when needGC flag is on
    	if ( (needGC==true)&&(time>=timeOfNeedGC+1) ){
    		System.gc();
    		needGC = false;
    		timeOfNeedGC = time;
    	}
    }
    
}