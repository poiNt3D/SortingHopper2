package point3d.sortinghopper2;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.sothatsit.usefulsnippets.EnchantGlow;
import point3d.sortinghopper2.SortingHopper;

public class PlayerListener implements Listener {

private final SortingHopper plugin;

public PlayerListener(SortingHopper plugin) {
      if (plugin == null) {
      	throw new IllegalArgumentException("Plugin cannot be null");
      }
      this.plugin = plugin;
}

@EventHandler
public void onPlayerInteract(PlayerInteractEvent event) {
	
	if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getPlayer().isSneaking() && event.getClickedBlock().getType().equals(Material.HOPPER)) {
		Hopper hopper = (Hopper)event.getClickedBlock().getState();
		if(Sorter.checkNames(hopper.getInventory().getName())){
			event.setCancelled(true);
			Location loc = event.getClickedBlock().getLocation();	
				if(loc != null){
				event.getPlayer().openInventory(plugin.getRules().getInv(loc));
				}
		}
	}
}
@EventHandler
public void onInventoryClick(InventoryClickEvent event) {
	if(event.getClickedInventory() == null){
		return;
	}
	
	Inventory inv = event.getInventory();
	if (plugin.getRules().checkInv(inv)) {
		event.setCancelled(true);


		
		//if player clicks sorter inventory menu
		if(Sorter.checkNames(event.getClickedInventory().getName())){
			if(event.isLeftClick()){
				inv.removeItem(event.getCurrentItem());
			} else {
				itemTagSwitch(event.getCurrentItem());
			}
		}
		//player clicks his own inventory, add item
		else if(inv.firstEmpty() >= 0 && inv.first(event.getCurrentItem()) < 0) {
			ItemStack item = new ItemStack(event.getCurrentItem().getType());
			ItemMeta meta = item.getItemMeta();
			meta.setLore(plugin.getConfig().getStringList("rule_lore"));
			item.setItemMeta(meta);
			inv.setItem(inv.firstEmpty(), item);
		}
	}
}
@EventHandler
public void onInventoryClose(InventoryCloseEvent event) {
	
	if (plugin.getConfig().getBoolean("autosave") && Sorter.checkNames(event.getInventory().getName())) {
		plugin.getRules().saveRules();
	}

}

/**
 * Called when player does a right click on an ItemStack from Sorter menu Inventory
 * Gets registred SortingTags for clicked item Material from the materialtags HashMap
 * Checks if the materialtags HashMap contains key of ItemStack's display name
 * Changes ItemStack's display name to the next key
 * Adds echant glow and configured lore for visual distinction
 * @param ItemStack
 */
public static void itemTagSwitch(ItemStack item){
	if(SortingTag.getMaterialTags(item.getType())==null){
		SortingHopper.DebugLog("no tags");
		return;
	}
	List<String> sortingtags = SortingTag.getMaterialTags(item.getType());
	SortingHopper.DebugLog(sortingtags.toString());
	ItemMeta meta = item.getItemMeta();
	if(sortingtags.contains(meta.getDisplayName())){
		int tagindex = sortingtags.indexOf(meta.getDisplayName()) + 1;
		if(tagindex >= sortingtags.size()){
			SortingHopper.DebugLog("Reset to default name");
			meta.setDisplayName(null);
			item.setItemMeta(meta);
			EnchantGlow.removeGlow(item);
		}
		else {
			SortingHopper.DebugLog("Setting name to " + sortingtags.get(tagindex)); 
			meta.setDisplayName(sortingtags.get(tagindex));
			item.setItemMeta(meta);
		}
	}
	else {
		SortingHopper.DebugLog("Seting name to tag 0s");
		meta.setDisplayName(sortingtags.get(0));
		item.setItemMeta(meta);
		EnchantGlow.addGlow(item);
	}
	
}
}
