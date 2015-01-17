package game.faction;

/**
 * Unit statistic data.
 * @author Sherman Ying and Kewei Zhou
 * @version January 21, 2013
 * @since 1.7
 */
public enum UnitData {
	// Human Units
	humanArtillery("Artillery", "Bombard the enemy.",Human.FACTION_NUM, 3,
			// display name, description, faction number, unit type
			50, 5, 30, 6, 1,  // Max health, damage reduction, attack damage, attack range, move range
			false, true, false, true, // attacks: building, unit; flying; attacks flying
			0, 1, 0, // self damage, aoe radius, linear effect distance
			3, 100, 100, 50, 200), //  max limit, 4 resource costs

	humanFighter("AirFighter", "A non-stealth plane.", Human.FACTION_NUM, 6,
			70, 5, 25, 3, 4,
			true,true, true, true, 
			0, 0, 0, 
			2, 100, 250, 200, 400),

	humanInfantry("Infantry", "A soldier equipped\nwith basic gear.", Human.FACTION_NUM, 0,
			20, 3, 10, 1, 3,
			true,true, false, true, 
			0, 0, 0, 
			5, 0, 0, 0, 100),

	humanMechanic("Mechanic", "Fixes your equipment.", Human.FACTION_NUM, 4,
			20, 3, -20, 1, 3,
			false,true, false, false, 
			0, 0, -10, 
			2, 50, 50, 0, 150),

	humanMothership("Mothership", "Ship name reads\n\"Starship Enterprise\".", Human.FACTION_NUM, 7,
			150, 20, 80, 3, 3,
			true, true, true, true, 
			0, 0, -10, 
			1, 2500, 2500, 2500, 2500),

	humanScout("Scout", "\"Scouts out\" enemy\nbuildings.", Human.FACTION_NUM, 1,
			10, 0, 10, 1, 5,
			true, false, false, false, 
			0, 0, 0, 
			5, 0, 0, 50, 100),

	humanSiege("Siege", "Hammer Time.", Human.FACTION_NUM, 5,
			100, 15, 50, 3, 2, 
			true, false,false, false, 
			0, 0, 0, 
			2, 200, 150, 400, 300),

	humanSniper("Sniper", "Boom! Headshot.",Human.FACTION_NUM, 2,
			15, 3, 50, 8, 1, 
			false, true,false, true,
			0, 0, 0, 3, 
			50, 50, 100, 100),

	// Mech Units
	mechBlaster("MechStorm Blaster", "A basic ranged fighter.", MechStorm.FACTION_NUM, 0, 
			40, 5, 10, 2,2, 
			true, true, false, true, 
			0, 0, 0, 
			4, 100, 100, 50, 0),

	mechDragon("MechStorm Dragon", "Not overused one bit.", MechStorm.FACTION_NUM, 4, 
			50, 10, 30, 1, 2,
			true, true, true, true, 
			3, 0, 0, 
			1, 500, 500, 500, 500),

	mechFighter("MechStorm Fighter", "It's always ready for\naction.", MechStorm.FACTION_NUM, 2, 
			80, 10, 20, 1,	2, 
			true, true, false, false, 
			0, 0, 0, 
			3, 250, 200, 150, 0),

	mechGiant("MechStorm Giant", "Not the most agile.", MechStorm.FACTION_NUM, 3, 
			80, 15, 25, 1, 1,
			true, true, false, false, 
			2, 1, 0, 
			2, 300, 250, 200, 0),

	mechLauncher("MechStorm Launcher", "Runs programs too.", MechStorm.FACTION_NUM, 1, 
			40, 5, 10, 5, 1, 
			true, true, false, true, 
			0, 1, 0, 
			3, 150, 200, 100, 0),

	mechOverlord("MechStorm Overlord", "Bow down to its\ntechnological might.", MechStorm.FACTION_NUM, 7, 
			200, 50, 40,3, 3, 
			true, true, true, true, 
			0, 0, 0, 
			1, 2500, 2500, 4000, 1000),

	mechRex("MechStorm Rex", "King of mechanical\nlizards.", MechStorm.FACTION_NUM, 6, 
			70, 25, 30, 1, 1, 
			true,true, false, false, 
			0, 1, 0, 
			1, 500, 500, 500, 500),

	mechShark("MechStorm Shark", "Rams into enemies at\nextreme speeds.", MechStorm.FACTION_NUM, 5,
			50, 10, 30, 1, 4,
			true, true, false, false,
			0, 0, 0, 
			1, 500, 500, 500, 500),

	// Swarm Units
	swarmAnnihilator("Obeswarm Annihilator", "When pure strength is\nneeded.",Swarm.FACTION_NUM, 7, 
			50, 20,120, 2, 2, 
			true, true, true, true,
			0, 0, 0, 
			1, 1000, 1000, 1000,1000),

	swarmCrusher("Obeswarm Crusher", "Get up close and\npersonal.", Swarm.FACTION_NUM, 3, 
			30, 10, 30, 1, 3,
			true, true, false, false, 
			0, 0, 0, 
			3, 100, 0, 100, 250),

	swarmEradicator("Obeswarm Eradicator", "Fights like a bowling\nball.", Swarm.FACTION_NUM, 6, 
			30, 15, 60,1, 2, 
			true, true, false, false, 
			3, 0, 0, 
			2, 100, 250, 200, 400),

	swarmInfector("Obeswarm Infector", "Do not touch.", Swarm.FACTION_NUM, 0,
			15, 5, 35, 1, 3,
			false, true, false, false, 
			0, 0, 0, 5, 0, 0, 0, 100),

	swarmKamikaze("Obeswarm Kamikaze", "Hide your important\nthings.", Swarm.FACTION_NUM, 
			1, 5, 0, 50, 0, 6,
			true, false, false, false, 
			0, 1, 50, 
			5, 0, 0, 0, 300),

	swarmRegenerator("Obeswarm Regenerator", "Grows back an arm.", Swarm.FACTION_NUM, 4,
			20, 5,-35, 1, 3, 
			false, true, false, false,
			0, 2, -35, 
			2, 200, 200, 50,400),

	swarmShielder("Obeswarm Shielder", "Hit me.", Swarm.FACTION_NUM, 2,
			70, 10, 0, 1, 3,
			false, true, false, false,
			0, 0, 0, 
			3, 0, 0, 50, 400),

	swarmThrasher("Obeswarm Thrasher", "Hit two enemies with\none attack.", Swarm.FACTION_NUM, 5, 
			40, 10, 45, 1,	3, 
			true, true, false, false, 
			2, 0, 0, 
			2, 200, 150, 400, 300);

	/**
	 * Gives the UnitData for the specified faction and unit type.
	 * @param factionType the faction type.
	 * @param unitType the unit type.
	 * @return the UnitData of the unit.
	 */
	public static UnitData getData(int factionType, int unitType)
	{
		// Check human units
		if(factionType == Human.FACTION_NUM)
		{
			if (unitType == 0)
				return UnitData.humanInfantry;
			if (unitType == 1)
				return UnitData.humanScout;
			if (unitType == 2)
				return UnitData.humanSniper;
			if (unitType == 3)
				return UnitData.humanArtillery;
			if (unitType == 4)
				return UnitData.humanMechanic;
			if (unitType == 5)
				return UnitData.humanSiege;
			if (unitType == 6)
				return UnitData.humanFighter;
			if (unitType == 7)
				return UnitData.humanMothership;
		}
		// Check Swarm Units
		if(factionType == Swarm.FACTION_NUM)
		{
			if (unitType == 0)
				return UnitData.swarmInfector;
			if (unitType == 1)
				return UnitData.swarmKamikaze;
			if (unitType == 2)
				return UnitData.swarmShielder;
			if (unitType == 3)
				return UnitData.swarmCrusher;
			if (unitType == 4)
				return UnitData.swarmRegenerator;
			if (unitType == 5)
				return UnitData.swarmThrasher;
			if (unitType == 6)
				return UnitData.swarmEradicator;
			if (unitType == 7)
				return UnitData.swarmAnnihilator;
		}
		// Check MechStorm units
		if(factionType == MechStorm.FACTION_NUM)
		{
			if (unitType == 0)
				return UnitData.mechBlaster;
			if (unitType == 1)
				return UnitData.mechLauncher;
			if (unitType == 2)
				return UnitData.mechFighter;
			if (unitType == 3)
				return UnitData.mechGiant;
			if (unitType == 4)
				return UnitData.mechDragon;
			if (unitType == 5)
				return UnitData.mechShark;
			if (unitType == 6)
				return UnitData.mechRex;
			if (unitType == 7)
				return UnitData.mechOverlord;
		}
		return null;
	}
	private int aoeRad;
	private int armour;
	private int attackRange;
	private boolean canAttackBuildings;
	private boolean canAttackFlying;
	private boolean canAttackUnits;
	private boolean canFly;
	private int damage;
	private int factionType;
	private int maxCount;
	private int maxHealth;
	private int moveRange;
	private String name;
	private String flavourText;
	private int linearDistance;
	private int[] resourceCost;
	private int selfDamage;

	private int unitType;

	UnitData(String unitName, String flavourText, int factionType, int unitType, int unitMaxHealth,
			int unitArmour, int unitDamage, int unitAttackRange,
			int unitMoveRange, boolean unitCanAttackBuildings,
			boolean unitCanAttackUnits, boolean unitCanFly,
			boolean unitCanAttackFlying, int lineDist, int unitAoeRad,
			int unitSelfDamage, int maxUnitCount, int resCost1, int resCost2,
			int resCost3, int resCost4) {
		name = "" + unitName;
		this.flavourText = flavourText;
		this.factionType = factionType;
		this.unitType = unitType;
		maxHealth = unitMaxHealth;
		armour = unitArmour;
		damage = unitDamage;
		attackRange = unitAttackRange;
		moveRange = unitMoveRange;
		canAttackBuildings = unitCanAttackBuildings;
		canAttackUnits = unitCanAttackUnits;
		canFly = unitCanFly;
		canAttackFlying = unitCanAttackFlying;
		linearDistance = lineDist;
		aoeRad = unitAoeRad;
		selfDamage = unitSelfDamage;
		maxCount = maxUnitCount;
		resourceCost = new int[4];
		resourceCost[0] = resCost1;
		resourceCost[1] = resCost2;
		resourceCost[2] = resCost3;
		resourceCost[3] = resCost4;
	}
	/**
	 * Gives whether this unit can attack buildings.
	 * @return whether this can attack buildings.
	 */
	public boolean canAttackBuildings()
	{
		return canAttackBuildings;
	}
	/**
	 * Gives whether this unit can attack flying units.
	 * @return whether this can attack flying units.
	 */
	public boolean canAttackFlying()
	{
		return canAttackFlying;
	}

	/**
	 * Gives whether this unit can attack grounded units.
	 * @return whether this can attack grounded units.
	 */
	public boolean canAttackUnits()
	{
		return canAttackUnits;
	}

	/**
	 * Tells whether this unit can fly.
	 * @return whether this unit can fly.
	 */
	public boolean canFly()
	{
		return canFly;
	}

	/**
	 * Gives the area of effect radius for attacks.
	 * @return the AOE radius.
	 */
	public int getAoeRadius()
	{
		return aoeRad;
	}

	/**
	 * Gives the damage reduction on hit.
	 * @return the damage reduction.
	 */
	public int getArmour()
	{
		return armour;
	}

	/**
	 * The attack range of the unit.
	 * @return the attacking range.
	 */
	public int getAttackRange()
	{
		return attackRange;
	}

	/**
	 * Gives the attack damage of the unit. Negative if this heals.
	 * @return the amount of damage this can deal.
	 */
	public int getDamage()
	{
		return damage;

	}

	/**
	 * Gives the flavour-text description.
	 * @return the description.
	 */
	public String getDescription()
	{
		return flavourText;
	}

	/**
	 * Gives the type of faction of this unit.
	 * @return the faction type.
	 */
	public int getFactionType()
	{
		return factionType;
	}

	/**
	 * Gives the linear effect distance.
	 * @return the linear effect distance.
	 */
	public int getLinearDistance()
	{
		return linearDistance;
	}

	/**
	 * Gives the maximum health of this unit.
	 * @return the max health.
	 */
	public int getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * Gives the range of movement of this unit.
	 * @return the movement range.
	 */
	public int getMoveRange()
	{
		return moveRange;
	}

	/**
	 * Gives the display name of this unit.
	 * @return the name of this unit.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gives the cost to build this unit for the given resource type.
	 * @param resType the type of resource.
	 * @return the cost of the resource.
	 */
	public int getResourceCost(int resType)
	{
		if (resType >= 0 && resType < 4)
			return resourceCost[resType];
		return 0;
	}

	/**
	 * Gives the type of unit.
	 * @return the unit type.
	 */
	public int getUnitType()
	{
		return unitType;
	}
	
	/**
	 * Gives the maximum number of these units you can store in a region.
	 * @return the maximum limit on units.
	 */
	public int maxCount()
	{
		return maxCount;
	}
	
	/**
	 * Gives the amount of damage this deals to itself after an attack.
	 * @return the amount of damage to take.
	 */
	public int selfDamage()
	{
		return selfDamage;
	}
}
