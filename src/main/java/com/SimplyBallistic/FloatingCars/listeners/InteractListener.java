package com.SimplyBallistic.FloatingCars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.FloatingCars.reflection.PacketListener;

public class InteractListener implements Listener {
@EventHandler
public void fuelAddEvent(PlayerInteractEvent e){
	
	
	if(e.getPlayer().getVehicle() instanceof ArmorStand){
		FCMain.cars.forEach((hc)->{
			if(e.getPlayer().getVehicle().getUniqueId().equals(hc.getCar().getUniqueId())
					&&e.getPlayer().getInventory().getItemInMainHand().getType().equals(hc.getFuel())){
				if(PacketListener.fuel.get(hc)+hc.getFuelTime()>hc.getCapacity()){
					FCMain.getZotLib().getPacketLibrary().getTitleManager().sendTitle(e.getPlayer(), "", 10, 10, 10);
				FCMain.getZotLib().getPacketLibrary().getTitleManager().sendSubTitle(e.getPlayer(), ChatColor.RED+"Your tank is full!", 10, 10, 10);
				}else{
					int fuelclick=FCMain.getInstance().getConfig().getInt("fuel-per-click",1);
					for(int i=0;i<fuelclick;i++){
						ItemStack pfuel=e.getPlayer().getInventory().getItemInMainHand();
						if(!pfuel.getType().equals(hc.getFuel())||PacketListener.fuel.get(hc)+hc.getFuelTime()>hc.getCapacity())break;
						pfuel.setAmount(pfuel.getAmount()-1);
						PacketListener.fuel.put(hc, PacketListener.fuel.get(hc)+hc.getFuelTime());
					}
				
				
				
			//e.getPlayer().getInventory().setItemInMainHand(pfuel);	
				FCMain.getZotLib().getPacketLibrary().getTitleManager().sendTitle(e.getPlayer(), ChatColor.GREEN+"Fuel added!", 5, 5, 5);
				}
			}
		});
		
	}
}
}
