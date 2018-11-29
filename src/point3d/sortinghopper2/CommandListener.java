package point3d.sortinghopper2;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
	
	private final SortingHopper plugin;
	public CommandListener(SortingHopper plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
	}

	    
	    @Override
	    public boolean onCommand(CommandSender sender, Command command, String arg1, String[] args) {
		    if (sender instanceof Player && !sender.isOp()) {
		            Message("Only operators can use this command", sender);
		            return false;
		    }
		    if (args.length == 0) {
			    Message("give | save [name] | load [name] | reload | importminecrafttags", sender);
			    return false;
		    }
		  switch (args[0]) {
		case "give":
			if (sender instanceof Player){
				((Player) sender).getInventory().addItem(Sorter.getItem());  
			}
			break;
		case "save":
			if(args.length == 1){
				plugin.getRules().saveRules();
			      Message("Saving rules", sender);
			  }
			else if(args.length == 2 && args[1].matches("[A-Za-z0-9]+")) {
				plugin.getRules().saveRules(args[1]);
				Message("Saving rules: " + args[1], sender);
			  }
			else {
				Message("Please, enter a valid name", sender);
			  }
			break;
		case "load":
			
			boolean result = false;
			if(args.length == 1){
				result = plugin.getRules().loadRules();
			}
			else if(args.length == 2 && args[1].matches("[A-Za-z0-9]+")) {
				result = plugin.getRules().loadRules(args[1]);
			}
			else {
				Message("Please, enter a valid name", sender);
				break;
			}
			if(result){
			      Message("Rules loaded", sender);
			} else {
				Message("File doesn't exist or corrupt", sender);					
			}
			break;
		case "reload":
			Message("Reloading config", sender);
			plugin.reload();
			break;
		case "importminecrafttags":
			TagUtil.importMinecraftTags();
			break;		
		default:
			Message("give | save [name] | load [name] | reload | importminecrafttags", sender);
			break;
		  }
	    return false;
	    }
	    
	    public void Message(String message, CommandSender sender){
		    if (sender instanceof Player) {
		            Player player = (Player) sender;
		            player.sendMessage("[Sorting Hopper] " + message);
		    }
		    else {
			    plugin.getLogger().info("[Sorting Hopper] " + message);
		    }
	}

	}