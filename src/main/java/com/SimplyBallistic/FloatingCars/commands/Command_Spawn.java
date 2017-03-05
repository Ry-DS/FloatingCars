package com.SimplyBallistic.FloatingCars.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.SimplyBallistic.FloatingCars.HoverCar;
import com.SimplyBallistic.FloatingCars.files.CarYml;

public class Command_Spawn implements SubCommand {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length==1){
			List<String>ret=new ArrayList<>();
			for(String s:CarYml.contents())
				if(s.startsWith(args[0]))
			ret.add(s);
			return ret;
		}
		return null;
			
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if(args.length!=1){sender.sendMessage(ChatColor.RED+"Not enough/Too many args! Give a car name to spawn");return true;}
        if(sender instanceof Player){
			
			Player p=(Player)sender;
			
			List<Block> blocks=p.getLineOfSight((Set<Material>)null, 50);
			Location l=blocks.get(0).getLocation();
			for(Block b:blocks)if(!b.getType().equals(Material.AIR))l=b.getLocation();
			
			
			
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
