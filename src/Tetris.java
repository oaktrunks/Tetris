//Main class responsible for holding everything, doing game loop
public class Tetris {
	private static TetrisFrame frame;
	private static Board gameBoard;
	
	public static void main(String[] args) {
        
        gameStart();
        
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
	
	//Called to start our tetris game
	private static void gameStart() {
		//Initialize our frame
		frame = new TetrisFrame();
        gameBoard = new Board();
        frame.add(gameBoard);
        
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
