
import java.awt.*;
import java.awt.image.*;
import java.util.*;
/**
 *
 * @author Kuba
 */
public final class StarField {
	// set some default values
    private int no_stars=2000;
    public int field_width=4096;
    public int field_height=3072;
    private int field_depth=5;

    public Star[] stars;
    
    int lo_r=224, hi_r=255, lo_g=224, hi_g=255, lo_b=224, hi_b=255, lo_a=64, hi_a=255;
    int dr, dg, db, da;

    public class Star{
        int x;
        int y;
        int z;
        int s;
        int img=0;
        Color color;
    }

    public StarField(){
        stars=new Star[no_stars];
        generate_starfield();
    }
    /*
     * @no_stars parameter something
     */

    public StarField(int no_stars, int field_width, int field_height, int field_depth){
        this.no_stars=no_stars;
        this.field_width=field_width;
        this.field_height=field_height;
        this.field_depth=field_depth;
        stars=new Star[no_stars];
        generate_starfield();
    }
    
    public void SetColor(int lr, int hr, int lg, int hg, int lb, int hb, int la, int ha){
    	lo_r = lr; hi_r = hr; lo_g = lg; hi_g = hg; lo_b = lb; hi_b = hb; lo_a = la; hi_a = ha;
        dr =hi_r-lo_r; dg =hi_g-lo_g; db =hi_b-lo_b; da =hi_a-lo_a;
    }

    public int get_size(){
        return no_stars;
    }

    public void generate_starfield(){
    	
        dr =hi_r-lo_r; dg =hi_g-lo_g; db =hi_b-lo_b; da =hi_a-lo_a;
        
        for (int x=0; x<no_stars; x++){
            //System.out.println(x);
            Random r=new Random();
            stars[x]=new Star();
            stars[x].x=Math.abs(r.nextInt()%field_width);
            stars[x].y=Math.abs(r.nextInt()%field_height);
            stars[x].z=Math.abs(r.nextInt()%field_depth);
            stars[x].s=Math.abs(r.nextInt()%2)+1;
            stars[x].img=Math.abs(r.nextInt()%6)+1;
            
            int red,green,blue,alpha;
            red = Math.abs(r.nextInt()%dr)+lo_r;
            green = Math.abs(r.nextInt()%dg)+lo_g;
            blue = Math.abs(r.nextInt()%db)+lo_b;
            alpha = Math.abs(r.nextInt()%da)+lo_a;
            
            stars[x].color = new Color(red,green,blue,alpha);
        }
    }
    
	public void Draw(Graphics2D graphic, ImageObserver loc){
		int x_off = (int) ((Global.player.cx+Global.player.offsetX));
		int y_off = -(int) ((Global.player.cy+Global.player.offsetY));
		
        int fsw= field_width;
        int fsh= field_height;
        for (int a=0; a<get_size(); a++){

            int z=stars[a].z;
            int x=stars[a].x-(x_off/(z+1));
            int y=stars[a].y-(y_off/(z+1));
            int s=stars[a].s;

			//int cx = fsw/2;
			//int cy = fsh/2;

			//x=(int)(cx+(x-cx)*Global.player.zoom);
			//y=(int)(cy+(y-cy)*Global.player.zoom);
            
            int xx=x/fsw;
            if (xx>0){
                x-=(fsw*xx);
            }
            else if(x<0){
                x+=(fsw*(-xx+1));
            }

            int yy=y/fsh;
            if (yy>0){
                y-=(fsh*yy);
            }
            else if(y<0){
                y+=(fsh*(-yy+1));
            }
            
            x*=Global.player.zoom;
            y*=Global.player.zoom;
			s*=Global.player.zoom;
			s = Math.max(1,s);
			
			y = (int) (y*Global.xyRatio);
			
			if(x < 0) continue;
			if(y < 0) continue;
			if(x > Global.view.sizex) continue;
			if(y > Global.view.sizey) continue;
			
            graphic.setColor(stars[a].color);
            graphic.fillOval(x, y, s, s);
        }
	}
}