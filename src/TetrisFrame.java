import javax.swing.*;

public class TetrisFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Game grid size is 10*20
	private static final int WIDTH = 300;
    private static final int HEIGHT = 600;
    
    public TetrisFrame() {
    	setSize(WIDTH, HEIGHT);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setTitle("Tetris");
    	setResizable(false);
    	setVisible(true);
    }

}
