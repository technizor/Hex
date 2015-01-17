package game.faction;

import game.world.Building;
import game.world.Region;

/**
 * The mechstorm faction, a faction heavily focus on defense, until it can
 * bring out its most powerful units.
 * 
 * @author Sherman Ying and Kewei Zhou
 * @version January 21, 2013
 * @since 1.7
 */
public class MechStorm extends Faction
{
	public static final int[] initialResource = { 900, 900, 900, 300 };
	public static final int[] maxResource = { 20000, 20000, 10000, 10000 };
	public static final int FACTION_NUM = 3;
	public static final String NAME = "MechStorm";
	public static final int[] resourceRate = { 300, 300, 300, 100 };

	/**
	 * Creates a player of the MechStorm faction
	 * 
	 * @param playerName
	 *            the player's name
	 */
	public MechStorm(String playerName) {
		super(playerName, NAME, FACTION_NUM, initialResource, maxResource,
				resourceRate);
	}

	@Override
	/**
	 * Gives a new building of this faction at the specified location.
	 */
	public Building newBuilding(Region region, int buildingType, int buildX,
			int buildY)
	{
		return new Building(region, BuildingData.getData(FACTION_NUM,
				buildingType), buildX, buildY);
	}
}
