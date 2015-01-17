package game.world;

import game.faction.Faction;
import game.faction.Human;
import game.faction.MechStorm;
import game.faction.Swarm;
import game.gui.ImageRes;
import game.gui.WorldMenu;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * The World version of the Map. Contains regions in its tiles. This simulates
 * the entire world.
 * 
 * @author Sherman Ying
 * @version January 18, 2013
 * @since 1.7
 */
public class World extends Map
{
	private Region activeMap; // the active region, if any
	private Faction activePlayer; // the turn player
	private ArrayList<Faction> playerList; // the list of players in the game

	// Game state
	private boolean isBuildPhase; // the phase of the turn
	private int turn; // turn number
	private String winner; // The winning player's name

	// Attack deployment
	private WorldMenu deployMenu; // A menu for choosing units to deploy
	private int deployedUnitCount; // A counter for the units to deploy into
									// battle
	private int[] deployedUnits; // A list of units to deploy
	private boolean selectedAttack; // Whether a target has been selected
	private int selectedAttackX, selectedAttackY; // Selection position

	/**
	 * Generates a new game world with the given side length.
	 * 
	 * @param mapSize
	 *            the side length.
	 * @param viewSize
	 *            the screen size.
	 * @param playerFactions
	 *            the factions of the players.
	 */
	public World(int mapSize, Dimension viewSize, int[] playerFactions) {
		super("Overworld", Map.WORLD_MAP, mapSize, viewSize);
		turn = 0;
		isBuildPhase = true;
		playerList = new ArrayList<Faction>();
		// Default map
		addNewPlayer("Player One", playerFactions[0]);
		addNewPlayer("Player Two", playerFactions[1]);
		((RegionTile) getTile(1, mapSize)).setController(playerList.get(0));
		// Add a third player
		if (playerFactions[2] != 0) {
			addNewPlayer("Player Three", playerFactions[2]);
			((RegionTile) getTile(mapSize, 1)).setController(playerList.get(1));
			((RegionTile) getTile(mapSize * 2 - 1, mapSize * 2 - 1))
					.setController(playerList.get(2));
		}
		// Only two players
		else {
			((RegionTile) getTile(mapSize * 2 - 1, mapSize))
					.setController(playerList.get(1));
		}
		// First player goes.
		activePlayer = playerList.get(0);
	}

	/**
	 * Gives the active map.
	 * 
	 * @return the active map.
	 */
	public Region activeMap()
	{
		return activeMap;
	}

	/**
	 * Gives the active player.
	 * 
	 * @return the active player.
	 */
	public Faction activePlayer()
	{
		return activePlayer;
	}

	/**
	 * Adds a player with the proper faction.
	 * 
	 * @param name
	 *            the player's name.
	 * @param factionType
	 *            the faction they want.
	 */
	public void addNewPlayer(String name, int factionType)
	{
		if (factionType == Human.FACTION_NUM)
			playerList.add(new Human(name));
		else if (factionType == Swarm.FACTION_NUM)
			playerList.add(new Swarm(name));
		else if (factionType == MechStorm.FACTION_NUM)
			playerList.add(new MechStorm(name));
	}

	/**
	 * Advances the game forward one turn phase.
	 */
	public void advancePhase()
	{
		activeMap = null;
		selectedAttack = false;
		if (isBuildPhase)
			isBuildPhase = false; // Switch the phase of the turn
		else {
			isBuildPhase = true;
			advanceTurn(); // Advance turn if advancing phase from combat phase
		}
	}

	/**
	 * Advances the game forward one turn.
	 */
	public void advanceTurn()
	{
		// Remove any players without territories remaining
		turn %= playerList.size();
		for (int player = playerList.size() - 1; player >= 0; player--) {
			if (playerList.get(player).territoryCount() <= 0) {
				playerList.remove(player);
			}
		}
		if (playerList.size() == 1) {
			// Player won the game
			winner = playerList.get(0).getPlayerName();
		}
		// Increment the turn and set the new active player.
		turn = (turn + 1) % playerList.size();
		activePlayer = playerList.get(turn % playerList.size());
		activePlayer.addResources(activePlayer.territoryCount());
	}

	/**
	 * Finishes deploying battle units when applicable.
	 */
	@Override
	public void cancelledAction()
	{
		if (deployMenu != null) {
			deployMenu = null;
			startBattle();
		}
		selectedAttack = false;
	}

	/**
	 * Selects a region to inspect.
	 */
	@Override
	public void confirmedAction()
	{
		RegionTile region = (this.getSelectedTile());
		if (isBuildPhase()) {
			// Set the view to the selected region
			if (region != null && region.getController() == activePlayer)
				activeMap = region.getRegion();
		} else if (!selectedAttack) {
			// Make sure that you have an adjacent region to move from
			RegionTile[] tiles = this.getTouchingTiles(getSelectX(),
					getSelectY());
			boolean hasAdjacentRegion = false;
			for (int i = 0; i < 6; i++)
				if (tiles[i] != null && !tiles[i].isInvisible()
						&& tiles[i].getController() == activePlayer())
					hasAdjacentRegion = true;
			if (region != null && hasAdjacentRegion) {
				// Capture the tile if it is not controlled
				if (region.getController() == null) {
					region.setController(activePlayer);
					advancePhase();
				}
				// Get the player to select the territory to invade from
				else if (region.getController() != activePlayer) {
					selectedAttack = true;
					selectedAttackX = getSelectX();
					selectedAttackY = getSelectY();
				}
			}
		}
		// When you select the attack target and invade position
		else if (selectedAttack) {
			// Continually add units to the deployment list until the menu is
			// exited or the limit is reached.
			if (deployMenu != null) {
				int option = deployMenu.getHexOption();
				if (deployedUnits[option] < this.getSelectedTile().getRegion()
						.getUnitStorage()[option]) {
					deployedUnits[option]++;
					deployedUnitCount++;
					if (deployedUnitCount < this.getSelectedTile().getRegion()
							.getMaxDeployment())
					{
						deployMenu = new WorldMenu(this.getSelectedTile()
								.getRegion(), deployedUnits, getViewSize(),
								this.getBackground(), option);
					}
					else {
						deployMenu = null;
						startBattle();
					}
				}
			}
			// Start deploying units
			else if (this.getSelectedTile().getController() == activePlayer()
					&& Tile.getDistance(getSelectX(), getSelectY(),
							selectedAttackX, selectedAttackY) == 1) {
				// Reset the deployment data
				deployedUnits = new int[24];
				deployedUnitCount = 0;
				deployMenu = new WorldMenu(this.getSelectedTile().getRegion(),
						deployedUnits, getViewSize(), this.getBackground(), 13);
			}
		}

	}

	/**
	 * Draws the region tiles.
	 */
	@Override
	public BufferedImage drawTile(int tileX, int tileY)
	{
		boolean isSelected = getSelectX() == tileX && getSelectY() == tileY;
		Faction factionType = ((RegionTile) this.getTile(tileX, tileY))
				.getController();
		BufferedImage img = new BufferedImage(Tile.DRAW_WIDTH,
				Tile.DRAW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = (Graphics2D) img.getGraphics();
		// Draw the colour of the faction on the tile
		if (factionType != null) {
			g2D.drawImage(
					getTile(tileX, tileY).draw(factionType.getFactionType(),
							isSelected), 0, 0, null);
		}
		// Otherwise draw the neutral gray tile
		else {
			g2D.drawImage(getTile(tileX, tileY).draw(0, isSelected), 0, 0, null);
		}
		// Draw the attacking icon if attacking
		if (selectedAttack && tileX == selectedAttackX
				&& tileY == selectedAttackY) {
			g2D.drawImage(ImageRes.attackPoint(),
					(img.getWidth(null) - Tile.DRAW_WIDTH) / 2,
					img.getHeight(null) - Tile.DRAW_HEIGHT, null);
		}
		return img;
	}

	/**
	 * Provides the sidebar access to its deployment menu.
	 * 
	 * @return
	 */
	public WorldMenu getDeployMenu()
	{
		return deployMenu;
	}

	/**
	 * Gives the tile at the selection position.
	 */
	@Override
	public RegionTile getSelectedTile()
	{
		return (RegionTile) super.getSelectedTile();
	}

	/**
	 * Gives the RegionTiles adjacent to the given position
	 */
	public RegionTile[] getTouchingTiles(int x, int y)
	{
		RegionTile[] regionTiles = new RegionTile[6];
		// Touching tiles are
		// (x-1, y), (x+1, y), (x, y-1), (x, y+1),
		// (x-1, y-1), (x+1, y+1)
		if(getTile(x - 1, y) instanceof RegionTile)
			regionTiles[0] = (RegionTile) getTile(x - 1, y);
		if(getTile(x + 1, y) instanceof RegionTile)
			regionTiles[1] = (RegionTile) getTile(x + 1, y);
		if(getTile(x, y - 1) instanceof RegionTile)
			regionTiles[2] = (RegionTile) getTile(x, y - 1);
		if(getTile(x, y + 1) instanceof RegionTile)
			regionTiles[3] = (RegionTile) getTile(x, y + 1);
		if(getTile(x - 1, y - 1) instanceof RegionTile)
			regionTiles[4] = (RegionTile) getTile(x - 1, y - 1);
		if(getTile(x + 1, y + 1) instanceof RegionTile)
			regionTiles[5] = (RegionTile) getTile(x + 1, y + 1);
		return regionTiles;
	}

	/**
	 * Gives the name of the winner of the entire game.
	 * 
	 * @return the name of the winner.
	 */
	public String getWinner()
	{
		return winner;
	}

	/**
	 * Gives whether the game is in the building phase.
	 * 
	 * @return whether it is the build phase.
	 */
	public boolean isBuildPhase()
	{
		return isBuildPhase;
	}

	@Override
	/**
	 * Changes the selection of the hex menu if it is active, otherwise changing the selection of itself.
	 */
	public void moveSelectHorizontal(boolean right)
	{
		if (deployMenu != null)
			deployMenu.moveSelectHorizontal(right);
		else
			super.moveSelectHorizontal(right);
	}

	@Override
	/**
	 * Changes the selection of the hex menu if it is active, otherwise changing the selection of itself.
	 */
	public void moveSelectVertical(boolean down)
	{
		if (deployMenu != null)
			deployMenu.moveSelectVertical(down);
		else
			super.moveSelectVertical(down);
	}

	/**
	 * Creates the proper type of tile for this map.
	 */
	@Override
	public Tile newTile(int tileX, int tileY, boolean isPlaceholder)
	{
		if (isPlaceholder)
			return new RegionTile(this, tileX, tileY, true);
		return new RegionTile(this, tileX, tileY);
	}

	/**
	 * Tells whether the player has chosen an attack target.
	 * 
	 * @return whether the player must select a territory to invade from.
	 */
	public boolean selectedAttack()
	{
		return selectedAttack;
	}

	/**
	 * Tells whether the player has targeted a sector for an attack.
	 * 
	 * @return whether the player has targeted a sector.
	 */
	public boolean selectedAttackPos()
	{
		return selectedAttack;
	}

	/**
	 * Deselects the active region, returning to the overworld view.
	 */
	public void setInactiveMap()
	{
		activeMap = null;
	}

	/**
	 * If the player has chosen to deploy any units, it will initiate the
	 * attack. Otherwise, it will reset.
	 */
	public void startBattle()
	{
		// Reset the actions if no units were deployed.
		selectedAttack = false;
		if (deployedUnitCount == 0)
			return;

		// Otherwise, begin the deployment
		activeMap = ((RegionTile) this
				.getTile(selectedAttackX, selectedAttackY)).getRegion();
		activeMap.setAttacker(activePlayer);
		int diameter = activeMap.getDiameter();
		int amountDeployed = 0;
		int unitType = 0;

		// Capture the tile if it is not controlled
		for (int x = 1; x <= diameter
				&& amountDeployed < this.deployedUnitCount; x++) {
			for (int y = 1; y <= diameter
					&& amountDeployed < this.deployedUnitCount; y++) {
				if (!activeMap.getTile(x, y).isInvisible()) {
					if (!activeMap.canBuildHere(x, y)) {
						do {
							unitType = (unitType + 1) % 24;
						} while (this.deployedUnits[unitType] <= 0);
						activeMap.deployUnit(activePlayer, unitType / 8 + 1,
								unitType % 8, x, y);
						amountDeployed++;
						this.getSelectedTile().getRegion().removeStoredUnit(unitType / 8 + 1,
								unitType % 8, 1);
						deployedUnits[unitType]--;
					}
				}
			}
		}
	}

	/**
	 * Gives the turn player.
	 * 
	 * @return the turn player.
	 */
	public Faction turnPlayer()
	{
		return playerList.get(turn % playerList.size());
	}
}
