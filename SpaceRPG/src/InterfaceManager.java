/**
 * @(#)InterfaceManager.java
 *
 *
 * @author 
 * @version 1.00 2012/7/11
 */

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collection;

public class InterfaceManager {
	
	Hashtable<String, UIElement> elements = new Hashtable(0);
	
	Object selectedObject;//This is for pane submenus and lists.
	
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
    
    public void removeElement(UIElement element){
    	String key="";
    	
    	ArrayList values = new ArrayList(elements.values());
    	ArrayList<String> keys = new ArrayList(elements.keySet());
    	
    	for(int i=0;i<values.size();i++){
    		if(values.get(i) == element){
    			key = keys.get(i);
    			break;
    		}
    	}
    	removeElement(key);
    }
    
    public Collection<UIElement> getElements(){
    	return elements.values();
    }
    
    public void Init(){
    	//This is where element layers get set.
    }
    
    public void Delete(){
    	Global.GUI = null;
    	Collection<UIElement> values = elements.values();
    	for(UIElement E : values){
    		E.addDelete();
    	}
    	
    }
}