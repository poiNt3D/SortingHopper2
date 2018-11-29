package point3d.sortinghopper2;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public final class PlaceListener  implements Listener{

	    private final SortingHopper plugin;
	    public PlaceListener(SortingHopper plugin) {
	        if (plugin == null) {
	            throw new IllegalArgumentException("Plugin cannot be null");
	        }
	        this.plugin = plugin;
	    }
	    
	    @EventHandler
	    public void onBlockPlace(BlockPlaceEvent event){
		    if(event.getItemInHand().getType().equals(Material.HOPPER) && plugin.getConfig().getStringList("names").contains((event.getItemInHand().getItemMeta().getDisplayName()))){
			    event.setCancelled(true);
			    event.getPlayer().sendMessage("[SortingHopper] Wrong item. Use crafting recipe.");
		    }
	    }
}
