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

public class SortingHopper extends JavaPlugin {
  	private static final Logger mclog = Logger.getLogger("minecraft");
        private final HopperListener hopperListener = new HopperListener(this);
        private final BreakListener breakListener = new BreakListener(this);
        
	@Override
	public void onDisable() {
		mclog.info("[SortingHopper] stopped...");
	}
 
	@Override
	public void onEnable() {
            
                PluginManager pm = getServer().getPluginManager();
                this.saveDefaultConfig();
                


                 
                pm.registerEvents(hopperListener, this);
                
                if(getConfig().getBoolean("replacedrops")){
                pm.registerEvents(breakListener, this);                    
                }
                
                if (getConfig().getBoolean("crafting.enabled")){
                addSorterRecipe();
        	}

		mclog.info("[SortingHopper] started!");
                
	}
        
public ItemStack getHopper() {
ItemStack hopper = new ItemStack(Material.HOPPER);
ItemMeta meta = hopper.getItemMeta();

List<String> names = this.getConfig().getStringList("names");
meta.setDisplayName(names.get(0));

hopper.setItemMeta(meta);
return hopper;
}

public boolean checkNames(String name){
List<String> names = this.getConfig().getStringList("names"); 
return names.contains(name);
}

 private void addSorterRecipe() {
    
    if (getConfig().getBoolean("crafting.shaped")) {
	ShapedRecipe recipe = new ShapedRecipe(getHopper());
	List<String> l = getConfig().getStringList("crafting.recipe");
	recipe.shape(l.toArray(new String[0]));
	ConfigurationSection cs = getConfig().getConfigurationSection("crafting.ingredients");
			for (String k : cs.getKeys(false)) {
				Material mat = Material.matchMaterial(cs.getString(k));
				recipe.setIngredient(k.charAt(0), mat);
			}
			getServer().addRecipe(recipe);
		} else {
			ShapelessRecipe recipe = new ShapelessRecipe(getHopper());
			List<String> l = getConfig().getStringList("crafting.recipe");
			for (String s : l) {
				recipe.addIngredient(Material.matchMaterial(s));
			}
			getServer().addRecipe(recipe);
		}
	}
}
