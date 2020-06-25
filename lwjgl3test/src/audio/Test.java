package audio;

import static org.lwjgl.openal.AL10.*;
import org.lwjgl.openal.*;
import static org.lwjgl.openal.ALC10.*;
import static javax.sound.sampled.AudioSystem.*;
import java.io.*;
import javax.sound.sampled.*;
import org.lwjgl.BufferUtils;
import java.nio.*;
import java.util.Scanner;


class Test{
	private static long device;
	private static int source;
	
	
    public static void main(String[] args) throws IOException{
    	setup();

    	// create an object of Scanner
    	Scanner inFile = new Scanner(System.in);

    	//reading input
    	int input = 0;
    	while (input != -1) {
    		System.out.println("Enter 1 for sound source looping around listener, 2 for entering a specific position for the source, or -1 to quit program");
    		input = inFile.nextInt();
    		if(input == 1) {
    			loopAround(source);
    		}
    		else if(input == 2) {
    			System.out.println("Please enter an angle from the positive X axis");
    			int angle = inFile.nextInt();
    			if(angle == -1) {
    				break;
    			}
    			System.out.println("Please enter a distance from the listener");
    			int distance = inFile.nextInt();
    			if(distance == -1) {
    				break;
    			}
    			playSound(angle, distance);
    		}
    		else if(input == -1) {
    			break;
    		}
    		else {
    			System.out.println("Please enter a valid input");
    			System.out.println();
    		}
    	}
    	
    	//closes device at the end of the program
    	alcCloseDevice(device);
    }
  
    
    public static void setup() {
	    try {
	    	// gets audio file
		    AudioInputStream stream = getAudioInputStream(new File("src/audio/ping.wav"));
		    
		    //figures our format of the given audio file
		    AudioFormat format = stream.getFormat();
		    int alFormat = -1;
		    if(format.getChannels() == 1){
		      if(format.getSampleSizeInBits() == 8){
		        alFormat = AL_FORMAT_MONO8;
		      }else if(format.getSampleSizeInBits() == 16){
		        alFormat = AL_FORMAT_MONO16;
		      }
		    }else if(format.getChannels() == 2){
		      if(format.getSampleSizeInBits() == 8){
		        alFormat = AL_FORMAT_STEREO8;
		      }else if(format.getSampleSizeInBits() == 16){
		        alFormat = AL_FORMAT_STEREO16 ;
		      }
		    }
		    if(alFormat == -1)throw new RuntimeException("can't handle format");
		    
		    //sets up buffer
		    byte[] byteArray = new byte[stream.available()];
		    stream.read(byteArray);
		    ByteBuffer audioBuffer = BufferUtils.createByteBuffer(byteArray.length);
		    audioBuffer.put(byteArray);
		    byteArray = null;
		    stream.close();
		    audioBuffer.flip();
		    
		    //opens device... needs to be closed at the end of the program
		    device = alcOpenDevice((java.lang.CharSequence)null);
		    
		    //creates contexts and capabilities
		    long context = alcCreateContext(device,(int[])null);
		    alcMakeContextCurrent(context);
		    ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		    ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
		    
		    //adds buffer to the source
		    int buffer = alGenBuffers();
		    alBufferData(buffer, alFormat, audioBuffer, (int)format.getSampleRate());
		    source = alGenSources();
			alSourcei(source,AL_BUFFER,buffer);
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
  }
  
  
  public static void loopAround(int source) {
	  try {
		  for(double i = 0; i <= (Math.PI * 2); i+= (Math.PI/6)) {
		    	alSource3f(source, AL_POSITION,  (float) (2 * Math.cos(i)), (float) 0,  (float) (2 * Math.sin(i)));
		    	alSourcePlay(source);
		    	Thread.sleep(2500);
		    }
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
  }
  
  
  public static void playSound(int angleDegrees, int distance) {
	  float angle = (float) (2 * Math.PI * angleDegrees / 360);

	  alSource3f(source, AL_POSITION,  (float) (distance * Math.cos(angle)), (float) 0,  (float) (distance * Math.sin(angle)));
  	  alSourcePlay(source);
  }
}