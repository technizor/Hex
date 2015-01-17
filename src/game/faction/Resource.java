package game.faction;

/**
 * Resource storage system, with adding, selling, and subtracting costs.
 * 
 * @author Sherman Ying and Kewei Zhou
 * @version January 21, 2013
 * @since 1.7
 */
public class Resource
{
	private int currentResource;
	private int maxResource;
	private int resourceRate;

	/**
	 * Creates a Resource supply at the initial value.
	 * 
	 * @param initialResource
	 *            the initial amount of resource
	 * @param maxResource
	 *            the limit of the resource
	 * @param resourceRate
	 *            the rate per turn per region of increase
	 */
	public Resource(int initialResource, int maxResource, int resourceRate) {
		this.maxResource = maxResource;
		this.currentResource = initialResource;
		this.resourceRate = resourceRate;
	}

	/**
	 * Adds to the resource supply based on the number of bases controlled.
	 * 
	 * @param producers
	 *            the number of command centres producing resources.
	 */
	public void addResources(int producers)
	{
		currentResource += producers * resourceRate;
		if (currentResource > getMaxResource())
			currentResource = getMaxResource();
	}

	/**
	 * Adds half of the cost of a building to the supply.
	 * 
	 * @param cost
	 *            the building's original cost.
	 */
	public void addSell(int cost)
	{
		currentResource += cost / 2;
		if (currentResource > maxResource)
			currentResource = maxResource;
	}

	/**
	 * Gives the current supply of resource.
	 * 
	 * @return the current supply.
	 */
	public int getCurrentResource()
	{
		return currentResource;
	}

	/**
	 * Gives the upper limit of resource storage.
	 * 
	 * @return the limit of storage.
	 */
	public int getMaxResource()
	{
		return maxResource;
	}

	/**
	 * Determines whether there are enough stored resources to afford the cost.
	 * 
	 * @param cost
	 *            the number of resources to be deducted.
	 * @return whether there are enough.
	 */
	public boolean isEnough(int cost)
	{
		return currentResource - cost >= 0;
	}

	/**
	 * Reduces the supply by the given cost.
	 * 
	 * @param cost
	 *            the number of resources required.
	 */
	public void subtractCost(int cost)
	{
		currentResource -= cost;
		if (currentResource < 0)
			currentResource = 0;
	}
}
