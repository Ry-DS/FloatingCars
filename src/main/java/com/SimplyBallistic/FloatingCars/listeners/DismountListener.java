package com.SimplyBallistic.FloatingCars.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;
@Deprecated
public class DismountListener implements Listener{
	@EventHandler
	public void onDismount(EntityDismountEvent e){
		if(e.getDismounted() instanceof ArmorStand||e.getEntity() instanceof ArmorStand){
		e.getDismounted().setGravity(true);
		e.getEntity().setGravity(true);
		
		}
		
		
	}

	
}
