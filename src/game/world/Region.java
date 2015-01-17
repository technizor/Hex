package game.world;

import game.faction.BuildingData;
import game.faction.Faction;
import game.faction.Resource;
import game.faction.UnitData;
import game.gui.HexMenu;
import game.gui.ImageRes;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Regional version of the Map handles building and battling.
 * 
 * @author Sherman Ying
 * @version January 13, 2013
 * @since 1.7
 */
public class Region extends Map
{
	/**
	 * Default size of a region.
	 */
	public static final int REGION_SIZE = 9;

	// Map properties
	private World map; // The overworld map
	private HexMenu buildMenu; // A menu for creating or deploying units and
								// buildings
	private int[] buildCount; // Buildings existing.
	private int[] unitCount; // Stored units.
	private Unit[][] units; // Deployed units.
	private Faction controller; // The controller of this region can change.
	private Faction attacker; // The attacker.
	private Faction winner; // The new controller.

	// Damage maps track the damage dealt to locations in combat turns.
	private int[][] buildDamageMap; // Damage for buildings.
	private int[][] unitDamageMap; // Damage for grounded units.
	private int[][] flyingDamageMap; // Damage for flying units.
	private int[][] healDamageMap; // Heals for grounded and flying units.

	// Combat command variables.
	private int combatTurn; // The turn of combat.
	private boolean buildingSelected; // Whether a building is selected.
	private boolean unitSelected; // Whether a unit is selected.
	private int firstSelectionX, firstSelectionY; // Selected positions.
	private boolean moveSelected; // Whether you have given the movement
									// command.
	private int moveSelectionX, moveSelectionY; // Movement positions.
	private boolean choseDeployUnit; // Whether a unit was chosen for
										// deployment.
	private int deployType; // Type of unit to deploy.

	/**
	 * Constructs a region with no controller.
	 * 
	 * @param name
	 *            the name of the region.
	 * @param mapSize
	 *            the side length of the map.
	 * @param map
	 *            the over world.
	 * @param viewSize
	 *            the size of the viewing window.
	 */
	public Region(String name, World map, Dimension viewSize) {
		super("Sector " + name, Map.REGION_MAP, REGION_SIZE, viewSize);

		units = new Unit[getDiameter() + 2][getDiameter() + 2];
		this.map = map;

		// Set up the unit limit trackers
		unitCount = new int[24]; // 0-7 human ,8-15 swarm, 16-23 mech
		buildCount = new int[24]; // 0-7 human ,8-15 swarm, 16-23 mech

		// Set up the damage maps
		buildDamageMap = new int[getDiameter() + 2][getDiameter() + 2];
		unitDamageMap = new int[getDiameter() + 2][getDiameter() + 2];
		flyingDamageMap = new int[getDiameter() + 2][getDiameter() + 2];
		healDamageMap = new int[getDiameter() + 2][getDiameter() + 2];
	}

	/**
	 * Constructs a building at the location, deducting resources.
	 * 
	 * @param buildType
	 *            the type of building to build.
	 * @param tileX
	 *            the x position to build at.
	 * @param tileY
	 *            the y position to build at.
	 */
	private void addBuilding(int buildType, int tileX, int tileY)
	{
		if (buildType >= 0 && buildType < 8) {
			Resource[] res = controller.getResources();
			// Deduct the correct amount of resources
			for (int type = 0; type < 4; type++)
				res[type].subtractCost(BuildingData.getData(
						controller.getFactionType(), buildType)
						.getResourceCost(type));
			buildCount[buildType]++;
			// Add the building
			setTile(controller.newBuilding(this, buildType, tileX, tileY),
					tileX, tileY);
		}
	}

	/**
	 * Adds a unit of the specified faction and unit type to this region
	 * 
	 * @param factionType
	 *            the faction type to add the unit
	 * @param unitType
	 *            the unit type to add
	 */
	private void addUnit(int factionType, int unitType)
	{
		if (unitType >= 0 && unitType < 8) {
			Resource[] res = controller.getResources();
			for (int type = 0; type < 4; type++)
				res[type].subtractCost(UnitData.getData(factionType, unitType)
						.getResourceCost(type));
			unitCount[(factionType - 1) * 8 + unitType]++;
		}
	}

	/**
	 * Advances the turn by moving units, then dealing damage to appropriate
	 * targets. After this, it removes any destroyed units and buildings, and
	 * checks for win conditions.
	 */
	private void advanceTurn()
	{
		// Move the units
		for (int xPos = 0; xPos < units.length; xPos++) {
			for (int yPos = 0; yPos < units.length; yPos++) {
				if (units[xPos][yPos] != null) {
					if (units[xPos][yPos].isPlaceholder()) {
						int origX = units[xPos][yPos].getXPos();
						int origY = units[xPos][yPos].getYPos();
						units[xPos][yPos] = units[origX][origY];
						units[origX][origY] = null;
						units[xPos][yPos].nextTurn();
					}
				}
			}
		}

		// Make sure the attack can continue by having at least 1 attacking unit
		// left
		boolean attackContinues = false;
		// Deal damage to all units and buildings
		for (int xPos = 0; xPos < units.length; xPos++) {
			for (int yPos = 0; yPos < units.length; yPos++) {
				// Damage a unit
				if (units[xPos][yPos] != null) {
					units[xPos][yPos].setAction(false);
					units[xPos][yPos].healUnit(healDamageMap[xPos][yPos]);
					if (units[xPos][yPos].canFly())
						units[xPos][yPos]
								.damageUnit(flyingDamageMap[xPos][yPos]);
					else
						units[xPos][yPos].damageUnit(unitDamageMap[xPos][yPos]);
					if (units[xPos][yPos].getCurrentHealth() <= 0)
						units[xPos][yPos] = null;
					else if (units[xPos][yPos].getController() == attacker)
						attackContinues = true;

				}
				// Damage a building
				else if (getTile(xPos, yPos).isBuilding()) {
					Building build = (Building) getTile(xPos, yPos);
					build.setAction(false);
					build.repairBuilding(build.getRegenPerTurn());
					build.damageBuilding(buildDamageMap[xPos][yPos]);
					if (build.getCurrentHealth() <= 0) {
						int buildType = build.getBuildingType();
						this.destroyBuilding(xPos, yPos);
						// If you destroyed a command post, the attacker wins
						if (buildType == 7)
							winner = attacker;
					}
				}
				// Reset all the damage maps
				buildDamageMap[xPos][yPos] = healDamageMap[xPos][yPos] = unitDamageMap[xPos][yPos] = flyingDamageMap[xPos][yPos] = 0;
			}
		}
		// When there are no more enemy units
		if (!attackContinues)
			winner = controller;
		// Otherwise continue
		else
			combatTurn++;
	}

	/**
	 * Tells whether a building like a deployment or tower has been selected.
	 * 
	 * @return whether a building is selected in combat
	 */
	public boolean buildingSelected()
	{
		return buildingSelected;
	}

	/**
	 * Executes an action in the build phase.
	 */
	private void buildPhaseAction()
	{
		// Bring up a Build menu.
		if (!getSelectedTile().isBuilding()) {
			if (buildMenu == null) {
				if (canBuildHere(getSelectX(), getSelectY()))
					buildMenu = new HexMenu(this, getViewSize(),
							getBackground());
			} else {
				int option = buildMenu.getHexOption();
				if (option < 7) {
					if (canAddBuilding(controller.getFactionType(), option)) {
						addBuilding(option, getSelectX(), getSelectY());
						buildMenu = null;
					}
				}
			}
		}
		// Bring up a unit production menu if selecting a factory.
		else if (getSelectedTile().isBuilding()
				&& ((Building) getSelectedTile()).getBuildingType() == 2) {
			if (buildMenu == null) {
				buildMenu = new HexMenu(this,
						((Building) getSelectedTile()).getFactionType(),
						getViewSize(), getBackground(), true);
			} else {
				int option = buildMenu.getHexOption();
				if (option < 8) {
					int unitFaction = ((Building) getSelectedTile())
							.getFactionType();
					if (this.canMakeUnit(unitFaction, option)) {
						this.addUnit(unitFaction, option);
						buildMenu = null;
					}
				}
			}
		}
	}

	/**
	 * Cancels an action in the building phase.
	 */
	private void buildPhaseCancel()
	{
		// Back out of this view
		if (buildMenu == null)
			map.setInactiveMap();
		// Exit the build/create unit menu.
		else
			buildMenu = null;
	}

	/**
	 * Tells whether a building can be built at all.
	 * 
	 * @param factionType
	 *            the faction constructing the building.
	 * @param buildType
	 *            the building type.
	 * @return whether it is possible to build.
	 */
	public boolean canAddBuilding(int factionType, int buildType)
	{
		int countPos = 8 * (factionType - 1) + buildType;
		if (countPos < 0 && buildType >= 24)
			return false;

		// Do not exceed max count
		if (buildCount[countPos] >= BuildingData
				.getData(factionType, buildType).maxCount())
			return false;

		// Make sure there are enough resources
		Resource[] res = controller.getResources();
		for (int type = 0; type < 4; type++)
			if (!res[type].isEnough(BuildingData
					.getData(factionType, buildType).getResourceCost(type)))
				return false;
		return true;
	}

	/**
	 * Tells whether it is possible to build in this location.
	 * 
	 * @param tileX
	 *            the x position to check.
	 * @param tileY
	 *            the y position to check.
	 * @return whether it is possible to build.
	 */
	public boolean canBuildHere(int tileX, int tileY)
	{
		int diameter = getDiameter();
		// You cannot build at the edges of the map, as this is the enemy unit
		// deployment area.
		if (tileX <= 1 || tileY <= 1 || tileX >= diameter || tileY >= diameter)
			return false;
		if (tileY - tileX == getSize() - 1)
			return false;
		if (tileX - tileY == getSize() - 1)
			return false;
		if (units[tileX][tileY] != null)
			return false;
		if (getTile(tileX, tileY).isBuilding())
			return false;
		return true;
	}

	/**
	 * Backtrack the action.
	 */
	@Override
	public void cancelledAction()
	{
		// Cancel the action for the current phase.
		if (map.isBuildPhase())
			buildPhaseCancel();
		else
			combatPhaseCancel();
	}

	public boolean canDeployUnit(int factionType, int unitType)
	{
		int countPos = (factionType-1) * 8 + unitType;
		return unitCount[countPos] > 0;
	}

	/**
	 * Checks to make sure you can still make and store this unit.
	 * 
	 * @param factionType
	 *            the type of faction of this unit.
	 * @param unitType
	 *            the type of unit.
	 * @return whether there is storage space.
	 */
	public boolean canMakeUnit(int factionType, int unitType)
	{
		int countPos = 8 * (factionType - 1) + unitType;
		if (countPos < 0 && unitType >= 24)
			return false;
		// Do not exceed max count
		if (unitCount[countPos] >= UnitData.getData(factionType, unitType)
				.maxCount())
			return false;

		// Make sure there are enough resources
		Resource[] res = controller.getResources();
		for (int type = 0; type < 4; type++)
			if (!res[type].isEnough(UnitData.getData(factionType, unitType)
					.getResourceCost(type)))
				return false;
		return true;
	}

	/**
	 * Tells whether the unit can move the the new position.
	 * 
	 * @param startX
	 *            the current x position.
	 * @param startY
	 *            the current y position.
	 * @param endX
	 *            the next x position.
	 * @param endY
	 *            the next y position.
	 * @return whether the unit can move.
	 */
	private boolean canMoveUnitHere(int startX, int startY, int endX, int endY)
	{
		// Stay in bounds
		if (startX < 0 || startY < 0 || endX < 0 || endY < 0
				|| startX > units.length || startY > units.length
				|| endX > units.length || endY > units.length)
			return false;
		// Make sure there is a unit
		if (units[startX][startY] == null)
			return false;
		// If its the same spot, the unit can stay.
		if (startX == endX && startY == endY)
			return true;
		// Otherwise, make a placeholder of the unit at the new location to
		// claim the spot.
		if (units[endX][endY] == null
				&& !getTile(endX, endY).isBuilding()
				&& !getTile(endX, endY).isInvisible()
				&& Tile.getDistance(startX, startY, endX, endY) <= units[startX][startY]
						.getMoveRange())
			return true;
		return false;
	}

	/**
	 * Whether the unit at this location can target another location based on
	 * its next move location.
	 * 
	 * @param unitX
	 *            the current x position.
	 * @param unitY
	 *            the current y position.
	 * @param moveX
	 *            the next x position.
	 * @param moveY
	 *            the next y position.
	 * @param targetX
	 *            the target x position.
	 * @param targetY
	 *            the target y position.
	 * @return whether the target can be attacked.
	 */
	private boolean canTargetHere(int unitX, int unitY, int moveX, int moveY,
			int targetX, int targetY)
	{
		int distance = Tile.getDistance(moveX, moveY, targetX, targetY);
		// Make sure you are in range
		if (distance <= units[unitX][unitY].getAttackRange())
			// Do not target your location if you can target another location
			if (distance == 0 && units[unitX][unitY].getAttackRange() > 0)
				return false;
			else
				return true;
		return false;
	}

	/**
	 * Executes a command in the combat phase.
	 */
	private void combatPhaseAction()
	{
		// Control the building menu if available.
		if (buildMenu != null) {
			int option = buildMenu.getHexOption();
			if (canDeployUnit(
					((Building) getTile(firstSelectionX, firstSelectionY))
							.getFactionType(),
					option)) {
				deployType = option;
				choseDeployUnit = true;
				buildMenu = null;
			}
		}
			
		// Otherwise, select the unit or building to interact with.
		else if (!unitSelected && !buildingSelected) {
			int selectionX = getSelectX();
			int selectionY = getSelectY();
			// Check for a unit
			if (units[selectionX][selectionY] != null
					&& this.getTurnPlayer() == units[selectionX][selectionY]
							.getController()
					&& !units[selectionX][selectionY].didAction()
					&& !units[selectionX][selectionY].isPlaceholder()) {
				// Save this unit's location
				unitSelected = true;
				firstSelectionX = selectionX;
				firstSelectionY = selectionY;
			}
			// Check for a Building
			else if (getTile(selectionX, selectionY).isBuilding()
					&& this.getTurnPlayer() == controller) {
				Building building = (Building) getSelectedTile();
				// Attacking an enemy
				if (building.isTower() && !building.didAction()) {
					// Save this building's location
					buildingSelected = true;
					firstSelectionX = selectionX;
					firstSelectionY = selectionY;
				}
				// Deploying units
				else if (building.isBunker()) {
					// Save this building's location
					buildingSelected = true;
					firstSelectionX = selectionX;
					firstSelectionY = selectionY;
					buildMenu = new HexMenu(this,
							((Building) getTile(firstSelectionX,
									firstSelectionY)).getFactionType(),
							getViewSize(), getBackground(), false);
				}
			}
			return;
		}
		// Unit action
		if (unitSelected) {
			// Set the move of this unit if the move is possible
			if (!moveSelected
					&& canMoveUnitHere(firstSelectionX, firstSelectionY,
							getSelectX(), getSelectY())) {
				this.setUnitMoveTarget(firstSelectionX, firstSelectionY,
						getSelectX(), getSelectY());
			}
			// Set the attack target of the selected unit
			else if (unitSelected && moveSelected) {
				if (this.canTargetHere(firstSelectionX, firstSelectionY,
						moveSelectionX, moveSelectionY, getSelectX(),
						getSelectY()))
					setUnitAttackTarget(firstSelectionX, firstSelectionY,
							moveSelectionX, moveSelectionY, getSelectX(),
							getSelectY());

			}
		}
		// Building action
		if (buildingSelected) {
			// Action for tower
			if (((Building) getTile(firstSelectionX, firstSelectionY))
					.isTower()) {
				setBuildingAttackTarget(firstSelectionX, firstSelectionY,
						getSelectX(), getSelectY());
			}
			// Deploy a defense unit
			else if (choseDeployUnit) {
				if (!getSelectedTile().isBuilding()
						&& units[getSelectX()][getSelectY()] == null) {
					this.deployStoredUnit(
							((Building) getTile(firstSelectionX,
									firstSelectionY)).getFactionType(),
							deployType, getSelectX(), getSelectY());
					buildingSelected = false;
					choseDeployUnit = false;
				}
			}
		}
	}

	/**
	 * Cancels a command in the combat phase.
	 */
	private void combatPhaseCancel()
	{
		// Undo the unit move.
		if (unitSelected) {
			// Cancel the move
			if (moveSelected) {
				moveSelected = false;
				if(moveSelectionX != firstSelectionX || moveSelectionY != firstSelectionY)
					units[moveSelectionX][moveSelectionY] = null;
				units[firstSelectionX][firstSelectionY].cancel();
			}
			// Unselect a unit
			else
				unitSelected = false;
				moveSelected = false;
		}
		// Undo a building move
		else if (buildingSelected) {
			// Cancel the deployment menu and unselect the bunker.
			if (((Building) getTile(firstSelectionX, firstSelectionY))
					.isBunker()) {
				buildMenu = null;
				buildingSelected = false;
				choseDeployUnit = false;
			}
			// Unselect the tower
			else
				buildingSelected = false;
		}
	}

	@Override
	/**
	 * Calls a build phase or combat phase action.
	 */
	public void confirmedAction()
	{
		// Call the correct actions.
		if (map.isBuildPhase()) {
			buildPhaseAction();
		} else {
			combatPhaseAction();
		}
	}

	/**
	 * Adds damage to the map from a unit.
	 * 
	 * @param xPos
	 *            the x position to deal damage to
	 * @param yPos
	 *            the y position to deal damage to
	 * @param damage
	 *            the damage to deal
	 * @param hitUnit
	 *            whether to hit units
	 * @param hitFlying
	 *            whether to hit flying units
	 * @param hitBuilding
	 *            whether to hit buildings
	 */
	private void damageLocation(int xPos, int yPos, int damage,
			boolean hitUnit, boolean hitFlying, boolean hitBuilding)
	{
		if (xPos >= 0 && yPos >= 0 && xPos < units.length
				&& yPos < units.length) {
			// Deal damage
			if (damage >= 0) {
				// Deal double damage if only hitting buildings or only units
				if (hitBuilding && !hitUnit)
					buildDamageMap[xPos][yPos] += damage * 2;
				if (hitUnit && !hitBuilding)
					unitDamageMap[xPos][yPos] += damage * 2;
				// otherwise deals normal damage
				if (hitBuilding && hitUnit) {
					unitDamageMap[xPos][yPos] += damage;
					buildDamageMap[xPos][yPos] += damage;
				}
				if (hitFlying) {
					flyingDamageMap[xPos][yPos] += damage;
					// Deal double damage if only hitting units
					if (hitBuilding)
						flyingDamageMap[xPos][yPos] += damage;
				}
				// Heal instead
			} else {
				healDamageMap[xPos][yPos] -= damage;
			}
		}
	}

	/**
	 * Deploys a stored unit at the given location.
	 * 
	 * @param factionType
	 *            the faction type of the unit.
	 * @param unitType
	 *            the type of the unit.
	 * @param xPos
	 *            the x position to deploy at.
	 * @param yPos
	 *            the y position to deploy at.
	 */
	private void deployStoredUnit(int factionType, int unitType, int xPos,
			int yPos)
	{
		// Get the correct position in the unit count array.
		int countPos = 8 * (factionType - 1) + unitType;
		if (countPos >= 0 && unitType < 24) {
			if (unitCount[countPos] > 0) {
				unitCount[countPos]--;
				if (xPos > 0 && xPos <= getDiameter() && yPos > 0
						&& yPos <= getDiameter()
						&& !this.getTile(xPos, yPos).isInvisible())
					units[xPos][yPos] = new Unit(controller, UnitData.getData(
							factionType, unitType), xPos, yPos);
			}
		}
	}

	/**
	 * Deploys a unit at the given location
	 * 
	 * @param controller
	 *            the controlling player
	 * @param factionType
	 *            the type of faction
	 * @param unitType
	 *            the type of unit
	 * @param xPos
	 *            the x position
	 * @param yPos
	 *            the y position
	 */
	public void deployUnit(Faction controller, int factionType, int unitType,
			int xPos, int yPos)
	{
		units[xPos][yPos] = new Unit(controller, UnitData.getData(factionType,
				unitType), xPos, yPos);
	}

	/**
	 * Destroy the building at the given position.
	 * 
	 * @param tileX
	 *            the x position of the building to destroy.
	 * @param tileY
	 *            the y position of the building to destroy.
	 */
	private void destroyBuilding(int tileX, int tileY)
	{
		// Make sure it is a building
		if (getSelectedTile().isBuilding()) {
			Building selection = ((Building) getSelectedTile());
			int factionType = selection.getFactionType() - 1;
			buildCount[factionType * 8 + selection.getBuildingType()]--;
			// Destroy building
			setTile(new BlockTile(this, tileX, tileY), tileX, tileY);
		}
	}

	/**
	 * Draws special images, like the buildings, units, and visual indicators
	 */
	@Override
	public BufferedImage drawTile(int tileX, int tileY)
	{ // Find proper draw position
		Image unitBuildingImg = null;
		Image overlayImg = null;
		BufferedImage fullImg = null;
		int factionType = 0;
		boolean isSelected = getSelectX() == tileX && getSelectY() == tileY;
		if (getTile(tileX, tileY).isBuilding()) {
			// Check if there is a building
			Building build = (Building) getTile(tileX, tileY);
			unitBuildingImg = build.drawBuilding(isSelected);
			factionType = this.getController().getFactionType();
		} else if (units[tileX][tileY] != null) {
			// Check if there is a unit
			if (units[tileX][tileY].isPlaceholder()
					|| (units[tileX][tileY].getNextX() == tileX && units[tileX][tileY]
							.getNextY() == tileY)) {
				unitBuildingImg = units[units[tileX][tileY].getXPos()][units[tileX][tileY]
						.getYPos()].draw(true);
			} else if (!units[tileX][tileY].isPlaceholder()) {
				unitBuildingImg = units[units[tileX][tileY].getXPos()][units[tileX][tileY]
						.getYPos()].draw(false);
			}
			if (moveSelected && tileX == moveSelectionX
					&& tileY == moveSelectionY)
				unitBuildingImg = units[this.firstSelectionX][firstSelectionY]
						.draw(true);
			factionType = units[tileX][tileY].getController().getFactionType();
			// Draw a movement cursor if in range and are moving the unit
		}
		if (unitSelected
				&& !moveSelected
				&& isSelected
				&& Tile.getDistance(firstSelectionX, firstSelectionY,
						getSelectX(), getSelectY()) <= units[firstSelectionX][firstSelectionY]
						.getMoveRange()) {
			overlayImg = ImageRes.movePoint();
			// Show attack point if in range
		} else if (unitSelected
				&& moveSelected
				&& isSelected
				&& canTargetHere(firstSelectionX, firstSelectionY,
						moveSelectionX, moveSelectionY, getSelectX(),
						getSelectY())) {
			overlayImg = ImageRes.attackPoint();
		} else if (buildingSelected
				&& isSelected
				&& Tile.getDistance(firstSelectionX, firstSelectionY,
						getSelectX(), getSelectY()) <= ((Building) getTile(
						firstSelectionX, firstSelectionY)).getAttackRange()) {
			overlayImg = ImageRes.attackPoint();
		}

		// Draw the unit or building if there was one on this tile
		if (unitBuildingImg != null) {
			fullImg = new BufferedImage(unitBuildingImg.getWidth(null),
					unitBuildingImg.getHeight(null) + Tile.DRAW_HEIGHT / 4,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2D = (Graphics2D) fullImg.getGraphics();
			g2D.drawImage(getTile(tileX, tileY).draw(factionType, isSelected),
					(fullImg.getWidth(null) - Tile.DRAW_WIDTH) / 2,
					fullImg.getHeight(null) - Tile.DRAW_HEIGHT, null);
			// Add a targeting indicator if this tile will take an attack.
			if (buildDamageMap[tileX][tileY] > 0
					|| unitDamageMap[tileX][tileY] > 0
					|| flyingDamageMap[tileX][tileY] > 0) {
				g2D.drawImage(ImageRes.target(),
						(fullImg.getWidth(null) - Tile.DRAW_WIDTH) / 2,
						fullImg.getHeight(null) - Tile.DRAW_HEIGHT, null);
			}
			g2D.drawImage(unitBuildingImg, 0, 0, null);
			if (overlayImg != null)
				g2D.drawImage(overlayImg,
						(fullImg.getWidth(null) - Tile.DRAW_WIDTH) / 2,
						fullImg.getHeight(null) - Tile.DRAW_HEIGHT, null);
		} else {
			fullImg = new BufferedImage(Tile.DRAW_WIDTH, Tile.DRAW_HEIGHT,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2D = (Graphics2D) fullImg.getGraphics();
			g2D.drawImage(getTile(tileX, tileY).draw(factionType, isSelected),
					0, 0, null);
			// Add a targeting indicator if this tile will take an attack.
			if (buildDamageMap[tileX][tileY] > 0
					|| unitDamageMap[tileX][tileY] > 0
					|| flyingDamageMap[tileX][tileY] > 0) {
				g2D.drawImage(ImageRes.target(),
						(fullImg.getWidth(null) - Tile.DRAW_WIDTH) / 2,
						fullImg.getHeight(null) - Tile.DRAW_HEIGHT, null);
			}
			if (overlayImg != null)
				g2D.drawImage(overlayImg,
						(fullImg.getWidth(null) - Tile.DRAW_WIDTH) / 2,
						fullImg.getHeight(null) - Tile.DRAW_HEIGHT, null);
		}

		return fullImg;
	}

	/**
	 * Ends the battle, giving control to the winning player.
	 * 
	 * @param winner
	 *            the winning player.
	 */
	private void endBattle(Faction winner)
	{
		// Delete all units, adding to storage any living units
		for (int x = 0; x < units.length; x++) {
			for (int y = 0; y < units.length; y++) {
				if (units[x][y] != null) {
					this.storeUnit(units[x][y].getFactionType(),
							units[x][y].getUnitType());
					units[x][y] = null;
				}
			}
		}
		// Set the controller
		this.setController(winner);
		attacker = null;
		winner = null;
		combatTurn = 0;
		map.setInactiveMap();
		map.advancePhase();
	}

	/**
	 * Gives the number of a specific type of building built in this region.
	 * 
	 * @param factionType
	 *            the faction that originally built the building.
	 * @param buildingType
	 *            the type of building.
	 * @return the number of buildings built.
	 */
	public int getBuildingCount(int factionType, int buildingType)
	{
		// Get the correct position in the building count array.
		int countPos = 8 * (factionType - 1) + buildingType;
		return buildCount[countPos];
	}

	/**
	 * Gives the player in control of this region.
	 * 
	 * @return the player controlling this sector.
	 */
	public Faction getController()
	{
		return controller;
	}

	/**
	 * Gives the hex menu.
	 * 
	 * @return the hex menu.
	 */
	public HexMenu getHexMenu()
	{
		return buildMenu;
	}

	public int getMaxDeployment()
	{
		return 6 * (getSize() - 1);
	}

	/**
	 * Gives the block tile at the selected point
	 */
	@Override
	public BlockTile getSelectedTile()
	{
		return (BlockTile) super.getSelectedTile();
	}

	/**
	 * Gives the unit at the selected point
	 * 
	 * @return the unit selected
	 */
	public Unit getSelectedUnit()
	{
		return units[getSelectX()][getSelectY()];
	}

	/**
	 * Gives the player whose turn it is, even in combat.
	 * 
	 * @return the player giving commands.
	 */
	public Faction getTurnPlayer()
	{
		if (map.isBuildPhase())
			return controller;
		if (combatTurn % 2 == 0)
			return attacker;
		return controller;
	}

	/**
	 * Gives the number of units for each faction type stored in this region.
	 * 
	 * @param factionType
	 *            the type of faction units to check.
	 * @param unitType
	 *            the unit type to check.
	 * @return the number of units in storage.
	 */
	public int getUnitCount(int factionType, int unitType)
	{
		factionType--;
		return unitCount[factionType * 8 + unitType];
	}

	/**
	 * Gives a copy of the unit counts.
	 * 
	 * @return the unit counts.
	 */
	public int[] getUnitStorage()
	{
		int[] unitList = new int[24];
		for (int unitType = 0; unitType < 24; unitType++)
			unitList[unitType] = unitCount[unitType];
		return unitList;
	}
	
	public void removeStoredUnit(int factionType, int unitType, int number)
	{
		int countPos = (factionType-1)*8+unitType;
		if(countPos < 0 || countPos >= 24)
			return;
		unitCount[countPos] -= number;
		if(unitCount[countPos] < 0) unitCount[countPos] = 0;
	}

	/**
	 * Gives the winner of the battle, if ongoing.
	 * 
	 * @return the winner.
	 */
	public Faction getWinner()
	{
		return winner;
	}

	/**
	 * Tells whether the player has selected a move location.
	 * 
	 * @return whether a move location was selected.
	 */
	public boolean moveSelected()
	{
		return moveSelected;
	}

	@Override
	/**
	 * Changes the selection of the hex menu if it is active, otherwise changing the selection of itself.
	 */
	public void moveSelectHorizontal(boolean right)
	{
		if (buildMenu != null)
			buildMenu.moveSelectHorizontal(right);
		else
			super.moveSelectHorizontal(right);
	}

	@Override
	/**
	 * Changes the selection of the hex menu if it is active, otherwise changing the selection of itself.
	 */
	public void moveSelectVertical(boolean down)
	{
		if (buildMenu != null)
			buildMenu.moveSelectVertical(down);
		else
			super.moveSelectVertical(down);
	}

	/**
	 * Creates the proper type of tiles for this map.
	 */
	@Override
	public Tile newTile(int tileX, int tileY, boolean isPlaceholder)
	{
		if (isPlaceholder)
			return new BlockTile(this, tileX, tileY, true);
		return new BlockTile(this, tileX, tileY);
	}

	/**
	 * Sells buildings during the build phase and advances the combat turn
	 * during the combat phase.
	 */
	public void actionTriggered()
	{

		// Sell the selected building
		if (map.isBuildPhase() && buildMenu == null) {
			sellBuilding(getSelectX(), getSelectY());
		}
		// End the battle if possible, after allowing viewing of results.
		else if (winner != null) {
			this.endBattle(winner);
		}
		// End your combat turn
		else if (!unitSelected && !buildingSelected) {
			advanceTurn();
		}
		// Finish your move with the unit without attacking.
		else if (unitSelected && moveSelected) {
			unitSelected = false;
			moveSelected = false;
			units[firstSelectionX][firstSelectionY].setAction(true);
		}
		
	}

	/**
	 * Destroys a building, refunding half its value.
	 * 
	 * @param tileX
	 *            the x position of the building to sell.
	 * @param tileY
	 *            the y position of the building to sell.
	 */
	private void sellBuilding(int tileX, int tileY)
	{
		// Make sure it is a building.
		if (getSelectedTile().isBuilding()) {
			Building selection = ((Building) getSelectedTile());
			// You cannot sell your command post.
			if (selection.getBuildingType() != 7) {
				// Add 50% of cost to resources.
				Resource[] res = controller.getResources();
				for (int type = 0; type < 4; type++) {
					res[type].addSell(selection.getResourceCost(type));
				}
				// Destroy building.
				destroyBuilding(tileX, tileY);
			}
		}
	}

	/**
	 * Set the attacking player of this region.
	 * 
	 * @param attacker
	 *            the active player that attacked this claimed territory.
	 */
	public void setAttacker(Faction attacker)
	{
		this.attacker = attacker;
	}

	/**
	 * Set the attacking target of a tower, applying the special damage effects.
	 * 
	 * @param buildX
	 *            the x position of the building.
	 * @param buildY
	 *            the y position of the building.
	 * @param targetX
	 *            the x position of the target.
	 * @param targetY
	 *            the y position of the target.
	 */
	private void setBuildingAttackTarget(int buildX, int buildY, int targetX,
			int targetY)
	{
		Building building = (Building) getTile(buildX, buildY);
		int damageVal = building.getDamage();
		boolean hitGrounded = building.canAttackGrounded();
		boolean hitFlying = building.canAttackFlying();
		// Make sure that you are in attack range.
		if (Tile.getDistance(buildX, buildY, targetX, targetY) <= building
				.getAttackRange()) {
			// Aoe damage around target location in six directions
			for (int radius = building.getAoeRadius(); radius >= 0; radius--) {
				int damageX = targetX + radius;
				int damageY = targetY + radius;
				damageLocation(damageX, damageY, damageVal, hitGrounded,
						hitFlying, false);
				damageX = targetX - radius;
				damageY = targetY - radius;
				damageLocation(damageX, damageY, damageVal, hitGrounded,
						hitFlying, false);
				damageY = targetY;
				damageLocation(damageX, damageY, damageVal, false, hitGrounded,
						hitFlying);
				damageX = targetX + radius;
				damageLocation(damageX, damageY, damageVal, hitGrounded,
						hitFlying, false);
				damageX = targetX;
				damageY = targetY - radius;
				damageLocation(damageX, damageY, damageVal, hitGrounded,
						hitFlying, false);
				damageY = targetY + radius;
				damageLocation(damageX, damageY, damageVal, hitGrounded,
						hitFlying, false);
			}
			// Linear damage
			int xDiff = targetX - buildX;
			int yDiff = targetY - buildY;
			for (int reachDist = building.getReach() - 1; reachDist > 0; reachDist--) {
				int damageX = buildX
						+ (xDiff > 0 ? reachDist : (xDiff < 0 ? reachDist * -1
								: 0));
				int damageY = buildY
						+ (yDiff > 0 ? reachDist : (yDiff < 0 ? reachDist * -1
								: 0));

				damageLocation(damageX, damageY, damageVal, hitGrounded,
						hitFlying, false);
			}
		}
		building.setAction(true);
		buildingSelected = false;
	}

	/**
	 * Changes the controller of this region.
	 * 
	 * @param player
	 *            the player to gain control of this region.
	 */
	public void setController(Faction player)
	{
		if (this.controller != null)
			controller.lostTerritory();
		this.controller = player;
		player.gainedTerritory();
		setTile(controller.newBuilding(7, this.getSize(), this.getSize()),
				this.getSize(), this.getSize());
	}

	/**
	 * Sets an attack target.
	 * 
	 * @param unitX
	 *            attacking unit's position x
	 * @param unitY
	 *            attacking unit's position y
	 * @param moveX
	 *            the next x position of the unit
	 * @param moveY
	 *            the next y position of the unit
	 * @param targetX
	 *            target position x
	 * @param targetY
	 *            target position y
	 */
	private void setUnitAttackTarget(int unitX, int unitY, int moveX,
			int moveY, int targetX, int targetY)
	{
		// Stay in bounds
		if (unitX < 0 || unitY < 0 || targetX < 0 || targetY < 0
				|| unitX > units.length || unitY > units.length
				|| targetX > units.length || targetY > units.length)
			return;
		// Set the attack target
		if (units[unitX][unitY] != null) {
			int damageVal = units[unitX][unitY].getDamage();
			boolean hitUnit = units[unitX][unitY].canAttackUnits();
			boolean hitFlying = units[unitX][unitY].canAttackFlying();
			boolean hitBuilding = units[unitX][unitY].canAttackBuildings();
			// Add to the damage tables
			for (int radius = units[unitX][unitY].getAoeRadius(); radius >= 0; radius--) {

				// Get damage properties
				int damageX = targetX + radius;
				int damageY = targetY + radius;

				// Add damage to the map
				damageLocation(damageX, damageY, damageVal, hitUnit, hitFlying,
						hitBuilding);
				damageX = targetX - radius;
				damageY = targetY - radius;
				damageLocation(damageX, damageY, damageVal, hitUnit, hitFlying,
						hitBuilding);
				damageY = targetY;
				damageLocation(damageX, damageY, damageVal, hitUnit, hitFlying,
						hitBuilding);
				damageX = targetX + radius;
				damageLocation(damageX, damageY, damageVal, hitUnit, hitFlying,
						hitBuilding);
				damageX = targetX;
				damageY = targetY - radius;
				damageLocation(damageX, damageY, damageVal, hitUnit, hitFlying,
						hitBuilding);
				damageY = targetY + radius;
				damageLocation(damageX, damageY, damageVal, hitUnit, hitFlying,
						hitBuilding);
			}
			// Linear damage
			int xDiff = targetX - moveX;
			int yDiff = targetY - moveY;
			for (int reachDist = 1; reachDist < units[unitX][unitY].getReach(); reachDist++) {
				// Get damage properties
				int damageX = moveX
						+ (xDiff > 0 ? reachDist : (xDiff < 0 ? reachDist * -1
								: 0));
				int damageY = moveY
						+ (yDiff > 0 ? reachDist : (yDiff < 0 ? reachDist * -1
								: 0));

				// Add damage to the map
				damageLocation(damageX, damageY, damageVal, hitUnit, hitFlying,
						hitBuilding);
			}
			// Deal damage to the unit's next location if they hurt themselves.
			flyingDamageMap[moveX][moveY] += units[unitX][unitY]
					.getSelfDamage();
			unitDamageMap[moveX][moveY] += units[unitX][unitY].getSelfDamage();
			moveSelected = false;
			unitSelected = false;
		}
		units[unitX][unitY].setAction(true);
	}

	/**
	 * Sets a movement target for a unit.
	 * 
	 * @param unitX
	 *            the original position x.
	 * @param unitY
	 *            the original position y
	 * @param moveX
	 *            the new position x
	 * @param moveY
	 *            the new position y
	 */
	private void setUnitMoveTarget(int unitX, int unitY, int moveX, int moveY)
	{
		// Stay in bounds
		if (unitX < 0 || unitY < 0 || moveX < 0 || moveY < 0
				|| unitX > units.length || unitY > units.length
				|| moveX > units.length || moveY > units.length)
			return;
		// If possible to move
		if (canMoveUnitHere(unitX, unitY, moveX, moveY)) {
			// Set move target, then add a placeholder unit so that no one can
			// move there
			units[unitX][unitY].setNext(moveX, moveY);
			// Don't make a placeholder if in the same spot
			if (moveX != unitX || moveY != unitY)
				units[moveX][moveY] = new Unit(units[unitX][unitY]);
			moveSelected = true;
			moveSelectionX = moveX;
			moveSelectionY = moveY;
		}
	}

	/**
	 * Adds a unit from the map to the storage if there is space.
	 * 
	 * @param factionType
	 *            the type of faction of the unit.
	 * @param unitType
	 *            the type of unit.
	 */
	public void storeUnit(int factionType, int unitType)
	{
		int countPos = 8 * (factionType - 1) + unitType;
		if (countPos < 0 && unitType >= 24)
			return;
		if (unitCount[countPos] < UnitData.getData(factionType, unitType)
				.maxCount()) {
			unitCount[countPos]++;
		}
	}

	/**
	 * Tells whether a unit was selected for a command in the combat phase.
	 * 
	 * @return whether a unit was selected.
	 */
	public boolean unitSelected()
	{
		return unitSelected;
	}
}
