package point3d.sortinghopper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Hopper event listener, manages the sorting function.
 */
public final class HopperListener implements Listener {

	private final SortingHopper plugin;

	/**
	 * Initializes the hopper listener.
	 *
	 * @param plugin the SortingHopper to connect the listener to.
	 */
	public HopperListener(SortingHopper plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
	}

	/**
	 * Inventory movement event, triggered by hopper seek.
	 *
	 * @param event the Inventory Move event object.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	public void onInventoryMoveItemEvent(InventoryMoveItemEvent event) {
		Inventory initiator = event.getInitiator();
		Inventory dest = event.getDestination();
		Inventory source = event.getSource();


		// Prevent items from emptying a sorter, or being pulled out by other hopper
		if (initiator != source && plugin.checkNames(source.getName())
				&& (!source.contains(event.getItem().getType())
						|| plugin.getConfig().getBoolean("preventitempull"))) {
			event.setCancelled(true);
			return;
		}

		if (plugin.checkNames(initiator.getName())
				&& !initiator.contains(event.getItem().getType())) {
			event.setCancelled(true);

			// Try to move items in other slots
			if (dest != initiator) {
				for (int slot = 1; slot < initiator.getSize(); slot++) {
					if ( this.MoveItem(initiator, slot, dest) ) {
						break;
					}
				}
			}
		}
	}

	/**
	 * Internal method moveItem, does a move.
	 *
	 * @param initiator Inventory source
	 * @param slot Inventory slot #
	 * @param dest Inventory destination
	 * @return indicates success of move
	 */
	public boolean MoveItem(Inventory initiator, Integer slot, Inventory dest) {
		ItemStack item = initiator.getItem(slot);
		if (item == null || !initiator.contains(item.getType(), 2)) {
			return false;
		}

		ItemStack newitem = item.clone();
		newitem.setAmount(1);
		if (!dest.addItem(newitem).isEmpty()) {
			return false;
		}

		Integer amount = item.getAmount();
		if (amount > 1) {
			Integer newamount = amount - 1;
			item.setAmount(newamount);
			return true;
		} else {
			initiator.removeItem(item);
		}

		return false;
	}
}
