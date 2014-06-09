package me.supermaxman.properties;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;

public class House implements Serializable{
	
	private static final long serialVersionUID = -563474290277795935L;

	private String owner;
	
    private HouseType type;
    
    private final String world = "plots";
    
    private String plot;
    
    private int id;
    
    private ArrayList<String> allowedIn;
    
    public House(String owner, String type) {
    	this.setOwner(owner);
    	this.setType(findType(type));
    	this.setId(getRandomId());
    	Properties.addHouse(owner, this);
    	buildPlot();
    	setAllowedIn(new ArrayList<String>());
    }

    
    public boolean isInside(Location l) {
    	Location loc = Properties.makeLocation(this.getPlot());
    	Location loc2 = Properties.makeLocation(this.getPlot()).add(Properties.conf.getInt("settings.plots.PlotSize")-1, 0, Properties.conf.getInt("settings.plots.PlotSize")-1);
    	int x = loc.getBlockX();
    	int z = loc.getBlockZ();
    	int x2 = loc2.getBlockX();
    	int z2 = loc2.getBlockZ();
    	
    	if(l.getBlockX()>=x && l.getBlockX()<=x2) {
        	if(l.getBlockZ()>=z && l.getBlockZ()<=z2) {
        		if(l.getBlockY()>=loc.getBlockY()) {
        			return true;
        		}
    		}
    	}
    	
    	return false;
    }
    
    
    public int getRandomId() {
    	Random r = new Random();
    	int i = (r.nextInt(10))+(r.nextInt(10)*10) + (r.nextInt(10)*100)  + (r.nextInt(10)*1000); 
    	while(Properties.ids.contains(i)) {
    		i = (r.nextInt(10))+(r.nextInt(10)*10) + (r.nextInt(10)*100)  + (r.nextInt(10)*1000); 
    	}
    	Properties.ids.add(i);
    	return i; 
    }
    
    public void buildPlot() {
    	setPlot(Properties.available.get(0));
    	setupHologram();
    	Properties.available.remove(plot);
    	Location loc = Properties.makeLocation(plot);
    	try {
			spawnHouse(loc.getWorld(), new File(Properties.plugin.getDataFolder() + File.separator + type.getSchematic()), new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		} catch (DataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	public void addAllowedIn(String in) {
		this.getAllowedIn().add(in);
	}
	public void removeAllowedIn(String in) {
		this.getAllowedIn().remove(in);
	}
    
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public void setupHologram() {
		HolographicDisplaysAPI.createHologram(Properties.plugin, Properties.makeLocation(this.getPlot()).add((Properties.conf.getInt("settings.plots.PlotSize")/2), 2, 0.5), 
				ChatColor.AQUA+"Owner:",
				ChatColor.GOLD+this.getOwner(),
				ChatColor.AQUA+"Type:",
				ChatColor.GOLD+this.getType().getType());
		
	}
	
	public World getWorld() {
		return Properties.plugin.getServer().getWorld(world);
	}
	
	public HouseType getType() {
		return type;
	}

	public void setType(HouseType type) {
		this.type = type;
	}
    
	public HouseType findType(String s) {
		return Properties.typelist.get(s);//more code
	}


	public String getPlot() {
		return plot;
	}


	public void setPlot(String plot) {
		this.plot = plot;
	}
    @SuppressWarnings({ "deprecation" })
	private void spawnHouse(World world, File file, Vector origin) throws DataException, IOException, MaxChangedBlocksException{
        EditSession es = new EditSession(new BukkitWorld(world), 999999999);
        CuboidClipboard cc = CuboidClipboard.loadSchematic(file);
        cc.paste(es, origin, false);
    }


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public ArrayList<String> getAllowedIn() {
		return allowedIn;
	}


	public void setAllowedIn(ArrayList<String> allowedIn) {
		this.allowedIn = allowedIn;
	}
	
	public void wipePlot() {
    	Location loc = Properties.makeLocation(plot);
    	Location l = Properties.makeLocation(plot).add((Properties.conf.getInt("settings.plots.PlotSize")/2), 2, 0.5);
		for(Hologram h : HolographicDisplaysAPI.getHolograms(Properties.plugin)) {
			if(h.getX()==l.getX() && h.getY() == l.getY() && h.getZ() == l.getZ()) {
				h.delete();
				break;
			}
		}
    	Properties.available.add(plot);
    	try {
			spawnHouse(loc.getWorld(), new File(Properties.plugin.getDataFolder() + File.separator + "wipe.schematic"), new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		} catch (DataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
