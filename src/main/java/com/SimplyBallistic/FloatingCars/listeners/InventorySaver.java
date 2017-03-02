package com.SimplyBallistic.FloatingCars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.SimplyBallistic.FloatingCars.files.PlayerData;

public class InventorySaver implements Listener {
	@EventHandler
	public void saveInv(InventoryCloseEvent e){
		
		if(e.getInventory().getName().contains("Car Inventory"))
			PlayerData.setInventory(e.getPlayer().getUniqueId(), e.getInventory(),ChatColor.stripColor(e.getInventory().getName().split(":")[1].trim()));
		
		
			
			
		
		
		
	}

}
