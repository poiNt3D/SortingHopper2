package point3d.sortinghopper2;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Block Break listener that replaces default drop of standard hopper
 * with "customized" hopper. Does not play well with block break protection
 * plugins.
 */
public final class BreakListener implements Listener {

	private final SortingHopper plugin;      
	/**
	 * Creates a Block Break listener
	 */
	public BreakListener(SortingHopper plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
	}

	/**
	 * Event handler for block breaking
	 *
	 * @param event the Block Break Event
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if (event.isCancelled() == false && event.getBlock().getType() == Material.HOPPER) {
			Location loc = event.getBlock().getLocation();
			if (plugin.getRules().checkLocation(loc)) {
				plugin.getRules().removeRule(loc);
				
				ItemStack drop = Sorter.getItem();

				// Looks hacky
				event.setCancelled(true);
				event.getBlock().setType(Material.AIR);
				event.getBlock().getWorld().dropItemNaturally(loc, drop);
			}
		}
	}
}
