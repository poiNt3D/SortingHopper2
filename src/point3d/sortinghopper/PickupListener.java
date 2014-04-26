package point3d.sortinghopper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

public class PickupListener implements Listener {
        
    private final SortingHopper plugin;
    public PickupListener(SortingHopper plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        this.plugin = plugin;
        }


    
@EventHandler
public void onInventoryPickupEvent(InventoryPickupItemEvent event) {

    if(plugin.checkNames(event.getInventory().getName())) {
        event.setCancelled(true);
    }
}
}
