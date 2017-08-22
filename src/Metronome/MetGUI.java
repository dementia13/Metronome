package Metronome;

//---- GUI LIBRARIES ----
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Metronome.Metronome.playBar;
import Metronome.Metronome.playBeat;

public class MetGUI extends Frame implements WindowListener,ActionListener {
	private static boolean clicked = false;
	private static boolean stopped = true;
	public ScheduledExecutorService exec;

    static class BarLoop{
    	boolean isRunning;
    	playBeat beat = new playBeat();
    	ScheduledExecutorService exec = 
    			Executors.newSingleThreadScheduledExecutor();
    	Runnable runnable = new Runnable(){
    		public void run(){
    			beat.run();
    		}
    	};
    	ScheduledFuture loop = exec.scheduleAtFixedRate
    			(runnable, 0, (long)((double)Metronome.MINUTE / 
    					Metronome.tempoInBPM * Metronome.NANO), 
    							TimeUnit.NANOSECONDS);
    	
    	void play(){
				try {
					loop.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
    	}
    	
    	void stop(){
    		loop.cancel(true);
    		exec.shutdownNow();
    	}

    }
    
    static BarLoop theLoop;
    
    static BarLoop getLoop(){
    	return theLoop;
    }
    
    static int getTempo(){
    	return (int)Metronome.tempoInBPM;
    }

    static void setLoop(BarLoop newLoop, boolean running){
    	theLoop = newLoop;
    	theLoop.isRunning = running;
    }
    
    public static void setTempo(int tempo){
    	Metronome.tempoInBPM = tempo;
    }
    
    public MetGUI() {

        //super(title);
        setLayout(new GridBagLayout());
        addWindowListener(this);
}
    

    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
static void createAndShowGUI() {
		
		/*
	    class SBAction extends AbstractAction {
	    	
	    	//protected Action startAction, stopAction;
	        
	    	
	        public SBAction(String text, ImageIcon icon,
	                          String desc, Integer mnemonic) {
	            super(text, icon);
	            putValue(SHORT_DESCRIPTION, desc);
	            putValue(MNEMONIC_KEY, mnemonic);
	        }
	        
	        public void actionPerformed(ActionEvent e) {
		
        String fileString = "/Users/sean/Documents/Programming/Eclipse/workspace/Metronome/src/click.wav";

        playSound();
	        }
	    
	            //displayResult("Action for first button/menu item", e);
	    

        SBAction startAction = new SBAction("Start", null,
                "Start the metronome",
                new Integer(KeyEvent.VK_L));
        
        SBAction stopAction = new SBAction("Stop", null,
                "Stop the metronome",
                new Integer(KeyEvent.VK_L)); 
        
        SBAction tempoAction = new SBAction("Tempo", null,
        		"Set the tempo",
        		new Integer(KeyEvent.KEY_LOCATION_NUMPAD));
	    }
        */
		JFrame metFrame = new JFrame("Metronome");
		metFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel metLabel = new JLabel("", SwingConstants.CENTER);
		metLabel.setPreferredSize(new Dimension(1136, 640));
		metLabel.setFont(metLabel.getFont().deriveFont(Font.BOLD));
		metLabel.setBackground(Color.WHITE);
		
		
		//-- DRAW MAIN FRAME --
		JLayeredPane layeredPane = new JLayeredPane();
		JPanel theContentPane = new JPanel();
		layeredPane.add(theContentPane, new Integer(0));
		metFrame.setContentPane(theContentPane);
		theContentPane.setBackground(Color.white);
		theContentPane.setPreferredSize(new Dimension(1136, 640));
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints mainConstraints = new GridBagConstraints();
		mainConstraints.fill = GridBagConstraints.BOTH;
		metFrame.setContentPane(theContentPane);
		
		//-- ADD INPUT PANEL AT TOP OF MAIN FRAME
		JPanel inputPane = new JPanel();
		inputPane.setBackground(Color.white);
		inputPane.setPreferredSize(new Dimension(1136, 60));
		theContentPane.add(inputPane, new Integer(10));
		mainConstraints.fill = GridBagConstraints.BOTH;
		mainConstraints.gridwidth = 3;
		mainConstraints.ipady = 75;
		mainConstraints.gridx = 0;
		mainConstraints.gridy = 0;
		layout.setConstraints(inputPane, mainConstraints);
		
		//-- CREATE INPUT FIELDS
			//-- TEMPO --
		GridBagConstraints inputConstraints = new GridBagConstraints();
		JPanel tempoPanel = new JPanel(new FlowLayout());
		tempoPanel.setBackground(Color.white);
		JLabel tempoLabel = new JLabel("Tempo:");
		JTextField tempoText = new JTextField(Integer.toString(getTempo()));
		JButton tempoButton = new JButton("Set");
		tempoPanel.add(tempoLabel);
		tempoPanel.add(tempoText);
		tempoPanel.add(tempoButton);
		inputPane.add(tempoPanel, new Integer(20));
		inputConstraints.weightx = 0;
		inputConstraints.ipadx = 10;
		inputConstraints.gridx = 0;
		inputConstraints.gridy = 0;
		layout.setConstraints(tempoPanel, inputConstraints);

		tempoText.addActionListener(null);
		tempoButton.addActionListener(null);
		
			//-- TIME SIGNATURE --
		JLabel sigLabel = new JLabel("Time Signature:");
		//JLabel upperTime = new JLabel("4");
		JComboBox upperTime = new JComboBox(Metronome.UPPER_VALUES);
		upperTime.setEditable(false);
		upperTime.setSelectedItem("4");
		final JLabel timeDivider = new JLabel("/");
		//JLabel lowerTime = new JLabel("4");
		JComboBox lowerTime = new JComboBox(Metronome.LOWER_VALUES);
		lowerTime.setEditable(false);
		lowerTime.setSelectedItem("4");
		JPanel timePanel = new JPanel();
		timePanel.setBackground(Color.white);
		inputPane.add(timePanel, new Integer(20));
		inputConstraints.weightx = 0;
		inputConstraints.gridx = 2;
		layout.setConstraints(timePanel, inputConstraints);
		
		//-- ADD INPUTS TO PANEL
		GridBagConstraints timeConstraints = new GridBagConstraints();
		timePanel.add(sigLabel, new Integer(30));
		timeConstraints.ipadx = 10;
		timeConstraints.gridx = 0;
		timeConstraints.gridy = 0;
		timePanel.add(upperTime, new Integer(30));
		timeConstraints.ipadx = 10;
		timeConstraints.gridx = 1;
		layout.setConstraints(upperTime, timeConstraints);
		timePanel.add(timeDivider, new Integer(30));
		timeConstraints.ipadx = 10;
		timeConstraints.gridx = 2;
		layout.setConstraints(timeDivider, timeConstraints);
		timePanel.add(lowerTime, new Integer(30));
		timeConstraints.ipadx = 10;
		timeConstraints.gridx = 3;
		layout.setConstraints(lowerTime, timeConstraints);
		
		// -- HORIZONTAL SLIDER ----------------------------------------
		JPanel sliderPanel = new JPanel();
		JSlider tempoSlider = new JSlider(60, 300);
		tempoSlider.setPreferredSize(new Dimension (1136,60));
		tempoSlider.setPaintTicks(true);
		//tempoSlider.setMajorTickSpacing(10);		// too cluttered
		tempoSlider.setMinorTickSpacing(4);
		tempoSlider.createStandardLabels(4);
		tempoSlider.setPaintLabels(true);
		tempoSlider.setSnapToTicks(true);
		sliderPanel.add(tempoSlider);
		
		theContentPane.add(sliderPanel, new Integer(20));
		GridBagConstraints sliderConstraints = new GridBagConstraints();
		sliderConstraints.fill = GridBagConstraints.HORIZONTAL;
		sliderConstraints.gridx = 0;
		sliderConstraints.gridy = 1;
		sliderConstraints.gridwidth = GridBagConstraints.REMAINDER;
		layout.setConstraints(sliderPanel, sliderConstraints);
		//sliderPanel.setVisible(true);		// unnecessary
		
		tempoSlider.addChangeListener(null);
		
		
		
		//-- ADD PANEL FOR LIGHTS AT LEFT OF MAIN FRAME
		JPanel displayPane = new JPanel(new FlowLayout());
		theContentPane.add(displayPane, new Integer(10));
		displayPane.setPreferredSize(new Dimension(800, 540));
		displayPane.setBackground(Color.white);
		mainConstraints.anchor = GridBagConstraints.BASELINE_LEADING;
		mainConstraints.weightx = 0.5;
		mainConstraints.ipadx = 0;
		mainConstraints.gridx = 0;
		mainConstraints.gridy = 2;
		layout.setConstraints(displayPane,  mainConstraints);
		
		//-- ADD LIGHTS TO DISPLAY --
		
			//	Create #LEDs equal to variable "upper"
			//	
			//	Swap LED images depending on beat
		
		//-- ADD BUTTON PANE AT RIGHT OF MAIN FRAME --
		JPanel buttonPane = new JPanel();
		theContentPane.add(buttonPane, new Integer(10));
		buttonPane.setPreferredSize(new Dimension(250, 540));
		buttonPane.setBackground(Color.white);
		mainConstraints.anchor = GridBagConstraints.BASELINE_TRAILING;
		mainConstraints.weightx = 0.66;
		mainConstraints.gridx = 1;
		mainConstraints.gridy = 2;
		layout.setConstraints(buttonPane, mainConstraints);
		
		//-- CREATE START & STOP BUTTONS
		GridBagConstraints btnConstraints = new GridBagConstraints();
		ImageIcon startIcon = new ImageIcon("/Applications/Metronome/files/start.png");
		ImageIcon stopIcon = new ImageIcon("/Applications/Metronome/files/stop.png");
		
		JButton startButton = new JButton(startIcon);
		buttonPane.add(startButton, new Integer(20));
		startButton.setPreferredSize(new Dimension(250, 250));
		startButton.setBorder(null);
		startButton.setEnabled(true);
		btnConstraints.anchor = GridBagConstraints.NORTH;
		layout.setConstraints(startButton, btnConstraints);

		JButton stopButton = new JButton(stopIcon);
		buttonPane.add(stopButton, new Integer(20));
		stopButton.setPreferredSize(new Dimension(250, 250));
		stopButton.setBorder(null);
		stopButton.setEnabled(true);
		btnConstraints.anchor = GridBagConstraints.SOUTH;
		layout.setConstraints(stopButton, btnConstraints);

		// -- BUTTON ACTION LISTENERS ----------------------------------
		
		startButton.addActionListener(new java.awt.event.ActionListener(){
			public void startActionPerformed(java.awt.event.ActionEvent evt){
				actionPerformed(evt);
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
			    BarLoop newLoop = new BarLoop();
				if(!clicked){
					stopped = false;
					clicked = true;
					setLoop(newLoop, true);
				}
			}
		});
		
		stopButton.addActionListener(new ActionListener(){
			public void stopActionPerformed(ActionEvent evt){
				actionPerformed(evt);
			}
			
			public void actionPerformed(ActionEvent e){
				if(!stopped){
					getLoop().stop();
				}
				clicked = false; 
				stopped = true;
			}
		});
		
		// -- TEMPO ACTION LISTENERS -------------------------------------
		
		tempoButton.addActionListener(new ActionListener(){
			public void tempoButtonAction(ActionEvent evt){
				
			}
			
			public void actionPerformed(ActionEvent e){
				setTempo(Integer.valueOf(tempoText.getText()));
				if(!stopped){
					stopButton.doClick();
					startButton.doClick();
				}
			}
		});
		
		tempoSlider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				setTempo(tempoSlider.getValue());
				tempoText.setText(Integer.toString(getTempo()));
			}
			
		});
		
		tempoText.addActionListener(new java.awt.event.ActionListener(){
			public void tempoTextActionPerformed(ActionEvent evt){
				actionPerformed(evt);
			}
			
			public void actionPerformed(ActionEvent e){
			}
		});
		
		// -- TIME SIGNATURE ACTION LISTENERS --------------------------
		
		lowerTime.addActionListener(new ActionListener(){
			public void lowerTimeAction(ActionEvent evt){
				
			}
			public void actionPerformed(ActionEvent e){
				Metronome.lower = (int) lowerTime.getSelectedItem();
			}
		});
		
		upperTime.addActionListener(new ActionListener(){
			public void upperTimeAction(ActionEvent evt){
				
			}
			public void actionPerformed(ActionEvent e){
				Metronome.upper = 
						Integer.valueOf((String) upperTime.getSelectedItem());
			}
		});
		
		inputPane.setVisible(true);
		tempoLabel.setVisible(true);
		displayPane.setVisible(true);
		buttonPane.setVisible(true);
		
		metFrame.setVisible(true); 
		
		metFrame.pack();
		
	
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		Metronome.sequencer.close();
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
