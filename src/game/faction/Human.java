package game.faction;

import game.world.Building;
import game.world.Region;

/**
 * The human faction, a balanced faction.
 * 
 * @author Sherman Ying and Kewei Zhou
 * @version January 21, 2013
 * @since 1.7
 */
public class Human extends Faction
{
	public static final int FACTION_NUM = 1;
	public static final int[] initialResource = { 750, 750, 750, 750 };
	public static final int[] maxResource = { 15000, 15000, 15000, 15000 };
	public static final String NAME = "Human";
	public static final int[] resourceRate = { 250, 250, 250, 250 };

	/**
	 * Creates a player of the human faction.
	 * 
	 * @param playerName
	 *            the player name
	 */
	public Human(String playerName) {
		super(playerName, NAME, FACTION_NUM, initialResource,
				maxResource, resourceRate);
	}

	@Override
	public Building newBuilding(Region region, int buildingType, int buildX,
			int buildY)
	{
		return new Building(region, BuildingData.getData(FACTION_NUM, buildingType), buildX, buildY);
	}
}
