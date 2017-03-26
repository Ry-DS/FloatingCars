package com.SimplyBallistic.FloatingCars.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DamageListener implements Listener {
@EventHandler
public void onFall(EntityDamageEvent e){
	if(!(e.getEntity() instanceof Player)||!(e.getEntity().getVehicle() instanceof ArmorStand))return;	
	if(e.getCause()==DamageCause.FALL)e.setCancelled(true);
	
	
}
}
