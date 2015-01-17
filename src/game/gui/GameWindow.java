package game.gui;

import game.world.World;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 * The main game window of HEX.
 * @author Sherman Ying
 * @version January 21, 2013
 * @since 1.7
 */
public class GameWindow extends JFrame implements Runnable, KeyListener
{
	private static final long serialVersionUID = 7838555421534168349L;
	private static String[] factionNames = {
		"None", "Human", "Obeswarm", "MechStorm"
	};
	/**
	 * Runs the game
	 * 
	 * @param args
	 *            Ignored.
	 */
	public static void main(final String[] args)
	{
		new GameWindow();
	}

	private int[] playerFactions;
	
	private int selectedOption;
	private int upKey;

	// Key codes
	private int downKey;
	private int leftKey;
	private int rightKey;
	private int confirmKey;
	private int cancelKey;
	private int actionKey;
	private boolean upPressed;

	// Saves key press states
	private boolean downPressed;
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean confirmPressed;
	private boolean cancelPressed;
	private boolean actionPressed;

	// The game view
	private World map;
	private Sidebar bar;
	private WorldView view;
	private int activeScreen;
	private Dimension viewSize;
	private Dimension barSize;
	private Dimension windowSize;
	
	/**
	 * Creates a new game window.
	 */
	public GameWindow() {
		this.setTitle("HEX");// small caps HEX is \u029C\u1D07x
		viewSize = new Dimension(750, 600);
		barSize = new Dimension(250, 600);
		windowSize = new Dimension(1000, 600);
		this.setDefaultKeys();
		selectedOption = 0;
		activeScreen = 0;
		// Set default player factions
		playerFactions = new int[3];
		playerFactions[0] = 1;
		playerFactions[1] = 2;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.getContentPane().setMaximumSize(windowSize);
		this.getContentPane().setMinimumSize(windowSize);
		this.getContentPane().setPreferredSize(windowSize);
		this.getContentPane().setSize(windowSize);
		pack();
		addKeyListener(this);
		this.setLocationRelativeTo(null);
		this.setIconImage(ImageRes.getWinIcon());
		setVisible(true);
		run();
	}


	@Override
	/**
	 * Enables directional movement, or triggers an action.
	 */
	public void keyPressed(KeyEvent event)
	{
		int keyNum = event.getKeyCode();
		// End the game.
		if(map != null && keyNum == actionKey && map.getWinner() != null)
		{
			bar = null;
			view = null;
			map = null;
		}
		// Save directional movement
		if (keyNum == upKey && !upPressed) {
			upPressed = true;
		} 
		else if (keyNum == leftKey && !leftPressed) {
			leftPressed = true;
		} 
		else if (keyNum == rightKey && !rightPressed) {
			rightPressed = true;
		} 
		else if (keyNum == downKey && !downPressed) {
			downPressed = true;
		}
		if(map != null) {
			if(map.activeMap() == null)
			{
				// Do an action on the overworld
				if (keyNum == confirmKey && !confirmPressed) {
					map.confirmedAction();
					confirmPressed = true;
				} 
				else if (keyNum == cancelKey && !cancelPressed) {
					map.cancelledAction();
					cancelPressed = true;
				} 
				else if (keyNum == actionKey && !actionPressed) {
					map.advancePhase();
					actionPressed = true;
				}
				// Do an action on the selected region
			} 
			else {
				if (keyNum == confirmKey && !confirmPressed) {
					map.activeMap().confirmedAction();
					confirmPressed = true;
				} 
				else if (keyNum == cancelKey && !cancelPressed) {
					map.activeMap().cancelledAction();
					cancelPressed = true;
				} 
				else if (keyNum == actionKey && !actionPressed) {
					map.activeMap().actionTriggered();
					actionPressed = true;
				}
			}
		} 
		else {
			//Title screen
			if(activeScreen == 0) // Start game
			{
				if(keyNum == confirmKey)
				{
					newGame();
				}
				// Option screen
			} 
			else if(activeScreen == 2)
			{
				if(keyNum == confirmKey && !confirmPressed)
				{
					// Changes player factions
					playerFactions[selectedOption] = (playerFactions[selectedOption] + 1) % 4;
					// Don't disable player one or two
					if(playerFactions[selectedOption] == 0 && selectedOption != 2)
						playerFactions[selectedOption]++;
					confirmPressed = true;
				}// Change the player we are changing the faction of
				else if(keyNum == actionKey && !actionPressed)
				{
					selectedOption = (selectedOption+1)%3;
					actionPressed = true;
				}
			}
		}
	}

	@Override
	/**
	 * Disables movement in the released key's direction.
	 */
	public void keyReleased(KeyEvent event)
	{
		int keyNum = event.getKeyCode();
		//Disable movement
		if (keyNum == upKey) {
			upPressed = false;
		} 
		else if (keyNum == leftKey) {
			leftPressed = false;
		} 
		else if (keyNum == rightKey) {
			rightPressed = false;
		}
		else if (keyNum == downKey) {
			downPressed = false;
		}
		else if (keyNum == confirmKey) {
			confirmPressed = false;
		} 
		else if (keyNum == cancelKey) {
			cancelPressed = false;
		} 
		else if (keyNum == actionKey) {
			actionPressed = false;
		}
	}

	@Override
	/**
	 * Not used.
	 */
	public void keyTyped(KeyEvent event)
	{
	}

	/**
	 * Start a new game.
	 */
	public void newGame()
	{
		map = new World(3, viewSize, playerFactions);
		view = new WorldView(map, viewSize, this);
		bar = new Sidebar(map, barSize);
	}

	/**
	 * Paints the screen.
	 * @param g the graphics to draw on.
	 */
	public void paintComponent(Graphics g)
	{
		BufferedImage image = new BufferedImage(windowSize.width,
				windowSize.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = (Graphics2D) (image.getGraphics());
		// Draw the game
		if (map != null && view != null && bar != null) {
			// Draw screen elements
			g2D.drawImage(view.drawWorld(), 0, 0, this);
			g2D.drawImage(bar.drawBar(), 750, 0, this);
			// Draw game pause overlay if paused
			// g2D.drawImage(pauseOverlay, 0, 0, this);
		}
		else {
			// Draw the selected screen
			g2D.drawImage(ImageRes.startScreen(activeScreen), 0, 0, null);
			// Draw options
			if(activeScreen == ImageRes.getScreenCount()-1)
			{
				// Display the chosen factions, and the selection rectangle to denote which faction is being changed
				g2D.setColor(Color.WHITE);
				g2D.drawString(factionNames[playerFactions[0]], 450, 220);
				g2D.drawString(factionNames[playerFactions[1]], 450, 290);
				g2D.drawString(factionNames[playerFactions[2]], 450, 360);
				g2D.drawRect(445, 208+70*selectedOption, 100, 20);
			}
		}
		g.drawImage(image, 0, 0, this);
	}

	/**
	 * Process directional actions.
	 */
	public void processActions()
	{
		if (map != null) {
			// Handle movement commands for the main world
			if (map.activeMap() == null) {
				if (upPressed) {
					map.moveSelectVertical(false);
				}
				else if (downPressed) {
					map.moveSelectVertical(true);
				}
				if (leftPressed) {
					map.moveSelectHorizontal(false);
				}
				else if (rightPressed) {
					map.moveSelectHorizontal(true);
				}
			}
			else {
				// Handle commands for the selected region, if any
				if (upPressed) {
					map.activeMap().moveSelectVertical(false);
				}
				else if (downPressed) {
					map.activeMap().moveSelectVertical(true);
				}
				if (leftPressed) {
					map.activeMap().moveSelectHorizontal(false);
				}
				else if (rightPressed) {
					map.activeMap().moveSelectHorizontal(true);
				}
			}
		} else {
			// Cycle screen backward
			if(upPressed || leftPressed)
			{
				activeScreen--;
				if(activeScreen < 0) activeScreen += ImageRes.getScreenCount();
			}
			// Cycle screen forward
			else if(downPressed || rightPressed)
			{
				activeScreen++;
				if(activeScreen >= ImageRes.getScreenCount()) activeScreen -= ImageRes.getScreenCount();
			}
		}
	}

	/**
	 * Sets all key pressed variables to false.
	 */
	private void releaseAllKeys()
	{
		upPressed = downPressed = rightPressed = leftPressed = false;
	}

	/**
	 * Paint loop.
	 */
	@Override
	public void run()
	{
		boolean running = true;
		int steps = 0;
		while (running) {
			// Prevent the java set resizable bug that changes insets by drawing to the content pane.
			paintComponent(this.getContentPane().getGraphics());
			if(!hasFocus()) // Release keys
				releaseAllKeys();
			if(steps % 2 == 0) this.processActions();
			steps++;
			if(steps > 300000) steps -= 300000;
			try {
				Thread.sleep(50);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the keys to the default values.
	 */
	public void setDefaultKeys()
	{
		upKey = KeyEvent.VK_UP;
		downKey = KeyEvent.VK_DOWN;
		leftKey = KeyEvent.VK_LEFT;
		rightKey = KeyEvent.VK_RIGHT;
		confirmKey = KeyEvent.VK_Z;
		cancelKey = KeyEvent.VK_X;
		actionKey = KeyEvent.VK_C;
	}
}
