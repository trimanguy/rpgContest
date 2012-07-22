/**
 * @(#)WeaponObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/27
 */
 
import java.util.*;

public class WeaponObj extends ItemObj{

	String missileImg; //The missileObj's sprite
	String missileHitImg; //The missileObj's "explosion" sprite
	
	String ammoTag; //What type of ammo is used upon fire()?
	int ammoRate; //How much ammo is consumed per fire()?
	
	int missileLife;//missileObj's life

	boolean missileHoming;//Is the missileObj homing? default is no
	double missileSpeed;//missileObj's speed in pixels per second
	double missileMaxSpeed;//missileObj's max speed in pixels per second
	double missileAcceleration;//missileObj's speed in pixels per second
	double missileTurnSpeed;//degrees per second.
	double angleSpread;//angular inaccuracy in degrees.
	
	double turnSpeed;//Degrees/second for the weapon itself
	double health;
	double armor;
	double mass;
	String descrip;
	
	/* Damage is computed as follows:
	 *
	 * shieldDamage = damageToShield;
	 *
	 * if no shields:  hullDamage = damageToHull-max(0,flatArmor-damageArmorPiercing);
	 *
	 * if has shields: hullDamage = damageThruShield-max(0,flatArmor-damageArmorPiercing);
	 */
	double damageToShield;
	double damageThruShield;
	double damageToHull;
	double damageArmorPiercing;

    public WeaponObj(String img, String hitImg, int life, double maxSpeed, double accel, double turnSpeed, double spread, double itemTurnSpeed, String damage,
    	double health, double armor, double delay, double mass, String descrip, boolean activateAble) {
    		
    	missileImg = img;
    	missileHitImg = hitImg;
    	missileLife = life;
    	missileMaxSpeed = maxSpeed;
    	missileAcceleration = accel;
    	missileTurnSpeed = turnSpeed;
    	angleSpread = spread;
    	this.turnSpeed = itemTurnSpeed;
    	this.health = health;
    	this.armor = armor;
    	activateDelay = delay;
    	this.mass = mass;
    	this.descrip = descrip;
    	this.canActivate = true;
    	
    	//parse damage
    	StringTokenizer st = new StringTokenizer(damage, ",");
    	damageToShield = Double.valueOf(st.nextToken());
    	damageThruShield = Double.valueOf(st.nextToken());
    	damageToHull = Double.valueOf(st.nextToken());
    	damageArmorPiercing = Double.valueOf(st.nextToken());
    	
    	if(turnSpeed != 0){
    		missileHoming = true;
    	}
    	type = "Weapon";
		   	
    	
    	
    	
    }
    
    public boolean canFire(){
    	//This function checks all requisites for this weapon to fire.
    	//This function might be moved into ItemObj and renamed during refactoring...
    	return true;
    }
    
    public void Fire(double nx, double ny, double angle, GameObj source, GameObj target){
    	angle += (Math.random()*2-1)*angleSpread;
    	
    	MissileObj O = new MissileObj(missileImg, missileHitImg, Global.codeContext, source, target, 
    		damageToShield, damageThruShield, damageToHull, damageArmorPiercing, 
    		nx, ny, missileTurnSpeed, missileSpeed, missileMaxSpeed,missileAcceleration, missileLife,
    		angle);
    }
}