package game.world;

import game.faction.Human;
import game.faction.MechStorm;
import game.faction.Swarm;
import game.gui.ImageRes;

import java.awt.Image;

/**
 * The generic Tile for both world and region maps.
 * 
 * @author Sherman Ying
 * @version January 21, 2012
 * @since 1.7
 */
public class Tile
{
	/**
	 * Draw Height of a tile.
	 */
	public static final int DRAW_HEIGHT = 32;
	/**
	 * Angled side horizontal draw offset.
	 */
	public static final int DRAW_SIDE_OFFSET = 15;

	/**
	 * Draw width of a tile.
	 */
	public static final int DRAW_WIDTH = 52;

	/**
	 * Calculates the tile distance between two tiles on a hexagonal grid
	 * 
	 * @param startX
	 *            the starting tile x position
	 * @param startY
	 *            the starting tile y position
	 * @param endX
	 *            the ending tile x position
	 * @param endY
	 *            the ending tile y position
	 * @return the distance, in tiles, between the two positions
	 */
	public static int getDistance(int startX, int startY, int endX, int endY)
	{
		// Change the origin to the starting point to get the offset
		int xDiff = startX - endX;
		int yDiff = startY - endY;

		// Determine the signs of the offset
		boolean xPositive = xDiff >= 0;
		boolean yPositive = yDiff >= 0;

		// Absolute value of offsets
		if (!xPositive)
			xDiff *= -1;
		if (!yPositive)
			yDiff *= -1;

		// If both have the same sign, return the highest offset
		if (xPositive == yPositive)
			if (xDiff >= yDiff)
				return xDiff;
			else
				return yDiff;
		// Otherwise, return manhattan distance
		return xDiff + yDiff;
	}

	private boolean isInvisible; // Whether this is an invisible tile
	private boolean isBuilding; // Whether this is a building.
	private Map map; // The map containing this tile.

	private int xPos, yPos; // Tile position.

	/**
	 * Creates a generic tile.
	 * 
	 * @param map
	 *            the map containing this tile.
	 * @param tileX
	 *            the X co-ordinate of this tile.
	 * @param tileY
	 *            the Y co-ordinate of this tile.
	 */
	public Tile(Map map, int tileX, int tileY) {
		setX(tileX);
		setY(tileY);
		this.map = map;
	}

	/**
	 * Creates a generic tile, or a placeholder tile.
	 * 
	 * @param map
	 *            the map containing this tile.
	 * @param tileX
	 *            the X co-ordinate of this tile.
	 * @param tileY
	 *            the Y co-ordinate of this tile.
	 * @param empty
	 *            whether this tile is an empty placeholder tile.
	 */
	public Tile(Map map, int tileX, int tileY, boolean empty) {
		setX(tileX);
		setY(tileY);
		isInvisible = true;
		this.map = map;
	}

	/**
	 * The tile draws itself and sends this to the map.
	 * 
	 * @return the tile display.
	 */
	public Image draw(int factionType, boolean isSelected)
	{
		if (isInvisible)
			return null;
		if (factionType != Human.FACTION_NUM
				&& factionType != Swarm.FACTION_NUM
				&& factionType != MechStorm.FACTION_NUM)
			return ImageRes.getTile(0, !isSelected);
		return ImageRes.getTile(factionType, !isSelected);
	}

	/**
	 * Calculates the distance between this tile and another position
	 * 
	 * @param xPos
	 *            the x position of the other tile
	 * @param yPos
	 *            the y position of the other tile
	 * @return the distance between this tile and the given position
	 */
	public int getDistanceTo(int xPos, int yPos)
	{
		return getDistance(this.xPos, this.yPos, xPos, yPos);
	}

	/**
	 * Gives the map of this tile.
	 * 
	 * @return the map of this tile.
	 */
	public Map getMap()
	{
		return map;
	}

	/**
	 * Gives the type of this map.
	 * 
	 * @return the type of the map of this tile.
	 */
	public int getType()
	{
		return map.getType();
	}

	/**
	 * Gives the X position of this tile.
	 * 
	 * @return the X position.
	 */
	public int getXPos()
	{
		return xPos;
	}

	/**
	 * Gives the Y position of this tile.
	 * 
	 * @return the Y position.
	 */
	public int getYPos()
	{
		return yPos;
	}

	public boolean isBuilding()
	{
		return isBuilding;
	}

	/**
	 * Tells whether this tile is a placeholder tile.
	 * 
	 * @return whether this tile is a placeholder.
	 */
	public boolean isInvisible()
	{
		return isInvisible;
	}

	/**
	 * Sets whether this is a building tile.
	 * 
	 * @param isBuilding
	 *            whether this tile represents a building.
	 */
	public void setIsBuilding(boolean isBuilding)
	{
		this.isBuilding = isBuilding;
	}

	/**
	 * Sets the X position of this tile. This is mainly for swapping tiles.
	 * 
	 * @param xPos
	 *            the new X position of this tile.
	 */
	public void setX(int xPos)
	{
		this.xPos = xPos;
	}

	/**
	 * Sets the Y position of this tile. This is mainly for swapping tiles.
	 * 
	 * @param yPos
	 *            the new Y position of this tile.
	 */
	public void setY(int yPos)
	{
		this.yPos = yPos;
	}
}
