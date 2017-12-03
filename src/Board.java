import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Board extends JPanel {
	private int [][] grid;
	//10 x 20 with 4 rows of padding up top
	private int HEIGHT = 24;
	private int WIDTH = 10;
	private Color[] colors;
	
	private Position[] currentlyFallingBlock;
	
	public Board() {
		grid = new int[HEIGHT][WIDTH];
		colors = new Color[] { 
	        	new Color(0, 0, 0), new Color(204, 102, 102), 
	            new Color(102, 204, 102), new Color(102, 102, 204), 
	            new Color(204, 204, 102), new Color(204, 102, 204), 
	            new Color(102, 204, 204), new Color(218, 170, 0)
		};
		currentlyFallingBlock = new Position[4];
		currentlyFallingBlock[0] = new Position();
		currentlyFallingBlock[1] = new Position();
		currentlyFallingBlock[2] = new Position();
		currentlyFallingBlock[3] = new Position();
	}
	
	//Sets the color of a block.
	public void setBlock(int i , int j, int color) {
		grid[i][j] = color;
	}
	
	//For debug.
	//Outputs grid in 2d grid format.
	public void dump() {
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				System.out.print(grid[i][j]);
			}
			System.out.print("\n");
		}
		for(int i = 0; i < 4; i++) {
			System.out.println(currentlyFallingBlock[i].y + " " + currentlyFallingBlock[i].x);
		}
	}
	
	//Spawn a random piece to begin falling
	public void spawnRandomPiece() {
		/* 1 : red dog
		 * 2 : green dog
		 * 3 : blue L
		 * 4 : yellow square
		 * 5 : purple T
		 * 6 : blue long piece
		 * 7 : orange L
		 */
		Random rand = new Random();
		
		//int n = rand.nextInt(7) + 1;
		int n = 1;
		
		switch (n) {
			case 1: 
				currentlyFallingBlock[0].x = 0;
				currentlyFallingBlock[0].y = 5;
				grid[0][5] = 1;
				
				currentlyFallingBlock[1].x = 1;
				currentlyFallingBlock[1].y = 5;
				grid[1][5] = 1;
				
				currentlyFallingBlock[2].x = 1;
				currentlyFallingBlock[2].y = 4;
				grid[1][4] = 1;
				
				currentlyFallingBlock[3].x = 2;
				currentlyFallingBlock[3].y = 4;
				grid[2][4] = 1;
				break;
					
		}
		
	}
	
	//This function should only be called after line clear.
	// This should never be used to drop currentlyFallingBlock
	public void massGravity() {
		//if there's nothing under a block move it down
		for(int i = HEIGHT - 1; i > 3; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(grid[i+1][j] == 0) {
					grid[i+1][j] = grid[i][j];
					grid[i][j] = 0;
				}
			}
		}
	}
	
//	//This function is used to drop currentlyFallingBlock
//	public void gravity() {
//
//		}
//	}
	
	//Redraws out gameboard
	public void drawBoard() {
		System.out.println("repainting");
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
	
	@Override
	protected void paintComponent(Graphics g) {
		//System.out.println("PaintComponent called");
		super.paintComponent(g);
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(grid[i][j] > 0){
					//draw block in right position with color indicated by grid[i][j]
					drawBlock(g, grid[i][j], i, j);
					System.out.println("Drawing block at index " + i +" , " + j);
				}
			}
		}
	}
}
