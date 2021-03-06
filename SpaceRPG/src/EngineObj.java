/**
 * @(#)EngineObj.java
 *
 *
 * @author 
 * @version 1.00 2012/7/11
 */


public class EngineObj extends ItemObj{

	//deprecated
	double acceleration=1;
	double maxThrust=1;
	//
	
	double maxVelocity;
	double maxAngVelocity;
	double maxPowerConsumption=1;
	
	double speedMultiplier=0.5;
	double turnMultiplier=0.5;
	
	double sigmoidPower = 10;

	public EngineObj(String name, String model,double maxSpeed,double rotSpeed, double health, double armor, double pwrReq, int size, String descrip){
		this.type = "Engine";
		this.name = name;
    	this.model = model;	
    	this.maxVelocity = maxSpeed;
    	this.maxAngVelocity = rotSpeed;
    	this.baseHealth = health;
    	this.armor = armor;
    	this.maxPowerConsumption = pwrReq;
    	this.size = size;
    	this.descrip = descrip;
	}

    public double getThrust(double percent){
    	return percent * maxThrust;
    }
    
    public double getPowerConsumption(double percent){
    	return maxPowerConsumption/2 * (Math.sin(Math.PI*(percent - 0.5))+1);
    }
    
    public double getPowerEquals(double power){//This function finds the velocity % needed to equate given power.
    	power *= 2/maxPowerConsumption;
    	
    	power -= 1;
    	
    	if(power > 1) power = 1;
    	
    	power = Math.asin(power);
    	power /= Math.PI;
    	power += 0.5;
    	
    	
    	return power;
    }
    
}