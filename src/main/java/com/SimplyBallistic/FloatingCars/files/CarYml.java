package com.SimplyBallistic.FloatingCars.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.SimplyBallistic.FloatingCars.FCMain;
import com.SimplyBallistic.FloatingCars.HoverCar;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
/**
 * Config for custom cars. Make new instance to reload config
 * @author ryan9
 *
 */
public class CarYml {
	private static YamlConfiguration config;
	public CarYml() {
		
		File f=new File(FCMain.getInstance().getDataFolder(), "Cars.yml");
		if(f.exists()){
		config=YamlConfiguration.loadConfiguration(f);
		FCMain.getInstance().getLogger().info("Already existant Cars yaml found! Loading that instead...");
		}
		else
			try {
				FCMain.getInstance().getLogger().info("Cars Yaml not found! Loading default...");
				Files.copy(FCMain.getInstance().getResource("Cars.yml"), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
				config=YamlConfiguration.loadConfiguration(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
	}
	/**
	 * Spawn car from config
	 * @param car the car type to find in config. 
	 * @param l Where to spawn the car
	 * @param owner The owner. Can be null to force public
	 * @return The hovercar created
	 */
	public static HoverCar spawnCar(String car,Location l,UUID owner){
		if(!config.contains(car))return null;
		ArmorStand as=(ArmorStand)l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
		HoverCar ret;
		ConfigurationSection carstats=config.getConfigurationSection(car);
		ret=new HoverCar() {
			
			@Override
			public String name() {

				return ChatColor.translateAlternateColorCodes('&', carstats.getString("name",ChatColor.RED+"ERROR! Invalid or no name given!")).replaceAll("%player%", Bukkit.getOfflinePlayer(owner).getName());
			}
			
			
			
			@Override
			public double getSpeed() {
				return carstats.getDouble("speed", 0.5);
			}
			
			
			
			@Override
			public Inventory getInventory() {
				if(!carstats.getBoolean("hasInv",false)||carstats.getBoolean("public"))
					return null;
				else return PlayerData.getPlayerInventory(getOwner(),getCarType());
				
			}
			
			@Override
			public int getFuelTime() {
				return carstats.getInt("fuel-time",100);
			}
			
			@Override
			public Material getFuel() {
				
				return Material.valueOf(carstats.getString("fuel","coal").toUpperCase());
			}
			
			@Override
			public ArmorStand getCar() {
				return as;
			}
			
			
			
			@Override
			public boolean canFly() {
				return carstats.getBoolean("flys",true);
			}



			@Override
			public UUID getOwner() {
				// TODO Auto-generated method stub
				if(carstats.getBoolean("public",false))
					return null;
				else return owner;
			}



			@Override
			public ItemStack getItem() {
				// TODO Auto-generated method stub
				@SuppressWarnings("deprecation")
				ItemStack iret=new ItemStack(Material.valueOf(carstats.getString("block", "potato_item").toUpperCase()),1,(short)0,(byte)carstats.getInt("block-data",0));
				
				if(iret.getType().isBlock()){
					ItemMeta meta=iret.getItemMeta();
					meta.setDisplayName(carstats.getString("item-name",ChatColor.RED+"ERROR! Invalid or no name given!"));
					meta.setLore(carstats.getStringList("lore"));
					iret.setItemMeta(meta);
					
					
					
					
					
					
				}else{
					ItemMeta meta=iret.getItemMeta();
					meta.setDisplayName(ChatColor.RED+"FATAL ERROR: Not a Block!");
					List<String>error=new ArrayList<>();
					error.add("The value you have under 'block' is not a block!");
					error.add("placing this >could< break the server! BE WARNED");
					meta.setLore(error);
					iret.setItemMeta(meta);
				}
				net.minecraft.server.v1_11_R1.ItemStack nmsitem=CraftItemStack.asNMSCopy(iret);
				NBTTagCompound nbt=nmsitem.getTag();
				nbt.setString("FCcar", getCarType());
				if(getOwner()!=null)
				nbt.setString("FCowner", getOwner().toString());
				nmsitem.setTag(nbt);
				iret=CraftItemStack.asCraftMirror(nmsitem);
				return iret;
			}



			@Override
			public double getSpaceSpeed() {
				// TODO Auto-generated method stub
				return carstats.getDouble("space-speed",1);
			}



			@Override
			public double getRevSpeed() {
				// TODO Auto-generated method stub
				return carstats.getDouble("rev-speed",0.2);
			}



			@Override
			public String getCarType() {
				// TODO Auto-generated method stub
				return car;
			}



			@Override
			public double getMaxHeight() {
				// TODO Auto-generated method stub
				return carstats.getDouble("max-height", 256);
			}
			@Override
			public double getShiftSpeed() {
				// TODO Auto-generated method stub
				return carstats.getDouble("shift-speed",1);
			}
			@Override
			public int getCapacity() {
				// TODO Auto-generated method stub
				return carstats.getInt("fuel-cap",1000);
			}
		};
		FCMain.cars.add(ret);
		as.setVisible(false);
		as.setHelmet(ret.getItem());
		as.setCustomName(ret.name());
		as.setCustomNameVisible(true);
		return ret;
	}
	public void saveConfig(){
		try {
			config.save(new File(FCMain.getInstance().getDataFolder(), "Cars.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	    }
	

}
