/**
 * @(#)InterfaceList.java
 *
 *
 * @author 
 * @version 1.00 2012/7/31
 */

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class InterfaceList extends UIElement{
	//This only shows vertically aligned lists.
	
	ArrayList<Object> values = new ArrayList(0);
	ArrayList<InterfaceItem> holders = new ArrayList(0);
	
	int width;
	int height;
	
	int indexHeight=44;
	int sliderWidth=16;
	
	InterfaceSlider slider;
	
    public InterfaceList(double x, double y) {
    	String path = "Resources/Interface/";
    	
    	width = 512;
    	height = 128;
    	
    	slider = new InterfaceSlider(x+width-sliderWidth, y, path+"genericSliderBack.png",
    		path+"genericSliderButton.png", null, null, 
    		0, 100, height);
    	slider.dx = 1;
    	slider.dy = 1;
    	
    	addChild(slider);
    }
    
    public InterfaceList(double x, double y, int w, int h, double nlayer) {
    	String path = "Resources/Interface/";
    	
    	width = w;
    	height = h;
    	
    	
    	slider = new InterfaceSlider(x+width-sliderWidth, y, path+"genericSliderBack.png",
    		path+"genericSliderButton.png", null, null, 
    		0, 100, h);
    	
    	slider.scaleTo(sliderWidth,h);
    	
    	scaleTo(w,h);
    	layer = nlayer;
    }
    
    public void addAll(ArrayList<Obj> O){
    	for(Obj E:O){
    		addObject(E);
    	}
    }
    
    public void removeAll(ArrayList<Obj> O){
    	for(Obj E:O){
    		removeObject(E);
    	}
    }
    
    public void addObject(Obj O){
    	if(values.contains(O)) return;
    	
    	values.add(O);
    	holders.add(new InterfaceItem(O));
    }
    
    public void removeObject(Obj O){
    	int i = values.indexOf(O);
    	if(i < 0 || i >= values.size()) return;
    	values.remove(O);
    	holders.get(i).addDelete();
    	holders.remove(i);
    	
    }
    
    public void mouseDropped(MouseEvent e, Obj O){
    	if(Global.GUI instanceof HangarInterface){
    		HangarInterface gui = ((HangarInterface) Global.GUI);
    		
	    	if(O instanceof InterfacePylon){
	    		Pylon P = ((InterfacePylon)O).pylon;
	    		if(gui.subPane == "crew"){
	    			P.equipItem((ItemObj) Global.GUI.selectedObject);
	    		}else{
	    			P.equipCrew((CharacterObj) Global.GUI.selectedObject);
	    		}
	    		
	    		Global.state.playerVault.remove(Global.GUI.selectedObject);
	    		Global.state.playerCargo.remove(Global.GUI.selectedObject);
	    	}
	    	
    		gui.updatePane();
    	}
    }
    public void mouseClicked(MouseEvent e){
    	
    	double position = e.getY();
    	position -= y;
    	
    	for(int i=0;i<values.size();i++){
    		double lowY, highY;
    		
    		int offset =(int) (slider.position * values.size());
    		
    		lowY = (i-offset)*indexHeight;
    		highY = (i+1-offset)*indexHeight;
    		
    		if(lowY < 0 || highY > height){
    			continue;
    		}
    		
    		if(y > lowY && y < highY){
    			holders.get(i).base.mouseClicked(e);
    			return;
    		}
    		
    	}
    	
    }
    
    public void Draw(Graphics2D G, ImageObserver loc){
    	slider.Draw(G,loc);
    	
    	super.Draw(G,loc);
    	
    	//Now draw the sub objects!
    	for(int i=0;i<values.size();i++){
    		double lowY, highY;
    		
    		int offset =(int) ((1-slider.position) * values.size());
    		
    		lowY = (i-offset)*indexHeight;
    		highY = (i+1-offset)*indexHeight;
    		
    		if(lowY < 0 || highY > height){
    			continue;
    		}
    		
    		Color lines = new Color(0,0,0,64);
    		G.setColor(lines);
    		G.drawLine((int) x,(int) (y+lowY), (int) (x+width), (int) (y+lowY));
    		G.drawLine((int) x,(int) (y+highY), (int) (x+width), (int) (y+highY));
    		
    		InterfaceItem I = holders.get(i);
    		I.move(x,lowY+y);
    		
    		Color color = new Color (0,0,0,0);
    		if(I.base == Global.GUI.selectedObject && I.base != null){
    			color = new Color (0,160,160,128);
    		}
    		
    		I.Draw(G,loc,(int) (x+1),(int) (y+lowY+1),width-2,height-2,color);
    	}
    }
    
}