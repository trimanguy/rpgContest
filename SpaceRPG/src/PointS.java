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
	
	public static PointS toScreen(PointS in){
        double nx,ny;
        nx = in.getX(); ny = in.getY();
        nx = (nx - Global.player.cx) * Global.player.zoom + Global.view.sizex/2;
        ny = (Global.player.cy - ny) * Global.player.zoom * Global.xyRatio + Global.view.sizey/2;
                   
        return new PointS(nx,ny);
    }
     
    public static PointS toWorld(PointS in){
        double nx,ny;
        nx = in.getX(); ny = in.getY();
        nx -= Global.view.sizex/2;
        ny -= Global.view.sizey/2;
        nx = (nx/Global.player.zoom)+Global.player.cx;
        ny = (-ny/Global.player.zoom/Global.xyRatio)+Global.player.cy;
                   
        return new PointS(nx,ny);
    }

}