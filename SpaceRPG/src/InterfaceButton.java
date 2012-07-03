/**
 * @(#)InterfaceButton.java
 *
 *
 * @author 
 * @version 1.00 2012/7/2
 */

import java.net.URL;
import java.awt.event.*;
import java.lang.reflect.*;


public class InterfaceButton extends UIElement{

    Obj callContext;
    String callMethod;
    
    public InterfaceButton(double x, double y, String image, URL spritecontext, String cM, Obj cC) {
    	super(x,y,image,spritecontext);
    	
    	callMethod = cM;
    	callContext = cC;	
    }
    
	public void mouseClicked(MouseEvent event){
		if(callMethod == null || callMethod == "") return;
		if(callContext == null) return;
		
		Method method=null;
		try {
		  	method = callContext.getClass().getMethod(callMethod);
		} catch (SecurityException e) {
		 	// ...
		} catch (NoSuchMethodException e) {
			// ...
		}
		try {
			method.invoke(callContext);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}
}