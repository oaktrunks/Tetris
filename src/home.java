import javax.swing.*;

public class home extends JPanel {
	
	private static final int WIDTH = 300;
    private static final int HEIGHT = 600;
    
    public home() {
    	JButton start = new JButton("Start Game");
    	JButton info = new JButton("How to Play");
    	JButton credits = new JButton("Credits");
    	
    	this.setLayout(null);
    	

    	start.setBounds(95,200,100,50);
    	this.add(start);
    	
    	info.setBounds(95,260,100,50);
    	this.add(info);
    	
    	credits.setBounds(95,320,100,50);
    	this.add(credits);
    	
    	
    	
    	
   
    }
}
