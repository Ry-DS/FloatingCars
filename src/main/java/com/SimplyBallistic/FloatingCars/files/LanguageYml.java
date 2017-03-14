package com.SimplyBallistic.FloatingCars.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.SimplyBallistic.FloatingCars.FCMain;

public class LanguageYml {
	private static YamlConfiguration config;
	public LanguageYml() {
		
		File f=new File(FCMain.getInstance().getDataFolder(), "Language.yml");
		if(f.exists()){
		config=YamlConfiguration.loadConfiguration(f);
		FCMain.getInstance().getLogger().info("Already existant Lang yaml found! Loading that instead...");
		}
		else
			try {
				FCMain.getInstance().getLogger().info("Lang Yaml not found! Loading default...");
				Files.copy(FCMain.getInstance().getResource("Language.yml"), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
				config=YamlConfiguration.loadConfiguration(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
	}
	public static String getAndConv(String key,String def){
		return ChatColor.translateAlternateColorCodes('&', config.getString(key, def));
		
	}
}
