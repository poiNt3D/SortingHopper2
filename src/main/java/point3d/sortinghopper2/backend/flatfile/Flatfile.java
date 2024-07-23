package point3d.sortinghopper2.backend.flatfile;

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

import me.sothatsit.usefulsnippets.GlowHelperUtil;
import point3d.sortinghopper2.Serialization;
import point3d.sortinghopper2.SortingHopper;
import point3d.sortinghopper2.backend.RulesBackend;

public class Flatfile implements RulesBackend {

	private final SortingHopper plugin;
	private final int backups_max;

	public Flatfile(SortingHopper plugin) {
		this.plugin = plugin;
		backups_max = plugin.getConfig().getInt("backups_number");
	}

	@Override
	public boolean save(HashMap<Location, Inventory> rules, String name) {
		try {
		      File file = new File(plugin.getDataFolder(), name + ".dat");
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
		      return true;
		      
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Location, Inventory> load(String name) {
	      HashMap<String, String> rawmap = new HashMap<>();
	      HashMap<Location, Inventory> resultmap = new HashMap<>();
		try {
		      File file = new File(plugin.getDataFolder(), name + ".dat");
		      if(!file.exists()){
		      	SortingHopper.mclog.severe("[SortingHopper] file not found: " + name + ".dat");
		      	return null;
		      }
		      ObjectInputStream input;
			input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
		      Object readObject = input.readObject();
		      input.close();
		      if(!(readObject instanceof HashMap)) return null;
		      rawmap = (HashMap<String, String>) readObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	   
	      for(String key : rawmap.keySet()) {
	      	try {
	      		//plugin.DebugLog("Key: " + Serialization.stringToLocation(key));
	      		resultmap.put(Serialization.stringToLocation(key), fixGlow(Serialization.inventoryFromBase64(rawmap.get(key))));
	      	} catch  (Exception e) {
	      		e.printStackTrace();
	      		return null;
	      	}
	      	
	       
	      }
	      return resultmap;
	}
	
	private static Inventory fixGlow(Inventory inv) {
		
		for(ItemStack item : inv.getContents()) {
			if(item!=null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
				GlowHelperUtil.addGlow(item);
			}
		}
		
		return inv;
	}

	@Override
	public boolean makeBackup(HashMap<Location, Inventory> rules) {
		return save(rules, "rules_backup_" + RulesBackend.getCurrentBackupN(backups_max));
	}

	@Override
	public int loadBackup() {
		for(int i = 1; i <= backups_max; i++) {
			int backupN =  RulesBackend.getBackupN(i, backups_max);
			HashMap<Location, Inventory> result = load("rules_backup_" +  backupN);
			if(result != null) {
				return backupN;
			}
			else {
				plugin.getLogger().warning("Unable to load backup " + i + ", trying next one...");
			}
		}
		return 0;
	}

}
