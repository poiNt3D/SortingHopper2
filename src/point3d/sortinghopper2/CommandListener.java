package point3d.sortinghopper2;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import point3d.sortinghopper2.backend.RulesBackend;

public class CommandListener implements CommandExecutor {
	
	private final SortingHopper plugin;
	private final String all_commands = "give | save [name] | load [name] | backup | restore| reload | importminecrafttags";
	
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
			    Message(all_commands, sender);
			    return false;
		    }
		  switch (args[0]) {
			case "give":
				if(args.length == 1){
					if (sender instanceof Player){
						((Player) sender).getInventory().addItem(Sorter.getItem());  
					}
					else {
						Message("Please, provide player name", sender);
					}
				
				  }
				else {
					String s = args[1];
					for(int i=2; i<args.length; i++) {
						s.concat(" " + args[i]);
					}
					Player player = Bukkit.getPlayer(s);
					if(player != null) {
						player.getInventory().addItem(Sorter.getItem());
						Message("Item was given to " + s, sender);
					}
				
				  }
				break;
				
			case "save":
				if(args.length == 1){
					plugin.getRules().saveRules();
				      Message("Saving rules", sender);
				  }
				else if(args.length == 2 && args[1].matches("[a-zA-Z0-9-_]+")) {
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
			case "backup":
				Message("Writing backup number "+ RulesBackend.getCurrentBackupN(plugin.getConfig().getInt("backups_number")), sender);
				plugin.getRules().makeBackup();
				break;
			case "restore":
				int restore_status = plugin.getRules().loadBackup();
				if(restore_status != 0) {
					Message("Restored backup number" + restore_status, sender);
				}
				else {
					Message("Restore failed", sender);
				}
				break;
			case "currentbackup":
				Message("" + RulesBackend.getCurrentBackupN(plugin.getConfig().getInt("backups_number")) , sender);
				break;			
			case "calculatebackup":
				Message("" + RulesBackend.getBackupN(Integer.parseInt(args[1]),(Integer.parseInt(args[2]))) , sender);
				break;
			case "reload":
				Message("Reloading config", sender);
				plugin.reload();
				break;
			case "importminecrafttags":
				TagUtil.importMinecraftTags();
				break;		
			default:
				Message(all_commands, sender);
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