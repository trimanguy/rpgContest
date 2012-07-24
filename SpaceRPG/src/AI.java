/**
 * @(#)AI.java
 *
 *
 * @author 
 * @version 1.00 2012/7/24
 */


public class AI {

	ShipObj ship;
	ShipObj target;
	
	String team;
	
	double idealTargetAngle;
	
	double minTargetDistance;
	double maxTargetDistance;
	
	double velocityTween = 1/2;
	
	double turnTimer;
	

    public AI(ShipObj nship) {
    	ship = nship;
    }
    
    public void Step(){
    	if(target == null){
    		IdleProcess();
    	}else{
    		HostileProcess();
    	}
    }
    
    public void IdleProcess(){
    	//This looks for hostile ships
    	//If there aren't any, then just fly around. Maybe sometimes dock at some place.
    	for(Obj E:Global.state.activeObjs){
    		if(E instanceof ShipObj){
    			if(ship.isHostile((GameObj) E)){
    				ShipObj O = (ShipObj) E;
    				
    				if(O.coreHealth <=0) continue;
    				
    				if(target == null){
    					target = O;
    				}else{
    					
    					double targetAngle = ship.getAngle(target);
    					double OAngle = ship.getAngle(O);
    					
    					if(Math.abs(targetAngle-ship.currAngle-idealTargetAngle) > 
    						Math.abs(OAngle-ship.currAngle-idealTargetAngle)){
    							
    						target = O;
    					}
    				}
    			}
    		}
    	}
    	//Child classes to AI can have different behaviors here
    	if(target == null){
    		if(Global.state.time >= turnTimer){
    			ship.destAngle = Math.random() * 360;
    			
    			ship.velocity += velocityTween * (ship.maxVelocity - ship.velocity);
    			
    			turnTimer = Global.state.time + Math.random() * 20;
    		}
    	}
    }
    
    public void HostileProcess(){
    	
    	if(target.coreHealth <=0){
    		target = null;
    		return;
    	}
    	
    	Vector2D DifPos = ship.getDistance(target);
    	double targetAngle = ship.getAngle(target);
    	
    	double targetDeltaAngle = target.destAngle-targetAngle+180;
    	if(targetDeltaAngle > 180) targetDeltaAngle -= 360;
    	if(targetDeltaAngle < -180) targetDeltaAngle += 360;
    	//First: If this ship has more speed than target or target isn't flying towards ship,
    	//	then engage distance control
    	
    	if((ship.maxVelocity >= target.maxVelocity || Math.abs(targetDeltaAngle) > 90) 
    		&& (DifPos.length < minTargetDistance || DifPos.length > maxTargetDistance)){
    		
    		ship.velocity += velocityTween * (ship.maxVelocity - ship.velocity); 
    		
    		if(DifPos.length < minTargetDistance) ship.destAngle = targetAngle + 180;
    		if(DifPos.length > maxTargetDistance) ship.destAngle = targetAngle;
    		
    	}else{
    		//Otherwise: Engage direction control.
    		
    		ship.velocity += velocityTween * (0-ship.velocity);
    		
    		ship.destAngle = targetAngle + idealTargetAngle;
    	}
    	
    	ship.fireOn(target);
    	
    	
    	
    }
}