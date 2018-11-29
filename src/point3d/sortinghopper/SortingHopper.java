package point3d.sortinghopper;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

	private List<String> names = null;

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

		this.getLogger().info("started!");
		
		if (getConfig().getBoolean("crafting.enabled")) {
			final ServerLoadListener serverloadistener = new ServerLoadListener(this);
			pm.registerEvents(serverloadistener, this);
		}
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
	public void addRecipe() {
 		NamespacedKey namespacekey = new NamespacedKey(this, this.getDescription().getName());
		if (getConfig().getBoolean("crafting.shaped")) {
			ShapedRecipe recipe = new ShapedRecipe(namespacekey, this.getItem());
			List<String> l = getConfig().getStringList("crafting.recipe");
			recipe.shape(l.toArray(new String[0]));

			ConfigurationSection cs = getConfig().getConfigurationSection("crafting.ingredients");
			for (String k : cs.getKeys(false)) {
				if(Material.matchMaterial(cs.getString(k)) != null){
					recipe.setIngredient(k.charAt(0), Material.matchMaterial(cs.getString(k)));
				}
				else if(cs.getString(k).equals("REDSTONE_COMPARATOR")){
					recipe.setIngredient(k.charAt(0), Material.COMPARATOR);
				}
				else {
					this.getLogger().warning("Wrong material in crafting recipe!");
				}
			}

			getServer().addRecipe(recipe);
		} else {
			ShapelessRecipe recipe = new ShapelessRecipe(namespacekey, this.getItem());

			List<String> l = getConfig().getStringList("crafting.recipe");
			for (String s : l) {
				if(Material.matchMaterial(s) != null){
					recipe.addIngredient(Material.matchMaterial(s));
				}
				else if(s.equals("REDSTONE_COMPARATOR")){
					recipe.addIngredient(Material.COMPARATOR);
				}
				else {
					this.getLogger().warning("Wrong material in crafting recipe!");
				}
			}

			getServer().addRecipe(recipe);
		}
	}
}
