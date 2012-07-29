/**
 * @(#)WeaponObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/27
 */
 
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;




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

    public WeaponObj(String name, String model, String img, String hitImg, int life, double maxSpeed, double accel, double turnSpeed, double spread, double itemTurnSpeed, String damage,
    	double health, double armor, double delay, int size, String descrip, boolean activateAble) {
    	
    	type = "Weapon";
    	this.name = name;
    	//this.model = model;	
    	missileImg = img;
    	missileHitImg = hitImg;
    	missileLife = life;
    	missileMaxSpeed = maxSpeed;
    	missileAcceleration = accel;
    	missileTurnSpeed = turnSpeed;
    	angleSpread = spread;
    	this.turnSpeed = itemTurnSpeed;
    	this.baseHealth = health;
    	this.armor = armor;
    	activateDelay = delay;
    	this.size = size;
    	this.descrip = descrip;
    	this.isActive = activateAble;
    	
    	//parse damage
    	StringTokenizer st = new StringTokenizer(damage, ",");
    	damageToShield = Double.valueOf(st.nextToken());
    	damageThruShield = Double.valueOf(st.nextToken());
    	damageToHull = Double.valueOf(st.nextToken());
    	damageArmorPiercing = Double.valueOf(st.nextToken());
    	
    	if(turnSpeed != 0){
    		missileHoming = true;
    	}
    	
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
    		
    	//System.out.println("soundtable size "+Utils.soundsTable.size());
    	if(this.name.equals("testBlaster")){
    		//AudioClip sound = ((AudioClip)Utils.soundsTable.get("iceball"));
    		//AudioClip test = getAudioClip(Global.codeContext,"Resources/Sounds/iceball.wav");
    		//SoundObj test = new SoundObj("Resources/Sounds/laser1.wav");
    		//test.play();
    		
    		InputStream is = this.getClass().getClassLoader().getResourceAsStream("Resources/Sounds/laser1.wav");
    		try{
    		
			AudioInputStream ain = AudioSystem.getAudioInputStream(is);
			DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat());      
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(ain);
			clip.start();
    		}catch (UnsupportedAudioFileException e){
    			
    		}catch(LineUnavailableException u){
    		
    		}catch(IOException i){
    			
    		}
    		
    		
    		
    	} else {
    		
    		//AudioClip test = getAudioClip(Global.codeContext,"Resources/Sounds/rlaunch.wav");
    		//SoundObj test = new SoundObj("Resources/Sounds/rlaunch.wav");
    		//test.play();
    	}
    	
    }
}