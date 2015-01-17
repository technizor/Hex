package game.faction;

import game.world.Building;
import game.world.Region;

/**
 * The Obeswarm faction, the faction tends to rush the enemy.
 * 
 * @author Sherman Ying and Kewei Zhou
 * @version January 21, 2013
 * @since 1.7
 */
public class Swarm extends Faction
{
	public static final int[] initialResource = { 750, 750, 600, 900 };
	public static final int[] maxResource = { 15000, 15000, 10000, 20000 };
	public static final String NAME = "Obeswarm";
	public static final int[] resourceRate = { 250, 250, 200, 300 };
	public static final int FACTION_NUM = 2;

	/**
	 * Creates a player of the Obeswarm faction.
	 * 
	 * @param playerName
	 *            the player name
	 */
	public Swarm(String playerName) {
		super(playerName, NAME, FACTION_NUM, initialResource,
				maxResource, resourceRate);
	}

	@Override
	/**
	 * Gives a new building of this faction at the specified location.
	 */
	public Building newBuilding(Region region, int buildingType, int buildX,
			int buildY)
	{
		return new Building(region, BuildingData.getData(FACTION_NUM, buildingType), buildX, buildY);
	}
}
