/**
 * @(#)SoundObj.java
 *
 *
 * @author 
 * @version 1.00 2012/7/31
 */

import java.io.*;
import javax.sound.sampled.*;
import java.util.jar.*;
import java.net.*;

public class SoundObj {

    public SoundObj(Boolean firing, String soundType) {
    	
    	String soundPath="";
    	
    	if(soundType.equals("laser")){
    		
    		if(firing){
    			int type = Utils.randomNumberGen(1,4);
    			soundPath = "Resources/Sounds/laser"+type+".wav";
    			//System.out.println("soundpath :"+soundPath);	
    		} else {
    			soundPath = "Resources/Sounds/laserhit1.wav";
    		}
    		
    	}else if(soundType.equals("missile")){
    		
    		if(firing){
    			soundPath = "Resources/Sounds/missile1.wav";
    		} else {
    			soundPath = "Resources/Sounds/missilehit1.wav";
    		}
    		
    	}
    	
		try{
	    	URL soundURL = getClass().getClassLoader().getResource(soundPath);
			AudioInputStream ain = AudioSystem.getAudioInputStream(soundURL);
			
	    	//InputStream is = getClass().getResourceAsStream(soundPath);
			//AudioInputStream ain = AudioSystem.getAudioInputStream(is);
			
			DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat());      
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(ain);
			//clip.setFramePosition(0);
			clip.start();
			System.out.println("PLAY SOUND CLIP "+soundPath);
		}
		catch(UnsupportedAudioFileException e){}
		catch(LineUnavailableException u){}
		catch(IOException i){}
    	
    }
    
}