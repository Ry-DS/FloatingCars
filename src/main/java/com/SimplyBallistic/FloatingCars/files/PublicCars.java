package com.SimplyBallistic.FloatingCars.files;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.SimplyBallistic.FloatingCars.FCMain;

public class PublicCars {
	File save;
	YamlConfiguration config;
	public PublicCars() {
	save=new File(FCMain.getInstance().getDataFolder(), "PlayerData.yml");
	config=YamlConfiguration.loadConfiguration(save);
	
}
	
	public static void newSpawn(Location l,String car,int lastTime,int respawnTime){}
	
	
}
