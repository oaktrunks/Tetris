import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.util.Timer;

//Main class responsible for holding everything, doing game loop
public class Tetris {
	private static TetrisFrame frame;
	private static Board gameBoard;
	private static JPanel splashScreen;
	private static JPanel infoScreen;
	private static JPanel creditScreen;
	private static JPanel gameOverScreen;
	private static Timer timer;
	
	private static int timerDelay;	
	private static int difficulty;
	
	public static void main(String[] args) {
		//Initialize our frame
		frame = new TetrisFrame();
			
		//Start splash screen
		createSplash();
	}
    
	//Creates the splash screen
	public static void createSplash() {
		frame.getContentPane().removeAll();
		
		splashScreen = new JPanel();
		
    	JButton start = new JButton("Start Game");
    	JButton info = new JButton("How to Play");
    	JButton credits = new JButton("Credits");
    	
    	JLabel heading = new JLabel("Tetris");
    	heading.setBounds(60, 25, 250, 250);
    	heading.setFont (heading.getFont ().deriveFont (64.0f));
    	splashScreen.add(heading);
    	
    	
    	splashScreen.setLayout(null);
    	
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
    	    	createCreditsScreen();
    	    }
    	});
    	splashScreen.add(credits);
    	
    	frame.add(splashScreen);
    	frame.revalidate();
        frame.repaint();
	}
	
	//Creates the info screen
	public static void createInfoScreen() {
		//new info screen
		infoScreen = new JPanel();
		//remove the current screen and revalidate
		frame.getContentPane().removeAll();
        
		infoScreen.setLayout(null);
		
		JButton back = new JButton("Back");
		JLabel title = new JLabel("How to Play");
		JLabel control = new JLabel("Controls:");
		JLabel controls = new JLabel("<html><p><ul><li>SpaceBar:<br></i> Instantly moves a piece to the bottom.<br><br>"
				+ "<li>Down Arrow:<br> Accelerates the piece downward.<br><br>"
				+ "<li>Left Arrow:<br> Moves the piece to the left.<br><br>"
				+ "<li>Right Arrow:<br> Moves the piece to the right.<br><br>"
				+ "<li>Up Arrow:<br> Rotates the piece.<br><br>");
		
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
    	    	//createGameOverScreen();
    	    }
    	});
    	infoScreen.add(back);
    	
		frame.add(infoScreen);
        frame.revalidate();
        frame.repaint();
	}
	
	//Creates the credits screen
	public static void createCreditsScreen() {
		creditScreen = new JPanel();
		frame.getContentPane().removeAll();
        
        creditScreen.setLayout(null);
        
		JButton back = new JButton("Back");
		JLabel title = new JLabel("Developers:");
		JLabel names = new JLabel("<html>Daniel Tomei<br> Tyler Coleman<br> Carlos Placencia</html>");
		
    	title.setBounds(50, 25, 200, 75);
    	title.setFont (title.getFont ().deriveFont (30.0f));
    	creditScreen.add(title);
    	
    	names.setBounds(50,30,300,220);
    	names.setFont (names.getFont ().deriveFont (24.0f));
    	creditScreen.add(names);
    	
    	back.setBounds(100, 400, 75, 30);
    	back.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent evt) {
    	    	createSplash();
    	    }
    	});
    	creditScreen.add(back);
    	
		frame.add(creditScreen);
        frame.revalidate();
        frame.repaint();
	}
	
	//Creates the game over screen
	public static void createGameOverScreen() {
		int linesCleared = gameBoard.getLinesCleared();
		
		gameOverScreen = new JPanel();
		//remove the current screen and revalidate
		frame.getContentPane().removeAll();
        
        gameOverScreen.setLayout(null);
        
		JButton back = new JButton("Back to Main Menu");
		JLabel title = new JLabel("Game Over");
		JLabel msg = new JLabel("You cleared " + linesCleared + " lines!" );
		
    	title.setBounds(65, 25, 200, 75);
    	title.setFont (title.getFont ().deriveFont (30.0f));
    	gameOverScreen.add(title);
    	
    	msg.setBounds(35,30,300,220);
    	msg.setFont (msg.getFont ().deriveFont (24.0f));
    	gameOverScreen.add(msg);
    	
    	back.setBounds(45, 400, 200, 30);
    	back.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent evt) {
    	    	createSplash();
    	    }
    	});
    	gameOverScreen.add(back);
    	
		frame.add(gameOverScreen);
        frame.revalidate();
        frame.repaint();
	}
	
	//Called to start our tetris game
	private static void gameStart() {
		//remove our splash screen
		frame.getContentPane().removeAll();

		//add game board
        gameBoard = new Board();
        frame.add(gameBoard);
        
        
        //repaint our frame
        frame.revalidate();
        frame.repaint();
        
        //gameBoard needs focus so that KeyAdapter works.
        gameBoard.requestFocusInWindow();
        
        //Start game
        difficulty = 1; //1 - 12
        gameBoard.spawnRandomPiece();
		timerDelay = 750; //Starting delay at difficulty 1
		
        startTimer(timerDelay);
        
	}
	
	//This creates a new timer, should only be used after
	// canceling the active timer
	// int delay - ms interval to run timer on
	public static void startTimer(int delay) {
		timer = new Timer();
        //Start running our game loop on a timer.
        timer.schedule(new TimerTask() {
        	@Override
        	public void run() {
        		gameLoop();
        	}
        }, 0 , delay);
	}
	
	//Gameloop responsible for keeping our game running
	// should be called on an interval by a timer
	public static void gameLoop() {
    	//Fall down one tick
    	if(gameBoard.gravity()) { //something fell
        	gameBoard.redraw();
    	}
    	
    	if(gameBoard.isGameOver()) {
    		//Stop gameloop timer
    		timer.cancel();
    		timer.purge();
    		
    		//Redirect to game over screen
    		createGameOverScreen();
    	}
    	
    	//Make game go faster if threshold has been met, up to certain difficulty
    	if(difficulty <= 12 && gameBoard.getLinesCleared() / 7 > difficulty) {
    		//increase difficulty
    		difficulty++;
    		
    		//make a new timer with a faster interval
    		timer.cancel();
    		timer.purge();
    		
    		timerDelay = 750 - difficulty * 50; //max difficulty is 50 ms
            startTimer(timerDelay);
    	}
	}
	
}
