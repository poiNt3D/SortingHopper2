package point3d.sortinghopper;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

public final class BreakListener  implements Listener{

    private final SortingHopper plugin;
    public BreakListener(SortingHopper plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        this.plugin = plugin;
    }
    
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE){
            return;
        }
        if (event.getBlock().getType() == Material.HOPPER){
            Hopper hopper = (Hopper)event.getBlock().getState();
            
            if(plugin.checkNames(hopper.getInventory().getName())){
                ItemStack drop = plugin.getItem();
                
                //Looks hacky
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);

                //-- Not working. Why?
                //event.getBlock().getDrops().clear();
                //event.getBlock().getDrops().add(drop);
                
            }
        }  
    }
    
}
