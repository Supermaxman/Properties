package me.supermaxman.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;



public class Properties extends JavaPlugin {
	public static Properties plugin;
    public static FileConfiguration conf;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static World plots;
	static HashMap<String, ArrayList<House>> houses = new HashMap<String, ArrayList<House>>();
	static HashMap<String, HouseType> typelist = new HashMap<String, HouseType>();
	static ArrayList<String> available = new ArrayList<String>();
	static ArrayList<Integer> ids = new ArrayList<Integer>();
	
    public static Economy economy = null;
    
	public void onEnable() {
		plugin = this;
		if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		saveDefaultConfig();
		setupWorld();
		readHouseTypes();
		loadHouses();
		getServer().getPluginManager().registerEvents(new PropertiesListener(), plugin);
        getCommand("properties").setExecutor(new PropertiesExecutor(this));
        getCommand("prop").setExecutor(new PropertiesExecutor(this));
		log.info(getName() + " has been enabled.");
		startEarning();
	}
	
	public void onDisable() {
		saveHouses();
		log.info(getName() + " has been disabled.");
	}
	
	@SuppressWarnings("unchecked")
	static void loadHouses() {
		try {
			houses = (HashMap<String, ArrayList<House>>) new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "houses.ser")).readObject();
			available = (ArrayList<String>) new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "available.ser")).readObject();
			ids = (ArrayList<Integer>) new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "ids.ser")).readObject();
			new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "houses.ser")).close();
			new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "available.ser")).close();
			new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + File.separator + "ids.ser")).close();
		} catch (Exception e) {
			log.warning("[" + plugin.getName() + "] Files could not be read! All files are now ignored.");
		}
		
	}
	
	static void saveHouses(){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(plugin.getDataFolder() + File.separator + "houses.ser"));
			oos.writeObject(houses);
			oos.close();
			ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(plugin.getDataFolder() + File.separator + "available.ser"));
			oos2.writeObject(available);
			oos2.close();
			ObjectOutputStream oos3 = new ObjectOutputStream(new FileOutputStream(plugin.getDataFolder() + File.separator + "ids.ser"));
			oos3.writeObject(ids);
			oos3.close();
		} catch (Exception e) {
			log.warning("[" + plugin.getName() + "] Files could not be saved!");
		}
	}
	
    public static void readHouseTypes() {
		conf = plugin.getConfig();
        if (conf.isConfigurationSection("settings")) {
            for (Map.Entry<String, Object> entry : conf.getConfigurationSection("settings.housetypes").getValues(false).entrySet()) {
                ConfigurationSection cs = conf.getConfigurationSection("settings.housetypes." + entry.getKey());
                typelist.put(entry.getKey(), new HouseType(entry.getKey(), cs.getString("schematic"), cs.getInt("price"), cs.getInt("earnings")));
                
            }
            
        }
    }
    
	
	
	public static String makeString(Location loc) {
		return loc.getWorld().getName() + "&&" + loc.getBlockX() + "&&" + loc.getBlockY() + "&&" + loc.getBlockZ(); 
	}
	
	public static Location makeLocation(String s) {
		String[] loc = s.split("&&");
		
		return new Location(Properties.plugin.getServer().getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3])); 
	}
	
	public void setupWorld() {
		if(plugin.getServer().getWorld("plots")!=null) {
			//plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "mv create plots normal -g Properties");
			plots = plugin.getServer().getWorld("plots");
			plots.setPVP(false);
		}
	}
	
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        int path = conf.getInt("settings.plots.PathSize");
        int plotx = conf.getInt("settings.plots.PlotSize");
        int plotz = conf.getInt("settings.plots.PlotSize");
        int podzialx = 1;
        int podzialz = 1;
        int wysokosc = 32;
        String pathMaterial = conf.getString("settings.plots.Path_Material");
        String plotMaterial = conf.getString("settings.plots.Plot_Material");
        String podMaterial = conf.getString("settings.plots.Under_Plot_Material");
        return new PlotGenerator(path, plotx, plotz,wysokosc,podzialx,podzialz,pathMaterial,plotMaterial,podMaterial);
    }

    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    public static void addHouse(String p, House h) {
    	if(!houses.containsKey(p)) {
    		houses.put(p, new ArrayList<House>());
    	}
    	houses.get(p).add(h);
    	Properties.saveHouses();
    }
    
    public static void removeHouse(String p, House h) {
    	houses.get(p).remove(h);
    	h.wipePlot();
    	Properties.saveHouses();
    }
    
    public static void tpTimer(final Player p, final House h) {
    	final Location loc = p.getLocation();
		Properties.plugin.getServer().getScheduler().scheduleSyncDelayedTask(Properties.plugin, new Runnable() {
			  public void run() {
				  if(p!=null && h!=null && loc !=null) {
					  	if(p.getLocation().getBlockX()==loc.getBlockX()&&
					  		p.getLocation().getBlockY()==loc.getBlockY()&&
					  		p.getLocation().getBlockZ()==loc.getBlockZ()) {
							p.sendMessage(PropertiesExecutor.getColoredText(Properties.conf.getString("settings.messages.TeleportMessage")));
		        			p.teleport(Properties.makeLocation(h.getPlot()));
					  	}else {
							p.sendMessage(PropertiesExecutor.getColoredText(Properties.conf.getString("settings.messages.TeleportInterupt")));
					  	}
				  }
			  }
		}, 140);
    }
    
    public static void startEarning() {
		Properties.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(Properties.plugin, new Runnable() {
			  public void run() {
				  for(String s : houses.keySet()) {
					  for(House h : houses.get(s)) {
		        			Properties.economy.depositPlayer(s, h.getType().getEarnings());
					  }
				  }
			  }
		}, Properties.conf.getInt("settings.housesettings.earningstime")*20, Properties.conf.getInt("settings.housesettings.earningstime")*20);
    }
}