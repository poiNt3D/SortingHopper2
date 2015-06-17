package point3d.sortinghopper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

/**
 * Listener for pickup event, if disabled in the config.
 */
public class PickupListener implements Listener {

	private final SortingHopper plugin;

	/**
	 * Initializes the listener cancelling pickup event.
	 * Configured by main configuration.
	 */
	public PickupListener(SortingHopper plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
	}

	/**
	 * Cancels inventory pickup of items with configured name.
	 *
	 * @param event the pickup event object
	 */
	@EventHandler
	public void onInventoryPickupEvent(InventoryPickupItemEvent event) {
		if (plugin.checkNames(event.getInventory().getName())) {
			event.setCancelled(true);
		}
	}
}
