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
public final class BreakListenerGentle implements Listener {

	private final SortingHopper plugin;
	private final ItemStack drop;
	/**
	 * Creates a Block Break listener
	 */
	public BreakListenerGentle(SortingHopper plugin, ItemStack drop) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
		this.drop = drop;
		
	}

	/**
	 * Event handler for block breaking
	 *
	 * @param event the Block Break Event
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() == Material.HOPPER) {
			Location loc = event.getBlock().getLocation();
			if (plugin.getRules().checkLocation(loc)) {
				if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
					event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
				}
				else if (!plugin.getRules().checkEmpty(loc)) {
					return;
				}
				
				plugin.getRules().removeRule(loc);
			}
		}
	}
}
