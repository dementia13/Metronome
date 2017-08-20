package Metronome;

/* 
 **
 * 
 * @author Sean Quinn
 * 
 * Project: Versatile Metronome
 * 
 * Purpose: Create a metronome with advanced practice features
 * 
 * 	- clicks over entire range of tempi used in music
 * 	- supports simple, compound and complex meters
 *  - cut time
 *  - includes major and minor accents for each bar
 *  - audible and visual display
 *  - visual display adjusts to meter
 *  - selectable click sound
 *  - eventual support for loops:
 *  	- MIDI loops
 *  	_ time-stretched samples
 *  	- onboard loop creation
 * 
 */


// ---- IMPORTS ----
import java.util.concurrent.*;

// ---- FILE HANDLING ----
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

//---- MIDI LIBRARIES ----
import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Metronome implements javax.sound.midi.Sequencer{
	  
/* -- CONSTANTS --------------------------------------------*/
	
	final static int MINUTE = 60;
	final static long NANO = 1000000000;
	
    // -- AUDIO FILE NAMES
	private final static String NML_BEAT = "src/nml_beat.wav";
	private final static String PRI_ACC = "src/pri_acc.wav";
	private final static String SEC_ACC = "src/sec_acc.wav";
	
	// -- NOTE VELOCITIES --
	private final int PRI_ACCENT = 105;
	private final int SEC_ACCENT = 95;
	private final int VELOCITY_NORM = 80;
	
	// -- MIDI TRACK 10 IS CUSTOMARILY FOR PERCUSSION SOUNDS --
	private final int TRACK = 9;	
	
/* -- VARIABLE DECLARATIONS --------------------------------*/
	
	// -- INTERFACE
	static int lower = 4;	// upper & lower
	static int upper = 4;	// time signature components
	
    //private int controllers[] = new int[0];
    private long currentPosition = 0;
    private static float factor;
    private static boolean loop = true;
    private int loopCount = 0;
    private long loopEndPoint = 0;
    private long loopStartPoint = 0;
    private int position = 0;
    private long sequenceLength = 0;
    static Sequencer sequencer = new Metronome();
    //private Soundbank soundbank = null;
    private static Sequence sq;
    //private static Sequencer sequencer;
    static int tempoInBPM = 120;
    private float tempoInMPQ = 2.0f * (float)Math.pow(10, -6);
	static long time = System.nanoTime();
	
	// -- TIME SIGNATURE CONSTANTS ----------------------------------
    final static String[] LOWER_VALUES = {"1", "2", "4", "8", "16"};
	final static String[] UPPER_VALUES = {"2", "3", "4", "5", "6", "7", 
			"8", "9", "11", "12", "13", "15"};

	static long beatLength = (long)((double)MINUTE / tempoInBPM * NANO);
	static long barLength = beatLength * upper;
    
    MidiDevice.Info metInfo;
    
    private static void Metronome(){
        final int MIN_TEMPO = 20;
        final int MAX_TEMPO = 500;
        int def_tempo = 120;
        int tempo = def_tempo;
    }
    
    private static Soundbank metBank;
    
    @Override
    
    public int[] addControllerEventListener(ControllerEventListener listener,
                               int[] controllers){
        return controllers;
    }
    
    public boolean addMetaEventListener(MetaEventListener listener){
        return false;
    }
    
    public void close(){
        
    }
    
    public MidiDevice.Info getDeviceInfo(){
        return metInfo;
    }
    
    public int getLoopCount(){
        return LOOP_CONTINUOUSLY;
    }
    
    public long getLoopEndPoint(){
        return loopEndPoint;
    }
    
    public long getLoopStartPoint(){
        return loopStartPoint;
    }
    
    public Sequencer.SyncMode getMasterSyncMode(){
        return Metronome.SyncMode.INTERNAL_CLOCK;
    }
    
    public Sequencer.SyncMode[] getMasterSyncModes(){
        Sequencer.SyncMode[] masterSyncModes = {Metronome.SyncMode.INTERNAL_CLOCK};
        return masterSyncModes;
    }
    
    public int getMaxReceivers(){
        return 0;
    }
    
    public int getMaxTransmitters(){
        return 0;
    }
    
    public long getMicrosecondLength(){
        return sequenceLength;
    }

    public long getMicrosecondPosition(){
        return position;
    }
    
    public Receiver getReceiver()
                     throws MidiUnavailableException{
        return null;
    }
    
    public java.util.List<Receiver> getReceivers(){
        return null;
    }
    
    public Sequencer.SyncMode getSlaveSyncMode(){
        return Metronome.SyncMode.NO_SYNC;
    }
    
    public Sequencer.SyncMode[] getSlaveSyncModes(){
        Sequencer.SyncMode[] slaveSyncMode = {};
        return slaveSyncMode;
    }
    
    public Sequence getSequence(){
        return sq;
    }
    
    public float getTempoFactor(){
        return factor;
    }
    
    public float getTempoInBPM(){
        return tempoInBPM;
    }
    
    public float getTempoInMPQ(){
        return tempoInMPQ;
    }
    
    public long getTickLength(){
        return sequenceLength;
    }
    
    public long getTickPosition(){
        return currentPosition;
    }
    /*
    static public Timer getTimer(){
    	return new Timer(true);
    }
	*/
    public boolean getTrackMute(int track){
        return false;
    }
    
    public boolean getTrackSolo(int track){
        return false;
    }
    
    public Transmitter getTransmitter()
                           throws MidiUnavailableException{
        return null;
    }
    
    public java.util.List<Transmitter> getTransmitters(){
        return null;
    }
    
    public boolean isOpen(){
        return false;
    }
    
    public boolean isRecording(){
        return false;
    }
    
    public  boolean isRunning(){
        return false;
    }
    
    public void open(){
        
    }
    
    public void recordDisable(Track track){
        track = null;
    }
    
    public void recordEnable(Track track,
                int channel){
        track = null;
        channel = 0;
    }
            
    public int[] removeControllerEventListener(ControllerEventListener listener,
                               int[] controllers){
        return controllers;
    }
    
    public void  removeMetaEventListener(MetaEventListener listener){
        
    }
    
    public void setLoopCount(int count){
        loopCount = count;
    } 
    
    public void setLoopEndPoint(long tick){
        loopEndPoint = tick;
    }
    
    public void setLoopStartPoint(long tick){
        loopStartPoint = tick;
    }
    
    public void setMasterSyncMode(Metronome.SyncMode sync){
        sync = Metronome.SyncMode.INTERNAL_CLOCK;
    }
    
    public void setMicrosecondPosition(long microseconds){
        position = 0;
    }
    
    public void setSequence(InputStream stream)
                 throws IOException,
                        InvalidMidiDataException{
        try{
            this.setSequence(stream);
        }
        catch(IOException e){
            System.out.println("Error reading sequence.");
        }
    }
    
    public void setSequence(Sequence sequence)
                 throws InvalidMidiDataException{
        
    }
    
    @Override
    public void setSlaveSyncMode(Sequencer.SyncMode sync){
        sync = Metronome.SyncMode.NO_SYNC;
    }
    
    @Override
    public void setTempoFactor(float factor){
        factor = 1.0f;
    }
    
    public void setTempoInBPM(float bpm){
        tempoInBPM = (int)(factor * bpm);
    }
    
    public void setTempoInMPQ(float mpq){
        
    }
    
    public void setTickPosition(long tick){
        currentPosition = tick;
    }

    public void setTrackMute(int track,
                boolean mute){
        
    }
    
    public void setTrackSolo(int track,
                boolean solo){
        
    }
    
    public void start(){
    	
    }
    
    public static void start(Sequence sq, boolean loop){
    	//playSound(PRI_ACC);
    	/*
    	if((sequencer != null) && (sq != null)
    			&& (sequencer.isOpen())){
    		try{
    			sequencer.setSequence(sq);
    			sequencer.start();
    			loop = true;
    		}
    		catch(InvalidMidiDataException imdex){
    			imdex.printStackTrace();
    		}
    	}*/
    }
    
    public void startRecording(){
        
    }
    
    public void stop(){
    	
    }
    
    public static void stop(Sequence sq){
        if (sequencer != null && sequencer.isOpen()) {
            sequencer.stop();
            sequencer.setMicrosecondPosition(0);
          }
    }
    
    public void stopRecording(){
        
    }
    
    // ---- Audio Classes & Methods --- 
    
    static class playBar {
    	Runnable bar = new Runnable(){
    		public void run(){
	    		while(beat < upper){
		    		if (beat == 0){ 	// play primary accent
		    			playSound(PRI_ACC);
		    				// & send to display
		    		}
		        	else{
		        		playSound(NML_BEAT);
		        	}
		    	}
    		}
    	};
    	int beat = 0;
    	long delay = 0;
		ScheduledExecutorService executor = 
				Executors.newSingleThreadScheduledExecutor();

		private void loopPlay(){
			ScheduledFuture aBar = executor.scheduleAtFixedRate(bar, 
					delay, beatLength, TimeUnit.NANOSECONDS);
			beat++;
	    	
		}
    }
    
    static class playBeat extends TimerTask{
			
			public void run() {
					//notePlay(99);
				for(int beat = 0; beat < upper; beat++){
					notePlay(beat);
					//System.out.println(time);
				}
			}
        
        static private  void notePlay(int beat){
        	//for(int beat = 0; beat < upper; beat++){
        		if (beat % time == 0){ 	// play primary accent
        			playSound(PRI_ACC);
        			//System.out.println("Accent");
        				// & send to display
        		}
        		// determine location of secondary accent
        		// if beat % time = secondary accent, play secondary accent
            	else
            		if((beat + 1) == getSecAcc()){
            			playSound(SEC_ACC);
            			//System.out.println("Weak Accent");
            		}
        		/*
            		else
            			if(beat == 99){
            				// silent start beat for timing
            				playSound(null);
            			}
        		*/
            		//if (beat % time)
            		//playSound(SEC_ACC);
    				// & send to display
        		// else play normal beat
            	else{
            		playSound(NML_BEAT);
            		//System.out.println("beat");
        			// & send to display
            	}
        	//beatTimer.schedule(beatCount, 0, barLength / upper);	
        	//}
        }
    }

    static int getSecAcc(){
    	switch (upper){
    	case 1:
    		return 0;
    	case 2:
    		return 0;
    	case 3:
    		return 0;
    	case 4:
    		return 3;
    	case 5:
    		return 4;
    	case 6:
    		return 4;
    	case 7:
    		return 5;
    	case 8:
    		return 5;
    	case 9:
    		return 4;
    	case 11:
    		return 4;
    	case 12:
    		return 7;
    	case 13:
    		return 5;
    	case 15:		// Possum Kingdom
    		return 8;
		default:
			return 0;
    	}
    }
    
    static void playSound(String fileString) { 

    	try {
    		File sFile = new File(fileString);
    		AudioInputStream inputStream = AudioSystem.getAudioInputStream(sFile.toURI().toURL());
    		Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start(); 
    	} 
		catch (Exception e) {
	        System.err.println(e.getMessage());
		}
    }
    
    // ---- UI Classes ----
    
	
	public static void main(String[] args) {
		/*
        // Obtain information about all the installed synthesizers.
       Vector synthInfos = null;
       MidiDevice device;
       MidiDevice.Info[] infos;
       infos = MidiSystem.getMidiDeviceInfo();
       for (int i = 0; i < infos.length; i++) {
           try {
    		   File clickFile = new File("../bin/4-4.mid");
    		   File clickBank = new File("../bin/click.sf2");
               device = MidiSystem.getMidiDevice(infos[i]);
               //Sequencer sequencer = new Metronome();
               sequencer.open();
               Sequence sq = MidiSystem.getSequence(clickFile);
               Synthesizer metSynth = MidiSystem.getSynthesizer();
               metSynth.open();
               //Soundbank click = (Soundbank)clickBank;
               Instrument[] metClick = metSynth.getDefaultSoundbank().getInstruments();
               //metSynth.loadInstrument(metClick[116]);
              // metSynth.loadInstruments(click, woodblock);
           }
               catch (MidiUnavailableException e) {
                   // Handle or throw exception...
             } catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           Metronome metronome = new Metronome();
           //Sequence sq = metronome.getSequence();

               //sq.start();
           /*
           if (device instanceof Synthesizer) {
               synthInfos.add(infos[i]);
           }
           System.out.println(infos[i]);  */    
       /*
       //Schedule a job for the event-dispatching thread:
       //creating and showing this application's GUI.
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
	    catch (UnsupportedLookAndFeelException e) {
	        // handle exception
	     }
	     catch (ClassNotFoundException e) {
	        // handle exception
	     }
	     catch (InstantiationException e) {
	        // handle exception
	     }
	     catch (IllegalAccessException e) {
	        // handle exception
	     }
		*/
        
		//MetGUI metWindow = new MetGUI();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				MetGUI.createAndShowGUI();
		        //String fileString = "/Applications/Metronome/Files/pri_acc.wav";
			}
		});
		
       }
	 
}
