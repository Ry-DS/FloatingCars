package com.SimplyBallistic.FloatingCars.commands;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.FloatingCars.HoverCar;
import com.SimplyBallistic.FloatingCars.files.CarYml;
import com.SimplyBallistic.FloatingCars.reflection.PacketListener;

public class Command_Spawn implements SubCommand {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// TODO Auto-generated method stub
		if(args.length==1){
			return CarYml.contents();
			
		}
		return null;
			
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
        if(sender instanceof Player){
			
			Player p=(Player)sender;
			
			List<Block> blocks=p.getLineOfSight((Set<Material>)null, 50);
			Location l=blocks.get(0).getLocation();
			for(Block b:blocks)if(!b.getType().equals(Material.AIR))l=b.getLocation();
			
			for(int i=0;i<FCMain.cars.size();){
				HoverCar hs=FCMain.cars.get(i);
				if(!hs.getCar().isDead()&&hs.getOwner().equals(p.getUniqueId())&&hs.getCarType().equals(args[0]))
					PacketListener.deleteCar(hs);
				
				else i++;
			}
			
			HoverCar car=CarYml.spawnCar(args[0], l, null);
			if(car==null){sender.sendMessage(ChatColor.GREEN+"That's not a valid car to spawn!");return true;}
			
			sender.sendMessage(ChatColor.GREEN+"Car Spawned!");
			return true;
			}
        else {sender.sendMessage(ChatColor.RED+"You need to be a Player!");}
        return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "spawn";
	}

}
