/**
 * @(#)particleSystem.java
 *
 *
 * @author 
 * @version 1.00 2012/6/29
 */
import java.util.ArrayList;
import java.net.URL;

public class ParticleSystem extends GameObj {

	public double quantity;
	//public ArrayList<Particle> particles = new ArrayList(0);
	
	public GameObj parent;
	
	public double layer=5; //Drawing layer
	
	public String particleType;
	
	public double timer;
	public double duration;
	
	public double rate;
	
	//You can apply accelerations to particles
	public double accelX; 
	public double accelY;
	
	//Coefficients of Drag
	public double dragX;
	public double dragY; 
	
	
    public ParticleSystem(double nx, double ny, String string, double dura) {
    	x = nx;
    	y = ny;
    	particleType = string;
    	duration = dura;
    	
    	Global.state.newObjBuffer.add(this);
    }
    
    public ParticleSystem(GameObj O, String string, double dura){
    	parent = O;
    	particleType = string;
    	duration = dura;
    	
    	Global.state.newObjBuffer.add(this);
    }
    
    public void Init(){
    	timer = Global.state.time + duration;
    	density = false;
    	
    	super.Init();
    }
    
    public void Step(){
    	if(Global.state.time > timer && duration >=0){
    		delete();
    	}
    	
    	double rate = quantity / duration; //Particles per second
    	rate *= Global.state.dt;
    	
    	if(parent != null){
    		x = parent.x;
    		y = parent.y;
    	}
    	
    	double dQuantity = Math.floor(quantity) - (quantity - rate);
    	if(dQuantity > 0){
    		for(int i=0;i<Math.ceil(dQuantity);i++){
    			quantity --;
    			/*
    			Particle O = new Particle(x,y,particleType);
    			particles.add(O);
    			*/
    		}
    	}
    	
    	
    }
    
}
/*
class Particle{
	public double x;
	public double y;
	
	public double angle;
	
	public Sprite sprite;
	
	public double velX;
	public double velY;
	
	public String type;
	
	public Particle(double nx, double ny, String ntype){
		//sprite = new Sprite(image, imagecontext, true);
		x = nx;
		y = ny;
		type = ntype;
		
		Init();
	}
	
	public void Init(){
		//Switch the type and initialize sprite parameters
	}
}
*/