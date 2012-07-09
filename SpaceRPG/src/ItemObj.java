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
	
	double turnSpeed;//Degrees/second
	
	int quantity;
	int maxQuantity=1;
	
	double baseHealth;
	
	double activateDelay;
	boolean canActivate = true; //da fuck is this??
	
    public ItemObj() {
    	
    }
    
    
}