package game.world;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * The generic hexagonal map for the game. Must be used as either a World map or
 * Regional map.
 * 
 * @author Sherman Ying
 * @version January 13, 2013
 * @since 1.7
 */
public abstract class Map
{
	/**
	 * Regional map type.
	 */
	public static final int REGION_MAP = 1337;
	/**
	 * World map type.
	 */
	public static final int WORLD_MAP = 9001;

	// Draw properties
	private Image background; // Background image is randomly generated
	private int drawHeight; // Height of the map display when drawing only tiles
	private Dimension viewSize; // The dimensions of the view to draw to

	// Properties of the map
	private String name; // The display name of the map
	private int sideLength; // Tile length of any given side
	private int diameter; // Tile length through the middle of the map
	private int type; // The type of map.
	private Tile[][] tiles; // Map tile set
	private int selectX, selectY; // Selection positions

	/**
	 * The generic constructor for any type of hexagonal map.
	 * 
	 * @param mapName
	 *            the display name of the map.
	 * @param mapType
	 *            Whether this is a world or regional map.
	 * @param mapSize
	 *            The shortest side length. The diameter is one less than double
	 *            the side length.
	 * @param viewSize
	 *            the size of the display for this map.
	 */
	public Map(String mapName, int mapType, int mapSize, Dimension viewSize) {
		name = mapName;
		sideLength = mapSize;
		type = mapType;
		diameter = sideLength * 2 - 1;

		drawHeight = (diameter * 2) * (Tile.DRAW_HEIGHT / 2);
		this.viewSize = viewSize;
		background = drawBackground();

		selectX = selectY = mapSize;
		createTiles();
	}

	/**
	 * Cancel the current action.
	 */
	public abstract void cancelledAction();

	/**
	 * Confirm an action.
	 */
	public abstract void confirmedAction();

	/**
	 * Creates the hexagonal map by filling in empty tiles around the edges and
	 * blank tiles in the centre. The map is 2 tiles larger than the diameter of
	 * the usable tiles for simpler co-ordinate reference.
	 */
	private void createTiles()
	{
		// Make the square of tiles.
		tiles = new Tile[diameter + 2][diameter + 2];
		int sideLength = getSize();
		// Initialize the tiles as either invisible or visible tiles.
		for (int row = 0; row < tiles.length; row++) {
			// Only the inner rows have tiles, not the edges.
			if (row > 0 && row <= diameter) {
				// Create the blank tiles.
				for (int col = 1; col < tiles[row].length - 1; col++)
					tiles[row][col] = newTile(row, col, false);
				tiles[row][0] = new Tile(this, row, 0, true);
				tiles[row][diameter + 1] = newTile(row, diameter + 1, true);

				// Make the map hexagonal by making some tiles from the ends
				// empty
				if (row <= sideLength) {
					for (int col = 0; col < sideLength - row; col++)
						tiles[row][diameter - col] = newTile(row, diameter
								- col, true);
				} else if (row > sideLength) {
					for (int col = 0; col <= -1 * (sideLength - row); col++)
						tiles[row][col] = newTile(row, col, true);
				}
			} else // First and last rows are empty
			{
				for (int col = 0; col < tiles[row].length; col++)
					tiles[row][col] = newTile(row, col, true);
			}
		}
	}

	/**
	 * The map draws the tiles in the proper order and displays this.
	 * 
	 * @return the displayed image of the map.
	 */
	public Image draw()
	{
		BufferedImage image = new BufferedImage(viewSize.width,
				viewSize.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = (Graphics2D) (image.getGraphics());
		// Draw a new background
		g2D.drawImage(background, 0, 0, null);

		// This is the offset between the end of a tile and the beginning of
		// another tile that is one draw row above or below
		int tileSideOffset = Tile.DRAW_WIDTH - Tile.DRAW_SIDE_OFFSET;
		// Create initial draw positions that assume a uniform size
		int drawX = (viewSize.width - Tile.DRAW_WIDTH) / 2;
		int drawY = (viewSize.height - drawHeight) / 2;

		// When drawing isometric hexagonal grids, z (or draw row) is the sum of
		// x and y.
		for (int z = 1; z <= diameter * 2; z++) {
			int tileDrawX = drawX;
			int tileX = z;
			int tileY = 1;

			// Draw every row
			while (tileY <= z) {
				// Do not go out of Array bounds
				if (tileX >= 1 && tileX <= diameter && tileY >= 1
						&& tileY <= diameter) {
					// Only draw tiles that exist
					if (!tiles[tileX][tileY].isInvisible()) {
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
	 * Creates a black background with randomly generated stars.
	 */
	private BufferedImage drawBackground()
	{
		// Create a black background.
		BufferedImage background = new BufferedImage(viewSize.width,
				viewSize.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = (Graphics2D) background.getGraphics();
		g2D.setColor(Color.BLACK);
		g2D.fillRect(0, 0, viewSize.width, viewSize.height);

		// Generate and draw the stars
		g2D.setColor(Color.WHITE);
		for (int i = (int) (viewSize.width * 1.5); i > 0; i--) {
			int x = (int) (viewSize.width * Math.random());
			int y = (int) (viewSize.height * Math.random());
			int s = (int) (3 * Math.random());
			g2D.fillOval(x - s / 2, y - s / 2, s, s);
		}
		return background;
	}

	/**
	 * Gives the Image of the selected tile, for use in the side bar display.
	 * 
	 * @return the selected tile image
	 */
	public Image drawSelectedTile()
	{
		return drawTile(selectX, selectY);
	}

	/**
	 * Draws the tile in a specific manner.
	 * 
	 * @param tileX
	 *            the tile x to draw.
	 * @param tileY
	 *            the tile y to draw.
	 * @return the tile image.
	 */
	public abstract Image drawTile(int tileX, int tileY);

	/**
	 * Shares the generated background.
	 * 
	 * @return the generated background.
	 */
	public Image getBackground()
	{
		return background;
	}

	/**
	 * Gives the longest tile distance across the map, or the diameter.
	 * 
	 * @return the diameter of the map.
	 */
	public int getDiameter()
	{
		return diameter;
	}

	/**
	 * Gives the display name of the map.
	 * 
	 * @return the display name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gives the tile at the selection position.
	 * 
	 * @return the tile selected.
	 */
	public Tile getSelectedTile()
	{
		return tiles[selectX][selectY];
	}

	/**
	 * Gives the selection x position.
	 * 
	 * @return the selection x.
	 */
	public int getSelectX()
	{
		return selectX;
	}

	/**
	 * Gives the selection y position.
	 * 
	 * @return the selection y.
	 */
	public int getSelectY()
	{
		return selectY;
	}

	/**
	 * Gives the shortest tile distance on the side of the map, or the side
	 * length.
	 * 
	 * @return the side length of the map.
	 */
	public int getSize()
	{
		return sideLength;
	}

	/**
	 * Gives the specified tile.
	 * 
	 * @param x
	 *            the X co-ordinate of the tile.
	 * @param y
	 *            the Y co-ordinate of the tile.
	 * @return the tile at the specified location.
	 */
	public Tile getTile(int x, int y)
	{
		return tiles[x][y];
	}

	/**
	 * Gives all of the tiles of the map.
	 * 
	 * @return the tiles of the map.
	 */
	public Tile[][] getTiles()
	{
		return tiles;
	}

	/**
	 * Gives the type of map.
	 * 
	 * @return the type of map.
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * Gives the view size dimensions.
	 * 
	 * @return the view size.
	 */
	public Dimension getViewSize()
	{
		return viewSize;
	}

	/**
	 * Changes the selection on the screen horizontally by incrementing or
	 * decrementing x or y.
	 * 
	 * @param right
	 *            whether to move the selection right.
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
		// Offset the selection if there is a space
		// beside it when over the edge.
		if (newX < 0 || newX >= tiles.length || newY < 0
				|| newY >= tiles.length || tiles[newX][newY].isInvisible()) {
			if (right) {
				if (newY <= sideLength) {
					newX++;
					newY++;
				} else {
					newX--;
					newY--;
				}
			} else {
				if (newX <= sideLength) {
					newX++;
					newY++;
				} else {
					newX--;
					newY--;
				}
			}
			// Cannot move the selection
			if (newX < 0 || newX >= tiles.length || newY < 0
					|| newY >= tiles.length || tiles[newX][newY].isInvisible())
				return;
		}
		// Change the selection
		selectX = newX;
		selectY = newY;
	}

	/**
	 * Changes the selection on the screen vertically by incrementing or
	 * decrementing x and y.
	 * 
	 * @param down
	 *            whether to move the selection down.
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
		// Offset the selection if there is a space
		// beside it when over the edge.
		if (newX < 0 || newX >= tiles.length || newY < 0
				|| newY >= tiles.length || tiles[newX][newY].isInvisible()) {
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
			if (newX < 0 || newX >= tiles.length || newY < 0
					|| newY >= tiles.length || tiles[newX][newY].isInvisible())
				return;
		}
		// Change the selection
		selectX = newX;
		selectY = newY;
	}

	/**
	 * Creates the proper type of tile for the type of map.
	 * 
	 * @param tileX
	 *            the tile x position.
	 * @param tileY
	 *            the tile y position.
	 * @param isPlaceholder
	 *            whether this is an invisible tile.
	 * @return the new tile, with the proper type.
	 */
	public abstract Tile newTile(int tileX, int tileY, boolean isPlaceholder);

	/**
	 * Change the given tile into a new tile.
	 * 
	 * @param tile
	 *            the new tile.
	 * @param tileX
	 *            the original tile x position.
	 * @param tileY
	 *            the original tile y position.
	 */
	public void setTile(Tile tile, int tileX, int tileY)
	{
		tiles[tileX][tileY] = tile;
	}
}
