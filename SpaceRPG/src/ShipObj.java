/**
 * @(#)ShipObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/22
 */
 
import java.util.*;
import java.net.*;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Color;


public class ShipObj extends GameObj {

    //Important imports from GameObj:
    //	ArrayList hitBoxes
    //	ArrayList pylons
    
    static ArrayList<ShipObj> allShips = new ArrayList(0);
	
    //double destAngle;
    String imageName;
    String descrip;
    double maxAngVel = 10; // Degrees per second
    double velocity = 0; //Pixels per second
    double maxVelocity;
    ShipObj aimTarget = null; //ship's target
    
    //both the timer and delay are in seconds.
    double fireTimer;
    double fireDelay = 1;//25;
    
    //shield health!
    double shieldForward=500;
    double shieldLeft=500;
    double shieldRight=500;
    double shieldRear=500;
    double shieldChargeTimer;
    double maxShield=500;
    
    //ship's power
    double maxPower; //energy bar size
    double powerMade; //base energy bar regen speed/sec
    double powerUsed; //sum of all energy used/sec (including engine, so remember to update upon speed up/down)
    double powerRegen; //maxPower-powerUsed; this is how much energy is in energy bar
    double currPower; //how much power we currently has available
    
	//ship's faction, defaults to pirate
	String faction = "pirate";
	
	//AI related fields
	boolean hasAI = true;
	double minIdealRange = 600;
	double maxIdealRange = 800;
	double tweenFactor = 0.1;
	double idealTargetAng = 0;
    
    
    public double getSpeed(){
    	return this.velocity;
    }
    
    /*** Ship Constructor, puts ship on screen ***/
    public ShipObj(String image, URL spritecontext, ArrayList<HitCircle> hitboxes, ArrayList<Pylon> newPylons, String descrip) { 
    	if(image != null && spritecontext != null)
    	{
    		sprite = new Sprite(image, spritecontext, true);
	    	context = spritecontext;
	    	
	    	hitCircles = hitboxes;
	    	pylons = newPylons;
	    	this.descrip = descrip;
	    	allShips.add(this);
	    	Global.state.newObjBuffer.add(this);
	    	size = 32;
    	}
    }
    
    /*** Ship Data Constructor, similar to C++ struct ***/
    public ShipObj(String image, ArrayList<HitCircle> hitboxes, ArrayList<Pylon> newPylons, String descrip){
    	//don't need to add to allShips cuz this just template obj
    	imageName = image;
    	hitCircles = hitboxes;
    	pylons = newPylons;
    	this.descrip = descrip;	
    	
    }
    
    public ShipObj findTarget(){
    	//autotarget if no aimTarget or aimTarget too far
    	
    	if (aimTarget==null){
    		//TODO: autotarget, do auto-targetting here
    		return null;
    	} else {
    		//if we already have target, continue to fire on it
    		return aimTarget;
    	}
    }
    
    public boolean isHostile(GameObj target){
    	//hostility stuff has to be processed here?
    	return this.faction != ((ShipObj)target).faction;
    }
    
    public void fireOn(ShipObj target){
    	for(int x = 0; x<pylons.size(); x++){
    		Pylon currPylon = pylons.get(x);
    		if ((currPylon.equipped!=null)&&(currPylon.equipped.type == "Weapon")){
    			currPylon.setTarget = target;
    		}
    	}
    }
    
    /*** Find how much to rotate this step ***/
    public double findDeltaAng(double targetAngle){
    	if(targetAngle >= 360) targetAngle -= 360;
    	if(targetAngle <0) targetAngle += 360;
    	double deltaAng = (targetAngle - currAngle);
    	double maxDeltaAng = maxAngVel*Global.state.dtt;
    	
    	if(deltaAng < -180) deltaAng += 360;
    	if(deltaAng > 180) deltaAng -= 360;
    	deltaAng = Math.min(maxDeltaAng,Math.max(-maxDeltaAng,deltaAng));
    	return deltaAng;
    }
    
    public void takeDmg(double amount){
    	this.currCoreHealth -= amount;
    	
    	if (this.currCoreHealth<=0){
    		//this.pylons=null;
    		this.addDelete();
    	}
    	
    }
    
    public void addDelete(){
    	super.addDelete();
    	
    	if(Global.state.playerObj.aimTarget == this)
    		Global.state.playerObj.aimTarget = null;
    	
    	
    	double nx = this.x + (Math.random()-0.5)*2*10;
    	double ny = this.y + (Math.random()-0.5)*2*10;
    	
    	new AnimatedParticle("Resources/Sprites/explode_1.png", Global.codeContext, 
	        			0.05, nx, ny);
	        			
    	nx = this.x + (Math.random()-0.5)*2*10;
    	ny = this.y + (Math.random()-0.5)*2*10;
    	
    	new AnimatedParticle("Resources/Sprites/explode_1.png", Global.codeContext, 
	        			0.05, nx, ny);
	        			
    	nx = this.x + (Math.random()-0.5)*2*size;
    	ny = this.y + (Math.random()-0.5)*2*size;
    	
    	new AnimatedParticle("Resources/Sprites/explode_3.png", Global.codeContext, 
	        			0.05, nx, ny);
    	nx = this.x + (Math.random()-0.5)*2*size;
    	ny = this.y + (Math.random()-0.5)*2*size;
    	
    	new AnimatedParticle("Resources/Sprites/explode_3.png", Global.codeContext, 
	        			0.05, nx, ny);
    	nx = this.x + (Math.random()-0.5)*2*size;
    	ny = this.y + (Math.random()-0.5)*2*size;
    	
    	new AnimatedParticle("Resources/Sprites/explode_3.png", Global.codeContext, 
	        			0.05, nx, ny);
    	nx = this.x + (Math.random()-0.5)*2*size;
    	ny = this.y + (Math.random()-0.5)*2*size;
    	
    	new AnimatedParticle("Resources/Sprites/explode_3.png", Global.codeContext, 
	        			0.05, nx, ny);
    }
    
    public void Step(){
    	//Ship alive?
    	if(this.currCoreHealth>0){
    		
	    	//Combat 
	    	this.fireOn(this.findTarget());
	    	
	    	//Process power generation
	    		
	    	//Process shield actions
	    	
	    	//Pylon actions
	    	for(int x=0;x<this.pylons.size();x++){
	    		pylons.get(x).Step();
	    	}
	    	
	    	//Ship Rotation
	    	rotate( this.findDeltaAng(destAngle) );
	    	
	    	//Setting Ship Frame
	    	int frame;
	    	frame = (int) Math.round(currAngle/5)+1;
	    	sprite.setFrame(frame);
	    	
	    	//Ship Movement
	    	velX = velocity * Math.cos(currAngle/180*Math.PI);
	    	velY = velocity * Math.sin(currAngle/180*Math.PI);
	    	move(velX*Global.state.dtt,velY*Global.state.dtt);
	    	
	    	for(Obj O:Global.state.activeObjs){
	    		if(!density) break;
	    		if(!(O instanceof ShipObj)) continue;
	    		if(O == this) continue;
	    		
	    		GameObj object = (GameObj) O;
	    		
	    		if(!object.density) continue;
	    		
	    		if(getDistance(object).length <=size + object.size){
	    		//if(checkCollision(object)!=null){
	    			//push eachother out of the way
	    			
	    			double radii = object.size+size;
	    			
	    			//first get the unit vector between the two objects
	    			Vector2D difPos = this.getDistance(object);
	    			Vector2D unit = difPos.unit();
	    			
	    			//then apply the push
	    			move(-radii * tweenFactor * tweenFactor * unit.x, -radii*tweenFactor * unit.y);
	    			
	    			O.move(radii * tweenFactor * tweenFactor * unit.x, radii*tweenFactor * unit.y);
	    		}
	    	}

    	}
    }
    
	public void move(double nx, double ny){
		super.move(nx,ny);
		
    	//Move camera if Ship is Player's Ship
    	if (this == Global.state.playerObj){
    		Global.player.cx=x; //+= velX*Global.state.dtt;
    		Global.player.cy=y; // += velY*Global.state.dtt;
    	}
	}
    
    public ShipObj(String image) {
    	super(image);
    }
    
    public ShipObj(String image, URL spritecontext) {
    	super(image, spritecontext);
    }
    
    public double getShields(double nx, double ny){//This will return the ship's shields that affects the Hitcircle object
    	double shield=0;
    	
    	PointS point = new PointS(nx-x,ny-y);
    	
    	double deltaAngle = getAngle(point) - currAngle;
    	if(deltaAngle >= 360) deltaAngle -= 360;
    	if(deltaAngle < 0) deltaAngle += 360;
    	
    	if(deltaAngle >=45 && deltaAngle <135) shield = shieldLeft;
    	if(deltaAngle >=135 && deltaAngle < 225) shield = shieldRear;
    	if(deltaAngle >=225 && deltaAngle < 315) shield = shieldRight;
    	if(deltaAngle < 45 || deltaAngle >= 315) shield = shieldForward;
    	
    	return shield;
    }
    
    public void setShields(double value, double nx, double ny){
    	value = Math.max(0,value);
    	PointS point = new PointS(nx-x,ny-y);
    	
    	double deltaAngle = getAngle(point) - currAngle;
    	if(deltaAngle >= 360) deltaAngle -= 360;
    	if(deltaAngle < 0) deltaAngle += 360;
    	
    	if(deltaAngle >=45 && deltaAngle <135) shieldLeft = value;
    	if(deltaAngle >=135 && deltaAngle < 225) shieldRear = value;
    	if(deltaAngle >=225 && deltaAngle < 315) shieldRight = value;
    	if(deltaAngle < 45 || deltaAngle >= 315) shieldForward = value;
    }
    
    public void Draw(Graphics2D G, ImageObserver loc){
    	if(sprite == null) return;
    	//Draw shields
    	
    	if(this == Global.state.playerObj || this == Global.state.playerObj.aimTarget)
    		drawShields(G, 20, 150, 240);
    	
    	transform(); //Applies the object's transformations to the sprite
    	
    	sprite.Draw(G,loc); //Draws the object's sprite
    }
    
    public void drawShields(Graphics2D G, int red, int green, int blue){
		if(maxShield > 0){
			double dx,dy;
			
			dx = this.x  - (sprite.frameX/2 * 1.25);
			dy = this.y  + (sprite.frameY/2 * 1.25) + 1;
			
			PointS coords = (new PointS(dx,dy)).toScreen();
			
			/*
			if (this == Global.state.playerObj){
    			//System.out.println("player's x,y: "+this.x+" "+this.y+" and dx,dy: "+dx+" "+dy);
    			System.out.println("player's x,y: "+(int)this.x+" "+(int)this.y+" player's cx,cy: "+(int)Global.player.cx+" "+(int)Global.player.cy+" and dx,dy: "+dx+" "+dy);
    		}
			*/
			int width = (int) (sprite.frameX * Global.player.zoom * 1.25);
			int height = (int) (Global.xyRatio * sprite.frameY * Global.player.zoom * 1.25);
			
			float alpha;
			Color dColor;
			
			//-44 degrees to 44 degrees
			alpha = (float) (shieldForward/maxShield * 255);
			alpha = Math.min(255,Math.max(alpha,0));
			dColor = new Color(red,green,blue,(int) alpha);
			G.setColor(dColor);
			G.drawArc((int) coords.x,(int) coords.y,width,height,-40+(int) currAngle,80);
			G.drawArc((int) coords.x+1,(int) coords.y+1,width-2,height-2,-40+(int) currAngle,80);
			
			//46 degrees to 134 degrees
			alpha = (float) (shieldLeft/maxShield * 255);
			alpha = Math.min(255,Math.max(alpha,0));
			dColor = new Color(red,green,blue,(int) alpha);
			G.setColor(dColor);
			G.drawArc((int) coords.x,(int) coords.y,width,height,50+(int) currAngle,80);
			G.drawArc((int) coords.x+1,(int) coords.y+1,width-2,height-2,50+(int) currAngle,80);
			
			//136 degrees to 224 degrees
			alpha = (float) (shieldRear/maxShield * 255);
			alpha = Math.min(255,Math.max(alpha,0));
			dColor = new Color(red,green,blue,(int) alpha);
			G.setColor(dColor);
			G.drawArc((int) coords.x,(int) coords.y,width,height,140+(int) currAngle,80);
			G.drawArc((int) coords.x+1,(int) coords.y+1,width-2,height-2,140+(int) currAngle,80);
			
			//226 degrees to 314 degrees
			alpha = (float) (shieldRight/maxShield * 255);
			alpha = Math.min(255,Math.max(alpha,0));
			dColor = new Color(red,green,blue,(int) alpha);
			G.setColor(dColor);
			G.drawArc((int) coords.x,(int) coords.y,width,height,230+(int) currAngle,80);
			G.drawArc((int) coords.x+1,(int) coords.y+1,width-2,height-2,230+(int) currAngle,80);
		}
    }
}