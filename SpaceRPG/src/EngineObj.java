/**
 * @(#)EngineObj.java
 *
 *
 * @author 
 * @version 1.00 2012/7/11
 */


public class EngineObj extends ItemObj{

	double acceleration=1;
	double maxThrust=1;
	double maxPowerConsumption=1;
	
	double sigmoidPower = 10;

    public double getThrust(double percent){
    	return percent * maxThrust;
    }
    
    public double getPowerConsumption(double percent){
    	return maxPowerConsumption/2 * (Math.sin(Math.PI*(percent - 0.5))+1);
    }
    
    public double getPowerEquals(double power){//This function finds the velocity % needed to equate given power.
    	power *= 2/maxPowerConsumption;
    	power -= 1;
    	
    	power = Math.asin(power);
    	power /= Math.PI;
    	power += 0.5;
    	
    	return power;
    }
    
}