package com.SimplyBallistic.FloatingCars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.FloatingCars.HoverCar;
import com.SimplyBallistic.FloatingCars.files.PlayerData;
import com.SimplyBallistic.FloatingCars.reflection.PacketListener;


public class RideListener implements Listener {
@EventHandler
public void onRide(PlayerInteractAtEntityEvent e){
	for(HoverCar hc:FCMain.cars){
		if(hc.getCar().getUniqueId().equals(e.getRightClicked().getUniqueId())){
			if(hc.getOwner()!=null&&!e.getPlayer().getUniqueId().equals(hc.getOwner())){
				e.getPlayer().sendMessage(ChatColor.RED+"That's not your car!");
				e.setCancelled(true);
				return;
			}
			if(e.getPlayer().isSneaking()){
				if(hc.getInventory()!=null){
				e.getPlayer().openInventory(PlayerData.getPlayerInventory(e.getPlayer().getUniqueId(),hc.getCarType()));
				e.setCancelled(true);
				}else
					e.getPlayer().sendMessage(ChatColor.RED+"This car doesnt have an inventory!");
				
				return;
				
				
			}
			if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(hc.getFuel())){
				
				if(PacketListener.fuel.get(hc)!=null&&PacketListener.fuel.get(hc)+hc.getFuelTime()>hc.getCapacity()){
					e.getPlayer().sendMessage(ChatColor.RED+"You don't have enough space for any more fuel!");
				}else{
				if(PacketListener.fuel.get(hc)==null)PacketListener.fuel.put(hc, PlayerData.getFuel(e.getPlayer().getUniqueId(), hc.getCarType()));	
				PacketListener.fuel.put(hc, PacketListener.fuel.get(hc)+hc.getFuelTime());
				ItemStack pfuel=e.getPlayer().getInventory().getItemInMainHand();
				pfuel.setAmount(pfuel.getAmount()-1);
				
			//e.getPlayer().getInventory().setItemInMainHand(pfuel);	
				e.getPlayer().sendMessage(ChatColor.GREEN+"Fuel Added!");
				}
			}else
				
			e.getRightClicked().addPassenger(e.getPlayer());
			e.setCancelled(true);
		}
	}
	/*if(e.getRightClicked() instanceof ArmorStand){
		
		
		
		
		
		//e.getRightClicked().setGravity(false);
		ArmorStand as=(ArmorStand)e.getRightClicked();
		as.setVisible(false);
		@SuppressWarnings("deprecation")
		ItemStack egg=new ItemStack(Material.MONSTER_EGGS, 1, (short)Material.COBBLESTONE.getId(),(byte)2);//works 0-3
		
		
		//as.setArms(true);
		as.setHelmet(egg);
		//e.getPlayer().getLocation().getWorld().dropItemNaturally(e.getPlayer().getLocation(), egg);
		
		
	}*/
	
}
}
