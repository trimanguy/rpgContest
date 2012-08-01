import java.awt.geom.*;

public class PointS extends Point2D.Double{
	/**next and prev may not be needed, deletion imminent**/
	//public PointS next;
	//public PointS prev;

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
	
	public PointS toScreen(){
        double nx,ny;
        nx = x; ny = y;
        nx = (nx - Global.player.cx - Global.player.offsetX) * Global.player.zoom + Global.view.sizex/2;
        ny = (Global.player.cy + Global.player.offsetY - ny) * Global.player.zoom * Global.xyRatio + Global.view.sizey/2;
                   
        return new PointS(nx,ny);
    }
     
    public PointS toWorld(){
        double nx,ny;
        nx = x; ny = y;
        nx -= Global.view.sizex/2;
        ny -= Global.view.sizey/2;
        nx = (nx/Global.player.zoom)+Global.player.cx+Global.player.offsetX;
        ny = (-ny/Global.player.zoom/Global.xyRatio)+Global.player.cy+Global.player.offsetY;
                   
        return new PointS(nx,ny);
    }

}