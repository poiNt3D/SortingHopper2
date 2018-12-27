package point3d.sortinghopper2;
import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import point3d.sortinghopper2.PlayerListener;
/**
 * Sorting Hopper plugin. Allows the creation of a special hopper that
 * "sorts", or only accepts a particular input.
 */
public class SortingHopper extends JavaPlugin{
	
  	public static final Logger mclog = Logger.getLogger("minecraft"); 
  	private final Rules rules = new Rules(this);
  	private final Sorter sorter = new Sorter(this);
  	private static boolean debug = true;
  	
  	
	@Override
	public void onEnable() {
		
		PluginManager pm = getServer().getPluginManager();
		    
		this.loadConf();
		debug=getConfig().getBoolean("debug");
		
		this.getCommand("sortinghopper").setExecutor(new CommandListener(this));
		
		final PlayerListener playerListener = new PlayerListener(this);
		pm.registerEvents(playerListener, this);
		
		final HopperListener hopperListener = new HopperListener(this);
		pm.registerEvents(hopperListener, this);
		
		if (getConfig().getBoolean("replacedrops")) {
			final BreakListener breakListener = new BreakListener(this);
			pm.registerEvents(breakListener, this);
		} else  {
			if(Material.matchMaterial(this.getConfig().getString("add_drop")) != null){
				ItemStack drop = new ItemStack(Material.matchMaterial(this.getConfig().getString("add_drop")));
				final BreakListenerGentle breakListener = new BreakListenerGentle(this, drop);
				pm.registerEvents(breakListener, this);
			}
		}
			
		
		//Config setting renamed, checking old name for compatibility 
		if(getConfig().getBoolean("preventrename") || getConfig().getBoolean("convert_old")){
			final PlaceListener placeListener = new PlaceListener(this);
			pm.registerEvents(placeListener, this);
		}
		
		if (getConfig().getBoolean("preventitempickup")) {
			final PickupListener pickupListener = new PickupListener();
			pm.registerEvents(pickupListener, this);
		}
		else if(getConfig().getBoolean("sortitempickup")) {
			final PickupListenerFilter pickupListener = new PickupListenerFilter(this);
			pm.registerEvents(pickupListener, this);
		}		
		if (getConfig().getBoolean("crafting.enabled")) {
			final ServerLoadListener serverloadistener = new ServerLoadListener(this);
			pm.registerEvents(serverloadistener, this);
		}

		
		rules.loadAndBackup();
		TagUtil.loadSortingTags();
		
		 
		this.getLogger().info("started!");
		                
		}
	@Override
	public void onDisable() {
			this.getLogger().info("Saving rules...");
			rules.saveRules();
	}

	public void reload(){
		this.reloadConfig();
		TagUtil.loadSortingTags();
		sorter.reload();
	}
	
	private void loadConf(){
		
		this.saveDefaultConfig();
		
		File itemgroupsyml = new File(this.getDataFolder(), "itemgroups.yml");
		if (!itemgroupsyml.exists()) {
			this.saveResource("itemgroups.yml", false);
		}
		sorter.reload();
	}
	
	public Rules getRules(){
		return this.rules;
	}

	public static void DebugLog(String message){
		if(debug){
			mclog.info("[Sorting Hopper2] " + message);
		}
	    
	}
}