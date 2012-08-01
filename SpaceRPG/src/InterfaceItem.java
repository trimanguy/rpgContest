
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Color;

class InterfaceItem extends UIElement{
	Obj base;
	
	public InterfaceItem(Obj O){
		base = O;
		//deduce the icon from O?
		getIcon(O);
	}
	
	public void getIcon(Obj O){
		//this initializes the sprite
		String path = "Resources/ItemIcons/";
		
		if(O instanceof WeaponObj){
			if(((WeaponObj)O).missileHoming){
				path += "Launcher.png";
			}else{
				path += "Blaster.png";
			}
		}else if(O instanceof ShieldObj){
			path += "Shield.png";
		}else if(O instanceof EngineObj){
			path += "Engines.png";
		}else if(O instanceof PowerCoreObj){
			path += "PowerCore.png";
		}else if(O instanceof ShipObj){
			path += "Ship.png";
		}else{
			path += "Generic.png";
		}
		
		
		sprite = new Sprite(path, false);
	}
	
    public void Draw(Graphics2D G, ImageObserver loc, int x, int y, int w, int h, Color color){
    	G.setColor(color);
    	G.fillRect(x,y,w,h);
    	
    	translate(x+(sprite.frameX-w)/2,y+(sprite.frameY-h)/2);
    	
    	super.Draw(G,loc);
    }
}