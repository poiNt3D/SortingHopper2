package point3d.sortinghopper2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.sothatsit.usefulsnippets.EnchantGlow;

public class Sorter {

	private static String sorter_primaryname = "Sorter";
	private static List<String> sorter_names =  new ArrayList<>(Arrays.asList(sorter_primaryname));
	private static List<String> sorter_lore =  new ArrayList<>();
	private static boolean check_lore = false;
	private static int invSize = 9;
	

	private final SortingHopper plugin;
	public Sorter(SortingHopper plugin) {
	      if (plugin == null) {
	            throw new IllegalArgumentException("Plugin cannot be null");
	      }
	        this.plugin = plugin;
	        this.reload();

	}
	public void reload(){
 		sorter_primaryname = plugin.getConfig().getStringList("names").get(0);
 	  	sorter_names = colorize(plugin.getConfig().getStringList("names"));
 	  	sorter_lore =  colorize(plugin.getConfig().getStringList("lore"));
 	  	check_lore =  plugin.getConfig().getBoolean("check_lore");
 	  	
 	  	if(plugin.getConfig().getInt("inventory_size") > 0){
 	  		invSize = plugin.getConfig().getInt("inventory_size");
 	  	}
	}

	/**
	 * Check the given string against configured names
	 *
	 * @param name The name string to test
	 */
//	public static boolean checkNames(String name){
//		return sorter_primaryname.equals(name) || sorter_names.contains(name);
//	}
//	
	
	/**
	 * Return an ItemStack with custom item meta
	 *
	 * @return an ItemStack with custom meta
	 */
	public static ItemStack getItem() {
		ItemStack item = new ItemStack(Material.HOPPER);
		EnchantGlow.addGlow(item);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(sorter_names.get(0));
		meta.setLore(sorter_lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static boolean checkItem(ItemStack item) {
		if(item.getType().equals(Material.HOPPER) && item.hasItemMeta()) {
			if(check_lore) {
				return item.getItemMeta().getLore().equals(sorter_lore);
			}
			else {
				return sorter_names.contains(item.getItemMeta().getDisplayName());
			}
		}
		else return false;
	}
	/**
	 * Create an Inventory with custom inventory name and configured size
	 * @return an Inventory with custom inventory name and configured size
	 */
 	public static Inventory createInv(){
		Inventory inv = Bukkit.createInventory(null, invSize, sorter_names.get(0));
		return inv;
 	}
 	
	
	public static List<String> colorize(List<String> l){
		List<String> r = new ArrayList<String>();
		for(String s : l){
			if(s instanceof String)r.add(ChatColor.translateAlternateColorCodes('&', "&r" + s));
			
		}
		return r;
	
	}

}
