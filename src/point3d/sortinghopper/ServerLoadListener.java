package point3d.sortinghopper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public final class ServerLoadListener implements Listener {

	private final SortingHopper plugin;

	/**
	 * Creates a Server Load listener
	 */
	public ServerLoadListener(SortingHopper plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
	}

	/**
	 * Initializes the listener triggering on server load.
	 * Needed to properly register new recipe when "load: STARTUP" is used.
	 * See https://hub.spigotmc.org/jira/browse/SPIGOT-4225 for details.
	 */
	@EventHandler
	public void onServerLoad(ServerLoadEvent event) {
		plugin.getLogger().info("Adding recipe");
		plugin.addRecipe();
	}
	
}
