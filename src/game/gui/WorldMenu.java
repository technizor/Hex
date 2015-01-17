package game.gui;

import game.faction.Faction;
import game.world.BlockTile;
import game.world.Building;
import game.world.Region;
import game.world.Tile;
import game.world.Unit;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * A visual menu for deploying units into combat.
 * 
 * @author Sherman Ying
 * @version January 21, 2013
 * @since 1.7
 */
public class WorldMenu
{
	private Image background;
	private Faction controller;
	private int drawHeight;
	private int width;
	private int height;
	private Region regionMap;
	private int selectX;
	private int selectY;
	private int size;
	private BlockTile[][] options;
	private int[] unitConstraints;
	private Unit[][] units;

	/**
	 * Creates a deployment menu.
	 * 
	 * @param attackingRegion
	 *            the attacking region to take units from.
	 * @param alreadyDeployed
	 *            the units already chosen to be deployed.
	 * @param screenSize
	 *            the size of the screen.
	 * @param background
	 *            the background of the overworld.
	 * @param lastOption
	 *            the last selected option on this menu.
	 */
	public WorldMenu(Region attackingRegion, int[] alreadyDeployed,
			Dimension screenSize, Image background, int lastOption) {
		regionMap = attackingRegion;
		width = screenSize.width;
		height = screenSize.height;
		this.background = background;
		controller = regionMap.getController();
		size = 5;
		selectX = lastOption / size;
		selectY = lastOption % size;
		drawHeight = Tile.DRAW_HEIGHT * size;
		unitConstraints = new int[24];
		options = new BlockTile[size][size];
		units = new Unit[size][size];
		int[] unitsAvailable = attackingRegion.getUnitStorage();
		for (int unitType = 0; unitType < 24; unitType++)
			unitConstraints[unitType] = unitsAvailable[unitType]
					- alreadyDeployed[unitType];
		newDeployMenu();
	}

	/**
	 * Checks whether you can deploy another of this type of unit.
	 * 
	 * @param factionType
	 *            the faction type of the unit.
	 * @param unitType
	 *            the unit type.
	 * @return whether you can deploy another
	 */
	public boolean canAddUnit(int factionType, int unitType)
	{
		if (unitConstraints[factionType * 8 + unitType] > 0)
			return true;
		return false;
	}

	/**
	 * Gives a rendered menu.
	 * 
	 * @return the entire menu image.
	 */
	public Image draw()
	{
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = (Graphics2D) (image.getGraphics());
		// Draw a new background
		g2D.drawImage(background, 0, 0, null);

		// This is the offset between the end of a tile and the beginning of
		// another tile
		// that is one draw row above or below
		int tileSideOffset = Tile.DRAW_WIDTH - Tile.DRAW_SIDE_OFFSET;
		// Create initial draw positions that assume a uniform size
		int drawX = (width - Tile.DRAW_WIDTH) / 2;
		int drawY = (height - drawHeight) / 2;

		// When drawing isometric hexagonal grids, z (or draw row) is the sum of
		// x and y.
		for (int z = 0; z < size * 2; z++) {
			int tileDrawX = drawX;
			int tileX = z;
			int tileY = 0;

			// Draw every row
			while (tileY <= z) {
				// Do not go out of Array bounds
				if (tileX >= 0 && tileX < size && tileY >= 0 && tileY < size) {
					// Only draw tiles that exist
					if (!options[tileX][tileY].isInvisible()) {
						Image tileImg = drawTile(tileX, tileY);
						int extraHeight = tileImg.getHeight(null)
								- Tile.DRAW_HEIGHT;
						int extraWidth = (tileImg.getWidth(null) - Tile.DRAW_WIDTH) / 2;
						g2D.drawImage(tileImg, tileDrawX - extraWidth, drawY
								- extraHeight, null);
					}
				}
				// Shift the draw x, leaving a space
				tileDrawX += tileSideOffset * 2;
				tileX--;
				tileY++;
			}
			// Shift the draw positions for the next row
			drawX -= tileSideOffset;
			drawY += Tile.DRAW_HEIGHT / 2;
		}
		// Send the image to the screen
		return image;
	}

	/**
	 * Gives an image of the selected tile.
	 * 
	 * @return an image of the selected tile.
	 */
	public Image drawSelectedTile()
	{
		return drawTile(selectX, selectY);
	}

	/**
	 * Gives an image of the tile.
	 * 
	 * @param tileX
	 *            the tile x position
	 * @param tileY
	 *            the tile y position
	 * @return the image of the tile.
	 */
	public Image drawTile(int tileX, int tileY)
	{ // Find proper draw position
		Image image = null;
		Image img;
		boolean isSelected = selectX == tileX && selectY == tileY;
		if (options[tileX][tileY].isBuilding()) {
			Building build = (Building) options[tileX][tileY];
			image = build.drawBuilding(isSelected);
		} else if (units[tileX][tileY] != null) {
			Unit unit = units[tileX][tileY];
			if (unit.isPlaceholder()) {
				image = units[tileX][tileY].draw(true);
			} else if (isSelected)
				image = units[tileX][tileY].draw(true);
			else
				image = units[tileX][tileY].draw(false);
		}
		if (image != null) {
			img = new BufferedImage(image.getWidth(null), image.getHeight(null)
					+ Tile.DRAW_HEIGHT / 4, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2D = (Graphics2D) img.getGraphics();
			g2D.drawImage(options[tileX][tileY].draw(
					controller.getFactionType(), isSelected), (img
					.getWidth(null) - Tile.DRAW_WIDTH) / 2, img.getHeight(null)
					- Tile.DRAW_HEIGHT, null);
			g2D.drawImage(image, 0, 0, null);
		} else {
			img = options[tileX][tileY].draw(controller.getFactionType(),
					isSelected);
		}
		return img;
	}

	/**
	 * Gives the selected option.
	 * 
	 * @return the selected option.
	 */
	public int getHexOption()
	{
		return size * selectX + selectY;
	}

	/**
	 * Gives the selected tile.
	 * 
	 * @return the selected tile.
	 */
	public BlockTile getSelectedTile()
	{
		return options[selectX][selectY];
	}

	/**
	 * Gives the selected unit.
	 * 
	 * @return the selected unit.
	 */
	public Unit getSelectedUnit()
	{
		return units[selectX][selectY];
	}

	/**
	 * Changes the selection y position by increasing or decreasing by 1.
	 * 
	 * @param increase
	 *            whether to increase the y position.
	 */
	public void moveSelectHorizontal(boolean right)
	{
		// Find the new positions
		int newX = selectX;
		int newY = selectY;
		// Calculate the co-ordinate difference to determine which horizontal
		// tile to select
		int coordDiff = selectX - selectY;
		if (coordDiff < 0)
			coordDiff *= -1;
		if (right) {
			if (coordDiff % 2 == 1)
				newX--;
			else
				newY++;
		} else {
			if (coordDiff % 2 == 0)
				newX++;
			else
				newY--;
		}
		// Offset the selection if there is a space beside it
		if (newX < 0 || newX >= options.length || newY < 0
				|| newY >= options.length || options[newX][newY].isInvisible()) {
			if (right) {
				if (newY < size) {
					newX++;
					newY++;
				} else {
					newX--;
					newY--;
				}
			} else {
				if (newX < size) {
					newX++;
					newY++;
				} else {
					newX--;
					newY--;
				}
			}
			// Cannot move the selection
			if (newX < 0 || newX >= options.length || newY < 0
					|| newY >= options.length
					|| options[newX][newY].isInvisible())
				return;
		}
		// Change the selection
		selectX = newX;
		selectY = newY;
	}

	/**
	 * Changes the selection x position by increasing or decreasing by 1.
	 * 
	 * @param increase
	 *            whether to increase the x position.
	 */
	public void moveSelectVertical(boolean down)
	{
		// Find the new positions
		int newX = selectX;
		int newY = selectY;
		if (down) {
			newX++;
			newY++;
		} else {
			newX--;
			newY--;
		}
		// Offset the selection if there is a space beside it
		if (newX < 0 || newX >= options.length || newY < 0
				|| newY >= options.length || options[newX][newY].isInvisible()) {
			if (newX > newY)
				if (down)
					newX--;
				else
					newY++;
			else if (newY > newX)
				if (down)
					newY--;
				else
					newX++;
			// Cannot move the selection
			if (newX < 0 || newX >= options.length || newY < 0
					|| newY >= options.length
					|| options[newX][newY].isInvisible())
				return;
		}
		// Change the selection
		selectX = newX;
		selectY = newY;
	}

	/**
	 * Generates a grid of deployable units for display.
	 */
	private void newDeployMenu()
	{
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				options[i][j] = new BlockTile(null, i, j);
				int option = i * size + j;
				if (i * size + j < 24 && canAddUnit(option / 8, option % 8))
					units[i][j] = controller.newUnit(option / 8 + 1,
							option % 8, i, j);
			}
	}
}
