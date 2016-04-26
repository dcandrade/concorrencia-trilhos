/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.Point;
import model.Quadro;
import model.Train;
import util.ITrain;

/**
 *
 * @author solenir
 */
public class MainWindow {
   private JFrame frame;
   private Container container;
   private Quadro quadro;
   private Facade facade; 
   private Train myTrain;
  
   
   
    public MainWindow(String title, Train myTrain) throws RemoteException, NotBoundException, MalformedURLException, AlreadyBoundException{
     
        this.myTrain = myTrain;
        this.container = new JPanel(new CardLayout());
        this.quadro = new Quadro(Color.blue);   
        
        this.container.add(quadro, "mainPrincipal");
        
        
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(container);
        frame.setResizable(false);
        setWindowSize(1000, 700);
        frame.setVisible(true);
        initialPanelMount();
    
    }
    
    private void setWindowSize(int width, int height){
        this.frame.setSize(width, height);
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setLocation((tela.width - width) / 2, (tela.height - height) / 2);
    }
    
    ITrain trainThree ;

    private void initialPanelMount() throws RemoteException, NotBoundException {
       ((CardLayout)container.getLayout()).show(container, "mainPrincipal"); 
        quadro.repaint();
        quadro.insertPoint(myTrain);
      
        JSlider slideMyTrain = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        slideMyTrain.setMajorTickSpacing(1);
        slideMyTrain.setMinorTickSpacing(1);
        slideMyTrain.setPaintTicks(true);
        slideMyTrain.setPaintLabels(true);
        slideMyTrain.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                if (!j.getValueIsAdjusting()) {
                    try {
                        myTrain.setSpeed((int) j.getValue());
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        JButton buttonInitial =  new JButton("Iniciar");
        buttonInitial.setBounds(850, 80, 100, 50);
        buttonInitial.setBorder(null);
        quadro.add(buttonInitial);
        buttonInitial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	startTrains();
            	
            	
            }
        });
        
     
        
     
        quadro.add(slideMyTrain);
        slideMyTrain.setBounds(1, 580, 992, 80);
        
        Thread t = new Thread(this.quadro);
        t.start();
       
        
            	
    }
    private void startTrains(){
    	try {
			this.facade = new Facade(myTrain);
			TreeMap<Integer, ITrain> trainsAux = facade.getTrain();
			ITrain trainAuxOne = trainsAux.get(trainsAux.firstKey());
			ITrain trainAuxTwo = trainsAux.get(trainsAux.lastKey());
			quadro.insertPoint(trainAuxOne);
			quadro.insertPoint(trainAuxTwo);
			
			JSlider slideAux = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
	        slideAux.setMajorTickSpacing(1);
	        slideAux.setMinorTickSpacing(1);
	        slideAux.setPaintTicks(true);
	        slideAux.setPaintLabels(true);
	        slideAux.addChangeListener(new ChangeListener() {

	            @Override
	            public void stateChanged(ChangeEvent e) {
	                JSlider j = (JSlider) e.getSource();
	                if (!j.getValueIsAdjusting()) {
	                    try {
	                        trainAuxOne.setSpeed((int) j.getValue());
	                    } catch (RemoteException ex) {
	                        ex.printStackTrace();
	                    }
	                }
	            }
	        });
	        
	        
	        JSlider slideAux2 = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
	        slideAux2.setMajorTickSpacing(1);
	        slideAux2.setMinorTickSpacing(1);
	        slideAux2.setPaintTicks(true);
	        slideAux2.setPaintLabels(true);
	        slideAux2.addChangeListener(new ChangeListener() {

	            @Override
	            public void stateChanged(ChangeEvent e) {
	                JSlider j = (JSlider) e.getSource();
	                if (!j.getValueIsAdjusting()) {
	                    try {
	                    	
	                    	 trainAuxTwo.setSpeed((int) j.getValue());
	                    } catch (RemoteException ex) {
	                        ex.printStackTrace();
	                    }
	                }
	            }
	        });
	        	        	              	      
	        slideAux.setBounds(1, 510, 992, 80);
	        slideAux2.setBounds(1, 440, 992, 80);
	         
	        quadro.add(slideAux);
	        quadro.add(slideAux2);
	    
	        myTrain.setReady();
	        try {
	        	while(trainAuxOne.noIsReady() && trainAuxTwo.noIsReady() )
	        		quadro.startPoints();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    }
    
    
}
