package game.faction;

/**
 * Building statistic data.
 * @author Sherman Ying and Kewei Zhou
 * @version January 21, 2013
 * @since 1.7
 */
public enum BuildingData {
	// Human Buildings
	humanAntiAir("Anti-Air Cannon", "No more pesky flies.", Human.FACTION_NUM, 5, 
			// Name, Description, Faction type, unit type
			75, 10, 50, 5, 			// Max HP, Armour, Attack Damage, Action range
			0, false, true, 0, 0, 	// Regen/turn, Hits ground, Hits flying, Linear effect distance, Aoe radius
			4, 250, 250, 200, 0),	// Max build count, 4 costs

	humanBarrier("Advanced Barrier", "An advanced wall.", Human.FACTION_NUM, 1, 
			200, 20, 0, 0,
			10,	false, false, 0, 0,
			30, 250, 100, 150, 0),

	humanBunker("Bunker", "Deploys human units\nfor defense.", Human.FACTION_NUM, 6, 
			75, 15, 0, 1,
			0, false, false, 0, 0,
			4, 250, 250, 300, 50),

	humanControl("Control Centre", "Defend this from\nattacks.", Human.FACTION_NUM, 7, 
			300, 20, 0, 0,
			0, false, false, 0, 0, 
			1, 0, 0, 0, 0),	

	humanFactory("Equipment Factory", "Builds human attack\nunits.", Human.FACTION_NUM, 2,
			50, 10, 0, 0, 
			0,false, false, 0, 0, 
			1, 200, 150, 200, 100),

	humanRange("Range Cannon", "Hits from afar.", Human.FACTION_NUM, 3, 
			75, 10, 20, 6,
			0, true,true, 0, 0,
			4, 200, 150, 250, 0),

	humanSplash("Burst Cannon", "Need crowd control?", Human.FACTION_NUM, 4, 
			75, 10, 25, 3,
			0, true,false, 0, 1, 
			4, 350, 50, 300, 0),

	humanWall("Basic Wall", "A wall.", Human.FACTION_NUM, 0, 
			125, 15, 0, 0,  	
			0, false,false, 0, 0,  	
			50, 100, 100, 25, 0),

	// MechStorm buildings
	mechAssembly("MechStorm Assembly", "You want machines? \nWe got machines.", MechStorm.FACTION_NUM, 2,  
			// Name, Description, Faction type, unit type
			100, 20, 0, 0,  		// Max HP, Armour, Attack Damage, Action range
			0, false, false, 0, 0,  // Regen/turn, Hits ground, Hits flying, Linear effect distance, Aoe radius
			1, 250, 200, 150, 0),	// Max build count, 4 costs

	mechBarrier("MechStorm Barrier", "iWall 4S.",MechStorm.FACTION_NUM, 1, 
			250, 30, 0, 0,
			10, false, false, 0, 0,
			30, 250, 250, 100, 0),

	mechControl("MechStorm Overmind", "Controls all buildings\nand units remotely.",MechStorm.FACTION_NUM, 7,
			350, 30, 0, 0,
			15, false, false, 0, 0, 
			1, 0, 0, 0, 0),			

	mechDeploy("MechStorm Deployment", "Deploy your mechs\nfor defending.",MechStorm.FACTION_NUM, 6, 
			100, 20, 0, 1,
			0, false, false, 0, 0,
			4, 500, 500, 350, 0),

	mechLaser("MechStorm Laser", "Not your average\nlaser pointer.", MechStorm.FACTION_NUM, 3,
			100, 15, 35, 1,
			0, true, true, 3, 0,
			4, 400, 400, 400, 0),

	mechParticle("MechStorm Particle Beam", "Rips apart flying\nunits.",MechStorm.FACTION_NUM, 5, 
			100, 15, 45, 4,
			0, false, true, 0, 1,
			2, 500, 500, 500, 0),

	mechShock("MechStorm Shockwave", "ZAP!", MechStorm.FACTION_NUM, 4, 
			100, 15, 30, 0,
			0, true, false, 0, 4, 
			4, 300, 300, 300, 0),

	mechWall("MechStorm Wall", "iWall 4.",MechStorm.FACTION_NUM, 0, 
			150, 25, 0, 0, 
			5, false, false, 0, 0, 50,
			50, 50, 100, 0),

	// Swarm buildings
	swarmBarrier("Obeswarm Barrier", "A larger wall.", Swarm.FACTION_NUM, 1,
			// Name, Description, Faction type, unit type
			150, 20, 0, 0, 	  	// Max HP, Armour, Attack Damage, Action range
			0, false, false, 0, 0,  	// Regen/turn, Hits ground, Hits flying, Linear effect distance, Aoe radius
			30, 200, 200, 0, 150),	// Max build count, 4 costs

	swarmControl("Obeswarm Hive", "Your all-natural\ncommand centre.", Swarm.FACTION_NUM, 7, 
			200, 25, 0, 0, 
			0, false, false, 0, 0,
			1, 0, 0, 0, 0),

	swarmLauncher("Obeswarm Launcher", "Solves both garbage\nand flying problems.", Swarm.FACTION_NUM, 5,
			75, 10, 35, 4,
			0, true, true, 0, 0,
			4, 200, 200, 350, 150),

	swarmOutlet("Obeswarm Outlet", "Release your horde.", Swarm.FACTION_NUM, 6, 
			100, 10, 0, 1, 
			0, false, false, 0, 0,
			4, 150, 150, 50, 250),

	swarmProducer("Obeswarm Producer", "Mass-produces\neverything.", Swarm.FACTION_NUM, 2, 
			50, 5, 0, 0, 
			0, false, false, 0, 0, 
			1, 300, 150, 50, 100),

	swarmQuake("Obeswarm Quake", "You can still feel\nthe aftershocks.", Swarm.FACTION_NUM, 4, 
			100, 10, 25, 0, 
			0,true, false, 0, 2,
			4, 150, 150, 50, 50),

	swarmShredder("Obeswarm Shredder", "Needs sharpening.", Swarm.FACTION_NUM, 3, 
			100, 10, 45, 0, 
			0, true, false, 0, 1, 
			4, 100, 200, 100, 50),

	swarmWall("Obeswarm Wall", "A wall.", Swarm.FACTION_NUM, 0, 
			100, 10, 0, 0,
			0, false,false, 0, 0,
			50, 50, 50, 0, 0);
		
	/**
	 * Gives the BuildingData for the specified faction and build type.
	 * @param factionType the faction type.
	 * @param buildingType the building type.
	 * @return the BuildingData of the building.
	 */
	public static BuildingData getData(int factionType, int buildingType)
	{
		// Check human units
		if(factionType == Human.FACTION_NUM)
		{
			if (buildingType == 0)
				return BuildingData.humanWall;
			if (buildingType == 1)
				return BuildingData.humanBarrier;
			if (buildingType == 2)
				return BuildingData.humanFactory;
			if (buildingType == 3)
				return BuildingData.humanRange;
			if (buildingType == 4)
				return BuildingData.humanSplash;
			if (buildingType == 5)
				return BuildingData.humanAntiAir;
			if (buildingType == 6)
				return BuildingData.humanBunker;
			if (buildingType == 7)
				return BuildingData.humanControl;
		}
		// Check Swarm Units
		if(factionType == Swarm.FACTION_NUM)
		{
			if (buildingType == 0)
				return BuildingData.swarmWall;
			if (buildingType == 1)
				return BuildingData.swarmBarrier;
			if (buildingType == 2)
				return BuildingData.swarmProducer;
			if (buildingType == 3)
				return BuildingData.swarmShredder;
			if (buildingType == 4)
				return BuildingData.swarmQuake;
			if (buildingType == 5)
				return BuildingData.swarmLauncher;
			if (buildingType == 6)
				return BuildingData.swarmOutlet;
			if (buildingType == 7)
				return BuildingData.swarmControl;
		}
		// Check MechStorm units
		if(factionType == MechStorm.FACTION_NUM)
		{
			if (buildingType == 0)
				return BuildingData.mechWall;
			if (buildingType == 1)
				return BuildingData.mechBarrier;
			if (buildingType == 2)
				return BuildingData.mechAssembly;
			if (buildingType == 3)
				return BuildingData.mechLaser;
			if (buildingType == 4)
				return BuildingData.mechShock;
			if (buildingType == 5)
				return BuildingData.mechParticle;
			if (buildingType == 6)
				return BuildingData.mechDeploy;
			if (buildingType == 7)
				return BuildingData.mechControl;
		}
		return null;
	} // The building deals damage to the tiles surrounding
	private int aoeRad;
	private int armour; // Damage reduction is higher than on units
	private int actionRange; // For defensive buildings
	private int buildingType; // Type of building
	private boolean canAttackFlying; // The building can attack flying units
	private boolean canAttackGrounded; // The building can attack grounded units
	private int damage; // Only defensive buildings do damage
	private int factionType; // Faction originator
	private int maxCount; // Maximum buildings buildable in one region
	private int maxHealth; // Maximum health
	private String name; // Name of the buildings
	private String flavourText; // A brief description
	private int lineDistance; // The building deals damage in a line, up to the
	private int regenPerTurn;

	private int[] resourceCost; // Cost to build

	/**
	 * Sets the building data.
	 * @param buildingName the name of the building
	 * @param flavourText the description
	 * @param factionType the type of faction
	 * @param buildingType the type of building
	 * @param buildingMaxHealth the maximum health
	 * @param buildingArmour the armour or damage reduction
	 * @param buildingDamage the attack damage
	 * @param buildingAttackRange the action range
	 * @param buildingRegen the regeneration per turn
	 * @param buildingCanAttackGrounded whether it can attack ground units
	 * @param buildingCanAttackFlying whether it can attack flying units
	 * @param lineDist the linear damage distance of the attack
	 * @param aoeRad the area of effect radius around the target position
	 * @param maxUnitCount the max build number per region
	 * @param antiMatterCost anti matter resource cost
	 * @param metalCost metal resource cost
	 * @param fuelCost fuel resource cost
	 * @param organicCost organic material resource cost
	 */
	BuildingData(String buildingName, String flavourText, int factionType, int buildingType,
			int buildingMaxHealth, int buildingArmour, int buildingDamage,
			int buildingAttackRange, int buildingRegen,
			boolean buildingCanAttackGrounded, boolean buildingCanAttackFlying,
			int lineDist, int aoeRad, int maxUnitCount, int antiMatterCost,
			int metalCost, int fuelCost, int organicCost) {
		name = "" + buildingName;
		this.flavourText = flavourText;
		maxHealth = buildingMaxHealth;
		this.factionType = factionType;
		this.buildingType = buildingType;
		armour = buildingArmour;
		damage = buildingDamage;
		actionRange = buildingAttackRange;
		regenPerTurn = buildingRegen;
		canAttackGrounded = buildingCanAttackGrounded;
		canAttackFlying = buildingCanAttackFlying;
		this.lineDistance = lineDist;
		this.aoeRad = aoeRad;
		maxCount = maxUnitCount;
		resourceCost = new int[4];
		resourceCost[0] = antiMatterCost;
		resourceCost[1] = metalCost;
		resourceCost[2] = fuelCost;
		resourceCost[3] = organicCost;
	}
	/**
	 * Whether the building can attack flying units.
	 * @return whether this can attack flying units.
	 */
	public boolean canAttackFlying()
	{
		return canAttackFlying;
	}
	/**
	 * Whether the building can attack grounded units.
	 * @return whether this can attack grounded units.
	 */
	public boolean canAttackGrounded()
	{
		return canAttackGrounded;
	}

	/**
	 * Gives the range of the actions.
	 * @return the action range.
	 */
	public int getActionRange()
	{
		return actionRange;
	}

	/**
	 * Gives the area of effect radius of the attacks.
	 * @return the AOE radius of attacks.
	 */
	public int getAoeRadius()
	{
		return aoeRad;
	}

	/**
	 * Gives the damage reduction stat of the building.
	 * @return the damage reduction.
	 */
	public int getArmour()
	{
		return armour;
	}

	/**
	 * Gives the building type.
	 * @return the type of building.
	 */
	public int getBuildingType()
	{
		return buildingType;
	}

	/**
	 * Gives the attack damage value.
	 * @return the attack damage.
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
	 * Gives the type of this building's faction.
	 * @return the faction type.
	 */
	public int getFactionType()
	{
		return factionType;
	}

	/**
	 * Gives the linear effect distance of the attack.
	 * @return the linear effect distance.
	 */
	public int getLinearDistance()
	{
		return lineDistance;
	}

	/**
	 * Gives the maximum health of the building.
	 * @return the max health of the building.
	 */
	public int getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * Gives the display name of the building.
	 * @return the display name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gives the amount of health repaired per turn.
	 * @return the health regeneration.
	 */
	public int getRegenPerTurn()
	{
		return regenPerTurn;
	}
	/**
	 * Gives the resource cost of the specified resource type.
	 * @param resType the type of resource.
	 * @return the cost to build this.
	 */
	public int getResourceCost(int resType)
	{
		if (resType >= 0 && resType < 4)
			return resourceCost[resType];
		return 0;
	}
	
	/**
	 * Gives the maximum number of these buildings in one map.
	 * @return
	 */
	public int maxCount()
	{
		return maxCount;
	}
}
