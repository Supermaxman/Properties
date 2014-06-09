package me.supermaxman.properties;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PropertiesExecutor extends BaseExecutor {
    @SuppressWarnings("deprecation")
	@Override
    protected void run(CommandSender sender, String[] args) {	
    	if(args.length > 0){
        	if(args[0].equalsIgnoreCase("buy")) {
        		if(!(sender instanceof Player))return;
        		Player p = (Player) sender;
        		if(args.length>1) {
    	        	if(Properties.typelist.containsKey(args[1])) {
        				if(Properties.economy.has(p.getName(), Properties.typelist.get(args[1]).getPrice())) {
        					int i = 0;
        					if(Properties.houses.containsKey(p.getName()))i=Properties.houses.get(p.getName()).size();
        					if(i<Properties.conf.getInt("settings.housesettings.maxhouses")) {
	            	        	new House(p.getName(), args[1]);
	            	        	Properties.economy.withdrawPlayer(p.getName(), Properties.typelist.get(args[1]).getPrice());
	        					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.PurchaseMessage")));
        					}else {
        						p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.MaxHouseLimitMessage")));
        					}
        				}else {
        					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoBalanceMessage")));
        				}
        			}else {
    					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.WrongTypeMessage")));
        			}
        		}else {
					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.CommandFormatIncorrectMessage")));
        		}
        	}else if(args[0].equalsIgnoreCase("sell")) {
        		if(!(sender instanceof Player))return;
        		Player p = (Player) sender;
        		if(args.length>1) {
    	        	if(Properties.houses.containsKey(p.getName())) {
    	        		House h = null;
    	        		for(House x : Properties.houses.get(p.getName())) {
    	        			if(String.valueOf(x.getId()).equals(args[1])) {
    	        				h = x;
    	        				break;
    	        			}
    	        		}
    	        		if(h!=null) {
    	        			Properties.removeHouse(p.getName(), h);
    	        			Properties.economy.depositPlayer(p.getName(), h.getType().getPrice()/2);
        					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.SaleMessage")));
    	        		}else {
        					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.WrongIdMessage")));
    	        		}
        			}else {
    					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoPropertiesMessage")));
        			}
        		}else {
					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.CommandFormatIncorrectMessage")));
        		}
        	}else if(args[0].equalsIgnoreCase("list")) {
        		if(!(sender instanceof Player))return;
        		Player p = (Player) sender;
        		if(Properties.houses.containsKey(p.getName())) {
        			for(House h : Properties.houses.get(p.getName())) {
        				p.sendMessage(h.getId()+" : "+ h.getType().getType());
        			}
        		}else {
					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoPropertiesMessage")));
        		}
        	}else if(args[0].equalsIgnoreCase("invite")) {
        		if(!(sender instanceof Player))return;
        		if(args.length>3) {
	        		Player p = (Player) sender;
	        		if(Properties.houses.containsKey(p.getName())) {
	        			House h = null;
	        			for(House x : Properties.houses.get(p.getName())) {
	        				if(String.valueOf(x.getId()).equals(args[3])) {
	        					h = x;
	        					break;
	        				}
	        			}
	        			if(h!=null) {
	        				h.addAllowedIn(args[2]);
	    					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.InviteMessage")));
	        			}else {
        					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.WrongIdMessage")));
	        			}
	        		}else {
    					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoPropertiesMessage")));
	        		}
        		}else {
					sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.CommandFormatIncorrectMessage")));
        		}
        		
        	}else if(args[0].equalsIgnoreCase("grant")) {
        		if((sender.isOp())) {
	        		if(args.length>2) {
	        			if(Properties.typelist.containsKey(args[2])) {
        					int i = 0;
        					if(Properties.houses.containsKey(args[1]))i=Properties.houses.get(args[1]).size();
        					if(i<Properties.conf.getInt("settings.housesettings.maxhouses")) {
                	        	House h = new House(args[1], args[2]);
                	        	if(args.length>3) {
                	        		if(args[3].equalsIgnoreCase("tp")) {
    	            	        		Player p = Properties.plugin.getServer().getPlayerExact(args[1]);
    	            	        		if(p != null) {
    	            	        			p.teleport(Properties.makeLocation(h.getPlot()));
    	            	        		}
                	        		}
                	        	}
        					}else {
        						sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.MaxHouseLimitMessage")));
        					}
	        			}else {
	    					sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.WrongTypeMessage")));
	        			}
	        		}else {
						sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.CommandFormatIncorrectMessage")));
	        		}
        		}else {
					sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoPermissionsMessage")));
        		}
        	}else if(args[0].equalsIgnoreCase("tp")) {
        		if(!(sender instanceof Player))return;
        		if(args.length>1) {
	        		Player p = (Player) sender;
	        		if(Properties.houses.containsKey(p.getName())) {
	        			House h = null;
	        			for(House x : Properties.houses.get(p.getName())) {
	        				if(String.valueOf(x.getId()).equals(args[1])) {
	        					h = x;
	        					break;
	        				}
	        			}
	        			if(h!=null) {
        					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.BeforeTeleportMessage")));
	        				Properties.tpTimer(p, h);
	        			}else {
        					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.WrongIdMessage")));
	        			}
	        		}else {
    					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoPropertiesMessage")));
	        		}
        		}else {
					sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.CommandFormatIncorrectMessage")));
        		}
        	}else if(args[0].equalsIgnoreCase("reload")) {
        		if(sender.isOp()) {
        			Properties.readHouseTypes();
					sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.ReloadMessage")));
        		}else {
					sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoPermissionsMessage")));
        		}
        	}else if(args[0].equalsIgnoreCase("revoke")) {
        		if(sender.isOp()) {
        			if(args.length>1) {
        				if(Properties.houses.containsKey(args[1])) {
        					for(House h : Properties.houses.get(args[1])) {
        						Properties.removeHouse(args[1], h);
        					}
        					sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.RevokeMessage")));
        					
        				}else {
        					sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoPropertiesMessage")));
        				}
    					
        			}else {
            			sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.CommandFormatIncorrectMessage")));
        			}
        		}else {
					sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoPermissionsMessage")));
        		}
        	}else if(args[0].equalsIgnoreCase("help")) {
    			sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.HelpMessage")));
        	}else {
    			sender.sendMessage(getColoredText(Properties.conf.getString("settings.messages.CommandFormatIncorrectMessage")));
        	}
        }else {
    		if(!(sender instanceof Player))return;
    		Player p = (Player) sender;
    		setupMenu(p);
        }
    }
    public static String getColoredText(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
     }
    public PropertiesExecutor(Properties pl) {
        super(pl);
    }
    
    public static int toMultiple(int i){
    	while(i % 9 != 0) {
    		i++;
    	}
    	return i;
    }
    
	static void setupMenu(Player p) {
		IconMenu menu = new IconMenu(ChatColor.BLUE+"Properties Menu", 9, new IconMenu.OptionClickEventHandler() {
	        @Override
	        public void onOptionClick(IconMenu.OptionClickEvent event) {
	        	if(event.getName().equals(ChatColor.GOLD+"Purchase")) {
	        		setupPurchaseMenu(event.getPlayer());
	        	}else if(event.getName().equals(ChatColor.GOLD+"Sell")) {
	        		setupSellMenu(event.getPlayer());
	        	}else if(event.getName().equals(ChatColor.DARK_PURPLE+"Teleport")) {
	        		setupTeleportMenu(event.getPlayer());
	        	}else if(event.getName().equals(ChatColor.AQUA+"Properties")) {
	        		setupPropertiesMenu(event.getPlayer());
	        	}
	            event.setWillClose(false);
	        }
	    }, Properties.plugin)
		.setOption(0, new ItemStack(Material.DIAMOND), ChatColor.GOLD+"Purchase", ChatColor.BLUE+"Buy properties")
		.setOption(1, new ItemStack(Material.EMERALD), ChatColor.GOLD+"Sell", ChatColor.BLUE+"Sell properties")
		.setOption(2, new ItemStack(Material.ENDER_PEARL), ChatColor.DARK_PURPLE+"Teleport", ChatColor.BLUE+"Teleport to properties")
		.setOption(3, new ItemStack(Material.BOOK), ChatColor.AQUA+"Properties", ChatColor.BLUE+"See properties");
		menu.open(p);
	}
    
	static void setupPurchaseMenu(Player p) {
		IconMenu menu = new IconMenu(ChatColor.GOLD+"Purchase", toMultiple(Properties.typelist.size()), new IconMenu.OptionClickEventHandler() {
	        @Override
	        public void onOptionClick(IconMenu.OptionClickEvent event) {
	        	if(event.willClose())return;
	        	String n = ChatColor.stripColor(event.getName());
	        	Player p = event.getPlayer();
				if(Properties.economy.has(p.getName(), Properties.typelist.get(n).getPrice())) {
					int i = 0;
					if(Properties.houses.containsKey(p.getName()))i=Properties.houses.get(p.getName()).size();
					if(i<Properties.conf.getInt("settings.housesettings.maxhouses")) {
	    	        	new House(p.getName(), n);
        	        	Properties.economy.withdrawPlayer(p.getName(), Properties.typelist.get(n).getPrice());
						p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.PurchaseMessage")));
	    	            event.setWillClose(true);
	    	            return;
					}else {
						p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.MaxHouseLimitMessage")));
					}
				}else {
					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.NoBalanceMessage")));
				}
	            event.setWillClose(false);
	        }
	    }, Properties.plugin);
		int i = 0;
		for(HouseType h : Properties.typelist.values()) {
			menu.setOption(i, new ItemStack(Material.BRICK), ChatColor.DARK_PURPLE+""+h.getType(), ChatColor.GOLD+""+h.getPrice());
			i++;
		}
		menu.open(p);
	}
    
	static void setupSellMenu(Player p) {
		String s = p.getName();
		IconMenu menu = new IconMenu(ChatColor.GOLD+"Sell", toMultiple(Properties.conf.getInt("settings.housesettings.maxhouses")), new IconMenu.OptionClickEventHandler() {
	        @Override
	        public void onOptionClick(IconMenu.OptionClickEvent event) {
	        	if(event.willClose())return;
	        	String n = ChatColor.stripColor(event.getName());
	        	Player p = event.getPlayer();
	        	if(Properties.houses.containsKey(p.getName())) {
	        		House h = null;
	        		for(House x : Properties.houses.get(p.getName())) {
	        			if(String.valueOf(x.getId()).equals(n)) {
	        				h = x;
	        				break;
	        			}
	        		}
	        		if(h!=null) {
	        			Properties.removeHouse(p.getName(), h);
	        			Properties.economy.depositPlayer(p.getName(), h.getType().getPrice()/2);
    					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.SaleMessage")));
	    	            event.setWillClose(true);
	    	            return;
	        		}
    			}
	            event.setWillClose(false);
	        }
	    }, Properties.plugin);
		if(Properties.houses.containsKey(s)) {
			int i = 0;
			for(House h : Properties.houses.get(s)) {
				menu.setOption(i, new ItemStack(Material.BRICK), ChatColor.DARK_PURPLE+""+h.getId(), ChatColor.AQUA+""+h.getType().getType(),ChatColor.GOLD+""+(h.getType().getPrice()/2));
				i++;
			}
		}
		menu.open(p);
	}
	
	static void setupTeleportMenu(Player p) {
		String s = p.getName();
		IconMenu menu = new IconMenu(ChatColor.DARK_PURPLE+"Teleport", toMultiple(Properties.conf.getInt("settings.housesettings.maxhouses")), new IconMenu.OptionClickEventHandler() {
	        @Override
	        public void onOptionClick(IconMenu.OptionClickEvent event) {
	        	if(event.willClose())return;
	        	String n = ChatColor.stripColor(event.getName());
	        	Player p = event.getPlayer();
        		if(Properties.houses.containsKey(p.getName())) {
        			House h = null;
        			for(House x : Properties.houses.get(p.getName())) {
        				if(String.valueOf(x.getId()).equals(n)) {
        					h = x;
        					break;
        				}
        			}
        			if(h!=null) {
    					p.sendMessage(getColoredText(Properties.conf.getString("settings.messages.BeforeTeleportMessage")));
        				Properties.tpTimer(p, h);
	    	            event.setWillClose(true);
	    	            return;
        			}
        		}
	            event.setWillClose(false);
	        }
	    }, Properties.plugin);
		if(Properties.houses.containsKey(s)) {
			int i = 0;
			for(House h : Properties.houses.get(s)) {
				menu.setOption(i, new ItemStack(Material.BRICK), ChatColor.DARK_PURPLE+""+h.getId(), ChatColor.AQUA+""+h.getType().getType(),ChatColor.GOLD+""+h.getType().getPrice());
				i++;
			}
		}
		menu.open(p);
	}
	
	static void setupPropertiesMenu(Player p) {
		String s = p.getName();
		IconMenu menu = new IconMenu(ChatColor.AQUA+"Properties", toMultiple(Properties.conf.getInt("settings.housesettings.maxhouses")), new IconMenu.OptionClickEventHandler() {
	        @Override
	        public void onOptionClick(IconMenu.OptionClickEvent event) {
	            event.setWillClose(false);
	        }
	    }, Properties.plugin);
		if(Properties.houses.containsKey(s)) {
			int i = 0;
			for(House h : Properties.houses.get(s)) {
				menu.setOption(i, new ItemStack(Material.BRICK), ChatColor.DARK_PURPLE+""+h.getId(), ChatColor.AQUA+""+h.getType().getType(),ChatColor.GOLD+""+h.getType().getPrice());
				i++;
			}
		}
		menu.open(p);
	}
}
