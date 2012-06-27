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

public class GameState {

	double time = 0.0;
	
	ArrayList<Obj> activeObjects = new ArrayList(0);
	Obj playerObject = null;

    public GameState() {
    	Global.state = this;
    	ArrayList<PointS> Pointss = new ArrayList(0);
    	
    	String image = "Resources/Sprites/Flak Frigate 1 - Thrust.png";
    	
    	new TestObj(image, Global.codeContext);
    }
    
    public void Tick(){
    	time = System.currentTimeMillis()/1000;
    	for(Obj O:activeObjects){
    		O.Step();
    		if(O.CameraCanSee()){
    			Global.view.addDrawObject(O);
    		}
    	}
    }
    
}