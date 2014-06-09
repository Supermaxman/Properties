package me.supermaxman.properties;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

	
public class PropertiesListener implements Listener {
	
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent e) {
		if(e.getEntity().getWorld().getName().equals("plots")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getBlock().getWorld().getName().equals("plots")) {
			if(!e.getPlayer().isOp()){
				boolean cancel = true;
				for(String s : Properties.houses.keySet()) {
					for(House h : Properties.houses.get(s)) {
						if(h.isInside(e.getBlock().getLocation())) {
							if(e.getPlayer().getName().equals(h.getOwner())) {
								if(Properties.conf.getBoolean("settings.plots.PlotBuild")) { //buildon
									cancel = false;
									break;
								}else {
									break;
								}
							}else {
								break;
							}
						}
					}
				}
				e.setCancelled(cancel);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getPlayer().getWorld().getName().equals("plots")) {
			if(!e.getPlayer().isOp()){
				boolean cancel = true;
				for(String s : Properties.houses.keySet()) {
					for(House h : Properties.houses.get(s)) {
						Location loc = e.getPlayer().getLocation();
						if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&e.getAction().equals(Action.LEFT_CLICK_BLOCK))loc = e.getClickedBlock().getLocation();
						if(h.isInside(loc)) {
							if(e.getPlayer().getName().equals(h.getOwner())) {
								cancel = false;
								break;
							}else {
								break;
							}
						}
					}
				}
				e.setCancelled(cancel);
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getBlock().getWorld().getName().equals("plots")) {
			if(!e.getPlayer().isOp()){
				boolean cancel = true;
				for(String s : Properties.houses.keySet()) {
					for(House h : Properties.houses.get(s)) {
						if(h.isInside(e.getBlock().getLocation())) {
							if(e.getPlayer().getName().equals(h.getOwner())) {
								if(Properties.conf.getBoolean("settings.plots.PlotBuild")) { //buildon
									cancel = false;
									break;
								}else {
									break;
								}
							}else {
								break;
							}
						}
					}
				}
				e.setCancelled(cancel);
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(p.getWorld().getName().equals("plots")) {
			if(!p.isOp()){
				boolean cancel = false;
				for(String s : Properties.houses.keySet()) {
					for(House h : Properties.houses.get(s)) {
						if(h.isInside(p.getLocation())) {
							if(!p.getName().equals(h.getOwner())) {
								if(!h.getAllowedIn().contains(p.getName())) {
									cancel = true;
								}
								break;	
							}else {
								break;
							}
						}
					}
				}
				Location l = new Location(e.getFrom().getWorld(), e.getFrom().getBlockX(), e.getFrom().getBlockY(), e.getFrom().getBlockZ());
				l.setDirection(p.getLocation().getDirection());
				if(cancel)e.setTo(l);
			}
		}
	}
	
	
	
}
