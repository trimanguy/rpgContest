/**
 * This is a physical object that is displayed on the game screen.
 *
 * Physically represented by a polygon, and graphically represented by a sprite.
 *
 * Provides functionality for collision detection with other polygons
 *
 * Note: Keep the AffineTransform for the Obj and the Sprite separate.
 * 
 * The Obj uses the transform() method to transform it's vertices.
 *     When a transformation is valid for whatever it's been used for, transform() is called
 *     to transform the vertices. This is a must for collision detection. Game Errors WILL occur 
 *     if collision detection is performed before the transformation.
 *
 * The sprite has no such feature, and cannot save transformations. 
 *     It depends on the AffineTransform object to keep track of all transformations, 
 *     and the deletion of the AffineTransform or inconsistent change of reference for the 
 *     AffineTransform will result in the incorrect rendering of the sprite.
 */

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.net.*;
import java.awt.geom.*;
import java.util.*;

public class Obj implements Comparable{
	
	public Sprite sprite;
	public Color color = Color.white;
	//public AffineTransform transform = new AffineTransform();
	
	public ArrayList<PointS> vertices;
	public URL context;
	
	public double x; //Center coords?
	public double y;
	public double angle;//Angle
	public double dx=1;//draw width
	public double dy=1;//draw height
	
	public double layer=3; //Drawing layer
	
	public boolean density = true;
	
	
    double vx,vy;
    
	public Obj(){
		
	}
	
    public Obj(ArrayList<PointS> Pointss, String image) {
    	vertices = (ArrayList)Pointss.clone();
    	if(image != null) {
	    	sprite = new Sprite(image, true);
	    	Init();
    	}
    }
    
    public Obj(ArrayList<PointS> Pointss, String image, URL spritecontext) {
    	vertices = (ArrayList)Pointss.clone();
    	if(image != null && spritecontext != null)
    	{
    		sprite = new Sprite(image, spritecontext, true);
	    	context = spritecontext;
	    	Init();
    	}
    }
    
    public void Init(){
    	Global.state.activeObjects.add(this);
    	if(CameraCanSee()){
    		Global.view.drawObjects.add(this);
    	}
    }
    
    public Polygon getPolygon(){
    	int[] x = new int[vertices.size()];
    	int[] y = new int[vertices.size()];
    	
    	for(int i=0;i<vertices.size();i++){
    		Point2D PointS = vertices.get(i);
    		x[i] = (int)PointS.getX();
    		y[i] = (int)PointS.getY();
    	}
    	
    	return new Polygon(x,y, vertices.size());
    }
    
    public void setVertices(ArrayList<PointS> Pointss){
    	vertices = (ArrayList) Pointss.clone();
    }
    
    public void setVertices(Polygon Poly){
    	PathIterator iterator = Poly.getPathIterator(new AffineTransform());
    	ArrayList<PointS> newvertices = new ArrayList();
    	double[] coords = new double[6];
    	
    	//I HATE ITERATORS
    	//Converting a transformed polygon back into an arraylist of coordinates.
    	PointS last = new PointS();
    	int type = iterator.currentSegment(coords);
    	while(type<2){
    		PointS P = new PointS(coords[0],coords[1]);
    		
    		boolean contained = false;
    		for(PointS U : newvertices){
    			if(U.compareTo(P) == 0){
    				contained = true;
    				break;
    			}
    		}
    		if(!contained)
    			newvertices.add(P);
    		
    		if(type == PathIterator.SEG_LINETO ){
    			last.next = P;
    			P.prev = last;
    		}
    		
    		last = P;
    		type = iterator.currentSegment(coords);
    		iterator.next();
    	}
    	
    	vertices = (ArrayList) newvertices.clone();
    }
    
    public void setColor(Color C){
    	color = C;
    }
    
    public void setSprite(Sprite s){
    	sprite = s;
    }
    
    public boolean contains(PointS P){
    	return getPolygon().contains(P);
    }
    
    public boolean checkCollision(Obj O){
    	if(O == this) return true;
    	//If I'm colliding with myself? Seems redundant.
    	
    	if(vertices.size() == 0 || O.vertices.size() == 0) return false;
    	//If I don't have any vertices, or the other doesn't have any vertices, return
    	
    	if(!this.density || !O.density) return false;
    	//If either of us aren't dense, no point in checking collisions.
    	
    	//First check if there are any vertices that are within the other.
    	Polygon thispoly = getPolygon();
    	Polygon otherpoly = O.getPolygon();
    	
    	for(PointS P:vertices){
    		if(otherpoly.contains(P))
    			return true; //If the other polygon contains one of my vertices
    	}
    	for(PointS P:O.vertices){
    		if(thispoly.contains(P))
    			return true; //If my polygon contains one of the other's vertices
    	}
    	
    	//Next, check if there are intersecting boundaries
    	for(PointS P:vertices){
    		Line2D thisline = new Line2D.Double(P,P.next);
    		for(PointS U:O.vertices){
    			O.linkVertices();
    			Line2D otherline = new Line2D.Double(U,U.next);
    			if(thisline.intersectsLine(otherline)) return true;
    			//Intersecting boundaries
    		}
    	}
    	
    	return false; //No PointS collisions and boundary intersections.
    }
    
    public static PointS lineCollision(PointS p1, PointS p2,
    	PointS p3, PointS p4){
    		double x1 = p1.getX();
    		double y1 = p1.getY();
    		double x2 = p2.getX();
    		double y2 = p2.getY();
    		double x3 = p3.getX();
    		double y3 = p3.getY();
    		double x4 = p4.getX();
    		double y4 = p4.getY();
    		//First, check if the two lines defined actually intersect.
    		double denominator = ((x1-x2)*(y3-y4))-((y1-y2)*(x3-x4));
    		if(denominator == 0) return null; //The lines are parallel!
    		
    		double px = (((x1*y2-y1*x2)*(x3-x4))-((x1-x2)*(x3*y4-y3*x4)))/denominator;
    		double py = (((x1*y2-y1*x2)*(y3-y4))-((y1-y2)*(x3*y4-y3*x4)))/denominator;
    		
    		//Checking if the intersection point is within the range of the line segments.
    		if(x1 <=x2) if(px < x1 || px > x2) return null;
    		if(x1 > x2) if(px > x1 || px < x2) return null;
    		if(y1 <=y2) if(py < y1 || py > y2) return null;
    		if(y1 > y2) if(py > y1 || py < y2) return null;
    		if(x3 <=x4) if(px < x3 || px > x4) return null;
    		if(x3 > x4) if(px > x3 || px < x4) return null;
    		if(y3 <=y4) if(py < y3 || py > y4) return null;
    		if(y3 > y4) if(py > y3 || py < y4) return null;
    		
    		return new PointS(px,py);//Everything checks out. Return the point of intersection
    	}
    /*	
    public boolean Hit(Projectile O){
    	//This is called when O hits this.
    	//Return FALSE to allow the projectile to pass through.
    	//Return TRUE to remove the projectile from the world.
    	return true;
    }
    */
	
	public void move(double nx, double ny){
		double px = nx+x, py = ny+y;
		translate(px,py);
	}
	
	public void translate(double nx, double ny){
		for(PointS P:vertices){
			P.x += nx-x;
			P.y += ny-y;
		}
		x=nx;
		y=ny;
		//sprite.move(nx,ny);
	}
	
	public void translate(Point2D PointS){
		double nx = PointS.getX();
		double ny = PointS.getY();
		for(PointS P:vertices){
			P.x += nx-x;
			P.y += ny-y;
		}
		x=nx;
		y=ny;
		//sprite.move(nx,ny);
	}
	
	public void rotate(double theta){
		double cx=0;
		double cy=0;
		for(PointS P:vertices){
			cx += P.x;
			cy += P.y;
		}
		cx/=vertices.size();
		cy/=vertices.size();
		rotate(theta,cx,cy);
	}
	
	public void rotate(double theta, Point2D PointS){ 
		double cx = PointS.getX();
		double cy = PointS.getY();
		
		rotate(theta,cx,cy);
	}
	
	public void rotate(double theta, double cx, double cy){// IN RADIANS!!!
		for(PointS P:vertices){
			double px = P.getX()-cx;
			double py = P.getY()-cy;
			
			double dist = Math.sqrt((px*px)+(py*py));
			double alpha = (Math.atan2(py,px));
			if(Math.round(dist)==0) continue;
			
			alpha += (theta);
			px = Math.cos(alpha)*dist;
			py = Math.sin(alpha)*dist;
			P.x = px+cx;
			P.y = py+cy;
		}
		//changing this coordinates
		if(cx != x && cy != y){
			double px = x-cx;
			double py = y-cy;
			
			double dist = Math.sqrt((px*px)+(py*py));
			double alpha = (Math.atan2(py,px));
			
			alpha += (theta);
			px = Math.cos(alpha)*dist;
			py = Math.sin(alpha)*dist;
			x = px+cx;
			y = py+cy;
		}
		
		angle += theta;
		if(angle >= 360) angle-=360;
		if(angle < 0) angle += 360;
	}
	
	public void setAngle(double theta){
		double cx=0;
		double cy=0;
		for(PointS P:vertices){
			cx += P.x;
			cy += P.y;
		}
		cx/=vertices.size();
		cy/=vertices.size();
		setAngle(theta,cx,cy);
	}
	
	public void setAngle(double theta, Point2D PointS){
		double cx = PointS.getX();
		double cy = PointS.getY();
		setAngle(theta,cx,cy);
	}
	
	public void setAngle(double theta, double cx, double cy){
		for(PointS P:vertices){
			double px = P.getX()-cx;
			double py = P.getY()-cy;
			
			double dist = Math.sqrt((px*px)+(py*py));
			double alpha = (Math.atan2(py,px));
			if(Math.round(dist)==0) continue;
			
			alpha += (theta-angle);
			px = Math.cos(alpha)*dist;
			py = Math.sin(alpha)*dist;
			P.x = px+cx;
			P.y = py+cy;
		}
		//changing this coordinates
		if(cx != x && cy != y){
			double px = x-cx;
			double py = y-cy;
			
			double dist = Math.sqrt((px*px)+(py*py));
			double alpha = (Math.atan2(py,px));
			
			alpha += (theta-angle);
			px = Math.cos(alpha)*dist;
			py = Math.sin(alpha)*dist;
			x = px+cx;
			y = py+cy;
		}
		
		angle = theta;
		angle = Math.max(0,Math.min(359,angle));
	}
	
	public void flip(){
		double cx=0, cy=0;
		for(PointS P:vertices){
			cx+=P.getX();
			cy+=P.getY();
		}
		cx/=vertices.size();
		cy/=vertices.size();
		flip(cx,cy);
	}
	
	public void flip(double cx, double cy){
		double flipangle = angle-Math.toRadians(90);
		ArrayList<PointS> points = new ArrayList(vertices.size());
		
		for(PointS P:vertices) points.add(null);
		
		for(int i=0;i<vertices.size();i++){
			PointS P = vertices.get(i);
			points.set(vertices.size()-1-i,P);
			
			double px = P.getX()-cx;
			double py = P.getY()-cy;
			
			double dist = Math.sqrt((px*px)+(py*py));
			double alpha = (flipangle-(Math.atan2(py,px)))*2+(Math.atan2(py,px));
			if(Math.round(dist)==0) continue;
			
			px = Math.cos(alpha)*dist;
			py = Math.sin(alpha)*dist;
			P.x = px+cx;
			P.y = py+cy;
		}
		vertices = points;
	}
	
	public void scale (double dw, double dh){
		//Rescaling
		double cx=0;
		double cy=0;
		
		for(PointS P:vertices){
			cx += P.x;
			cy += P.y;
		}
		cx/=vertices.size();
		cy/=vertices.size();
		
		for(PointS P: vertices){
			double px = P.getX()-cx;
			double py = P.getY()-cy;
			px*=dw;
			py*=dh;
			P.x = cx+px;
			P.y = cy+py;
		}
		
		if(cx != x && cy != y){
			double px = x-cx;
			double py = y-cy;
			px*=dw;
			py*=dh;
			x = cx+px;
			y = cy+py;
		}
		
		dx *= dw;
		dy *= dh;
		//sprite.scale(dw,dh);
	}
	
	public void scaleTo (double dw, double dh){
		//Setting the scale
		double cx=0;
		double cy=0;
		for(PointS P:vertices){
			cx += P.x;
			cy += P.y;
		}
		cx/=vertices.size();
		cy/=vertices.size();
		
		for(PointS P: vertices){
			double px = P.getX()-cx;
			double py = P.getY()-cy;
			px*=dw/dx;
			py*=dh/dy;
			P.x = cx+px;
			P.y = cy+py;
		}
		
		if(cx != x && cy != y){
			double px = x-cx;
			double py = y-cy;
			px*=dw/dx;
			py*=dh/dy;
			x = cx+px;
			y = cy+py;
		}
		
		dx = dw;
		dy = dh;
		//sprite.scaleTo(dw,dh);
	}
    
    public void linkVertices(){
    	
    	for(int i=0;i<vertices.size();i++){
    		PointS P = vertices.get(i);
    		//if(P.next != null && P.prev != null) continue;
    		if(i > 0 && P.prev == null)
    			P.prev = vertices.get(i-1);
    		if(i<vertices.size()-1 && P.next == null)
    			P.next = vertices.get(i+1);
    		else if(P.next == null){
    			P.next = vertices.get(0);
    			vertices.get(0).prev = P;
    		}
    	}
    }
    
    public void transform(){
    	AffineTransform transform = new AffineTransform();
    	
    	double rx, ry;
    	rx = (x - Global.player.cx) * Global.player.zoom + Global.view.sizex/2;
    	ry = (Global.player.cy - y) * Global.player.zoom * Global.xyRatio + Global.view.sizey/2;
    	rx -= (sprite.frameX/2) * Global.player.zoom;
    	ry -= (sprite.frameY/2) * Global.player.zoom;
    	
    	transform.translate(rx, ry);
    	//transform.rotate(angle);
    	transform.scale(dx * Global.player.zoom, dy * Global.player.zoom);
    	
    	sprite.setTransform(transform);
    }
    
    public void Draw(Graphics2D G, ImageObserver loc){
    	
    	//Once testing sprite/polygon coherence is complete, there needs to be a section added
    	//Where the transform is modified to make the drawn coordinates relative to the player.
    	
    	transform(); //Applies the object's transformations to the sprite
    	sprite.Draw(G,loc); //Draws the object's sprite
    	linkVertices();//I'm not too sure why this is here, of all places.
    }
    
	public void flipX(){
		scale(-dx,dy);
	}
	
	public void flipY(){
		scale(dx,-dy);
	}
	
    public int compareTo(Obj O){
    	//I couldn't get my mergesort to work right? I don't know.
    	//This is a workaround for that.
    	double l = layer;
    	double ol = O.layer;
    	if(ol > l) return -1;
    	if(ol < l) return 1;
    	return 0;
    }
    
    public int compareTo(Object O){
    	return O.toString().compareTo(this.toString());
    }
    
    public boolean CameraCanSee(){
    	if(Global.player == null) return false;
    	
    	double rx, ry;
    	rx = Math.abs(x - Global.player.cx);
    	ry = Math.abs(y - Global.player.cy);
    	
    	if(sprite != null){
    		rx -= sprite.frameX/2;
    		ry -= sprite.frameY/2;
    		if(rx < 0) rx = 0;
    		if(ry < 0) ry = 0;
    	}
    	
    	if(rx * Global.player.zoom > Global.view.sizex/2) return false;
    	if(ry * Global.player.zoom * Global.xyRatio > Global.view.sizey/2) return false;
    	return true;
    }
    
    public void Step(){ //This is what will be called by the gamestate every tick
    }
}