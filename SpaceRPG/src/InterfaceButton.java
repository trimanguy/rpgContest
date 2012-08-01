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

    Object callContext;
    String callMethod;
    String callParam;
    
    public InterfaceButton(double x, double y, String image, double nlayer,
    	String cM, Object cC, String cP) {
    	
    	super(x,y,image, nlayer);
    	
    	callMethod = cM;
    	callContext = cC;	
    	callParam = cP;
    }
    
	public void mouseClicked(MouseEvent event){
		if(callMethod == null || callMethod == "") return;
		if(callContext == null) return;
		
		Method method=null;
		try {
		  	if(callParam == null) method = callContext.getClass().getMethod(callMethod);
		  	else  method = callContext.getClass().getMethod(callMethod,String.class);
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