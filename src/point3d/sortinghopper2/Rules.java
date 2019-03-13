package point3d.sortinghopper2;

import java.util.HashMap;


import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import point3d.sortinghopper2.backend.RulesBackend;
import point3d.sortinghopper2.backend.flatfile.Flatfile;

public class Rules {
	private final SortingHopper plugin;
	private final RulesBackend backend;
	public HashMap<Location, Inventory> rules = new HashMap<Location, Inventory>();
	
	public Rules(SortingHopper plugin) {
	      if (plugin == null) {
	            throw new IllegalArgumentException("Plugin cannot be null");
	      }
	        this.plugin = plugin;
	        
	        switch(plugin.getConfig().getString("backend")) {
	        case "flatfile": 
	      	this.backend = new Flatfile(plugin);
	        	break;
	        default:
	      	plugin.getLogger().info("[SoringHopper] Wrong backend set in config.yml, using flatfile");
	      	this.backend = new Flatfile(plugin);
	        }
	}
	

	    
	
	public Inventory getInv(Location loc){
		if(rules.containsKey(loc)){
	  		return rules.get(loc);
	  	}  		
	  	else {
	  		Inventory inv = Sorter.createInv();
			rules.put(loc, inv);
	  		return inv;
	  	}
	}
	
	public boolean checkInv(Inventory inv){
		return rules.containsValue(inv);
	}
	
	public boolean checkLocation(Location loc){
		return rules.containsKey(loc);
	}

	public void removeRule(Location loc){
      	if(rules.containsKey(loc)){
      		SortingHopper.DebugLog("Break: Removing rule");
      		rules.remove(loc);
      	}
	}

	public void saveRules(){
		saveRules("rules");
	}
	public void saveRules(String filename) {

		backend.save(this.rules, filename);

	}
	public void loadAndBackup(){
		
		if(loadRules()) {
			backend.makeBackup(this.rules);
		}
	}
	
	public boolean loadRules(){
		return loadRules("rules");
	}

	public boolean loadRules(String filename){
		HashMap<Location, Inventory> result = backend.load(filename);
		if(result != null) {
			this.rules = result;
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean makeBackup() {
		return backend.makeBackup(this.rules);
	}
	
	public int loadBackup() {
		return backend.loadBackup();
	}
	
	public int getCurrentBackupN() {
		return RulesBackend.getCurrentBackupN(plugin.getConfig().getInt("backups_number"));
	}	
}
