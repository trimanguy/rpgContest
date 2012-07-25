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
    	
    	Global.state.playerObj = Utils.createShip("escort1","alliance");
    	Global.GUI = new ActiveInterface ();
    	for(Pylon P:Global.state.playerObj.pylons){
    		P.equipItem(Utils.createWeapon("testBlaster"));
    	}
    	
    	ShipObj ship1 = Utils.createShip("flak1"); 
        for(Pylon P:ship1.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        
        ShipObj ship2 = Utils.createShip("escort1", "alliance");
        ship2.y = 50; ship2.x=30;
        for(Pylon P:ship2.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship2.velocity = 50;
        
        ShipObj ship3 = Utils.createShip("flak1");
        ship3.y = -40;ship3.x=10;
        for(Pylon P:ship3.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship3.velocity = 50;
        
        ShipObj ship4 = Utils.createShip("escort1", "alliance");
        ship4.x = -20; ship4.y=100;
        for(Pylon P:ship4.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship4.velocity = 50;
        
        ShipObj ship5 = Utils.createShip("escort1", "alliance");
        ship5.x = -30; ship5.y=150;
        for(Pylon P:ship5.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship5.velocity = 50;
        
        ShipObj ship6 = Utils.createShip("escort1","alliance");
        ship6.x = -20; ship6.y=200;
        for(Pylon P:ship6.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship6.velocity = 50;
        
        ShipObj ship7 = Utils.createShip("escort1","alliance");
        ship7.x = -20; ship7.y=-100;
        for(Pylon P:ship7.pylons){
        	P.equipItem(Utils.createWeapon("testBlaster"));
        }
        ship7.velocity = 50;
    }
    
    public void Tick(){
    	
    	dt = System.currentTimeMillis() - lastTime;
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