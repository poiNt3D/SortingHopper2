package point3d.sortinghopper2;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

/**
 * Listener for pickup event, if disabled in the config.
 */
public class PickupListener implements Listener {

	/**
	 * Cancels item pickup for inventory with configured name.
	 *
	 * @param event the pickup event object
	 */
	@EventHandler
	public void onInventoryPickupEvent(InventoryPickupItemEvent event) {
		if (Sorter.checkNames(event.getInventory().getName())) {
			event.setCancelled(true);
		}
	}
}
