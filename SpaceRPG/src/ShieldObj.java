/**
 * @(#)ShieldObj.java
 *
 *
 * @author 
 * @version 1.00 2012/7/11
 */


public class ShieldObj extends ItemObj{
	double maxShield;
	double regenDelay;
	
	double shieldPowerConsumption;
	
	double shieldBoostRate;
	double boostCost;
	double boostAmount;
	
	public ShieldObj(String name,String model,double maxShield,double delay,double boostCost,
		double health,double armor,double powerReq, int size, String descrip){
		
		this.type = "Shield";
		this.name = name;
		this.model = model;
		this.maxShield = maxShield;
		this.regenDelay = delay;
		this.boostCost = boostCost;
		this.baseHealth = health;
		this.armor = armor;
		this.shieldPowerConsumption = powerReq;
		this.size = size;
		this.descrip = descrip;
	}
}