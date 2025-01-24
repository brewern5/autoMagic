/*
 *  This creates a new window and presses the 'C' key to cast spells and then will wait for an hour so magicka
 *  can regenerate in Elder Scrolls Oblivion
 *  @Author Nate Brewer
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import javafx.beans.value.ChangeListener;

import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent; 

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public abstract class autoMagicMain implements ActionListener, KeyListener, ChangeListener {

    public static SwingWorker sw;

    public static boolean firstStart;

    public static Robot robot;

    // This is the Key Code value of 'C'
    public static int spellCast = 67;

    public static int wait = 84;

    public static int castTimes = 14; 

    public static void startThread(){

        sw = new SwingWorker() {

            @Override
            protected String doInBackground()
            throws Exception {

                System.out.println("Starting Thread");

                Thread.sleep(2000);

                try{
                    robot = new Robot();
                } catch(AWTException e){
                    e.printStackTrace();
                }

                while(true){
                    try{
                        for(int i = 0; i < castTimes; i++){
                            robot.keyPress(KeyEvent.VK_C);
                            Thread.sleep(2000);
                        }
                            robot.keyPress(KeyEvent.VK_T);
                            Thread.sleep(1000);
                            robot.keyPress(KeyEvent.VK_DOWN);
                            Thread.sleep(500);
                            robot.keyPress(KeyEvent.VK_DOWN);
                            Thread.sleep(500);
                            robot.keyPress(KeyEvent.VK_ENTER);
                            Thread.sleep(2500);
                    } catch (InterruptedException e){
                        //e.printStackTrace();
                        System.out.println("Ending Thread");
                        sw.cancel(true);
                        break;
                    }
                }
                return "Finished";
            }
        };

    }

    public static void startStop(Boolean onOff, boolean firstStart){

        if(onOff){
            
            startThread();
            sw.execute();
            
        }
        else if(!onOff){
            sw.cancel(true);
        }
        
    }

    public static void main(String[] args) throws Exception{

        firstStart = true;

        JFrame frame = new JFrame("Oblivion Auto Caster");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new GridLayout(3, 1));
        //frame.setLayout(new GridLayout(2, 1));

        frame.addWindowListener(new WindowAdapter() { 
            // Method 
            @Override
            public void windowClosing(WindowEvent e) 
            { 
                System.exit(0); 
            } 
        }); 
        
        // Constructs the panel that holds the inpuit text fields
        JPanel textPanel = new JPanel();

        JTextArea spellCastInput = new JTextArea("Enter the Key you use to Spell Cast\nThe Default is 'C'"); //user can switch their spell cast button in this application
        
        JSeparator vertInputSeparator1 = new JSeparator();
        vertInputSeparator1.setOrientation(SwingConstants.VERTICAL);

        JTextArea waitInput = new JTextArea("Enter the Key you use to wait\nThe Default is 'T'"); //user can switch their wait key bind in this application

        JButton spellChangeButton = new JButton("Enter");

        JSeparator vertInputSeparator2 = new JSeparator();
        vertInputSeparator2.setOrientation(SwingConstants.VERTICAL);

        JButton waitChangeButton = new JButton("Enter");
        

        // Constructs the slider to select the amount of times the spell is cast before waiting
        JPanel sliderPanel = new JPanel();

        JSeparator sliderSeparator = new JSeparator();

        JLabel sliderLabel = new JLabel("(0-100) How many times do you want to cast?");

        JSlider useTimes = new JSlider(); //user can determine how often they can cast before waiting

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


        textPanel.add(spellCastInput);
        textPanel.add(vertInputSeparator1);
        textPanel.add(waitInput);
        textPanel.add(spellChangeButton);
        textPanel.add(vertInputSeparator2);
        textPanel.add(waitChangeButton);
        textPanel.setLayout(new GridLayout(2, 3));


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
        

        spellChangeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ev){
                String s = ev.getActionCommand();
                if (spellChangeButton.equals("Enter")){

                }
            }
        });


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
                startStop(true, firstStart);
                firstStart = false;
            }
        });
        // Turns off the loop that presses the desired key
        stop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ev){
                startStop(false, firstStart);
            }
        });

        //frame.setLayout(new BorderLayout());
        frame.add(textPanel, BorderLayout.CENTER);

        frame.add(sliderPanel, BorderLayout.CENTER);

        frame.add(btnPanel, BorderLayout.CENTER);

        frame.pack();

        frame.setVisible(true);
    }

}


