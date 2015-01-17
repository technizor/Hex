package game.world;

import game.faction.Faction;

/**
 * A tile that represents a region on the map.
 * 
 * @author Sherman Ying
 * @version January 21, 2012
 * @since 1.7
 */
public class RegionTile extends Tile
{
	private Region region; // The actual region.

	/**
	 * Creates a tile that represents a region.
	 * 
	 * @param map
	 *            the world containing this tile and region.
	 * @param tileX
	 *            the region x position on the map.
	 * @param tileY
	 *            the region y position on the map.
	 */
	public RegionTile(World map, int tileX, int tileY) {
		super(map, tileX, tileY);
		String name = "(" + tileX + "," + tileY + ")";
		region = new Region(name, map, map.getViewSize());
	}

	/**
	 * Creates a placeholder tile that is used to fill the array of tiles.
	 * 
	 * @param map
	 *            the world map.
	 * @param tileX
	 *            region x position.
	 * @param tileY
	 *            region y position.
	 * @param isPlaceholder
	 *            whether this is a placeholder.
	 */
	public RegionTile(World map, int tileX, int tileY, boolean isPlaceholder) {
		super(map, tileX, tileY, isPlaceholder);
		if (!isPlaceholder) {
			String name = "(" + tileX + "," + tileY + ")";
			region = new Region(name, map, map.getViewSize());
		}
	}

	/**
	 * Gives the controlling player
	 * 
	 * @return the controlling player
	 */
	public Faction getController()
	{
		return region.getController();
	}

	/**
	 * Gives the region that this tile represents.
	 * 
	 * @return the region of this tile.
	 */
	public Region getRegion()
	{
		return region;
	}

	/**
	 * Sets the controlling player of this tile and region.
	 */
	public void setController(Faction player)
	{
		region.setController(player);
	}
}
