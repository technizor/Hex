package game.gui;

import game.faction.Faction;
import game.faction.Resource;
import game.world.BlockTile;
import game.world.Building;
import game.world.Region;
import game.world.Unit;
import game.world.World;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Renders information pertaining to the game state.
 * 
 * @author Sherman Ying
 * @version January 21, 2013
 * @since 1.7
 */
public class Sidebar
{
	private Dimension barSize;
	private World map; // The map containing the world data

	/**
	 * Creates a new sidebar with the given data location.
	 * 
	 * @param map
	 *            the world to check.
	 * @param barSize
	 *            the size of the side bar.
	 */
	public Sidebar(World map, Dimension barSize) {
		this.map = map;
		this.barSize = barSize;
	}

	/**
	 * Renders an image of the sidebar.
	 * 
	 * @return the rendered image.
	 */
	public BufferedImage drawBar()
	{
		BufferedImage image = new BufferedImage(barSize.width, barSize.height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = (Graphics2D) image.getGraphics();
		Region activeMap = map.activeMap();
		Faction currentPlayer = map.activePlayer();
		if (activeMap != null && !map.isBuildPhase())
			currentPlayer = map.activeMap().getTurnPlayer();
		g2D.drawImage(ImageRes.sideBar(currentPlayer.getFactionType()), 0, 0,
				null);
		if (activeMap != null) {
			HexMenu hexMenu = activeMap.getHexMenu();
			drawString(g2D, activeMap.getName(), 30, 38);
			int[] unitList = activeMap.getUnitStorage();
			drawUnitStorage(g2D, unitList);
			if (hexMenu != null) {
				if (hexMenu.isBuildMenu())
					drawString(g2D, "Select Building to create.", 35, 132);
				else if (map.isBuildPhase())
					drawString(g2D, "Create a unit.", 35, 132);
				else
					drawString(g2D, "Deploy a unit.", 35, 132);
				BlockTile tile = hexMenu.getSelectedTile();
				Unit unit = hexMenu.getSelectedUnit();
				if (tile.isBuilding()) {
					drawString(g2D, ((Building) tile).getFullDescription(),
							100, 348);
					g2D.drawImage(hexMenu.drawSelectedTile(), 30, 330, null);
					for (int type = 0; type < 4; type++)
						drawString(
								g2D,
								"Cost: "
										+ ((Building) tile)
												.getResourceCost(type), 130,
								185 + 34 * type);
					drawString(g2D, ((Building) tile).getInfoString(), 35, 4150);
				} else if (unit != null) {
					drawString(g2D, unit.getFullDescription(), 100, 348);
					g2D.drawImage(hexMenu.drawSelectedTile(), 30, 330, null);
					for (int type = 0; type < 4; type++)
						drawString(g2D, "Cost: " + unit.getResourceCost(type),
								130, 185 + 34 * type);
					drawString(g2D, unit.getInfoString(), 35, 415);
				}
			} else {
				if (map.isBuildPhase())
					drawString(g2D, "Select a tile to interact with.", 35, 132);
				else if (activeMap.getWinner() == null) {
					boolean buildingSelected = activeMap.buildingSelected();
					boolean unitSelected = activeMap.unitSelected();
					boolean moveSelected = activeMap.moveSelected();
					if (!buildingSelected && !unitSelected) {
						if (currentPlayer == activeMap.getTurnPlayer())
							drawString(g2D, "Select a unit to command.", 35,
									132);
						else
							drawString(g2D, "Select a unit or building.", 35,
									132);
					} else if (buildingSelected) {
						drawString(g2D, "Select a target location.", 35, 132);
					} else {
						if (!moveSelected)
							drawString(g2D, "Select a move location.", 35, 132);
						else
							drawString(g2D, "Select an attack target.", 35, 132);
					}
				} else {
					drawString(g2D, activeMap.getWinner().getPlayerName()
							+ " has won!", 35, 132);
				}
				BlockTile tile = activeMap.getSelectedTile();
				Unit unit = activeMap.getSelectedUnit();
				if (tile.isBuilding()) {
					drawString(g2D, ((Building) tile).getFullDescription(),
							100, 348);
					g2D.drawImage(activeMap.drawSelectedTile(), 30, 330, null);
					for (int type = 0; type < 4; type++)
						g2D.drawString(
								"Cost: "
										+ ((Building) tile)
												.getResourceCost(type), 130,
								185 + 34 * type);
					drawString(g2D, ((Building) tile).getInfoString(), 35, 415);
				} else if (unit != null) {
					drawString(g2D, unit.getFullDescription(), 100, 348);
					g2D.drawImage(activeMap.drawSelectedTile(), 30, 330, null);
					for (int type = 0; type < 4; type++)
						g2D.drawString("Cost: " + unit.getResourceCost(type),
								130, 185 + 34 * type);
					drawString(g2D, unit.getInfoString(), 35, 415);
				}
			}
		} else {
			Region region = map.getSelectedTile().getRegion();
			g2D.drawString(map.getName(), 30, 38);
			if (map.getWinner() != null)
				drawString(g2D, map.getWinner() + " has won the game!", 35, 132);
			if (map.isBuildPhase())
				drawString(g2D, "Select a region you control.", 35, 132);
			else
				drawString(g2D, "Select a region to capture.", 35, 132);
			g2D.drawImage(map.drawSelectedTile(), 30, 348, null);
			String regionInfo = region.getName();
			if (region.getController() != null) {
				regionInfo += "\n" + region.getController().getPlayerName()
						+ "\n" + region.getController().getFactionName();
			}
			drawString(g2D, regionInfo, 106, 346);

			// Draw unit storage for this region if it is controlled
			if (region.getController() != null) {
				int[] unitList = region.getUnitStorage();
				drawUnitStorage(g2D, unitList);
			}
		}
		g2D.drawString(currentPlayer.getFullName(), 35, 92);
		Resource[] res = currentPlayer.getResources();
		for (int type = 0; type < 4; type++)
			drawString(g2D, "" + res[type].getCurrentResource(), 35,
					185 + 34 * type);
		if (map.isBuildPhase())
			drawString(g2D, "Build Phase", 128, 38);
		else
			drawString(g2D, "Combat Phase", 128, 38);
		return image;
	}

	/**
	 * Draws a string with multiple lines.
	 * 
	 * @param g2D
	 *            the graphics to draw on.
	 * @param str
	 *            the string to draw.
	 * @param textX
	 *            the text x position.
	 * @param textY
	 *            the text y position.
	 */
	public void drawString(Graphics2D g2D, String str, int textX, int textY)
	{
		for (String line : str.split("\n")) {
			g2D.drawString(line, textX, textY);
			textY += g2D.getFontMetrics().getHeight();
		}
	}

	/**
	 * Draws the list of units stored.
	 * 
	 * @param g2D
	 *            the graphics to draw on.
	 * @param unitList
	 *            the lsit of units stored.
	 */
	private void drawUnitStorage(Graphics2D g2D, int[] unitList)
	{
		g2D.drawImage(ImageRes.unitList(), 26, 464, null);
		for (int faction = 0; faction < 3; faction++) {
			for (int unit = 0; unit < 8; unit++) {
				drawString(g2D, "" + unitList[faction * 8 + unit],
						66 + 66 * faction, 474 + 14 * unit);
			}
		}
	}
}
