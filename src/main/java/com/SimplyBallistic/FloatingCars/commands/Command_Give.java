package com.SimplyBallistic.FloatingCars.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.SimplyBallistic.FloatingCars.files.CarYml;
import com.SimplyBallistic.FloatingCars.files.PlayerData;

public class Command_Give implements SubCommand{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// TODO Auto-generated method stub
		List<String> ret=new ArrayList<>();
		if(args.length==1)
		for(Player p:Bukkit.getOnlinePlayers())ret.add(p.getName());
		if(args.length==2){ret.clear();ret.addAll(CarYml.contents());}
		return ret;
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if(args.length!=2){sender.sendMessage(ChatColor.RED+"You need to give a player and a car to give!");return true;}
		if(Bukkit.getPlayer(args[0])==null){sender.sendMessage("That isn't a player thats online!");return true;}
		if(!CarYml.contains(args[1])){sender.sendMessage(ChatColor.RED+"That isn't a valid car!");return true;}
		if(PlayerData.playerOwns(Bukkit.getPlayer(args[0]).getUniqueId(), args[1])){sender.sendMessage(ChatColor.RED+args[0]+" already owns that car!");return true;}
		PlayerData.giveCar(Bukkit.getPlayer(args[0]).getUniqueId(),args[1]);
		sender.sendMessage(ChatColor.GREEN+"Car sucessfully added to "+args[0]+"'s garage!");
		
		
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "give";
	}

	
}
