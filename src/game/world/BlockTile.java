package game.world;

/**
 * A generic tile contained in a region.
 * 
 * @author Sherman Ying
 * @version January 21, 2012
 * @since 1.7
 */
public class BlockTile extends Tile
{
	/**
	 * Constructs a normal visible tile.
	 * 
	 * @param map
	 *            the containing map.
	 * @param tileX
	 *            the tile x position.
	 * @param tileY
	 *            the tile y position.
	 */
	public BlockTile(Region map, int tileX, int tileY) {
		super(map, tileX, tileY);
	}

	/**
	 * Constructs an invisible tile for spacing purposes.
	 * 
	 * @param map
	 *            the containing map.
	 * @param tileX
	 *            the tile x position.
	 * @param tileY
	 *            the tile y position.
	 * @param isPlaceholder
	 *            whether this is invisible.
	 */
	public BlockTile(Region map, int tileX, int tileY, boolean isPlaceholder) {
		super(map, tileX, tileY, isPlaceholder);
	}
}
