/**
 * @(#)ItemObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/27
 */


public class ItemObj extends Obj{
	
	String name;
	String model; //path to model img (hangar use)
	String descrip;
	
	int size; //This corresponds to Pylon.size
	String type;//This corresponds to Pylon.type
	
	int quantity;
	int maxQuantity=1;
	
	double baseHealth;
	double armor;
	
	double activateDelay;
	boolean isActive; //false = item is passive
	
    public ItemObj() {
    	
    }
    
    
}