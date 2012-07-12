/**
 * Pylon object serving as equipment slots and turrets with angles and shit.
 * 
 * STILL UNDECIDED HOW TO PROCESS WEAPON GROUPS!
 */

import java.util.ArrayList;

public class Pylon {

	ShipObj source;
	
	GameObj target; //if it's appropriate. you know.
	
	ItemObj equipped;
	
	/* //Possibly deprecated... Up to you.
	 * //This stuff is for pylon clusters and weapon stacks...
	 *
	 * int capacity = 1;
	 * int clusterIndex = 0;
	 * ArrayList<WeaponObj> cluster = new ArrayList(0);
	 * ArrayList<Point2D> offsets = new(ArrayList(0);
	*/
	
	int size; //Module size. Smaller is bigger?
	String type;//Possible types: "weapon","engine","power","shield","support"
	
	String tag;//My thought is you assign a tag to a pylon and then a tag to the associated hitCircles
	
	double activateTimer;
	//The delay should depend upon the equipped module
	
	boolean activated=true; //Is the pylon's equipped item Activated?
	int activateParams; //For some reason this seemed appropriate. I don't know how else to pass the 
	//	Player's keypress to a sheild activation...
	
	
	double currAngle; //This is angle relative to world coordinates
	double selfAngle; //This is angle relative to the ship direction
	double maxAngVel; //Degrees per second
	//double angThrust; //angThrust = Mass*AngVel. Use this formula to calibrate. Set to 0 for static turrets.
	
	//All angles must be in degrees from 0 to 360
	
	double centerAngle; //This is the "centered" or "neutral" orientation of the pylon
	double arcAngle; //This is 1/2 the angular range of the pylon in degrees
	
	//This should be relative to the top left corner of the displayed sprite...
	double screenX; 
	double screenY;
	String gui; //For gui
	
	//Polar coordinates relative to the ship in world coordinates. 
	//Makes it convenient to concatenate ship rotation.
	double polarRadius; //This is in pixels
	double polarAngle; //This is in degrees
	
	//baseHealth and realHealth we already discussed.
	double baseHealth;
	double realHealth;
	
	//Flat armor!
	double flatArmor;
	
	

	

    public Pylon(ShipObj source, double radius, double angle, double centerAngle, double arcAngle, double screenX, double screenY, String image) {
    	//for testing purposes
    	baseHealth = 100;
    	type = "Weapon";
    	int size = 9999;
    	realHealth = 100;
    	//WeaponObj testGun = new WeaponObj("Resources/Sprites/PlasmaSmall.png", "Resources/Sprites/explode_2.png", 10 , 300.0, 30000.0, 0.0, 2.0, 180); //fake guns for testing
    	//public WeaponObj(String img, String hitImg, int life, double maxSpeed, double accel, double turnSpeed, double spread) {
    	//this.equipped = (ItemObj)testGun; 
    	//
    	this.polarRadius = radius;
    	this.polarAngle = angle;
    	this.centerAngle = centerAngle;
    	this.arcAngle = arcAngle;
    	this.screenX=screenX;
    	this.screenY=screenY;
    	this.gui=image;
    	//this.angThrust = angSpeed;
    	
    	this.selfAngle = centerAngle;
    		
    	if (source!=null){
    		source.pylons.add(this);
    		this.source = source;
    	}
    }
    
    public void addSource(GameObj s){
    	source = (ShipObj)s;
    }
    
    public boolean canEquip(ItemObj O){
    	return (O.type.compareTo(type)==0 && O.size >= size);
    }
    
    public void equipItem(ItemObj item){
    	this.equipped = item;
    	this.realHealth = baseHealth + item.baseHealth; 
    }
    
    public void Step(){//This should be called by the ShipObj during ShipObj.Step()
    	
    	realHealth = baseHealth/2*(Math.sin(Global.state.time/10)+1);
    	System.out.println(realHealth);
    	
    	if(equipped == null) return; //There is no equipped module!
    	
    	if(!equipped.canActivate) return; //Equipped is a passive module
    	
    	if(realHealth <= 0) return; //Pylon's been knocked out and will not function.
    	
    	
    	if(Global.state.time < activateTimer) return;
    	
    	activateTimer = Global.state.time+equipped.activateDelay;
    	
    	updateCurrentAngle();
    	
    	
    	if(activated){
	    	if(type.compareTo("Weapon")==0){//Since "string"=="string" is weird...
	    		
	    		WeaponObj weapon = (WeaponObj) equipped;
	    	//	System.out.println("pylon check");
	    		//First check if this weapon can fire based upon the context (ship power and ammunition)
	    		if(!weapon.canFire()) return;
	    		
	    		//figure out where the missile's origin is
				PointS P = getCoords();
				double nx = P.x;
				double ny = P.y;
	    		
	    		if(target == null){
	    			//fire the weapon regardless of target and pylon orientation? commented out for now
	    			//	weapon.Fire(nx,ny,currAngle, source, null);
	    		}else{
	    			//pewpew test
	    			//System.out.println("FIRE!!");
	    			//Align the weapon towards target and if the pylon pointing at target, fire.
	    			double targetAng=0;
	    			if(weapon.missileHoming){
	    				targetAng = getAngle(target);
	    			}
	    			else{
	    				targetAng = findTargetAng(target,weapon.missileMaxSpeed);
	    			}
	    			
	    			
	    			AlignTo(targetAng);
	    			
	    			
	    			System.out.println(""+targetAng+", "+selfAngle+": "+(targetAng-selfAngle));
	    			
	    			if(Math.abs(targetAng-selfAngle)<=weapon.angleSpread){
	    				//fire
	    				weapon.Fire(nx,ny,currAngle, source, target);
	    			}
	    		
	    			
	    			//this part for testing only, DELETE AFTER
	    			//weapon.Fire(nx,ny,currAngle, source, target);
	    		}
	    		
	    	}else if(type.compareTo("Shield")==0){
	    		//First check the ship power and if the direction determined 
	    		//	by activateParam is full health or not
	    		
	    		//Activate the shield; activateParam correlates with shield direction
	    		
	    	}else if(type.compareTo("Support")==0){
	    		//check if this support item can fire based upon the context (ship power and ammunition)
	    		
	    		//Activate the support module.
	    		
	    	}
    	}else{
    		//In the event of autoattack, find a target within the angular range and etc...
    		
    		//Then set activated = true
    		
    		
    		//Realign the pylon to the center orientation.
    		UpdateAngVel();
    		AlignTo(centerAngle);
    	}
    	
    }
    
    public void UpdateAngVel(){ 
    	//This is used to compute the maximum angular velocity based upon equipped itemObj
    	maxAngVel = 180;
    	if(type.compareTo("Weapon")==0 && equipped != null){
    		//simplify this down so maxAngVel is hardcoded in shipFile. Eliminate excess fields!
    		//System.out.println(""+this.angThrust+", "+equipped.mass);
    		WeaponObj weapon = (WeaponObj) equipped;
    		maxAngVel = weapon.turnSpeed;
    	}
    }
    
    public void initHealth(){ 
    	//Initializes realHealth to the maximum health.
    	realHealth = baseHealth;
    	if(equipped != null) realHealth += equipped.baseHealth;
    }
    
    public void updateCurrentAngle(){
    	currAngle = source.currAngle + selfAngle;
    	
    	if(currAngle >= 360) currAngle -= 360;
    	if(currAngle < 0) currAngle += 360;
    }
    
    public boolean AlignTo(double targetAng){
    	//first update the maximum angular speed
    	double deltaAng = findDeltaAng(targetAng);
    	
    	
    	selfAngle += deltaAng;
    	double checkAngle = selfAngle - centerAngle;
    	if(checkAngle < -arcAngle) selfAngle = centerAngle - arcAngle;
    	if(checkAngle > arcAngle) selfAngle = centerAngle + arcAngle;
    	
    	if(selfAngle < -180) selfAngle += 360;
    	if(selfAngle >= 180) selfAngle -= 360;   
    	
    	updateCurrentAngle();
    	
    	return (selfAngle == targetAng);
    }
    
    public double findDeltaAng(double destAngle){
    	UpdateAngVel();
    	
    	double deltaAng = (destAngle - selfAngle);
    	double maxDeltaAng = maxAngVel * Global.state.dtt;
    	
    	//System.out.println(""+deltaAng+", "+maxDeltaAng);
    	
    	if(deltaAng < -180) deltaAng += 360;
    	if(deltaAng > 180) deltaAng -= 360;
    	deltaAng = Math.min(maxDeltaAng,Math.max(-maxDeltaAng,deltaAng));
    	return deltaAng;
    }
    
    
    public double findTargetAng(GameObj target, double missileSpeed){
    	
    	if( (target==null)||(target==source) ) return currAngle; //Ship can't fire on itself
    	
    	//Compute the firing vector
    	double a,b,c,d,distance,t0,t1,t;
    	Vector2D V = new Vector2D(target.velX,target.velY);
    	Vector2D PO = new Vector2D(target.x-source.x,target.y-source.y);
    	Vector2D P = null;
    	
    	distance = PO.length;
    	
    	a = (V.dot(V))-(missileSpeed*missileSpeed);
    	b = PO.dot(V.multiply(2));
    	c = PO.dot(PO);
    	d = b*b - 4*a*c; //Doesn't this look familiar?
    	
    	if(d < 0) {
    		return currAngle; //No solution.
    	//d < 0 means that any bullet travelling at missileSpeed will never hit the target
    	
    	}
    	
    	t0 = (-b - Math.sqrt(d))/(2*a);
    	t1 = (-b + Math.sqrt(d))/(2*a);
    	
    	if(t0 < 0){ t = t1;
    	}else{
    		if(t1 < 0){ t = t0;
    		}else{
    			t = Math.min(t0,t1);
    		}
    	}
    	
    	P = PO.add(V.multiply(t)); //Vector to the target interception point relative to the source in world pixels.
    	
    	//Convert the firing vector to angle in degrees
    	double returnAngle = P.toAngle()-source.currAngle;
    	if(returnAngle < -180) returnAngle += 360;
    	if(returnAngle > 180) returnAngle -= 360;
    	
    	return returnAngle;
    }
    
    public double getAngle(GameObj O){//This reutrns an angle relative to the source object's angle.
    	if(source == null || O == null) return 0;
    	
    	double sx = source.x, sy = source.y;
    	PointS P = getCoords();
    	sx += P.x; sy += P.y;
    	
    	sx = O.x-sx;
    	sy = O.y-sy;
    	
    	double angle = Math.atan2(sy,sx);
    	if(angle < 0) angle += 360;
    	if(angle >=360) angle -= 360;
    	angle -= source.currAngle;
    	
    	return angle;
    }
    
    public PointS getCoords(){
		double nx = source.x+polarRadius*Math.cos(Math.toRadians(polarAngle+source.currAngle));
		double ny = source.y+polarRadius*Math.sin(Math.toRadians(polarAngle+source.currAngle));
    	return new PointS(nx,ny);
    }
}