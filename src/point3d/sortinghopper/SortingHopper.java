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
  	public static final Logger mclog = Logger.getLogger("minecraft");   
        private final HopperListener hopperListener = new HopperListener(this);
//        private final BreakListener breakListener = new BreakListener(this);
        // I dunno, should i have it here or there?
   
	@Override
	public void onEnable() {
            
                PluginManager pm = getServer().getPluginManager();
                this.saveDefaultConfig();               
                 
                pm.registerEvents(hopperListener, this);
                
                if(getConfig().getBoolean("replacedrops")){
                    final BreakListener breakListener = new BreakListener(this);
                    pm.registerEvents(breakListener, this);                    
                }
                if(getConfig().getBoolean("preventitempickup")){
                    final PickupListener pickupListener = new PickupListener(this);
                    pm.registerEvents(pickupListener, this);                    
                }                
                if (getConfig().getBoolean("crafting.enabled")){
                    addRecipe(getItem());
        	}               
		mclog.info("[SortingHopper] started!");
                
	}
        
        
public void DebugLog(String message){
    mclog.info(message);
}

        
public boolean checkNames(String name){
List<String> names = this.getConfig().getStringList("names"); 
return names.contains(name);
}

public ItemStack getItem() {
ItemStack item = new ItemStack(Material.HOPPER);
ItemMeta meta = item.getItemMeta();

List<String> names = this.getConfig().getStringList("names");
meta.setDisplayName(names.get(0));

item.setItemMeta(meta);
return item;
}

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
