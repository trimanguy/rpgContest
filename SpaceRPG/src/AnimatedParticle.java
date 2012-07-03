/**
 * @(#)AnimatedParticle.java
 *
 *
 * @author 
 * @version 1.00 2012/7/3
 */

import java.net.URL;

public class AnimatedParticle extends GameObj{

	double frameTimer;
	double frameDelay;
	//double layer = 11;
	
	int mouseOpacity = 0;
	boolean density = false;
	
    public AnimatedParticle(String image, URL spritecontext, double fD, double nx, double ny) { 
    	
    	super(image,spritecontext);
    	
    	frameTimer = Global.state.time+fD;
    	frameDelay = fD;
    	
    	translate(nx,ny);
    }
    
    public void Init(){
    	layer = 15;
    	super.Init();
    }
    
    public void Step(){
    	if(sprite.frame >= sprite.source.getWidth()/sprite.frameX){
    		addDelete();
    		return;
    	}
    	
    	if(Global.state.time >= frameTimer){
    		sprite.setFrame(sprite.frame+1);
    		frameTimer = Global.state.time+frameDelay;
    	}
    }
    
}