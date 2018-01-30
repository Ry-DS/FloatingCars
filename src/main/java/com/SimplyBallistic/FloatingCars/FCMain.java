package com.SimplyBallistic.FloatingCars;

import com.SimplyBallistic.FloatingCars.commands.Command_Main;
import com.SimplyBallistic.FloatingCars.files.CarYml;
import com.SimplyBallistic.FloatingCars.files.LanguageYml;
import com.SimplyBallistic.FloatingCars.files.PlayerData;
import com.SimplyBallistic.FloatingCars.listeners.DamageListener;
import com.SimplyBallistic.FloatingCars.listeners.InteractListener;
import com.SimplyBallistic.FloatingCars.listeners.InventorySaver;
import com.SimplyBallistic.FloatingCars.listeners.RideListener;
import com.SimplyBallistic.FloatingCars.reflection.PacketListener;
import com.SimplyBallistic.util.StopWatch;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FCMain extends JavaPlugin {
	//TODO
	/*
	Make option public cars dont need fuel, configurable
	Make public cars that despawn in a time, then respawn somewhere. Location set by command. need new config for that
	All da commands, killall, gui, config reload, give,
	 */
	private static FCMain instance;
	public static List<HoverCar> cars;
	public static Map<HoverCar,StopWatch> pcars;
	public static final String PREFIX = "[" + ChatColor.GOLD + ChatColor.ITALIC + "SpaceCars" + ChatColor.RESET + "]";
	@Override
	public void onLoad() {
		
	}
	@Override
	public void onEnable() {
		instance=this;
		cars = new ArrayList<>();
		pcars=new HashMap<>();
		new CarYml();
		new PlayerData();
		new LanguageYml();
	saveDefaultConfig();
	getServer().getPluginManager().registerEvents(new RideListener(), this);
	//getServer().getPluginManager().registerEvents(new DismountListener(), this);
	getServer().getPluginManager().registerEvents(new InventorySaver(), this);
	getServer().getPluginManager().registerEvents(new InteractListener(), this);
	getServer().getPluginManager().registerEvents(new DamageListener(), this);
	getCommand("spacecar").setExecutor(new Command_Main());
	//new HoverScheduler();
	new PacketListener(getInstance());
		Bukkit.getScheduler().runTaskLater(this, () -> {
			getLogger().info("Started World Cleanup");
			int i = 0;
			for (World w : Bukkit.getWorlds())
				for (Entity en : w.getEntities()) {
					if (en instanceof ArmorStand) {
						ArmorStand as = (ArmorStand) en;
						if (!as.getHelmet().getType().toString().contains("HELMET")) {
							i++;
							as.remove();
						}
					}
				}
			getLogger().info("Cleaned Up " + i + " rogue cars!");
		}, 100);

	
	}
	@Override
	public void onDisable() {
//	cars.forEach((as)->as.getCar().remove());
//	PacketListener.fuel.forEach((hc,i)->{PlayerData.setFuel(hc.getOwner(), hc.getCarType(), i);});
		PacketListener.deleteAll();	
	
	}
	public static FCMain getInstance(){
		return instance;
	}
}
