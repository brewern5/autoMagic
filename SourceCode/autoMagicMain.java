/*
 *  This creates a new window and presses the 'C' key to cast spells and then will wait for an hour so magicka
 *  can regenerate in Elder Scrolls Oblivion
 *  @Author Nate Brewer
 */

import java.awt.*;
import javax.swing.*;

import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent; 

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public abstract class autoMagicMain implements ActionListener, KeyListener {

    public static SwingWorker sw;

    public static boolean firstStart;

    public static Robot robot;


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

                robot.keyPress(KeyEvent.VK_C);

                while(true){
                    try{
                        for(int i = 0; i < 14; i++){
                            robot.keyPress(KeyEvent.VK_C);
                            Thread.sleep(2000);
                        }
                            robot.keyPress(KeyEvent.VK_T);
                            robot.keyPress(KeyEvent.VK_DOWN);
                            robot.keyPress(KeyEvent.VK_DOWN);
                            robot.keyPress(KeyEvent.VK_ENTER);
                            Thread.sleep(2500);
                    } catch (InterruptedException e){
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

        JFrame frame = new JFrame("JFrame Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(2, 1));

        frame.addWindowListener(new WindowAdapter() { 
            // Method 
            @Override
            public void windowClosing(WindowEvent e) 
            { 
                System.exit(0); 
            } 
        }); 

        JPanel panel = new JPanel();
        JButton start = new JButton("Start");

        JButton stop = new JButton("Stop");

        panel.add(start);
        panel.add(stop);

        // Add action to the button
        start.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent ev){
                startStop(true, firstStart);
                firstStart = false;
            }
        });

        stop.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent ev){
                startStop(false, firstStart);
            }
        });

        frame.setLayout(new BorderLayout());

        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

}


