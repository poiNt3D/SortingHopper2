package point3d.sortinghopper2;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
/**
 * Hopper event listener, manages the sorting function.
 */
public final class HopperListener implements Listener {
	private final SortingHopper plugin;      
	/**
	 * Creates a Block Break listener
	 */
	public HopperListener(SortingHopper plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
	}

	/**
	 * Inventory movement event, triggered by hopper seek.
	 *
	 * @param event the Inventory Move event object.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	public void onInventoryMoveItemEvent(InventoryMoveItemEvent event) {
		    Inventory dest = event.getDestination();

		    if(Sorter.checkNames(dest.getName())){
			  Inventory inv =  plugin.getRules().getInv(dest.getLocation());
		        if(!checkSimilarity(event.getItem().getType(), inv)) {            
		            event.setCancelled(true);                            
		        }
		    }
	}
	
	/*
	 * Checks if given inventory menu contains rules for given material
	 */
	public static boolean checkSimilarity(Material material, Inventory inv){
			
		for(ItemStack itemstack : inv.getContents()){
			if(itemstack == null){
				continue;
			}
			if(material.equals(itemstack.getType())){
				return true;
			}
			String itemname = itemstack.getItemMeta().getDisplayName().toString();
			if(SortingTag.getSortingTag(itemname) != null && SortingTag.getSortingTag(itemname).isTagged(material)){
				return true;
			}
		}
		
		return false;
	}
}
