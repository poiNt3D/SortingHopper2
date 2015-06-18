package point3d.sortinghopper;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Sorting Hopper plugin. Allows the creation of a special hopper that
 * "sorts", or only accepts a particular input.
 */
public class SortingHopper extends JavaPlugin {

	private final List<String> names = null;

	/**
	 * Sets up listeners for the SortingHopper
	 */
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		this.saveDefaultConfig();

		names = this.getConfig().getStringList("names");

		final HopperListener hopperListener = new HopperListener(this);
		pm.registerEvents(hopperListener, this);

		if (getConfig().getBoolean("replacedrops")) {
			final BreakListener breakListener = new BreakListener(this);
			pm.registerEvents(breakListener, this);
		}
		if (getConfig().getBoolean("preventitempickup")) {
			final PickupListener pickupListener = new PickupListener(this);
			pm.registerEvents(pickupListener, this);
		}
		if (getConfig().getBoolean("crafting.enabled")) {
			addRecipe(getItem());
		}
		getLogger().info("[SortingHopper] started!");
	}

	/**
	 * Check the given string against configured names
	 *
	 * @param name The name string to test
	 */
	public boolean checkNames(String name) {
		return names.contains(name);
	}

	/**
	 * Return an ItemStack with a custom display name
	 *
	 * @return an ItemStack with custom meta
	 */
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.HOPPER);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(names.get(0));

		item.setItemMeta(meta);
		return item;
	}

	/**
	 * Startup method, adds a specific crafting recipe for this sorting hopper
	 * if defined.
	 *
	 * @param item the ItemStack item to configure a new recipe for.
	 */
	private void addRecipe(ItemStack item) {
		if (getConfig().getBoolean("crafting.shaped")) {
			ShapedRecipe recipe = new ShapedRecipe(item);
			List<String> l = getConfig().getStringList("crafting.recipe");
			recipe.shape(l.toArray(new String[0]));

			ConfigurationSection cs = getConfig().getConfigurationSection("crafting.ingredients");
			for (String k : cs.getKeys(false)) {
				Material mat = Material.matchMaterial(cs.getString(k));
				recipe.setIngredient(k.charAt(0), mat);
			}

			getServer().addRecipe(recipe);
		} else {
			ShapelessRecipe recipe = new ShapelessRecipe(item);

			List<String> l = getConfig().getStringList("crafting.recipe");
			for (String s : l) {
				recipe.addIngredient(Material.matchMaterial(s));
			}

			getServer().addRecipe(recipe);
		}
	}
}
