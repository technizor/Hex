package game.gui;

import game.faction.Human;
import game.faction.MechStorm;
import game.faction.Swarm;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageRes
{
	// File location strings
	private static final String buildingDirectory = "hex_images/build/";
	private static final String guiDirectory = "hex_images/gui/";
	private static final String tileDirectory = "hex_images/tile/";
	private static final String unitDirectory = "hex_images/units/";
	private static final String sidebar = "sidebar.png";
	private static final String screen = "screen";
	private static final String[] tileColours = { "gray", "red", "green",
			"blue" };
	private static final String icon = "winIcon.png";
	private static final String target = "target.png";
	private static final String move = "move.png";
	private static final String attack = "attack.png";
	private static final String unit = "unit_list.png";

	// Images loaded at the start up of the game window.
	private static final Image winIcon = getIcon();
	private static final Image targetImg = getTarget();
	private static final Image unitList = getUnitList();
	private static final Image[][] humanBuild = getBuildings(Human.FACTION_NUM);
	private static final Image[][] humanUnits = getUnits(Human.FACTION_NUM);
	private static final Image[][] mechBuild = getBuildings(MechStorm.FACTION_NUM);
	private static final Image[][] mechUnits = getUnits(MechStorm.FACTION_NUM);
	private static final Image[][] swarmBuild = getBuildings(Swarm.FACTION_NUM);
	private static final Image[][] swarmUnits = getUnits(Swarm.FACTION_NUM);

	private static final Image[] sideBars = getSideBars();
	private static final Image[][] tileBases = getTileBases();
	private static final Image[] startScreens = { getStartScreen(0),
			getStartScreen(1), getStartScreen(2) ,getStartScreen(3) ,getStartScreen(4) ,getStartScreen(5) ,getStartScreen(6) ,
			getStartScreen(7),  getStartScreen(8)};
	private static final Image movePoint = getMovePoint();
	private static final Image attackPoint = getAttackPoint();

	/**
	 * Gives the loaded attack icon.
	 * 
	 * @return the attack icon.
	 */
	public static Image attackPoint()
	{
		return attackPoint;
	}

	/**
	 * Loads all the building sprites for the given faction.
	 * 
	 * @param factionNum
	 *            the faction.
	 * @return the building sprites.
	 */
	private static Image[][] getBuildings(int factionNum)
	{
		String begin = buildingDirectory;
		if (factionNum == Human.FACTION_NUM)
			begin += "human_";
		else if (factionNum == Swarm.FACTION_NUM)
			begin += "swarm_";
		else if (factionNum == MechStorm.FACTION_NUM)
			begin += "mech_";
		Image[][] buildingSprites = new Image[2][8];
		for (int buildType = 0; buildType < 8; buildType++) {
			try {
				buildingSprites[0][buildType] = ImageIO.read(new File(begin + buildType + ".png"));
			} catch (IOException ioe) {
			}
		}
		for (int buildType = 0; buildType < 8; buildType++) {
			try {
				buildingSprites[1][buildType] = ImageIO.read(new File(begin
						+ buildType + "_trans.png"));
			} catch (IOException ioe) {
			}
		}
		return buildingSprites;
	}

	/**
	 * Loads all the unit sprites for the given faction.
	 * 
	 * @param factionNum
	 *            the faction.
	 * @return the unit sprites.
	 */
	private static Image[][] getUnits(int factionNum)
	{
		String begin = unitDirectory;
		if (factionNum == Human.FACTION_NUM)
			begin += "human_";
		else if (factionNum == Swarm.FACTION_NUM)
			begin += "swarm_";
		else if (factionNum == MechStorm.FACTION_NUM)
			begin += "mech_";
		Image[][] unitSprites = new Image[2][8];
		for (int unitType = 0; unitType < 8; unitType++) {
			try {
				unitSprites[0][unitType] = ImageIO.read(new File(begin
						+ unitType + ".png"));
			} catch (IOException ioe) {
			}
		}
		for (int unitType = 0; unitType < 8; unitType++) {
			try {
				unitSprites[1][unitType] = ImageIO.read(new File(begin
						+ unitType + "_trans.png"));
			} catch (IOException ioe) {
			}
		}
		return unitSprites;
	}

	/**
	 * Loads the attack icon.
	 * 
	 * @return the attack icon.
	 */
	private static Image getAttackPoint()
	{
		try {
			return ImageIO.read(new File(guiDirectory + attack));
		} catch (IOException ioe) {
		}
		return null;
	}

	/**
	 * Gives the human unit sprites
	 * 
	 * @param type
	 *            the type of unit
	 * @param transparent
	 *            whether the sprite is transparent
	 * @return the unit sprite
	 */
	public static Image getHumanBuild(int type, boolean transparent)
	{
		return humanBuild[transparent ? 1 : 0][type];
	}

	/**
	 * Gives the human unit sprites
	 * 
	 * @param type
	 *            the type of unit
	 * @param transparent
	 *            whether the sprite is transparent
	 * @return the unit sprite
	 */
	public static Image getHumanUnit(int type, boolean transparent)
	{
		return humanUnits[transparent ? 1 : 0][type];
	}

	/**
	 * Loads the window icon.
	 * 
	 * @return the window icon.
	 */
	private static Image getIcon()
	{
		try {
			return ImageIO.read(new File(guiDirectory + icon));
		} catch (IOException ioe) {
		}
		return null;
	}

	/**
	 * Gives the mech unit sprites
	 * 
	 * @param type
	 *            the type of unit
	 * @param transparent
	 *            whether the sprite is transparent
	 * @return the unit sprite
	 */
	public static Image getMechBuild(int type, boolean transparent)
	{
		return mechBuild[transparent ? 1 : 0][type];
	}

	/**
	 * Gives the mech unit sprites
	 * 
	 * @param type
	 *            the type of unit
	 * @param transparent
	 *            whether the sprite is transparent
	 * @return the unit sprite
	 */
	public static Image getMechUnit(int type, boolean transparent)
	{
		return mechUnits[transparent ? 1 : 0][type];
	}

	/**
	 * Loads the move icon.
	 * 
	 * @return the move icon.
	 */
	private static Image getMovePoint()
	{
		try {
			return ImageIO.read(new File(guiDirectory + move));
		} catch (IOException ioe) {
		}
		return null;
	}

	/**
	 * Gives the sidebar background
	 * 
	 * @return the sidebar image
	 */
	private static Image[] getSideBars()
	{
		Image[] bar = new Image[3];
		try {
			bar[0] = ImageIO.read(new File(guiDirectory + "human_"
					+ sidebar));
		} catch (IOException ioe) {
		}
		try {
			bar[1] = ImageIO.read(new File(guiDirectory + "swarm_"
					+ sidebar));
		} catch (IOException ioe) {
		}
		try {
			bar[2] = ImageIO.read(new File(guiDirectory + "mech_"
					+ sidebar));
		} catch (IOException ioe) {
		}
		return bar;
	}

	/**
	 * Loads the specified start screen.
	 * 
	 * @param screenNum
	 *            the order of the screen.
	 * @return the screen.
	 */
	private static Image getStartScreen(int screenNum)
	{
		try {
			return ImageIO.read(new File(guiDirectory + screen + screenNum
					+ ".png"));
		} catch (IOException ioe) {
		}
		return null;
	}

	/**
	 * Gives the swarm unit sprites
	 * 
	 * @param type
	 *            the type of unit
	 * @param transparent
	 *            whether the sprite is transparent
	 * @return the unit sprite
	 */
	public static Image getSwarmBuild(int type, boolean transparent)
	{
		return swarmBuild[transparent ? 1 : 0][type];
	}

	/**
	 * Gives the swarm unit sprites
	 * 
	 * @param type
	 *            the type of unit
	 * @param transparent
	 *            whether the sprite is transparent
	 * @return the unit sprite
	 */
	public static Image getSwarmUnit(int type, boolean transparent)
	{
		return swarmUnits[transparent ? 1 : 0][type];
	}

	/**
	 * Gives the targeting sprite
	 * 
	 * @return the target sprite
	 */
	private static Image getTarget()
	{
		try {
			return ImageIO.read(new File(tileDirectory + target));
		} catch (IOException ioe) {
		}
		return null;
	}

	/**
	 * Gives the tile base for the specified faction.
	 * 
	 * @param faction
	 *            the faction.
	 * @param transparent
	 *            whether it is transparent.
	 * @return the tile base.
	 */
	public static Image getTile(int faction, boolean transparent)
	{
		return tileBases[transparent ? 1 : 0][faction];
	}

	/**
	 * Gives the tile base
	 * 
	 * @param factionType
	 *            player id, 0-4, 0 being neutral
	 * @param transparent
	 *            whether the transparent tile base is needed
	 * @return the tile base image
	 */
	private static Image[][] getTileBases()
	{
		Image[][] tileBases = new Image[2][4];
		for (int type = 0; type < 4; type++) {
			try {
				tileBases[0][type] = ImageIO.read(new File(tileDirectory
						+ "tile_" + tileColours[type] + ".png"));
			} catch (IOException ioe) {
			}
		}
		for (int type = 0; type < 4; type++) {
			try {
				tileBases[1][type] = ImageIO.read(new File(tileDirectory
						+ "tile_" + tileColours[type] + "_trans.png"));
			} catch (IOException ioe) {
				
			}
		}
		return tileBases;
	}

	/**
	 * Loads the unit list image.
	 * 
	 * @return the unit list.
	 */
	private static Image getUnitList()
	{
		try {
			return ImageIO.read(new File(guiDirectory + unit));
		} catch (IOException ioe) {
		}
		return null;
	}

	/**
	 * Gives the window icon.
	 * 
	 * @return the window icon.
	 */
	public static Image getWinIcon()
	{
		return winIcon;
	}

	/**
	 * Gives the move icon.
	 * 
	 * @return the move icon.
	 */
	public static Image movePoint()
	{
		return movePoint;
	}

	/**
	 * Gives the side bar for the proper faction.
	 * 
	 * @param factionType
	 *            the faction.
	 * @return the proper sidebar.
	 */
	public static Image sideBar(int factionType)
	{
		if (factionType == Human.FACTION_NUM)
			return sideBars[0];
		if (factionType == Swarm.FACTION_NUM)
			return sideBars[1];
		if (factionType == MechStorm.FACTION_NUM)
			return sideBars[2];
		return null;
	}

	/**
	 * Gives the specified start screen.
	 * 
	 * @param screenNum
	 *            the number of the screen.
	 * @return the screen.
	 */
	public static Image startScreen(int screenNum)
	{
		if (screenNum >= 0 && screenNum < startScreens.length)
			return startScreens[screenNum];
		return null;
	}

	/**
	 * Gives the targeting icon.
	 * 
	 * @return the target icon.
	 */
	public static Image target()
	{
		return targetImg;
	}

	/**
	 * Gives the unit list image.
	 * 
	 * @return the unit list.
	 */
	public static Image unitList()
	{
		return unitList;
	}

	/**
	 * Gives the number of start screens loaded.
	 * 
	 * @return the number of screens.
	 */
	public static int getScreenCount()
	{
		return startScreens.length;
	}
}
