package me.supermaxman.properties;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class OpHouseExecutor extends BaseExecutor {
    @Override
    protected void run(CommandSender sender, String[] args) {	
    	if(sender.isOp()) {
	    	if(args.length > 1){
	        	if(Properties.typelist.containsKey(args[1])) {
		        	new House(args[0], args[1]);
	        	}else {
		        	sender.sendMessage(ChatColor.RED+"Command used incorrectly, unknown house type.");
	        	}
	        }else {
	        	sender.sendMessage(ChatColor.RED+"Command used incorrectly, please type /addhouse [name] [housetype]");
	        }
    	}else {
    		sender.sendMessage(ChatColor.RED+"You are not op.");
    	}
    }

    public OpHouseExecutor(Properties pl) {
        super(pl);
    }
}
