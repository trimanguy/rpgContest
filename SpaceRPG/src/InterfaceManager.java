/**
 * @(#)InterfaceManager.java
 *
 *
 * @author 
 * @version 1.00 2012/7/11
 */

import java.util.Hashtable;
import java.util.Collection;

public class InterfaceManager {
	
	Hashtable<String, UIElement> elements = new Hashtable(0);
	
    public InterfaceManager() {
    }
    
    public UIElement getElement(String key){
    	return elements.get(key);
    }
    
    public void setElement(String key, UIElement e){
    	elements.put(key,e);
    }
    
    public void removeElement(String key){
    	UIElement e = getElement(key);
    	
    	elements.remove(key);
    	
    	e.addDelete();
    }
    
    public Collection<UIElement> getElements(){
    	return elements.values();
    }
    
    public void Init(){
    	//This is where element layers get set.
    }
}