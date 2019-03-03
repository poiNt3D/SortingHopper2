package point3d.sortinghopper2.backend;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

public interface RulesBackend {
	
	//Days since January 1, 1970
	final static int backup_d = (int) System.currentTimeMillis() / 86400000;
	
	public boolean save(HashMap<Location, Inventory> rules, String name);
	public HashMap<Location, Inventory> load(String name);
	public boolean makeBackup(HashMap<Location, Inventory> rules);
	public int loadBackup();

	//Determine today's backup number
	public static int getCurrentBackupN(int backups_max) {
		if(backups_max == 0) return 0;
		return backup_d % backups_max + 1;
	}
	
	//Calculate backup number n days earlier
	public static int getBackupN(int n, int backups_max) {
		if(backups_max == 0) return 0;
		int current = backup_d % backups_max + 1;
		if(current-n > 0) return current-n;
		else return backups_max+(current-n);
	}
}
 