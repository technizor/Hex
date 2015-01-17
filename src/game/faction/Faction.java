package game.faction;

import game.world.Building;
import game.world.Region;
import game.world.Unit;

/**
 * The players are represented by Factions of specific types.
 * @author Sherman Ying and Kewei Zhou
 * @version January 21, 2013
 * @since 1.7
 */
public abstract class Faction
{
	private String factionName;
	private int factionType;
	private int[] maxUnits;
	private int territories;

	private String playerName;
	private Resource[] resources;

	/**
	 * Constructs a Faction of the given type, with the given player name and resource count.
	 * @param playerName the name of the player.
	 * @param factionName the name of the faction.
	 * @param factionType the number of the faction.
	 * @param initialResource the initial resource supply.
	 * @param maxResource the max resource supply.
	 * @param resourceRate the per base per turn resource rate.
	 */
	public Faction(String playerName, String factionName, int factionType, int[] initialResource, int[] maxResource,
			int[] resourceRate) {
		this.playerName = playerName;
		this.factionName = factionName;
		this.factionType = factionType;
		maxUnits = new int[8];
		for (int type = 0; type < 8; type++) {
			maxUnits[type] = newUnit(type, 0, 0).maxCount();
		}
		resources = new Resource[4];
		for (int type = 0; type < 4; type++) {
			resources[type] = new Resource(initialResource[type], maxResource[type],
					resourceRate[type]);
		}
	}

	/**
	 * Adds resources based on the number of producers.
	 * 
	 * @param producers
	 *            the number of times to add the resources.
	 */
	public void addResources(int producers)
	{
		for (int type = 0; type < 4; type++)
			this.resources[type].addResources(producers);
	}

	public void gainedTerritory()
	{
		territories++;
	}

	public String getFactionName()
	{
		return factionName;
	}

	public int getFactionType()
	{
		return factionType;
	}

	public String getFullName()
	{
		return playerName + " (" + factionName + ")";
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public Resource[] getResources()
	{
		return resources;
	}

	public int territoryCount()
	{
		return territories;
	}

	public void lostTerritory()
	{
		territories--;
	}

	public Building newBuilding(int buildingType, int buildX, int buildY)
	{
		return newBuilding(null, buildingType, buildX, buildY);
	}

	public abstract Building newBuilding(Region region, int buildingType,
			int buildX, int buildY);

	public Unit newUnit(Faction controller, int unitType, int unitX, int unitY)
	{
		return newUnit(controller, this.factionType, unitType, unitX, unitY);
	}
	
	public Unit newUnit(Faction controller, int factionType, int unitType,
			int unitX, int unitY)
	{
		return new Unit(controller, UnitData.getData(factionType, unitType), unitX, unitY);
	}
	public Unit newUnit(int unitType, int unitX, int unitY)
	{
		return newUnit(this, unitType, unitX, unitY);
	}

	public Unit newUnit(int factionType, int unitType, int unitX, int unitY)
	{
		return newUnit(this, factionType, unitType, unitX, unitY);
	}
}
