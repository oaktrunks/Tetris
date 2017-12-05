import java.awt.event.*;
import javax.swing.*;

//Main class responsible for holding everything, doing game loop
public class Tetris {
	private static TetrisFrame frame;
	private static Board gameBoard;
	//private static home splash;
	private static JPanel splashScreen;
	
	public static void main(String[] args) {
		//Initialize our frame
		frame = new TetrisFrame();
		
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
	
//	@Override
//    public void actionPerformed(ActionEvent e) {
//            System.out.println(e.getActionCommand());
//    }
    
	public static void createSplash() {
		splashScreen = new JPanel();
		frame.add(splashScreen);
		
    	JButton start = new JButton("Start Game");
    	JButton info = new JButton("How to Play");
    	JButton credits = new JButton("Credits");
    	
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
    	    	//show info screen
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
	}
	
	//Called to start our tetris game
	private static void gameStart() {
		//remove our splash screen
		frame.removeAll();
		
		//add game board
        gameBoard = new Board();
        frame.add(gameBoard);
        
        //repaint our frame
        frame.revalidate();
        frame.repaint();
        
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

        //Start game loop
        int sleepTime = 750; //rough time between input polls in ms
        //int difficulty = 1; //difficulty slider, decides how fast blocks drop (1-15)
        //int counter = 0; //used with difficulty and pollRate to make blocks drop
        boolean playing = true;
        boolean isFalling = false;
        while(playing) {
        	//If nothing is currently falling generate a new shape
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
        	
        	//Sleep for pollRate milliseconds until next tick
	        try {
	            Thread.sleep(sleepTime);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
        }
        
	}
}
