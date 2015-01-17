package game.gui;

import game.world.World;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * Renders an image of the current world view.
 * @author Sherman Ying
 * @version January 21, 2013
 * @since 1.7
 */
public class WorldView
{
	private Image lastFrameDrawn; // Stores the last frame drawn
	private World map; // The map containing the world data
	private JFrame parent; // The containing window of this screen
	private Dimension viewSize; // Screen size

	/**
	 * Creates a world viewer.
	 * @param map the map to view.
	 * @param viewSize the size of the view.
	 * @param parent the containing game window.
	 */
	public WorldView(World map, Dimension viewSize, JFrame parent) {
		this.map = map;
		this.parent = parent;
		this.viewSize = viewSize;
	}

	/**
	 * Draws an image of the world.
	 * @return the world view.
	 */
	public BufferedImage drawWorld()
	{
		BufferedImage image = new BufferedImage(viewSize.width,
				viewSize.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = (Graphics2D) image.getGraphics();
		// Don't redraw the map unless the game is in focus
		if (parent.hasFocus())
			if (map.activeMap() == null) {
				WorldMenu deployMenu = map.getDeployMenu();
				if (deployMenu != null)
					lastFrameDrawn = deployMenu.draw();
				else lastFrameDrawn = map.draw();
			}
			else {
				HexMenu hexMenu = map.activeMap().getHexMenu();
				if (hexMenu != null)
					lastFrameDrawn = hexMenu.draw();
				else
					lastFrameDrawn = map.activeMap().draw();
			}
		g2D.drawImage(lastFrameDrawn, 0, 0, null);
		return image;
	}
}
