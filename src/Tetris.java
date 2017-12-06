import java.awt.event.*;
import javax.swing.*;

import javafx.scene.image.Image;

import java.util.*;
import java.util.Timer;

//Main class responsible for holding everything, doing game loop
public class Tetris {
	private static TetrisFrame frame;
	private static Board gameBoard;
	//private static home splash;
	private static JPanel splashScreen;
	private static JPanel infoScreen;
	private static Timer timer;
	
	private static boolean isFalling;
	private static int timerDelay;
	
	public static void main(String[] args) {
		//Initialize our frame
		frame = new TetrisFrame();
		
		timer = new Timer();
		isFalling = false;
		timerDelay = 750;
		
		//Start splash screen
		createSplash();
		
		
        //gameStart();
        
//        gameBoard.setBlock(0, 0, 0);
//        gameBoard.setBlock(0, 1, 1);
//        gameBoard.setBlock(0, 2, 2);
//        gameBoard.setBlock(0, 3, 3);
//        gameBoard.setBlock(0, 4, 4);
//        gameBoard.setBlock(0, 5, 5);
//        gameBoard.setBlock(0, 6, 6);
//        gameBoard.setBlock(0, 7, 7);
//        gameBoard.setBlock(0, 8, 8);
        
        
        //gameBoard.dump();
        //gameBoard.redraw();
	}
    
	public static void createSplash() {
		frame.getContentPane().removeAll();
		
		splashScreen = new JPanel();
		
    	JButton start = new JButton("Start Game");
    	JButton info = new JButton("How to Play");
    	JButton credits = new JButton("Credits");
    	
    	JLabel heading = new JLabel("Tetris");
    	heading.setBounds(65, 25, 250, 250);
    	heading.setFont (heading.getFont ().deriveFont (64.0f));
    	splashScreen.add(heading);
    	
//    	JLabel heading = new JLabel(new ImageIcon("/pics/tetlog.jpg"));
//    	heading.setBounds(65, 25, 250, 250);
//    	splashScreen.add(heading);
    	
    	splashScreen.setLayout(null);
    	
    	//TODO implement buttons, fix gameStart()
    	start.setBounds(95,200,100,50);
    	start.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent evt) {
    	    	//start game
    	    	gameStart();
    	    }
    	});
    	splashScreen.add(start);
    	
    	info.setBounds(95,260,100,50);
    	info.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent evt) {
    	    	createInfoScreen();
    	    }
    	});
    	splashScreen.add(info);
    	
    	credits.setBounds(95,320,100,50);
    	credits.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent evt) {
    	    	//show credits screen
    	    }
    	});
    	splashScreen.add(credits);
    	
    	frame.add(splashScreen);
    	frame.revalidate();
        frame.repaint();
	}
	
	public static void createInfoScreen() {
		//new info screen
		infoScreen = new JPanel();
		//remove the current screen and revalidate
		frame.getContentPane().removeAll();
		frame.add(infoScreen);
        frame.revalidate();
        frame.repaint();
        
		infoScreen.setLayout(null);
		
		JButton back = new JButton("Back");
		JLabel title = new JLabel("How to Play");
		JLabel control = new JLabel("Controls:");
		JLabel controls = new JLabel("<html><p><ul><li>Up Arrow & SpaceBar:<br></i> instantly moves a piece to the bottom.<br><br>"
				+ "<li>Down Arrow:<br> Accelerates the piece downward.<br><br>"
				+ "<li>Left Arrow:<br> Moves the piece to the left.<br><br>"
				+ "<li>Right Arrow:<br> Moves the piece to the right.<br><br>"
				+ "<li>Z:<br> Rotates the piece counter-clockwise.<br><br>"
				+ "<li>X:<br> Rotates the piece clockwise.</ul></p></html>");
		
    	title.setBounds(70, 25, 150, 25);
    	title.setFont (title.getFont ().deriveFont (24.0f));
    	infoScreen.add(title);
    	
    	control.setBounds(0, 30, 300, 150);
    	control.setFont (title.getFont ().deriveFont (20.0f));
    	infoScreen.add(control);
    	controls.setBounds(0, 100, 300, 300);
    	controls.setFont (title.getFont ().deriveFont (12.0f));
    	infoScreen.add(controls);
    	
    	back.setBounds(100, 400, 75, 30);
    	back.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent evt) {
    	    	createSplash();
    	    }
    	});
    	infoScreen.add(back);
    	
	}
	
	//Called to start our tetris game
	private static void gameStart() {
		//remove our splash screen
		//System.out.println("removing all panels");
		frame.getContentPane().removeAll();
		//splashScreen.setVisible(false);
		//System.out.println("removed all panels");
		
		//add game board
		//System.out.println("adding gameboard");
        gameBoard = new Board();
        frame.add(gameBoard);
        
        //System.out.println("added gameboard");
        
        //repaint our frame
        //System.out.println("revalidating and repainting");
        frame.revalidate();
        frame.repaint();
        //frame.setVisible(true);
        //System.out.println("adding gameboard");
        
        //gameBoard needs focus so that KeyAdapter works.
        gameBoard.requestFocusInWindow();
        
        //debug blocks
//        gameBoard.setBlock(4, 0, 1);
//        gameBoard.setBlock(4, 1, 1);
//        gameBoard.setBlock(4, 2, 2);
//        gameBoard.setBlock(4, 3, 3);
//        gameBoard.setBlock(4, 4, 4);
//        gameBoard.setBlock(4, 5, 5);
//        gameBoard.setBlock(4, 6, 6);
//        gameBoard.setBlock(4, 7, 7);
//        gameBoard.setBlock(4, 9, 7);
//        
//        gameBoard.setBlock(23, 0, 1);
//        gameBoard.setBlock(23, 1, 1);
//        gameBoard.setBlock(23, 2, 2);
//        gameBoard.setBlock(23, 3, 3);
//        gameBoard.setBlock(23, 4, 4);
//        gameBoard.setBlock(23, 5, 5);
//        gameBoard.setBlock(23, 6, 6);
//        gameBoard.setBlock(23, 7, 7);
//        gameBoard.setBlock(23, 9, 7);
        
        //Start running our game loop on a timer.
        timer.schedule(new TimerTask() {
        	@Override
        	public void run() {
        		gameLoop();
        	}
        }, 0 , timerDelay);
        
	}
	
	public static void gameLoop() {
    	//If nothing is currently falling generate a new shape
    	System.out.println("playing");
    	if(!isFalling) {
    		gameBoard.spawnRandomPiece();
    		isFalling = true;
    	}     	
    	      	
    	
    	
    	//Fall down one tick
//        	gameBoard.dump();
    	if(gameBoard.gravity()) { //something fell
        	//Redraw board after falling
        	gameBoard.redraw();
//	        	gameBoard.dump();
    	}
    	else { //everything is now static
    		isFalling = false;
    	}      
	}
	
}
