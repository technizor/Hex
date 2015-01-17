package game.world;

import game.faction.Faction;
import game.faction.Human;
import game.faction.MechStorm;
import game.faction.Swarm;
import game.faction.UnitData;
import game.gui.ImageRes;

import java.awt.Image;

/**
 * An attack unit in the game HEX. Created by giving it UnitData that determine
 * the properties and performance of the unit.
 * 
 * @author Sherman Ying and Kewei Zhou
 * @version January 21, 2013
 * @since 1.7
 */
public class Unit
{
	// Unit properties
	private Faction controller; // The player controlling this unit
	private int factionType;
	private int unitType;
	private boolean isPlaceholder;
	private int maxCount; // Maximum possible units stored on a factory
	private String name; // Display name of the unit
	private String description;
	private String fullDesc;
	private int[] resourceCost; // Resource cost for this unit

	// Defense
	private int armour; // Damage migitation
	private boolean canFly; // If this unit can fly, it cannot be attacked by
	private int currentHealth; // Tracks the amount of health remaining
	private int maxHealth; // Max possible health

	// Offense
	private int aoeRad; // If this unit deals aoe damage, this deals damage
	private int attackRange; // Attack Range
	private boolean canAttackBuildings; // If this unit can attack buildings
	private boolean canAttackFlying; // If this unit can attack flying units
	private boolean canAttackUnits; // If this unit can attack units
	private int damage; // Base damage
	private int moveRange; // Movement range
	private int selfDamage; // If this unit damages or heals itself during its
	private int reachDist; // If this unit deals damage in a line (1 of 6)

	// Action variables
	private int nextX, nextY;
	private int xPos, yPos;
	private boolean action;

	/**
	 * Creates a unit with the given properties
	 * 
	 * @param player
	 *            the controller of the unit
	 * @param maxHealth
	 *            the maximum possible health
	 * @param damage
	 *            the damage stat of the unit
	 * @param armour
	 *            the amount of damage migitation
	 * @param attackBuilding
	 *            whether this can attack a building
	 * @param attackUnit
	 *            whether this can attack a unit
	 * @param range
	 *            the distance this unit can attack
	 * @param mobility
	 *            the distance this unit can move
	 * @param flying
	 *            whether this unit can fly
	 */
	public Unit(Faction player, UnitData data, int xPos, int yPos) {
		controller = player;
		// Copy data from the Unit data
		name = data.getName();
		description = data.getDescription();
		unitType = data.getUnitType();
		fullDesc = name + "\n" + description;
		factionType = data.getFactionType();
		maxHealth = data.getMaxHealth();
		armour = data.getArmour();
		damage = data.getDamage();
		attackRange = data.getAttackRange();
		moveRange = data.getMoveRange();
		canAttackBuildings = data.canAttackBuildings();
		canAttackUnits = data.canAttackUnits();
		canFly = data.canFly();
		canAttackFlying = data.canAttackFlying();
		reachDist = data.getLinearDistance();
		aoeRad = data.getAoeRadius();
		selfDamage = data.selfDamage();
		maxCount = data.maxCount();
		resourceCost = new int[4];
		for (int type = 0; type < 4; type++) {
			resourceCost[type] = data.getResourceCost(type);
		}
		// Set current properties
		currentHealth = maxHealth;
		this.xPos = nextX = xPos;
		this.yPos = nextY = yPos;
	}

	/**
	 * Makes a placeholder unit for claiming a movement location. Stores the
	 * original unit's location.
	 * 
	 * @param origX
	 *            the original x position
	 * @param origY
	 *            the original y position
	 */
	public Unit(Unit origUnit) {
		xPos = origUnit.getXPos();
		yPos = origUnit.getYPos();
		isPlaceholder = true;
		controller = origUnit.getController();
		resourceCost = new int[4];
		fullDesc = origUnit.getFullDescription();
		maxHealth = origUnit.getMaxHealth();
		currentHealth = origUnit.getCurrentHealth();
		armour = origUnit.getArmour();
		damage = origUnit.getDamage();
		attackRange = origUnit.getAttackRange();
		moveRange = origUnit.getMoveRange();
		canAttackBuildings = origUnit.canAttackBuildings();
		canAttackUnits = origUnit.canAttackUnits();
		canFly = origUnit.canFly();
		canAttackFlying = origUnit.canAttackFlying();
		reachDist = origUnit.getReach();
		aoeRad = origUnit.getAoeRadius();
		for (int type = 0; type < 4; type++) {
			resourceCost[type] = origUnit.getResourceCost(type);
		}
	}

	/**
	 * Tells whether this unit can attack buildings.
	 * 
	 * @return whether this unit can attack buildings.
	 */
	public boolean canAttackBuildings()
	{
		return canAttackBuildings;
	}

	/**
	 * Tells whether this unit can attack flying units.
	 * 
	 * @return whether this unit can attack flying units.
	 */
	public boolean canAttackFlying()
	{
		return canAttackFlying;
	}

	/**
	 * Tells whether this unit can attack ground units.
	 * 
	 * @return whether this unit can attack ground units.
	 */
	public boolean canAttackUnits()
	{
		return canAttackUnits;
	}

	/**
	 * Resets the unit properties due to cancelling the move.
	 */
	public void cancel()
	{
		nextX = xPos;
		nextY = yPos;
		action = false;
	}

	/**
	 * Tells whether this unit is flying.
	 * 
	 * @return whether this unit is a flying unit.
	 */
	public boolean canFly()
	{
		return canFly;
	}

	/**
	 * Damages a unit, taking damage reduction (armour) into account
	 * 
	 * @param pureDamage
	 *            the full damage that would be taken if armour was at zero
	 * @return the amount of damage that would be taken after reduction
	 */
	public int damageUnit(int pureDamage)
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
	 * Tells whether this unit has been given a command this turn.
	 * 
	 * @return whether this did its action.
	 */
	public boolean didAction()
	{
		return action;
	}

	/**
	 * Gives the unit image.
	 * 
	 * @param isSelected
	 *            whether this unit is selected.
	 * @return the unit image.
	 */
	public Image draw(boolean isSelected)
	{
		if (factionType == Human.FACTION_NUM)
			return ImageRes.getHumanUnit(unitType, !isSelected);
		if (factionType == MechStorm.FACTION_NUM)
			return ImageRes.getMechUnit(unitType, !isSelected);
		if (factionType == Swarm.FACTION_NUM)
			return ImageRes.getSwarmUnit(unitType, !isSelected);
		return null;
	}

	/**
	 * Gives the aoe radius.
	 * 
	 * @return the aoe radius of the attack.
	 */
	public int getAoeRadius()
	{
		return aoeRad;
	}

	/**
	 * Finds the amount of armour a unit or building has
	 * 
	 * @return the unit or building's armour value
	 */
	public int getArmour()
	{
		return armour;
	}

	/**
	 * Finds the unit or building's range of attack
	 * 
	 * @return the unit or building's range of attack
	 */
	public int getAttackRange()
	{
		return attackRange;
	}

	/**
	 * Finds the unit or building's owner
	 * 
	 * @return the unit or building's owner
	 */
	public Faction getController()
	{
		return controller;
	}

	/**
	 * Finds the unit or building's current health
	 * 
	 * @return the unit or building's current health
	 */
	public int getCurrentHealth()
	{
		return currentHealth;
	}

	/**
	 * Finds out the amount of damage a unit or building can deal
	 * 
	 * @return the unit or building's ability to attack
	 */
	public int getDamage()
	{
		return damage;
	}

	/**
	 * Gives the witty descriptors of the unit.
	 * 
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Gives the type of faction this unit originates from.
	 * 
	 * @return the faction type.
	 */
	public int getFactionType()
	{
		return factionType;
	}

	/**
	 * Gives the full description of the unit, the name and its description.
	 * 
	 * @return the full description.
	 */
	public String getFullDescription()
	{
		return fullDesc;
	}

	/**
	 * Constructs text giving details about the unit stats.
	 * 
	 * @return the information.
	 */
	public String getInfoString()
	{
		String targetStr = "";
		if (canAttackBuildings())
			targetStr += "B";
		if (canAttackUnits())
			targetStr += "U";
		if (canAttackFlying())
			targetStr += "F";
		if (targetStr.equals("BUF"))
			targetStr = "ALL";
		String output = "HP -" + getCurrentHealth() + "/" + getMaxHealth();
		if (getDamage() >= 0)
			output += "    DMG-" + getDamage();
		else
			output += "    HEAL" + (getDamage() * -1);
		output += "    RED-" + getArmour() + "\nMOV-" + getMoveRange()
				+ "    RNG-" + getAttackRange() + "    AOE-" + getAoeRadius()
				+ "\nRCH-" + getReach() + "    FLY-" + (canFly() ? "Y" : "N")
				+ "    TGT-" + targetStr;
		return output;
	}

	/**
	 * Finds the unit or building's maximum health
	 * 
	 * @return the unit or building's maximum health
	 */
	public int getMaxHealth()
	{
		return maxHealth;
	}

	public int getMoveRange()
	{
		return moveRange;
	}

	/**
	 * Gives the name of the unit, for display purposes.
	 * 
	 * @return the name of the unit
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gives the next move x position.
	 * 
	 * @return the next x position.
	 */
	public int getNextX()
	{
		return nextX;
	}

	/**
	 * Gives the next move y position.
	 * 
	 * @return the next y position.
	 */
	public int getNextY()
	{
		return nextY;
	}

	/**
	 * Gives the reach property
	 * 
	 * @return the reach distance.
	 */
	public int getReach()
	{
		return reachDist;
	}

	public int getResourceCost(int type)
	{
		return resourceCost[type];
	}

	/**
	 * Gives the damage done to itself after an attack.
	 * 
	 * @return the damage done to itself.
	 */
	public int getSelfDamage()
	{
		return selfDamage;
	}

	public int getUnitType()
	{
		return unitType;
	}

	/**
	 * Gives the current x position.
	 * 
	 * @return the current x position.
	 */
	public int getXPos()
	{
		return xPos;
	}

	/**
	 * Gives the current y position.
	 * 
	 * @return the current y position.
	 */
	public int getYPos()
	{
		return yPos;
	}

	/**
	 * Heals a unit for the given amount and gives how much health was restored
	 * 
	 * @param repairAmount
	 *            the amount of damage to repair for
	 * @return the actual amount of health repaired
	 */
	public int healUnit(int healAmount)
	{
		int oldHealth = currentHealth;
		int newHealth = oldHealth + healAmount;
		// Cannot increase health above maximum
		if (newHealth > maxHealth)
			currentHealth = maxHealth;
		else
			currentHealth = newHealth;
		// Return real amount repaired
		return currentHealth - oldHealth;
	}

	/**
	 * Tells whether this is a placeholder to claim movement locations.
	 * 
	 * @return whether this is a placeholder for another unit.
	 */
	public boolean isPlaceholder()
	{
		return isPlaceholder;
	}

	/**
	 * Gives the max storage count of this unit class.
	 * 
	 * @return the maximum number of units of this type.
	 */
	public int maxCount()
	{
		return maxCount;
	}

	/**
	 * Resets the unit's command properties so it can attack on its next turn.
	 */
	public void nextTurn()
	{
		xPos = nextX;
		yPos = nextY;
	}

	/**
	 * Sets whether this unit already was given a command.
	 * 
	 * @param didAction
	 *            whether this unit did a command.
	 */
	public void setAction(boolean didAction)
	{
		action = didAction;
	}

	/**
	 * Sets the move target, if possible, and returns whether this was
	 * successful.
	 * 
	 * @param nextX
	 *            the movement x position of the unit.
	 * @param nextY
	 *            the movement y position of the unit.
	 * @return whether the unit is able to move.
	 */
	public boolean setNext(int nextX, int nextY)
	{
		if (Tile.getDistance(xPos, yPos, nextX, nextY) <= moveRange) {
			this.nextX = nextX;
			this.nextY = nextY;
			return true;
		}
		return false;
	}
}
