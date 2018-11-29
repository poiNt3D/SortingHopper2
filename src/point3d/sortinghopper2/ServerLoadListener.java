package point3d.sortinghopper2;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

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
		addRecipe();
	}
	
	/**
	 * Startup method, adds a specific crafting recipe for this sorting hopper
	 * if defined.
	 */
 	public void addRecipe() {
 		NamespacedKey namespacekey = new NamespacedKey(plugin, plugin.getDescription().getName());
 		
 		if (plugin.getConfig().getBoolean("crafting.shaped")) {
			ShapedRecipe recipe = new ShapedRecipe(namespacekey, Sorter.getItem());
			List<String> l = plugin.getConfig().getStringList("crafting.recipe");
			recipe.shape(l.toArray(new String[0]));
			ConfigurationSection cs = plugin.getConfig().getConfigurationSection("crafting.ingredients");
			for (String k : cs.getKeys(false)) {
				if(Material.matchMaterial(cs.getString(k)) != null){
					recipe.setIngredient(k.charAt(0), Material.matchMaterial(cs.getString(k)));
				}
				else if(cs.getString(k).equals("REDSTONE_COMPARATOR")){
					recipe.setIngredient(k.charAt(0), Material.COMPARATOR);
				}
				else {
					plugin.getLogger().warning("Wrong material in crafting recipe: " + k);
				}
			}
			Bukkit.getServer().addRecipe(recipe);
 		}
 		else {
			ShapelessRecipe recipe = new ShapelessRecipe(namespacekey, Sorter.getItem());
			List<String> l = plugin.getConfig().getStringList("crafting.recipe");
			for (String s : l) {
				if(Material.matchMaterial(s) != null){
					recipe.addIngredient(Material.matchMaterial(s));
				}
				else if(s.equals("REDSTONE_COMPARATOR")){
					recipe.addIngredient(Material.COMPARATOR);
				}
				else {
					plugin.getLogger().warning("Wrong material in crafting recipe: " + s);
				}
			}
			Bukkit.getServer().addRecipe(recipe);
 		}
 	                
 	}
}
