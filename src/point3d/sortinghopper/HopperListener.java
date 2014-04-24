package point3d.sortinghopper;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class HopperListener implements Listener {
        
    private final SortingHopper plugin;
    public HopperListener(SortingHopper plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        this.plugin = plugin;
    }


    
@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
public void onInventoryMoveItemEvent(InventoryMoveItemEvent event) {
    Inventory initiator = event.getInitiator();
    Inventory dest = event.getDestination();
    Inventory source = event.getSource();
    
    
    //Prevent items from being pulled out by other hopper
if(plugin.getConfig().getBoolean("preventitempull")){
    if(plugin.checkNames(source.getName()) && initiator != source){ 
        event.setCancelled(true);        
    }
}

    if(plugin.checkNames(initiator.getName())){   
        
        if(!initiator.containsAtLeast(event.getItem(),1)) {
            
            event.setCancelled(true);
            
            //Try to move items in other slots
            if(dest != initiator) {
                for(int n=1; n < initiator.getSize(); n++) {
                    if(this.MoveItem(initiator, n, dest) == true){break;}
               }                
            }            
        }
    }
}

    public boolean MoveItem(Inventory initiator, Integer n, Inventory dest) {
        
        ItemStack item = initiator.getItem(n);
        if(item == null || !initiator.containsAtLeast(item, 2)){
            return false;
        }

        ItemStack newitem = item.clone();
        newitem.setAmount(1);
        if (!dest.addItem(newitem).isEmpty()){return false;}
        Integer amount = item.getAmount();
        if(amount > 1){

            Integer newamount = amount - 1;
            item.setAmount(newamount);
            return true;
            } else {
            
            initiator.removeItem(item);
        }
            
        return false;

    }
}
