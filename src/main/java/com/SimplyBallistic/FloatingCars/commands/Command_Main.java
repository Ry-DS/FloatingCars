package com.SimplyBallistic.FloatingCars.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.FloatingCars.files.CarYml;
import com.SimplyBallistic.FloatingCars.files.PlayerData;

public class Command_Main implements TabExecutor {

	SubCommand[] commands=new SubCommand[]{
			new Command_Give(),
			new Command_Killall(),
			new Command_Spawn(),
			new Command_Garage()
			
	};

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if(args.length==0){
			sender.sendMessage(ChatColor.GOLD+"Floating Cars: By BallisticBlaze");
			return true;}
		if(args[0].equals("reload")){
			new CarYml();
			new PlayerData();
			FCMain.getInstance().reloadConfig();
			sender.sendMessage(ChatColor.GREEN+"Reload Complete!");
			return true;
			
		}
		String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

		for(SubCommand subc:commands)
			if(subc.getName().equalsIgnoreCase(args[0]))
				if(sender.hasPermission(command.getPermission()+subc.getName()))
				return subc.onCommand(sender, command, label, commandArgs);
				else sender.sendMessage(command.getPermissionMessage());
			
			sender.sendMessage(ChatColor.RED+"That isn't a valid option!");
		
			
			
		
		
		
		
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// TODO Auto-generated method stub
		if(args.length==0)return null;
		String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
		List<String> ret=new ArrayList<>();
		
			for(SubCommand subc:commands){
				if(args.length==1){
					if(subc.getName().startsWith(args[0])&&sender.hasPermission(command.getPermission()+subc.getName()))
					ret.add(subc.getName());}
				else if(subc.getName().equalsIgnoreCase(args[0])){
					ret=subc.onTabComplete(sender, command, alias, commandArgs);
				break;	
				}
				
			}
			return ret;
		
		
	}
}
