package demogame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Graphics2D;

public class GamePlay extends JPanel implements ActionListener, KeyListener{
	
	private static final long serialVersionUID = -3485585641295222000L;
	private boolean play = false;	//Game is not played automatically, only by user
	private int totalBricks = 21;
	private Timer timer;
	private int delay = 8;
	private int ballposX = 320;
	private int ballposY = 350;
	private int ballXdir = 1;	//How long the ball should move in X direction
	private int ballYdir = -2;
	private int playerX = 3;	//beginning of paddle
	
	private MapGenerator map;
	
	private int score = 0;
	
	public GamePlay() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(true);
		
		timer = new Timer(delay, this);
		timer.start();
		
		map = new MapGenerator(3, 7);	//Row = 3, Column = 7
		
		play = true;
	}
	
	public void paint(Graphics g) {	//inbuilt
		// black canvas
		g.setColor(Color.white);
		g.fillRect(1, 1, 692, 592);
		
		// border
		g.setColor(Color.gray);
		g.fillRect(0, 0, 692, 3);	//Up
		g.fillRect(0, 3, 3, 592);	//Left
		g.fillRect(681, 3, 3, 592);	//Right
		
		// paddle
		g.setColor(Color.red);
		g.fillRect(playerX, 555, 100, 8);
		
		// ball
		g.setColor(Color.green);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		//bricks
		map.draw((Graphics2D) g);						// ?
		
		//score
		g.setColor(Color.pink);
		g.setFont(new Font("serif", Font.BOLD, 20));
		g.drawString("Score : " + score, 550, 30);
		
		//Game Over
		if(ballposY >= 554) {	//cross paddle
			play = false;
			
			ballXdir = 0;								
			ballYdir = 0;
			
			g.setColor(Color.pink);
			g.setFont(new Font("sarif", Font.BOLD, 40));
			g.drawString("Game Over!, Score : " + score , 120, 300);
			
			g.setFont(new Font("sarif", Font.BOLD, 25));
			g.drawString("Press Enter to Restart!", 210, 350);
		}
		
		//Win
		if(totalBricks == 0) {
			play = false;

			ballXdir = 0;								
			ballYdir = 0;
			
			g.setColor(Color.pink);
			g.setFont(new Font("sarif", Font.BOLD, 40));
			g.drawString("Congratulations! You Won!", 120, 300);
			g.drawString("Score : " + score , 125, 350);
		}
	}

	private void moveLeft() {
		play = true;
		playerX -= 20;	//Go left
	}
	
	private void moveRight() {
		play = true;
		playerX += 20;	//Go Right
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {	//Left key
			if(playerX <= 3)
				playerX = 3;
			else
				moveLeft();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {	//Right key
			if(playerX >= 582)
				playerX = 582;
			else
				moveRight();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {	//Enter K=key
			if(!play) {
				
				//initial values
				score = 0;
				totalBricks = 21;
				ballposX = 320;
				ballposY = 350;
				ballXdir = 1;	//How much the ball should move in X direction
				ballYdir = -2;
				playerX = 3;
				
				map = new MapGenerator(3, 7);
			}
		}
		
		repaint();	//To change paddle position
	}

	public void actionPerformed(ActionEvent e) {
		if(play) {
			
			//Boundary
			if(ballposX <= 3) 	//Left
				ballXdir = -ballXdir;
			
			if(ballposY <= 3) 	//Up
				ballYdir = -ballYdir;
			
			if(ballposX >= 661) //Right
				ballXdir = -ballXdir;
			
			//Paddle boundary
			Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20); //Rectangle around ball
			Rectangle paddleRect = new Rectangle(playerX, 555, 100, 8);
			
			if(ballRect.intersects(paddleRect)) {
				ballYdir = -ballYdir;
			}
			
			//break brick
			A: for(int i = 0; i<map.map.length; i++) {	//1st map is object reference, 2nd is instance variable map array
				for(int j = 0; j<map.map[0].length; j++) {
					
					if(map.map[i][j] > 0) {
						int width = map.brickWidth;
						int height = map.brickHeight;
						int brickXpos = 70 + j*width;
						int brickYpos = 50 + i*height;
						
						Rectangle brickRect = new Rectangle(brickXpos, brickYpos, width, height);
						
						if(ballRect.intersects(brickRect)) {
							map.setBrick(0, i, j);
							totalBricks--;
							score += 5; 
							
							//direction change after hit
							if(ballposX + 19 <= brickXpos ||	//form left 
									ballposX + 1 >= brickXpos + width) {	//from right
								ballXdir = -ballXdir;	//change X position
							}
							else {
								ballYdir = -ballYdir;	//change Y position
							}
							break A;	//from outer for loop
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
		}
		repaint();
	}
	
	public void keyTyped(KeyEvent e) {}
	
	public void keyReleased(KeyEvent e) {}
}
