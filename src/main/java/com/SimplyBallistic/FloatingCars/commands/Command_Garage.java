package com.SimplyBallistic.FloatingCars.commands;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.FloatingCars.files.CarYml;
import com.SimplyBallistic.FloatingCars.files.LanguageYml;
import com.SimplyBallistic.FloatingCars.files.PlayerData;
import com.SimplyBallistic.util.IconMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Command_Garage implements SubCommand {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if(!(sender instanceof Player)){sender.sendMessage("You need to be a Player!");return true;}
		Player p=(Player)sender;
		IconMenu menu=new IconMenu(ChatColor.GOLD+LanguageYml.getAndConv("garage-name", "Garage: "+ChatColor.YELLOW+p.getName()).replaceAll("%player%", p.getName()), 18, event->{
			
			event.setWillDestroy(true);
			event.setWillClose(true);
            CarYml.spawnCar(event.getName(), p.getLocation(), p.getUniqueId());
		}, FCMain.getInstance());
		int i=0;
		for(String s:PlayerData.playerOwnsList( p.getUniqueId() )){
			menu.setOption(i, CarYml.getItem(s, p.getUniqueId()), s, "");
			i++;
		}
		menu.open(p);
		return true;
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "garage";
	}

}
