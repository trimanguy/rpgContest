/**
 * @(#)AI.java
 *
 *
 * @author 
 * @version 1.00 2012/7/24
 */


public class AI {
	
	double stepTimer;
	double stepDelay = 0.1;

    public AI() {
    	Global.state.activeAI = this;
    	
    }
    
    public void Step(){
    	if(Global.state.time < stepTimer) return;
    	stepTimer = Global.state.time + stepDelay;
    	
    	//go through all vessels
    	
    	for(Obj O:Global.state.activeObjs){
    		if(!(O instanceof ShipObj)) continue;
    		if(O == Global.state.playerObj) continue;
    		
    		ShipObj ship = (ShipObj) O;
    		
    		if(!ship.hasAI) continue;
    		
    		if(ship.aimTarget == null){
    			
    			//Find aim targets!
    			//I can't think of a way to do this that's faster than O(N^2)
    			
    			
    			if(ship.aimTarget == null){
    				//Ship idle or natural behavior happens here.
    				
    				ship.destAngle = Math.random()*360;
    				
    				ship.velocity += ship.tweenFactor * (ship.maxVelocity * Math.random() - ship.velocity);
    			}
    			
    		}else{
    			double targetAng = ship.getAngle(ship.aimTarget);
    			double targetDeltaAng = (targetAng+180) - ship.aimTarget.currAngle;
    			while(targetDeltaAng < -180) targetDeltaAng += 360;
    			while(targetDeltaAng > 180) targetDeltaAng -= 360;
    			
    			Vector2D DifPos = ship.getDistance(ship.aimTarget);
    			
    			if((ship.maxVelocity >= ship.aimTarget.maxVelocity || Math.abs(targetDeltaAng)>90) && 
    				(DifPos.length < ship.minIdealRange || DifPos.length > ship.maxIdealRange)){
    				//Control distance
    				
    				if(DifPos.length < ship.minIdealRange) ship.destAngle = targetAng + 180;
    				if(DifPos.length > ship.maxIdealRange) ship.destAngle = targetAng;
    				
    				ship.velocity += ship.tweenFactor * (ship.maxVelocity - ship.velocity);
    				
    			}else{
    				//Control direction
    				
    				ship.destAngle = targetAng + ship.idealTargetAng;
    				
    				ship.velocity += ship.tweenFactor * (0 - ship.velocity);
    			}
    			
    			ship.fireOn(ship.aimTarget);
    				
    		}
    	}
    }
    
}