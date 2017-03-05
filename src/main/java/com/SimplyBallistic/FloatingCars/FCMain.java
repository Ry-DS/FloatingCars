package com.SimplyBallistic.FloatingCars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.SimplyBallistic.FloatingCars.commands.Command_Main;
import com.SimplyBallistic.FloatingCars.files.CarYml;
import com.SimplyBallistic.FloatingCars.files.PlayerData;
import com.SimplyBallistic.FloatingCars.listeners.DamageListener;
import com.SimplyBallistic.FloatingCars.listeners.InteractListener;
import com.SimplyBallistic.FloatingCars.listeners.InventorySaver;
import com.SimplyBallistic.FloatingCars.listeners.RideListener;
import com.SimplyBallistic.FloatingCars.reflection.PacketListener;
import com.SimplyBallistic.util.StopWatch;

import XZot1K.plugins.zl.ZotLib;

public class FCMain extends JavaPlugin {
	//TODO
	/*
	Make option public cars dont need fuel, configurable
	Make public cars that despawn in a time, then respawn somewhere. Location set by command. need new config for that
	All da commands, killall, gui, config reload, give,
	 */
	private static FCMain instance;
	private static ZotLib zotLib;
	public static List<HoverCar> cars;
	public static Map<HoverCar,StopWatch> pcars;
	public static String prefix="["+ChatColor.GOLD+ChatColor.ITALIC+"SpaceCars"+ChatColor.RESET+"]";
	@Override
	public void onLoad() {
		instance=this;
		cars=new ArrayList<HoverCar>();
		pcars=new HashMap<>();
		new CarYml();
		new PlayerData();
	saveDefaultConfig();
	}
	@Override
	public void onEnable() {
		if(!isZotLibInstalled()){
			getLogger().severe("ZotLib not installed! Shutting down...");
		getServer().getPluginManager().disablePlugin(this);	
		}
	getServer().getPluginManager().registerEvents(new RideListener(), this);
	//getServer().getPluginManager().registerEvents(new DismountListener(), this);
	getServer().getPluginManager().registerEvents(new InventorySaver(), this);
	getServer().getPluginManager().registerEvents(new InteractListener(), this);
	getServer().getPluginManager().registerEvents(new DamageListener(), this);
	getCommand("spacecar").setExecutor(new Command_Main());
	//new HoverScheduler();
	new PacketListener(getInstance());
	
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
	 private boolean isZotLibInstalled()
	    {
	        ZotLib zotLib = (ZotLib) getServer().getPluginManager().getPlugin("ZotLib");
	        if(zotLib != null)
	        {
	            setZotLib(zotLib);
	            return true;
	        }
	        return false;
	    }

	    public static ZotLib getZotLib()
	    {
	        return zotLib;
	    }
	    private void setZotLib(ZotLib zotLib)
	    {
	        FCMain.zotLib = zotLib;
	    }
}
