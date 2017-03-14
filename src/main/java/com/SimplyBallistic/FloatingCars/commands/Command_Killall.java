package com.SimplyBallistic.FloatingCars.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.SimplyBallistic.FloatingCars.files.LanguageYml;
import com.SimplyBallistic.FloatingCars.reflection.PacketListener;

public class Command_Killall implements SubCommand{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		PacketListener.deleteAll();
		sender.sendMessage(LanguageYml.getAndConv("killall", ChatColor.GREEN+"Removed all cars"));
		
		
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "killall";
	}

}
