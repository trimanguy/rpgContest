import java.awt.geom.*;

public class PointS extends Point2D.Double{
	public PointS next;
	public PointS prev;
	
	public PointS(){
		
	}
	public PointS(double nx, double ny){
		x = nx;
		y = ny;
	}
	
	public int compareTo(PointS P){
		if(P.getX() > x) return -1;
		if(P.getX() < x) return 1;
		if(P.getY() > y) return -1;
		if(P.getY() < y) return 1;
		return 0;
	}
}