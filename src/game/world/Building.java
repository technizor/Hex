package game.world;

import game.faction.BuildingData;
import game.faction.Human;
import game.faction.MechStorm;
import game.faction.Swarm;
import game.gui.ImageRes;

import java.awt.Image;

/**
 * A generic building.
 * 
 * @author Kewei Zhou and Sherman Ying
 * @version January 2012
 * @since 1.7
 */
public class Building extends BlockTile
{
	private int buildingType; // Building type
	private int factionType; // Faction it is built from
	private boolean action; // Indicates whether it did its action for this turn
	private String name; // Name of the building
	private String description; // The witty description.
	private String fullDesc; // The name and description.
	private int maxCount; // Maximum buildings buildable in one region.
	private int[] resourceCost; // Cost to build.

	// Defensive
	private int armour; // Damage reduction is higher than on units.
	private int currentHealth; // Current health.
	private int maxHealth; // Maximum health.
	private int regenPerTurn; // Health regeneration per combat turn.

	// Offensive
	private int aoeRadius; // The building deals damage to the tiles surrounding
	private int attackRange; // For defensive buildings
	private boolean canAttackFlying; // The building can attack flying units
	private boolean canAttackGrounded; // The building can attack grounded units
	private int damage; // Only defensive buildings do damage
	private int reachDist; // The building deals damage in a line, up to the

	/**
	 * Creates a new building with the given data.
	 * 
	 * @param map
	 *            the map this is on.
	 * @param data
	 *            the building information.
	 * @param tileX
	 *            the tile x position.
	 * @param tileY
	 *            the tile y position.
	 */
	public Building(Region map, BuildingData data, int tileX, int tileY) {
		super(map, tileY, tileX);
		setIsBuilding(true);
		action = false;
		// Save data
		name = data.getName();
		description = data.getDescription();
		fullDesc = name + "\n" + description;
		factionType = data.getFactionType();
		buildingType = data.getBuildingType();
		maxHealth = currentHealth = data.getMaxHealth();
		armour = data.getArmour();
		damage = data.getDamage();
		attackRange = data.getActionRange();
		regenPerTurn = data.getRegenPerTurn();
		canAttackGrounded = data.canAttackGrounded();
		canAttackFlying = data.canAttackFlying();
		reachDist = data.getLinearDistance();
		aoeRadius = data.getAoeRadius();
		maxCount = data.maxCount();
		resourceCost = new int[4];
		for (int type = 0; type < 4; type++) {
			resourceCost[type] = data.getResourceCost(type);
		}
	}

	/**
	 * Tells whether this can hit flying units.
	 * 
	 * @return whether this can attack flying units.
	 */
	public boolean canAttackFlying()
	{
		return canAttackFlying;
	}

	/**
	 * Tells whether this can hit ground units.
	 * 
	 * @return whether this can attack ground units.
	 */
	public boolean canAttackGrounded()
	{
		return canAttackGrounded;
	}

	/**
	 * Damages a unit, taking damage reduction (armour) into account
	 * 
	 * @param pureDamage
	 *            the full damage that would be taken if armour was at zero
	 * @return the amount of damage that would be taken after reduction
	 */
	public int damageBuilding(int pureDamage)
	{
		if (pureDamage == 0)
			return 0;
		int actualDamage = pureDamage - armour;
		// Minimum damage is 1
		if (actualDamage < 1)
			actualDamage = 1;
		currentHealth -= actualDamage;
		// Make sure the health doesn't go below zero
		if (currentHealth < 0)
			currentHealth = 0;
		// Return the damage that would have been taken after subtracting the
		// armour
		// This is so that players can easily calculate how much damage they
		// would have done
		return actualDamage;
	}

	/**
	 * Tells whether this building attacked this turn.
	 * 
	 * @return whether this attacked.
	 */
	public boolean didAction()
	{
		return action;
	}

	/**
	 * Gives the sprite of the image, with or without transparency.
	 * 
	 * @param opaque
	 *            whether this image is opaque.
	 * @return the sprite of the image.
	 */
	public Image drawBuilding(boolean opaque)
	{
		if (factionType == Human.FACTION_NUM)
			return ImageRes.getHumanBuild(buildingType, !opaque);
		if (factionType == MechStorm.FACTION_NUM)
			return ImageRes.getMechBuild(buildingType, !opaque);
		if (factionType == Swarm.FACTION_NUM)
			return ImageRes.getSwarmBuild(buildingType, !opaque);
		return null;
	}

	/**
	 * Gives the AOE radius of the attack.
	 * 
	 * @return the AOE radius of the attack.
	 */
	public int getAoeRadius()
	{
		return aoeRadius;
	}

	/**
	 * Gives the amount of damage reduction.
	 * 
	 * @return the amount of damage reduction.
	 */
	public int getArmour()

	{
		return armour;
	}

	/**
	 * Gives the action range of this building.
	 * 
	 * @return the action range.
	 */
	public int getAttackRange()
	{
		return attackRange;
	}

	/**
	 * Gives the building type.
	 * 
	 * @return the type of building.
	 */
	public int getBuildingType()
	{
		return buildingType;
	}

	/**
	 * Gives the current health of this building.
	 * 
	 * @return the current health.
	 */
	public int getCurrentHealth()
	{
		return currentHealth;
	}

	/**
	 * Gives the amount of damage this building can do.
	 * 
	 * @return the amount of damage.
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * Gives the witty descriptors.
	 * 
	 * @return the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Gives the type of faction that made this.
	 * 
	 * @return the faction type.
	 */
	public int getFactionType()
	{
		return factionType;
	}

	/**
	 * Gives the name and the description.
	 * 
	 * @return the full description.
	 */
	public String getFullDescription()
	{
		return fullDesc;
	}

	/**
	 * Gives an informative text detailing the building stats.
	 * 
	 * @return the information.
	 */
	public String getInfoString()
	{
		String output = "HP -" + getCurrentHealth() + "/" + getMaxHealth();
		if (getDamage() >= 0)
			output += "    DMG-" + getDamage();
		else
			output += "    HEAL" + (getDamage() * -1);
		output += "\nRED-" + getArmour() + "    RNG-" + getAttackRange()
				+ "    AOE-" + getAoeRadius() + "\nRCH-" + getReach()
				+ "    FLY-" + (canAttackFlying() ? "Y" : "N") + "    GRD-"
				+ (canAttackGrounded() ? "Y" : "N");
		return output;
	}

	/**
	 * Gives the maximum number of these buildings you can have.
	 * 
	 * @return the max limit.
	 */
	public int getMaxCount()
	{
		return maxCount;
	}

	/**
	 * Gives the original health.
	 * 
	 * @return the maximum possible health.
	 */
	public int getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * Gives the display name.
	 * 
	 * @return the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gives the reaching distance of this building's attacks.
	 * 
	 * @return the reach distance.
	 */
	public int getReach()
	{
		return reachDist;
	}

	/**
	 * Gives the health regeneration per combat turn.
	 * 
	 * @return the health regeneration.
	 */
	public int getRegenPerTurn()
	{
		return regenPerTurn;
	}

	/**
	 * Gives the resource cost for a particular resource.
	 * 
	 * @param type
	 *            the type of resource.
	 * @return the cost of making this building.
	 */
	public int getResourceCost(int type)
	{
		return resourceCost[type];
	}

	/**
	 * Tells whether this deploys units for defense.
	 * 
	 * @return whether this deployed defense units.
	 */
	public boolean isBunker()
	{
		return buildingType == 6;
	}

	/**
	 * Tells whether this produces units.
	 * 
	 * @return whether this makes units.
	 */
	public boolean isFactory()
	{
		return buildingType == 2;
	}

	/**
	 * Tells whether this does damage with its actions.
	 * 
	 * @return whether this does damage.
	 */
	public boolean isTower()
	{
		return damage != 0;
	}

	/**
	 * Repairs a building for the given amount and gives how much health was
	 * restored
	 * 
	 * @param repairAmount
	 *            the amount of damage to repair for
	 * @return the actual amount of health repaired
	 */
	public int repairBuilding(int repairAmount)
	{
		int oldHealth = currentHealth;
		int newHealth = oldHealth + repairAmount;
		// Cannot increase health above maximum
		if (newHealth > maxHealth)
			currentHealth = maxHealth;
		else
			currentHealth = newHealth;
		// Return real amount repaired
		return currentHealth - oldHealth;
	}

	/**
	 * Sets whether this building fired a shot, if it was a tower.
	 * 
	 * @param didAction
	 *            whether it shot.
	 */
	public void setAction(boolean didAction)
	{
		action = didAction;
	}
} // BuildingCreator class
