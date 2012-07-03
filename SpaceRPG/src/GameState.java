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
	Boolean needGC = false;
	
	
	ArrayList<Obj> activeObjs = new ArrayList(0);
	ArrayList<Obj> newObjBuffer = new ArrayList(0);
	ArrayList<Obj> deleteBuffer = new ArrayList(0);
	
	ShipObj playerObj = null;

    public GameState() {
    	Global.state = this;
    	ArrayList<PointS> Pointss = new ArrayList(0);
    	
    	String image = "Resources/Sprites/EscortFrigate1-Thrust.png";
    	
    	ArrayList<HitCircle> gah= new ArrayList(0);
    	Global.state.playerObj = new ShipObj(image, Global.codeContext, 50, 0.5, gah);
    	new ShipObj(image, Global.codeContext, 35, 0.5, gah);
    	
    	File shipFile = new File("Data/ShipFile.txt");
    	Utils.parseShipFile(shipFile);
    	ShipObj flak1 = (ShipObj)Utils.shipTable.get("flak1");
    	new ShipObj(flak1.imageName, Global.codeContext,flak1.velocity,flak1.maxAngVel, flak1.hitCircles);
    	
    	new ActiveInterface ();
    }
    
    public void Tick(){
    	
    	dt = System.currentTimeMillis() - lastTime;
    	lastTime = System.currentTimeMillis();
    	
    	//System.out.println("DELTA TIME: "+dt);
    	
    	//time = System.currentTimeMillis()/1000;
    	//dt = 10;
    	
    	time += dt/1000;
    	
    	for(Obj O:activeObjs){
    		O.Step();
    		if(O.CameraCanSee()){
    			Global.view.addDrawObject(O);
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