/**
 * @(#)SpaceRPG
 *
 * SpaceRPG Applet application
 *
 * @author 
 * @version 1.00 2012/6/21
 */
 
 //TREE IS TESTING
 
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
		int x=800;
		int y=600;
		
		// Set GUI attributes
		this.setLayout(new FlowLayout());
		board.setPreferredSize(new Dimension(x, y));

		// Place elements
		this.add(board);
		view = new GameView(this, x, y, createImage(x,y));
		
		//Add input listener(s)
		Player p = new Player();
		addKeyListener(p);
		addMouseListener(p);
		
		
		//Initialize the global variables
		Global.game = this;
		Global.view = view;
		Global.codeContext = getCodeBase();
		
		
		state = new GameState();
		Global.state = state;
		
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
	
	public void setState(GameState newState){
		state = newState;
		Global.state = newState;
	}
}