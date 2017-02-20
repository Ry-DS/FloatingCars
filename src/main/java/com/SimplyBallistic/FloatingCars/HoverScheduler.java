package com.SimplyBallistic.FloatingCars;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
/**
 * @deprecated
 * Now done in packet listener. Buggy and does not work well
 * @author ryan9
 *
 */
public class HoverScheduler extends BukkitRunnable {
    public static List<UUID> cars=new ArrayList<>();
    boolean hoverUp=true;
    //trying to use delta timer here. 
    int tickcounts=20;
    int elapsedTicks=0;
    public HoverScheduler() {
    	runTaskTimerAsynchronously(FCMain.getInstance(), 20, 5);
    }
	@Override
	
	public void run() {
		elapsedTicks+=5;
		
		for(int i=0;i<cars.size();i++){
			ArmorStand as=(ArmorStand) Bukkit.getEntity(cars.get(i));
			if(elapsedTicks<tickcounts)
				if(hoverUp)
			as.setVelocity(as.getVelocity().setY(0.005));
				else as.setVelocity(as.getVelocity().setY(-0.005));
			else{ hoverUp=!hoverUp; elapsedTicks=0;}
			
			
			
		}
		
		}
		
		
	public static Vector genForwUpVec(Location loc){
		return loc.getDirection().multiply(2);
		
	}	

}
