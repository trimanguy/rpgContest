/**
 * @(#)SpaceRPG
 *
 * SpaceRPG Applet application
 *
 * @author 
 * @version 1.00 2012/6/21
 */
 
import java.awt.*;
import java.awt.geom.*;
import java.applet.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GameApplet extends JApplet implements Runnable {
	
	// Drawing panel
  	Thread thread = new Thread(this);
  	public JPanel board = new JPanel();
  	public GameState state;
  	public GameView view;
  	

	// Called once when Applet is first opened in browser 	  
	public void init() {
		//Applet dimensions
		int x=1024;
		int y=768;
		
		// Set GUI attributes
		this.setLayout(new FlowLayout());
		board.setPreferredSize(new Dimension(x, y));

		// Place elements
		this.add(board);
		view = new GameView(this, x, y, createImage(x,y));
		
		//Add input listeners
		
		
		// Start the thread, which calls the run() method.
		view.thread.start();
		thread.start();
	}
	
	public void update(Graphics G){
		paint(G);
	}
	
	public void run(){
		
		while(true){
			if(state != null){
				state.Tick();
			}
			try{
				thread.sleep(10);//100 ticks/second
			} catch(Exception E){
				
			}
		}
	}
	
	public void destroy(){
		//Unload resources and stuff here
	}
}