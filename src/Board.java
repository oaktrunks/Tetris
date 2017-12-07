import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;

//Class that holds most of the back-end game knowledge
// Controls almost everything inside the tetris game.
public class Board extends JPanel {
	private int [][] grid;
	//10 x 20 with 4 rows of padding up top
	private int HEIGHT = 24;
	private int WIDTH = 10;
	private Color[] colors;
	
	//Blocks that are currently falling down from the screen
	private Position[] currentlyFallingBlock;
	
	//1-4 indicating current rotation
	private int currentRotation;
	
	//Used for when game is over and for difficulty adjustments
	private boolean gameOver;
	private int linesCleared;
	private int[] lineBlockCount;
	
	//Used for random shape selection
	private int[] pieceArray;
	private int pieceCounter;
	
	public Board() {
		grid = new int[HEIGHT][WIDTH];
		//Grid specifies color and status of a block
		/*
		 * 0 means it's empty
		 * 1 is red
		 * 2 is green
		 * 3 is blue
		 * 4 is yellow
		 * 5 is purple
		 * 6 is blue
		 * 7 is orange
		 * 
		 * 7 is added to each number to indicate it is still falling
		 */
		
		//array of our different shape colors. 
		colors = new Color[] { 
	        	new Color(0, 0, 0), new Color(204, 102, 102), 
	            new Color(102, 204, 102), new Color(102, 102, 204), 
	            new Color(204, 204, 102), new Color(204, 102, 204), 
	            new Color(102, 204, 204), new Color(218, 170, 0)
		};
		
		//Values vital to game over
		gameOver = false;
		linesCleared = 0;
		lineBlockCount = new int[HEIGHT];
		
		//Holds the random order that our pieces drop in
		pieceArray = new int[] {1, 2, 3, 4, 5, 6, 7};
		pieceCounter = 0; //starting at index 0
		shufflePieceArray();
		
		//Used to see which block is currently falling
		// so that we know what to rotate etc.
		currentlyFallingBlock = new Position[4];
		currentlyFallingBlock[0] = new Position();
		currentlyFallingBlock[1] = new Position();
		currentlyFallingBlock[2] = new Position();
		currentlyFallingBlock[3] = new Position();
		
		//Used to detect our inputs
		setFocusable(true);
		//Handle our inputs
		addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
            	int keyCode = e.getKeyCode();
            	
            	if(keyCode == KeyEvent.VK_DOWN) {
            		//move down a tick
            		gravity();
            		redraw();
            	}
            	if(keyCode == KeyEvent.VK_RIGHT) {
            		//move right
            		moveRight();
            		redraw();
            	}
            	if(keyCode == KeyEvent.VK_LEFT) {
            		//move left
            		moveLeft();
            		redraw();
            	}
            	if(keyCode == KeyEvent.VK_UP) {
            		//rotate
            		rotate();
            		redraw();
            	}
            	if(keyCode == KeyEvent.VK_SPACE) {
            		//instantly drop
            		while(gravity());
            		
            		redraw();
            	}
            }
		});
	}
	
	//rotates currently falling shape
	private void rotate() {
		int color = grid[currentlyFallingBlock[0].x][currentlyFallingBlock[0].y];
		
		switch (color - 7) {
			case 1: //red Z
				if (currentRotation == 1 
						&& grid[currentlyFallingBlock[1].x-1][currentlyFallingBlock[1].y] == 0
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y - 1] == 0) {
					// completely remove original blocks
					removeCurrentlyFallingBlocks();
	
					moveFallingBlock(color, 0, -1, 1);
					moveFallingBlock(color, 1, 0, 0);
					moveFallingBlock(color, 2, -1, -1);
					moveFallingBlock(color, 3, 0, -2);
	
					currentRotation = 2;
				} 
				else if (currentRotation == 2 
						&& currentlyFallingBlock[1].y < WIDTH - 1
						&& grid[currentlyFallingBlock[3].x][currentlyFallingBlock[1].y+1] == 0
						&& grid[currentlyFallingBlock[3].x][currentlyFallingBlock[2].y+2] == 0) {
					// completely remove original blocks
					removeCurrentlyFallingBlocks();
	
					moveFallingBlock(color, 0, 1, -1);
					moveFallingBlock(color, 1, 0, 0);
					moveFallingBlock(color, 2, 1, 1);
					moveFallingBlock(color, 3, 0, 2);
	
					currentRotation = 1;
				} 
				break;
			case 2: //green Z
				if (currentRotation == 1 
						&& grid[currentlyFallingBlock[1].x-1][currentlyFallingBlock[1].y] == 0
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y+1] == 0) {
					// completely remove original blocks
					removeCurrentlyFallingBlocks();
		
					moveFallingBlock(color, 0, -1, -1);
					moveFallingBlock(color, 1, 0, 0);
					moveFallingBlock(color, 2, -1, 1);
					moveFallingBlock(color, 3, 0, 2);
		
					currentRotation = 2;
				} 
				else if (currentRotation == 2 
						&& currentlyFallingBlock[1].y > 0
						&& grid[currentlyFallingBlock[3].x][currentlyFallingBlock[1].y-1] == 0
						&& grid[currentlyFallingBlock[3].x][currentlyFallingBlock[2].y-2] == 0) {
					// completely remove original blocks
					removeCurrentlyFallingBlocks();
		
					moveFallingBlock(color, 0, 1, 1);
					moveFallingBlock(color, 1, 0, 0);
					moveFallingBlock(color, 2, 1, -1);
					moveFallingBlock(color, 3, 0, -2);
		
					currentRotation = 1;
				} 
				break;
			case 3: // blue L piece
				if(currentRotation == 1
						&& currentlyFallingBlock[2].x < HEIGHT - 1
						&& grid[currentlyFallingBlock[2].x-1][currentlyFallingBlock[2].y] == 0
						&& grid[currentlyFallingBlock[2].x+1][currentlyFallingBlock[2].y] == 0
						&& grid[currentlyFallingBlock[3].x-1][currentlyFallingBlock[3].y] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 0, 2);
					moveFallingBlock(color, 1, -1, 1);
					moveFallingBlock(color, 2, 0, 0);
					moveFallingBlock(color, 3, 1, -1);
						
					currentRotation = 2;
				}
				else if(currentRotation == 2
						&& currentlyFallingBlock[2].y > 0
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y+1] == 0
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y-1] == 0
						&& grid[currentlyFallingBlock[3].x][currentlyFallingBlock[3].y+1] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 2, 0);
					moveFallingBlock(color, 1, 1, 1);					
					moveFallingBlock(color, 2, 0, 0);
					moveFallingBlock(color, 3, -1, -1);
										
					currentRotation = 3;
				}
				else if(currentRotation == 3
						&& grid[currentlyFallingBlock[2].x-1][currentlyFallingBlock[2].y] == 0
						&& grid[currentlyFallingBlock[2].x+1][currentlyFallingBlock[2].y] == 0
						&& grid[currentlyFallingBlock[3].x+1][currentlyFallingBlock[3].y] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 0, -2);
					moveFallingBlock(color, 1, 1, -1);					
					moveFallingBlock(color, 2, 0, 0);
					moveFallingBlock(color, 3, -1, 1);
										
					currentRotation = 4;
				}
				else if(currentRotation == 4
						&& currentlyFallingBlock[2].y < WIDTH - 1
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y+1] == 0
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y-1] == 0
						&& grid[currentlyFallingBlock[3].x][currentlyFallingBlock[3].y-1] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, -2, 0);
					moveFallingBlock(color, 1, -1, -1);					
					moveFallingBlock(color, 2, 0, 0);
					moveFallingBlock(color, 3, 1, 1);
										
					currentRotation = 1;
				}
				break;
				
			case 4: //square
				//do nothing
				break;
				
			case 5: //t piece
				if(currentRotation == 4
						&& currentlyFallingBlock[0].y < WIDTH - 1
						&& grid[currentlyFallingBlock[0].x][currentlyFallingBlock[0].y+1] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 0, 0);
					moveFallingBlock(color, 1, -1, 1);
					moveFallingBlock(color, 2, 1, 1);
					moveFallingBlock(color, 3, -1, -1);
						
					currentRotation = 1;
				}
				else if(currentRotation == 3
						&& grid[currentlyFallingBlock[0].x-1][currentlyFallingBlock[0].y] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 0, 0);
					moveFallingBlock(color, 1, -1, -1);					
					moveFallingBlock(color, 2, -1, 1);
					moveFallingBlock(color, 3, 1, -1);
										
					currentRotation = 4;
				}
				else if(currentRotation == 2
						&& currentlyFallingBlock[0].y > 0
						&& grid[currentlyFallingBlock[0].x][currentlyFallingBlock[0].y-1] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 0, 0);
					moveFallingBlock(color, 1, 1, -1);					
					moveFallingBlock(color, 2, -1, -1);
					moveFallingBlock(color, 3, 1, 1);
										
					currentRotation = 3;
				}
				else if(currentRotation == 1
						&& currentlyFallingBlock[0].x < HEIGHT - 1
						&& grid[currentlyFallingBlock[0].x+1][currentlyFallingBlock[0].y] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 0, 0);
					moveFallingBlock(color, 1, 1, 1);					
					moveFallingBlock(color, 2, 1, -1);
					moveFallingBlock(color, 3, -1, 1);
										
					currentRotation = 2;
				}
				break;
				
			case 6: //long piece
				//checking if rotation causes collision
				if(currentRotation == 1
						&& currentlyFallingBlock[1].x < HEIGHT - 3
						&& grid[currentlyFallingBlock[0].x+1][currentlyFallingBlock[0].y+2] == 0
						&& grid[currentlyFallingBlock[0].x+2][currentlyFallingBlock[0].y+2] == 0
						&& grid[currentlyFallingBlock[0].x+3][currentlyFallingBlock[0].y+2] == 0 
						) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					//add blocks in rotated positions
					moveFallingBlock(color, 0, -1, 2);			
					moveFallingBlock(color, 1, 0, 1);				
					moveFallingBlock(color, 2, 1, 0);
					moveFallingBlock(color, 3, 2, -1);
					
					currentRotation = 2;
				}
				else if(currentRotation == 2
						&& currentlyFallingBlock[1].y < WIDTH - 1
						&& currentlyFallingBlock[1].y > 1
						&& grid[currentlyFallingBlock[1].x][currentlyFallingBlock[1].y-2] == 0
						&& grid[currentlyFallingBlock[1].x][currentlyFallingBlock[1].y-1] == 0
						&& grid[currentlyFallingBlock[1].x][currentlyFallingBlock[1].y+1] == 0 
						) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					//add blocks in rotated positions
					moveFallingBlock(color, 0, 1, -2);			
					moveFallingBlock(color, 1, 0, -1);				
					moveFallingBlock(color, 2, -1, 0);
					moveFallingBlock(color, 3, -2, 1);
					
					currentRotation = 1;
				}
				break;
				
			case 7: //orange L piece
				if(currentRotation == 1
						&& currentlyFallingBlock[2].x < HEIGHT - 1
						&& grid[currentlyFallingBlock[2].x-1][currentlyFallingBlock[2].y] == 0
						&& grid[currentlyFallingBlock[2].x+1][currentlyFallingBlock[2].y] == 0
						&& grid[currentlyFallingBlock[1].x+1][currentlyFallingBlock[1].y] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 2, 0);
					moveFallingBlock(color, 1, 1, -1);
					moveFallingBlock(color, 2, 0, 0);
					moveFallingBlock(color, 3, -1, 1);
						
					currentRotation = 2;
				}
				else if(currentRotation == 2
						&& currentlyFallingBlock[2].y > 0
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y+1] == 0
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y-1] == 0
						&& grid[currentlyFallingBlock[1].x][currentlyFallingBlock[1].y-1] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 0, -2);
					moveFallingBlock(color, 1, -1, -1);					
					moveFallingBlock(color, 2, 0, 0);
					moveFallingBlock(color, 3, 1, 1);
										
					currentRotation = 3;
				}
				else if(currentRotation == 3
						&& grid[currentlyFallingBlock[2].x-1][currentlyFallingBlock[2].y] == 0
						&& grid[currentlyFallingBlock[2].x+1][currentlyFallingBlock[2].y] == 0
						&& grid[currentlyFallingBlock[1].x-1][currentlyFallingBlock[1].y] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, -2, 0);
					moveFallingBlock(color, 1, -1, 1);					
					moveFallingBlock(color, 2, 0, 0);
					moveFallingBlock(color, 3, 1, -1);
										
					currentRotation = 4;
				}
				else if(currentRotation == 4
						&& currentlyFallingBlock[2].y < WIDTH - 1
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y+1] == 0
						&& grid[currentlyFallingBlock[2].x][currentlyFallingBlock[2].y-1] == 0
						&& grid[currentlyFallingBlock[1].x][currentlyFallingBlock[1].y+1] == 0) {
					//completely remove original blocks
					removeCurrentlyFallingBlocks();
					
					moveFallingBlock(color, 0, 0, 2);
					moveFallingBlock(color, 1, 1, 1);					
					moveFallingBlock(color, 2, 0, 0);
					moveFallingBlock(color, 3, -1, -1);
										
					currentRotation = 1;
				}
				break;
		}
	}
	
	//Used to remove currently falling blocks 
	// so that they can be redrawn in a new position
	private void removeCurrentlyFallingBlocks() {
		for(int i = 0; i < 4; i++) {
			grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] = 0;
		}
	}
	
	//Function that moves falling block to a new position
	// color is the color of the falling block
	// i is the falling block's index
	// dx is relative position to move x by
	// dy is relative position to move y by
	// should only be called after collision has been checked
	private void moveFallingBlock(int color, int i , int dx, int dy) {
		currentlyFallingBlock[i].x += dx;
		currentlyFallingBlock[i].y += dy;
		grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] = color;
	}
	
	//Sets the color of a block. Mostly used for debugging
	public void setBlock(int i , int j, int color) {
		grid[i][j] = color;
	}
	
	//Spawn a random piece to begin falling
	// Every 7 pieces that spawn are randomly ordered
	// but every possible type of piece must show up
	// within 7 pieces.
	public void spawnRandomPiece() {
		/* 1 : red Z
		 * 2 : green Z
		 * 3 : blue L
		 * 4 : yellow square
		 * 5 : purple T
		 * 6 : blue long piece
		 * 7 : orange L
		 */
		
		//Get next piece to drop
		int n = pieceArray[pieceCounter];
		pieceCounter++;
		
		if(pieceCounter > 6) { //ran out of shapes to spawn
			shufflePieceArray();
			pieceCounter = 0;
		}
		//Setting spawning block's rotation to first rotation
		currentRotation = 1;
		
		//7 is added to grid to indicate they are falling
		//Each case is one of the different tetris shapes
		switch (n) {
			case 1: 
				currentlyFallingBlock[0].x = 0;
				currentlyFallingBlock[0].y = 3;
				grid[0][3] = 1 + 7;
				
				currentlyFallingBlock[1].x = 0;
				currentlyFallingBlock[1].y = 4;
				grid[0][4] = 1 + 7;
				
				currentlyFallingBlock[2].x = 1;
				currentlyFallingBlock[2].y = 4;
				grid[1][4] = 1 + 7;
				
				currentlyFallingBlock[3].x = 1;
				currentlyFallingBlock[3].y = 5;
				grid[1][5] = 1 + 7;
				break;
				
			case 2: 
				currentlyFallingBlock[0].x = 0;
				currentlyFallingBlock[0].y = 5;
				grid[0][5] = 2 + 7;
				
				currentlyFallingBlock[1].x = 0;
				currentlyFallingBlock[1].y = 4;
				grid[0][4] = 2 + 7;
				
				currentlyFallingBlock[2].x = 1;
				currentlyFallingBlock[2].y = 4;
				grid[1][4] = 2 + 7;
				
				currentlyFallingBlock[3].x = 1;
				currentlyFallingBlock[3].y = 3;
				grid[1][3] = 2 + 7;
				break;
				
			case 3: 
				currentlyFallingBlock[0].x = 0;
				currentlyFallingBlock[0].y = 3;
				grid[0][3] = 3 + 7;
				
				currentlyFallingBlock[1].x = 1;
				currentlyFallingBlock[1].y = 3;
				grid[1][3] = 3 + 7;
				
				currentlyFallingBlock[2].x = 1;
				currentlyFallingBlock[2].y = 4;
				grid[1][4] = 3 + 7;
				
				currentlyFallingBlock[3].x = 1;
				currentlyFallingBlock[3].y = 5;
				grid[1][5] = 3 + 7;
				break;
				
			case 4: 
				currentlyFallingBlock[0].x = 0;
				currentlyFallingBlock[0].y = 4;
				grid[0][4] = 4 + 7;
				
				currentlyFallingBlock[1].x = 0;
				currentlyFallingBlock[1].y = 5;
				grid[0][5] = 4 + 7;
				
				currentlyFallingBlock[2].x = 1;
				currentlyFallingBlock[2].y = 4;
				grid[1][4] = 4 + 7;
				
				currentlyFallingBlock[3].x = 1;
				currentlyFallingBlock[3].y = 5;
				grid[1][5] = 4 + 7;
				break;
				
			case 5: 
				currentlyFallingBlock[0].x = 1;
				currentlyFallingBlock[0].y = 4;
				grid[1][4] = 5 + 7;
				
				currentlyFallingBlock[1].x = 0;
				currentlyFallingBlock[1].y = 4;
				grid[0][4] = 5 + 7;
				
				currentlyFallingBlock[3].x = 1;
				currentlyFallingBlock[3].y = 3;
				grid[1][3] = 5 + 7;
				
				currentlyFallingBlock[2].x = 1;
				currentlyFallingBlock[2].y = 5;
				grid[1][5] = 5 + 7;
				break;
				
			case 6: 
				currentlyFallingBlock[0].x = 1;
				currentlyFallingBlock[0].y = 3;
				grid[1][3] = 6 + 7;
				
				currentlyFallingBlock[1].x = 1;
				currentlyFallingBlock[1].y = 4;
				grid[1][4] = 6 + 7;
				
				currentlyFallingBlock[2].x = 1;
				currentlyFallingBlock[2].y = 5;
				grid[1][5] = 6 + 7;
				
				currentlyFallingBlock[3].x = 1;
				currentlyFallingBlock[3].y = 6;
				grid[1][6] = 6 + 7;
				break;
				
			case 7: 
				currentlyFallingBlock[0].x = 0;
				currentlyFallingBlock[0].y = 5;
				grid[0][5] = 7 + 7;
				
				currentlyFallingBlock[1].x = 1;
				currentlyFallingBlock[1].y = 5;
				grid[1][5] = 7 + 7;
				
				currentlyFallingBlock[2].x = 1;
				currentlyFallingBlock[2].y = 4;
				grid[1][4] = 7 + 7;
				
				currentlyFallingBlock[3].x = 1;
				currentlyFallingBlock[3].y = 3;
				grid[1][3] = 7 + 7;
				break;
		}
		gravity();
		gravity();
		redraw();
		
	}
	
	//Puts our 7 possible piece in a random drop order
	// This way the game feels fair
	// and you're not getting the same piece 4 times in a row
	// or waiting forever to get a line.
	private void shufflePieceArray()
	{
	    int index;
	    Random random = new Random();
	    for (int i = pieceArray.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        if (index != i)
	        {
	        	pieceArray[index] ^= pieceArray[i];
	        	pieceArray[i] ^= pieceArray[index];
	        	pieceArray[index] ^= pieceArray[i];
	        }
	    }
	}
	
	//This function should only be called after line clear.
	// This should never be used to drop currentlyFallingBlock
	//brings every line above parameter line, down one
	public void massGravity(int line) {
		//iterate over lines above line
		for(int i = line - 1; i >= 0; i--) {
			//set lineBlockCount down one line
			lineBlockCount[i+1] = lineBlockCount[i];
			lineBlockCount[i] = 0;
			
			//iterate over blocks, move them down
			for(int j = 0; j < WIDTH; j++) {
				if(grid[i+1][j] == 0) {
					grid[i+1][j] = grid[i][j];
					grid[i][j] = 0;
				}
			}
		}
	}
	
	//This function is used to drop currentlyFallingBlock one tick
	//Returns true if blocks moved down, false if everything is static
	// Also spawns a new shape if current one lands.
	// Checks if game is over
	public boolean gravity() {
		if(gameOver)
			return false;
		
		//return value
		boolean fell = true;
		//if there's no collision
		if(canFall()) {
			//move every falling piece down one
			int color = grid[currentlyFallingBlock[0].x][currentlyFallingBlock[0].y];
			
			//completely remove original blocks
			for(int i = 0; i < 4; i++) {
				grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] = 0;
			}
			//recreating shape one spot down
			for(int i = 0; i < 4; i++) {
				//moving piece location down
				currentlyFallingBlock[i].x++;
				
				//changing block's color
				grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] = color;
			}
		}
		//if there is collision set every piece to static
		// update lineBlockCounts
		// check if lines are clear
		// and spawn a new block
		else {
			for(int i = 0; i < 4; i++) {
				//just checking if it's greater than 7 to solve future problems
				if(grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] > 7) {
					//set every piece to same color. but static (minus 7)
					grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] -= 7;
				}
				
				//updating lineBlockCount
				lineBlockCount[currentlyFallingBlock[i].x]++;
				
			}
			for(int i = 0; i < 4; i++) {
				//checking if line is full
				if(isLineFull(currentlyFallingBlock[i].x)) {
					//clear line, increment score
					clearLine(currentlyFallingBlock[i].x);
				}
			}
			//redraw after clearing lines
			redraw();
			
			fell = false;
			
			//Check if game is over
			checkGameOver();		

			spawnRandomPiece();
			
		}
		//redraw();
		
		return fell;
	}
	
	//Checks if there's a collision with currentlyFallingBlock
	public boolean canFall() {
		boolean collision = false;
		
		//check if static block is under any of the four pieces
		for(int i = 0; i < 4; i++) {
			if(currentlyFallingBlock[i].x == HEIGHT - 1) {
				collision = true;
			}
			else if(grid[currentlyFallingBlock[i].x + 1][currentlyFallingBlock[i].y] > 0
					&& grid[currentlyFallingBlock[i].x + 1][currentlyFallingBlock[i].y] < 8) {
				collision = true;
			}
		}
		
		return !collision;
	}
	
	//Returns true if a line is full of blocks
	public boolean isLineFull(int line) {
		return lineBlockCount[line] == WIDTH;
	}
	
	//Empties a line
	private void clearLine(int line) {
		linesCleared++;
		//clear the line
		for(int i = 0; i < WIDTH; i++) {
			grid[line][i] = 0;
		}
		//reset block counter
		lineBlockCount[line] = 0;
		
		//drop everything above that line down one
		massGravity(line);
	}
	
	//Moves currently falling shape to the right
	public void moveRight() {
		boolean canMove = true;
		//check if any of four pieces are on right most column
		for(int i = 0; i < 4; i++) {
			if(currentlyFallingBlock[i].y == WIDTH - 1) {
				canMove = false;
			}
		}
		//check if any of four pieces have anything to the right
		for(int i = 0; canMove && i < 4; i++) {
			if(grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y+1] > 0 
					&& grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y+1] < 8 ) {
				canMove = false;
			}
		}
		if(canMove) {
			//move every block to the right
			int color = grid[currentlyFallingBlock[0].x][currentlyFallingBlock[0].y];
			
			//completely remove original blocks
			for(int i = 0; i < 4; i++) {
				grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] = 0;
			}
			//recreating shape one spot right
			for(int i = 0; i < 4; i++) {
				//moving piece location right
				currentlyFallingBlock[i].y++;
				
				//changing block's color
				grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] = color;
			}
		}
	}
	
	//Moves currently falling shape to the left
	public void moveLeft() {
		boolean canMove = true;
		//check if any of four pieces are on right most column
		for(int i = 0; i < 4; i++) {
			if(currentlyFallingBlock[i].y == 0) {
				canMove = false;
			}
		}
		//check if any of four pieces have anything to the left
		for(int i = 0; canMove && i < 4; i++) {
			if(grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y-1] != 0
					&& grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y-1] < 8 ) {
				canMove = false;
			}
		}
		if(canMove) {
			//move every block to the left
			int color = grid[currentlyFallingBlock[0].x][currentlyFallingBlock[0].y];
			
			//completely remove original blocks
			for(int i = 0; i < 4; i++) {
				grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] = 0;
			}
			//recreating shape one spot left
			for(int i = 0; i < 4; i++) {
				//moving piece location left
				currentlyFallingBlock[i].y--;
				
				//changing block's color
				grid[currentlyFallingBlock[i].x][currentlyFallingBlock[i].y] = color;
			}
		}
	}
	
	//Updates this.gameOver to indicate
	// current status of game
	private void checkGameOver() {
		//if there's a static block in first two rows, game is over
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j  < WIDTH; j++) {
				if(grid[i][j] > 0 && grid[i][j] < 8) {
					gameOver = true;
				}
			}
		}
	}
	
	//returns lines cleared
	public int getLinesCleared() {
		return linesCleared;
	}
	
	//returns true if game is over
	public boolean isGameOver() {
		return gameOver;
	}
	
	//Redraws gameboard
	public void redraw() {
		repaint();
	}
	
	//Draws a block
	public void drawBlock(Graphics g, int color, int i, int j) {
		g.setColor(colors[color]);
		//index * (25 + spacing between pieces) + spacing between screen edges
		int squareX = j * 27 + 13;
		int squareY = i * 27 - 80;
		g.fillRect(squareX,squareY,25,25);
	}
	
	//Rendering function
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(grid[i][j] > 0){
					//draw block in right position with color indicated by grid[i][j]
					if(grid[i][j] < 8)
						drawBlock(g, grid[i][j], i, j);
					else
						drawBlock(g, grid[i][j] - 7, i, j);
				}
			}
		}
	}
}
