/**
 * @(#)PowerCoreObj.java
 *
 *
 * @author 
 * @version 1.00 2012/7/11
 */


public class PowerCoreObj extends ItemObj{

	double maxPower; //energy bar size 
	double regenRate; //base regen rate/sec
	
	public PowerCoreObj(String name, String model, Double power, Double regen, Double health, Double armor, int size, String descrip){
		
		this.type = "PowerCore";
		this.name = name;
		this.model = model;
		this.maxPower = power;
		this.regenRate = regen;
		this.baseHealth = health;
		this.armor = armor;
		this.size = size;
		this.descrip = descrip;
		
	}

}