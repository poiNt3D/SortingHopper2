package point3d.sortinghopper2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.sothatsit.usefulsnippets.EnchantGlow;

public class Rules {
	private final SortingHopper plugin;
	public HashMap<Location, Inventory> rules = new HashMap<Location, Inventory>();
	
	public Rules(SortingHopper plugin) {
	      if (plugin == null) {
	            throw new IllegalArgumentException("Plugin cannot be null");
	      }
	        this.plugin = plugin;
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
		return Sorter.checkNames(inv.getName()) && rules.containsValue(inv);
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

		try {
		      File file = new File(plugin.getDataFolder(), filename + ".dat");
		      ObjectOutputStream output;
			output = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
		      HashMap<String, String> rules_serialized = new HashMap<String, String>();
		      
		      //this.rules.forEach((k,v) -> rules_serialized.put(Serialization.locationToString(k), Serialization.toBase64(v)));
		      
		      for(Location k : rules.keySet()){
		      	if(k != null){
		      		rules_serialized.put(Serialization.locationToString(k), Serialization.toBase64(rules.get(k)));
		      	}
		      }
		       
			output.writeObject(rules_serialized);
		      output.flush();
		      output.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	public void loadAndBackup(){
		
		File rulesfile = new File(plugin.getDataFolder(), "rules.dat");
		
		if(rulesfile.exists()){
			//Check if rules are loading and then overwrite backup
			if(loadRules()){
				File backupfile = new File(plugin.getDataFolder(), "rules.bak");
				
				if(backupfile.exists()){
					backupfile.delete();
				}
				
				rulesfile.renameTo(new File(plugin.getDataFolder(), "rules.bak"));
			    	saveRules();
			}
		}
	}
	
	public boolean loadRules(){
		return loadRules("rules");
	}
	@SuppressWarnings("unchecked")
	public boolean loadRules(String filename){
	      HashMap<String, String> map = new HashMap<String, String>();
		try {
		      File file = new File(plugin.getDataFolder(), filename + ".dat");
		      if(!file.exists()){
		      	SortingHopper.mclog.severe("[SortingHopper] file not found: " + filename + ".dat");
		      	return false;
		      }
		      ObjectInputStream input;
			input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
		      Object readObject = input.readObject();
		      input.close();
		      if(!(readObject instanceof HashMap)) return false;
		      map = (HashMap<String, String>) readObject;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	   
	      for(String key : map.keySet()) {
	      	try {
	      		//plugin.DebugLog("Key: " + Serialization.stringToLocation(key));
	      		this.rules.put(Serialization.stringToLocation(key), fixGlow(Serialization.inventoryFromBase64(map.get(key))));
	      	} catch  (Exception e) {
	      		e.printStackTrace();
	      		return false;
	      	}
	      	
	       
	      }
	      return true;
	}
	
	private static Inventory fixGlow(Inventory inv) {
		
		for(ItemStack item : inv.getContents()) {
			if(item!=null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
				EnchantGlow.addGlow(item);
			}
		}
		
		return inv;
	}
}
