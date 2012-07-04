/**
 * @(#)ItemObj.java
 *
 *
 * @author 
 * @version 1.00 2012/6/27
 */


public class ItemObj extends Obj{

	String descrip;
	String name;
	double mass;
	
	int size; //This corresponds to Pylon.size
	String type;//This corresponds to Pylon.type
	
	int quantity;
	int maxQuantity=1;
	
	double baseHealth;
	
	double activateDelay;
	boolean canActivate = false;
	
    public ItemObj() {
    	
    }
    
    
}