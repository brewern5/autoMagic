/*
 *  This creates a new window and presses a user defined key to cast spells and then will 
 *  wait for an in-game hour so magicka can regenerate and the program can continue to cast 
 *  spells automatically while gaining experience in their respective school of magic.
 *  This is designed to work in Elder Scrolls Oblivion.
 * 
 *  @Author Nate Brewer
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import javafx.beans.value.ChangeListener;

import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent; 

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public abstract class autoMagicMain implements ActionListener, KeyListener, ChangeListener {

    // Will hold the thread that executes the main loop of this program
    public static SwingWorker sw;

    // Queries the user screen size
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // Only need the Y value since the mouse will only move down
    public static int centerY = screenSize.height/2;

    // The Entity that creates the keystokes/mouse movements
    public static Robot robot;

    // This is the Key Code value of 'C'
    public static int spellCast = 67;

    // This is the Key Code value of 'T'
    public static int wait = 84;

    // This will store the users amount of times they wish to cast before waiting
    public static int castTimes; 

    /*
     *  startThread will create a swingworker to create a new thread in order to preserve GUI
     *  integrety so the user can still stop and start the program. When the swing worker is 
     *  created it will repeatedly press the user desired button for spell casting and then
     *  after the user defined 'cast time', it will bring up the in-game 'wait' menu and wait
     *  for 1 hour to replenish Magicka to contiune casting spells. This will run until the user
     *  closes the program or hits 'End'
     */

    public static void startThread(){

        // Creates a seperate Thread from the GUI so the user can still start and stop the program
        sw = new SwingWorker() {

            @Override
            protected String doInBackground()
            throws Exception {

                System.out.println("Starting Thread");
                System.out.println("Screen Size: " + screenSize);
                try{
                    robot = new Robot();
                } catch(AWTException e){
                    e.printStackTrace();
                }
                
                Thread.sleep(5000);

                while(true){
                    try{

                        // Will press the user's desired 'spell cast' button
                        for(int i = 0; i < castTimes; i++){

                            System.out.println("C pressed");
                            robot.keyPress(KeyEvent.VK_C);
                            Thread.sleep(250);

                            System.out.println("C released");
                            robot.keyRelease(KeyEvent.VK_C);

                            Thread.sleep(750);
                        }
                            
                            // Presses the key to open the wait menu
                            System.out.println("T pressed");
                            robot.keyPress(KeyEvent.VK_T);
                            Thread.sleep(250);

                            System.out.println("T released");
                            robot.keyRelease(KeyEvent.VK_T);
                            Thread.sleep(500);

                            // Queries mouse info to move it accuratly down to the wait button
                            PointerInfo mouseInfo = MouseInfo.getPointerInfo();
                            Point mousePoint = mouseInfo.getLocation();

                            int mouseX = (int)mousePoint.getX();
                            int mouseY = (int)mousePoint.getY(); 

                            // Moves mouse to the wait button
                            System.out.println("Mouse Moved To Wait");
                            robot.mouseMove(mouseX, mouseY+(centerY/4)+30);
                            System.out.println(mouseX + ", " + (mouseY - ((centerY/4)+ 30 )));
                            Thread.sleep(500);

                            // Pressing the wait button in the wait menu

                            System.out.println("M1 Pressed");
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            Thread.sleep(250);

                            System.out.println("M1 Released");
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);


                            // Wait for for the wait screen to release back to the game
                            Thread.sleep(6000);
                    } catch (InterruptedException e){
                        //e.printStackTrace();
                        System.out.println("Ending Thread");
                        sw.cancel(true);
                        break;
                    }
                }
                System.out.println("Worker Destroyed");
                return "Finished";
            }
        };

    }

    // Helper method to stop the swing worker
    public static void startStop(Boolean onOff){

        if(onOff){
            
            startThread();
            sw.execute();
            
        }
        else if(!onOff){
            sw.cancel(true);
        }
        
    }

    public static void main(String[] args) throws Exception{

        /*
         *  Start JFrame constructor
         */

        JFrame frame = new JFrame("Oblivion Spell Caster");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new GridLayout(3, 1));

        frame.addWindowListener(new WindowAdapter() { 
            // Method 
            @Override
            public void windowClosing(WindowEvent e) 
            { 
                System.exit(0); 
            } 
        }); 

        /*
         *  End JFrame constructor
         */
        

        /*
         *  Start JPanel constructors
         */

        // Constructs the panel that holds the inpuit text fields
        JPanel textPanel = new JPanel();

        JLabel spellCastLabel = new JLabel("Enter the Key you use to Spell Cast. The Default is 'C'");

        JSeparator vertInputSeparator1 = new JSeparator();
        vertInputSeparator1.setOrientation(SwingConstants.VERTICAL);

        JLabel waitChangeLabel = new JLabel("Enter the Key you use to wait. The Default is 'T'");

        // User can switch their spell cast button in this application
        JTextArea spellCastInput = new JTextArea();
        
        JSeparator vertInputSeparator2 = new JSeparator();
        vertInputSeparator2.setOrientation(SwingConstants.VERTICAL);
        
        // User can switch their wait key bind in this application
        JTextArea waitInput = new JTextArea(); 

        JButton spellChangeButton = new JButton("Enter");

        JSeparator vertInputSeparator3 = new JSeparator();
        vertInputSeparator3.setOrientation(SwingConstants.VERTICAL);

        JButton waitChangeButton = new JButton("Enter");
        

        // Constructs the slider to select the amount of times the spell is cast before waiting
        JPanel sliderPanel = new JPanel();

        JSeparator sliderSeparator = new JSeparator();

        JLabel sliderLabel = new JLabel("(0-100) How many times do you want to cast?");

        JSlider useTimes = new JSlider(); 

        JLabel userCastTimes = new JLabel("");

        useTimes.setPaintTicks(true);
        useTimes.setPaintTrack(true);
        useTimes.setPaintLabels(true);

        useTimes.setMajorTickSpacing(10);
        useTimes.setMinorTickSpacing(1);
    

        // Constructs the panel that starts/stops the loop that presses the key
        JPanel btnPanel = new JPanel();

        JSeparator btnSeparator1 = new JSeparator();
        btnSeparator1.setOrientation(SwingConstants.HORIZONTAL);

        JSeparator btnSeparator2 = new JSeparator();
        btnSeparator2.setOrientation(SwingConstants.HORIZONTAL);

        JButton start = new JButton("Start");
        start.setPreferredSize(new Dimension(80, 20));

        JButton stop = new JButton("Stop");
        stop.setPreferredSize(new Dimension(80, 20));


        textPanel.add(spellCastLabel);
        textPanel.add(vertInputSeparator1);
        textPanel.add(waitChangeLabel);
        textPanel.add(spellCastInput);
        textPanel.add(vertInputSeparator2);
        textPanel.add(waitInput);
        textPanel.add(spellChangeButton);
        textPanel.add(vertInputSeparator3);
        textPanel.add(waitChangeButton);
        textPanel.setLayout(new GridLayout(3, 3));


        sliderPanel.add(sliderSeparator);
        sliderPanel.add(sliderLabel);
        sliderPanel.add(useTimes);
        sliderPanel.add(userCastTimes);
        sliderPanel.setLayout(new GridLayout(4, 1));


        btnPanel.add(btnSeparator1);
        btnPanel.add(btnSeparator2);
        btnPanel.add(start);
        btnPanel.add(stop);
        btnPanel.setLayout(new GridLayout(2, 1));

        /*
         *  End JPanel Constructors
         */


        /*
         *  Start actionListener Constructors 
         */

        spellChangeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ev){
                String s = ev.getActionCommand();
                if (spellChangeButton.equals("Enter")){
                    // TODO: store user entered key and replace the current 'spellcast' key
                }
            }
        });

        waitChangeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ev){
                String s = ev.getActionCommand();
                if (waitChangeButton.equals("Enter")){
                    // TODO: store user entered key and replace the current 'wait' key
                }
            }
        });


        // Slider value for the cast times
        useTimes.addChangeListener(new javax.swing.event.ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e){
                userCastTimes.setText("("+useTimes.getValue()+") Times");
                castTimes = useTimes.getValue();
            }
        });


        // Will turn on the loop that presses the desired key repeatedly
        start.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ev){
                startStop(true);
            }
        });
        // Turns off the loop that presses the desired key
        stop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ev){
                startStop(false);
            }
        });

        /*
         *  End actionListener Constructors
         */


        
        /*
         *  Adding JPanels to the JFrame
         */

        frame.add(textPanel, BorderLayout.CENTER);

        frame.add(sliderPanel, BorderLayout.CENTER);

        frame.add(btnPanel, BorderLayout.CENTER);

        frame.pack();

        frame.setVisible(true);
    }

}


