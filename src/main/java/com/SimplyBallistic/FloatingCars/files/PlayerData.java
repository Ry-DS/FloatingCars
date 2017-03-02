package com.SimplyBallistic.FloatingCars.files;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.util.tacoserialization.InventorySerialization;

public class PlayerData {
	private static YamlConfiguration config;
	private static File save;
	public PlayerData(){
		save=new File(FCMain.getInstance().getDataFolder(), "PlayerData.yml");
		config=YamlConfiguration.loadConfiguration(save);
		
	}
public static Inventory getPlayerInventory(UUID id,String car){
	if(id==null)
	return null;
if(!Bukkit.getOfflinePlayer(id).isOnline())return null;
if(!config.contains(id+".cars."+car))return null;
Inventory ret=Bukkit.createInventory(Bukkit.getPlayer(id), InventoryType.CHEST,ChatColor.GOLD+"Car Inventory: "+ChatColor.DARK_BLUE+car);

if(!config.contains(id+".cars."+car+".inventory")){
	setInventory(id, ret,car);
	return ret;
	}
ret.setContents(InventorySerialization.getInventory(config.getString(id+".cars."+car+".inventory"), 27));
	
	return ret;

}
public static void setInventory(UUID id, Inventory i,String car){
	if(!config.contains(id+".cars."+car))return;
	config.set(id+".cars."+car+".inventory", InventorySerialization.serializeInventoryAsString(i, true));
	try {
		config.save(save);
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	
}
public static boolean playerOwns(UUID id,String car){
	
	return config.contains(id+".cars."+car);
}
public static void giveCar(UUID id,String car){
	if(config.contains(id+".cars."+car))return;
	config.createSection(id+".cars."+car);
	try {
		config.save(save);
	} catch (IOException e) {
		e.printStackTrace();
	}
	
}
//TODO Public implementation if requester wants it
public static void setFuel(UUID id,String car,Integer amount){
	if(!config.contains(id+".cars."+car))return;
	if(id==null||amount==null)return;
	config.set(id+".cars."+car+".fuel",amount);
	try {
		config.save(save);
	} catch (IOException e) {
		e.printStackTrace();
	}
}
public static int getFuel(UUID id,String car){
	if (id==null)return -1;
	if(!config.contains(id+".cars."+car))return -1;
	if(!config.contains(id+".cars."+car+".fuel"))setFuel(id, car, 0);
	return config.getInt(id+".cars."+car+".fuel");
}

}
